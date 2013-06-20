package ch.specchio.file.reader.spectrum.hd5;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class fgi_2008a {

	private static String DATASETNAME1 = "DataPoints";
	private static String DATASETNAME2 = "Wavelengths";
	private static String DATASETNAME3 = "DataStructureVersion";
	private static final int DIM0 = 0;
	private static final int ADIM0 = 0;
	private static final int ADIM1 = 0;
	private static final int ADIM2 = 0;

	private static String DATA = "Data";
	private static String[] DATA_OBJECTS;
	private static String INFO = "Info";
	private static String[] INFO_OBJECTS = { "Authors", "Measurement",
			"RawData", "Target" };

	// private static String[] MEASUREMENT_OBJECTS = { "Campaign", "GPS",
	// "InstrumentName", "InstrumentParameters", "LightSource",
	// "Location", "MeasurementTime", "Weather", "WhiteReferenceTarget" };
	private static String[] MEASUREMENT_OBJECTS;

	private static String[] RAWDATA_OBJECTS;
	private static String[] TARGET_OBJECTS = { "Description", "Keywords",
			"Name" };

	public ArrayList<Measurement> m = new ArrayList<Measurement>();

	public int no_of_measurements;
	public int no_of_wvls;
	public Float[] Wavelength;
	public String[][] calculationProcesses;
	public int inst_param_dims;
	public int no_of_spec_types;
	public int no_of_calc_proc;

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
	// public ArrayList<String> Instrument_Parameters_List = new
	// ArrayList<String>();

	public ArrayList<Information> inf = new ArrayList<Information>();

	// Information n_tmp = new Information();

	public void getIdentifiers(String FILENEMAE) {

	}

	public void read_Data(String FILENAME, String ds_version) {
		int file_id = -1;
		int group_id = -1;
		int[] dataset_id;
		int[] dataspace_id;
		int[] dset_datatype;
		long[] size;
		int[] rank;
		long[] dims = new long[1];
		long[] dset_dims;
		int nmembers_DataPoints = -1;
		int nmembers_SpectrumTypes = -1;
		int rank_Refl = -1;
		int rank_UTC = -1;
		long[] adimsRefl = { ADIM0 };
		long[] adimsUTC = { ADIM1 };
		long[] adimsCalcProc = { ADIM2 };
		String[] datapoints_membernames;
		int[] datapoints_member_class;
		String[] spectrumtypes_membernames;
		int[] spectrumtypes_member_class;
		int memtype = -1;
		long[] offset;
		int[] datapoints_membertypes;
		int[] spectrumtypes_membertypes;
		int test = -1;
		int[] member_insert;
		int[] member_size;
		int[] member_size_spectrumTypes;
		int LightAz_dataset = -1;
		int memory_datatype = -1;
		int LightAz_dataspace = -1;
		int read_successful = -1;
		int LightAz_dataset_id = -1;
		byte[] buffer1;
		byte[] buffer2;
		byte[] buffer3;
		int n_objects = -1;
		int refl_ident = -1;
		int utc_ident = -1;
		int pol_ident = -1;
		int spec_type_ident = -1;
		byte[][] buffer;
		int utc_type = -1;

		String data_structure_version = ds_version;
		// Open an existing file.
		try {
			file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Open Group
		try {
			if (file_id >= 0)
				group_id = H5.H5Gopen(file_id, DATA, HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the number of Objects in the group
		try {
			if (group_id >= 0)
				n_objects = H5.H5Gn_members(file_id, DATA);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DATA_OBJECTS = new String[n_objects];
		dataset_id = new int[n_objects];
		dataspace_id = new int[n_objects];
		dset_datatype = new int[n_objects];
		size = new long[n_objects];
		rank = new int[n_objects];
		dset_dims = new long[n_objects];
		// Get the names of the datasets within the group
		try {
			if (group_id >= 0)
				for (int i = 0; i < n_objects; i++)
					DATA_OBJECTS[i] = H5.H5Lget_name_by_idx(file_id, DATA,
							HDF5Constants.H5P_DEFAULT, 0, i,
							HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataset_id = new int[n_objects];
		// Open all Datasets and safe the dataset_ids into an array
		try {
			if (group_id >= 0)
				for (int i = 0; i < DATA_OBJECTS.length; i++)
					dataset_id[i] = H5.H5Dopen(group_id, DATA_OBJECTS[i],
							HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the identifier of the datatype in the file
		try {
			if (file_id >= 0)
				for (int i = 0; i < DATA_OBJECTS.length; i++)
					dset_datatype[i] = H5.H5Dget_type(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the storagesize of the dataset
		try {
			for (int i = 0; i < DATA_OBJECTS.length; i++)
				size[i] = H5.H5Dget_storage_size(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get the dataspace of the dataset and the dimensionality of the
		// dataset
		try {
			for (int i = 0; i < DATA_OBJECTS.length; i++)
				if (dataset_id[i] >= 0)
					dataspace_id[i] = H5.H5Dget_space(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < DATA_OBJECTS.length; i++)
				if (dataspace_id[i] >= 0)
					rank[i] = H5.H5Sget_simple_extent_ndims(dataspace_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get the dimensions of the dataset
		try {
			for (int i = 0; i < DATA_OBJECTS.length; i++) {
				if (dataspace_id[i] >= 0)
					H5.H5Sget_simple_extent_dims(dataspace_id[i], dims, null);
				dset_dims[i] = dims[0];
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// get the number of compound members in the dataset DataPoints
		try {
			if (dset_datatype[0] >= 0)
				nmembers_DataPoints = H5.H5Tget_nmembers(dset_datatype[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		datapoints_membernames = new String[nmembers_DataPoints];
		datapoints_member_class = new int[nmembers_DataPoints];
		offset = new long[nmembers_DataPoints];
		datapoints_membertypes = new int[nmembers_DataPoints];
		member_insert = new int[nmembers_DataPoints];
		member_size = new int[nmembers_DataPoints];

		try {
			for (int i = 0; i < nmembers_DataPoints; i++) {
				datapoints_member_class[i] = H5.H5Tget_member_class(
						dset_datatype[0], i);
				datapoints_membernames[i] = H5.H5Tget_member_name(
						dset_datatype[0], i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the number of compound members in the dataset SpectrumTypes
		try {
			if (dset_datatype[1] >= 0)
				nmembers_SpectrumTypes = H5.H5Tget_nmembers(dset_datatype[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		spectrumtypes_membernames = new String[nmembers_SpectrumTypes];
		spectrumtypes_member_class = new int[nmembers_SpectrumTypes];
		spectrumtypes_membertypes = new int[nmembers_SpectrumTypes];
		member_size_spectrumTypes = new int[nmembers_SpectrumTypes];

		try {
			for (int i = 0; i < nmembers_SpectrumTypes; i++) {
				spectrumtypes_member_class[i] = H5.H5Tget_member_class(
						dset_datatype[1], i);
				spectrumtypes_membernames[i] = H5.H5Tget_member_name(
						dset_datatype[1], i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get membertypes
		try {
			if (dataset_id[0] >= 0) {
				for (int i = 0; i < nmembers_DataPoints; i++)
					datapoints_membertypes[i] = H5.H5Tget_member_type(
							dset_datatype[0], i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (dataset_id[0] >= 0) {
				for (int i = 0; i < nmembers_SpectrumTypes; i++)
					spectrumtypes_membertypes[i] = H5.H5Tget_member_type(
							dset_datatype[1], i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the identifier for the Reflectance ant UTC_Time member
		for (int i = 0; i < nmembers_DataPoints; i++) {
			if (datapoints_membernames[i].equalsIgnoreCase("Spectrum"))
				refl_ident = i;
			if (datapoints_membernames[i].equalsIgnoreCase("UTC_Time"))
				utc_ident = i;
			if (datapoints_membernames[i].equalsIgnoreCase("Polarization"))
				pol_ident = i;
			if (datapoints_membernames[i].equalsIgnoreCase("SpectrumType"))
				spec_type_ident = i;

		}

		// Get the array dimension of the included arrays in the dataset
		try {
			if (dataspace_id[0] >= 0) {
				H5.H5Tget_array_dims(datapoints_membertypes[refl_ident],
						adimsRefl);
				rank_Refl = H5
						.H5Tget_array_ndims(datapoints_membertypes[refl_ident]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (dataspace_id[0] >= 0 && utc_ident > -1) {
				H5.H5Tget_array_dims(datapoints_membertypes[utc_ident],
						adimsUTC);
				rank_UTC = H5
						.H5Tget_array_ndims(datapoints_membertypes[utc_ident]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get data type of values in utc_time array
		try {
			if (utc_ident > -1) {
				int utc_super = H5
						.H5Tget_super(datapoints_membertypes[utc_ident]);
				utc_type = H5.H5Tget_class(utc_super);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get the sizes of every member
		try {
			if (dset_datatype[0] >= 0) {
				for (int i = 0; i < nmembers_DataPoints; i++)
					member_size[i] = H5.H5Tget_size(datapoints_membertypes[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (dset_datatype[1] >= 0) {
				for (int i = 0; i < nmembers_SpectrumTypes; i++)
					member_size_spectrumTypes[i] = H5
							.H5Tget_size(spectrumtypes_membertypes[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create buffers for reading the byte stream

		buffer1 = new byte[(int) size[0]];
		buffer2 = new byte[(int) size[1]];
		buffer3 = new byte[(int) size[2]];

		// Read data
		try {
			if ((dataset_id[0] >= 0) && (dset_datatype[0] >= 0))
				read_successful = H5.H5Dread(dataset_id[0], dset_datatype[0],
						HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
						HDF5Constants.H5P_DEFAULT, buffer1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(
				buffer1));

		no_of_measurements = (int) dset_dims[0];

		for (int i = 0; i < no_of_measurements; i++) {

			Measurement m_tmp = new Measurement((int) adimsRefl[0],
					(int) adimsUTC[0], (int) member_size[pol_ident],
					(int) member_size[spec_type_ident]);

			try {
				m_tmp.readExternal(in, datapoints_membernames,
						data_structure_version, utc_type);
				m.add(m_tmp);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}

		// Get the array dimension of the included arrays in the dataset
		try {
			if (dataspace_id[1] >= 0) {
				H5.H5Tget_array_dims(spectrumtypes_membertypes[1],
						adimsCalcProc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Read SpectrumTypes Table (under Data)
		try {
			if ((dataset_id[1] >= 0) && (dset_datatype[1] >= 0))
				read_successful = H5.H5Dread(dataset_id[1], dset_datatype[1],
						HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
						HDF5Constants.H5P_DEFAULT, buffer2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DataInputStream in2 = new DataInputStream(new ByteArrayInputStream(
				buffer2));

		no_of_spec_types = (int) dset_dims[1];
		no_of_calc_proc = (int) adimsCalcProc[0];
		calculationProcesses = new String[no_of_spec_types][(int) adimsCalcProc[0]];

		for (int i = 0; i < dset_dims[1]; i++) {
			//
			// Measurement m_tmp = m.get(i);
			//
			try {
				readExternal_spectrumTypes(in2, member_size_spectrumTypes,
						(int) adimsCalcProc[0], i);
				// m.add(m_tmp);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			//
			// try {
			// m_tmp.readExternal_specType_noUse(in2,
			// member_size_spectrumTypes, (int) adimsCalcProc[0]);
			// } catch (IOException e) {
			// e.printStackTrace();
			// } catch (ClassNotFoundException e) {
			// e.printStackTrace();
			// }
			// } else if (m_tmp.spectrum_type.equals("BRF") && i == 0) {
			// try {
			// m_tmp.readExternal_specType_noUse(in2,
			// member_size_spectrumTypes, (int) adimsCalcProc[0]);
			// } catch (IOException e) {
			// e.printStackTrace();
			// } catch (ClassNotFoundException e) {
			// e.printStackTrace();
			// }
			// } else if (m_tmp.spectrum_type.equals("BRF") && i == 1) {
			// try {
			// m_tmp.readExternal_spectrumTypes_BRF(in2,
			// member_size_spectrumTypes, (int) adimsCalcProc[0]);
			// m.add(m_tmp);
			// } catch (IOException e) {
			// e.printStackTrace();
			// } catch (ClassNotFoundException e) {
			// e.printStackTrace();
			// }

			// }

		}

		// Read Wavelengths

		no_of_wvls = (int) dset_dims[2];
		Wavelength = new Float[no_of_wvls];

		try {
			if ((dataset_id[2] >= 0) && (dset_datatype[2] >= 0))
				read_successful = H5.H5Dread(dataset_id[2], dset_datatype[2],
						HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
						HDF5Constants.H5P_DEFAULT, buffer3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DataInputStream in3 = new DataInputStream(new ByteArrayInputStream(
				buffer3));
		try {
			readExternal_Wavelength(in3);
			// m_tmp.readExternal_Wavelength(in);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	// Read SpectrumTypes table
	public void readExternal_spectrumTypes(DataInputStream in2,
			int[] member_size_spectrumTypes, int dimsCalcProc, int i)
			throws IOException, ClassNotFoundException {

		String spectrum_type = read_string(in2, member_size_spectrumTypes[0])
				.trim();
		for (int j = 0; j < dimsCalcProc; j++) {
			calculationProcesses[i][j] = read_string(in2,
					member_size_spectrumTypes[1] / 2).trim();
		}
	}

	public void readExternal_Wavelength(DataInputStream in3)
			throws IOException, ClassNotFoundException {

		for (int i = 0; i < no_of_wvls; i++) {
			Wavelength[i] = Measurement.read_float(in3);

		}

	}

	public void read_Data_Structure_Version(String FILENAME) {
		int file_id = -1;
		int group_id = -1;
		int dataset_id = -1;
		int dset_datatype = -1;
		long size = -1;
		int dataspace_id = -1;
		int rank = -1;
		long[] dims = { DIM0 };
		byte[] buffer;
		int read_successful = -1;

		// Open an existing file.
		try {
			file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Open dataset
		try {
			dataset_id = H5.H5Dopen(file_id, DATASETNAME3,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get the identifier of the datatype in the file
		try {
			if (file_id >= 0)
				dset_datatype = H5.H5Dget_type(dataset_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get the storagesize of the dataset
		try {
			size = H5.H5Dget_storage_size(dataset_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get the dataspace of the dataset and the dimensionality of the
		// dataset
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
		// Get the dimensions of the dataset
		try {
			if (dataspace_id >= 0)
				H5.H5Sget_simple_extent_dims(dataspace_id, dims, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create buffer for reading in the data
		buffer = new byte[(int) size];

		// Read Data
		try {
			if ((dataset_id >= 0) && (dset_datatype >= 0))
				read_successful = H5.H5Dread(dataset_id, dset_datatype,
						HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
						HDF5Constants.H5P_DEFAULT, buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(
				buffer));

		for (int i = 0; i < dims[0]; i++) {

			// this.ds_version = new Measurement((int) size);

			Measurement m_tmp = m.get(i);
			m_tmp.no_of_chars = (int) size;

			try {
				m_tmp.readExternal_DStruct_Vers(in, (int) size);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}

	}

	public void read_Info_Group(String FILENAME) {
		int[] dataset_id;
		int file_id = -1;
		int group_id = -1;
		int[] dset_datatype;
		long[] size;
		int[] dataspace_id;
		int[] rank;
		long[] dims;
		byte[] buffer;
		int read_successful = -1;
		int n_objects = -1;
		String[] object_names;
		int[] object_types;
		String[] onames = new String[1];
		int[] otypes = new int[1];
		int no_of_dset = 0;
		long[] dset_dims;
		String[] datasets;
		long[] member_size;

		// Open existing File
		try {
			file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Open Info group
		try {
			if (file_id >= 0)
				group_id = H5.H5Gopen(file_id, INFO, HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the number of Objects in the group
		try {
			if (group_id >= 0)
				n_objects = H5.H5Gn_members(file_id, INFO);
		} catch (Exception e) {
			e.printStackTrace();
		}

		object_names = new String[n_objects];
		object_types = new int[n_objects];

		// Get names and types of all objects

		for (int i = 0; i < n_objects; i++) {
			try {
				if (group_id >= 0)
					H5.H5Gget_obj_info_idx(file_id, INFO, i, onames, otypes);
			} catch (Exception e) {
				e.printStackTrace();
			}
			object_names[i] = onames[0];
			object_types[i] = otypes[0];
		}

		// Get number of datasets in Group
		for (int i = 0; i < object_types.length; i++) {
			if (object_types[i] == 1)
				no_of_dset++;
		}
		datasets = new String[no_of_dset];

		int z = 0;

		// Get an array with all the dataset names in the group in it
		for (int i = 0; i < object_names.length; i++) {
			if (object_types[i] == 1) {
				datasets[z] = object_names[i];
				z++;
			}
		}

		dataset_id = new int[no_of_dset];
		dset_datatype = new int[no_of_dset];
		size = new long[no_of_dset];
		dataspace_id = new int[no_of_dset];
		rank = new int[no_of_dset];
		dims = new long[no_of_dset];
		dset_dims = new long[no_of_dset];
		member_size = new long[no_of_dset];

		// Open all dataset
		for (int i = 0; i < no_of_dset; i++) {
			try {
				dataset_id[i] = H5.H5Dopen(group_id, datasets[i],
						HDF5Constants.H5P_DEFAULT);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// get the identifier of the datatype in the file
			try {
				if (file_id >= 0)
					dset_datatype[i] = H5.H5Dget_type(dataset_id[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Get the storagesize of the dataset
			try {
				size[i] = H5.H5Dget_storage_size(dataset_id[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Get the dataspace of the dataset and the dimensionality of the
			// dataset
			try {
				if (dataset_id[i] >= 0)
					dataspace_id[i] = H5.H5Dget_space(dataset_id[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (dataspace_id[i] >= 0)
					rank[i] = H5.H5Sget_simple_extent_ndims(dataspace_id[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Get the dimensions of the dataset
			try {
				if (dataspace_id[i] >= 0)
					H5.H5Sget_simple_extent_dims(dataspace_id[i], dims, null);
				dset_dims[i] = dims[0];
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Get membersizes
			try {
				if (dataset_id[i] >= 0)
					member_size[i] = H5.H5Tget_size(dset_datatype[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		Information n_tmp = new Information();
		
		for (int i = 0; i < no_of_dset; i++) {
			// Create buffer for reading in the data
			buffer = new byte[(int) size[i]];

			// Read Data
			try {
				if ((dataset_id[i] >= 0) && (dset_datatype[i] >= 0))
					read_successful = H5.H5Dread(dataset_id[i],
							dset_datatype[i], HDF5Constants.H5S_ALL,
							HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
							buffer);
			} catch (Exception e) {
				e.printStackTrace();
			}
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(
					buffer));

			// for (int j = 0; j < dset_dims[i]; j++) {

//			Information n_tmp = new Information();

			try {
				n_tmp.readExternal_String(in, (int) member_size[i],
						datasets[i], dset_dims[i]);
//				inf.add(n_tmp);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			// }
		}
		inf.add(n_tmp);
	}

	public void read_Measurement_Group(String FILENAME) {
		int file_id = -1;
		int info_group_id = -1;
		int measurement_group_id = -1;
		int[] dataset_id;
		int[] dset_datatype;
		long[] size;
		int[] dataspace_id;
		int[] rank;
		long[] dims = new long[1];
		long[] dset_dims;
		byte[] buffer1;
		byte[] buffer2;
		byte[] buffer3;
		byte[] buffer4;
		byte[] buffer5;
		byte[] buffer6;
		byte[] buffer7;
		byte[] buffer8;
		byte[] buffer9;
		byte[] buffer10;
		byte[] dset_buffers;
		int read_successful;
		long totalSize;
		byte[] buffer;
		int size_test;
		long no_of_char;
		long[] member_size;
		int n_objects = -1;
		int inst_param_ident = -1;

		// Open existing File
		try {
			file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Open Info Group
		try {
			if (file_id >= 0)
				info_group_id = H5.H5Gopen(file_id, INFO,
						HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Open Measurement Group
		try {
			if (info_group_id >= 0)
				measurement_group_id = H5.H5Gopen(info_group_id, "Measurement",
						HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get the number of Objects in the group
		try {
			if (measurement_group_id >= 0)
				n_objects = H5.H5Gn_members(info_group_id, "Measurement");
		} catch (Exception e) {
			e.printStackTrace();
		}

		MEASUREMENT_OBJECTS = new String[n_objects];
		// Get the names of the datasets within the group
		try {
			if (measurement_group_id >= 0)
				for (int i = 0; i < n_objects; i++)
					MEASUREMENT_OBJECTS[i] = H5.H5Lget_name_by_idx(
							info_group_id, "Measurement",
							HDF5Constants.H5P_DEFAULT, 0, i,
							HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataset_id = new int[n_objects];
		// Open all Datasets and safe the dataset_ids into an array
		try {
			if (measurement_group_id >= 0)
				for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++)
					dataset_id[i] = H5.H5Dopen(measurement_group_id,
							MEASUREMENT_OBJECTS[i], HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dset_datatype = new int[n_objects];
		// get the identifier of the datatype in the file
		try {
			if (file_id >= 0)
				for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++)
					dset_datatype[i] = H5.H5Dget_type(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		size = new long[n_objects];
		// Get the storagesize of the dataset
		try {
			for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++)
				size[i] = H5.H5Dget_storage_size(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataspace_id = new int[n_objects];
		rank = new int[n_objects];
		// Get the dataspace of the dataset and the dimensionality of the
		// dataset
		try {
			for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++)
				if (dataset_id[i] >= 0)
					dataspace_id[i] = H5.H5Dget_space(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++)
				if (dataspace_id[i] >= 0)
					rank[i] = H5.H5Sget_simple_extent_ndims(dataspace_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dset_dims = new long[n_objects];
		// Get the dimensions of the dataset
		try {
			for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++) {
				if (dataspace_id[i] >= 0)
					H5.H5Sget_simple_extent_dims(dataspace_id[i], dims, null);
				dset_dims[i] = dims[0];
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++) {
			if (MEASUREMENT_OBJECTS[i].equalsIgnoreCase("InstrumentParameters"))
				inst_param_ident = i;
		}

		if (inst_param_ident > -1)
			inst_param_dims = (int) dset_dims[inst_param_ident];

		member_size = new long[MEASUREMENT_OBJECTS.length];
		// Get membersizes
		for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++) {
			try {
				member_size[i] = H5.H5Tget_size(dset_datatype[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++) {

			buffer = new byte[(int) size[i]];

			try {
				if ((dataset_id[i] >= 0) && (dset_datatype[i] >= 0))
					read_successful = H5.H5Dread(dataset_id[i],
							dset_datatype[i], HDF5Constants.H5S_ALL,
							HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
							buffer);
			} catch (Exception e) {
				e.printStackTrace();
			}

			DataInputStream in = new DataInputStream(new ByteArrayInputStream(
					buffer));

			Information n_tmp = inf.get(0);

			try {
				n_tmp.readExternal_String(in, (int) member_size[i],
						MEASUREMENT_OBJECTS[i], dset_dims[i]);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	// totalSize = size[0] + size[1] + size[2] + size[3] + size[4] + size[5]
	// + size[6] + size[7] + size[8];
	// buffer = new byte[(int)totalSize];

	// dset_buffers = new byte[MEASUREMENT_OBJECTS.length]
	// for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++)
	// dset_buffers = new byte[MEASUREMENT_OBJECTS.length * (int)size[i]];
	// dset_buffers[i] = new byte[(int) size[i]];

	// buffer1 = new byte[(int) size[0]];
	// buffer2 = new byte[(int) size[1]];
	// buffer3 = new byte[(int) size[2]];
	// buffer4 = new byte[(int) size[3]];
	// buffer5 = new byte[(int) size[4]];
	// buffer6 = new byte[(int) size[5]];
	// buffer7 = new byte[(int) size[6]];
	// buffer8 = new byte[(int) size[7]];
	// buffer9 = new byte[(int) size[8]];
	//
	// // byte[] buffer = {buffer1, buffer2, buffer3, buffer4, buffer5,
	// // buffer6, buffer7, buffer8, buffer9};
	//
	// // Das einlesen in die buffers muss wahrscheinlich einzeln verlaufen und
	// // kann nicht ueber
	// // eine Schlaufe geschehen. oder wie sollte das gehen????
	//
	// // Read Campaign
	// try {
	// if ((dataset_id[0] >= 0) && (dset_datatype[0] >= 0))
	// read_successful = H5.H5Dread(dataset_id[0], dset_datatype[0],
	// HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
	// HDF5Constants.H5P_DEFAULT, buffer1);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in1 = new DataInputStream(new ByteArrayInputStream(
	// buffer1));
	//
	// for (int i = 0; i < dset_dims[0]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_Campaign(in1, (int) size[0]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read GPS
	// try {
	// if ((dataset_id[1] >= 0) && (dset_datatype[1] >= 0))
	// read_successful = H5.H5Dread(dataset_id[1], dset_datatype[1],
	// HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
	// HDF5Constants.H5P_DEFAULT, buffer2);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in2 = new DataInputStream(new ByteArrayInputStream(
	// buffer2));
	//
	// for (int i = 0; i < dset_dims[1]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_GPS(in2, (int) size[1]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read Instrument Name
	// try {
	// if ((dataset_id[2] >= 0) && (dset_datatype[2] >= 0))
	// read_successful = H5.H5Dread(dataset_id[2], dset_datatype[2],
	// HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
	// HDF5Constants.H5P_DEFAULT, buffer3);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in3 = new DataInputStream(new ByteArrayInputStream(
	// buffer3));
	//
	// for (int i = 0; i < dset_dims[2]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_InstrumentName(in3, (int) size[2]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read Instrument Parameters
	// try {
	// if ((dataset_id[3] >= 0) && (dset_datatype[3] >= 0))
	// read_successful = H5.H5Dread(dataset_id[3], dset_datatype[3],
	// HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
	// HDF5Constants.H5P_DEFAULT, buffer4);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in4 = new DataInputStream(new ByteArrayInputStream(
	// buffer4));
	//
	// // for (int i = 0; i < dset_dims[3]; i++) {
	// //
	// // Information n_tmp = inf.get(i);
	//
	// try {
	// readExternal_InstrumentParameters(in4, (int) member_size);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// // }
	//
	// // Read Light Source
	// try {
	// if ((dataset_id[4] >= 0) && (dset_datatype[4] >= 0))
	// read_successful = H5.H5Dread(dataset_id[4], dset_datatype[4],
	// HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
	// HDF5Constants.H5P_DEFAULT, buffer5);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in5 = new DataInputStream(new ByteArrayInputStream(
	// buffer5));
	//
	// for (int i = 0; i < dset_dims[4]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_LightSource(in5, (int) size[4]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	// // Switch between reading routine for outside measurements and
	// // measurements in laboratories
	// if (MEASUREMENT_OBJECTS[5].equalsIgnoreCase("Location")) { // Reading
	// // routine
	// // for
	// // outside
	// // Measurements
	// // at day
	//
	// // Read Location
	// try {
	// if ((dataset_id[5] >= 0) && (dset_datatype[5] >= 0))
	// read_successful = H5.H5Dread(dataset_id[5],
	// dset_datatype[5], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer6);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in6 = new DataInputStream(new ByteArrayInputStream(
	// buffer6));
	//
	// for (int i = 0; i < dset_dims[5]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_Location(in6, (int) size[5]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read Measurement Time
	// try {
	// if ((dataset_id[6] >= 0) && (dset_datatype[6] >= 0))
	// read_successful = H5.H5Dread(dataset_id[6],
	// dset_datatype[6], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer7);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in7 = new DataInputStream(new ByteArrayInputStream(
	// buffer7));
	//
	// for (int i = 0; i < dset_dims[6]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_MeasurementTime(in7, (int) size[6]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read Weather
	//
	// try {
	// if ((dataset_id[7] >= 0) && (dset_datatype[7] >= 0))
	// read_successful = H5.H5Dread(dataset_id[7],
	// dset_datatype[7], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer8);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in8 = new DataInputStream(new ByteArrayInputStream(
	// buffer8));
	//
	// for (int i = 0; i < dset_dims[7]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_Weather(in8, (int) size[7]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	// // Read White Reference Target
	// try {
	// if ((dataset_id[8] >= 0) && (dset_datatype[8] >= 0))
	// read_successful = H5.H5Dread(dataset_id[8],
	// dset_datatype[8], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer9);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in9 = new DataInputStream(new ByteArrayInputStream(
	// buffer9));
	//
	// for (int i = 0; i < dset_dims[8]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_WR_Target(in9, (int) size[8]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// } else if (MEASUREMENT_OBJECTS[5]
	// .equalsIgnoreCase("LightSourceParameters")
	// && MEASUREMENT_OBJECTS.length == 9) { // Reading routine for
	// // Measurements in
	// // Laboratories (e.i.
	// // with
	// // Dataset "LightSourceParameters" and without DS "Weather")
	// // Read LightSourceParameters
	// try {
	// if ((dataset_id[5] >= 0) && (dset_datatype[5] >= 0))
	// read_successful = H5.H5Dread(dataset_id[5],
	// dset_datatype[5], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer6);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in6 = new DataInputStream(new ByteArrayInputStream(
	// buffer6));
	//
	// for (int i = 0; i < dset_dims[5]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_LightSourceParam(in6, (int) size[5]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read Location
	// try {
	// if ((dataset_id[6] >= 0) && (dset_datatype[6] >= 0))
	// read_successful = H5.H5Dread(dataset_id[6],
	// dset_datatype[6], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer7);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in7 = new DataInputStream(new ByteArrayInputStream(
	// buffer7));
	//
	// for (int i = 0; i < dset_dims[6]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_Location(in7, (int) size[6]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read Measurement Time
	// try {
	// if ((dataset_id[7] >= 0) && (dset_datatype[7] >= 0))
	// read_successful = H5.H5Dread(dataset_id[7],
	// dset_datatype[7], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer8);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in8 = new DataInputStream(new ByteArrayInputStream(
	// buffer8));
	//
	// for (int i = 0; i < dset_dims[7]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_MeasurementTime(in8, (int) size[7]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read White Reference Target
	// try {
	// if ((dataset_id[8] >= 0) && (dset_datatype[8] >= 0))
	// read_successful = H5.H5Dread(dataset_id[8],
	// dset_datatype[8], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer9);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in9 = new DataInputStream(new ByteArrayInputStream(
	// buffer9));
	//
	// for (int i = 0; i < dset_dims[8]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_WR_Target(in9, (int) size[8]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// } else if (MEASUREMENT_OBJECTS[5]
	// .equalsIgnoreCase("LightSourceParameters")
	// && MEASUREMENT_OBJECTS.length == 10) {
	// // Reading routine for outside measurements but with a lamp as light
	// // source
	//
	// // Read Light source parameters
	// try {
	// if ((dataset_id[5] >= 0) && (dset_datatype[5] >= 0))
	// read_successful = H5.H5Dread(dataset_id[5],
	// dset_datatype[5], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer6);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in6 = new DataInputStream(new ByteArrayInputStream(
	// buffer6));
	//
	// for (int i = 0; i < dset_dims[5]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_LightSourceParam(in6, (int) size[5]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read Location
	// try {
	// if ((dataset_id[6] >= 0) && (dset_datatype[6] >= 0))
	// read_successful = H5.H5Dread(dataset_id[6],
	// dset_datatype[6], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer7);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in7 = new DataInputStream(new ByteArrayInputStream(
	// buffer7));
	//
	// for (int i = 0; i < dset_dims[6]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_Location(in7, (int) size[6]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read Measurement Time
	// try {
	// if ((dataset_id[7] >= 0) && (dset_datatype[7] >= 0))
	// read_successful = H5.H5Dread(dataset_id[7],
	// dset_datatype[7], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer8);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in8 = new DataInputStream(new ByteArrayInputStream(
	// buffer8));
	//
	// for (int i = 0; i < dset_dims[7]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_MeasurementTime(in8, (int) size[7]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // Read Weather Information
	// try {
	// if ((dataset_id[8] >= 0) && (dset_datatype[8] >= 0))
	// read_successful = H5.H5Dread(dataset_id[8],
	// dset_datatype[8], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer9);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in9 = new DataInputStream(new ByteArrayInputStream(
	// buffer9));
	//
	// for (int i = 0; i < dset_dims[8]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_Weather(in9, (int) size[8]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// buffer10 = new byte[(int) size[9]];
	//
	// // Read White Reference Target
	// try {
	// if ((dataset_id[9] >= 0) && (dset_datatype[9] >= 0))
	// read_successful = H5.H5Dread(dataset_id[9],
	// dset_datatype[9], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer10);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in10 = new DataInputStream(
	// new ByteArrayInputStream(buffer10));
	//
	// for (int i = 0; i < dset_dims[9]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_WR_Target(in10, (int) size[9]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }
	//
	// }

	// // Read Instrument Parameter Information
	// public void readExternal_InstrumentParameters(DataInputStream in,
	// int no_of_chars) throws IOException, ClassNotFoundException {
	//
	// Instrument_Parameters = new String[inst_param_dims];
	//
	// for (int i = 0; i < inst_param_dims; i++)
	// Instrument_Parameters[i] = (read_string(in, no_of_chars)).trim();
	// // ArrayList<String> Instrument_Parameters_List = new
	// // ArrayList<String>();
	// // Instrument_Parameters_List.add(Instrument_Parameters);
	// }

	public static String read_string(DataInputStream in, int no_of_chars)
			throws IOException {
		byte[] bytes = new byte[no_of_chars];
		in.read(bytes);

		int cnt = 0;

		while (bytes[cnt] != 0)
			cnt++;

		byte[] tmp = new byte[cnt]; // instead of new byte[cnt + 1]

		System.arraycopy(bytes, 0, tmp, 0, cnt); // Copies 2, 3, 4 into dst

		return new String(tmp);
	}

	public void readRawData_Group(String FILENAME) {
		int file_id = -1;
		int info_group_id = -1;
		int raw_data_group_id = -1;
		int[] dataset_id;
		int[] dset_datatype;
		long[] size;
		int[] dataspace_id;
		int[] rank;
		long[] dims = new long[1];
		long[] dset_dims;
		byte[] buffer1;
		byte[] buffer2;
		byte[] buffer3;
		byte[] buffer;
		int read_successful;
		int n_objects = 0;
		long[] member_size;

		// Open existing File
		try {
			file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Open info group
		try {
			if (file_id >= 0)
				info_group_id = H5.H5Gopen(file_id, INFO,
						HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Open raw data group
		try {
			if (info_group_id >= 0)
				raw_data_group_id = H5.H5Gopen(info_group_id, "RawData",
						HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the number of Objects in the group
		try {
			if (raw_data_group_id >= 0)
				n_objects = H5.H5Gn_members(info_group_id, "RawData");
		} catch (Exception e) {
			e.printStackTrace();
		}

		RAWDATA_OBJECTS = new String[n_objects];
		dataset_id = new int[n_objects];
		dset_datatype = new int[n_objects];
		size = new long[n_objects];
		dataspace_id = new int[n_objects];
		rank = new int[n_objects];
		dset_dims = new long[n_objects];

		// Get the names of the datasets within the group
		try {
			if (raw_data_group_id >= 0)
				for (int i = 0; i < n_objects; i++)
					RAWDATA_OBJECTS[i] = H5.H5Lget_name_by_idx(info_group_id,
							"RawData", HDF5Constants.H5P_DEFAULT, 0, i,
							HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataset_id = new int[n_objects];

		// Open all Datasets and safe the dataset_ids into an array
		try {
			if (raw_data_group_id >= 0)
				for (int i = 0; i < RAWDATA_OBJECTS.length; i++)
					dataset_id[i] = H5.H5Dopen(raw_data_group_id,
							RAWDATA_OBJECTS[i], HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the identifier of the datatype in the file
		try {
			if (raw_data_group_id >= 0)
				for (int i = 0; i < RAWDATA_OBJECTS.length; i++)
					dset_datatype[i] = H5.H5Dget_type(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the storagesize of the dataset
		try {
			for (int i = 0; i < RAWDATA_OBJECTS.length; i++)
				size[i] = H5.H5Dget_storage_size(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get the dataspace of the dataset and the dimensionality of the
		// dataset
		try {
			for (int i = 0; i < RAWDATA_OBJECTS.length; i++)
				if (dataset_id[i] >= 0)
					dataspace_id[i] = H5.H5Dget_space(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < RAWDATA_OBJECTS.length; i++)
				if (dataspace_id[i] >= 0)
					rank[i] = H5.H5Sget_simple_extent_ndims(dataspace_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get the dimensions of the dataset
		try {
			for (int i = 0; i < RAWDATA_OBJECTS.length; i++) {
				if (dataspace_id[i] >= 0)
					H5.H5Sget_simple_extent_dims(dataspace_id[i], dims, null);
				dset_dims[i] = dims[0];
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// buffer1 = new byte[(int) size[0]];
		// buffer2 = new byte[(int) size[1]];
		// buffer3 = new byte[(int) size[2]];

		member_size = new long[n_objects];
		// Get membersizes
		for (int i = 0; i < RAWDATA_OBJECTS.length; i++) {
			try {
				member_size[i] = H5.H5Tget_size(dset_datatype[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < n_objects; i++) {

			buffer = new byte[(int) size[i]];

			try {
				if ((dataset_id[i] >= 0) && (dset_datatype[i] >= 0))
					read_successful = H5.H5Dread(dataset_id[i],
							dset_datatype[i], HDF5Constants.H5S_ALL,
							HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
							buffer);
			} catch (Exception e) {
				e.printStackTrace();
			}

			DataInputStream in = new DataInputStream(new ByteArrayInputStream(
					buffer));

			// for (int i = 0; i < dset_dims[0]; i++) {

			Information n_tmp = inf.get(0);

			try {
				n_tmp.readExternal_String(in, (int) member_size[i],
						RAWDATA_OBJECTS[i], dset_dims[i]);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			// }
		}

		// // Read Raw Data Format
		// try {
		// if ((dataset_id[1] >= 0) && (dset_datatype[1] >= 0))
		// read_successful = H5.H5Dread(dataset_id[1], dset_datatype[1],
		// HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
		// HDF5Constants.H5P_DEFAULT, buffer2);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// DataInputStream in2 = new DataInputStream(new ByteArrayInputStream(
		// buffer2));
		//
		// for (int i = 0; i < dset_dims[1]; i++) {
		//
		// Information n_tmp = inf.get(i);
		//
		// try {
		// n_tmp.readExternal_RawDataFormat(in2, (int) size[1]);
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// // Read Source File
		// try {
		// if ((dataset_id[2] >= 0) && (dset_datatype[2] >= 0))
		// read_successful = H5.H5Dread(dataset_id[2], dset_datatype[2],
		// HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
		// HDF5Constants.H5P_DEFAULT, buffer3);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// DataInputStream in3 = new DataInputStream(new ByteArrayInputStream(
		// buffer3));
		//
		// for (int i = 0; i < dset_dims[2]; i++) {
		//
		// Information n_tmp = inf.get(i);
		//
		// try {
		// n_tmp.readExternal_SourceFile(in3, (int) size[2]);
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		//
		// }
	}

	public void readTarget_Group(String FILENAME) {
		int file_id = -1;
		int info_group_id = -1;
		int target_group_id = -1;
		int[] dataset_id;
		int[] dset_datatype;
		long[] size;
		int[] dataspace_id;
		int[] rank;
		long[] dims = new long[1];
		long[] maxdims = new long[2];
		long[] dset_maxdims = new long[4];
		long[] dset_dims;
		byte[] buffer1;
		byte[] buffer2;
		byte[] buffer3;
		int read_successful;
		long number_of_char;
		long[] member_size;
		int n_objects = 0;
		byte[] buffer;
		int keywords_ident = -1;

		// Open existing File
		try {
			file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Open Group
		try {
			if (file_id >= 0)
				info_group_id = H5.H5Gopen(file_id, INFO,
						HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Open Target group
		try {
			if (info_group_id >= 0)
				target_group_id = H5.H5Gopen(info_group_id, "Target",
						HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the number of Objects in the group
		try {
			if (target_group_id >= 0)
				n_objects = H5.H5Gn_members(info_group_id, "Target");
		} catch (Exception e) {
			e.printStackTrace();
		}

		TARGET_OBJECTS = new String[n_objects];
		dataset_id = new int[n_objects];
		dset_datatype = new int[n_objects];
		size = new long[n_objects];
		dataspace_id = new int[n_objects];
		rank = new int[n_objects];
		dset_dims = new long[n_objects];

		// Get the names of the datasets within the group
		try {
			if (target_group_id >= 0)
				for (int i = 0; i < n_objects; i++)
					TARGET_OBJECTS[i] = H5.H5Lget_name_by_idx(info_group_id,
							"Target", HDF5Constants.H5P_DEFAULT, 0, i,
							HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataset_id = new int[n_objects];

		// Open all Datasets and safe the dataset_ids into an array
		try {
			if (target_group_id >= 0)
				for (int i = 0; i < TARGET_OBJECTS.length; i++)
					dataset_id[i] = H5.H5Dopen(target_group_id,
							TARGET_OBJECTS[i], HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the identifier of the datatype in the file
		try {
			if (file_id >= 0)
				for (int i = 0; i < TARGET_OBJECTS.length; i++)
					dset_datatype[i] = H5.H5Dget_type(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the storagesize of the dataset
		try {
			for (int i = 0; i < TARGET_OBJECTS.length; i++)
				size[i] = H5.H5Dget_storage_size(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get the dataspace of the dataset and the dimensionality of the
		// dataset
		try {
			for (int i = 0; i < TARGET_OBJECTS.length; i++)
				if (dataset_id[i] >= 0)
					dataspace_id[i] = H5.H5Dget_space(dataset_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < TARGET_OBJECTS.length; i++)
				if (dataspace_id[i] >= 0)
					rank[i] = H5.H5Sget_simple_extent_ndims(dataspace_id[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Get the dimensions of the dataset
		try {
			for (int i = 0; i < TARGET_OBJECTS.length; i++) {
				if (dataspace_id[i] >= 0)
					H5.H5Sget_simple_extent_dims(dataspace_id[i], dims, null);
				dset_dims[i] = dims[0];
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < TARGET_OBJECTS.length; i++) {
			if (TARGET_OBJECTS[i].equalsIgnoreCase("Keywords"))
				keywords_ident = i;
		}

		if (keywords_ident > -1)
			Keyword_dims = (int) dset_dims[keywords_ident];

		member_size = new long[n_objects];
		// Get membersizes
		for (int i = 0; i < TARGET_OBJECTS.length; i++) {
			try {
				member_size[i] = H5.H5Tget_size(dset_datatype[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// try{
		// H5.H5Sget_simple_extent_dims(dataspace_id[1], null, maxdims);
		// } catch (Exception e){
		// e.printStackTrace();
		// }

		// buffer1 = new byte[(int) size[0]];
		// buffer2 = new byte[(int) dset_dims[1] * (int) member_size[0]];
		// buffer3 = new byte[(int) size[2]];

		for (int i = 0; i < n_objects; i++) {

			buffer = new byte[(int) size[i]];

			// Read datasets
			try {
				if ((dataset_id[i] >= 0) && (dset_datatype[i] >= 0))
					read_successful = H5.H5Dread(dataset_id[i],
							dset_datatype[i], HDF5Constants.H5S_ALL,
							HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
							buffer);
			} catch (Exception e) {
				e.printStackTrace();
			}

			DataInputStream in = new DataInputStream(new ByteArrayInputStream(
					buffer));

			// for (int i = 0; i < dset_dims[0]; i++) {

			Information n_tmp = inf.get(0);

			try {
				n_tmp.readExternal_String(in, (int) member_size[i],
						TARGET_OBJECTS[i], dset_dims[i]);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			// }
		}
	}

	// // Read Keywords
	// try {
	// if ((dataset_id[1] >= 0) && (dset_datatype[1] >= 0))
	// read_successful = H5.H5Dread(dataset_id[1], dset_datatype[1],
	// HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
	// HDF5Constants.H5P_DEFAULT, buffer2);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in2 = new DataInputStream(new ByteArrayInputStream(
	// buffer2));
	//
	// // for (int i = 0; i < dset_dims[1]; i++) {
	// //
	// // Information n_tmp = inf.get(i);
	//
	// try {
	// readExternal_Keywords(in2, member_size[0]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// // Read Name
	// try {
	// if ((dataset_id[2] >= 0) && (dset_datatype[2] >= 0))
	// read_successful = H5.H5Dread(dataset_id[2], dset_datatype[2],
	// HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
	// HDF5Constants.H5P_DEFAULT, buffer3);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// DataInputStream in3 = new DataInputStream(new ByteArrayInputStream(
	// buffer3));
	//
	// for (int i = 0; i < dset_dims[2]; i++) {
	//
	// Information n_tmp = inf.get(i);
	//
	// try {
	// n_tmp.readExternal_Name(in3, (int) size[1]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }

	// // Read Keywords Information
	// public void readExternal_Keywords(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Keywords = new String[Keyword_dims];
	//
	// for (int i = 0; i < Keyword_dims; i++)
	// Keywords[i] = read_string(in, no_of_chars).trim();
	// // ArrayList<String> KeywordsList = new ArrayList<String>(2);
	// }

	// public static void main(String[] args) {
	// String file_path =
	// "/Users/dkuekenb/Documents/HDF5_Examplefiles/HDF5_Snow_Example/Snow7.brflib.h5";
	// fgi_2008a test = new fgi_2008a();
	// test.read_Data(file_path);
	// test.read_Data_Structure_Version(file_path);
	// test.read_authors(file_path);
	// test.read_Measurement_Group(file_path);
	// test.readRawData_Group(file_path);
	// test.readTarget_Group(file_path);
	// int x = -1;
	// // compound_dataset.ReadWavelength();
	// // compound_dataset.ReadDataset();
	// }

}
