package ch.specchio.file.reader.spectrum;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.file.reader.campaign.SpecchioCampaignDataLoader;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.Metadata;
import ch.specchio.types.SpecchioMessage;
import ch.specchio.types.SpectralFile;
import ch.specchio.types.spatial_pos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class FloX_FileLoader extends SpectralFileLoader {
	
	String spectrum_number, date, time, measurement_designator, IT_WR, IT_VEG, chamber_temp, outside_temp, box_rel_hum, rel_hum, instrument_number, gps_time, gps_date, lat, lon;
	ArrayList<ArrayList<Float>> DNs = new ArrayList<ArrayList<Float>>();
	Metadata smd;
	
	public FloX_FileLoader(SPECCHIOClient specchio_client, SpecchioCampaignDataLoader campaignDataLoader) {
		super("FloX", specchio_client, campaignDataLoader);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SpectralFile load(File file) throws IOException, MetaParameterFormatException {
		spec_file = new SpectralFile();
		
		
		spec_file.setCompany("JB Hyperspectral");
		
		// Parse file	
		Reader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
		//Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(bufferedReader);
		Reader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()));	
		CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.newFormat(';'));
		
		Iterable<CSVRecord> csvRecords = csvParser.getRecords();
		
		for (CSVRecord r : csvRecords) {
			
			// skip empty lines
			if(r.get(0).length() != 0)
			{
				// check if this a metadata line
				if(r.get(3).equals("auto_mode"))
				{
					spectrum_number = r.get(0);
					date = r.get(1);
					time = r.get(2);	
					IT_WR = r.get(5);	
					IT_VEG = r.get(7);	
					chamber_temp = r.get(11);
					outside_temp = r.get(13);
					box_rel_hum = r.get(15);
					rel_hum = r.get(17);
					instrument_number = r.get(18);
					gps_time  = r.get(20);
					gps_date = r.get(22);
					lat = r.get(24);
					lon = r.get(26);
					
				}
				else
				{
					// this is a measurement
					measurement_designator = r.get(0);
					
					// fill vector
					ArrayList<Float> DN = new ArrayList<Float>();
					
					for(int i=1;i<r.size();i++)
					{
						DN.add(Float.valueOf(r.get(i)));
					}
					
					DNs.add(DN);
					
					// add spectrum metadata to the file object
					spec_file.addSpectrumFilename(spectrum_number + "-" + measurement_designator + " (" + file.getName() + ")");
					
					
					MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Acquisition Time"));
					DateTime dt;
					int time_str_len = time.length();
					Integer sec = Integer.valueOf(time.substring(time_str_len-2,time_str_len));
					Integer min = Integer.valueOf(time.substring(time_str_len-4,time_str_len-2));
					Integer hour = Integer.valueOf(time.substring(0,time_str_len-4));
					Integer mday = Integer.valueOf(date.substring(4,6));
					Integer month = Integer.valueOf(date.substring(2,4)); 
					Integer year = Integer.valueOf(date.substring(0,2)) + 2000;
					
					
					dt =  new DateTime(year, month, mday, hour, min, sec, DateTimeZone.UTC); // joda months start at 1
					
					mp.setValue(dt);
					
					smd = new Metadata();
					smd.addEntry(mp);	
					
					mp = MetaParameter.newInstance(attributes_name_hash.get("Instrument Temperature"));
					mp.setValue(chamber_temp);
					smd.addEntry(mp);
					
					mp = MetaParameter.newInstance(attributes_name_hash.get("Ambient Temperature"));
					mp.setValue(chamber_temp);
					smd.addEntry(mp);	
					
					mp = MetaParameter.newInstance(attributes_name_hash.get("Relative Humidity"));
					mp.setValue(rel_hum);
					smd.addEntry(mp);					
				
					mp = MetaParameter.newInstance(attributes_name_hash.get("Integration Time"));
					if(measurement_designator.equals("WR") || measurement_designator.equals("DC_WR"))
					{
						mp.setValue(IT_WR);
					}
					else
					{
						mp.setValue(IT_VEG);
					}
					smd.addEntry(mp);	
					
					// add coordinates
					spatial_pos pos = new spatial_pos();
					String[] lat_tokens = lat.split(" ");
					int lat_sign = 1;
					if(lat_tokens.length>1)
					{
						if(lat_tokens[1].equals("N"))
						{
							// remains positive
						}
						else
							lat_sign = -1;
					}
					
					String[] lon_tokens = lon.split(" ");
					int lon_sign = 1;
					if(lon_tokens.length>1)
					{
						if(lon_tokens[1].equals("E"))
						{
							// remains positive
						}
						else
							lon_sign = -1;
					}					
					
					pos.latitude = Double.valueOf(lat_tokens[0]) * lat_sign;
					pos.longitude = Double.valueOf(lon_tokens[0]) * lon_sign;
					
					spec_file.addPos(pos);
					
					spec_file.addEavMetadata(smd);
					
					int x  =0;
					
				}
			
			}

		}
		
		reader.close();
		csvParser.close();
		
		// add measurements as float matrix
		Float[][] f = new Float[DNs.size()][DNs.get(0).size()];
		
		for(int i = 0;i<DNs.size();i++)
		{				
			f[i] = DNs.get(i).toArray(f[i]);
		}		
		spec_file.setNumberOfSpectra(DNs.size());
		spec_file.setMeasurements(f);
		
		String path = file.getAbsolutePath();
		spec_file.setPath(path);		
		spec_file.setFilename(file.getName());
		spec_file.setFileFormatName(this.file_format_name);		
		
		spec_file.setInstrumentName(instrument_number);
		
		// get wavelength reference from cal.csv
		
		File cal_file = this.campaignDataLoader.getFlox_rox_cal_file();
		boolean cal_file_not_found = false;
		
		if(cal_file.exists())
		{

			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(cal_file), StandardCharsets.UTF_8));
			reader = Files.newBufferedReader(Paths.get(cal_file.getAbsolutePath()));	
			csvParser = new CSVParser(bufferedReader, CSVFormat.newFormat(';'));
			csvRecords = csvParser.getRecords();
			
			String first_token = csvRecords.iterator().next().get(0);
			
			if(!first_token.equals("wl_F"))
				cal_file_not_found = true;
			else
			{
				int i=0;
				ArrayList<Float> wvls = new ArrayList<Float>();
				for (CSVRecord r : csvRecords) {
				
					if(i>0)
					{					
						wvls.add(Float.valueOf(r.get(0)));
					}
					i++;
					
				}
				
				spec_file.addWvls(new Float[wvls.size()]);
				spec_file.setWvls(0, wvls.toArray(spec_file.getWvls(0)));	
			}

		}
		else
		{
			cal_file_not_found = true;			
		}
		
		
		if(cal_file_not_found)
		{
			
			// output file error
			spec_file.setFileErrorCode(SpectralFile.UNRECOVERABLE_ERROR);
			ArrayList<SpecchioMessage> file_errors = spec_file.getFileErrors();
			if(file_errors == null)
			{
				file_errors = new ArrayList<SpecchioMessage>();						
			}

			file_errors.add(new SpecchioMessage("No calibration file (cal.csv) could be found alongside the spectral file. You moron!!!", SpecchioMessage.ERROR));
			spec_file.setFileErrors(file_errors);
			
		}
		
		
	    
		return spec_file;
	}

  
}
