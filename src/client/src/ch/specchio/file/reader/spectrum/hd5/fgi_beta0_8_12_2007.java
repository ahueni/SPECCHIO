package ch.specchio.file.reader.spectrum.hd5;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import ch.specchio.types.spatial_pos;

import java.io.File;

public class fgi_beta0_8_12_2007 {

	private static String DATASETNAME1 = "DataPoints";
	private static final int DIM0 = -1;
	private static final int ADIM0 = -1;

	public ArrayList<Measurement> m = new ArrayList<Measurement>();

	public int no_of_measurements;
	public int no_of_wvls;
	public Float[] Wavelength;

	public String Authors;
	public String Campaign;
	public String GPS;
	public String Instrument_Name;
	public String[] Instrument_Parameters;
	public String Light_Source;
	public String Location;
	public String Measurement_Time;
	public String Weather;
	public String White_Reference_Target;
	public String RawDataFormat;
	public String SourceFile;
	public String Description;
	public int Keyword_dims;
	public String[] Keywords;
	public String Name;
	public spatial_pos gps_pos;
	public String Processing_one_string;

	// Metadata from the xml file
	public String title;
	public String location;
	public String date;
	public String Measurer;
	public String[] Processing;
	public String Extra_info;
	public String Target_Name;
	public String H5_file;

	public ArrayList<Information> inf = new ArrayList<Information>();
	public ArrayList<fgi_beta0_8_12_2007> metadata = new ArrayList<fgi_beta0_8_12_2007>();

