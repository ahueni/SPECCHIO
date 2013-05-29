package ch.specchio.file.reader.spectrum;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.Metadata;
import ch.specchio.types.SpectralFile;
import ch.specchio.types.spatial_pos;

public class SPECPR_FileLoader extends SpectralFileLoader {

	SpectralFile specpr_file;
	int no_of_wvls_records = 0;


	public SPECPR_FileLoader() {
		super("SPECPR");

	}

	public SpectralFile load(File file) throws IOException, MetaParameterFormatException {

		
		
		String tdata;

		long filesize = file.length();
		int no_of_spectra = -1;
		int record_size = 1536;
		int rec_index = -1;
		int times_visited = 0;
		int no_of_values_left = 0;
		int irwav = 0;
		int saved_wvls_records = 0;
		int last_wvl_index = 0;
		
		boolean data_record_fully_loaded = false;
		
		ArrayList<Float[]> wvls = new ArrayList<Float[]>();

		specpr_file = new SpectralFile();
		
		specpr_file.setSpecprFile(true);

		specpr_file.setPath(file.getAbsolutePath());
		specpr_file.setBasename(file.getName());
		specpr_file.setFileFormatName(this.file_format_name);

		file_input = new FileInputStream(file);

		specpr_file.setCompany("SPECPR");

		no_of_spectra = get_number_of_spectra(file, filesize);

		specpr_file.setNumberOfSpectra(no_of_spectra);

		specpr_file.setFilename(file.getName());

		data_in = new DataInputStream(file_input);

		String record_zero = read_string(data_in, 1536);

		for (int i = record_size; i < filesize; i += record_size) {

			// byte[] test = new byte[4];
			// for(int j = 0; j < 4; j++){
			// test[j] = data_in.readByte();
			// }

			data_in.skip(3);

			byte b = data_in.readByte();
			boolean bit00 = isBitSet(b, 0); // continuation data flag
			boolean bit01 = isBitSet(b, 1); // text/data flag
			boolean bit02 = isBitSet(b, 2); // flag to indicate whether or not
											// the data for the error bar (1
											// sigma standard deviation of the
											// mean) is in the next record set =
											// 0: no errors
			boolean bit03 = isBitSet(b, 3); // RA, Dec/Long., Lat flag
			boolean bit04 = isBitSet(b, 4); // time file was last processed in
											// civil (0) or universal(1) time
			boolean bit05 = isBitSet(b, 5); // time of start of spectral run in
											// civil (0) or universal(1) time
			boolean bit06 = isBitSet(b, 6);
			boolean bit07 = isBitSet(b, 7);

			// case 1
			if (bit00 == false && bit01 == false) {

				
				
				rec_index++;
				times_visited = 0;
				data_record_fully_loaded = false;

				// Title which describes the data (ititl)
				String title = read_string(data_in, 40).trim();
				specpr_file.setSpecprExtNumber(rec_index, 0); // number extension for labelling spectras with same
							// name
				int ext_at = 0;
				
				if (rec_index != 0){
//				while (specpr_file.spectra_filenames[rec_index - 1]
//						.charAt(ext_at) != '_') {
//					ext_at++;
//				}
					
					ext_at = specpr_file.getSpectrumFilename(rec_index - 1).indexOf('_');
				}

				if (rec_index != 0 && title.contains(specpr_file.getSpectrumFilename(rec_index - 1)
						.substring(0, ext_at-1))) {
					specpr_file.setSpecprExtNumber(rec_index, specpr_file.getSpecprExtNumber(rec_index - 1) + 1);
					specpr_file.setSpectrumFilename(rec_index, title + 
							"_" + Integer.toString(specpr_file.getSpecprExtNumber(rec_index)));
				} else {
					specpr_file.setSpectrumFilename(rec_index, title + "_"+ Integer.toString(specpr_file.getSpecprExtNumber(rec_index)));
				}

				if (title.contains("Wavelengths") || title.contains("Bandpass")
						|| title.contains("FWHM")
						|| title.contains("Spectralon")) {
					specpr_file.setSpectrumFlag(rec_index, false);
				} else {
					specpr_file.setSpectrumFlag(rec_index, true);
				}
				
				// this is some fake info anyway ....
//				specpr_file.spectra_numbers[rec_index] = rec_index;
				
//				MetaParameter mp = new MetaParameter("General", "");
//				mp.setAttribute_name("Spectrum number");
//				mp.setValue(rec_index, "RAW");
//				smd.add_entry(mp);						
				

				// Name of the user that created the data record (usernm)
				specpr_file.setAuthors(new String[no_of_spectra]);
				specpr_file.setAuthor(rec_index, read_string(data_in, 8));

				// specpr files usually store reflectance data
				specpr_file.setMeasurementUnits(rec_index, 1); // reflectance by
																// default

				// Time, when data was last processed (civil or universal)
				int iscta = data_in.readInt();

				// Time at the start of the spectral run (civil or universal)
				int isctb = data_in.readInt();

				// Date when data was last processed (stored in file as Julian
				// Day number * 10)
				int jdatea = data_in.readInt();

				// Date when the spectral run began (stored in file as Julian
				// Day number * 10) (jdateb)
				int jdateb = data_in.readInt();
				specpr_file.setCaptureDate(rec_index, get_capture_date(jdateb,
						isctb));

				// Siderial time when the spectral run started (scaled seconds
				// by 24000) (istb)
				int istb = data_in.readInt();
				specpr_file.setSiderialTime(rec_index, get_siderial_time(istb));

				// Right ascension coordinates of an astronomical object or
				// longitude on a planetary surface (integer numbers in seconds
				// * 1000)
				int isra = data_in.readInt();

				// Declination coordinates of an astronomical object
				// or latitude on a planetary surface (integer numbers in
				// seconds * 1000)
				int isdec = data_in.readInt();

				if (bit03 == true) {
					// in this case, isra and isdec have stored the longitude
					// and latitude information of a spot on a planetary surface
					spatial_pos p = new spatial_pos();
					p.longitude = ((double) isra / 1000.0) / 3600.0;
					p.latitude = ((double) isdec / 1000.0) / 3600.0;
					
					specpr_file.addPos(rec_index, p);

				}
				if (bit03 == false) {
					// in this case, isra and isdec have stored the right
					// ascension and declination of an astronomical object
					// TODO: write a routine for storing RA and DEC data...
				}

				// Total number of channels in the spectrum
				specpr_file.addNumberOfChannels(data_in.readInt());
				no_of_values_left = specpr_file.getNumberOfChannels(rec_index);
				
				
				// Caution!!! this line is just temporary, needs to be deleted, as soon as the several sensors issue has been resolved!!!
//				specpr_file.no_of_channels = 2151;
				// Caution!!! this line is just temporary, needs to be deleted, as soon as the several sensors issue has been resolved!!!
				
				
				// The equivalent atmospheric thickness through which the
				// observation was obtained
				int irmas = data_in.readInt();
				specpr_file.setAtmThickness(rec_index, irmas / 1000);

				// The number of independent spectral scans which were added to
				// make the spectrum
				int revs = data_in.readInt();
				specpr_file.setNumberOfIndSpectralScans(rec_index, revs);

				// The channel numbers which define the band normalization
				int iband1 = data_in.readInt();

				// The channel numbers which define the band normalization
				int iband2 = data_in.readInt();

				// The record number within the file where the wavelengths are
				// found
				irwav = data_in.readInt();
//				
				
				

				// The record pointer to where the resolution can be found (or
				// horizontal error bar)
				int irespt = data_in.readInt();

				// The record number within the file where the data is located
				int irecno = data_in.readInt();
				specpr_file.setRecordNumber(rec_index, irecno);

				// Text record pointer. This pointer points to a data record
				// where additional text describing the data may be found
				int itpntr = data_in.readInt();
				specpr_file.setTextRecordPointer(rec_index, itpntr);

				// The program automatic 60 character history
				String ihist = read_string(data_in, 60);
				specpr_file.setAutomaticHistory(rec_index, ihist);

				// Manual history
				String mhist = read_string(data_in, 296);
				specpr_file.setManualHistory(rec_index, mhist);

				// The number of independent spectral runs which were summed or
				// averaged to make this spectrum
				int nruns = data_in.readInt();
				specpr_file.setNumberOfRuns(rec_index, nruns);

				// The angle of incidence of illuminating radiation (in
				// arc-seconds*6000)
				int siangl = data_in.readInt();
				specpr_file.addIlluminationZenith(rec_index, (float) (((float) siangl / 6000.0) / 3600.0));

				// The angle of emission of illuminating radiation (in
				// arc-seconds*6000)
				int seangl = data_in.readInt();
				specpr_file.addSensorZenith(rec_index, (float) (((float) seangl / 6000.0) / 3600.0));

				// The phase angle between iangl and eangl (in arc-seconds*1500)
				int sphase = data_in.readInt();
				specpr_file.setPhaseAngle(rec_index, sphase);

				// Weighted number of runs (The number of runs of the spectrum
				// with
				// the minimum runs which was used in processing this spectrum)
				int iwtrns = data_in.readInt();

				// The time observed in the sample beam for each half chop in
				// milliseconds (for chopping spectrometers only)
				int itimch = data_in.readInt();

				// The band normalization factor. For data scaled to 1.0,
				// multiply by this number to recover photometric level
				Float xnrm = data_in.readFloat();

				// The time it takes to make one scan of the entire spectrum in
				// seconds
				Float scatim = data_in.readFloat();

				// Total integration time (usually = scatime * nruns)
				Float timint = data_in.readFloat();
//				specpr_file.rec_instr_set[rec_index] = new InstrumentSettings();
//				specpr_file.rec_instr_set[rec_index].add_setting(
//						"Integration_time", timint);
				
				Metadata smd = new Metadata();
				
				MetaParameter mp = MetaParameter.newInstance(attributes_name_hash.get("Integration Time"));
				mp.setValue((int)(timint*1000), "ms");
				smd.add_entry(mp);		
				
				specpr_file.addEavMetadata(smd);
				

				// Temperature in degrees Kelvin
				Float tempd = data_in.readFloat();
				specpr_file.setTemperature(rec_index, tempd - (float) 273.15);

				// The spectral data (only 256 channels! The rest of the
				// spectrum is stored the cdata field of case 2)
				Float[] data;
				specpr_file.setMeasurement(rec_index, new Float[specpr_file.getNumberOfChannels(rec_index)]);

				int empty_data_count = 0;
				if (no_of_values_left < 256) {
					empty_data_count = 256 - no_of_values_left;
					data = new Float[no_of_values_left];
					for (int j = 0; j < data.length; j++) {
						data[j] = data_in.readFloat();
						no_of_values_left--;
					}
					for (int j = 0; j < data.length; j++) {
						specpr_file.setMeasurement(rec_index, j, data[j]);
					}
					skip(data_in, empty_data_count * 4);
				} else {
					data = new Float[256];
					for (int j = 0; j < data.length; j++) {
						data[j] = data_in.readFloat();
					}
					for (int j = 0; j < data.length; j++) {
						specpr_file.setMeasurement(rec_index, j, data[j]);
					}
					no_of_values_left -= 256;
				}
				
				if (no_of_values_left <= 0){
					data_record_fully_loaded = true;
				}
				
				
				if (specpr_file.getRecordNumber(rec_index) != irwav && irwav != 0 && irwav < specpr_file.getRecordNumber(rec_index)){
					specpr_file.addWvls(wvls.get(irwav));
//					specpr_file.wvls.set(rec_index, wvls.get(irwav));
				}
				

				
				
				

				int x = 1;

			}

			// case 2
			if (bit00 == true && bit01 == false) {

				// The continuation of the spectrum data from case 1 (383
				// channels)
				Float[] cdata;
				if (no_of_values_left < 383) {
					cdata = new Float[no_of_values_left];
					for (int j = 0; j < cdata.length; j++) {
						cdata[j] = data_in.readFloat();
					}
					for (int j = 0; j < cdata.length; j++) {
						specpr_file.setMeasurement(rec_index, 256 + times_visited * 383 + j, cdata[j]);
					}
					skip(data_in, (383 - no_of_values_left) * 4);
					no_of_values_left = 0;
				} else {

					cdata = new Float[383];

					for (int j = 0; j < cdata.length; j++) {
						cdata[j] = data_in.readFloat();
					}

					for (int j = 0; j < cdata.length; j++) {
						specpr_file.setMeasurement(rec_index, 256 + times_visited * 383 + j, cdata[j]);
					}

					no_of_values_left -= 383;
					
					}
					times_visited++;
					if(no_of_values_left <= 0){
						data_record_fully_loaded = true;
				}

			}

			// case 3
			if (bit00 == false && bit01 == true) {

				// Title which describes the data
				String ititle = read_string(data_in, 40);

				// The name of the user that created the data record
				String usernm = read_string(data_in, 8);

				// Text data record pointer. This pointer points to a data
				// record where additional text may be found
				String itxtpt = read_string(data_in, 4);

				// The number of text characters
				int itxtch = data_in.readInt();

				// 1476 characters of text
				String itext = read_string(data_in, 1476);

				int x = 1;

			}

			// case 4
			if (bit00 == true && bit01 == true) {

				// some text
				tdata = read_string(data_in, 1532);

				// skip(data_in, 3);
				//
				// byte[] byte_data = new byte[10000];
				// for (int j = 0; j < byte_data.length; j++) {
				// byte_data[j] = data_in.readByte();
				// }

			}



			// byte[] byte_data = new byte[10000];
			// for (int j = 0; j < byte_data.length; j++) {
			// byte_data[j] = data_in.readByte();
			// }
			
			int x = 1;
			
			//The last part of this if statement is kind of strange. Wavelength records shouldn't point to a normal spectrum, but there was one file where the wavelength record pointed
			// to some random record
			
			if (data_record_fully_loaded == true && rec_index > -1 && specpr_file.getSpectrumFilename(rec_index).contains("Wavelengths")){ 	// (specpr_file.rec_number[rec_index] == irwav || irwav == 0 || irwav == 1430)){ // strange behaviour in file. use this for a more generic way: (does not work properly either...)specpr_file.rec_number[rec_index]
				Float[] wvl = new Float[specpr_file.getNumberOfChannels(rec_index)];
				for (int z = 0; z < wvl.length; z++){
					wvl[z] = specpr_file.getMeasurement(rec_index, z) * 1000;
				}
				
				while(last_wvl_index != specpr_file.getRecordNumber(rec_index) && saved_wvls_records < no_of_wvls_records){
					wvls.add(null);
					last_wvl_index++;
				}
				
				if (specpr_file.getSpectrumFilename(rec_index).contains("Wavelengths") && saved_wvls_records < no_of_wvls_records){
					wvls.add(wvl);
					specpr_file.addWvls(wvl);
					saved_wvls_records++;
					last_wvl_index++;
				}

		}
		
		
		
		
			
			
		}
		

		
		
		return specpr_file;

	}

