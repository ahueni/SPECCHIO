package ch.specchio.file.reader.spectrum;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ch.specchio.types.MetaDate;
import ch.specchio.types.SpectralFile;

public class MFR_FileLoader extends SpectralFileLoader {
	
	int no_of_channels = 7; // could read this from sensor definition in database!!!
	
	public MFR_FileLoader()
	{
		super("MFR Out File");
	}


	public SpectralFile load(File file) throws IOException
	{
		SpectralFile sf = new SpectralFile();
		
		sf.setPath(file.getAbsolutePath());		
		sf.setFilename(file.getName());
		sf.setFileFormatName(this.file_format_name);
		
		sf.setInstrumentTypeNumber(7); // MFR 7
		
		
		file_input = new FileInputStream (file);			
				
		data_in = new DataInputStream(file_input);
		
		read_MFR_file(data_in, sf);
		

		
		data_in.close ();
		
		return sf;
	}
	
	public void read_MFR_file(DataInputStream in, SpectralFile f) throws IOException
	{
		String line;
		boolean hdr_ended = false;
		
		// use buffered stream to read lines
		BufferedReader d = new BufferedReader(new InputStreamReader(in));
		
		// read line by line
		while(!hdr_ended && (line=d.readLine()) != null)
		{
			// tokenise the line
			String[] tokens = line.split(" ");
			
			// analyse the tokens
			hdr_ended = analyse_MFR_file(tokens, d, f);						
		}		
		
		// read the measurements
		f.setMeasurements(read_data(data_in, d, f));
		
		f.addNumberOfChannels(no_of_channels);
		
		// create the spectra_names from capture_time
		//f.spectra_filenames = new String[f.no_of_spectra()];
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
//		// get UTC timezone
//		TimeZone tz = TimeZone.getTimeZone("UTC");
//		formatter.setTimeZone(tz);
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern(MetaDate.DEFAULT_DATE_FORMAT).withZoneUTC();
		
		
		for(int i = 0; i < f.getNumberOfSpectra(); i++)
		{
	
			f.addSpectrumFilename(fmt.print(f.getCaptureDate(i)));
		}
		
		
	}
	
	public boolean analyse_MFR_file(String[] tokens, BufferedReader in, SpectralFile hdr)
	{
		String t1 = tokens[0];
		boolean hdr_ended = false;
		
		if(t1.equals("PRODUCES"))
		{
			hdr.setCompany("YES");
		}
		
		if(t1.equals("MFRSR"))
		{
			// skip: this is channel data
		}
		
		if(t1.equals(""))
		{
			hdr_ended = true;
		}
		
		
		return hdr_ended;
	}
	
	Float[][] read_data(DataInputStream in, BufferedReader d, SpectralFile sf) throws IOException
	{
		int line_cnt = 0;
		String line;
		ArrayList<Float> total, diffuse; 
		ArrayList<Double> julian_days;
		
		total = new ArrayList<Float>();
		diffuse = new ArrayList<Float>();
		julian_days = new ArrayList<Double>();
				
		// read line by line
		while((line=d.readLine()) != null)
		{			
			// remove all spaces
			line = line.replace(" ", "");
			
			
			// tokenise the line
			String[] tokens = line.split("\t"); // values are separated by two spaces
			
			// first token is the julian day
			julian_days.add(Double.valueOf(tokens[0]));
			
			// second token is the cosine -> skip
			
			
			// third to ninth token is total irradiance (7 channels)
			int offset = 2;
			for(int i = 0; i < no_of_channels; i++)
			{
				total.add(Float.valueOf(tokens[offset + i]));
			}
			
			// 10th to 16th token is diffuse irradiance
			offset = 9;
			for(int i = 0; i < no_of_channels; i++)
			{
				diffuse.add(Float.valueOf(tokens[offset + i]));
			}
			
			// 17th to 23rd token is direct irradiance -> skip
			

			line_cnt++;
			
		}
		
		
		// array to store the total and diffuse irradiance
		// total and diffuse channels are written alternating
		// i.e. e.g. f[0] -> total channels of first measurement
		//           f[1] -> diffuse channels of first measurement
		Float[][] f = new Float[line_cnt * 2][no_of_channels];
		
		sf.setNumberOfSpectra(line_cnt * 2);
		
		ListIterator<Float> tot_li = total.listIterator();
		ListIterator<Float> dif_li = diffuse.listIterator();
		int channel = 0;
		int measurement_cnt = 0;
		while(tot_li.hasNext() && dif_li.hasNext())
		{
			f[measurement_cnt][channel] = tot_li.next();
			f[measurement_cnt+1][channel++] = dif_li.next();
			// set measurement unit (always Radiance with the MFR)
			
			sf.addMeasurementUnits(2);
			sf.addMeasurementUnits(2);
			
			if(channel == no_of_channels)
			{
				channel = 0;
				measurement_cnt += 2;
			}
		}
		
		// calculate dates from Julian days
		// the dates are also alternating: total, diffuse, total, diffuse, etc
		sf.setCaptureDates(new DateTime[line_cnt * 2]);
		
		int date_cnt = 0;
		ListIterator<Double> days = julian_days.listIterator();
		while(days.hasNext())
		{
			double julian_date_time = days.next();
			long julian_day = (int)julian_date_time;
			//double j_day = (double)julian_day;
			double day_time = julian_date_time - julian_day;
			
			julian_day--; // subtract one because due to the calculation in YESDAS, normal julian day
							// calculations are different by one day (see YESDAS manual)

			// constants
			int MILLIS_PER_SEC = 1000;
			int SECS_PER_MIN = 60;
			int MINS_PER_HOUR = 60;
			int HOURS_PER_DAY = 24;
			int MILLIS_PER_DAY = MILLIS_PER_SEC * SECS_PER_MIN * MINS_PER_HOUR* HOURS_PER_DAY;
			
			// julian day in milliseconds
			long jd_mil = julian_day * MILLIS_PER_DAY;
			
			// time in seconds:
			// 1 day = 24*3600 seconds
			long seconds = (int) (day_time * 24*3600);
			long time_mil = seconds * MILLIS_PER_SEC;
			
			// the day count starts on January, 1. 1900
			// get the milliseconds for this starting date
			TimeZone tz = TimeZone.getTimeZone("UTC");
			Calendar cal = Calendar.getInstance(tz);
			cal.set(1900, Calendar.JANUARY, 1, 0,0,0);
			long start_time_mil = cal.getTimeInMillis();
			//start_time_mil -= MILLIS_PER_DAY/2;
			
			//Date start_date = cal.getTime();

			//SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			//formatter.setTimeZone(tz);

			
			//System.out.println("  Capture time : " + formatter.format(cal.getTime()));
			
			//cal.setTimeInMillis(jd_mil);
			//Date jd_date = cal.getTime();
			
			// sum up all the millis to get the real date
//			cal.setTimeInMillis(jd_mil + time_mil + start_time_mil);
			
			
			
//			Date day = cal.getTime();
			//System.out.println("  Capture time : " + formatter.format(cal.getTime()));	
			
			DateTime dt = new DateTime(jd_mil + time_mil + start_time_mil, DateTimeZone.UTC);
			
			sf.setCaptureDate(date_cnt++, dt); // entry for total irradiance
			sf.setCaptureDate(date_cnt++, dt); // entry for diffuse irradiance
			
		}
		
		
		return f;
	
	}

}
