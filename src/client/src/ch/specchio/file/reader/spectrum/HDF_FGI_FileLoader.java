package ch.specchio.file.reader.spectrum;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.TimeZone;

import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.Metadata;
import ch.specchio.types.SpectralFile;
import ch.specchio.types.spatial_pos;
import ch.specchio.file.reader.spectrum.hd5.*;

public class HDF_FGI_FileLoader extends SpectralFileLoader {

	SpectralFile hdf5_fgi_file;
	public String version = null;
	String xml_vers = null;
	boolean hdrf_brf_combined = false;
	String BRF_or_HDRF = "";
	int no_of_brf = 0;
	int no_of_hdrf = 0;
	boolean info_as_xml = false; // needed for 2010a data structure version, if
									// info file is stored as an xml file!

	public HDF_FGI_FileLoader() {
		super("HDF5 FGI");

	}

	@Override
	public SpectralFile load(File file) throws IOException, MetaParameterFormatException {

		if (file.getName().contains("Info"))
			return null; // ignore info files
		if (file.getName().endsWith(".xml"))
			return null; // ignore .xml files

		// searching for an xml file and retrieve Version number

		File[] xml_file = xml_finder(file.getParent());
		if (xml_file.length > 0 && xml_file[0].getName().contains("Info.lib2")) {
			info_as_xml = true;
		}

		hdf5_fgi_file = new SpectralFile();

		hdf5_fgi_file.setCompany("FGI");
		hdf5_fgi_file.setSpectrumNameType("Common");

		hdf5_fgi_file.setPath(file.getAbsolutePath());
		hdf5_fgi_file.setFilename(file.getName());
		hdf5_fgi_file.setFileFormatName(this.file_format_name);

		hdf5_fgi_file.setSharedMetadataForMultipleSpectra(true);

		String[] filename_tokens = hdf5_fgi_file.getBasename().split("\\.");
		BRF_or_HDRF = filename_tokens[1];

		if (xml_file.length < 1 || info_as_xml == true) {
			try {
				DataStructureVersion ds_vers = new DataStructureVersion();
				ds_vers.ReadDataStructureVersion(hdf5_fgi_file.getPath());
				version = ds_vers.DStruct_Version.trim();
			}
			catch (HDF5LibraryException ex) {
				throw new IOException(ex);
			}
			catch (UnsatisfiedLinkError ex) {
				throw new IOException("HDF5 files were found, but the HDF5 libraries are not installed.", ex);
			}
		}

		if (version.equalsIgnoreCase("lib2_data_2010a")) {

			// open the compound dataset and read the data
			compound_dataset ds = new compound_dataset();

			ds.ReadDataset(hdf5_fgi_file.getPath(), version);
			ds.ReadWavelength(hdf5_fgi_file.getPath());
			ds.ReadDataStructureVersion(hdf5_fgi_file.getPath());
			hdf5_fgi_file.setNumberOfSpectra(ds.GetNoOfSpectra());
			hdf5_fgi_file.setFgiDataStructureVersion(version);
			hdf5_fgi_file.setFgiHdrfBrfCombined(false);

			// open the info file and retrive the data

			String filename = file.getName();
			String[] tokens = filename.split("\\.");
			tokens[1] = "Info";
			if (info_as_xml == true) {
				tokens[3] = "xml";
			}
			String info_filename = arrayToString2(tokens, ".");
			int end_of_path_index = file.getAbsolutePath().lastIndexOf(
					File.separator);
			File info_file = new File(file.getAbsolutePath().substring(0,
					end_of_path_index + 1)
					+ info_filename);

			String info_file_name = info_file.getAbsolutePath();

			Read_Info_File info = new Read_Info_File();

			if (info_as_xml == false) {
				info.readAuthors(info_file_name);
				info.readMeasurement_Group(info_file_name);
				info.readRawData_Group(info_file_name);
				info.readTarget_Group(info_file_name);

				Information inf = info.inf.get(0);

				hdf5_fgi_file.setAuthors(inf.Authors);
				hdf5_fgi_file.setFgiComments(inf.Comments);
				hdf5_fgi_file.setCampaignName(inf.Campaign.trim());
				hdf5_fgi_file.setSinglePos(inf.gps_pos);
				if (inf.gps_pos == null && inf.Location != null) {
					hdf5_fgi_file.setSinglePos(new spatial_pos());
					hdf5_fgi_file.getSinglePos().location_name = inf.Location
							.trim();
				}
				// fill array
				
				spatial_pos p = hdf5_fgi_file.getSinglePos();
				p.location_name = inf.Location.trim();
				
				for (int i = 0; i < ds.GetNoOfSpectra(); i++) {
					hdf5_fgi_file.addPos(p);
					hdf5_fgi_file.addSpectrumName(inf.Name.trim());
				}

				hdf5_fgi_file.setInstrumentExtendedName(inf.Instrument_Name
						.trim());
				String[] inst_name_tokens = inf.Instrument_Name.split("_");
				hdf5_fgi_file.setInstrumentName(inst_name_tokens[0].trim());
				hdf5_fgi_file.setInstrumentParameters(new String[info.inst_param_dims]);
				hdf5_fgi_file.setInstrumentParameters(inf.Instrument_Parameters);
				hdf5_fgi_file.setLightSource(inf.Light_Source.trim());
				if (inf.Light_Source.equalsIgnoreCase("FGI_OrielQTH"))
					hdf5_fgi_file.setLightSource("Oriel 1000 W QTH");
				if (inf.Light_Source.equalsIgnoreCase("FGI_Makita"))
					hdf5_fgi_file.setLightSource("Makita spectrometer flash light (ASD)");
				if (!inf.Light_Source.endsWith("Sun"))
					hdf5_fgi_file.setLightSourceParam(inf.Light_Source_Param
							.trim());
				hdf5_fgi_file.setLocation(inf.Location.trim());

				if (inf.Weather != null && inf.Weather.contains("/8"))
					hdf5_fgi_file.setWeather(inf.Weather);
				else
					hdf5_fgi_file.setWeatherComment(inf.Weather);

				// Get information about sampling environment
				if (inf.Weather != null)
					hdf5_fgi_file.setSamplingEnvironment("Field");
				else
					hdf5_fgi_file.setSamplingEnvironment("Laboratory");

				hdf5_fgi_file.setWhiteReferenceTarget(inf.White_Reference_Target
						.trim());
				hdf5_fgi_file.setRawDataFormat(inf.RawDataFormat.trim());
				hdf5_fgi_file.setSourceFile(inf.SourceFile.replace('\\', '/')
						.trim());
				hdf5_fgi_file.setTargetDescription(inf.Description.trim());
				hdf5_fgi_file.setKeywords(new String[info.keyword_dims]);
				hdf5_fgi_file.setKeywords(inf.Keywords);
				String[] i_token = inf.Instrument_Parameters[0].split(",");
				String Unit = i_token[1]
						.substring(i_token[1].indexOf(' ', 2) + 1);
				hdf5_fgi_file.setInstrumentNumber(Unit);
				String Calibration = i_token[2].substring(
						i_token[2].indexOf(" ", 2) + 1).trim();
				hdf5_fgi_file.setCalibrationSeries(Integer
						.parseInt(Calibration));
				String[] distance_tokens = inf.Instrument_Parameters[3]
						.split(" ");
				float arm = Float.parseFloat(distance_tokens[1]);
				hdf5_fgi_file.setArmLengths(new Float[ds.GetNoOfSpectra()]);
				String[] optics_token = inf.Instrument_Parameters[1].split(":");
				hdf5_fgi_file.setFgiOptics(optics_token[1].trim());

				// fill array of armlength
				for (int i = 0; i < ds.GetNoOfSpectra(); i++) {
					hdf5_fgi_file.setArmLength(i, arm / 100);

				}
				String[] az_sens_token = inf.Instrument_Parameters[2]
						.split(" ");
				hdf5_fgi_file.setAzimuthSensorType(az_sens_token[1].trim());

				// for (int i = 0; i < ds.no_of_measurements; i++) {
				// hdf5_fgi_file.azimuth_sensor_type[i] =
				// az_sens_token[1].trim();
				// }
				if (!inf.Instrument_Parameters[1].contains("Other")) {
					String[] foreoptics_token = inf.Instrument_Parameters[1]
							.split("_");
					String foreopt = foreoptics_token[1].substring(0, 1);
					int foreoptic = Integer.parseInt(foreopt);
					hdf5_fgi_file.setForeopticDegrees(foreoptic);
				}
			} else {
				info.read_info_as_xml(info_file_name);

				hdf5_fgi_file.setAuthors(info.authors);
				hdf5_fgi_file.setCampaignName(info.campaign.trim());
				hdf5_fgi_file.setFgiComments(info.comments);
				hdf5_fgi_file.setSinglePos(info.gps);
				if (info.gps == null && info.location != null) {
					hdf5_fgi_file.setSinglePos(new spatial_pos());
					hdf5_fgi_file.getSinglePos().location_name = info.location
							.trim();
				}
				// fill array
				spatial_pos p = hdf5_fgi_file.getSinglePos();
				p.location_name = info.location.trim();
				for (int i = 0; i < ds.GetNoOfSpectra(); i++) {

					hdf5_fgi_file.addPos(p);
					hdf5_fgi_file.addSpectrumName(info.name.trim());
				}

				hdf5_fgi_file.setInstrumentExtendedName(info.instrument_name
						.trim());
				String[] inst_name_tokens = info.instrument_name.split("_");
				hdf5_fgi_file.setInstrumentName(inst_name_tokens[0].trim());
				hdf5_fgi_file.setInstrumentParameters(new String[info.inst_param_dims]);
				hdf5_fgi_file.setInstrumentParameters(info.instrument_parameters);
				hdf5_fgi_file.setLightSource(info.light_source.trim());
				if (info.light_source.equalsIgnoreCase("FGI_OrielQTH"))
					hdf5_fgi_file.setLightSource("Oriel 1000 W QTH");
				if (info.light_source.equalsIgnoreCase("FGI_Makita"))
					hdf5_fgi_file.setLightSource("Makita spectrometer flash light (ASD)");
				if (!info.light_source.endsWith("Sun"))
					hdf5_fgi_file.setLightSourceParam(info.light_source_param
							.trim());
				hdf5_fgi_file.setLocation(info.location.trim());

				if (info.weather != null && info.weather.contains("/8"))
					hdf5_fgi_file.setWeather(info.weather);
				else
					hdf5_fgi_file.setWeatherComment(info.weather);

				// Get information about sampling environment
				if (info.weather != null)
					hdf5_fgi_file.setSamplingEnvironment("Field");
				else
					hdf5_fgi_file.setSamplingEnvironment("Laboratory");

				hdf5_fgi_file.setWhiteReferenceTarget(info.white_reference_target
						.trim());
				hdf5_fgi_file.setRawDataFormat(info.raw_data_format.trim());
				hdf5_fgi_file.setSourceFile(info.source_file.replace('\\', '/')
						.trim());
				hdf5_fgi_file.setTargetDescription(info.description.trim());
				hdf5_fgi_file.setKeywords(new String[info.keyword_dims]);
				hdf5_fgi_file.setKeywords(info.keywords);
				String[] i_token = info.instrument_parameters[0].split(",");
				String Unit = i_token[1]
						.substring(i_token[1].indexOf(' ', 2) + 1);
				hdf5_fgi_file.setInstrumentNumber(Unit);
				String Calibration = i_token[2].substring(
						i_token[2].indexOf(" ", 2) + 1).trim();
				hdf5_fgi_file.setCalibrationSeries(Integer
						.parseInt(Calibration));
				String[] distance_tokens = info.instrument_parameters[3]
						.split(" ");
				float arm = Float.parseFloat(distance_tokens[1]);
				hdf5_fgi_file.setArmLengths(new Float[ds.GetNoOfSpectra()]);

				// fill array of armlength
				for (int i = 0; i < ds.GetNoOfSpectra(); i++) {
					hdf5_fgi_file.setArmLength(i, arm / 100);

				}
				String[] az_sens_token = info.instrument_parameters[2]
						.split(" ");
				hdf5_fgi_file.setAzimuthSensorType(az_sens_token[1].trim());

				// for (int i = 0; i < ds.no_of_measurements; i++) {
				// hdf5_fgi_file.azimuth_sensor_type[i] =
				// az_sens_token[1].trim();
				// }
				if (!info.instrument_parameters[1].contains("Other")) {
					String[] foreoptics_token = info.instrument_parameters[1]
							.split("_");
					String foreopt = foreoptics_token[1].substring(0, 1);
					int foreoptic = Integer.parseInt(foreopt);
					hdf5_fgi_file.setForeopticDegrees(foreoptic);
				}

				String[] optics_token = info.instrument_parameters[1]
						.split(":");
				hdf5_fgi_file.setFgiOptics(optics_token[1].trim());

			}
			
			

			// construct spectra names from file name if
			// there is more than one spectrum in the file
			if (hdf5_fgi_file.getNumberOfSpectra() > 1)
				for (int i = 0; i < hdf5_fgi_file.getNumberOfSpectra(); i++) {
					hdf5_fgi_file.addSpectrumFilename(hdf5_fgi_file.getBasename()
							+ "_" + Integer.toString(i));
//					hdf5_fgi_file.spectra_numbers[i] = i + 1; // simple auto
																// numbering
				}
			else // use the body name as spectrum name
			{
				hdf5_fgi_file.addSpectrumFilename(hdf5_fgi_file.getBasename()
						+ hdf5_fgi_file.getExt());
//				hdf5_fgi_file.spectra_numbers[0] = 1;

				// concat with spectrum name if available
				if (hdf5_fgi_file.getNumberOfSpectraNames() == hdf5_fgi_file
						.getNumberOfSpectra()) {
					hdf5_fgi_file.setSpectrumFilename(0, hdf5_fgi_file.getSpectrumFilename(0)
							+ " " + hdf5_fgi_file.getSpectrumFilename(0));
				}
			}

			// // fill name array
			// if (!hdf5_fgi_file.target_description.equals("")) {
			// hdf5_fgi_file.number_of_spectra_names = ds.no_of_measurements;
			//
			// for (int i = 0; i < ds.no_of_measurements; i++) {
			// hdf5_fgi_file.spectra_names[i] =
			// hdf5_fgi_file.target_description;
			// }
			//
			// }

			// create data structures in the spectral file
			Measurement m = ds.m.get(0);

			hdf5_fgi_file.addNumberOfChannels(m.refl_size);
			hdf5_fgi_file.setMeasurements(new Float[ds.GetNoOfSpectra()][hdf5_fgi_file.getNumberOfChannels(0)]);
//			hdf5_fgi_file.illumination_azimuth = new Float[ds.GetNoOfSpectra()];
//			hdf5_fgi_file.illumination_zenith = new Float[ds.GetNoOfSpectra()];
//			hdf5_fgi_file.sensor_azimuth = new Float[ds.GetNoOfSpectra()];
//			hdf5_fgi_file.sensor_zenith = new Float[ds.GetNoOfSpectra()];
			hdf5_fgi_file.setMeasurementIds(new int[ds.GetNoOfSpectra()]);
			hdf5_fgi_file.addWvls(new Float[hdf5_fgi_file.getNumberOfChannels(0)]);

			// fill data into spectral file fields
			// ArrayListIterator li = new ds.m.ArrayListIterator
			ListIterator<Measurement> li = ds.m.listIterator();

			hdf5_fgi_file.setWvls(0, ds.Wavelength);

			int i = 0;
			while (li.hasNext()) {
				m = li.next();

				fill_angles_date_and_id(hdf5_fgi_file, i, m, m.Reflectance);

				hdf5_fgi_file.addMeasurementUnits(1); // reflectance by
														// default

				// hdf5_fgi_file.sensor_azimuth[i] = m.SensorAz;
				// hdf5_fgi_file.sensor_zenith[i] = m.SensorZen;
				// hdf5_fgi_file.illumination_azimuth[i] = m.LightAz;
				// hdf5_fgi_file.illumination_zenith[i] = m.LightZen;
				//
				// hdf5_fgi_file.measurement_id[i] = m.MeasurementID;
				//
				// hdf5_fgi_file.capture_dates[i] = get_time(m);
				//
				// hdf5_fgi_file.measurements[i] = m.Reflectance;

				i++;

				if (m.hasQandU()) {
					fill_angles_date_and_id(hdf5_fgi_file, i, m, m.ReflectanceQ);
					hdf5_fgi_file.setMeasurementUnits(i, 110);
					i++;
					fill_angles_date_and_id(hdf5_fgi_file, i, m, m.ReflectanceU);
					hdf5_fgi_file.setMeasurementUnits(i, 120);
					i++;
				}

			}

			if (ds.m.get(0).hasQandU())
				hdf5_fgi_file.setSpectralGroupingSize(3); // I/Q/U data in
															// matrix
			
//			String[] reference = get_references(hdf5_fgi_file.keywords, hdf5_fgi_file.capture_dates, hdf5_fgi_file.campaign_name);
			

		} else if (xml_file.length < 1 && version.equalsIgnoreCase("FGI_2008a")) {
			fgi_2008a ds = new fgi_2008a();

			ds.read_Data(hdf5_fgi_file.getPath(), version);
			ds.read_Data_Structure_Version(hdf5_fgi_file.getPath());
			ds.read_Info_Group(hdf5_fgi_file.getPath());
			ds.read_Measurement_Group(hdf5_fgi_file.getPath());
			ds.readRawData_Group(hdf5_fgi_file.getPath());
			ds.readTarget_Group(hdf5_fgi_file.getPath());
			hdf5_fgi_file.setNumberOfSpectra(ds.no_of_measurements);
			hdf5_fgi_file.setFgiDataStructureVersion(version);
			hdrf_brf_combined = true;
			hdf5_fgi_file.setFgiHdrfBrfCombined(hdrf_brf_combined);

			Information inf = ds.inf.get(0);

			if (inf.Authors != null)
				hdf5_fgi_file.setAuthors(inf.Authors);
			if (inf.Comments != null)
				hdf5_fgi_file.setFgiComments(inf.Comments);
			if (inf.Campaign != null)
				hdf5_fgi_file.setCampaignName(inf.Campaign.trim());
			if (inf.gps_pos != null)
				hdf5_fgi_file.setSinglePos(inf.gps_pos);
			if (inf.gps_pos == null && inf.Location != null) {
				hdf5_fgi_file.setSinglePos(new spatial_pos());
				hdf5_fgi_file.getSinglePos().location_name = inf.Location.trim();
			}

			// fill array
			spatial_pos p = hdf5_fgi_file.getSinglePos();
			p.location_name = inf.Location.trim();			
			for (int i = 0; i < ds.no_of_measurements; i++) {
				hdf5_fgi_file.addPos(p);
				hdf5_fgi_file.addMeasurementUnits(1); // reflectance by
														// default
				hdf5_fgi_file.addSpectrumName(inf.Name.trim());
			}

			hdf5_fgi_file.setInstrumentExtendedName(inf.Instrument_Name.trim());
			String[] inst_name_tokens = inf.Instrument_Name.split("_");
			hdf5_fgi_file.setInstrumentName(inst_name_tokens[0].trim());
			hdf5_fgi_file.setInstrumentParameters(new String[ds.inst_param_dims]);
			hdf5_fgi_file.setInstrumentParameters(ds.Instrument_Parameters);
			hdf5_fgi_file.setLightSource(inf.Light_Source.trim());
			if (inf.Light_Source.equalsIgnoreCase("FGI_OrielQTH"))
				hdf5_fgi_file.setLightSource("Oriel 1000 W QTH");
			if (inf.Light_Source.equalsIgnoreCase("FGI_Makita"))
				hdf5_fgi_file.setLightSource("Makita spectrometer flash light (ASD)");
			if (!inf.Light_Source.endsWith("Sun")
					&& inf.Light_Source_Param != null)
				hdf5_fgi_file.setLightSourceParam(inf.Light_Source_Param
						.trim());
			if (inf.Location != null)
				hdf5_fgi_file.setLocation(inf.Location.trim());

			if (inf.Weather != null && inf.Weather.contains("/8") && inf.Weather.indexOf("/") < 3)
				hdf5_fgi_file.setWeather(inf.Weather);
			else if(inf.Weather != null && inf.Weather.contains("/8") && inf.Weather.indexOf("/") > 3){
				int index = inf.Weather.indexOf("/");
				hdf5_fgi_file.setWeather(inf.Weather.substring(index - 1, index + 2).trim());
				hdf5_fgi_file.setWeatherComment(inf.Weather.substring(0, index - 2).trim());
			}
			else
				hdf5_fgi_file.setWeatherComment(inf.Weather);

			// if (inf.Weather != null) {
			// hdf5_fgi_file.weather = inf.Weather;
			// if (inf.Weather.contains("perfect"))
			// hdf5_fgi_file.weather = "0/8";
			// }

			if (inf.Weather != null)
				hdf5_fgi_file.setSamplingEnvironment("Field");
			else
				hdf5_fgi_file.setSamplingEnvironment("Laboratory");

			if (inf.White_Reference_Target != null)
				hdf5_fgi_file.setWhiteReferenceTarget(inf.White_Reference_Target
						.trim());
			if (inf.Processing != null)
				hdf5_fgi_file.setProcessing(inf.Processing.trim());
			if (inf.RawDataFormat != null)
				hdf5_fgi_file.setRawDataFormat(inf.RawDataFormat.trim());
			if (inf.SourceFile != null)
				hdf5_fgi_file.setSourceFile(inf.SourceFile.replace('\\', '/'));
			if (inf.Description != null)
				hdf5_fgi_file.setTargetDescription(inf.Description.trim());
			if (inf.Keywords != null) {
				hdf5_fgi_file.setKeywords(new String[ds.Keyword_dims]);
				hdf5_fgi_file.setKeywords(inf.Keywords);
			}
			if (inf.Instrument_Parameters != null) {
				String[] i_token = inf.Instrument_Parameters[0].split(",");
				String Unit = i_token[1]
						.substring(i_token[1].indexOf(' ', 2) + 1);
				hdf5_fgi_file.setInstrumentNumber(Unit);
				String Calibration = i_token[2].substring(
						i_token[2].indexOf(" ", 2) + 1).trim();
				hdf5_fgi_file.setCalibrationSeries(Integer
						.parseInt(Calibration));
				String[] distance_tokens = inf.Instrument_Parameters[3]
						.split(" ");
				float arm = Float.parseFloat(distance_tokens[1]);
				hdf5_fgi_file.setArmLengths(new Float[ds.no_of_measurements]);

				// fill array of armlength
				for (int i = 0; i < ds.no_of_measurements; i++) {
					hdf5_fgi_file.setArmLength(i, arm / 100);

				}
				String[] az_sens_token = inf.Instrument_Parameters[2]
						.split(" ");
				hdf5_fgi_file.setAzimuthSensorType(az_sens_token[1].trim());

				// for (int i = 0; i < ds.no_of_measurements; i++) {
				// hdf5_fgi_file.azimuth_sensor_type[i] = az_sens_token[1];
				// }
				if (!inf.Instrument_Parameters[1].contains("Other")) {
					String[] foreoptics_token = inf.Instrument_Parameters[1]
							.split("_");
					String foreopt = foreoptics_token[1].substring(0, 1);
					int foreoptic = Integer.parseInt(foreopt);
					hdf5_fgi_file.setForeopticDegrees(foreoptic);
				}

				String[] optics_token = inf.Instrument_Parameters[1].split(":");
				hdf5_fgi_file.setFgiOptics(optics_token[1].trim());
			}

			// // construct spectra names from file name if
			// // there is more than one spectrum in the file
			// if (hdf5_fgi_file.getNumberOfSpectra() > 1)
			// for (int i = 0; i < hdf5_fgi_file.getNumberOfSpectra(); i++) {
			// String[] tokens_getBasename = hdf5_fgi_file.getBasename.split("\\.");
			// if()
			// hdf5_fgi_file.spectra_filenames[i] =
			// tokens_getBasename[0].concat(".")
			// + "_";
			// hdf5_fgi_file.spectra_numbers[i] = i + 1; // simple auto
			// // numbering
			// hdf5_fgi_file.spectra_filenames[i] =
			// hdf5_fgi_file.spectra_filenames[i]
			// .concat(Integer.toString(i));
			// }
			// else // use the body name as spectrum name
			// {
			// hdf5_fgi_file.spectra_filenames[0] = hdf5_fgi_file.getBasename
			// + hdf5_fgi_file.ext;
			// hdf5_fgi_file.spectra_numbers[0] = 1;
			//
			// // concat with spectrum name if available
			// if (hdf5_fgi_file.number_of_spectra_names == hdf5_fgi_file
			// .getNumberOfSpectra()) {
			// hdf5_fgi_file.spectra_filenames[0] =
			// hdf5_fgi_file.spectra_filenames[0]
			// .concat(" ");
			// hdf5_fgi_file.spectra_filenames[0] =
			// hdf5_fgi_file.spectra_filenames[0]
			// .concat(hdf5_fgi_file.spectra_names[0]);
			// }
			// }

			// fill name array
			// if (!hdf5_fgi_file.target_description.equals("")) {
			// hdf5_fgi_file.number_of_spectra_names = ds.no_of_measurements;
			//
			// for (int i = 0; i < ds.no_of_measurements; i++) {
			// hdf5_fgi_file.spectra_names[i] = hdf5_fgi_file.target_description
			// .trim();
			// }
			//
			// }
			// create data structures in the spectral file
			Measurement m = ds.m.get(0);

			hdf5_fgi_file.addNumberOfChannels(m.refl_size);
			hdf5_fgi_file.setMeasurements(new Float[ds.no_of_measurements][hdf5_fgi_file.getNumberOfChannels(0)]);
			hdf5_fgi_file.setMeasurementIds(new int[ds.no_of_measurements]);
			hdf5_fgi_file.addWvls(new Float[hdf5_fgi_file.getNumberOfChannels(0)]);
			if (m.polarization != null)
				hdf5_fgi_file.setPolarizations(new String[ds.no_of_measurements]);
			if (m.polarization_dir != null)
				hdf5_fgi_file.setPolarizationDirections(new Double[ds.no_of_measurements]);
			hdf5_fgi_file.setSpectrumTypes(new String[ds.no_of_measurements]);
			hdf5_fgi_file.setFgiHdrfBrfFlags(new String[ds.no_of_measurements]);

			hdf5_fgi_file.setFgiCalculationProcesses(new String[ds.no_of_spec_types][ds.no_of_calc_proc]);

			// fill data into spectral file fields
			// ArrayListIterator li = new ds.m.ArrayListIterator
			ListIterator<Measurement> li = ds.m.listIterator();

			hdf5_fgi_file.setWvls(0, ds.Wavelength);
			hdf5_fgi_file.setFgiCalculationProcesses(ds.calculationProcesses);

			int i = 0;
			while (li.hasNext()) {
				m = li.next();

				hdf5_fgi_file.addIlluminationZenith(m.LightZen);
				hdf5_fgi_file.addIlluminationAzimuth(m.LightAz);
				hdf5_fgi_file.addSensorZenith(m.SensorZen);
				hdf5_fgi_file.addSensorAzimuth(m.SensorAz);

				hdf5_fgi_file.setMeasurementId(i, m.MeasurementID);

				if (m.UTC_Time != null)
					hdf5_fgi_file.setCaptureDate(i, get_time(m));
				else
					hdf5_fgi_file.setCaptureDate(i, get_time(inf.Measurement_Time));

				if (m.polarization != null && m.polarization_dir == null) {
					hdf5_fgi_file.setPolarization(i, m.polarization);
					if (hdf5_fgi_file.getPolarization(i).equals("H"))
						hdf5_fgi_file.setPolarization(i, "Horizontal");
					if (hdf5_fgi_file.getPolarization(i).equals("V"))
						hdf5_fgi_file.setPolarization(i, "Vertical");
					if (hdf5_fgi_file.getPolarization(i).equals("\\"))
						hdf5_fgi_file.setPolarization(i, "\\ ");
				}
				if (m.polarization_dir != null)
					hdf5_fgi_file.setPolarizationDirection(i, m.polarization_dir
							.doubleValue());

				hdf5_fgi_file.setMeasurement(i, m.Reflectance);

				hdf5_fgi_file.setSpectrumType(i, m.spectrum_type);
				hdf5_fgi_file.setFgiHdrfBrfFlag(i, m.spectrum_type);

				if (hdf5_fgi_file.getFgiHdrfBrfFlag(i).equals("BRF"))
					hdf5_fgi_file.setMeasurementType(i, 2);
				if (hdf5_fgi_file.getFgiHdrfBrfFlag(i).equals("HDRF"))
					hdf5_fgi_file.setMeasurementType(i, 8);

				i++;
			}
			// construct spectra names from file name if
			// there is more than one spectrum in the file

			// Get no of hdrf and brf

			for (int j = 0; j < hdf5_fgi_file.getNumberOfSpectra(); j++) {
				if (hdf5_fgi_file.getFgiHdrfBrfFlag(j).equals("BRF"))
					no_of_brf++;
				else
					no_of_hdrf++;
			}

			// if (hdf5_fgi_file.getNumberOfSpectra() > 1) {
			// for (int j = 0; j < hdf5_fgi_file.getNumberOfSpectra(); j++) {
			// String[] tokens_getBasename = hdf5_fgi_file.getBasename
			// .split("\\.");
			// if (hdf5_fgi_file.getFgiHdrfBrfFlag[j].equals("BRF")) {
			// hdf5_fgi_file.spectra_filenames[j] = tokens_getBasename[0]
			// .concat(".BRF.").concat(tokens_getBasename[1])
			// .concat("_");
			// } else {
			// hdf5_fgi_file.spectra_filenames[j] = tokens_getBasename[0]
			// .concat(".HDRF.").concat(tokens_getBasename[1])
			// .concat("_");
			// }
			//
			// hdf5_fgi_file.spectra_numbers[j] = j + 1; // simple auto
			// // numbering
			// hdf5_fgi_file.spectra_filenames[j] =
			// hdf5_fgi_file.spectra_filenames[j]
			// .concat(Integer.toString(j));
			// }

			if (hdf5_fgi_file.getNumberOfSpectra() > 1) {
				int j = 0;
				for (int z = 0; z < hdf5_fgi_file.getNumberOfSpectra(); z++) {
					String[] tokens_getBasename = hdf5_fgi_file.getBasename()
							.split("\\.");
					if (hdf5_fgi_file.getFgiHdrfBrfFlag(z).equals("BRF")) {
						hdf5_fgi_file.addSpectrumFilename(tokens_getBasename[0] + ".BRF." + tokens_getBasename[1] + "_" + Integer.toString(j));
//						hdf5_fgi_file.spectra_numbers[z] = z + 1;

						if (j == no_of_brf - 1) {
							j = 0;
						} else {
							j++;
						}

					} else if (hdf5_fgi_file.getFgiHdrfBrfFlag(z)
							.equals("HDRF")) {
						hdf5_fgi_file.addSpectrumFilename(tokens_getBasename[0] + ".HDRF." + tokens_getBasename[1]+ "_" + Integer.toString(j));
//						hdf5_fgi_file.spectra_numbers[z] = z + 1;

						if (j == no_of_hdrf - 1) {
							j = 0;
						} else {
							j++;
						}

					}
				}

			} else // use the body name as spectrum name
			{
				String spec_name_addon = "";
				// concat with spectrum name if available
				if (hdf5_fgi_file.getNumberOfSpectraNames() == hdf5_fgi_file
						.getNumberOfSpectra()) {
					spec_name_addon = " " + hdf5_fgi_file.getSpectrumName(0);
				}				
				hdf5_fgi_file.addSpectrumFilename(hdf5_fgi_file.getBasename()
						+ hdf5_fgi_file.getExt() + spec_name_addon);
//				hdf5_fgi_file.spectra_numbers[0] = 1;


			}

		} else if (version == null && xml_file.length > 0
				&& info_as_xml == false) {

			fgi_beta0_8_12_2007 ds = new fgi_beta0_8_12_2007();

			ds.read_Data(hdf5_fgi_file.getPath());
			ds.read_info(xml_file[0].getAbsolutePath());
			hdf5_fgi_file.setNumberOfSpectra(ds.no_of_measurements);

			// TODO: Stimmt die Annahme zum Data Structure Version. Nicht
			// sicher, da keine explizite Angabe!
			hdf5_fgi_file.setFgiDataStructureVersion("Newest Version");

			// Information inf = ds.inf.get(0);

			hdf5_fgi_file.setAuthors(new String[1]);
			hdf5_fgi_file.setAuthor(0, ds.Measurer);
			String[] location_tokens = ds.Location.split(",");
			String[] date_tokens = ds.date.split("\\.");
			if (date_tokens[2].length() > 4) {
				String[] date_tokens2 = date_tokens[2].split(" ");
				hdf5_fgi_file.setCampaignName(date_tokens2[0]
						.concat(location_tokens[0]));
			} else {
				hdf5_fgi_file.setCampaignName(date_tokens[2]
						.concat(location_tokens[0]));
			}

			if (ds.date.length() < 11) {
				for (int i = 0; i < ds.no_of_measurements; i++)
					hdf5_fgi_file.setCaptureDate(i, get_short_time(ds.date));
				hdf5_fgi_file.setFgiFileLoaderComment("Time set to 00:00 as there is no time information available in spectral file");
			} else {
				for (int i = 0; i < ds.no_of_measurements; i++)
					hdf5_fgi_file.setCaptureDate(i, get_time(ds.date));
			}
			// TODO: else statement to cover case where they captured the whole
			// date with hours, minutes and seconds
			if (ds.gps_pos != null) {
				hdf5_fgi_file.setSinglePos(ds.gps_pos);
				hdf5_fgi_file.getSinglePos().location_name = ds.Location.trim();
				for (int i = 0; i < ds.no_of_measurements; i++) {
					hdf5_fgi_file.addPos(hdf5_fgi_file.getSinglePos());

				}
			}

			for (int i = 0; i < ds.no_of_measurements; i++) {
				hdf5_fgi_file.addMeasurementUnits(1); // reflectance by
														// default
			}

			hdf5_fgi_file.setInstrumentName(ds.Instrument_Name.trim());

			hdf5_fgi_file.setLightSource(ds.Light_Source.trim());
			// hdf5_fgi_file.location = ds.Location;

			if (ds.Weather.contains("/8"))
				hdf5_fgi_file.setWeather(ds.Weather);
			else
				hdf5_fgi_file.setWeatherComment(ds.Weather);

			hdf5_fgi_file.setWhiteReferenceTarget(ds.White_Reference_Target
					.trim());
			// hdf5_fgi_file.target_description = ds.Description.trim();
			if (ds.Description.contains("TARGET_1TARGET_2")) {
				String[] tokens = ds.Description.split("\t");
				hdf5_fgi_file.setTargetDescription(tokens[1].trim());
			} else {
				hdf5_fgi_file.setTargetDescription(ds.Description.trim());
			}
			hdf5_fgi_file.setKeywords(new String[ds.Keywords.length]);
			hdf5_fgi_file.setKeywords(ds.Keywords);
			hdf5_fgi_file.setArmLengths(new Float[ds.no_of_measurements]);
			// hdf5_fgi_file.processing = ds.Processing_one_string;
			hdf5_fgi_file.setProcessingList(new String[ds.Processing.length]);
			hdf5_fgi_file.setProcessingList(ds.Processing);
			// hdf5_fgi_file.FGI_datastructure_version = ds.Extra_info
			// .substring(1).trim();

			// TODO: Achtung: arm lŠnge kann nicht aus dem xml-file
			// herausgelesen werden, somit jetzt mit null abgespeichert.
			// fill array of armlength
			for (int i = 0; i < ds.no_of_measurements; i++) {
				hdf5_fgi_file.setArmLength(i, null);

			}

			// construct spectra names from file name if
			// there is more than one spectrum in the file
			if (hdf5_fgi_file.getNumberOfSpectra() > 1)
				for (int i = 0; i < hdf5_fgi_file.getNumberOfSpectra(); i++) {
					hdf5_fgi_file.addSpectrumFilename(hdf5_fgi_file.getBasename()
							+ "_" + Integer.toString(i));
//					hdf5_fgi_file.spectra_numbers[i] = i + 1; // simple auto
																// numbering
				}
			else // use the body name as spectrum name
			{
				
				String spec_name_addon = "";
				// concat with spectrum name if available
				if (hdf5_fgi_file.getNumberOfSpectraNames() == hdf5_fgi_file
						.getNumberOfSpectra()) {
					spec_name_addon = " " + hdf5_fgi_file.getSpectrumName(0);
				}						
				
				hdf5_fgi_file.addSpectrumFilename(hdf5_fgi_file.getBasename()
						+ hdf5_fgi_file.getExt() + spec_name_addon);
//				hdf5_fgi_file.spectra_numbers[0] = 1;


			}

			// fill name array

			hdf5_fgi_file.setNumberOfSpectraNames(ds.no_of_measurements);

			for (int i = 0; i < ds.no_of_measurements; i++) {
				hdf5_fgi_file.addSpectrumName(ds.title.trim());
			}

			// create data structures in the spectral file
			Measurement m = ds.m.get(0);

			hdf5_fgi_file.addNumberOfChannels(m.refl_size);
			hdf5_fgi_file.setMeasurements(new Float[ds.no_of_measurements][hdf5_fgi_file.getNumberOfChannels(0)]);
//			hdf5_fgi_file.illumination_azimuth = new Float[ds.no_of_measurements];
//			hdf5_fgi_file.illumination_zenith = new Float[ds.no_of_measurements];
//			hdf5_fgi_file.sensor_azimuth = new Float[ds.no_of_measurements];
//			hdf5_fgi_file.sensor_zenith = new Float[ds.no_of_measurements];

			// fill data into spectral file fields
			// ArrayListIterator li = new ds.m.ArrayListIterator
			ListIterator<Measurement> li = ds.m.listIterator();

			// hdf5_fgi_file.wvls = ds.Wavelength;

			int i = 0;
			while (li.hasNext()) {
				m = li.next();

				hdf5_fgi_file.addIlluminationAzimuth(m.LightAz);
				hdf5_fgi_file.addIlluminationZenith(m.LightZen);
				hdf5_fgi_file.addSensorAzimuth(m.SensorAz);
				hdf5_fgi_file.addSensorZenith(m.SensorZen);
				hdf5_fgi_file.setMeasurement(i, m.Reflectance);

				i++;
			}
			xml_file = null;

		}

		// ListIterator<Information> li_inf = info.inf.listIterator();
		//
		// int j = 0;
		//
		// while(li_inf.hasNext())
		// {
		// // hdf5_fgi_file.instrument_parameters[i] = inf.;
		//
		// j++;
		// }

		// generic specchio metadata filling
		fill_eav();

		return hdf5_fgi_file;
	}

