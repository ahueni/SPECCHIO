package ch.specchio.file.reader.campaign;

import java.io.*;
import java.util.ArrayList;
import java.util.ListIterator;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.file.reader.spectrum.*;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.SpecchioMessage;
import ch.specchio.types.SpectralFile;
import ch.specchio.types.SpectralFileInsertResult;
import ch.specchio.types.SpectralFiles;

public class SpecchioCampaignDataLoader extends CampaignDataLoader {
	
	private SPECCHIOClient specchio_client;
	private SpectralFileLoader sfl;

	private int root_hierarchy_id;
	
	ArrayList<String> file_errors = new ArrayList<String>();
	private int successful_file_counter;
	private int parsed_file_counter;

	public SpecchioCampaignDataLoader(CampaignDataLoaderListener listener, SPECCHIOClient specchio_client) {
		super(listener);
		
		this.specchio_client = specchio_client;

	}

	// the actual code for loading a campaign
	public void run() {
		try {
			
			// clear EAV known metadata entries because some delete operation might have happened in the meantime
			specchio_client.clearMetaparameterRedundancyList();

			// tell the listener that we're about to begin
			listener.campaignDataLoading();
			
			// update the campaign data on the server
			specchio_client.updateCampaign(campaign);

			// get the data path for this campaign
			File f = new File(campaign.getPath());

			// now we create the root hierarchy for this campaign
			root_hierarchy_id = insert_hierarchy(f.getName(), 0);
			load_directory(root_hierarchy_id, f, false);
			
			// force a refresh of the client cache for potentially new sensors, instruments and calibrations
			// TODO: only refresh if new sensors, instruments and/or calibrations were actually inserted
			specchio_client.refreshMetadataCategory("sensor");
			specchio_client.refreshMetadataCategory("instrument");
			specchio_client.refreshMetadataCategory("calibration");
			
			// tell the listener that we're finished
			listener.campaignDataLoaded(parsed_file_counter, successful_file_counter, spectrum_counter, this.file_errors);

		}
		catch (SPECCHIOClientException ex) {
			listener.campaignDataLoadError(ex.getMessage() + "\n" + ex.getDetails());
		}
		catch (IOException ex) {
			listener.campaignDataLoadError(ex.getMessage());
		}
		
		// tell the listener that we're finished
		//listener.campaignDataLoaded(successful_file_counter, this.file_errors);

	}

