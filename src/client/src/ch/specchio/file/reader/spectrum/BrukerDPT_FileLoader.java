package ch.specchio.file.reader.spectrum;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.SpectralFile;

public class BrukerDPT_FileLoader extends SpectralFileLoader {

	public BrukerDPT_FileLoader(SPECCHIOClient specchio_client) {
		super("Bruker_FTIR_DPT", specchio_client);
	}

	@Override
	public SpectralFile load(File file) throws IOException,
			MetaParameterFormatException {
		spec_file = new SpectralFile();
		spec_file.setNumberOfSpectra(1); // is that always correct????
		
		spec_file.setPath(file.getAbsolutePath());		
		spec_file.setFilename(file.getName());
		spec_file.setFileFormatName(this.file_format_name);
		
		spec_file.setCompany("Bruker FTIR");
		spec_file.addSpectrumFilename(spec_file.getFilename()); 
		
		spec_file.addMeasurementUnits(0, MeasurementUnit.Reflectance);
		
		file_input = new FileInputStream (file);			
				
		data_in = new DataInputStream(file_input);
		
		read_DPT_file(data_in, spec_file);		
		
		data_in.close ();
		
		return spec_file;		
		

	}
	
	
	public void read_DPT_file(DataInputStream in, SpectralFile f) throws IOException
	{
		// use buffered stream to read lines
		BufferedReader d = new BufferedReader(new InputStreamReader(in));
				
		// read the measurements
		f.setMeasurements(read_data(data_in, d));
		
		f.addNumberOfChannels(f.getMeasurement(0).length);
		
	}	
	
	Float[][] read_data(DataInputStream in, BufferedReader d) throws IOException
	{
		String line;
		ArrayList<Float> target, wvls;
		
		wvls = new ArrayList<Float>();
		target = new ArrayList<Float>();

				
		// read line by line
		while((line=d.readLine()) != null)
		{
			// tokenise the line
			String[] tokens = line.split(","); // values are separated by a tab
			
			// first token is wavelength
			wvls.add(10000000 / Float.valueOf(tokens[0])); // conversion from wavenumber to nanometers: http://www.powerstream.com/inverse-cm.htm
			
			// second token is data of target
			target.add(Float.valueOf(tokens[1]));
				
		}
		
		
		
		Float[][] f = new Float[1][target.size()];
		spec_file.addWvls(new Float[target.size()]);
		

		spec_file.setWvls(0, wvls.toArray(spec_file.getWvls(0)));
		
		f[0] = target.toArray(f[0]);
		
		return f;

		
	}
	

}
