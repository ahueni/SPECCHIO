

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

public class ModtranAlbedoFileLoader extends SpectralFileLoader {
	
	boolean not_eof = true;
	boolean spectral_interpolation = false;
	
	public ModtranAlbedoFileLoader()
	{
		super("MODTRAN_Albedo_File");
	}
	
	
	public SpectralFile load(File file) throws IOException
	{
		SpectralFile f = new SpectralFile();
		
		f.setFileFormatName(this.file_format_name);
		
		// read file first to know how many spectra there are
		file_input = new FileInputStream (file);			
		
		data_in = new DataInputStream(file_input);
		
		
		BufferedReader d = new BufferedReader(new InputStreamReader(data_in));		
		
		f.setMeasurements(read_data(d, f));
		
		
		
		f.setPath(file.getAbsolutePath());		
		f.setFilename(file.getName());		
		
		f.setCompany("MODTRAN");
	
		
		data_in.close ();
		
		

		
		return f;
	}
	


	Float[][] read_data(BufferedReader d, SpectralFile f)
	{
		// first column: wvl
		// second column: channel b
		// third column: channel a

		spectral_interpolation = false;
		
		ArrayList<Float> values = null;
		
		ArrayList<ArrayList<Float>> all_spectra = new ArrayList<ArrayList<Float>>();

		ArrayList<Float> wvl = new ArrayList<Float>();
		String line;
		
		try {
			
			// skip header
			line=d.readLine();
			
			while (line.charAt(0) == '!')
			{
				line=d.readLine();
			}
			
			boolean start_of_spectrum = true;
			
			// read all lines
			
			while(line != null && line.length() > 0)
			{
				
				if (line.charAt(0) == '!')
				{
					line=d.readLine();
					start_of_spectrum = true;
				}
				
				
				if (start_of_spectrum)
				{
					// get the spectrum name out of it
					String[] tokens = line.split(" ");
					
					String name = tokens[tokens.length - 1];
					
					f.addSpectrumFilename(name);
					
					f.addMeasurementUnits(1);
					
					start_of_spectrum = false;
					
					if (values != null) all_spectra.add(values); // values are null on first entry
					
					values = new ArrayList<Float>();
					
					
				}
				else
				{
					String[] tokens = line.split(" ");
					
					// get wvls
					int index = 0;
					while(tokens[index].equals(""))
					{
						index++;
					}
					
					Float wvl_ = Float.valueOf(tokens[index++]);
					
					if (all_spectra.size()==0) // we are still reading the first spectrum and therefore we store the wvls
					{
						wvl.add(wvl_);
					}
					
					while(tokens[index].equals(""))
					{
						index++;
					}
					
					Float value = Float.valueOf(tokens[index]);
					
					values.add(value);
					
				}
				

				
				line=d.readLine();

			}

			all_spectra.add(values); // add the last read values to the list 
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		f.addNumberOfChannels(wvl.size());
		
		// fill wvls
		f.addWvls(new Float[wvl.size()]);
		Float[] tmp = new Float[wvl.size()];
		wvl.toArray(tmp);
		f.setWvls(0, tmp);		// before wvls was an arraylist: wvl.toArray(f.wvls);
		

		
		Float[][] out_spectrum = new Float[all_spectra.size()][f.getNumberOfChannels(0)];

//		ListIterator<Float> a_li = a.listIterator();
//
//		int i = 0;
//		while(a_li.hasNext())
//		{
//
//			out_spectrum[1][i++] = (Float)a_li.next(); // channel a
//		}	
		
		f.setNumberOfSpectra(all_spectra.size());
		
		ListIterator<ArrayList<Float>> li = all_spectra.listIterator();
		int cnt = 0;
		
		while(li.hasNext())
		{
			ArrayList<Float> tmp_list = li.next();
			
			tmp = new Float[wvl.size()];
			tmp_list.toArray(tmp);
			
			//out_spectrum[cnt] = tmp;
			System.arraycopy(tmp, 0, out_spectrum[cnt], 0, wvl.size());

			
			cnt++;
		}
		
		
		return out_spectrum;
		
	}
	
	
}
