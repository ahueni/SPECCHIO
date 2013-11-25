package ch.specchio.factories;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ListIterator;
import java.util.TimeZone;

import ch.specchio.types.Campaign;
import ch.specchio.types.Instrument;
import ch.specchio.types.MetaDate;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.Metadata;
import ch.specchio.types.SpecchioMessage;
import ch.specchio.types.SpectralFile;
import ch.specchio.types.SpectralFileInsertResult;
import ch.specchio.types.SpectrumDataLink;
import ch.specchio.eav_db.SQL_StatementBuilder;
import ch.specchio.eav_db.id_and_op_struct;


/**
 * Class for manipulating spectral files in the database.
 */
public class SpectralFileFactory extends SPECCHIOFactory {
	
	/** the campaign to which this factory belongs */
	private Campaign campaign = null;
	
	/** campaign factory for inserting hierarchy nodes */
	private CampaignFactory campaign_factory = null;
	
	/**
	 * Constructor. This constructor builds a spectral file factory that is not associated with
	 * a particular campaign. Some factory methods cannot be used without a campaign.
	 * 
	 * @param db_user		database account user name
	 * @param db_password	database account password
	 * 
	 * @throws SPECCHIOFactoryException	could not establish initial context
	 */
	public SpectralFileFactory(String db_user, String db_password) throws SPECCHIOFactoryException {

		super(db_user, db_password);
		
	}
	
	
	/**
	 * Constructor. The constructor builds a spectral file factory that is associated with a
	 * particular campaign.
	 * 
	 * @param campaign_type	the type of campaign to which the spectra will belong
	 * @param campaign_id	the identifier of the campaign to which the spectra will belong
	 * @param db_user		database account user name
	 * @param db_password	database account password
	 * 
	 * @throws SPECCHIOFactoryException	could not establish initial context
	 */
	public SpectralFileFactory(String db_user, String db_password, String campaign_type, int campaign_id) throws SPECCHIOFactoryException {
		
		this(db_user, db_password);
		
		// save campaign information for later
		campaign_factory = CampaignFactory.getInstance(this, campaign_type);
		campaign = campaign_factory.getCampaign(campaign_id);
		
	}
	
	
	/**
	 * Copy constructor. Construct a new factory that uses the same database connection
	 * as an existing factory.
	 * 
	 * @param factory	the existing factory
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public SpectralFileFactory(SPECCHIOFactory factory) throws SPECCHIOFactoryException {
		
		super(factory);
		
	}
		
	
	
	/**
	 * Get the file format identifier for a given file format name.
	 * 
	 * @param file_format_name	the file format name
	 * 
	 * @returns the identifier corresponding to file_format_name, or -1 of the name was not found
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	public int getIdForFileFormat(String file_format_name) throws SPECCHIOFactoryException {
		
		// initialise to "not found"
		int file_format_id = -1;
		
		try {
		
			String query = "SELECT file_format_id from file_format where name = '" + file_format_name + "'";
			Statement stmt = getStatementBuilder().createStatement();
			ResultSet rs = stmt.executeQuery(query);	
			while (rs.next())
			{
				file_format_id = rs.getInt(1);
			}
			rs.close();
			stmt.close();
			
		}
		catch (SQLException ex) {
			// bad SQL
			throw new SPECCHIOFactoryException(ex);
		}
		
		return file_format_id;
	
	}
	

	/**
	 * Get the identifier of a sub-hierarchy with a given name, creating the
	 * hierarchy if it doesn't exist.
	 * 
	 * @param parent_id			the identifier of the the parent of the hierarchy
	 * @param hierarchy_name	the name of the desired heirarchy
	 * 
	 * @return the identifier of the child of parent_id with the name hierarchy_name
	 *
	 * @throws SPECCHIOFactoryException	database error
	 */
	private int getSubHierarchyId(int parent_id, String hierarchy_name) throws SPECCHIOFactoryException {
		
		// see if the node already exists
		int sub_hierarchy_id = campaign_factory.getHierarchyNodeId(campaign.getId(), hierarchy_name, parent_id);
		
		if (sub_hierarchy_id == -1) {
			// the sub-hierarchy doesn't exist; create it
			sub_hierarchy_id = campaign_factory.insertHierarchyNode(campaign.getId(), hierarchy_name, parent_id);
		}
		
		return sub_hierarchy_id;
		
	}
	
	
	private void getSubHierarchyId(Hashtable<String, Integer> subhierarchies, int parent_id, String hierarchy_name) throws SPECCHIOFactoryException
	{
		Integer subhierarchy_id = getSubHierarchyId(parent_id, hierarchy_name);		
		subhierarchies.put(hierarchy_name, subhierarchy_id);		
	}
	
	
	
