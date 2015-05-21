package ch.specchio.file.reader.spectrum;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ch.specchio.types.SpectralFile;


public class ENVI_SLB_FileLoader extends SpectralFileLoader {
	
	SpectralFile envi_file;
	Character c = new Character(' ');
	
	ArrayList<Float> wvls = new ArrayList<Float>();
	
	public ENVI_SLB_FileLoader()
	{
		super("ENVI SLB");
	}
	
	public SpectralFile load(File file) throws IOException
	{
		envi_file = new SpectralFile();
		
		envi_file.setSpectrumNameType("ENVI Hdr");
		
		envi_file.setFilename(file.getName());
		envi_file.setFileFormatName(this.file_format_name);
		
		// we take only slb's and sli's as input, header files
		// will be read within this routine by constructing the header file name
		if(envi_file.getExt().equals("slb") || envi_file.getExt().equals("sli"))
		{			
			envi_file.setPath(file.getAbsolutePath());		
			
			// expected header path name
			String hdr_pathname = envi_file.getPath().substring(0, 
					envi_file.getPath().length() - envi_file.getExt().length()) + "hdr";

			// new file object for the header
			File hdr = new File(hdr_pathname);
			file_input = new FileInputStream (hdr);						
			data_in = new DataInputStream(file_input);
			
			read_ENVI_header(data_in, envi_file);
			
			file_input.close();
			data_in.close ();
			
			

			if(envi_file.getNumberOfSpectraNames() == envi_file.getNumberOfSpectra())
			{
				for(int i=0;i < envi_file.getNumberOfSpectra(); i++)
				{
					envi_file.addSpectrumFilename(envi_file.getSpectrumName(i));					
				}	
			}
			else  // clean up at some point ....
			{
			
				
				// construct spectra names from file name if
				// there is more than one spectrum in the file
				if(envi_file.getNumberOfSpectra() > 1)
				{
					for(int i=0;i < envi_file.getNumberOfSpectra(); i++)
					{
						envi_file.addSpectrumFilename(envi_file.getBasename() + "_" + Integer.toString(i));						
//						envi_file.spectra_numbers[i] = i+1; // simple auto numbering						
					}
					
				}
				else // use the body name as spectrum name
				{
					envi_file.addSpectrumFilename(envi_file.getBasename() + envi_file.getExt());		
//					envi_file.spectra_numbers[0] = 1;
					
					// concat with spectrum name if available
					if(envi_file.getNumberOfSpectraNames() == envi_file.getNumberOfSpectra())
					{
						envi_file.setSpectrumFilename(0, envi_file.getSpectrumFilename(0) + " " + envi_file.getSpectrumName(0));
					}
				}
			}
		
			// read body		
			file_input = new FileInputStream (file);					
			data_in = new DataInputStream(file_input);
			
			// load all spectra
			envi_file.setMeasurements(read_data(data_in, envi_file.getNumberOfChannels(0), envi_file.getNumberOfSpectra()));
			
			return envi_file;
		}
		else
			return null;
	}	
	
	
	Float[][] read_data(DataInputStream in, int channels, int no_of_spectra) throws IOException
	{
		Float[][] f = new Float[no_of_spectra][channels];
		
		if(this.envi_file.getDataType() == 4) // 32 bit float
		{		
			
			for(int spec_no = 0; spec_no < no_of_spectra;spec_no++)
			{
				for(int band=0;band < channels;band++)
				{
					if(envi_file.getByteOrder() == 1)
						f[spec_no][band] = in.readFloat();
					else
						f[spec_no][band] = read_float(in);				
				}
			}		
		}
		
		if(this.envi_file.getDataType() == 5) // 64 bit float
		{	
			
			for(int spec_no = 0; spec_no < no_of_spectra;spec_no++)
			{
				for(int band=0;band < channels;band++)
				{
					if(envi_file.getByteOrder() == 1)
						f[spec_no][band] = (float) in.readDouble();
					else
						f[spec_no][band] = read_double(in).floatValue();				
				}
			}	

			
		}
		
				
		return f;
	}