	public int get_number_of_spectra(File file, long file_size)
			throws IOException {
		int no_of_spectra = 0;

		boolean is_spectra = false;

		FileInputStream file_in = new FileInputStream(file);

		DataInputStream data_in = new DataInputStream(file_in);

		String record_zero = read_string(data_in, 1536);

		for (int i = 1536; i < file_size; i += 1536) {
			skip(data_in, 3);
			byte b = data_in.readByte();
			boolean bit00 = isBitSet(b, 0);
			boolean bit01 = isBitSet(b, 1);
			boolean bit02 = isBitSet(b, 2);
			boolean bit03 = isBitSet(b, 3);
			boolean bit04 = isBitSet(b, 4);
			boolean bit05 = isBitSet(b, 5);
			boolean bit06 = isBitSet(b, 6);
			boolean bit07 = isBitSet(b, 7);

			if (bit00 == false && bit01 == false) {
				// this is case 1, where we have the header plus the spectrum
				// data stored

				no_of_spectra++;
		

				 String title = read_string(data_in, 40);
				 if (title.contains("Wavelengths")){
					 no_of_wvls_records++;
				 }
				 skip(data_in, 1492);
				// title.contains("Bandpass")
				// || title.contains("FWHM")
				// || title.contains("Spectralon")) {
				// skip(data_in, 1492);
				// } else {
				// no_of_spectra++;
				// skip(data_in, 1492);
				// }
			}
			// else if(bit00 == false && bit01 == true){
			// String test = read_string(data_in, 10);
			// if(test.equals("----------")){
			// is_spectra = true;
			// }
			// skip(data_in, 1522);
			// }
			else {
				skip(data_in, 1532);
			}

		}

		data_in.close();
		file_in.close();

		specpr_file.setMeasurements(new Float[no_of_spectra][]);

		specpr_file.setSpectraFlags(new boolean[no_of_spectra]);
		specpr_file.setSiderialTimes(new String[no_of_spectra]);
		specpr_file.setAtmThicknesses(new int[no_of_spectra]);
		specpr_file.setNumberOfIndSpectralScans(new int[no_of_spectra]);
		specpr_file.setRecordNumbers(new int[no_of_spectra]);
		specpr_file.setTextRecordPointers(new int[no_of_spectra]);
		specpr_file.setAutomaticHistories(new String[no_of_spectra]);
		specpr_file.setNumberOfRuns(new int[no_of_spectra]);
		specpr_file.setManualHistories(new String[no_of_spectra]);
		specpr_file.setPhaseAngles(new int[no_of_spectra]);
		specpr_file.setTemperatures(new Float[no_of_spectra]);
//		specpr_file.rec_instr_set = new InstrumentSettings[no_of_spectra];
//		specpr_file.illumination_zenith = new Float[no_of_spectra];
//		specpr_file.sensor_zenith = new Float[no_of_spectra];
//		specpr_file.specpr_no_of_channels = new int[no_of_spectra];
		specpr_file.setSpecprExtNumbers(new int[no_of_spectra]);

		int x = -1;
		return no_of_spectra;

	}