	protected void fill_eav() throws MetaParameterFormatException{

//		String previous_BRF_HDRF_status = "unknown";
//		String previous_polarization_status = "unknown";
//		Double previous_polarization_dir_status = null;
//		if (hdf5_fgi_file.getFgiHdrfBrfFlags() != null)
//			previous_BRF_HDRF_status = hdf5_fgi_file.getFgiHdrfBrfFlag(0);
//		if (hdf5_fgi_file.getPolarizations() != null)
//			previous_polarization_status = hdf5_fgi_file.getPolarization(0);
//		if (hdf5_fgi_file.getPolarizationDirections() != null)
//			previous_polarization_dir_status = hdf5_fgi_file.getPolarizationDirection(0);

		for (int spec_no = 0; spec_no < hdf5_fgi_file.getNumberOfSpectra(); spec_no++) {

			Metadata smd = new Metadata();

			if (hdf5_fgi_file.getFgiDataStructureVersion() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("File Version"));
				mp.setValue(hdf5_fgi_file.getFgiDataStructureVersion(), "String");
				smd.add_entry(mp);
			}

			if (hdf5_fgi_file.getInstrumentExtendedName() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Extended Instrument Name"));
				mp.setValue(hdf5_fgi_file.getInstrumentExtendedName(), "String");
				smd.add_entry(mp);
			}

			if (hdf5_fgi_file.getCampaignName() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Campaign Name"));
				mp.setValue(hdf5_fgi_file.getCampaignName(), "String");
				smd.add_entry(mp);
			}

			if (hdf5_fgi_file.getAuthors() != null) {

				// String[] authors = hdf5_fgi_file.author.split(",");

				for (int i = 0; i < hdf5_fgi_file.getAuthors().length; i++) {
					MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Investigator"));
					mp.setValue(hdf5_fgi_file.getAuthor(i), "String");
					smd.add_entry(mp);
				}

			}
			if (hdf5_fgi_file.getLightSourceParam() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Light Source Parameters"));
				mp.setValue(hdf5_fgi_file.getLightSourceParam(), "String");
				smd.add_entry(mp);
			}
			if (hdf5_fgi_file.getTargetDescription() != null
					&& hdf5_fgi_file.getTargetDescription().length() > 1) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Target Description"));
				mp.setValue(hdf5_fgi_file.getTargetDescription(), "String");
				smd.add_entry(mp);
			}
			if (hdf5_fgi_file.getWhiteReferenceTarget() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("White Reference Panel Name"));
				mp.setValue(hdf5_fgi_file.getWhiteReferenceTarget(), "String");
				smd.add_entry(mp);
			}
			if (hdf5_fgi_file.getProcessing() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Processing Algorithm"));
				mp.setValue(hdf5_fgi_file.getProcessing(), "String");
				smd.add_entry(mp);
			}
			if (hdf5_fgi_file.getProcessingList() != null) {
				for (int i = 0; i < hdf5_fgi_file.getProcessingList().length; i++) {
					MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Processing Algorithm"));
					mp.setValue(hdf5_fgi_file.getProcessingList()[i], "String");
					smd.add_entry(mp);
				}
			}
			if (hdf5_fgi_file.getRawDataFormat() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Raw Data Format"));
				mp.setValue(hdf5_fgi_file.getRawDataFormat(), "String");
				smd.add_entry(mp);
			}
			if (hdf5_fgi_file.getSourceFile() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Source File"));
				mp.setAttributeName("Source file");
				mp.setValue(hdf5_fgi_file.getSourceFile(), "String");
				smd.add_entry(mp);
			}
			if (hdf5_fgi_file.getAzimuthSensorType() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Azimuth Sensor Type"));
				mp.setValue(hdf5_fgi_file.getAzimuthSensorType(), "String");
				smd.add_entry(mp);
			}
			if (hdf5_fgi_file.getCalibrationSeries() != -1) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Calibration Number"));
				mp.setValue(hdf5_fgi_file.getCalibrationSeries(), "RAW");
				smd.add_entry(mp);
			}

			if (hdf5_fgi_file.getKeywords() != null
					&& hdf5_fgi_file.getKeyword(0).length() > 1) {

				for (int i = 0; i < hdf5_fgi_file.getKeywords().length; i++) {
					MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Keyword"));
					mp.setValue(hdf5_fgi_file.getKeyword(i), "String");
					smd.add_entry(mp);
				}

			}
			if (hdf5_fgi_file.getFgiCalculationProcesses() != null) {
				if (hdf5_fgi_file.getFgiHdrfBrfFlag(spec_no).equals("HDRF")
						&& hdf5_fgi_file.getFgiCalculationProcesses().length > 1) {
					for (int i = 0; i < hdf5_fgi_file.getFgiCalculationProcess(0).length; i++) {
						MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Processing Module"));
						mp.setValue(
								hdf5_fgi_file.getFgiCalculationProcess(0, i),
								"String");
						smd.add_entry(mp);
					}
				} else if (hdf5_fgi_file.getFgiHdrfBrfFlag(spec_no)
						.equals("BRF")
						&& hdf5_fgi_file.getFgiCalculationProcesses().length > 1) {
					for (int i = 0; i < hdf5_fgi_file.getFgiCalculationProcess(1).length; i++) {
						MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Processing Module"));
						mp.setValue(
								hdf5_fgi_file.getFgiCalculationProcess(1, i),
								"String");
						smd.add_entry(mp);
					}
				} else if (hdf5_fgi_file.getFgiHdrfBrfFlag(spec_no)
						.equals("HDRF")
						&& hdf5_fgi_file.getFgiCalculationProcesses().length == 1) {
					for (int i = 0; i < hdf5_fgi_file.getFgiCalculationProcess(0).length; i++) {
						MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Processing Module"));
						mp.setValue(
								hdf5_fgi_file.getFgiCalculationProcess(0, i),
								"String");
						smd.add_entry(mp);
					}
				} else if (hdf5_fgi_file.getFgiHdrfBrfFlag(spec_no)
						.equals("BRF")
						&& hdf5_fgi_file.getFgiCalculationProcesses().length == 1) {
					for (int i = 0; i < hdf5_fgi_file.getFgiCalculationProcess(0).length; i++) {
						MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Processing Module"));
						mp.setValue(
								hdf5_fgi_file.getFgiCalculationProcess(0, i),
								"String");
						smd.add_entry(mp);
					}
				}
			}
			if (hdf5_fgi_file.getFgiFileLoaderComment() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Data Ingestion Notes"));
				mp.setValue(hdf5_fgi_file.getFgiFileLoaderComment(), "String");
				smd.add_entry(mp);
			}
			if (hdf5_fgi_file.getPolarizations() != null
					&& hdf5_fgi_file.getPolarization(spec_no) != null && !hdf5_fgi_file.getPolarization(spec_no).equals("-")) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Polarization"));
				mp.setValue(hdf5_fgi_file.getPolarization(spec_no), "String");
				smd.add_entry(mp);
			}
			
			if (hdf5_fgi_file.getPolarizationDirections() != null
					&& !hdf5_fgi_file.getPolarizationDirection(spec_no)
							.equals(new Double(0.0 / 0.0))) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Polarization Direction"));
				mp.setValue(hdf5_fgi_file.getPolarizationDirection(spec_no), "RAW");
				smd.add_entry(mp);
			}

			if (hdf5_fgi_file.getWeatherComment() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Weather Conditions"));
				mp.setAttributeName("Weather Conditions");
				mp.setValue(hdf5_fgi_file.getWeatherComment(), "String");
				smd.add_entry(mp);
			}

			if (hdf5_fgi_file.getFgiComments() != null) {
				for(int i = 0; i < hdf5_fgi_file.getFgiComments().length; i++){
					MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Comments"));
					mp.setValue(hdf5_fgi_file.getFgiComment(i), "String");
					smd.add_entry(mp);
				}
			}
			
			if (hdf5_fgi_file.getFgiOptics() != null) {
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Optics Name"));
				mp.setValue(hdf5_fgi_file.getFgiOptics(), "String");
				smd.add_entry(mp);
			}

			hdf5_fgi_file.addEavMetadata(spec_no, smd);
			
			
			////// redundancies are now removed in the EAV DB Services

			// a state change of BRF to HDRF or vice versa indicates a break in
			// redundancy
