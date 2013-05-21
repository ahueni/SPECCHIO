/* SPECCHIO Project
 * (c) RSL 2006-2011
 * written by ahueni 
 */

package ch.specchio.file.reader.spectrum;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ch.specchio.types.MetaParameter;
import ch.specchio.types.Metadata;
import ch.specchio.types.SpectralFile;
import ch.specchio.types.spatial_pos;

public class ASD_FileLoader extends SpectralFileLoader {
	
	public SpectralFile asd_file;
	Metadata smd = new Metadata();
	
	public ASD_FileLoader()
	{
		super("ASD Binary");
		
		asd_file = new SpectralFile(); // requires this when determining the file type by reading the header
		asd_file.setNumberOfSpectra(1);	
	}

	public SpectralFile load(File file) throws IOException
	{		
		smd = new Metadata();
		asd_file = new SpectralFile();
		asd_file.setNumberOfSpectra(1);		
		
		asd_file.setPath(file.getAbsolutePath());		
		asd_file.setFilename(file.getName());
		asd_file.setFileFormatName(this.file_format_name);
		
		// spectrum number is contained in the extension for normal ASD files
		try {			
			MetaParameter mp = MetaParameter.newInstance(this.attributes_name_hash.get("Spectrum Number"));
			mp.setValue(Integer.valueOf(asd_file.getExt()), "RAW");
			smd.add_entry(mp);				
			
		} catch (NumberFormatException e) {
			// exception: must be a system calibration file
//			asd_file.spectra_numbers[0] = 0;
		}	
						
		asd_file.addSpectrumFilename(asd_file.getFilename());
		
		file_input = new FileInputStream (file);			
				
		data_in = new DataInputStream(file_input);
		
		
		
		read_ASD_header(data_in, asd_file);
		
		// store in first row as there is only one spectrum in ASD files
		asd_file.setMeasurements(read_data(data_in, asd_file.getNumberOfChannels(0)));
		
		data_in.close ();
		
		return asd_file;
	}
	
	public void read_ASD_header(DataInputStream in, SpectralFile hdr) throws IOException
	{
		
		// read the company name
		hdr.setCompany(read_string(in, 3));
		
		// comments
		hdr.setComment(read_string(in, 157));
		Character first_char = hdr.getComment().charAt(0);

		 int i = 0;
		 
		 while(i < hdr.getComment().length() && hdr.getComment().charAt(i) != 0) i++; // cut string at first zero		 
		 
		 hdr.setComment(hdr.getComment().substring(0, i));

		if(first_char == 0)
		{
			hdr.setComment("");
		}
		
		// date
		hdr.setCaptureDate(0, read_asd_time(in));
		
		// skip till dc_corr
		skip(in, 4);
		
		// read dc_time: Time of last dc, seconds since 1/1/1970
		// -> getting the delta time appears not possible, due to this timestamp being in UTC and the recording time being in local time!!!!!
		
		this.read_long(in);	// long dc_time = 
//		TimeZone tz = TimeZone.getTimeZone("UTC");
//		Calendar cal = Calendar.getInstance(tz);
//		cal.setTimeInMillis(dc_time*1000);
//		//cal.setTimeInMillis(0);
//
//		//SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmm");
//		//formatter.setTimeZone(tz);
//		
//		//String out=formatter.format(cal.getTime());
//		
//		//String timezone = tz.getDisplayName();
//		
//		tz = TimeZone.getDefault();
//		long offset = tz.getOffset(cal.getTimeInMillis());
//		
//		tz = TimeZone.getTimeZone("UTC");
//		
//		cal.setTime(hdr.capture_dates[0]);
//		//String out2=formatter.format(cal.getTime());
//		
//		long measurement_time =  cal.getTimeInMillis();
//		
//		int delta_seconds = (int) (measurement_time - (dc_time - offset/1000))/1000;
//		//hdr.instr_set.add_setting("Time_since_last_dc", delta_seconds);
//
////		MetaParameter mp = new MetaParameter("Instrument Settings", "");
////		mp.setAttribute_name("Time since last DC");
////		mp.setValue(delta_seconds, "s");
////		smd.add_entry(mp);		
		
		
		// read data type
		hdr.addMeasurementUnits((int) in.readByte());
		
		// skip till channels
		skip(in, 17);
		
		// get number of channels
		hdr.addNumberOfChannels(read_short(in));
		
		// skip till GPS data
		skip(in, 128);
		
		// read GPS data
		hdr.addPos(read_gps_data(in));
		
		// integration time in ms
		MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Integration Time"));
		mp.setValue( this.read_int(in), "ms");
		smd.add_entry(mp);				
		
		// read foreoptic
		hdr.setForeopticDegrees(read_short(in));
		
		if(hdr.getForeopticDegrees() == 0)
			hdr.setForeopticDegrees(25); // default for the bare fibre
		
		// skip dark current correction value
		skip(in, 2);
		
		// read calibration series number
		hdr.setCalibrationSeries(read_short(in));
		
		// read instrument number
		hdr.setInstrumentNumber(read_short(in).toString());
		
		// skip to sample count
		skip(in, 27);
		
		// read number of samples in the average
		//hdr.internal_average_cnt = read_short(in);
		mp = MetaParameter.newInstance(attributes_name_hash.get("Number of internal Scans"));
		mp.setValue(read_short(in), "RAW");		
		smd.add_entry(mp);	
		
		// read instrument type
		hdr.setInstrumentTypeNumber(in.readByte());
		
		skip(in, 4); // cal bulb no

		mp = MetaParameter.newInstance(attributes_name_hash.get("Gain_SWIR1"));
		mp.setValue( this.read_short(in), "RAW");
		smd.add_entry(mp);			
		
		mp = MetaParameter.newInstance(attributes_name_hash.get("Gain_SWIR2"));
		mp.setValue( this.read_short(in), "RAW");
		smd.add_entry(mp);	
		
		mp = MetaParameter.newInstance(attributes_name_hash.get("Offset_SWIR1"));
		mp.setValue( this.read_short(in), "RAW");
		smd.add_entry(mp);		
		
		mp = MetaParameter.newInstance(attributes_name_hash.get("Offset_SWIR2"));
		mp.setValue( this.read_short(in), "RAW");
		smd.add_entry(mp);		
		
		hdr.addEavMetadata(smd);
		
		// skip to end of header
		skip(in, 40);
		
	}
	
