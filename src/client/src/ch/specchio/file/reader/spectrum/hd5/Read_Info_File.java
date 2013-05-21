package ch.specchio.file.reader.spectrum.hd5;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5File;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.tree.MutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;

import ch.specchio.types.spatial_pos;

public class Read_Info_File {

	private static String[] FILE_OBJECTS;

	private static String AUTHORS = "Authors";

	private static String MEASUREMENT = "Measurement";
	public static String[] MEASUREMENT_OBJECTS;

	private static String RAWDATA = "RawData";
	private static String[] RAWDATA_OBJECTS = { "RawDataFormat", "SourceFile" };

	private static String TARGET = "Target";
	private static String[] TARGET_OBJECTS = { "Description", "Keywords",
			"Name" };

	public int inst_param_dims;

	public String[] authors;
	public String[] comments;
	public String campaign;
	public spatial_pos gps;
	public String gps_string;
	public String instrument_name;
	public String[] instrument_parameters;
	public String light_source;
	public String light_source_param;
	public String location;
	public String measurement_time;
	public String weather;
	public String white_reference_target;
	public String raw_data_format;
	public String source_file;
	public String description;
	public int keyword_dims;
	public String[] keywords;
	public String name;
	// public ArrayList<String> Instrument_Parameters_List = new
	// ArrayList<String>();

	public ArrayList<Information> inf = new ArrayList<Information>();

	public Read_Info_File() {

	}

