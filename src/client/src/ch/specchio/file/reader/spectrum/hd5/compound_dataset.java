package ch.specchio.file.reader.spectrum.hd5;

//file path: /Users/dkuekenb/Documents/HDF5_Examplefiles/Example_files_h5_xml/Snow7.BRF.lib2.h5

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class compound_dataset {
	// private static String FILENAME =
	// "/Users/dkuekenb/Documents/HDF5_Examplefiles/Example_files_h5_xml/Snow7.BRF.lib2.h5";
	private static String DATASETNAME1 = "DataPoints";
	private static String DATASETNAME2 = "Wavelengths";
	private static String DATASETNAME3 = "DataStructureVersion";
	private static final int DIM0 = -1;
	private static final int ADIM0 = -1;
	private static final int ADIM1 = -1;

	public ArrayList<Measurement> m = new ArrayList<Measurement>();

	protected int no_of_measurements;
	public int no_of_wvls;
	public Float[] Wavelength;

	public compound_dataset() {
		// m = new Measurement();
	}

	public void ReadDataset(String FILENAME, String ds_version) {
		int file_id = -1;
		int dataset_id = -1;
		int dataspace_id = -1;
		int nmembers = -1;
		int rank = -1;
		int rank_Refl = -1;
		int rank_UTC = -1;
		long[] dims = { DIM0 };
		long[] adimsRefl = { ADIM0 };
		long[] adimsUTC = { ADIM1 };
		byte[] buffer;
		String[] membernames;
		int comp_datatype_id = -1;
		int[] member_class;
		int memtype = -1;
		long size = -1;
		long[] offset;
		int[] membertypes;
		int test = -1;
		int[] member_insert;
		int[] member_size;
		int LightAz_dataset = -1;
		int memory_datatype = -1;
		int LightAz_dataspace = -1;
		int read_successful = -1;
		int LightAz_dataset_id = -1;
		int utc_pos = -1;
		int n_objects = 0;
		int refl_ident = -1;
		int utc_ident = -1;
		int utc_type = -1;

		String data_structure_version = ds_version;

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
				comp_datatype_id = H5.H5Dget_type(dataset_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the size of the datatype
		try {
			if (comp_datatype_id >= 0)
				size = H5.H5Dget_storage_size(dataset_id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get the number of compound members in the dataset
		try {
			if (comp_datatype_id >= 0)
				nmembers = H5.H5Tget_nmembers(comp_datatype_id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		membernames = new String[nmembers];
		member_class = new int[nmembers];
		offset = new long[nmembers];
		membertypes = new int[nmembers];
		member_insert = new int[nmembers];
		member_size = new int[nmembers];

		try {
			for (int i = 0; i < nmembers; i++) {
				member_class[i] = H5.H5Tget_member_class(comp_datatype_id, i);
				membernames[i] = H5.H5Tget_member_name(comp_datatype_id, i);

				if (membernames[i].equals("UTC_Time"))
					utc_pos = i;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get membertypes
		try {
			if (comp_datatype_id >= 0) {
				for (int i = 0; i < nmembers; i++)
					membertypes[i] = H5.H5Tget_member_type(comp_datatype_id, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the sizes of every member
		try {
			if (comp_datatype_id >= 0) {
				for (int i = 0; i < nmembers; i++)
					member_size[i] = H5.H5Tget_size(membertypes[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the offset of every member
		try {
			if (comp_datatype_id >= 0) {
				for (int i = 0; i < nmembers; i++)
					offset[i] = H5.H5Tget_member_offset(comp_datatype_id, i);
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

		// Get the identifier for the Reflectance ant UTC_Time member
		for (int i = 0; i < nmembers; i++) {
			if (membernames[i].equalsIgnoreCase("ReflI"))
				refl_ident = i;
			else if (membernames[i].equalsIgnoreCase("StokesI"))
				refl_ident = i;
		}
		for (int i = 0; i < nmembers; i++) {
			if (membernames[i].equalsIgnoreCase("UTC_Time"))
				utc_ident = i;
		}

		// Get the array dimension of the included arrays in the dataset
		try {
			if (dataspace_id >= 0) {
				H5.H5Tget_array_dims(membertypes[refl_ident], adimsRefl);
				rank_Refl = H5.H5Tget_array_ndims(membertypes[refl_ident]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (dataspace_id >= 0) {
				H5.H5Tget_array_dims(membertypes[utc_ident], adimsUTC);
				rank_UTC = H5.H5Tget_array_ndims(membertypes[utc_ident]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get data type of values in utc_time array
		try{
			int utc_super =  H5.H5Tget_super(membertypes[utc_ident]);
			utc_type = H5.H5Tget_class(utc_super);
		} catch (Exception e){
			e.printStackTrace();
		}
		// Reading a dataset using H5Tget_native_type
		try {
			if (comp_datatype_id >= 0)
				memory_datatype = H5.H5Tget_native_type(comp_datatype_id,
						HDF5Constants.H5T_DIR_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create the buffer for reading in the byte-Stream.
		buffer = new byte[(int) size];

		// Read data
		try {
			if ((comp_datatype_id >= 0) && (memory_datatype >= 0))
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

			Measurement m_tmp = new Measurement((int) adimsRefl[0],
					(int) adimsUTC[0], membernames);

			try {
				m_tmp.readExternal(in, membernames, data_structure_version, utc_type);
				m.add(m_tmp);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}

	}

	public int GetNoOfSpectra() {

		Measurement m_tmp = m.get(0);

		if (m_tmp.hasQandU()) {
			return no_of_measurements * 3;
		}

		return no_of_measurements;

	}

	public void ReadWavelength(String FILENAME) {

		int file_id = -1;
		int dataset_id = -1;
		int dataspace_id = -1;
		long size = -1;
		int dset_datatype = -1;
		int member_class = -1;
		int rank = -1;
		long[] dims = { DIM0 };
		long offset = -1;
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
			dataset_id = H5.H5Dopen(file_id, DATASETNAME2,
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

		no_of_wvls = (int) dims[0];
		Wavelength = new Float[no_of_wvls];

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

		// for (int i = 0; i < this.no_of_measurements; i++) {
		//
		//
		// //m = new Measurement();
		//
		// Measurement m_tmp = m.get(i);

		try {
			readExternal_Wavelength(in);
			// m_tmp.readExternal_Wavelength(in);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			// }

		}

	}

	public void readExternal_Wavelength(DataInputStream in) throws IOException,
			ClassNotFoundException {

		for (int i = 0; i < no_of_wvls; i++) {
			Wavelength[i] = Measurement.read_float(in);

		}

	}

	public void ReadDataStructureVersion(String FILENAME) {

		int file_id = -1;
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

	// public static void main(String[] args) {
	// compound_dataset.ReadDataStructureVersion();
	// compound_dataset.ReadWavelength();
	// compound_dataset.ReadDataset();
	// }
}