	private Hashtable<String, Integer> getSubHierarchyIds(SpectralFile spec_file, int hierarchy_id) throws SPECCHIOFactoryException
	{
		Hashtable<String, Integer> subhierarchies = new Hashtable<String, Integer>();
		
		if (spec_file.getCompany().equals("SVC")
				&& spec_file.getInstrumentTypeNumber() == 1024
				&& spec_file.getNumberOfSpectra() == 3) {

			// HR 1024 files contain target and reference radiances plus
			// reflectances
			// thus here we must split them into two new hierarchies:
			// reflectance and radiance with radiance having two subfolders

			getSubHierarchyId(subhierarchies, hierarchy_id, "Radiance");
			getSubHierarchyId(subhierarchies, hierarchy_id, "Reflectance");
			getSubHierarchyId(subhierarchies, subhierarchies.get("Radiance"), "Targets");
			getSubHierarchyId(subhierarchies, subhierarchies.get("Radiance"), "References");
		}

		if (spec_file.getCompany().equals("COST_OO_CSV")) {

			// COST_OO_CSV files contain target and reference radiances plus
			// reflectances
			// thus here we must split them into two new hierarchies:
			// reflectance and radiance with radiance having two subfolders
			
			getSubHierarchyId(subhierarchies, hierarchy_id, "Radiance");
			getSubHierarchyId(subhierarchies, hierarchy_id, "Reflectance");
			getSubHierarchyId(subhierarchies, subhierarchies.get("Radiance"), "Targets");
			getSubHierarchyId(subhierarchies, subhierarchies.get("Radiance"), "References");		

		}
		
//		if (spec_file.getCompany().equals("PP Systems") && spec_file.getCapturingSoftwareName() != null && ( 
//				spec_file.getCapturingSoftwareName().equals("UnispecDCcf")
//				|| spec_file.getCapturingSoftwareName().equals("UniSpec Dual Channel NETCF")
//				)) {
		if (spec_file.getCompany().equals("PP Systems") && spec_file.getFileFormatName().equals("UniSpec_SPU")) 
		{				

			// PP dual channel files contain target and irradiance DNs
			// thus here we must split them into two new hierarchies
			getSubHierarchyId(subhierarchies, hierarchy_id, "Raw");
			getSubHierarchyId(subhierarchies, subhierarchies.get("Raw"), "channel a");
			getSubHierarchyId(subhierarchies, subhierarchies.get("Raw"), "channel b");					
			
		}

		if (spec_file.getCompany().equals("GER") || (spec_file.getCompany().equals("PP Systems") && spec_file.getCapturingSoftwareName() == null) && !spec_file.getFileFormatName().equals("UniSpec_SPU")) {
			// GER files contain target and reference radiances
			// thus here we must split them into two new hierarchies
			getSubHierarchyId(subhierarchies, hierarchy_id, "Targets");
			getSubHierarchyId(subhierarchies, hierarchy_id, "References");
		}

		if (spec_file.getCompany().equals("YES")) {
			// if the supplied hierarchy is a new one then we must insert new
			// sub hierarchies
			getSubHierarchyId(subhierarchies, hierarchy_id, "Total");
			getSubHierarchyId(subhierarchies, hierarchy_id, "Diffuse");
		}

		if (spec_file.getCompany().equals("FGI")) {

			// create new sub hierarchy for this data collection
			String[] tokens = spec_file.getBasename().split("\\.");
			getSubHierarchyId(subhierarchies, hierarchy_id, tokens[0]);
			int FGI_collection_id = subhierarchies.get(tokens[0]);

			// create sub hierarchies within the data collection for HDRF, BRF,
			// etc

			if (spec_file.isFgiHdrfBrfCombined() == false) {
				getSubHierarchyId(subhierarchies, FGI_collection_id, tokens[1]);
				int FGI_sub_id = subhierarchies.get(tokens[1]);
				
				if (spec_file.getSpectralGroupingSize() > 1) {
					getSubHierarchyId(subhierarchies, FGI_sub_id, "R");
					getSubHierarchyId(subhierarchies, FGI_sub_id, "Mueller10");
					getSubHierarchyId(subhierarchies, FGI_sub_id, "Mueller20");
				}


			} else // hdrf_brf_combined == true
			{
				getSubHierarchyId(subhierarchies, FGI_collection_id, "BRF");
				getSubHierarchyId(subhierarchies, FGI_collection_id, "HDRF");
			}

		}

		if (spec_file.getAsdV7() == true) {
			
			getSubHierarchyId(subhierarchies, hierarchy_id, "DN");

			// insert radiance spectrum
			if (spec_file.getAsdV7RadianceFlag() == true) {
				getSubHierarchyId(subhierarchies, hierarchy_id, "Radiance");
			}

			// insert reflectance spectrum
			if (spec_file.getAsdV7ReflectanceFlag() == true) {
				getSubHierarchyId(subhierarchies, hierarchy_id, "Reflectance");
			}			

		}
		
		return subhierarchies;
	}
	

	void insertHierarchySpectrumReferences(int hierarchy_id, int spectrum_id, int recursion_break_at_parent_id) throws SPECCHIOFactoryException {
		ArrayList<Integer> spectrum_ids = new ArrayList<Integer>();
	
		spectrum_ids.add(spectrum_id);
		insertHierarchySpectrumReferences(hierarchy_id, spectrum_ids,
				recursion_break_at_parent_id);
	
	}

