package ch.specchio.file.reader.spectrum;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.file.reader.campaign.SpecchioCampaignDataLoader;
import ch.specchio.types.SpecchioMessage;
import ch.specchio.types.SpectralFile;

public class UniSpec_FileLoader extends SpectralFileLoader {
	
	boolean not_eof = true;
	
	public UniSpec_FileLoader(SPECCHIOClient specchio_client, SpecchioCampaignDataLoader campaignDataLoader)
	{
		super("UniSpec", specchio_client, campaignDataLoader);
	}
	
	
	public SpectralFile load(File file) throws IOException
	{
		SpectralFile f = new SpectralFile();
		f.setNumberOfSpectra(2); 
		
		//f.instr_set = new InstrumentSettings();
		
		f.setPath(file.getAbsolutePath());		
		f.setFilename(file.getName());		
		
		f.setCompany("PP Systems");
		f.setFileFormatName(this.file_format_name);
		f.setInstrumentTypeNumber(2); // hard coded arbitrary value within SPECCHIO as PP systems assigns no instrument codes
	
		file_input = new FileInputStream (file);			
		
		data_in = new DataInputStream(file_input);
		
		
		BufferedReader d = new BufferedReader(new InputStreamReader(data_in));
		
		// analyse header to get no of spectra
		boolean success = read_header(d, f, true);
		
		if (!success) return f;
		
		f.addSpectrumFilename(f.getFilename());
		f.addSpectrumFilename(f.getFilename());
		
		f.addMeasurementUnits(0); // DN
		f.addMeasurementUnits(0); // DN		
		
		f.setMeasurements(read_data(d, f));
//		f.capture_dates[0] = file.lastModified(); 
		
		data_in.close ();
		
		return f;
	}
	
	// reads the header line in order to get the number of spectra contained in this file
	// or stores the spectra names (depending on the analyse flag)

	public boolean read_header(BufferedReader d, SpectralFile f, boolean analyse) throws IOException
	{
		
		try
		{
		
		String line=d.readLine(); // line 1: File path
		
		while(line.length() == 0 || line.substring(0,1).equals("\""))
		{
			d.mark(500); // best guess ...
			
			if(line.length() > 0)
			{
				
				String[] sub_toks = line.split(":");
				
				String tag = sub_toks[0];
				tag = tag.substring(1);
				//String val = sub_toks[1];
				
				
				if (tag.equals("Time")) 
				{
					// skip white spaces
					int start = 0;
					//while(start+1 < sub_toks[1].length() && (sub_toks[1].substring(start, start+1)).equals(" ")) start++;
					
					String time_str = line.substring(6, line.length() - 1);
					
					while(start+1 < time_str.length() && (time_str.substring(start, start+1)).equals(" ")) start++;
					time_str = time_str.substring(start);
					
					// clean up tokens to have just one space between date and time
					String[] tokens = time_str.split("\\s+");
					
					if (tokens.length == 3 && !(time_str.contains("PM") || time_str.contains("AM")))
					{
						// looks like we got one of the odd three token items without AM/PM
						time_str = tokens[0] + " " + tokens[1];
					}
					else
					{
						time_str = tokens[0];
						for(int i=1;i<tokens.length;i++)
						{
							time_str = time_str + " " + this.remove_leading_spaces( tokens[i]);
						}
					}					
					
					// "Time:       10/30/80  14:33:57"
					DateTimeFormatter formatter;
					
					if(time_str.contains("PM") || time_str.contains("AM"))
					{
						formatter = DateTimeFormat.forPattern("MM/dd/yy hh:mm:ss a").withZoneUTC();
					}
					else
					{
						formatter = DateTimeFormat.forPattern("MM/dd/yy HH:mm:ss").withZoneUTC();
					}
					
					DateTime dt = formatter.parseDateTime(time_str);
				
//					DateTimeFormatter fmt = DateTimeFormat.forPattern(MetaDate.DEFAULT_DATE_FORMAT);
//					String date_str = fmt.print(dt);

					
//					DateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
//				    Date date;
//					try {
//						date = sdf.parse(time_str);
						
//						System.out.println("Date and Time: " + date);
						
						f.setCaptureDate(0, dt);		
						f.setCaptureDate(1, dt); // time for both measurements is taken as the same	
						
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}

					
//					DateFormat df = DateFormat.getDateInstance();
//					try {
//						Date myDate = df.parse(time_str);
//						
//						System.out.println("Date and Time: " + myDate);
//						
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				    
					
					
//					String time_part_1 = sub_toks[1].substring(start);
//					
//					String[] time_tokens_1 = time_part_1.split("  ");
//					
//					String[] date_tokens = time_tokens_1[0].split("/");
//					
//					String hrs = time_tokens_1[1];
//					
//					String day = date_tokens[1];
//					String month = date_tokens[0];
//					String year_str = date_tokens[2];
//					
//					Integer year = Integer.valueOf(year_str);
//					
//					if(year > 70)
//					{
//						year = year +1900;
//					}
//					else
//					{
//						year = year + 2000;
//					}
//					
//					Integer minutes = Integer.valueOf(sub_toks[2]);
//					String seconds_str = sub_toks[3].substring(0, sub_toks[3].length()-1);
//					Integer seconds = Integer.valueOf(seconds_str);
//					
//					TimeZone tz = TimeZone.getTimeZone("UTC");
//					Calendar cal = Calendar.getInstance(tz);
//					cal.set(year, Integer.valueOf(month)-1, // month is zero based!
//							Integer.valueOf(day), Integer.valueOf(hrs), 
//							minutes, seconds);
//					
//					f.capture_dates[0] = cal.getTime();		
//					f.capture_dates[1] = f.capture_dates[0]; // time for both measurements is taken as the same				
					
					
					
				}
			}
			
			line=d.readLine();
			
		}
		
		} catch (NullPointerException e) {
			
			f.setFileErrorCode(SpectralFile.UNRECOVERABLE_ERROR);
			ArrayList<SpecchioMessage> file_errors = new ArrayList<SpecchioMessage>();
			file_errors.add(new SpecchioMessage("Incomplete File.", SpecchioMessage.ERROR));
			f.setFileErrors(file_errors);

			return false;
		}
		
