package ch.specchio.factories;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ch.specchio.eav_db.EAVDBServices;
import ch.specchio.eav_db.SQL_StatementBuilder;
import ch.specchio.spaces.Space;
import ch.specchio.types.CategoryTable;
import ch.specchio.types.ConflictInfo;
import ch.specchio.types.ConflictStruct;
import ch.specchio.types.ConflictTable;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.Metadata;
import ch.specchio.types.Spectrum;
import ch.specchio.types.TaxonomyNodeObject;
import ch.specchio.types.attribute;
import ch.specchio.types.Units;

/**
 * Class for manipulating metadata in the database.
 */
public class MetadataFactory extends SPECCHIOFactory {
	
	/**
	 * Constructor. 
	 * 
	 * @param db_user		database account user name
	 * @param db_password	database account password
	 * 
	 * @throws SPECCHIOFactoryException	could not establish initial context
	 */
	public MetadataFactory(String db_user, String db_password) throws SPECCHIOFactoryException {

		super(db_user, db_password);
		
	}
	
	
	/**
	 * Copy constructor. Construct a new factory that uses the same database connection
	 * as an existing factory.
	 * 
	 * @param factory	the existing factory
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public MetadataFactory(SPECCHIOFactory factory) throws SPECCHIOFactoryException {
		
		super(factory);
		
	}
	
	
	/**
	 * Check for metadata conflicts.
	 * 
	 * @param cd_d	the conflict detection descriptor
	 * 
	 * @return a hash mapping metadata field names to conflict information structures
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database
	 */
	public ConflictTable detectConflicts(Integer[] ids, String[] fieldnames) throws SPECCHIOFactoryException {
		
		
		ConflictTable stati = new ConflictTable();


		try {
			
			ArrayList<String> field_cnt_strings = new ArrayList<String>();

			
			for (int i=0;i<fieldnames.length;i++)
			{
				field_cnt_strings.add("count(distinct " + fieldnames[i] + "_id" + ")");				
			}
			
			
			String query = getStatementBuilder().assemble_sql_select_query(getStatementBuilder().conc_cols(field_cnt_strings), "spectrum", "spectrum_id in (" + 
					getStatementBuilder().conc_ids(ids) +
					")");

	
			Statement stmt = getStatementBuilder().createStatement();
			ResultSet rs;
			ConflictStruct conflict;

			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				
				
				for (int j=1;j<=fieldnames.length;j++)
				{
					int cnt = rs.getInt(j);
					
					conflict = new ConflictStruct();
					
					if (cnt == 0) conflict.setStatus(0);
					if (cnt == 1) conflict.setStatus(1);
					if (cnt > 1) conflict.setStatus(2);
					
					ConflictInfo conflict_info = new ConflictInfo(conflict);
					stati.put(fieldnames[j-1], conflict_info);
					
				}
						
			
			}
			
			rs.close();	
			stmt.close();
				
		} catch (SQLException ex) {
			// bad SQL
			throw new SPECCHIOFactoryException(ex);
		}
		
