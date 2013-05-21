package ch.specchio.file.reader.spectrum.hd5;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DataStructureVersion {

	private static String DATASETNAME = "DataStructureVersion";
	public String DStruct_Version;

	public void ReadDataStructureVersion(String FILENAME) throws HDF5LibraryException, IOException {

		int file_id = -1;
		int dataset_id = -1;
		int dset_datatype = -1;
		long size = -1;
		int dataspace_id = -1;
		int rank = -1;
		long[] dims = new long[1];
		byte[] buffer;
		int read_successful = -1;

		// Open an existing file.
		file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);

		// Open dataset
		dataset_id = H5.H5Dopen(file_id, DATASETNAME, HDF5Constants.H5P_DEFAULT);

		// get the identifier of the datatype in the file
		if (file_id >= 0)
			dset_datatype = H5.H5Dget_type(dataset_id);
		
		// Get the storagesize of the dataset
		size = H5.H5Dget_storage_size(dataset_id);
		
		// Get the dataspace of the dataset and the dimensionality of the
		// dataset
		if (dataset_id >= 0)
			dataspace_id = H5.H5Dget_space(dataset_id);
		if (dataspace_id >= 0)
			rank = H5.H5Sget_simple_extent_ndims(dataspace_id);
		
		// Get the dimensions of the dataset
		if (dataspace_id >= 0)
			H5.H5Sget_simple_extent_dims(dataspace_id, dims, null);

		// Create buffer for reading in the data
		buffer = new byte[(int) size];

		// Read Data
		if ((dataset_id >= 0) && (dset_datatype >= 0))
			read_successful = H5.H5Dread(dataset_id, dset_datatype,
					HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
					HDF5Constants.H5P_DEFAULT, buffer);
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(
				buffer));

		readExternal_DStruct_Vers(in, (int) size);

		int x = -1;

	}

	// Read Data Structure Version dataset
	public String readExternal_DStruct_Vers(DataInputStream in, int no_of_chars)
			throws IOException {

		DStruct_Version = read_string(in, no_of_chars);
		return DStruct_Version;
	}

	protected String read_string(DataInputStream in, int no_of_chars)
			throws IOException {
		byte[] bytes = new byte[no_of_chars];
		in.read(bytes);
		return new String(bytes);
	}

}