	public void read_Data(String FILENAME) {
		int file_id = -1;
		int dataset_id = -1;
		int dataspace_id = -1;
		int dset_datatype = -1;
		long size = -1;
		int rank = -1;
		long[] dims = { DIM0 };
		long dset_dims = -1;
		int nmembers = -1;
		int rank_Refl = -1;
		int memory_datatype = -1;
		int[] member_class = new int[5];
		String[] membernames = new String[5];
		int[] membertypes = new int[5];
		long[] adimsRefl = { ADIM0 };
		byte[] buffer;
		int[] member_size = new int[5];
		int read_successful = -1;

		// Open an existing file.
		try {
			file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Open an existing dataset.
		try {
			if (file_id >= 0)
				dataset_id = H5.H5Dopen(file_id, DATASETNAME1,
						HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the identifier of the compound datatype in the file
		try {
			if (file_id >= 0)
				dset_datatype = H5.H5Dget_type(dataset_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the size of the datatype
		try {
			if (dset_datatype >= 0)
				size = H5.H5Tget_size(dset_datatype);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get the number of compound members in the dataset
		try {
			if (dset_datatype >= 0)
				nmembers = H5.H5Tget_nmembers(dset_datatype);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < nmembers; i++) {
				member_class[i] = H5.H5Tget_member_class(dset_datatype, i);
				membernames[i] = H5.H5Tget_member_name(dset_datatype, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get membertypes
		try {
			if (dset_datatype >= 0) {
				for (int i = 0; i < nmembers; i++)
					membertypes[i] = H5.H5Tget_member_type(dset_datatype, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the sizes of every member
		try {
			if (dset_datatype >= 0) {
				for (int i = 0; i < nmembers; i++)
					member_size[i] = H5.H5Tget_size(membertypes[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get dataspace and allocate memory for read buffer.
		try {
			if (dataset_id >= 0)
				dataspace_id = H5.H5Dget_space(dataset_id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (dataspace_id >= 0)
				rank = H5.H5Sget_simple_extent_ndims(dataspace_id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the dimension of the compound Dataset
		try {
			if (dataspace_id >= 0)
				H5.H5Sget_simple_extent_dims(dataspace_id, dims, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the array dimension of the included arrays in the dataset
		try {
			if (dataspace_id >= 0) {
				H5.H5Tget_array_dims(membertypes[4], adimsRefl);
				rank_Refl = H5.H5Tget_array_ndims(membertypes[4]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Reading a dataset using H5Tget_native_type
		try {
			if (dset_datatype >= 0)
				memory_datatype = H5.H5Tget_native_type(dset_datatype,
						HDF5Constants.H5T_DIR_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create the buffer for reading in the byte-Stream.
		buffer = new byte[(int) dims[0] * (int) size];

		// Read data
		try {
			if ((dset_datatype >= 0) && (memory_datatype >= 0))
				read_successful = H5.H5Dread(dataset_id, memory_datatype,
						HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
						HDF5Constants.H5P_DEFAULT, buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(
				buffer));

		no_of_measurements = (int) dims[0];

		for (int i = 0; i < dims[0]; i++) {

			Measurement m_tmp = new Measurement((int) adimsRefl[0]);

			try {
				m_tmp.readExternal_fgi_beta08(in);
				m.add(m_tmp);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}


	}

	public void read_info(String FILENAME) {
		double latitude;
		double longitude;

		try {

			File fXmlFile = new File(FILENAME);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("brflib_metadata");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					title = getTagValue("title", eElement).trim();
					date = getTagValue("date", eElement).trim();
					Location = getTagValue("location", eElement).trim();
					Light_Source = getTagValue("light", eElement).trim();
					Instrument_Name = getTagValue("instrument", eElement)
							.trim();
					White_Reference_Target = getTagValue("whitereference",
							eElement).trim();
					String measurer = getTagValue("measurer", eElement).trim();
					Measurer = measurer.trim();
					// String[] tokens = measurer.split(",");
					// Measurer = new String[tokens.length];
					// for(int i = 0; i < tokens.length; i++){
					// Measurer[i] = tokens[i].trim();
					// }
					Weather = getTagValue("weather", eElement).trim();
					String processing = getTagValue("processing", eElement)
							.trim();
					String[] tokens1 = processing.split(",");
					Processing = new String[tokens1.length];
					for (int j = 0; j < tokens1.length; j++) {
						Processing[j] = tokens1[j].trim();
					}
					Processing_one_string = Processing[0];
					for (int i = 1; i < Processing.length; i++) {
						Processing_one_string = Processing_one_string.concat(
								", ").concat(Processing[i]);
					}
					Extra_info = getTagValue("extrainfo", eElement).trim();
					Target_Name = getTagValue("target_name", eElement).trim();
					Description = getTagValue("description", eElement).trim();
					if (Description.contains("#GPS")) {
						String[] description_tokens = Description.split("#");
						String[] gps_tokens = description_tokens[1].split("\t");
						double deg_lat = Double.parseDouble(gps_tokens[1]
								.substring(0, 2));
						double min_lat = Double.parseDouble(gps_tokens[1]
								.substring(2, 8));
						double deg_long = Double.parseDouble(gps_tokens[2]
								.substring(0, 3));
						double min_long = Double.parseDouble(gps_tokens[2]
								.substring(3, 9));

						if (gps_tokens[1].contains("N"))
							latitude = deg_lat + (min_lat / 60);
						else
							latitude = -1 * (deg_lat + (min_lat / 60));
						if (gps_tokens[2].contains("W"))
							longitude = deg_long + (min_long / 60);
						else
							longitude = -1 * (deg_long + (min_long / 60));

						gps_pos = new spatial_pos();
						gps_pos.latitude = latitude;
						gps_pos.longitude = longitude;
						
						Description = description_tokens[0];
					} else {
						gps_pos = new spatial_pos();
						gps_pos.location_name = Location;
					}

					String keywords = getTagValue("keywords", eElement).trim();
					String[] tokens2 = keywords.split(",");
					Keywords = new String[tokens2.length];
					for (int z = 0; z < tokens2.length; z++) {
						Keywords[z] = tokens2[z].trim();
					}
					H5_file = getTagValue("h5file", eElement).trim();
					String No_of_spectra = getTagValue("N_spectra", eElement)
							.trim();
					no_of_measurements = Integer.parseInt(No_of_spectra);
					String No_wvls = getTagValue("nwavelengths", eElement)
							.trim();
					no_of_wvls = Integer.parseInt(No_wvls);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

	// public static void main(String[] args) {
	// String FILENAME =
	// "/Users/dkuekenb/Documents/HDF5_Examplefiles/Example_files_h5_xml/dwarfbedited.brflib_g.h5";
	// String xml_FILENAME =
	// "/Users/dkuekenb/Documents/HDF5_Examplefiles/Example_files_h5_xml/dwarfbedited.brflib_g.xml";
	// fgi_beta0_8_12_2007 test = new fgi_beta0_8_12_2007();
	// test.read_Data(FILENAME);
	// test.read_info(xml_FILENAME);
	// }

}