//			if (hdf5_fgi_file.getFgiHdrfBrfFlags() != null
//					&& !previous_BRF_HDRF_status
//							.equals(hdf5_fgi_file.getFgiHdrfBrfFlag(spec_no))) // combined
//																				// BRF
//																				// and
//																				// HDRF
//																				// in
//																				// file
//			{
//				redundancy_group_no++;
//				previous_BRF_HDRF_status = hdf5_fgi_file.getFgiHdrfBrfFlag(spec_no);
//			}

//			hdf5_fgi_file.setEavMetadataRedundancyGroup(spec_no, redundancy_group_no);

			// in 2008a data structure version polarization can change
//			if (hdf5_fgi_file.getPolarizations() != null && hdf5_fgi_file.getPolarization(spec_no) != null
//					&& !previous_polarization_status
//							.equals(hdf5_fgi_file.getPolarization(spec_no))) {
//				redundancy_group_no++;
//				previous_polarization_status = hdf5_fgi_file.getPolarization(spec_no);
//			}

//			hdf5_fgi_file.setEavMetadataRedundancyGroup(spec_no, redundancy_group_no);

			// field polarization_dir could change
//			if (hdf5_fgi_file.getPolarizationDirections() != null
//					&& !previous_polarization_dir_status
//							.equals(hdf5_fgi_file.getPolarizationDirection(spec_no))) {
//				redundancy_group_no++;
//				previous_polarization_dir_status = hdf5_fgi_file.getPolarizationDirection(spec_no);
//			}

