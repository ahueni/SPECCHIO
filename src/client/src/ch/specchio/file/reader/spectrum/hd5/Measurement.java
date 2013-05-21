package ch.specchio.file.reader.spectrum.hd5;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Measurement {

	public float LightAz;
	public float LightZen;
	public int MeasurementID;
	public Float[] Reflectance;
	public Float[] ReflectanceQ;
	public Float[] ReflectanceU;
	public float SensorAz;
	public float SensorZen;
	public float[] UTC_Time;
	public int refl_size;
	public int UTC_size;
	public int wave_size;
	public int polarization_size;
	public int spectrumType_size;
	public String polarization;
	public Double polarization_dir = null;
	public String spectrum_type;
	public String[] calculationProcesses;

	public String DStruct_Version;
	public int no_of_chars;
	public String[] member_names;

	int reflq_pos = -1;
	int reflu_pos = -1;

	// public int dims;

	// Constructor for Data Structure Version FGI_2010a-08/2010
	public Measurement(int refl_size, int UTC_size, String[] member_names) {

		Reflectance = new Float[refl_size];
		ReflectanceQ = new Float[refl_size];
		ReflectanceU = new Float[refl_size];
		this.refl_size = refl_size;
		UTC_Time = new float[UTC_size];
		this.UTC_size = UTC_size;
		// Wavelength = new Float[refl_size];

		for (int i = 0; i < member_names.length; i++) {
			if (member_names[i].equals("ReflQ"))
				reflq_pos = i;
			if (member_names[i].equals("ReflU"))
				reflu_pos = i;
			if (member_names[i].equals("StokesQ"))
				reflq_pos = i;
			if (member_names[i].equals("StokesU"))
				reflu_pos = i;
		}

	}

	// Constructor for Data Structure Version FGI_2008a-06/2008 with Polarization
	public Measurement(int refl_size, int UTC_size, int polarization_size,
			int spectrumType_size) {
		Reflectance = new Float[refl_size];
		this.refl_size = refl_size;
		if (UTC_size > 0)
			UTC_Time = new float[UTC_size];
		this.UTC_size = UTC_size;
		this.polarization_size = polarization_size;
		this.spectrumType_size = spectrumType_size;

	}

	// Constructor for Data Structure Version FGI_beta0.8-12/2007
	public Measurement(int refl_size) {
		Reflectance = new Float[refl_size];
		this.refl_size = refl_size;
	}

	public Measurement() {

	}

	public boolean hasQandU() {
		if (this.reflq_pos > -1 && reflu_pos > -1)
			return true;
		return false;
	}

	// public Measurement(int no_of_chars){
	// this.no_of_chars = no_of_chars;
	//
	// }

	// Each data member field must be shown how to be written and read.
	// Strings need to be handled by bytes.
	public void readExternal_old(DataInputStream in) throws IOException,
			ClassNotFoundException {

		LightAz = read_float(in);
		ArrayList<Float> LightAzList = new ArrayList<Float>();
		LightAzList.add(LightAz);
		LightZen = read_float(in);
		ArrayList<Float> LightZenList = new ArrayList<Float>();
		LightZenList.add(LightZen);
		MeasurementID = read_uint(in);
		ArrayList<Integer> MeasurementIdList = new ArrayList<Integer>();
		MeasurementIdList.add(MeasurementID);

		// ArrayList<Float> Ref_of_Spectra = new ArrayList<Float>(refl_size);
		for (int band = 0; band < refl_size; band++) {
			Reflectance[band] = read_float(in);

			// Ref_of_Spectra.add(Reflectance[band]);
		}
		// ArrayList<ArrayList> ReflectanceList = new ArrayList<ArrayList>();
		// ReflectanceList.add(Ref_of_Spectra);

		if (this.reflq_pos > -1) {
			for (int band = 0; band < refl_size; band++) {
				ReflectanceQ[band] = read_float(in);
			}
		}

		if (this.reflu_pos > -1) {
			for (int band = 0; band < refl_size; band++) {
				ReflectanceU[band] = read_float(in);
			}
		}

		SensorAz = read_float(in);
		ArrayList<Float> SensorAzList = new ArrayList<Float>();
		SensorAzList.add(SensorAz);
		SensorZen = read_float(in);
		ArrayList<Float> SensorZenList = new ArrayList<Float>();
		SensorZenList.add(SensorZen);

		ArrayList<Float> Time_of_Spectra = new ArrayList<Float>(UTC_size);
		for (int i = 0; i < UTC_size; i++) {
			UTC_Time[i] = read_float(in);
			Time_of_Spectra.add(UTC_Time[i]);
		}
		ArrayList<ArrayList> Time_List = new ArrayList<ArrayList>();
		Time_List.add(Time_of_Spectra);

	}

	// Read Data Structure Version FGI_2008a
	public void readExternal_FGI_2008a(DataInputStream in) throws IOException,
			ClassNotFoundException {

		LightZen = read_float(in);
		LightAz = read_float(in);
		SensorZen = read_float(in);
		SensorAz = read_float(in);
		MeasurementID = read_uint(in);

		for (int i = 0; i < UTC_size; i++) {
			UTC_Time[i] = read_uint(in);
		}

		polarization = read_string(in, polarization_size).trim();
		polarization_dir = read_float(in).doubleValue();
		for (int band = 0; band < refl_size; band++) {
			Reflectance[band] = read_float(in);
		}
		spectrum_type = read_string(in, spectrumType_size).trim();
	}

	public void readExternal(DataInputStream in, String[] member_names,
			String ds_version, int utc_type) throws IOException, ClassNotFoundException {
		this.member_names = member_names;
		for (int i = 0; i < member_names.length; i++) {
			if (member_names[i].equalsIgnoreCase("LightZen"))
				LightZen = read_float(in);
			if (member_names[i].equalsIgnoreCase("LightAz"))
				LightAz = read_float(in);
			if (member_names[i].equalsIgnoreCase("SensorZen"))
				SensorZen = read_float(in);
			if (member_names[i].equalsIgnoreCase("SensorAz"))
				SensorAz = read_float(in);
			if (member_names[i].equalsIgnoreCase("MeasurementID"))
				MeasurementID = read_uint(in);
			if (member_names[i].equalsIgnoreCase("UTC_Time")) {
				if (utc_type == 0)
					for (int j = 0; j < UTC_size; j++) {
						UTC_Time[j] = read_uint(in);
						
					}
				if (utc_type == 1)
					for (int j = 0; j < UTC_size; j++) {
						UTC_Time[j] = read_float(in);
					}

			}
			
			if (member_names[i].equalsIgnoreCase("Polarization"))
				polarization = read_string(in, polarization_size).trim();
			if (member_names[i].equalsIgnoreCase("PolarizationDir"))
				polarization_dir = read_float(in).doubleValue();
			if (member_names[i].equalsIgnoreCase("Spectrum"))
				for (int band = 0; band < refl_size; band++) {
					Reflectance[band] = read_float(in);
				}
			if (member_names[i].equalsIgnoreCase("ReflI"))
				for (int band = 0; band < refl_size; band++) {
					Reflectance[band] = read_float(in);
				}

			if (member_names[i].equalsIgnoreCase("ReflQ")
					&& this.reflq_pos > -1) {
				for (int band = 0; band < refl_size; band++) {
					ReflectanceQ[band] = read_float(in);
				}
			}

			if (member_names[i].equalsIgnoreCase("ReflU")
					&& this.reflu_pos > -1) {
				for (int band = 0; band < refl_size; band++) {
					ReflectanceU[band] = read_float(in);
				}
			}
			
			if (member_names[i].equalsIgnoreCase("StokesI")){
				for (int band = 0; band < refl_size; band++){
					Reflectance[band] = read_float(in);
				}
			}
			
			if (member_names[i].equalsIgnoreCase("StokesQ") && this.reflq_pos > -1){
				for (int band = 0; band < refl_size; band++) {
					ReflectanceQ[band] = read_float(in);
				}
			}
			
			if (member_names[i].equalsIgnoreCase("StokesU") && this.reflu_pos > -1){
				for (int band = 0; band < refl_size; band++) {
					ReflectanceU[band] = read_float(in);
				}
			}
			

			if (member_names[i].equalsIgnoreCase("SpectrumType"))
				spectrum_type = read_string(in, spectrumType_size).trim();

		}
	}

	public void readExternal_fgi_beta08(DataInputStream in) throws IOException,
			ClassNotFoundException {

		LightAz = read_float(in);
		LightZen = read_float(in);
		SensorAz = read_float(in);
		SensorZen = read_float(in);
		for (int band = 0; band < refl_size; band++) {
			Reflectance[band] = read_float(in);
		}
	}

	// // Read SpectrumTypes table
	// public void readExternal_spectrumTypes(DataInputStream in2, int[]
	// member_size_spectrumTypes, int dimsCalcProc) throws IOException,
	// ClassNotFoundException {
	//
	// calculationProcesses = new String[dimsCalcProc];
	//
	// String spectrum_type = read_string(in2, member_size_spectrumTypes[0]);
	// for (int i = 0; i < dimsCalcProc; i++){
	// calculationProcesses[i] = read_string(in2,
	// member_size_spectrumTypes[1]/2).trim();
	// }
	// }

	public void readExternal_spectrumTypes_BRF(DataInputStream in2,
			int[] member_size_spectrumTypes, int dimsCalcProc)
			throws IOException, ClassNotFoundException {

		calculationProcesses = new String[dimsCalcProc];

		String no_use = read_string(in2, member_size_spectrumTypes[0]);
		for (int i = 0; i < dimsCalcProc; i++) {
			calculationProcesses[i] = read_string(in2,
					member_size_spectrumTypes[1]);
		}
	}

	public void readExternal_specType_noUse(DataInputStream in2,
			int[] member_size_spectrumTypes, int dimsCalcProc)
			throws IOException, ClassNotFoundException {

		String no_use = read_string(in2, member_size_spectrumTypes[0]);
		String no_use2 = read_string(in2, member_size_spectrumTypes[1]);
	}

	// Read the wavelength dataset

	// Read Data Structure Version dataset
	public void readExternal_DStruct_Vers(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		DStruct_Version = read_string(in, no_of_chars);
	}

	protected String read_string(DataInputStream in, int no_of_chars)
			throws IOException {
		byte[] bytes = new byte[no_of_chars];
		in.read(bytes);
		return new String(bytes);
	}

	protected Integer read_short(DataInputStream in) throws IOException {
		byte[] b = new byte[2];
		in.read(b);
		return (new Integer(arr2int(b, 0)));
	}

	protected Integer read_int(DataInputStream in) throws IOException {
		byte[] b = new byte[4];
		in.read(b);
		Integer n = arr4int(b, 0); // strange why reading just an integer wont
									// work (uint not existing in Java???)
		return n;
	}

	protected Integer read_uint(DataInputStream in) throws IOException {
		byte[] b = new byte[4];
		in.read(b);
		Integer n = (int) arr4uint(b, 0); // strange why reading just an integer
											// wont work (uint not existing in
											// Java???)
		return n;
	}

	protected Integer read_long(DataInputStream in) throws IOException {
		byte[] b = new byte[4];
		in.read(b);
		long n = arr2long(b, 0);

		int as_int = (int) n;

		return as_int;
	}

	public static Float read_float(DataInputStream in) throws IOException {
		byte[] b = new byte[4];
		in.read(b);
		return (new Float(arr2float(b, 0)));
	}

	protected Double read_double(DataInputStream in) throws IOException {
		byte[] b = new byte[8];
		in.read(b);
		return (new Double(arr2double(b, 0)));
	}

	protected void skip(DataInputStream in, int no_of_bytes) {
		try {
			in.skipBytes(no_of_bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static double arr2double(byte[] arr, int start) {
		int i = 0;
		int len = 8;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			// System.out.println(java.lang.Byte.toString(arr[i]) + " " + i);
			cnt++;
		}
		long accum = 0;
		i = 0;
		for (int shiftBy = 0; shiftBy < 64; shiftBy += 8) {
			accum |= ((long) (tmp[i] & 0xff)) << shiftBy;
			i++;
		}
		return Double.longBitsToDouble(accum);
	}

	public static float arr2float(byte[] arr, int start) {
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		int accum = 0;
		i = 0;
		for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			accum |= ((long) (tmp[i] & 0xff)) << shiftBy;
			i++;
		}
		return Float.intBitsToFloat(accum);
	}

	public static long arr2long(byte[] arr, int start) {
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			accum |= ((long) (tmp[i] & 0xff)) << shiftBy;
			i++;
		}
		return accum;
	}

	public static int arr2int(byte[] arr, int start) {
		int low = arr[start] & 0xff;
		int high = arr[start + 1] & 0xff;
		return (int) (high << 8 | low);
	}

	public static int arr4int(byte[] arr, int start) {
		int b1 = arr[start] & 0xff;
		int b2 = arr[start + 1] & 0xff;
		int b3 = arr[start + 1] & 0xff;
		int b4 = arr[start + 1] & 0xff;
		return (int) (b4 << 24 | b3 << 16 | b2 << 8 | b1);
	}

	public static long arr4uint(byte[] by, int start) {

		long value = 0;
		for (int i = 0; i < by.length; i++) {
			value += (by[i] & 0xff) << (8 * i);
		}

		return value;
	}

}