	Float[][] read_data(DataInputStream in, int channels) throws IOException
	{
		Float[][] f = new Float[1][channels];		
		for(int i=0;i < channels;i++)
		{
			f[0][i] = read_float(in);
		}	
		return f;
	}
	
	Date read_asd_time(DataInputStream in) throws IOException
	{
		TimeZone tz = TimeZone.getTimeZone("UTC");
		//TimeZone tz = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance(tz);
		
		Integer sec = read_short(in);
		Integer min = read_short(in);
		Integer hour = read_short(in);
		Integer mday = read_short(in);
		Integer month = read_short(in); // months start at 0 in the ASD structure
		Integer year = read_short(in) + 1900;
		
		// skip three shorts
		skip(in, 6);
		
		// month starts at 0: this conforms with the java calendar class!
		cal.set(year, month, mday, hour, min, sec);
		

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmm");
		formatter.setTimeZone(tz);
		
		//String out=formatter.format(cal.getTime());		
						
		return cal.getTime();
	}
	
	spatial_pos read_gps_data(DataInputStream in) throws IOException
	{
		spatial_pos pos = null;
		
		// skip true heading and speed
		skip(in, 16);
		
		double lat = read_double(in);
		double lon = read_double(in);
		double alt = read_double(in);
		
//		// reformat to dd.mmmmmmmmm
//		int lat_deg = (int)lat/100;
//		double lat_min = (lat - lat_deg*100)/60;
//		
//		int lon_deg = (int)lon/100;
//		double lon_min = (lon - lon_deg*100)/60;
		
		// only create position record if the position is not zero
		if(lat != 0 && lon != 0 && alt != 0)
		{
			pos = new spatial_pos();
//			pos.latitude = lat_deg + lat_min;
//			pos.longitude = lon_deg + lon_min;
//			pos.altitude = alt;			
			pos.latitude = asd_file.DDDmm2DDDdecimals(lat);
			pos.longitude = asd_file.DDDmm2DDDdecimals(lon);
			pos.altitude = alt;						
		}
		
		
		// skip rest
		skip(in, 16);
				
		return pos;
	}
	
	
}



