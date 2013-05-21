package ch.specchio.file.reader.spectrum;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.ListIterator;

import ch.specchio.types.SpectralFile;

public class TXT_FileLoader extends SpectralFileLoader {
	
	boolean not_eof = true;
	
	public TXT_FileLoader()
	{
		super("TXT File");
	}
	
	
	public SpectralFile load(File file) throws IOException
	{
		SpectralFile f = new SpectralFile();
		//f.set_no_of_spectra(2); 
		
		f.setPath(file.getAbsolutePath());		
		f.setFilename(file.getName());
		f.setFileFormatName(this.file_format_name);
		
		// spectrum number is contained in the extension
		//f.spectra_numbers[0] = Integer.valueOf(f.ext);
		//f.spectra_numbers[1] = Integer.valueOf(f.ext);
		
		//f.spectra_filenames[0] = f.filename(); // target name
		//f.spectra_filenames[1] = f.filename(); // reference name
		
		f.setCompany("Unknown");
		
		//f.measurement_unit = 2;
		
		//f.capture_dates = new Date[f.no_of_spectra()]; 
		
		file_input = new FileInputStream (file);			
		
		data_in = new DataInputStream(file_input);
		
		
		BufferedReader d = new BufferedReader(new InputStreamReader(data_in));
		
		// analyse header to get no of spectra
		read_header(d, f, true);
		d.close();
		
		file_input = new FileInputStream (file);				
		data_in = new DataInputStream(file_input);
		d = new BufferedReader(new InputStreamReader(data_in));
		
		// read the whole file
		read_file(d, f);
		
		
		
		data_in.close ();
		file_input.close();
		
		return f;
	}
	
	// reads the header line in order to get the number of spectra contained in this file
	// or stores the spectra names (depending on the analyse flag)

	public void read_header(BufferedReader d, SpectralFile f, boolean analyse) throws IOException
	{
		int spec_cnt = 0;
		
		// mark start of file
		//d.mark(10000); // hope this read buffer is enough ....
		
		String sp_name;
		
		whitespace(d);
		
		// wvl
		char c = (char) d.read();
		c = (char) d.read();
		c = (char) d.read();
				
		whitespace(d);
		
		sp_name = spectral_name_or_value(d);
		
		while(!sp_name.equals(""))
		{
			// cnt the spectra
			spec_cnt++;
			
			if(analyse == false)
			{
				// the number of spectra is already know, ie. structures are setup
				// now the names can be stored
				f.addSpectrumFilename(sp_name);
				f.addSpectrumFilename(sp_name); // pseudo filenames are taken from header
			}
			
			// overread spaces and read next name
			whitespace(d);			
			sp_name = spectral_name_or_value(d);			
			
		}
		
		if(analyse)
			f.setNumberOfSpectra(spec_cnt);
		
		// read new line character
		c = (char) d.read();
		
		// move read position back to start
		//d.reset();
	}
	
	
	public String spectral_name_or_value(BufferedReader d) throws IOException
	{
		String name = "";
		
		char c = (char) d.read();
		

		while(c != ' ' && c != '\n' && c != '\t'  && c != '\r' && not_eof)
		{
			// add to string
			name = name + c;
			
			d.mark(10);
			int val = d.read();
			if (val == -1) // deal with the fact that excel files end right after the last character
			{
				not_eof = false;
			}
			else
			{
				c = (char) val;	
			}
			
		}
		
		d.reset(); // back to last non-name character
		
		return name;
	}
	
	
	public void read_file(BufferedReader d, SpectralFile f) throws IOException
	{

		this.read_header(d, f, false);
		
		// read the measurements
		f.setMeasurements(read_data(d, f));

	}
	

	
	Float[][] read_data(BufferedReader d, SpectralFile f)
	{
		int spec_cnt = 0;
		
		String value = "";
		ArrayList<Float>[] spectra = new ArrayList[f.getNumberOfSpectra()];
		ArrayList<Float> wvl = new ArrayList<Float>();
		
		// init array
		for(int i = 0; i < f.getNumberOfSpectra();i++)
		{
			spectra[i] = new ArrayList<Float>();
		}
		
	
		try {
			
			// read all lines
			while(not_eof)
			{
				
				whitespace(d);
				
				// wavelength
				value = spectral_name_or_value(d);
				if(!value.equals("")){
					wvl.add(Float.valueOf(value));
				} else {
					wvl.add(Float.valueOf(0));
				}
				
				// read the values from the current line				
				do
				{
					whitespace(d);
					
					value = spectral_name_or_value(d);
					
					if(!value.equals(""))
					{
						// 
						// store value in list
						spectra[spec_cnt++].add(Float.valueOf(value));
						//System.out.println(value);
					}
					else
					{
						d.read(); // read new line
						//System.out.println(c);
						
						d.mark(1);
						if(d.read() == -1)
						{
							not_eof = false;
						}
						d.reset();
					}

					
				} while(!value.equals("") && not_eof);
				
				
				spec_cnt = 0;
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Float[][] fl = new Float[f.getNumberOfSpectra()][spectra[0].size()];
		
		
		// for each list entry  fill value vectors
		for(int spec = 0; spec < f.getNumberOfSpectra(); spec++)
		{
			ListIterator li = spectra[spec].listIterator();
			int i = 0;
			while(li.hasNext())
			{
				fl[spec][i++] = (Float)li.next();
			}		
			
			// fill channel info
			f.addNumberOfChannels(spectra[0].size());			
		}
		
		// fill wvls
		f.addWvls(new Float[wvl.size()]);
		f.setWvls(0, wvl.toArray(f.getWvls(0)));
	

		
		return fl;
		
		
		
	}
	
	
}