	public void readAuthors(String info_file_name) {

		int dataset_id = -1;
		int file_id = -1;
		int dset_datatype = -1;
		long size = -1;
		int dataspace_id = -1;
		int rank = -1;
		long[] dims = new long[1];
		byte[] buffer;
		int read_successful = -1;
		long member_size = -1;
		int n_objects = -1;
		long[] num_obj = new long[1];
		String file_name = null;
		String[] object_names = null;
		int[] otype = null;
		long[] ref = null;

		// Open existing File
		try {
			file_id = H5.H5Fopen(info_file_name, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (file_id >= 0)
				H5.H5Gget_num_objs(file_id, num_obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		object_names = new String[(int) num_obj[0]];
		otype = new int[(int) num_obj[0]];
		ref = new long[(int) num_obj[0]];

		// Get the names of the datasets within the group
		try {
			if (file_id >= 0)
				n_objects = H5.H5Gget_obj_info_all(file_id, file_name,
						object_names, otype, ref);
		} catch (Exception e) {
			e.printStackTrace();
		}

		FILE_OBJECTS = object_names;

		// Open dataset
		try {
			dataset_id = H5
					.H5Dopen(file_id, AUTHORS, HDF5Constants.H5P_DEFAULT);
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

		;
		// Get membersizes
		try {
			member_size = H5.H5Tget_size(dset_datatype);
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

			Information n_tmp = new Information((int) size);

			try {
				n_tmp.readExternal_String(in, (int) member_size, AUTHORS,
						(int) dims[0]);
				inf.add(n_tmp);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	public void readMeasurement_Group(String info_file_name) {

		int file_id = -1;
		int group_id = -1;
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
			file_id = H5.H5Fopen(info_file_name, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Open Group
		try {
			if (file_id >= 0)
				group_id = H5.H5Gopen(file_id, MEASUREMENT,
						HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the number of Objects in the group
		try {
			if (group_id >= 0)
				n_objects = H5.H5Gn_members(file_id, MEASUREMENT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		MEASUREMENT_OBJECTS = new String[n_objects];
		// Get the names of the datasets within the group
		try {
			if (group_id >= 0)
				for (int i = 0; i < n_objects; i++)
					MEASUREMENT_OBJECTS[i] = H5.H5Lget_name_by_idx(file_id,
							MEASUREMENT, HDF5Constants.H5P_DEFAULT, 0, i,
							HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		dataset_id = new int[n_objects];
		// Open all Datasets and safe the dataset_ids into an array
		try {
			if (group_id >= 0)
				for (int i = 0; i < MEASUREMENT_OBJECTS.length; i++)
					dataset_id[i] = H5.H5Dopen(group_id,
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

		member_size = new long[n_objects];
		try {
			for (int i = 0; i < n_objects; i++)
				member_size[i] = H5.H5Tget_size(dset_datatype[i]);
		} catch (Exception e) {
			e.printStackTrace();
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
						MEASUREMENT_OBJECTS[i], dset_dims[i]);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			// }
		}
	}

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
	//
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
	// }

	// // Read Instrument Parameter Information
	// public void readExternal_InstrumentParameters(DataInputStream in,
	// int no_of_chars) throws IOException, ClassNotFoundException {
	//
	// Instrument_Parameters = new String[inst_param_dims];
	//
	// for (int i = 0; i < inst_param_dims; i++)
	// Instrument_Parameters[i] = (read_string(in, no_of_chars));
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

		byte[] tmp = new byte[cnt + 1];

		System.arraycopy(bytes, 0, tmp, 0, cnt); // Copies 2, 3, 4 into dst

		return new String(tmp);
	}

	public void readRawData_Group(String info_file_name) {
		int file_id = -1;
		int group_id = -1;
		int[] dataset_id;
		int[] dset_datatype;
		long[] size;
		int[] dataspace_id;
		int[] rank;
		long[] dims = new long[1];
		long[] dset_dims;
		byte[] buffer1;
		byte[] buffer2;
		byte[] buffer;
		int read_successful;
		int n_objects = -1;
		long[] member_size;

		// Open existing File
		try {
			file_id = H5.H5Fopen(info_file_name, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Open Group
		try {
			if (file_id >= 0)
				group_id = H5.H5Gopen(file_id, RAWDATA,
						HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the number of Objects in the group
		try {
			if (group_id >= 0)
				n_objects = H5.H5Gn_members(file_id, "RawData");
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
			if (group_id >= 0)
				for (int i = 0; i < n_objects; i++)
					RAWDATA_OBJECTS[i] = H5.H5Lget_name_by_idx(file_id,
							"RawData", HDF5Constants.H5P_DEFAULT, 0, i,
							HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataset_id = new int[n_objects];

		// Open all Datasets and safe the dataset_ids into an array
		try {
			if (group_id >= 0)
				for (int i = 0; i < RAWDATA_OBJECTS.length; i++)
					dataset_id[i] = H5.H5Dopen(group_id, RAWDATA_OBJECTS[i],
							HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the identifier of the datatype in the file
		try {
			if (file_id >= 0)
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

		member_size = new long[n_objects];
		// Get membersizes
		for (int i = 0; i < RAWDATA_OBJECTS.length; i++) {
			try {
				member_size[i] = H5.H5Tget_size(dset_datatype[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// buffer1 = new byte[(int) size[0]];
		// buffer2 = new byte[(int) size[1]];

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
	}

	// // Read Source File
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
	// n_tmp.readExternal_SourceFile(in2, (int) size[1]);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }

	public void readTarget_Group(String info_file_name) {
		int file_id = -1;
		int group_id = -1;
		int[] dataset_id = new int[TARGET_OBJECTS.length];
		int[] dset_datatype = new int[TARGET_OBJECTS.length];
		long[] size = new long[TARGET_OBJECTS.length];
		int[] dataspace_id = new int[TARGET_OBJECTS.length];
		int[] rank = new int[TARGET_OBJECTS.length];
		long[] dims = new long[1];
		long[] maxdims = new long[2];
		long[] dset_maxdims = new long[4];
		long[] dset_dims = new long[TARGET_OBJECTS.length];
		byte[] buffer1;
		byte[] buffer2;
		byte[] buffer3;
		int read_successful;
		long number_of_char;
		long[] member_size;
		int n_objects = 0;
		int keywords_ident = -1;
		byte[] buffer;

		// Open existing File
		try {
			file_id = H5.H5Fopen(info_file_name, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Open Group
		try {
			if (file_id >= 0)
				group_id = H5.H5Gopen(file_id, TARGET,
						HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the number of Objects in the group
		try {
			if (group_id >= 0)
				n_objects = H5.H5Gn_members(file_id, "Target");
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
			if (group_id >= 0)
				for (int i = 0; i < n_objects; i++)
					TARGET_OBJECTS[i] = H5.H5Lget_name_by_idx(file_id,
							"Target", HDF5Constants.H5P_DEFAULT, 0, i,
							HDF5Constants.H5P_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataset_id = new int[n_objects];

		// Open all Datasets and safe the dataset_ids into an array
		try {
			if (group_id >= 0)
				for (int i = 0; i < TARGET_OBJECTS.length; i++)
					dataset_id[i] = H5.H5Dopen(group_id, TARGET_OBJECTS[i],
							HDF5Constants.H5P_DEFAULT);
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
			keyword_dims = (int) dset_dims[keywords_ident];

		member_size = new long[n_objects];
		try {
			for (int i = 0; i < n_objects; i++)
				member_size[i] = H5.H5Tget_size(dset_datatype[i]);
		} catch (Exception e) {
			e.printStackTrace();
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

			// Read Description
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
		}
	}

	public void read_info_as_xml(String file_name) {

		// try{
		//
		// SAXParserFactory factory = SAXParserFactory.newInstance();
		// SAXParser saxParser = factory.newSAXParser();
		//
		// DefaultHandler handler = new DefaultHandler() {
		//
		// boolean bauthors = false;
		//
		// public void startElement(String uri, String localName, String qName,
		// Attributes attributes) throws SAXException{
		//
		// System.out.println("Start Element :" + qName);
		//
		// if (qName.equalsIgnoreCase("Authors")){
		// bauthors = true;
		// }
		// }
		//
		// public void endElement(String uri, String localName, String qName)
		// throws SAXException{
		// System.out.println("End Element :" + qName);
		// }
		//
		// public void characters(char ch[], int start, int length) throws
		// SAXException {
		// System.out.println(new String(ch, start, length));
		// if(bauthors) {
		// authors.add(new String(ch, start, length));
		// // System.out.println("item : " + new String(ch, start, length));
		// bauthors = false;
		// }
		// }
		// };
		//
		// File xmlFile = new File(file_name);
		// InputStream inputStream = new FileInputStream(xmlFile);
		// Reader reader = new InputStreamReader(inputStream, "UTF-8");
		//
		// InputSource is = new InputSource(reader);
		// is.setEncoding("UTF-8");
		//
		// saxParser.parse(is, handler);
		//
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		
		
		boolean field_exists = true;
		
		try {
			File fXmlFile = new File(file_name);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Authors");
			
			Node nNode = nList.item(0);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eNode = (Element) nNode;

				int no_of_item_tags = eNode.getElementsByTagName("item")
						.getLength();
				
				if(no_of_item_tags > 1){
					authors = getTagValue("item", eNode, no_of_item_tags);
				} else {
					authors[0] = getTagValue("item", eNode);
				}

			}
			
			nList = doc.getElementsByTagName("Comments");
			
			nNode = nList.item(0);

			if (nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eNode = (Element) nNode;

				int no_of_item_tags = eNode.getElementsByTagName("item")
						.getLength();
				
				if(no_of_item_tags > 1){
					comments = getTagValue("item", eNode, no_of_item_tags);
				} else {
					comments = new String[1];
					comments[0] = getTagValue("item", eNode);
				}

			}
			
			nList = doc.getElementsByTagName("Name");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				name = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("Description");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				description = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("Keywords");
			
			nNode = nList.item(0);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eNode = (Element) nNode;

				int no_of_item_tags = eNode.getElementsByTagName("item")
						.getLength();
				keyword_dims = no_of_item_tags;
				
				if(no_of_item_tags > 1){
					keywords = getTagValue("item", eNode, no_of_item_tags);
				} else {
					keywords[0] = getTagValue("item", eNode);
				}

			}
			nList = doc.getElementsByTagName("InstrumentName");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				instrument_name = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("InstrumentParameters");
			
			nNode = nList.item(0);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eNode = (Element) nNode;

				int no_of_item_tags = eNode.getElementsByTagName("item")
						.getLength();
				inst_param_dims = no_of_item_tags;
				
				if(no_of_item_tags > 1){
					instrument_parameters = getTagValue("item", eNode, no_of_item_tags);
				} else {
					instrument_parameters[0] = getTagValue("item", eNode);
				}

			}
			
			nList = doc.getElementsByTagName("LightSource");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				light_source = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("LightSourceParameter");
			
			if (nList.item(0) == null)
				field_exists = false;
			else
				nNode = nList.item(0);
				
			if (field_exists && nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				light_source_param = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("WhiteReferenceTarget");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				white_reference_target = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("Campaign");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				campaign = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("MeasurementTime");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				measurement_time = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("Location");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				location = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("GPS");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				gps_string = getTagValue("item", eNode);
				Information n_tmp = new Information();
				gps = n_tmp.getSpatialPos(gps_string);
			}
			
			nList = doc.getElementsByTagName("Weather");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				weather = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("RawDataFormat");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				raw_data_format = getTagValue("item", eNode);
			}
			
			nList = doc.getElementsByTagName("SourceFile");
			
			nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				
				source_file = getTagValue("item", eNode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String[] getTagValue(String sTag, Element eElement,
			int no_of_items) {

		String[] values = new String[no_of_items];

		for (int i = 0; i < no_of_items; i++) {
			NodeList nlList = eElement.getElementsByTagName(sTag).item(i)
					.getChildNodes();

			Node nValue = (Node) nlList.item(0);

			values[i] = nValue.getNodeValue().trim();

		}
		return values;

	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue().trim();
	}

	// // Read Keywords
	// try {
	// if ((dataset_id[1] >= 0) && (dset_datatype[1] >= 0))
	// read_successful = H5.H5Dread(dataset_id[1],
	// dset_datatype[1], HDF5Constants.H5S_ALL,
	// HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
	// buffer2);
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

	// // Read Author Information
	// public void readExternal_Authors(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Authors = Information.read_string(in, no_of_chars);
	// ArrayList<String> AuthorsList = new ArrayList<String>(1);
	// AuthorsList.add(Authors);
	//
	// }
	//
	// // Read Campaign Information
	// public void readExternal_Campaign(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Campaign = Information.read_string(in, no_of_chars);
	// ArrayList<String> CampaignList = new ArrayList<String>(1);
	// CampaignList.add(Campaign);
	// }
	//
	// // Read GPS Information
	// public void readExternal_GPS(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// GPS = Information.read_string(in, no_of_chars);
	// ArrayList<String> GPSList = new ArrayList<String>(1);
	// GPSList.add(GPS);
	// }
	//
	// // Read Instrument Name Information
	// public void readExternal_InstrumentName(DataInputStream in, int
	// no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Instrument_Name = Information.read_string(in, no_of_chars);
	// ArrayList<String> Instrument_Name_List = new ArrayList<String>(1);
	// Instrument_Name_List.add(Instrument_Name);
	// }
	//
	// // Read Instrument Parameter Information
	// public void readExternal_InstrumentParameters(DataInputStream in,
	// int no_of_chars) throws IOException, ClassNotFoundException {
	//
	// Instrument_Parameters = Information.read_string(in, no_of_chars);
	// ArrayList<String> Instrument_Parameters_List = new ArrayList<String>();
	// Instrument_Parameters_List.add(Instrument_Parameters);
	// }
	//
	// // Read Light Source Information
	// public void readExternal_LightSource(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Light_Source = Information.read_string(in, no_of_chars);
	// ArrayList<String> LightSourceList = new ArrayList<String>(1);
	// LightSourceList.add(Light_Source);
	// }
	//
	// // Read Location Information
	// public void readExternal_Location(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Location = Information.read_string(in, no_of_chars);
	// ArrayList<String> LocationList = new ArrayList<String>(1);
	// LocationList.add(Location);
	// }
	//
	// // Read Measurement Time Information
	// public void readExternal_MeasurementTime(DataInputStream in, int
	// no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Measurement_Time = Information.read_string(in, no_of_chars);
	// ArrayList<String> MeasurementT_List = new ArrayList<String>(1);
	// MeasurementT_List.add(Measurement_Time);
	// }
	//
	// // Read Weather Information
	// public void readExternal_Weather(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Weather = Information.read_string(in, no_of_chars);
	// ArrayList<String> WeatherList = new ArrayList<String>(1);
	// WeatherList.add(Weather);
	// }
	//
	// // Read White Reference Target Information
	// public void readExternal_WR_Target(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// White_Reference_Target = Information.read_string(in, no_of_chars);
	// ArrayList<String> WR_Target_List = new ArrayList<String>(1);
	// WR_Target_List.add(White_Reference_Target);
	// }
	//
	// // Read Raw Data Format Information
	// public void readExternal_RawDataFormat(DataInputStream in, int
	// no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// RawDataFormat = Information.read_string(in, no_of_chars);
	// ArrayList<String> RawDataFormat_List = new ArrayList<String>(1);
	// RawDataFormat_List.add(RawDataFormat);
	// }
	//
	// // Read Source File Information
	// public void readExternal_SourceFile(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// SourceFile = Information.read_string(in, no_of_chars);
	// ArrayList<String> SourceFileList = new ArrayList<String>(1);
	// SourceFileList.add(SourceFile);
	// }
	//
	// // Read Description Information
	// public void readExternal_Description(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Description = Information.read_string(in, no_of_chars);
	// ArrayList<String> DescriptionList = new ArrayList<String>(1);
	// DescriptionList.add(Description);
	// }
	//
	// // Read Keywords Information
	// public void readExternal_Keywords(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Keywords = Information.read_string(in, no_of_chars);
	// ArrayList<String> KeywordsList = new ArrayList<String>(2);
	// KeywordsList.add(Keywords);
	// }
	//
	// // Read Name
	// public void readExternal_Name(DataInputStream in, int no_of_chars)
	// throws IOException, ClassNotFoundException {
	//
	// Name = Information.read_string(in, no_of_chars);
	// ArrayList<String> NameList = new ArrayList<String>(1);
	// NameList.add(Name);
	// }

	// public static void main(String[] args) {
	// Read_Info_File.readAuthors();
	// Read_Info_File.readMeasurement_Group();
	// Read_Info_File.readRawData_Group();
	// Read_Info_File.readTarget_Group();
	//
	// }

}