	public static Date get_capture_date(int jdn, int time) {
		Date date = new Date();

		TimeZone tz = TimeZone.getTimeZone("UTC");
		// TimeZone tz = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance(tz);

		float sec_of_day = time / 24000; // stored time value is scaled by 24000
		float hour_w = sec_of_day / 3600;
		int hour_int = (int) hour_w;
		float hour = (float) hour_int;
		float min_w = (hour_w - hour) * 60;
		int min_int = (int) min_w;
		float min = (float) min_int;
		int sec = Math.round((min_w - min) * 60);

		double hour_part = (hour) / 24.0; // (hour - 12.0)/24.0;
		double min_part = min / 1440.0;
		double sec_part = sec / 86400.0;

		double jd = (float) ((float) jdn / 10.0) + hour_part + min_part
				+ sec_part;
		int[] date_table = new int[3];

		date_table = julianToDate(jd);

		// Months start at 0 in calendar class
		cal.set(date_table[0], date_table[1] - 1, date_table[2], hour_int,
				min_int, sec);

		// SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmm");
		// formatter.setTimeZone(tz);
		//
		// String out = formatter.format(cal.getTime());
		//
		// int x = -1;

		return cal.getTime();
	}

	public static String get_siderial_time(int time) {

		float sec_of_day = time / 24000; // stored time value is scaled by 24000
		float hour_w = sec_of_day / 3600;
		int hour_int = (int) hour_w;
		float hour = (float) hour_int;
		float min_w = (hour_w - hour) * 60;
		int min_int = (int) min_w;
		float min = (float) min_int;
		int sec = Math.round((min_w - min) * 60);

		String sid_time = String.valueOf(hour_int).concat(":")
				.concat(String.valueOf(min_int)).concat(":")
				.concat(String.valueOf(sec));
		return sid_time;
	}

