package ch.specchio.file.reader.spectrum;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.Metadata;
import ch.specchio.types.SpectralFile;

public class COST_OO_FileLoader extends SpectralFileLoader {
	
	SpectralFile spec_file;

	public COST_OO_FileLoader() {
		super("COST_OO_CSV");


	}

	@Override
	public SpectralFile load(File file) throws IOException, MetaParameterFormatException {
		
		if (file.getName().contains("FullSpec_index_manual"))
			return null; // ignore info files		
		
		int no_of_spectra = 3;
		
		spec_file = new SpectralFile();
		spec_file.setNumberOfSpectra(no_of_spectra); 
		
		
//		for (int i = 0; i < no_of_spectra; i++)
//		{
//			spec_file.specchio_eav_metadata[i] = new SPECCHIOMetadata();
//		}
		
		
		spec_file.setPath(file.getAbsolutePath());		
		spec_file.setFilename(file.getName());
		spec_file.setFileFormatName(this.file_format_name);
		
		spec_file.setCompany("COST_OO_CSV");
		
		// get metadata from the FullSpec_index_manual.csv file
		int end_of_path_index = file.getAbsolutePath().lastIndexOf(
				File.separator);
		File meta_file = new File(file.getAbsolutePath().substring(0,
				end_of_path_index + 1)
				+ "FullSpec_index_manual.csv");		
		get_metadata(meta_file, spec_file);
		
		
		// spectrum number is contained in the extension
//		spec_file.spectra_numbers[0] = Integer.valueOf(spec_file.base_name.substring(spec_file.base_name.length()-3));
//		spec_file.spectra_numbers[1] = spec_file.spectra_numbers[0];
//		spec_file.spectra_numbers[2] = spec_file.spectra_numbers[0];
		
		Metadata smd = new Metadata();
		
		Integer no = Integer.valueOf(spec_file.getBasename().substring(spec_file.getBasename().length()-3));
		
		MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Spectrum Number"));
		mp.setValue(no, "RAW");
		smd.addEntry(mp);				
		
		spec_file.addEavMetadata(smd);
		spec_file.addEavMetadata(smd);
		spec_file.addEavMetadata(smd);
		
						
		spec_file.addSpectrumFilename(spec_file.getFilename()); // target name
		spec_file.addSpectrumFilename(spec_file.getFilename()); // reference name
		spec_file.addSpectrumFilename(spec_file.getFilename()); // reflectance name
//
//		
		spec_file.addMeasurementUnits(2); // radiance as default
		spec_file.addMeasurementUnits(2);
		spec_file.addMeasurementUnits(1); // reflectance
		
		
		file_input = new FileInputStream (file);			
		
		data_in = new DataInputStream(file_input);
		
		read_COST_OO_CSV_file(data_in, spec_file);
		
		
		
		data_in.close ();		
		
		
		return spec_file;
	}