//			hdf5_fgi_file.setEavMetadataRedundancyGroup(spec_no, redundancy_group_no);

		}

	}

	File[] xml_finder(String dirName) {
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".xml");
			}
		});
	}

	void fill_angles_date_and_id(SpectralFile hdf5_fgi_file, int index,
			Measurement m, Float[] refl_vector) {

		hdf5_fgi_file.addSensorAzimuth(index, m.SensorAz);
		hdf5_fgi_file.addSensorZenith(index, m.SensorZen);
		hdf5_fgi_file.addIlluminationAzimuth(index, m.LightAz);
		hdf5_fgi_file.addIlluminationZenith(index, m.LightZen);

		if (this.BRF_or_HDRF.equals("BRF"))
			hdf5_fgi_file.setMeasurementType(index, 2);
		if (this.BRF_or_HDRF.equals("HDRF"))
			hdf5_fgi_file.setMeasurementType(index, 8);

		hdf5_fgi_file.setMeasurementId(index, m.MeasurementID);

		try {
			hdf5_fgi_file.setCaptureDate(index, get_time(m));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		hdf5_fgi_file.setMeasurement(index, refl_vector);

	}

	Date get_time(Measurement m) throws IOException {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		// TimeZone tz = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance(tz);

		Integer sec = (int) m.UTC_Time[5];
		Integer min = (int) m.UTC_Time[4];
		Integer hour = (int) m.UTC_Time[3];
		Integer mday = (int) m.UTC_Time[2];
		Integer month = (int) m.UTC_Time[1];
		Integer year = (int) m.UTC_Time[0];

		// month must start at 0: this conforms with the java calendar class!
		cal.set(year, month - 1, mday, hour, min, sec);

		// SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		// formatter.setTimeZone(tz);
		//
		// String out=formatter.format(cal.getTime());

		return cal.getTime();
	}

	Date get_time(String date) throws IOException {
		if (date.contains(":") && !date.contains("-")) {
			TimeZone tz = TimeZone.getTimeZone("UTC");
			Calendar cal = Calendar.getInstance(tz);

			String[] tokens_date = date.split(" ");
			String[] tokens_date2 = tokens_date[0].split("\\.");
			String[] tokens_time = tokens_date[1].split(":");

			Integer sec = 0;
			Integer min = Integer.parseInt(tokens_time[1]);
			Integer hour = Integer.parseInt(tokens_time[0]);
			Integer mday = Integer.parseInt(tokens_date2[0]);
			Integer month = Integer.parseInt(tokens_date2[1]);
			Integer year = Integer.parseInt(tokens_date2[2]);

			cal.set(year, month - 1, mday, hour, min, sec);

			return cal.getTime();
		} else if (date.contains("\\.")) { // if time is splitted with a dot
			TimeZone tz = TimeZone.getTimeZone("UTC");
			Calendar cal = Calendar.getInstance(tz);

			String[] tokens_date = date.split(" ");
			String[] tokens_date2 = tokens_date[0].split("\\.");
			String[] tokens_time = tokens_date[1].split("\\.");

			Integer sec = 0;
			Integer min = Integer.parseInt(tokens_time[1]);
			Integer hour = Integer.parseInt(tokens_time[0]);
			Integer mday = Integer.parseInt(tokens_date2[0]);
			Integer month = Integer.parseInt(tokens_date2[1]);
			Integer year = Integer.parseInt(tokens_date2[2]);

			cal.set(year, month - 1, mday, hour, min, sec);

			return cal.getTime();
		} else { // if date is splitted by a -
			TimeZone tz = TimeZone.getTimeZone("UTC");
			Calendar cal = Calendar.getInstance(tz);

			if (date.length() < 11) {
				String[] tokens_date = date.split("-");
				Integer year = Integer.parseInt(tokens_date[0]);
				Integer month = Integer.parseInt(tokens_date[1]);
				Integer mday = Integer.parseInt(tokens_date[2]);
				Integer hour = 0;
				Integer min = 0;
				Integer sec = 0;

				hdf5_fgi_file.setFgiFileLoaderComment("Time set to 00:00 as there is no time information available in spectral file");

				cal.set(year, month - 1, mday, hour, min, sec);

				return cal.getTime();

			} else {
				String[] tokens_date = date.split(" ");
				String[] tokens_date2 = tokens_date[0].split("-");
				String[] time_tokens = tokens_date[1].split(":");

				Integer sec = 0;
				Integer min = Integer.parseInt(time_tokens[1]);
				Integer hour = Integer.parseInt(time_tokens[0]);
				Integer mday = Integer.parseInt(tokens_date2[2]);
				Integer month = Integer.parseInt(tokens_date2[1]);
				Integer year = Integer.parseInt(tokens_date2[0]);

				cal.set(year, month - 1, mday, hour, min, sec);

				return cal.getTime();
			}
		}

	}
	
//	String[] get_references(String[] keywords, Date[] capture_dates, String campaign_name){
//		
//		String[] reference_array = new String[2];
//		ArrayList<String> references = new ArrayList<String>();
//	
//		if(keywords[0].equals("Vegetation") && capture_dates[0].getYear() < 2005){
//			
//			references.add("Jouni Peltoniemi et al. BRDF measurement of understory \n" +
//					"vegetation in pine forests: dwarf shrubs, \n" +
//					"lichen and moss. Remote Sens. Environment, \n" +
//					"94(3):343Ð354, 15 Feb 2005. \n\n");
//		}
//		
//		if(keywords[0].equals("Vegetation") && capture_dates[0].getYear() >= 2005){
//			
//			references.add("Juha Suomalainen et al. Polarised bidirectional reflectance \n " +
//							"factor measurements from vegetated land surfaces. \n" +
//							"J. Quant. Spectrosc. Radiat. Transfer, 110:1044Ð1056, 2009.\n\n");
//			
//		}
//		
//		if(keywords[])
//		
//		
//		
//		
//		references.add("Juha Suomalainen et al. Polarised multiangular reflectance \n" +
//				"measurements using Finnish Geodetic Institute field goniospectrometer. \n " +
//				"Sensors, 9(5):3891Ð3907, 2009.\n\n");
//		
//		references.toArray(reference_array);
//		
//		return reference_array;
//
//		
//		
//	}

	Date get_short_time(String date) throws IOException {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		Calendar cal = Calendar.getInstance(tz);

		String[] tokens = date.split("\\.");

		Integer sec = 0;
		Integer min = 0;
		Integer hour = 0;
		Integer mday = Integer.parseInt(tokens[0]);
		Integer month = Integer.parseInt(tokens[1]);
		Integer year = Integer.parseInt(tokens[2]);

		cal.set(year, month - 1, mday, hour, min, sec);

		return cal.getTime();
	}

	public static String arrayToString2(String[] a, String separator) {
		StringBuffer result = new StringBuffer();
		if (a.length > 0) {
			result.append(a[0]);
			for (int i = 1; i < a.length; i++) {
				result.append(separator);
				result.append(a[i]);
			}
		}
		return result.toString();
	}

}