	public static int[] julianToDate(double jdn) {

		// Conversion from Wikipedia --> does not compute correctly...
		// double J = jdn + 0.5;
		// double j = J + 32044;
		// double g = j / 146097;
		// double dg = j % 146097;
		// double c = (dg / 36524.0 + 1.0) * 3.0 / 4.0;
		// double dc = dg - c * 36524;
		// double b = dc / 1461;
		// double db = dc % 1461;
		// double a = (db / 365 + 1) * 3 / 4;
		// double da = db - a * 365;
		// double y = g * 400 + c * 100 + b * 4 + a; //this is the integer
		// number of full years elapsed sinde March 1, 4801BC at 00:00UTC
		// double m = (da * 5 + 308) / 153 - 2; // this is the integer number of
		// full months elapsed since the last March 1 at 00:00 UTC
		// double d = da - (m + 4) * 153 / 5 +122; // this is the number of days
		// elapsed since day 1 of the month at 00:00 UTC, including fractions of
		// one day
		// int Y = (int)y - 4800 + ((int)m + 2) / 12;
		// int M = ((int)m + 2) % 12 + 1;
		// int D = (int)d + 1;
		//
		// return new int[] {Y, M, D};

		// Gregorian Calendar --> sometimes returns a wrong date, where the day
		// number is 1 day too short...
		int jGreg = 15 + 31 * (10 + 12 * 1582);
		int jalpha, ja, jb, jc, jd, je;
		int year, month, day;
		double julian = jdn + 0.5; // / 86400.0;
		ja = (int) julian;
		if (ja >= jGreg) {
			jalpha = (int) (((ja - 1867216) - 0.25) / 36524.25);
			ja = ja + 1 + jalpha - jalpha / 4;
		}

		jb = ja + 1524;
		jc = (int) (6680.0 + ((jb - 2439870) - 122.1) / 365.25);
		jd = 365 * jc + jc / 4;
		je = (int) ((jb - jd) / 30.6001);
		day = (jb - jd - (int) (30.6001 * je));
		month = je - 1;
		if (month > 12)
			month = month - 12;
		year = jc - 4715;
		if (month > 2)
			year--;
		if (year <= 0)
			year--;

		return new int[] { year, month, day };

		// double z, f, a, b, c, d, e, m, aux;
		// Date date = new Date();
		// jd += 0.5;
		// z = Math.floor(jd);
		// f = jd - z;
		//
		// if(z >= 2299161.0){
		// a = Math.floor((z - 1867216.25) / 36524.25);
		// a = z + 1 + a - Math.floor(a / 4);
		// } else {
		// a = z;
		// }
		//
		// b = a + 1524;
		// c = Math.floor((b - 122.1) / 365.25);
		// d = Math.floor(365.25 * c);
		// e = Math.floor((b - d) / 30.6001);
		// aux = b - d - Math.floor(30.6001 * e) + f;
		//
		// // Calendar calendar = new GregorianCalendar();
		// // calendar.setTime(date);
		// TimeZone tz = TimeZone.getTimeZone("UTC");
		// //TimeZone tz = TimeZone.getDefault();
		// Calendar calendar = Calendar.getInstance(tz);
		// calendar.set(Calendar.DAY_OF_MONTH, (int) aux);
		// aux = ((aux - calendar.get(Calendar.DAY_OF_MONTH)) * 24);
		// calendar.set(Calendar.HOUR_OF_DAY, (int)aux);
		// calendar.set(Calendar.MINUTE, (int) ((aux -
		// calendar.get(Calendar.HOUR_OF_DAY)) * 60));
		//
		// if (e < 13.5){
		// m = e - 1;
		// } else {
		// m = e - 13;
		// }
		//
		// calendar.set(Calendar.MONTH, (int) m - 1);
		// if(m > 2.5) {
		// calendar.set(Calendar.YEAR, (int) (c - 4716));
		// } else {
		// calendar.set(Calendar.YEAR, (int) (c - 4715));
		// }
		//
		// Date test = calendar.getTime();
		// return calendar;

	}

	private static boolean isBitSet(byte b, int bit) {

		return (b & (1 << bit)) != 0;
	}

	protected String read_string(DataInputStream in, int no_of_chars)
			throws IOException {
		byte[] bytes = new byte[no_of_chars];
		in.read(bytes);

		// add null at the end
		// byte[] bytes_ = new byte[no_of_chars+1];
		//
		// for (int i=0;i<no_of_chars;i++) bytes_[i] = bytes[i];
		// bytes_[no_of_chars] = 0;
		//

		return new String(bytes);
	}

	public static int swap(int value) {
		int b1 = (value >> 0) & 0xff;
		int b2 = (value >> 8) & 0xff;
		int b3 = (value >> 16) & 0xff;
		int b4 = (value >> 32) & 0xff;

		return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;

	}

}