		return stati;
	}
	
	
	/**
	 * Check for conflicts in EAV metadata.
	 * 
	 * @param ids	the attribute identifiers to be checked
	 * 
	 * @return a hash mapping attribute id to conflict information structures
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database
	 */
	public ConflictTable detectEavConflicts(Integer[] ids) throws SPECCHIOFactoryException
	{
		ConflictTable stati = new ConflictTable();

		try {
			Statement stmt = getStatementBuilder().createStatement();
			
	
			boolean double_check = false;
			int distinct_spectrum_id_cnt;
			int distinct_eav_id_cnt;
			double multi_value_cnt;
			Integer attribute_id;
			Integer eav_id;
			ConflictStruct conflict;
			ArrayList<Integer> eav_ids_to_double_check = new ArrayList<Integer>();
		
			String query = "SELECT count(distinct sxe.spectrum_id), count(distinct eav.eav_id), count(eav.eav_id)/count(distinct sxe.spectrum_id), attribute_id, eav.eav_id from spectrum_x_eav sxe, eav eav where sxe.spectrum_id in (" +
			getStatementBuilder().conc_ids(ids) +
				") and sxe.eav_id = eav.eav_id group by eav_id order by attribute_id";	
			
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
			
				distinct_spectrum_id_cnt = rs.getInt(1);		
				distinct_eav_id_cnt = rs.getInt(2);	
				multi_value_cnt = rs.getDouble(3);	
				attribute_id  = rs.getInt(4);	
				eav_id = rs.getInt(5);		
				
				conflict = new ConflictStruct();
				if (distinct_eav_id_cnt == 1 && distinct_spectrum_id_cnt == ids.length)
				{
						conflict.setStatus(1); // unique
						double_check = true;
				}
				else if (distinct_eav_id_cnt== 1 && distinct_spectrum_id_cnt != ids.length)
				{
					conflict.setStatus(2); // ambiguous
					double_check = false;
				}
				else
				{
					System.out.println("unexpected case in conflict detection !!!!");
				}
				conflict.setNumberOfSelectedRecords(ids.length);
				conflict.setNumberOfSharingRecords(1);					
										
				ConflictInfo conflict_info = stati.get(attribute_id);	
				if(conflict_info == null)
					conflict_info = new ConflictInfo();
				
				conflict_info.addConflict(eav_id, conflict);
				
				stati.put(attribute_id, conflict_info);
					
				if(double_check)
					eav_ids_to_double_check.add(eav_id);
				

			}
				
				
			// second query to figure out the number of total spectra referring to the eav entries (number of shared records)
			query = "SELECT count(sxe.spectrum_id), attribute_id, eav.eav_id from spectrum_x_eav sxe, eav eav where sxe.eav_id in (" +
			getStatementBuilder().conc_ids(eav_ids_to_double_check) +
				") and sxe.eav_id = eav.eav_id group by eav.eav_id order by attribute_id";			
			
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				
				multi_value_cnt = rs.getDouble(1);	
				attribute_id  = rs.getInt(2);				
				eav_id = rs.getInt(3);		
				
				if(multi_value_cnt > 1)
				{
					// update existing conflict data
					ConflictInfo conflict_info = stati.get(attribute_id);
					
					conflict = conflict_info.getConflictData(eav_id);
					conflict.setStatus(3);
					conflict.setNumberOfSharingRecords((int) multi_value_cnt);

				}					
					
			}				
				
			rs.close();						
			stmt.close();
			
			
		} catch (SQLException ex) {
			// bad SQL
			throw new SPECCHIOFactoryException(ex);
		}
		
		
		return stati;
		
	}
	
	
	/**
	 * Get the list of attributes in a given category.
	 * 
	 * @param category_name		the name of the category
	 * 
	 * @returns an array of all the attributes in the given category
	 * 
	 * @throws invalid value for category_name
	 */
	public List<attribute> getAttributesForCategory(String category_name) {
		
		return getAttributes().get_attributes(category_name);
		
	}
	
	
	/**
	 * Get the units for an attribute.
	 * 
	 * @param attr	the attribute
	 * 
	 * @return a Units object representing the attribute's units
	 */
	public Units getAttributeUnits(attribute attr) {
		
		return getAttributes().get_units(attr);
		
		
	}
	
	 	
	/**
	 * Get a hash table mapping identifiers to names.
	 * 
	 * @param category	the category name
	 * 
	 * @returns a category table mapping identifiers to names
	 * 
	 * @throws SPECCHIOFactoryException	invalid value for category
	 */
	public CategoryTable getCategoryTable(String category) throws SPECCHIOFactoryException {
		
		CategoryTable table = new CategoryTable();
		
		try {
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			String query = "select " + SQL.quote_identifier(category + "_id") + ", name from " + SQL.quote_identifier(category);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				table.put(rs.getInt(1), rs.getString(2));
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return table;
		
	}
	
	
	/**
	 * Get the metadata for a given spectrum.
	 * 
	 * @param spectrum_id	the identifier of the spectrum
	 * 
	 * @return the metadata for the specific spectrum
	 * 
	 * @throws SPECCHIOFactoryException the spectrum does not exist
	 */
	public Metadata getMetadataForSpectrum(int spectrum_id) throws SPECCHIOFactoryException {
		
		Metadata md = new Metadata();
			
		md.setFrameId(spectrum_id);
			
		ArrayList<Integer> metaparameter_ids = new ArrayList<Integer>();
		
		metaparameter_ids = getEavServices().get_eav_ids(spectrum_id);			

		// bulk reading of metaparameters
		try {
			getEavServices().metadata_bulk_loader(md, metaparameter_ids);
		} catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}

		return md;
			
	}
	
	
	/**
	 * Get the values of a given meta-parameter for a list of spectrum identifiers.
	 * 
	 * @param id		the spectrum identifiers for which to retrieve metadata
	 * @param attrName	the attribute name
	 * @param distinct	if true, return distinct values only
	 * 
	 * @return a list of meta-parameter objects corresponding to the desired attribute of each input id
	 *
	 * @throws SPECCHIOFactoryException	database error
	 */
	public ArrayList<MetaParameter> getMetaParameterValues(ArrayList<Integer> ids, Integer attrId, boolean distinct) throws SPECCHIOFactoryException {
		
		ArrayList<MetaParameter> mp_list = new ArrayList<MetaParameter>();
		
		try {
			// create SQL-building objects
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			String primary_id_name = getEavServices().get_primary_id_name();
			String primary_x_eav_tablename = getEavServices().get_primary_x_eav_tablename();
			String temp_tablename = SQL.prefix(getTempDatabaseName(), "eav_frame_compilation");
			
			
			attribute attr;
			//int attrId = getAttributes().get_attribute_id(attrName);
			if (attrId != 0) {
				// get the descriptor of the desired attribute
				attr = getAttributes().get_attribute_info(attrId);
				
				// create temporary table
				String ddl_string = "CREATE TEMPORARY TABLE IF NOT EXISTS " + temp_tablename + " " +
						"(eav_id INT NOT NULL, " +
						primary_id_name + " " +
						"INT NOT NULL, " +
						"id INT NOT NULL " +
						"AUTO_INCREMENT, PRIMARY KEY (id))";
				stmt.executeUpdate(ddl_string);
				
				// insert eav idenifiers into the temporary table
				String conc_ids = SQL.conc_ids(ids);
				String query = "insert into " + temp_tablename + " " +
						"(eav_id, " +  primary_id_name + ") " +
						"select eav_id, " + primary_id_name + " from " + primary_x_eav_tablename +
							" where " + primary_id_name + " in (" + conc_ids + ") " +
							" order by FIELD (" + primary_id_name + ", "+ conc_ids +")";
				stmt.executeUpdate(query);
				
				// build the list of metaparameters
				query = "select " + ((distinct)? "distinct " : "") + SQL.prefix("eav", attr.getDefaultStorageField()) + " " +
						"from eav, " + temp_tablename + " efc " +
						"where eav.attribute_id = "  + Integer.toString(attrId) + " and eav.eav_id = efc.eav_id order by efc.id";
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) 
				{
					Object o = rs.getObject(1);
					if (o != null) {
						try {
							MetaParameter mp = MetaParameter.newInstance(attr);
							mp.setValue(o);
							mp_list.add(mp);
						}
						catch (MetaParameterFormatException ex) {
							// should never happen but we'll log an error just in case
							System.err.println("Metaparameter format exception when converting " + attr.getDefaultStorageField() + " attribute.");
						}
					}
				}
				rs.close();						
				
				
				// clear temporary table
				query = "delete from " + temp_tablename;
				stmt.executeUpdate(query);
				
				// clean up
				stmt.close();
			}
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return mp_list;
		
	}
	
	
	/**
	 * Get the data policies for a collection of space.
	 * 
	 * @param spaces	the spaces
	 * 
	 * @return a list of Strings representing the policies that apply to the input spae
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public ArrayList<String> getPoliciesForSpace(Space space) throws SPECCHIOFactoryException {
		
		// get a list of spectrum identifiers associated with policies
		int data_policy_attr_id = getAttributes().get_attribute_id("Data Usage Policy");
		ArrayList<Integer> spectra_with_policies = getEavServices().filter_by_eav(space.getSpectrumIds(), data_policy_attr_id);
		
		// retrieve the policy objects and convert to strings
		ArrayList<String> policies = new ArrayList<String>();
		if(spectra_with_policies.size() > 0) {
			for (Object value : getMetaParameterValues(spectra_with_policies, data_policy_attr_id, true)) {
				policies.add(((MetaParameter) value).getValue().toString());
			}
		}
		
		return policies;
		
	}
	
	
	
	/**
	 * Get the root node of a taxonomy
	 * 
	 * @param attribute_id	id of the required taxonomy
	 * 
	 * @return taxonomy node
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */	
	public TaxonomyNodeObject getTaxonomyRoot(int attribute_id) {
	
		 // get attribute info to get name of taxonomy
		 attribute attr_info = this.getAttributes().get_attribute_info(attribute_id);
		 
		 TaxonomyNodeObject root = new TaxonomyNodeObject(attr_info.getName(), attribute_id);
		 			 		 
		 return root;	
	 }
	 
	
	/**
	 * Get the node of a taxonomy
	 * 
	 * @param taxonomy_id	id of the required taxonomy
	 * 
	 * @return taxonomy node
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	
	public TaxonomyNodeObject getTaxonomyObject(int taxonomy_id) throws SPECCHIOFactoryException {
		
		// loading children of root
		String query = "select attribute_id, name, code, description from taxonomy where taxonomy_id = " + taxonomy_id;
		
		TaxonomyNodeObject tn = null;
		
		Statement stmt;
		try {
			stmt = getStatementBuilder().createStatement();
			
			
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {	
				
				int i = 1;
				int attribute_id = rs.getInt(i++);
				String name = rs.getString(i++);
				String code = rs.getString(i++);
				String description = rs.getString(i++);
				
				tn = new TaxonomyNodeObject(name, attribute_id, taxonomy_id, null);
				tn.setCode(code);
				tn.setDescription(description);
						
			}			
			rs.close();
			stmt.close();			
		} catch (SQLException e) {
			throw new SPECCHIOFactoryException(e);
		}

		return tn;
	}
	
	
	/**
	 * Get the children of a taxonomy node
	 * 
	 * @param parent_node	node whose children are required
	 * 
	 * @return taxonomy node list
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */	
	public ArrayList<TaxonomyNodeObject> getTaxonomyChildren(TaxonomyNodeObject parent_node) throws SPECCHIOFactoryException {
		
		ArrayList<TaxonomyNodeObject> children = new ArrayList<TaxonomyNodeObject>();
		
		try {
			Statement stmt = getStatementBuilder().createStatement();
			String query = "select taxonomy_id, name, code, description from taxonomy where " +
					"attribute_id = " + parent_node.getAttribute_id() + " and " +
					( (parent_node.getId() == 0) ? "parent_id is null" : "parent_id = " + parent_node.getId());
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {	
				int i = 1;
				int taxonomy_id = rs.getInt(i++);
				String name = rs.getString(i++);
				String code = rs.getString(i++);
				String description = rs.getString(i++);
				
				TaxonomyNodeObject child = new TaxonomyNodeObject(name, parent_node.getAttribute_id(), taxonomy_id, parent_node);
				child.setCode(code);
				child.setDescription(description);
				children.add(child);
						
			}			
			rs.close();
			stmt.close();			
		} catch (SQLException e) {
			throw new SPECCHIOFactoryException(e);
		}
		
		return children;
		
	}

	 
	 
	
	/**
	 * Remove metadata.
	 * 
	 * @param mp	the item of metadata to be removed
	 * @param ids	the list of spectra from which the metadata is to be removed (null for all spectra)
	 */
	public void removeMetadata(MetaParameter mp, Integer[] ids) {
		
		getEavServices().delete_primary_x_eav(ids, mp.getEavId());
		
	}
	
	/**
	 * Remove metaparameters of given attribute id from all spectra.
	 * 
	 * @param mp	the attribute id of the metaparameters to be removed
	 * @param ids	the list of spectra from which the metadata is to be removed (null for all spectra)
	 */
	public void removeMetadata(int attribute_id, ArrayList<Integer> ids) {
		
		// get all eav ids for that attribute and spectra ids
		ArrayList<Integer> eav_ids = getEavServices().get_eav_ids(ids, attribute_id);
		
		for(int eav_id : eav_ids)
		{
			getEavServices().delete_primary_x_eav(ids, eav_id);
		}
		
	}
	
	

	/**
	 * Remove metadata from all spectra.
	 * 
	 * @param mp	the item of metadata to be removed
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public void removeMetadata(MetaParameter mp) throws SPECCHIOFactoryException {
		
		try {
			EAVDBServices eav = getEavServices();
			ArrayList<Integer> spectrum_ids = eav.getPrimaryIds(mp.getEavId());
			eav.delete_primary_x_eav(spectrum_ids, mp.getEavId());
			eav.delete_eav(mp.getEavId());
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
	}
	
	
	/**
	 * Update an item of metadata for a given set of identifiers.
	 * 
	 * @param mp		the new metadata
	 * @param ids		the identifiers of the spectra to be updated
	 * 
	 * @return the identifier of the inserted metadata
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public int updateMetadata(MetaParameter mp, Integer[] ids) throws SPECCHIOFactoryException {
		
		int eav_id = 0;

		try {
			EAVDBServices eav = getEavServices();
			if(mp.getEavId() == 0)
			{
				// get the campaign to which these metaparameters belong
				int campaign_id = 0;
				if (ids.length > 0) {
					SpectrumFactory sf = new SpectrumFactory(this);
					Spectrum s = sf.getSpectrum(ids[0], false);
					campaign_id = s.getCampaignId();
					sf.dispose();
				}
				
				mp = eav.reduce_redundancy(mp);
				eav_id = eav.insert_metaparameter_into_db(campaign_id, mp);
				eav.insert_primary_x_eav(ids, eav_id);
			}
			else
			{
				eav.update_metaparameter(mp);
				eav_id = mp.getEavId();
			}
		}
		catch (IOException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return eav_id;
		
	}
	
	
	/**
	 * Update and re-identify an item of metadata for a given set of identifiers.
	 * 
	 * @param mp		the new metadata
	 * @param ids		the identifiers of the spectra to be updated
	 * 
	 * @return the identifier of the inserted metadata
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public int updateMetadataWithNewId(MetaParameter mp, Integer[] ids) throws SPECCHIOFactoryException {
		
		int eav_id;

		try {
			EAVDBServices eav = getEavServices();
			
			// save the old id so that we can delete it later
			Integer old_eav_id = mp.getEavId();
			Integer old_campaign_id = eav.get_campaign_id_for_eav(mp.getEavId());
			
			// force re-insertion of the meta-parameter by setting its eav id to 0
			mp.setEavId(0);
			eav_id = eav.insert_metaparameter_into_db(old_campaign_id, mp);
			
			// insert the new id and remove the old one
			eav.insert_primary_x_eav(ids, mp.getEavId());			
			eav.delete_primary_x_eav(ids, old_eav_id);
		}
		catch (IOException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return eav_id;
		
	}




}