	private void get_metadata(File metadata_compilation_filename, SpectralFile spec_file) throws MetaParameterFormatException
	{
		String line;
		boolean found_entry = false;
		
		String[] filename_tokens = spec_file.getBasename().split("_");
		
		String sample_name = filename_tokens[1];
		
		// try opening file
		try {
			FileInputStream file_input = new FileInputStream (metadata_compilation_filename);
			
			DataInputStream data_in = new DataInputStream(file_input);		
		
			BufferedReader d = new BufferedReader(new InputStreamReader(data_in));
			
			// read header
			String hdr = d.readLine();
			String[] col_names= hdr.split(";");
			
			// read line by line
			while((line=d.readLine()) != null && !found_entry)
			{
				// tokenise
				String[] tokens = line.split(";"); // values are separated by a semicolon
				
				if(tokens[0].equals(sample_name))
				{
					
					// time
					TimeZone tz = TimeZone.getTimeZone("UTC");
					Calendar cal = Calendar.getInstance(tz);					
//					float dayOfYear_float = Float.valueOf(tokens[2]);
//					int dayOfYear = (int) dayOfYear_float;
//					TimeZone tz = TimeZone.getTimeZone("UTC");
//					Calendar cal = Calendar.getInstance(tz);
//					cal.set(Calendar.DAY_OF_YEAR, (dayOfYear));
					
					String[] time_tokens = tokens[1].split(":");
					
					String date = sample_name.substring(2, 10);

					Integer sec = 0;
					Integer min = Integer.valueOf(time_tokens[1]);
					Integer hour = Integer.valueOf(time_tokens[0]);
					Integer mday = Integer.valueOf(date.substring(7, 8));
					Integer month = Integer.valueOf(date.substring(5, 6)) - 1; // months start at 0 in Java
					Integer year = Integer.valueOf(date.substring(0, 4));					

					cal.set(year, month, mday, hour, min, sec);
					
//					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmm");
//					formatter.setTimeZone(tz);			
//					String out_=formatter.format(cal.getTime());					
					
					spec_file.setCaptureDate(0, cal.getTime());
					spec_file.setCaptureDate(1, spec_file.getCaptureDate(0));
					spec_file.setCaptureDate(2, spec_file.getCaptureDate(0));     
					
					
					// illumination angles for all three spectra

					spec_file.addIlluminationZenith(Float.valueOf(tokens[3]));
					spec_file.addIlluminationZenith(Float.valueOf(tokens[3]));
					spec_file.addIlluminationZenith(Float.valueOf(tokens[3]));
				
					
					
					
					spec_file.addIlluminationAzimuth(Float.valueOf(tokens[4]));
					spec_file.addIlluminationAzimuth(Float.valueOf(tokens[4]));
					spec_file.addIlluminationAzimuth(Float.valueOf(tokens[4]));

					
					
					// sensor angles : assumed to be nadir looking
					
					spec_file.addSensorZenith(0f);
					spec_file.addSensorZenith(0f);
					spec_file.addSensorZenith(0f);
					
					
					spec_file.addSensorAzimuth(0f);
					spec_file.addSensorAzimuth(0f);
					spec_file.addSensorAzimuth(0f);
					
					
					// illumination source: Sun is assumed to be default and only value for FLEX data
					spec_file.setLightSource("Sun");
					
					
					// auto loading of all further info as EAV entries
					for (int i = 5; i < col_names.length; i++)
					{
						MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get(col_names[i]));						
						
						Object val = null;
						try
						{
							val = Integer.valueOf(tokens[i]);

						} catch (NumberFormatException ex) {
							
							try
							{
								val = Double.valueOf(tokens[i]);

							} catch (NumberFormatException exx) {
								
								int length = tokens[i].length();
								if (length > 200)
								{
									tokens[i] = tokens[i].substring(0, 200-1);					
								}
								
								val = tokens[i].replace("'", ""); // string, // clean by removing single quotes  // cut to 200 characters
								
							}				
						}							
						
						//System.out.println(mp.getAttribute_name());
						
						mp.setValue(val);
						spec_file.getEavMetadata(0).addEntry(mp);
						spec_file.getEavMetadata(1).addEntry(mp);
						spec_file.getEavMetadata(2).addEntry(mp);


					}
					
					found_entry = true;
					
				}
			
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

		
	}

	private void read_COST_OO_CSV_file(DataInputStream data_in,
			SpectralFile f)  throws IOException {
		

		String line;
		
		// use buffered stream to read lines
		BufferedReader d = new BufferedReader(new InputStreamReader(data_in));
		
		// read header line
		line=d.readLine();
		
		
		// read the measurements
		f.setMeasurements(read_data(data_in, d));
		
		f.addNumberOfChannels(f.getMeasurement(0).length);		
		

	}

	private Float[][] read_data(DataInputStream data_in, BufferedReader d)  throws IOException
	{

		Float[][] f;
		String line;
		ArrayList<Float> target, reference, reflectance, wvls;
		
		wvls = new ArrayList<Float>();
		target = new ArrayList<Float>();
		reference = new ArrayList<Float>();
		reflectance = new ArrayList<Float>();
				
		// read line by line
		while((line=d.readLine()) != null)
		{
						
			// tokenise the line
			String[] tokens = line.split(";"); // values are separated by a semicolon
			
			// first token is wavelength
			wvls.add(Float.valueOf(tokens[0]));
			
			// second token is radiance of reference
			Float ref_val = Float.valueOf(tokens[1]);
			reference.add(ref_val);
			
			// third token is radiance of target
			target.add(Float.valueOf(tokens[2]));
			
			// 4th token is radiance of reflectance
			reflectance.add(Float.valueOf(tokens[3]));			
			
		}
		

		f = new Float[3][target.size()];
		
		f[0] = reference.toArray(f[0]);
		f[1] = target.toArray(f[1]);
		f[2] = reflectance.toArray(f[2]);
		
		
		
		
		spec_file.addWvls(new Float[target.size()]);
		spec_file.setWvls(0,wvls.toArray(spec_file.getWvls(0)));
		
		
		return f;
		
		
	}

}