	// Recursive method: if the parent_id is zero then it is the root directory
	// otherwise parent_id is the hierarchy_level_id of the parent directory
	// The dir is a File object that points to the directory on the
	// file system to be read
	void load_directory(int parent_id, File dir, boolean parent_garbage_flag) throws SPECCHIOClientException, FileNotFoundException {
		int hierarchy_id = 0;
		ArrayList<File> files, directories;
		SpectralFile spec_file;
		boolean is_garbage = parent_garbage_flag;

		// Garbage detection: all data that are under a folder called 'Garbage' will get an EAV garbage flag
		// this allows users to load also suboptimal (i.e. garbage) data into the database, but easily exclude them from any selection
		if(dir.getName().equals("Garbage"))
		{
			is_garbage = true; // marks this directory as the one recognised as garbage
		}

		// get the names of all files in dirs in the current dir
		String[] whole_content = dir.list();
		if (whole_content == null) {
			throw new FileNotFoundException("The campaign directory " + dir.toString() + " does not exist.");
		}

		// File filter
		// count files that we do not want
		for (int i = 0; i < whole_content.length; i++) {
			// filter the dot files
			if (whole_content[i].startsWith(".", 0)) {
				System.out.println("Filtered .<file>");
			}
		}

		ArrayList<String> content = new ArrayList<String>();

		// build content without unwanted files
		for (int i = 0; i < whole_content.length; i++) {
			if (!whole_content[i].startsWith(".", 0)) {
				content.add(whole_content[i]);
			}
		}

		// create array to store the File objects in
		directories = new ArrayList<File>();
		files = new ArrayList<File>();

		// get the number of files and subdirectories in the current
		// directory
		ListIterator<String> li = content.listIterator();
		while(li.hasNext()) {
			// here we construct the absolute pathname for each object in
			// the directory

			File f = new File(dir.toString() + File.separator + li.next());

			if (f.isDirectory())
			{
				directories.add(f);
			}
			else
			{
				files.add(f);
			}
		}

		// if there are subdirs, call all of them (recursive call)
		// only call dirs, files are ignored
		ListIterator<File> dir_li = directories.listIterator();
		while(dir_li.hasNext()) 
		{
			File curr_dir = dir_li.next();

			// use the names of the first hierarchy (the one below
			// the root) to show in progress report
			if (parent_id == root_hierarchy_id) {
				listener.campaignDataLoadOperation(curr_dir.getName());
			}

			// create a new entry in the database for this directory
			hierarchy_id = insert_hierarchy(curr_dir.getName(), parent_id);

			load_directory(hierarchy_id, curr_dir, parent_garbage_flag);
		}

		// load each file using the spectral
		// file loader
		if (files.size() > 0) {	



			// get the spectral file loader needed for this directory
			// sfl = get_spectral_file_loader(files);



			ArrayList<SpectralFile> spectral_file_list = new ArrayList<SpectralFile>();			

			// iterate over the files
			ListIterator<File> file_li = files.listIterator();

			while(file_li.hasNext()) {
				File file = file_li.next();

				ArrayList<File> this_file = new ArrayList<File>(); // overkill ... change to single object later ...

				this_file.add(file);
				
				

				sfl = get_spectral_file_loader(this_file);

				if (sfl != null) {

					try {

						// the loader can return null, e.g. if ENVI files are
						// read
						// and a body (*.slb) is passed.
						// In such a case no spectrum is inserted.
						spec_file = sfl.load(file);
						if (spec_file != null && spec_file.getNumberOfSpectra() > 0)
						{
							if(spec_file.getFileErrorCode() != SpectralFile.UNRECOVERABLE_ERROR)
							{
								spec_file.setGarbageIndicator(is_garbage);

								// add to file list
								spectral_file_list.add(spec_file);

							}
							else
							{
								// serious error
								// add the message to the list of all errors
								// concatenate all errors into one message
								StringBuffer buf = new StringBuffer("Issues found in " + spec_file.getFilename() + ":");

								for (SpecchioMessage error : spec_file.getFileErrors()) {

									buf.append("\n\t");

									buf.append(error.toString());
								}

								buf.append("\n");

								// add the message to the list of all errors
								this.file_errors.add(buf.toString());								
								
							}
							file_counter++;

						}


						parsed_file_counter++;

						listener.campaignDataLoadFileCount(file_counter, spectrum_counter);
					}
					catch (IOException ex) {
						listener.campaignDataLoadError(file + ": " + ex.getMessage());
					}
					catch (MetaParameterFormatException ex) {
						listener.campaignDataLoadError(file + ": " + ex.getMessage());
					}
				}
			}
			
			if (spectral_file_list.size() > 0)
			{

				// check existence of all spectral files
				SpectralFiles sfs = new SpectralFiles();

				ArrayList<SpectralFile> spectral_light_file_list = new ArrayList<SpectralFile>();

				// create lightweight objects
				ListIterator<SpectralFile> sf_li = spectral_file_list.listIterator();

				while(sf_li.hasNext()) {
					spec_file = sf_li.next();		
					SpectralFile light_clone = new SpectralFile(spec_file);
					light_clone.setHierarchyId(parent_id);
					light_clone.setCampaignId(campaign.getId());
					light_clone.setCampaignType(campaign.getType());	

					spectral_light_file_list.add(light_clone);
				}

				sfs.setSpectral_file_list(spectral_light_file_list);
				sfs.setCampaignId(campaign.getId());
				sfs.setCampaignType(campaign.getType());	

				boolean[] exists_array = specchio_client.spectralFilesExist(sfs);


				// insert spectral files

				sf_li = spectral_file_list.listIterator();

				int index = 0;

				while(sf_li.hasNext()) {
					spec_file = sf_li.next();		

					if (exists_array[index] == false)
					{

						SpectralFileInsertResult insert_result = new SpectralFileInsertResult();

						insert_result = insert_spectral_file(spec_file, parent_id);

						spectrum_counter += insert_result.getSpectrumIds().size();

						if(insert_result.getSpectrumIds().size() > 0) successful_file_counter++;

						insert_result.addErrors(spec_file.getFileErrors()); // compile into one list of errors							
						//				if(insert_result.getErrors().size() == 0) successful_file_counter++;

						// check on file errors
						if(insert_result.getErrors().size() > 0)
						{
							// concatenate all errors into one message
							StringBuffer buf = new StringBuffer("Issues found in " + spec_file.getFilename() + ":");

							for (SpecchioMessage error : insert_result.get_nonredudant_errors()) {

								buf.append("\n\t");

								buf.append(error.toString());
							}

							buf.append("\n");

							// add the message to the list of all errors
							this.file_errors.add(buf.toString());

						}	

						listener.campaignDataLoadFileCount(file_counter, spectrum_counter);



					}

					index = index + 1;

				}
			}

		} 
//		else {
//			listener.campaignDataLoadError(
//					"Unknown file types in directory " + dir.toString() + ". \n" +
//							"Data will not be loaded.\n" +
//							"Please check the file types and refer to the user guide for a list of supported files."
//					);
//		}


	}
	
	
	SpectralFileInsertResult insert_spectral_file(SpectralFile spec_file, int hierarchy_id) throws SPECCHIOClientException {
		
		SpectralFileInsertResult results = new SpectralFileInsertResult();
		
		// first check whether or not the file has already been loaded
		// to do this, create a clone of the spectral file, remove it's measurement to reduce size and send it 
		// to the web service
//		SpectralFile light_clone = new SpectralFile(spec_file);
//		light_clone.setHierarchyId(hierarchy_id);
//		light_clone.setCampaignId(campaign.getId());
//		light_clone.setCampaignType(campaign.getType());
//		
//		boolean exists = specchio_client.spectralFileExists(light_clone);
//		
//		// if it doesn't exist, upload it
//		if (!exists) {
			spec_file.setCampaignType(campaign.getType());
			spec_file.setCampaignId(campaign.getId());
			spec_file.setHierarchyId(hierarchy_id);
			results = specchio_client.insertSpectralFile(spec_file);
			
//			ids = new int[results.size()];
//			for (int i = 0; i < results.size(); i++) {
//				ids[i] = results.get(i);
//			}
//		} 
		
		return results;
		
	}

