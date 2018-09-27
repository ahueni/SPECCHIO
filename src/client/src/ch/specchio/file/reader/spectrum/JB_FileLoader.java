package ch.specchio.file.reader.spectrum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.DateTime;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.file.reader.campaign.SpecchioCampaignDataLoader;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.SpecchioMessage;
import ch.specchio.types.SpectralFile;

public abstract class JB_FileLoader extends SpectralFileLoader {
	
	boolean is_fluoresence_sensor = false;
	String voltage;

	private ArrayList<Float> wvls_fluorescence = new ArrayList<Float>();
	private ArrayList<Float> up_coef_fluorescence = new ArrayList<Float>();
	private ArrayList<Float> dw_coef_fluorescence = new ArrayList<Float>();
	private ArrayList<Float> wvls_broadrange = new ArrayList<Float>();
	private ArrayList<Float> up_coef_broadrange = new ArrayList<Float>();
	private ArrayList<Float> dw_coef_broadrange = new ArrayList<Float>();
	

	public JB_FileLoader(String file_format_name, SPECCHIOClient specchio_client,
			SpecchioCampaignDataLoader campaignDataLoader) {
		super(file_format_name, specchio_client, campaignDataLoader);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SpectralFile load(File file) throws IOException, MetaParameterFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	
	protected void setCalibration_date(String string)
	{
		Integer mday = Integer.valueOf(string.substring(6,8));
		Integer month = Integer.valueOf(string.substring(4,6)); 
		Integer year = Integer.valueOf(string.substring(0,4));
		
		DateTime dt = new DateTime(year, month, mday, 0, 0);
		spec_file.setCalibration_date(dt);	
		setCalibration_date(dt);
		
	}
	
	
	protected void getCalibrationData()
	{
	
		
		File cal_file = this.campaignDataLoader.getFlox_rox_cal_file();
		boolean cal_file_not_found = false;
		
		if(cal_file != null && cal_file.exists())
		{

			BufferedReader bufferedReader;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(cal_file), StandardCharsets.UTF_8));

			//BufferedReader reader = Files.newBufferedReader(Paths.get(cal_file.getAbsolutePath()));	
			CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.newFormat(';'));
			//csvRecords = csvParser.getRecords();
			
			String first_token = csvParser.iterator().next().get(0);
			
			
			
			if(first_token.equals("wl_F")) // RoX cal file format: F stands for full range
			{
				int i=0;

				for (CSVRecord r : csvParser) {
				
					if(i>0)
					{					
						wvls_broadrange.add(Float.valueOf(r.get(0)));
						up_coef_broadrange.add(Float.valueOf(r.get(1)));
						dw_coef_broadrange.add(Float.valueOf(r.get(2)));
					}
					i++;
					
					if(r.getRecordNumber()==4)
					{
						setCalibration_date(r.get(6));
						Integer cal_number = Integer.getInteger(r.get(6));
						spec_file.setCalibrationSeries(cal_number);
					}

				}
				

				
				spec_file.addWvls(new Float[wvls_broadrange.size()]);
				spec_file.setWvls(0, wvls_broadrange.toArray(spec_file.getWvls(0)));	
			}
			else if(first_token.equals("wl")) // FloX cal file format (two spectrometers)
			{
				int i=0;

				for (CSVRecord r : csvParser) {
				
					if(i>0)
					{					
						wvls_fluorescence.add(Float.valueOf(r.get(0)));
						up_coef_fluorescence.add(Float.valueOf(r.get(1)));
						dw_coef_fluorescence.add(Float.valueOf(r.get(2)));						
						wvls_broadrange.add(Float.valueOf(r.get(3)));
						up_coef_broadrange.add(Float.valueOf(r.get(4)));
						dw_coef_broadrange.add(Float.valueOf(r.get(5)));						
					}
					i++;
					
					if(r.getRecordNumber()==4)
					{
						setCalibration_date(r.get(6));
						Integer cal_number = Integer.parseInt(r.get(6));
						spec_file.setCalibrationSeries(cal_number);
					}
					
				}
				
				if (is_fluoresence_sensor)
				{
					spec_file.addWvls(new Float[wvls_fluorescence.size()]);
					spec_file.setWvls(0, wvls_fluorescence.toArray(spec_file.getWvls(0)));						
				}
				else
				{
					spec_file.addWvls(new Float[wvls_broadrange.size()]);
					spec_file.setWvls(0, wvls_broadrange.toArray(spec_file.getWvls(0)));					
				}

				csvParser.close();
				
			}				
			else
				cal_file_not_found = true;
			
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				
	}

	public ArrayList<Float> getUp_coef_fluorescence() {
		return up_coef_fluorescence;
	}

	public ArrayList<Float> getDw_coef_fluorescence() {
		return dw_coef_fluorescence;
	}

	public ArrayList<Float> getUp_coef_broadrange() {
		return up_coef_broadrange;
	}

	public ArrayList<Float> getDw_coef_broadrange() {
		return dw_coef_broadrange;
	}
	
	public boolean is_fluoresence_sensor() {
		return is_fluoresence_sensor;
	}



	
	
}
