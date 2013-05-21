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
import java.util.TimeZone;
import java.text.DateFormatSymbols;

import ch.specchio.types.SpectralFile;

public class OO_FileLoader extends SpectralFileLoader {
	
	SpectralFile spec_file;

	public OO_FileLoader() {
		super("OOSpectraSuite");
	}

	public SpectralFile load(File file) throws IOException
	{
		spec_file = new SpectralFile();
		spec_file.setNumberOfSpectra(1); // is that always correct????
		
		spec_file.setPath(file.getAbsolutePath());		
		spec_file.setFilename(file.getName());
		spec_file.setFileFormatName(this.file_format_name);
		
		// spectrum number is contained in the extension
//		spec_file.spectra_numbers[0] = Integer.valueOf(spec_file.base_name.substring(spec_file.base_name.length()-4));
//		spec_file.spectra_numbers[1] = spec_file.spectra_numbers[0];
//		spec_file.spectra_numbers[2] = spec_file.spectra_numbers[0];
						
		spec_file.addSpectrumFilename(spec_file.getFilename()); // target name
//		spec_file.spectra_filenames[1] = spec_file.filename(); // reference name
//		spec_file.spectra_filenames[2] = spec_file.filename(); // reflectance name
//
//		
		spec_file.addMeasurementUnits(2); // radiance as default
//		spec_file.measurement_units[1] = 2;
//		spec_file.measurement_units[2] = 1;
		
//		spec_file.capture_dates = new Date[spec_file.no_of_spectra()]; 
		
		file_input = new FileInputStream (file);			
				
		data_in = new DataInputStream(file_input);
		
		read_OO_file(data_in, spec_file);
		
		
		
		data_in.close ();
		
		return spec_file;
	}
	
	public void read_OO_file(DataInputStream in, SpectralFile f) throws IOException
	{
		String line;
		boolean hdr_ended = false;
		
		// use buffered stream to read lines
		BufferedReader d = new BufferedReader(new InputStreamReader(in));
		
		// read line by line
		while(!hdr_ended && (line=d.readLine()) != null)
		{
			// tokenise the line
			String[] tokens = line.split(": ");
			
			// analyse the tokens
			hdr_ended = analyse_OO_file(tokens, d, f);						
		}		
		
		
		line=d.readLine(); // read "Wavelength(nm); radiance(W*m-2*sr-1*nm-1)"
		
		// read the measurements
		f.setMeasurements(read_data(data_in, d));
		
		f.addNumberOfChannels(f.getMeasurement(0).length);
		
	}
	
	public boolean analyse_OO_file(String[] tokens, BufferedReader in, SpectralFile hdr)
	{
		String t1 = tokens[0];
		boolean hdr_ended = false;
		
		// remove tab from token 0 if required
		t1 = t1.replace("\t", "");
		
		
		if(t1.equals("SpectraSuite Data File"))
		{
			hdr.setCompany("OceanOptics");
		}
		
		if(t1.equals("Spectrometers"))
		{
			tokens[1] = tokens[1].replace("\t", "");
			tokens[1] = tokens[1].replace(" ", ""); // remove spaces from token 2		
			hdr.setInstrumentTypeNumber(4); //tokens[1].substring(0, 3);
			hdr.setInstrumentNumber(tokens[1].substring(4));			
		}
		
//		Date: Tue Apr 06 14:03:39 CEST 2010

		
		if(t1.equals("Date"))
		{
			tokens[1] = tokens[1].replace("\t", "");
			String[] time_data = tokens[1].split(" ");
			
			DateFormatSymbols dfs = new DateFormatSymbols();
			String[] months = dfs.getMonths();	
			
			int month = 0;
			
			for (int i=0; i < 12; i = i + 1)
			{
				//String short_month = months[i].substring(0, 3);
				if (time_data[1].equals(months[i].substring(0, 3)))
				{
					month = i;
					break;
				}
				
			}
			
			int year = Integer.valueOf(time_data[5]);
			
			String[] time = time_data[3].split(":");
			

			TimeZone tz = TimeZone.getTimeZone("UTC");
			Calendar cal = Calendar.getInstance(tz);
			cal.set(year, month, 
					Integer.valueOf(time_data[2]), Integer.valueOf(time[0]), 
					Integer.valueOf(time[1]), Integer.valueOf(time[2]));

			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			formatter.setTimeZone(tz);
			
			hdr.setCaptureDate(cal.getTime());
			hdr.setCaptureDate(0, hdr.getCaptureDate());


		}

		
		if(t1.equals(">>>>>Begin Processed Spectral Data<<<<<"))
		{
			hdr_ended = true;
		}
		
		
		return hdr_ended;
		
	}


	Float[][] read_data(DataInputStream in, BufferedReader d) throws IOException
	//Float[] read_data(DataInputStream in, BufferedReader d) throws IOException
	{
		String line;
		ArrayList<Float> target, wvls;
		
		wvls = new ArrayList<Float>();
		target = new ArrayList<Float>();

				
		// read line by line
		while((line=d.readLine()) != null && !line.equals(">>>>>End Processed Spectral Data<<<<<"))
		{
			
			// GER specific treatment: needed for the HR????
			// There are some strange cases where radiance values can be negative (of the last channel)
			// the minus signs are inserted into the second space that is separating the values
			// In order to have the values still being separated by two spaces, a replace must be done:
			//line = line.replaceAll("-", " -");
			
			// tokenise the line
			String[] tokens = line.split(";"); // values are separated by a tab
			
			// first token is wavelength
			wvls.add(Float.valueOf(tokens[0]));
			
			// second token is radiance of target
			if (tokens[1].equals("#N/D"))
			{
				target.add(0.0F);
			}
			else
			{
			
			
				target.add(Float.valueOf(tokens[1]));
			}
				
			
		}
		
		
		
		Float[][] f = new Float[1][target.size()];
		spec_file.addWvls(new Float[target.size()]);
		

		spec_file.setWvls(0,wvls.toArray(spec_file.getWvls(0)));
		
		f[0] = target.toArray(f[0]);

		
		return f;
		
		
		
	}

}