	public int insert_hierarchy(String name, Integer parent_id) throws SPECCHIOClientException {
		
		// see if the node already exists
		Integer id = specchio_client.getHierarchyId(campaign, name, parent_id);
		
		if (id == -1) {
			// the node doesn't exist; insert it
			id = specchio_client.insertHierarchy(campaign, name, parent_id);
		}
		
		return id;
	}

	SpectralFileLoader get_spectral_file_loader(ArrayList<File> files) throws SPECCHIOClientException {
		ArrayList<String> exts = new ArrayList<String>();

		// first thing we do is to get a distinct list of all file extensions
		ListIterator<File> li = files.listIterator();
		while(li.hasNext()) {
			String filename =li.next().getName();
			String[] tokens = filename.split("\\.");

			String ext = "";

			if (tokens.length < 2) {
				ext = null;
			} else {
				ext = tokens[tokens.length - 1]; // last element is the
													// extension
			}

			if (!exts.contains(ext))
				exts.add(ext);
		}
		
		// instantiate the appropriate kind of loader
		SpectralFileLoader loader = null;
		try {
			// cx if there are header files and slb (sli) files
			// in that case we got ENVI header and spectral libary files
			if (exts.contains("hdr")
					&& (exts.contains("slb") || exts.contains("sli")))
				loader = new ENVI_SLB_FileLoader();

			// cx for APOGEE files
			else if (exts.contains("TRM"))
				loader = new APOGEE_FileLoader();
			
			else if (exts.contains("xls"))
				loader = new XLS_FileLoader();		
			
			// cx for Spectral Evoluation files
			else if (exts.contains("sed"))
				loader = new Spectral_Evolution_FileLoader();		
			
			
			// cx for UNISPEC SPT files
			else if (exts.contains("SPT"))
				loader = new UniSpec_FileLoader();		
			
			
			// cx for UNISPEC SPU files
			else if (exts.contains("spu") || exts.contains("SPU"))
				loader = new UniSpec_SPU_FileLoader();					

			// cx for MFR out files
			else if (exts.contains("OUT"))
				loader = new MFR_FileLoader();

			// cx for HDF FGI out files
			else if (exts.contains("h5"))
				loader = new HDF_FGI_FileLoader();
			
			// cx for MODTRAN albedo input dat files
			else if (exts.contains("dat"))
				loader = new ModtranAlbedoFileLoader();	
			
			else {
	
				// those were the easy cases, now we need to start looking into the
				// files. For this, use the first file in the list.
				FileInputStream file_input = null;
				DataInputStream data_in = null;
	
				file_input = new FileInputStream(files.get(0));
				data_in = new DataInputStream(file_input);
				String line, line2, line3, line4;
	
				// use buffered stream to read lines
				BufferedReader d = new BufferedReader(
						new InputStreamReader(data_in));
				line = d.readLine();
				line2 = d.readLine();
				line3 = d.readLine();
				line4 = d.readLine();
				
	
				// cx for JAZ (Ocean Optics files)
				if (exts.contains("txt") && "SpectraSuite Data File".equals(line)) {
					loader = new JAZ_FileLoader();
				}
	
				// cx for SpectraSuite OO (Ocean Optics files)
				else if (exts.contains("csv")
						&& ("SpectraSuite Data File".equals(line) ||
								"SpectraSuite Data File\t".equals(line))) {
					loader = new OO_FileLoader();
	
				}
				
				
				// cx for Microtops TXT file
				else if ((exts.contains("csv") || exts.contains("TXT"))
						&& line.substring(0, 4).equals("REC#")
						&& line2.equals("FIELDS:")
						) {
					loader = new Microtops_FileLoader();
	
				}
								
				
				// cx for Ocean View TXT (Ocean Optics files produced by Ocean View Software)
				else if (exts.contains("txt")
						&& (line.contains("Data from")) 
						&& (line.contains("Node"))) {
					loader = new OceanView_FileLoader();
	
				}				
	
				// cx for COST OO CSV file format
				else if (exts.contains("csv")
						&& ("Wl; WR; S; Ref; Info;wl; mri_counts;gains;mri_irradiance".equals(line)
								|| "Wl; WR; S; Ref; Info".equals(line) ||
									"Sample;solar_local;DOY.dayfraction;SZA (deg);SAA (deg);COS(SZA);Qs quality_WR_stability;Ql quality_WR_level;Qd quality_WR_S_difference;Qh quality_WR;Qsat quality_WR;totalQ;Qwl_cal;wl of min L(Ha);wl of min L(O2B);wl of min L(O2A);Lin@400nm;Lin@500nm;Lin@600nm;Lin@680nm;Lin@O2-B;Lin@700nm;Lin@747.5 same as CF;Lin@753_broad;Lin@O2-A;Lin@800nm;Lin@890nm;Lin@990nm;PRI;R531;R570;Lin@643;CF@F656;NF@F656;Lin@680;CF@F687;NF@F687;Lin@753;CF@F760;NF@F760;R680;R800;SR(800,680);ND(800,680);ND(750,705);ND(858.5,645);ND(531,555);ND(531,551);ND(531,645);ND(800,550);SIPI(800,680,445);PSRI(680,500,750);NPQI(415,435);TVI(800,550,680);SR(740,720);GRI;SAVI;MSAVI;OSAVI;MTCI;RVI;WDVI;EVI;GEMI;BI;MODIS_PRI4;MODIS_PRI12;MODIS_PRI1;MODIS_NDVI;MODIS_EVI;R_blu_MODIS;R_green_MODIS;R_nir_MODIS;R_rep_MERIS;R_nir_MERIS;WI(900,970);ND(410,710);ND(530,570);ND(550,410);ND(720,420);ND(542,550);WC_R/(R+G+B);WC_G/(R+G+B);WC_B/(R+G+B);WC_GEI=2*G-(R+B);PPFDsum(umol m-2s-1);PPFDinteg(umol m-2s-1);fAPAR+fTPAR sum;fAPAR+fTPAR^2*Rsoil integ;n of Nan in Wr;perc. of Nan in Wr;n of Nan in S;perc. of Nan in S".equals(line))) {
					loader = new COST_OO_FileLoader();
	
				}
				
				
	
				// cx for TXT (ENVI format) files
				else if (exts.contains("txt") || exts.contains("TXT")) {
					loader = new TXT_FileLoader();
				}
	
				// cx for Spectra Vista HR-1024 files
				else if (exts.contains("sig")
						&& ("/*** Spectra Vista HR-1024 ***/".equals(line) ||
								"/*** Spectra Vista SIG Data ***/".equals(line))) {
					loader = new Spectra_Vista_HR_1024_FileLoader();
				}
	
				// cx if we got GER files
				else if ("///GER SIGNATUR FILE///".equals(line)) {
				loader = new GER_FileLoader();
				}
	
				// cx if we got ASD files with the new file format (Indico Version
				// 7)
				else if (exts.contains("asd")) {
					loader = new ASD_FileFormat_V7_FileLoader();
				}
	
				// cx for SPECPR files (no extensions)
				else if (line != null && line.contains("SPECPR")) {
					loader = new SPECPR_FileLoader();
				}
	
				d.close();
				file_input.close();
				data_in.close();

				if (loader == null) {
					// cx if we got ASD files
					// to do this we open randomly the first file and read an ASD header
					file_input = new FileInputStream(files.get(0));
					data_in = new DataInputStream(file_input);
					ASD_FileLoader asd_loader = new ASD_FileLoader();
					SpectralFile sf = asd_loader.asd_file;
		
					asd_loader.read_ASD_header(data_in, sf);
		
					if (sf.getCompany().equals("ASD")) {
						loader = new ASD_FileLoader();
					}
		
					file_input.close();
					data_in.close();
				}
				
				if (loader == null) {
					// cx if we got new ASD files without the proper ending: the case for calibration files
					// to do this we open randomly the first file and read an ASD header

					ASD_FileFormat_V7_FileLoader asd_loader = new ASD_FileFormat_V7_FileLoader();
		
					SpectralFile sf = asd_loader.load(files.get(0));
		
					if (sf.getCompany().equals("ASD")) {
						loader = new ASD_FileFormat_V7_FileLoader();
					}
		
					file_input.close();
					data_in.close();
				}
				
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MetaParameterFormatException e) {
			e.printStackTrace();
		} catch (org.joda.time.IllegalFieldValueException e) {
			e.printStackTrace();
		}
		
		// set the file format id according to the file format name and the database
		if (loader != null) {
			int file_format_id = specchio_client.getFileFormatId(loader.get_file_format_name());
			loader.set_file_format_id(file_format_id);
		}

		return loader;
	}


}
