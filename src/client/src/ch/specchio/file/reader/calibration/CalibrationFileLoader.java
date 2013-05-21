package ch.specchio.file.reader.calibration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import ch.specchio.file.reader.spectrum.Spectra_Vista_HR_1024_FileLoader;
import ch.specchio.file.reader.spectrum.TXT_FileLoader;
import ch.specchio.file.reader.spectrum.UniSpec_SPU_FileLoader;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.types.Calibration;
import ch.specchio.types.SpectralFile;

public class CalibrationFileLoader {
	
	public CalibrationFileLoader() {
		
	}
	
	
	public Calibration loadFile(File file) throws IOException {
		
		Calibration cal = new Calibration();
		boolean file_loaded = false;
		
		// figure out what type of file this is
		String[] tokens = file.getName().split("\\.");

		String ext = tokens[tokens.length-1]; // last element is the extension		
		
		
		// cx for TXT (kneub format) files
		if(ext.equals("txt"))
		{
			file_loaded = true;
			TXT_FileLoader l;
			l = new TXT_FileLoader();
			SpectralFile spec_file = l.load(file);
				
			// these measurements are actually reflectances (in ways)
			spec_file.addMeasurementUnits(MeasurementUnit.Reflectance);
			
			cal.setSpectralFile(spec_file);
				
		}
		
		// UniSpec files
		if(ext.equals("spu"))
		{
			file_loaded = true;
			UniSpec_SPU_FileLoader l;
			l = new UniSpec_SPU_FileLoader();
			SpectralFile spec_file = l.load(file);
				
			//spec_file.addMeasurementUnits(MeasurementUnit.Wavelength);
			spec_file.setMeasurementUnits(0, MeasurementUnit.Wavelength);
				
			// replace the first vector with the wvls
			spec_file.setMeasurement(0, spec_file.getWvls(0));				
				
			cal.setIncludesUncertainty(false);
			
			cal.setSpectralFile(spec_file);
		}
		
		
		
		if (!file_loaded)
		{
		
			FileInputStream file_input = null;
			DataInputStream data_in = null;		
			file_input = new FileInputStream (file);
	
			data_in = new DataInputStream(file_input);	
			String line;
		
		
			// cx if we got GER files
			// use buffered stream to read lines
			BufferedReader d = new BufferedReader(new InputStreamReader(data_in));
			line=d.readLine();
		
			// cx for Spectra Vista HR-1024 files
			if(ext.equals("sig") && (line.equals("/*** Spectra Vista HR-1024 ***/") || line.equals("/*** Spectra Vista SIG Data ***/")))
			{
				file_loaded = true;
				d.close();
				data_in.close();

				Spectra_Vista_HR_1024_FileLoader l = new Spectra_Vista_HR_1024_FileLoader();
				SpectralFile spec_file = l.load(file);
				
				spec_file.setNumberOfSpectra(1); // override the setting (there are actually 3 spectra)
				
				// these measurements are actually reflectances (in ways)
				spec_file.addMeasurementUnits(MeasurementUnit.Wavelength);
				
				// replace the first vector with the wvls
				spec_file.setMeasurement(0, spec_file.getWvls(0));
				
				cal.setIncludesUncertainty(false);
				
				cal.setSpectralFile(spec_file);
				
			}

		}
		
		return cal;
		
	}

}