		d.reset();
		
		
		//line = line.replace("  ", " "); // replace double spaces with single ones
//		line = line.replace("\"", ""); // remove the double quotes at the start
//		line = line.substring(1, line.length()-1);
//		
//		String[] tokens = line.split("  ");	
//		
//		// first token holds the information about the measurement type
//		if (tokens[0].equals("TRANS->")) f.measurement_units[0] = 6;
//
//		for(int i=2;i<tokens.length; i++)
//		{
//			tokens[i] = tokens[i].replace(" ", ""); // remove spaces from the token
//			
//			String[] sub_toks = tokens[i].split(":");
//			
//			String tag = sub_toks[0];
//			String val = sub_toks[1];
//			
//			//if (tag.equals("Pix")) f.no_of_channels = Integer.valueOf(val);
//			
//			if (tag.equals("Time")) 
//			{
//				String int_time_str = val.substring(0, val.length()-2);
//				f.instr_set.add_setting("Integration_time", Integer.valueOf(int_time_str)); // cut the ms from the number
//				// should store here the unit of the integration time as well!
//				
//			}
//			
//			if (tag.equals("Avg")) f.internal_average_cnt = Integer.valueOf(val);
//		}
//
//		f.set_no_of_spectra(1);
		
		return true;

	}
	

	
	Float[][] read_data(BufferedReader d, SpectralFile f)
	{
		

		ArrayList<Float> tgt = new ArrayList<Float>();
		ArrayList<Float> ref = new ArrayList<Float>();
		ArrayList<Float> wvl = new ArrayList<Float>();
		String line;
		
		try {
			
			// read all lines
			line=d.readLine();
			while(line != null && line.length() > 0)
			{
				//line = line.substring(1); // cut start white space
				
				String[] tokens = line.split("\\s+");		
				
				//if(tokens.length == 1) tokens = line.split(" ");	// special case for e.g. 401.29 -3

				tgt.add(Float.valueOf(tokens[1]));				
				wvl.add(Float.valueOf(tokens[0]));
				
				line=d.readLine();

			}

			
		
			line=d.readLine(); // Reference:
			line=d.readLine();
			while(line != null && line.length() > 0)
			{
				//line = line.substring(1); // cut start white space			
	
				ref.add(Float.valueOf(line));				
				line=d.readLine();
			}
	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		f.addNumberOfChannels(tgt.size());
		
		// fill wvls
		f.addWvls(new Float[wvl.size()]);
		Float[] tmp = new Float[wvl.size()];
		wvl.toArray(tmp);
		f.setWvls(0, tmp);		// before wvls was an arraylist: wvl.toArray(f.wvls);
		


		
		Float[][] out_spectrum = new Float[f.getNumberOfSpectra()][f.getNumberOfChannels(0)];
		
		// check if size of the two vectors are the same: happens if corrupted files are encountered (e.g. YS00093.SPT of John Gamons sky oaks dataset)
		if (tgt.size() != ref.size())
		{
			f.setFileErrorCode(SpectralFile.UNRECOVERABLE_ERROR);
			ArrayList<SpecchioMessage> file_errors = new ArrayList<SpecchioMessage>();
			file_errors.add(new SpecchioMessage("Target and reference vectors are of unequal length.", SpecchioMessage.ERROR));
			f.setFileErrors(file_errors);
		}
		else
		{

			ListIterator<Float> tgt_li = tgt.listIterator();
			ListIterator<Float> ref_li = ref.listIterator();
			int i = 0;
			while(tgt_li.hasNext())
			{
				out_spectrum[0][i] = (Float)tgt_li.next();
				out_spectrum[1][i++] = (Float)ref_li.next();
			}
		}
		
		return out_spectrum;
		
	}
	
	
}
