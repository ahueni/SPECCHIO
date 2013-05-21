package ch.specchio.file.reader.spectrum.hd5;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import ch.specchio.types.spatial_pos;


public class Information {

	int no_of_chars;
	public String[] Authors;
	public String[] Comments;
	public String[] ReferenceFiles;
	public String Campaign;
	public String GPS;
	public String Instrument_Name;
	public String[] Instrument_Parameters;
	public String Light_Source;
	public String Light_Source_Param;
	public String Location;
	public String Measurement_Time;
	public String Weather;
	public String White_Reference_Target;
	public String Processing;
	public String RawDataFormat;
	public String SourceFile;
	public String Description;
	public long Keyword_dims;
	public String[] Keywords;
	public String Name;
	public spatial_pos gps_pos;
	

	public Information(int no_of_char) {
		this.no_of_chars = no_of_chars;
	}
	public Information(){
		
	}

	public Information(int no_of_char, long dset_dims) {
		this.no_of_chars = no_of_char;
		this.Keyword_dims = dset_dims;
//		Keywords = new String[(int) dset_dims];
	}
	
	public void readExternal_String(DataInputStream in, int no_of_chars, String dataset_name, long dset_dims) throws IOException, ClassNotFoundException {
		this.no_of_chars = no_of_chars;
		if(dataset_name.equalsIgnoreCase("Authors")){
			Authors = new String[(int)dset_dims];
			for(int i = 0; i < dset_dims; i++){
				Authors[i] = read_string(in, no_of_chars).trim();
			}
		}
		if(dataset_name.equalsIgnoreCase("Comments")){
			Comments = new String[(int)dset_dims];
			for(int i = 0; i < dset_dims;i++)
				Comments[i] = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("ReferenceFiles")){
			ReferenceFiles = new String[(int)dset_dims];
			for(int i = 0; i < dset_dims; i++)
				ReferenceFiles[i] = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("Campaign")){
			Campaign = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("GPS")){
			GPS = read_string(in, no_of_chars).trim();
			gps_pos = getSpatialPos(GPS);
		}
		if(dataset_name.equalsIgnoreCase("InstrumentName")){
			Instrument_Name = read_string(in, no_of_chars).trim();	
		}
		if(dataset_name.equalsIgnoreCase("InstrumentParameters")){
			Instrument_Parameters = new String[(int)dset_dims];
			for(int i = 0; i < dset_dims; i++)
				Instrument_Parameters[i] = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("LightSource")){
			Light_Source = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("Location")){
			Location = read_string(in,no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("MeasurementTime")){
			Measurement_Time = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("Weather")){
			Weather = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("WhiteReferenceTarget")){
			White_Reference_Target = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("LightSourceParameters")){
			Light_Source_Param = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("RawDataFormat")){
			RawDataFormat = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("SourceFile")){
			SourceFile = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("Description")){
			Description = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("Keywords")){
			Keywords = new String[(int) dset_dims];
			for(int i = 0; i < dset_dims; i++)
				Keywords[i] = read_string(in, no_of_chars).trim();
		}
		if(dataset_name.equalsIgnoreCase("Name")){
			Name = read_string(in, no_of_chars).trim();
		}
	}

	// Read Author Information
//	public void readExternal_Authors(DataInputStream in, int no_of_chars)
//			throws IOException, ClassNotFoundException {
//
//		Authors = read_string(in, no_of_chars).trim();
//		ArrayList<String> AuthorsList = new ArrayList<String>(1);
//		AuthorsList.add(Authors);
//
//	}

	// Read Campaign Information
	public void readExternal_Campaign(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		Campaign = read_string(in, no_of_chars).trim();
		ArrayList<String> CampaignList = new ArrayList<String>(1);
		CampaignList.add(Campaign);
	}

	// Read GPS Information
	public void readExternal_GPS(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		GPS = read_string(in, no_of_chars).trim();
		gps_pos = getSpatialPos(GPS);
		ArrayList<String> GPSList = new ArrayList<String>(1);
		GPSList.add(GPS);
	}
	// Convert the String GPS data into the class spatial_pos
	public spatial_pos getSpatialPos(String gps)
	{
		double latitude;
		double longitude;
		double altitude;
		String gps_string = gps;
		String[] tokens = gps_string.split("d");
		double deg_lat = Double.parseDouble(tokens[0]);
		int d1_index = gps_string.indexOf("d");
		int m1_index = gps_string.indexOf("m");
		int d2_index = gps_string.indexOf("d", d1_index+1);
		int m2_index = gps_string.indexOf("m", m1_index+1);
		String lat_min = gps_string.substring(d1_index+1, m1_index);
		double min_lat = Double.parseDouble(lat_min);
		String[] tokens2 = gps_string.split(" ");
		String long_deg = tokens2[2].substring(0, tokens2[2].indexOf("d"));
		double deg_long = Double.parseDouble(long_deg);
		String long_min = tokens[2].substring(0, tokens[2].indexOf("m"));
		double min_long = Double.parseDouble(long_min);
		
		if(gps_string.contains("N"))
			latitude = deg_lat + (min_lat/60);
		else
			latitude = -1 * (deg_lat + (min_lat/60));
		if(gps_string.contains("W"))
			longitude = deg_long + (min_long/60);
		else
			longitude = -1 * (deg_long + (min_long/60));
		
		
		spatial_pos pos = new spatial_pos();
		pos.latitude = latitude;
		pos.longitude = longitude;
		
		return pos;
		
	}

	// Read Instrument Name Information
	public void readExternal_InstrumentName(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		Instrument_Name = read_string(in, no_of_chars).trim();
		ArrayList<String> Instrument_Name_List = new ArrayList<String>(1);
		Instrument_Name_List.add(Instrument_Name);
	}

//	// Read Instrument Parameter Information
//	public void readExternal_InstrumentParameters(DataInputStream in,
//			int no_of_chars) throws IOException, ClassNotFoundException {
//
//			Instrument_Parameters = read_string(in, no_of_chars);
//			ArrayList<String> Instrument_Parameters_List = new ArrayList<String>();
//			Instrument_Parameters_List.add(Instrument_Parameters);
//	}

	// Read Light Source Information
	public void readExternal_LightSource(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		Light_Source = read_string(in, no_of_chars).trim();
		ArrayList<String> LightSourceList = new ArrayList<String>(1);
		LightSourceList.add(Light_Source);
	}
	
	// Read Light Source Parameters Information
	public void readExternal_LightSourceParam(DataInputStream in, int no_of_chars) throws IOException, ClassNotFoundException {
		
		Light_Source_Param = read_string(in, no_of_chars).trim();
		
	}

	// Read Location Information
	public void readExternal_Location(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		Location = read_string(in, no_of_chars).trim();
		ArrayList<String> LocationList = new ArrayList<String>(1);
		LocationList.add(Location);
	}

	// Read Measurement Time Information
	public void readExternal_MeasurementTime(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		Measurement_Time = read_string(in, no_of_chars).trim();
		ArrayList<String> MeasurementT_List = new ArrayList<String>(1);
		MeasurementT_List.add(Measurement_Time);
	}

	// Read Weather Information
	public void readExternal_Weather(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		Weather = read_string(in, no_of_chars).trim();
		ArrayList<String> WeatherList = new ArrayList<String>(1);
		WeatherList.add(Weather);
	}

	// Read White Reference Target Information
	public void readExternal_WR_Target(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		White_Reference_Target = read_string(in, no_of_chars).trim();
		ArrayList<String> WR_Target_List = new ArrayList<String>(1);
		WR_Target_List.add(White_Reference_Target);
	}
	
	// Read Processing Information
	public void readExternal_Processing(DataInputStream in, int no_of_chars) throws IOException, ClassNotFoundException{
		
		Processing = read_string(in, no_of_chars).trim();
		ArrayList<String> Processing_List = new ArrayList<String>(1);
		Processing_List.add(Processing);
	}

	// Read Raw Data Format Information
	public void readExternal_RawDataFormat(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		RawDataFormat = read_string(in, no_of_chars).trim();
		ArrayList<String> RawDataFormat_List = new ArrayList<String>(1);
		RawDataFormat_List.add(RawDataFormat);
	}

	// Read Source File Information
	public void readExternal_SourceFile(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		SourceFile = read_string(in, no_of_chars).trim();
		ArrayList<String> SourceFileList = new ArrayList<String>(1);
		SourceFileList.add(SourceFile);
	}

	// Read Description Information
	public void readExternal_Description(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		Description = read_string(in, no_of_chars).trim();
		ArrayList<String> DescriptionList = new ArrayList<String>(1);
		DescriptionList.add(Description);
	}

//	// Read Keywords Information
//	public void readExternal_Keywords(DataInputStream in, int no_of_chars)
//			throws IOException, ClassNotFoundException {
//
//			Keywords = read_string(in, no_of_chars);
//			ArrayList<String> KeywordsList = new ArrayList<String>(2);
//			KeywordsList.add(Keywords);
//	}

	// Read Name
	public void readExternal_Name(DataInputStream in, int no_of_chars)
			throws IOException, ClassNotFoundException {

		Name = read_string(in, no_of_chars).trim();
		ArrayList<String> NameList = new ArrayList<String>(1);
		NameList.add(Name);
	}

	public static String read_string(DataInputStream in, int no_of_chars)
			throws IOException {
		byte[] bytes = new byte[no_of_chars];
		in.read(bytes);
		
		int cnt = 0;
		
		while (bytes[cnt] != 0) cnt ++;
		
		byte[] tmp = new byte[cnt];
		
		System.arraycopy(bytes, 0, tmp, 0, cnt); // Copies 2, 3, 4 into dst
		
		return new String(tmp);
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

	protected Float read_float(DataInputStream in) throws IOException {
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
	
//	public double DDDmm2DDDdecimals(double in)
//	{		
//		// reformat to dd.mmmmmmmmm
//		int deg = (int)in/100;
//		double min = (in - deg*100)/60;		
//		
//		return(deg + min);		
//	}

}