	void insertHierarchySpectrumReferences(int hierarchy_id, int spectrum_id) throws SPECCHIOFactoryException {

		insertHierarchySpectrumReferences(hierarchy_id, spectrum_id, 0);	
	}
	

	public void insertHierarchySpectrumReferences(int hierarchy_id, ArrayList<Integer> spectrum_ids) throws SPECCHIOFactoryException {
		insertHierarchySpectrumReferences(hierarchy_id, spectrum_ids, 0);
	}
	

	void insertHierarchySpectrumReferences(int hierarchy_id, ArrayList<Integer> spectrum_ids, int recursion_break_at_hierarchy_id) throws SPECCHIOFactoryException {
		if (spectrum_ids.size() == 0)
			return; // catch empty list

		try {
			
			SQL_StatementBuilder SQL = getStatementBuilder();
		
			ArrayList<String> value_strings = new ArrayList<String>();
			String query = "insert into hierarchy_level_x_spectrum_view (hierarchy_level_id, spectrum_id) values ";
			Statement stmt = getStatementBuilder().createStatement();

			// build value list
			ListIterator<Integer> li = spectrum_ids.listIterator();
			while (li.hasNext()) {
				String str = "(" + hierarchy_id + "," + li.next() + ")";
				value_strings.add(str);
			}
			query = query + SQL.conc_cols(value_strings);
			stmt.executeUpdate(query);

			// check if there is a parent of this hierarchy
			if (hierarchy_id != recursion_break_at_hierarchy_id) {
				query = "select parent_level_id from hierarchy_level where hierarchy_level_id = " + hierarchy_id;
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					int parent_id = rs.getInt(1);
	
					if (parent_id != 0) {
						insertHierarchySpectrumReferences(parent_id, spectrum_ids, recursion_break_at_hierarchy_id);
					}
				}
				rs.close();
			}

			stmt.close();

		} catch (SQLException ex) {
			throw new SPECCHIOFactoryException(ex);
		}

	}
	

	/**
	 * Insert a link between two spectra.
	 * 
	 * @param dl	the data link
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	public void insertLink(SpectrumDataLink dl) throws SPECCHIOFactoryException {
		
		try {
			
			// create an SQL statement
			Statement stmt = getConnection().createStatement();
			
			// build the update string
			String query = "insert into spectrum_datalink_view (spectrum_id, linked_spectrum_id, datalink_type_id) "
					+ "values ("
					+ Integer.toString(dl.getReferencingId())
					+ ", "
					+ Integer.toString(dl.getReferencedId())
					+ ", "
					+ "(select datalink_type_id from datalink_type where name = '"
					+ dl.getLinkType() + "'))";

			stmt.executeUpdate(query);
			
		}
		catch (SQLException ex) {
			// bad SQL
			throw new SPECCHIOFactoryException(ex);
		}
		
	}

	
	/**
	 * Insert a spectral file into the database. This method handles the cases where
	 * there is more than one spectrum stored in a single file.
	 * 
	 * @param spec_file		the file to be inserted
	 * @param hierarchy_id	the identifier of the hierarchy node under which it is to be inserted
	 * 
	 * @return descriptor of a list of the spectrum identifiers and any errors created during the insertion
	 *
	 * @throws SPECCHIOFactoryException	the file could not be inserted
	 */
	public SpectralFileInsertResult insertSpectralFile(SpectralFile spec_file, int hierarchy_id) throws SPECCHIOFactoryException {

		SpectralFileInsertResult insert_result = new SpectralFileInsertResult();
		
		boolean special_hierarchy_files = false;
		
		Hashtable<String, Integer> subhierarchies = getSubHierarchyIds(spec_file, hierarchy_id);

		if (spec_file.getCompany().equals("SVC")
				&& spec_file.getInstrumentTypeNumber() == 1024
				&& spec_file.getNumberOfSpectra() == 3) {
			special_hierarchy_files = true;
			SpectralFileInsertResult tgt_spectrum_result, ref_spectrum_result, reflectance_spectrum_result;

			// HR 1024 files contain target and reference radiances plus
			// reflectances
			// thus here we must split them into two new hierarchies:
			// reflectance and radiance with radiance having two subfolders

			// insert target spectrum
			tgt_spectrum_result = insertSpectrumAndHierarchyLink(spec_file, 1, subhierarchies.get("Targets"));
			insert_result.add(tgt_spectrum_result);

			// insert reference spectrum
			ref_spectrum_result = insertSpectrumAndHierarchyLink(spec_file, 0, subhierarchies.get("References"));
			insert_result.add(ref_spectrum_result);

			// insert reflectance spectrum
			reflectance_spectrum_result = insertSpectrumAndHierarchyLink(spec_file, 2, subhierarchies.get("Reflectance"));
			insert_result.add(reflectance_spectrum_result);
			
			// if spectra were inserted we need to add a datalink that links the
			// target to the reference spectrum
			if (ref_spectrum_result.getSpectrumIds().get(0) != 0) {
				insertLink(new SpectrumDataLink(tgt_spectrum_result.getSpectrumIds().get(0), ref_spectrum_result.getSpectrumIds().get(0), "Spectralon data"));
				insertLink(new SpectrumDataLink(reflectance_spectrum_result.getSpectrumIds().get(0), tgt_spectrum_result.getSpectrumIds().get(0), "Radiance data"));
			}

		}

		if (spec_file.getCompany().equals("COST_OO_CSV")) {
			special_hierarchy_files = true;
			SpectralFileInsertResult tgt_spectrum_result, ref_result, reflectance_spectrum_result;

			// COST_OO_CSV files contain target and reference radiances plus
			// reflectances
			// thus here we must split them into two new hierarchies:
			// reflectance and radiance with radiance having two subfolders

			// insert target spectrum
			tgt_spectrum_result = insertSpectrumAndHierarchyLink(spec_file, 1, subhierarchies.get("Targets"));
			insert_result.add(tgt_spectrum_result);

			// insert reference spectrum
			ref_result = insertSpectrumAndHierarchyLink(spec_file, 0, subhierarchies.get("References"));
			insert_result.add(ref_result);

			// insert reflectance spectrum
			reflectance_spectrum_result = insertSpectrumAndHierarchyLink(spec_file, 2, subhierarchies.get("Reflectance"));
			insert_result.add(reflectance_spectrum_result);
			
			// if spectra were inserted we need to add a datalink that links the
			// target to the reference spectrum
			if (ref_result.getSpectrumIds().get(0) != 0) {
				insertLink(new SpectrumDataLink(tgt_spectrum_result.getSpectrumIds().get(0), ref_result.getSpectrumIds().get(0), "Spectralon data"));
				insertLink(new SpectrumDataLink(reflectance_spectrum_result.getSpectrumIds().get(0), tgt_spectrum_result.getSpectrumIds().get(0), "Radiance data"));
			}

		}
		
//		if (spec_file.getCompany().equals("PP Systems") && spec_file.getCapturingSoftwareName() != null && ( 
//				spec_file.getCapturingSoftwareName().equals("UnispecDCcf")
//				|| spec_file.getCapturingSoftwareName().equals("UniSpec Dual Channel NETCF" ||)
//				)) {
		
		if (spec_file.getCompany().equals("PP Systems") && spec_file.getFileFormatName().equals("UniSpec_SPU")) 
		{		
			special_hierarchy_files = true;
			
			// insert a spectrum
			insert_result.add(insertSpectrumAndHierarchyLink(spec_file, 1, subhierarchies.get("channel a")));
			// insert b spectrum
			insert_result.add(insertSpectrumAndHierarchyLink(spec_file, 0, subhierarchies.get("channel b")));
						
		}

		if (spec_file.getCompany().equals("GER") || (spec_file.getCompany().equals("PP Systems") && spec_file.getCapturingSoftwareName() == null)  && !spec_file.getFileFormatName().equals("UniSpec_SPU")) {
			special_hierarchy_files = true;
			SpectralFileInsertResult referencing_spectrum_result, referenced_spectrum_result;

			// GER files contain target and reference radiances
			// thus here we must split them into two new hierarchies
			
			int target_sub_hierarchy_id = subhierarchies.get("Targets");
			int reference_sub_hierarchy_id = subhierarchies.get("References");

			// insert target spectrum
			referencing_spectrum_result = insertSpectrumAndHierarchyLink(spec_file, 0, target_sub_hierarchy_id);
			insert_result.add(referencing_spectrum_result);

			// insert reference spectrum
			referenced_spectrum_result = insertSpectrumAndHierarchyLink(spec_file, 1, reference_sub_hierarchy_id);
			insert_result.add(referenced_spectrum_result);

			// if spectra were inserted we need to add a datalink that links the
			// target to the reference spectrum
			if (referencing_spectrum_result.getSpectrumIds().get(0) != 0) {
				insertLink(new SpectrumDataLink(referencing_spectrum_result.getSpectrumIds().get(0), referenced_spectrum_result.getSpectrumIds().get(0), "Spectralon data"));
			}

		}

		if (spec_file.getCompany().equals("YES")) {
			special_hierarchy_files = true;

			int total_sub_hierarchy_id = subhierarchies.get("Total");
			int diffuse_sub_hierarchy_id = subhierarchies.get("Diffuse");

			// insert all spectra where the no of spectra is the count of total
			// plus diffuse
			for (int i = 0; i < spec_file.getNumberOfSpectra(); i += 2) {
				// insert total spectrum
				SpectralFileInsertResult tmp = insertSpectrumAndHierarchyLink(spec_file, i, total_sub_hierarchy_id);
				insert_result.add(tmp);

				// insert diffuse spectrum
				tmp = insertSpectrumAndHierarchyLink(spec_file, i + 1, diffuse_sub_hierarchy_id);
				insert_result.add(tmp);
			}

		}

		if (spec_file.getCompany().equals("FGI")) {
			special_hierarchy_files = true;

			// create new sub hierarchy for this data collection
			String[] tokens = spec_file.getBasename().split("\\.");
			int FGI_collection_id = subhierarchies.get(tokens[0]);

			// create sub hierarchies within the data collection for HDRF, BRF,
			// etc

			if (spec_file.isFgiHdrfBrfCombined() == false) {
				int FGI_sub_id = getSubHierarchyId(FGI_collection_id, tokens[1]);
				int FGI_I_id = 0;
				int FGI_Q_id = 0;
				int FGI_U_id = 0;

				if (spec_file.getSpectralGroupingSize() > 1) {
					FGI_I_id = subhierarchies.get("R");
					FGI_Q_id = subhierarchies.get("Mueller10");
					FGI_U_id = subhierarchies.get("Mueller20");
							
				}

				for (int i = 0; i < spec_file.getNumberOfSpectra(); i++) {
					if (spec_file.getSpectralGroupingSize() > 1) {
						insert_result.add(insertSpectrumAndHierarchyLink(spec_file, i++, FGI_I_id));
						insert_result.add(insertSpectrumAndHierarchyLink(spec_file, i++, FGI_Q_id));
						insert_result.add(insertSpectrumAndHierarchyLink(spec_file, i, FGI_U_id));

					} else {
						insert_result.add(insertSpectrumAndHierarchyLink(spec_file, i, FGI_sub_id));
					}
				}

			} else // hdrf_brf_combined == true
			{
				int FGI_BRF_sub_id = subhierarchies.get("BRF");
				int FGI_HDRF_sub_id = subhierarchies.get("HDRF");

				for (int i = 0; i < spec_file.getNumberOfSpectra(); i++) {

					if (spec_file.getFgiHdrfBrfFlag(i).equals("BRF")) {
						insert_result.add(insertSpectrumAndHierarchyLink(spec_file, i, FGI_BRF_sub_id));
					}

					if (spec_file.getFgiHdrfBrfFlag(i).equals("HDRF")) {
						insert_result.add(insertSpectrumAndHierarchyLink(spec_file, i, FGI_HDRF_sub_id));
					}

				}

			}

		}

		if (spec_file.getAsdV7() == true) {
			special_hierarchy_files = true;
			// insert dn spectrum
			insert_result.add(insertSpectrumAndHierarchyLink(spec_file, 0, subhierarchies.get("DN")));

			// insert radiance spectrum
			if (spec_file.getAsdV7RadianceFlag() == true) {
				insert_result.add(insertSpectrumAndHierarchyLink(spec_file, 1, subhierarchies.get("Radiance")));
			}

			// insert reflectance spectrum
			if (spec_file.getAsdV7ReflectanceFlag() == true) {
				insert_result.add(insertSpectrumAndHierarchyLink(spec_file, 1, subhierarchies.get("Reflectance")));
			}
			

		}

		if (!special_hierarchy_files) {

			for (int i = 0; i < spec_file.getNumberOfSpectra(); i++) {
				insert_result.add(insertSpectrum(spec_file, i, hierarchy_id, special_hierarchy_files));
			}

			// insert links to the hierarchies
			insertHierarchySpectrumReferences(spec_file.getHierarchyId(), insert_result.getSpectrumIds());
		}
		
		return insert_result;
	}
	
	
	/**
	 * Insert a spectrum into the database.
	 * 
	 * @param spec_file		the spectral file from which the spectrum will be drawn
	 * @param spec_no		the index of the spectrum in the file
	 * @param hierarchy_id	the identifier of the node under which the spectrum will be placed
	 * @param auto_sub_h	boolean indicating if this spectral file required the creation of sub-hierarchies
	 * 
	 * @return the identifier of the new spectrum
	 * 
	 * @throws SPECCHIOFactoryException	could not insert the spectrum
	 */
	public SpectralFileInsertResult insertSpectrum(SpectralFile spec_file, int spec_no, int hierarchy_id, boolean auto_sub_hierarchies) throws SPECCHIOFactoryException {
		
		SpectralFileInsertResult insert_result = new SpectralFileInsertResult();
		
		// this is the final check if the spectrum does exist.
		// the first check on the client side will check for all subhierachies and cause a load if it is missing in one of the automatically create sub-hierarchies
		// therefore, for each hierarchy the check must be carried out again.
		// this second check is reduced to only spectral files where subhierarchies exist
		
		boolean exists = false;
		if(auto_sub_hierarchies) exists = this.spectrumExists(spec_file.getFilename(), hierarchy_id);
		
		
		
		if(!exists)
		{
			try {
				
				Integer id = 0;
				Integer illumination_source_id = 0;
				Integer gonio_id = 0;
				Integer sampling_environment_id = 0;
				id_and_op_struct hierarchy_id_and_op, campaign_id_and_op, illumination_source_id_and_op, gonio_id_and_op, sampling_environment_id_and_op;
				String query;
				ResultSet rs;
				SQL_StatementBuilder SQL = new SQL_StatementBuilder(getConnection());
		
				campaign_id_and_op = SQL.is_null_key_get_val_and_op(campaign.getId());
				hierarchy_id_and_op = SQL.is_null_key_get_val_and_op(hierarchy_id);
				
				Metadata md = spec_file.getEavMetadata(spec_no);
		
				// if there is a position available for the spectrum create new position
				// record
				if (spec_file.getPos().size() > spec_no && spec_file.getPos(spec_no) != null) {
		
					MetaParameter mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Longitude", "Location"));
					mp.setValue(spec_file.getPos(spec_no).longitude, "Degrees");
					md.add_entry(mp);			
					
					mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Latitude", "Location"));
					mp.setValue(spec_file.getPos(spec_no).latitude, "Degrees");
					md.add_entry(mp);	
					
					if(spec_file.getPos(spec_no).altitude != null)
					{
						mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Altitude", "Location"));
						mp.setValue(spec_file.getPos(spec_no).altitude, "Degrees");
						md.add_entry(mp);	
					}
					
					if (!spec_file.getPos(spec_no).location_name.equals(""))
					{
						mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Location Name", "Location"));
						mp.setValue(spec_file.getPos(spec_no).location_name, "String");
						md.add_entry(mp);	
					}
		
				}
		
				// illum
				if (spec_file.getLightSource() != null) {
		
					query = "select illumination_source_id from illumination_source where name = '"
							+ spec_file.getLightSource() + "'";
					Statement stmt = getStatementBuilder().createStatement();
					rs = stmt.executeQuery(query);
		
					while (rs.next()) {
						illumination_source_id = rs.getInt(1);
					}
		
					rs.close();
					stmt.close();
		
				}
				illumination_source_id_and_op = SQL
						.is_null_key_get_val_and_op(illumination_source_id);
		
				// gonio
				if (spec_file.getInstrumentName() != null) {
		
					gonio_id = getDataCache().get_goniometer_id(spec_file.getInstrumentName());
		
				}
				gonio_id_and_op = SQL.is_null_key_get_val_and_op(gonio_id);
		
				// sampling environment
				if (spec_file.getSamplingEnvironment() != null) {			
					sampling_environment_id = getDataCache().get_sampling_environment_id(spec_file.getSamplingEnvironment());			
				}
				sampling_environment_id_and_op = SQL
						.is_null_key_get_val_and_op(sampling_environment_id);
				
				
				// geometry via EAV: SPECCHIO V3.0
				if (spec_file.getIlluminationAzimuths().size() > 0)
				{
					MetaParameter mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Illumination Azimuth", "Sampling Geometry"));
					mp.setValue(spec_file.getIlluminationAzimuth(spec_no), "Degrees");
					md.add_entry(mp);										
				}
				
				if (spec_file.getIlluminationZeniths().size() > 0)
				{
					MetaParameter mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Illumination Zenith", "Sampling Geometry"));
					mp.setValue(spec_file.getIlluminationZenith(spec_no), "Degrees");
					md.add_entry(mp);										
				}		
				
				if (spec_file.getSensorAzimuths().size() > 0)
				{
					MetaParameter mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Sensor Azimuth", "Sampling Geometry"));
					mp.setValue(spec_file.getSensorAzimuth(spec_no), "Degrees");
					md.add_entry(mp);										
				}	
				
				if (spec_file.getSensorZeniths().size() > 0)
				{
					MetaParameter mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Sensor Zenith", "Sampling Geometry"));
					mp.setValue(spec_file.getSensorZenith(spec_no), "Degrees");
					md.add_entry(mp);										
				}		
				
				if (spec_file.getArmLengths() != null && spec_file.getArmLength(spec_no) != null) {
					MetaParameter mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Sensor Distance", "Sampling Geometry"));
					mp.setValue(spec_file.getArmLength(spec_no), "m");
					md.add_entry(mp);											
				}
				
			
				// file format
				int file_format_id = getIdForFileFormat(spec_file.getFileFormatName());
				
				int sensor_id = getDataCache().get_sensor_id_for_file(spec_file, spec_no);
				
				if (sensor_id == 0)
				{
					insert_result.addError(new SpecchioMessage("No matching sensor found.", SpecchioMessage.WARNING));
				}
				
				SpecchioMessage msg = new SpecchioMessage();
				
				Instrument instrument = getDataCache().get_instrument_id_for_file(spec_file, spec_no, msg);
				
				String instrument_id = "null";
				int calibration_id = 0;
				
				if(instrument != null)
				{
					instrument_id = Integer.toString(instrument.getInstrumentId());			
					calibration_id = instrument.getCalibrationId();
				}
				
				if (msg.getMessage() != null)
				{
					insert_result.addError(msg);
				}
				
				
				query = "INSERT INTO spectrum_view "
						+ "("
						+ " hierarchy_level_id, sensor_id, campaign_id, "
						+ "file_format_id, instrument_id, calibration_id, "
						+ "measurement_unit_id, measurement_type_id, illumination_source_id, goniometer_id, sampling_environment_id) "
						+ "VALUES (" 
						+ hierarchy_id_and_op.id
						+ ", "
						+ SQL.is_null_key_get_val_and_op(sensor_id).id
						+ ", "
						+ campaign_id_and_op.id
						+ ", "
						+ (file_format_id == -1 ? "null" : Integer.toString(file_format_id))
						+ ", "
						+ instrument_id
						+ ", "
						+ (calibration_id == 0 ? "null" : Integer.toString(calibration_id))
						+ ", "
						+ SQL.is_null_key_get_val_and_op(getDataCache().get_measurement_unit_id_for_file(spec_file, spec_no)).id
						+ ", "
						+ getDataCache().get_measurement_type_id_for_file(spec_file, spec_no)
						+ ", "
						+ illumination_source_id_and_op.id
						+ ", "
						+ gonio_id_and_op.id
						+ ", "
						+ sampling_environment_id_and_op.id + ")";
				
				Statement stmt = getStatementBuilder().createStatement();
				stmt.executeUpdate(query);
				
				rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
				while (rs.next())
					id = rs.getInt(1);
				rs.close();
				
				stmt.close();
		
				// insert the measurement blob
				String update_stm = "UPDATE spectrum_view set measurement = ? where spectrum_id = "
						+ id.toString();
				PreparedStatement statement = SQL.prepareStatement(update_stm);
		
				InputStream refl = spec_file.getInputStream(spec_no);
				statement.setBinaryStream(1, refl, spec_file.getNumberOfChannels(0) * 4);
				statement.executeUpdate();
		
				try {
					refl.close();
				} catch (IOException e) {
					// should never happen
					e.printStackTrace();
				}
				
				// filename
				MetaParameter mp = MetaParameter.newInstance(getAttributes().get_attribute_info("File Name", "General"));
				mp.setValue(spec_file.getSpectrumFilename(spec_no), "String");
				md.add_entry(mp);
				
				// capture and insert times
				Date capture_date = spec_file.getCaptureDate(spec_no);
				if (capture_date != null) {
					MetaDate mpd = (MetaDate)MetaParameter.newInstance(getAttributes().get_attribute_info("Acquisition Time", "General"));
					mpd.setValue(capture_date);
					md.add_entry(mpd);
				}
				
				// UTC insert time
				TimeZone tz = TimeZone.getTimeZone("UTC");
				Calendar cal = Calendar.getInstance(tz);	
				MetaDate mpd = (MetaDate)MetaParameter.newInstance(getAttributes().get_attribute_info("Loading Time", "General"));	
				mpd.setValue(cal.getTime());
				md.add_entry(mpd);				
				
				// add instrument number as EAV if instrument is not defined in DB but not empty
				if(instrument_id.equals("null") &&
						spec_file.getInstrumentNumber() != null &&
						!spec_file.getInstrumentNumber().equals(""))			
				{			
					mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Instrument Serial Number", "Instrument"));
					mp.setValue(spec_file.getInstrumentNumber());
					md.add_entry(mp);				
				}
				
				// cloud cover (convert from oktas)
				if (spec_file.getWeather() != null && spec_file.getWeather().length() > 0) {
					String oktas = spec_file.getWeather().substring(0, 1);
					
					int okta_as_int = Integer.parseInt(oktas);			
					double cloud_cover =  okta_as_int / 8.0 * 100;
					
					mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Cloud Cover", "Environmental Conditions"));
					mp.setValue(cloud_cover, "%");
					md.add_entry(mp);				
					
		
				}
				
				// add foreoptic to EAV if defined
				if(spec_file.getForeopticDegrees() != 0)
				{
					mp = MetaParameter.newInstance(getAttributes().get_attribute_info("FOV", "Optics"));
					mp.setValue(spec_file.getForeopticDegrees(), "Degrees");
					md.add_entry(mp);								
				}
				
				if(spec_file.getComment() != null)
				{
					mp = MetaParameter.newInstance(getAttributes().get_attribute_info("File Comments", "General"));
					mp.setValue(spec_file.escape_string(spec_file.getComment()), "String");
					md.add_entry(mp);								
				}		
		
				// update the EAV with possible garbage flags
				if(spec_file.getGarbageIndicator() == true)
				{
					mp = MetaParameter.newInstance(getAttributes().get_attribute_info("Garbage Flag", "General"));
					mp.setValue(1, "Raw");
					md.add_entry(mp);
				}		
				
				
				// spectra names
				 if (spec_file.getSpectraNames().size() > 0) {			 
					 mp = MetaParameter.newInstance(getAttributes().get_attribute_info(spec_file.getSpectrumNameType(), "Names"));
					 mp.setValue(spec_file.getSpectrumName(spec_no), "String");
					 md.add_entry(mp);			 
				 }
				
		
				// automatic processing of EAV data
				if (spec_file.getEavMetadata(spec_no) != null) {
					ArrayList<Integer> eav_ids = getEavServices().insert_metadata_into_db(campaign.getId(), spec_file.getEavMetadata(spec_no));
					getEavServices().insert_primary_x_eav(id, eav_ids);
				}
		
				// update of the attribute default storage field information if needed
				// removed this call, as administrators should always define the default storage field (no more auto-generation of attributes for SPECCHIO instances)
				//getAttributes().define_default_storage_fields();
				
				insert_result.addSpectrumId(id);
		
				return insert_result;
				
			}
			catch (IOException ex) {
				// database error
				throw new SPECCHIOFactoryException(ex);
			}
			catch (SQLException ex) {
				// database error
				throw new SPECCHIOFactoryException(ex);
			}
			catch (MetaParameterFormatException ex) {
				// a metaparameter object has the wrong type
				throw new SPECCHIOFactoryException(ex);
			}
		}
		else
		{
			insert_result.addSpectrumId(0);
			return insert_result;
		}

	}
	
	
	/**
	 * Helper method for insertSpectra(). Insert both a spectrum and a hierarchy link.
	 * 
	 * @param spec_file		the spectral file to be inserted
	 * @param spec_no		the index of the spectrum to be inserted
	 * @param hierarchy_id	the identifier of the hierarchy node into which to insert
	 * 
	 * @return insert result descriptor of the new spectrum
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	private SpectralFileInsertResult insertSpectrumAndHierarchyLink(SpectralFile spec_file,
			int spec_no, int hierarchy_id)
			throws SPECCHIOFactoryException {

		SpectralFileInsertResult insert_result = insertSpectrum(spec_file, spec_no, hierarchy_id, true); // always a spectrum that prompted auto-subhierarchy creation

		if (insert_result.getSpectrumIds().get(0) > 0) // always only one spectrum id is returned in the list
		{
			insertHierarchySpectrumReferences(hierarchy_id, insert_result.getSpectrumIds().get(0),
					0);			
		}


		return insert_result;
	}
		
	
	/**
	 * Helper method for insertSpectra(). Insert both a spectrum and a hierarchy link.
	 * 
	 * @param spec_file		the spectral file to be inserted
	 * @param spec_no		the index of the spectrum to be inserted
	 * @param hierarchy_id	the identifier of the hierarchy node into which to insert
	 * @param recusion_break_at_parent_id   id of a hierarchy where to break the recursive insert (deprecated)
	 * 
	 * @return insert result descriptor of the new spectrum
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	private SpectralFileInsertResult insertSpectrumAndHierarchyLink(SpectralFile spec_file,
			int spec_no, int hierarchy_id, int recursion_break_at_hierarchy_id)
			throws SPECCHIOFactoryException {

		SpectralFileInsertResult insert_result = insertSpectrum(spec_file, spec_no, hierarchy_id, true); // always a spectrum that prompted auto-subhierarchy creation

		if (insert_result.getSpectrumIds().get(0) > 0) // always only one spectrum id is returned in the list
		{
			insertHierarchySpectrumReferences(hierarchy_id, insert_result.getSpectrumIds().get(0),
					recursion_break_at_hierarchy_id);			
		}


		return insert_result;
	}
	
	
	/**
	 * Test for the existence of a given spectral file in the database.
	 * 
	 * @param descriptor	the descriptor of the file to be tested for
	 * 
	 * @return true if the file exists in the database, and false otherwise
	 */
	public boolean spectrumExists(SpectralFile spec_file, int hierarchy_id) throws SPECCHIOFactoryException {
		
		// get sub hierarchy ids for this spectral file
		Hashtable<String, Integer> sub_ids = this.getSubHierarchyIds(spec_file, hierarchy_id);
		
		boolean exists = true;
		
		if(sub_ids.size() > 0)
		{		
			// check if the spectrum exists in all the sub hierarchies
			Enumeration<Integer> li = sub_ids.elements();
			
			
			
			while(li.hasMoreElements())
			{
				exists = exists & spectrumExists(spec_file.getSpectrumFilename(0), li.nextElement());
			}			
		}
		else
		{
			exists = spectrumExists(spec_file.getSpectrumFilename(0), hierarchy_id);			
		}
		
		return exists;
	}
	
	
	/**
	 * Test for the existence of a filename within a sub-hierarchy.
	 * 
	 * @oaram filename		the filename
	 * @param hierarchy_id	the identifier of the hierarchy to check
	 * 
	 * @return true if a spectrum with the specified filename exists within the identified hierarchy
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	private boolean spectrumExists(String filename, int hierarchy_id) throws SPECCHIOFactoryException {
		
		try {
			// build a query that will return "1" if the file exists
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			id_and_op_struct p_id_and_op = new id_and_op_struct(hierarchy_id);
			String query = "select count(spectrum.spectrum_id) from spectrum, spectrum_x_eav, eav, hierarchy_level_x_spectrum" +
					" where spectrum.spectrum_id=spectrum_x_eav.spectrum_id" +
					" and spectrum_x_eav.eav_id=eav.eav_id" +
					" and eav.attribute_id=" + getAttributes().get_attribute_id("File Name") +
					" and eav.string_val=" + SQL.quote_string(filename) +
					" and hierarchy_level_x_spectrum.spectrum_id=spectrum.spectrum_id" +
					" and hierarchy_level_x_spectrum.hierarchy_level_id " + p_id_and_op.op + " " + p_id_and_op.id;

			// execute the statement
			ResultSet rs = stmt.executeQuery(query);
			int exists = 0;
			while (rs.next()) {
				exists = rs.getInt(1);
			}
			rs.close();
			stmt.close();
			
			return exists > 0;
			
		} catch (SQLException ex) {
			// bad SQL
			throw new SPECCHIOFactoryException(ex);
		}
		                          
		
	}	
	

}