	void read_ENVI_header(DataInputStream in, SpectralFile sf) throws IOException
	{
		String line;
		
		// use buffered stream to read lines
		BufferedReader d = new BufferedReader(new InputStreamReader(in));
		d.mark(100); // mark to enable re-read of line
		
		// read line by line
		while((line=d.readLine()) != null)
		{
			// tokenise the line
			String[] tokens = line.split(" ");
			
			// analyse the tokens
			analyse_ENVI_HDR(tokens, d, sf);	
			
			d.mark(100); // mark to enable re-read of line
		}
		
		if (sf.getMeasurementUnits().size()==0)
		{
			for(int i=0;i<sf.getNumberOfSpectra();i++)
			{
				sf.addMeasurementUnits(0); // assume DN if no unit if given in header
			}
		}
		
	}
	
	void analyse_ENVI_HDR(String[] tokens, BufferedReader in, SpectralFile hdr) throws IOException
	{
		String t1 = tokens[0];
		
		if(t1.equals("ENVI"))
		{
			hdr.setCompany(t1);
		}
		
		if(t1.equals("samples"))
		{ // this is the number of channels (really odd, that one ...)
			hdr.addNumberOfChannels(Integer.valueOf(get_value_from_tokens(tokens)).intValue());
		}
		
		if(t1.equals("lines"))
		{ // this is probably the number of spectra in the body file
			hdr.setNumberOfSpectra(Integer.valueOf(get_value_from_tokens(tokens)).intValue());
		}
		
		if(t1.equals("data") && tokens[1].equals("type"))
		{
			hdr.setDataType(Integer.valueOf(get_value_from_tokens(tokens)).intValue());
		}
		
		if(t1.equals("interleave"))
		{
			hdr.setInterleave(get_value_from_tokens(tokens));
		}
		
		if(t1.equals("byte") && tokens[1].equals("order"))
		{
			hdr.setByteOrder(Integer.valueOf(get_value_from_tokens(tokens)).intValue());
		}
		
		if(t1.equals("spectra") && tokens[1].equals("names"))
		{
			// return to start of line and re-read
			in.reset();			
			read_spectra_names(in);
		}
		
		if(t1.equals("wavelength") && tokens[1].equals("="))
		{
			// return to start of line and re-read
			in.reset();			
			read_wvls(in);
			
			hdr.addWvls(wvls.toArray(new Float[wvls.size()]));
		}
		
		
		
	}
	
	String get_value_from_tokens(String[] tokens)
	{
		int i = 0;
		// search the equal sign because the value follows afterwards
		while(!(tokens[i++].equals("=")));
		
		return tokens[i];
	}
	
	void read_spectra_names(BufferedReader in) throws IOException
	{

		
		envi_file.setNumberOfSpectraNames(0);
		
		opening_parenthesis(in);
		names(in);
		

	}
	
	void read_wvls(BufferedReader in) throws IOException
	{
		opening_parenthesis(in);
		wvls(in);

	}	
	
	// reads till opening parenthesis is found
	void opening_parenthesis(BufferedReader in) throws IOException
	{		
		do
		{
			read_char(in);
		} while (!c.equals('{'));
		
	}
	
	void names(BufferedReader in) throws IOException
	{
		spaces(in);
		name(in);
		
		while(c.equals(','))
		{
			spaces(in);
			linebreak(in);
			name(in);
		}
		
	}
	
	
	
	void name(BufferedReader in) throws IOException
	{
		StringBuffer name = new StringBuffer("");
				
		while(!(c.equals(',') || c.equals('}')))
		{
			name.append(c);
			read_char(in);
		}
		
		envi_file.addSpectrumName(name.toString());
		
	}
	
	void wvls(BufferedReader in) throws IOException
	{
		spaces(in);
		wvl(in);
		
		while(c.equals(','))
		{
			spaces(in);
			linebreak(in);
			wvl(in);
		}
		
	}	
	
	void wvl(BufferedReader in) throws IOException
	{
		String wvl = new String();
				
		while(!(c.equals(',') || c.equals('}')))
		{
			wvl = wvl + c;
			read_char(in);
		}
		
		wvls.add(Float.valueOf(wvl));
		
		
	}	
	
	void spaces(BufferedReader in) throws IOException
	{
		do
		{
			read_char(in);
		} while(c.equals(' '));
		
	}
	
	void linebreak(BufferedReader in) throws IOException
	{
		if(c.equals('\n'))
			read_char(in);		
	}

	
	
	void read_char(BufferedReader in) throws IOException
	{
		char[] c_arr = new char[1];
		in.read(c_arr,0,1);
		c = c_arr[0];
	}
	
	

}
