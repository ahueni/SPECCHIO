package ch.specchio.factories;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import ch.specchio.eav_db.SQL_StatementBuilder;
import ch.specchio.queries.EAVQueryConditionObject;
import ch.specchio.queries.Query;
import ch.specchio.queries.QueryCondition;
import ch.specchio.types.Instrument;
import ch.specchio.types.MetaDatatype;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.Picture;
import ch.specchio.types.PictureTable;
import ch.specchio.types.Sensor;
import ch.specchio.types.Spectrum;
import ch.specchio.types.SpectrumDataLink;
import ch.specchio.types.SpectrumFactorTable;


/**
 * Class for manipulating spectra stored in the database.
 */
public class SpectrumFactory extends SPECCHIOFactory {
	
	/** metadata factory for loading spectrum metadata */
	private MetadataFactory MF;
	
	
	/**
	 * Constructor. 
	 * 
	 * @param db_user		database account user name
	 * @param db_password	database account password
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public SpectrumFactory(String db_user, String db_password) throws SPECCHIOFactoryException {

		super(db_user, db_password);
		
		MF = new MetadataFactory(this);
		
	}
	
	
	/**
	 * Copy constructor. Construct a new factory that uses the same database connection
	 * as an existing factory.
	 * 
	 * @param factory	the existing factory
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public SpectrumFactory(SPECCHIOFactory factory) throws SPECCHIOFactoryException {
		
		super(factory);
		
		MF = new MetadataFactory(this);
		
	}
	
	
	
	/**
	 * Build a query string from a Query object, using only non EAV conditions
	 * 
	 * @param query		the query
	 * @param attr		the attributes to use in building this query
	 * 
	 * @return an SQL query corresponding to the input object
	 * 
	 * @throws SQLException	database error
	 */
	private String buildNonEAVQuery(Query query) throws SQLException {
		
		SQL_StatementBuilder SB = getStatementBuilder();
		
		ArrayList<String> tables = new ArrayList<String>();
		String conds = "";
		String qco_cond = "";
		boolean valid_conds_available = false;
		
		tables.add(query.getTableName()); // spectrum or frame must always be in the table list
		
		// make sure we have the primary key name for the table
		String primary_key_name = SB.get_primary_key_name(query.getTableName()); 
		
		// collect information from all standard condition fields
		for (QueryCondition cond : query.getStandardConditionFields()) {
			EAVQueryConditionObject co = (EAVQueryConditionObject)cond;
			
			tables = co.get_tablenames();
			
			if(!co.getValue().equals("0")) // this assumes that only foreign key conditions are added here, and hence, zero keys stemming from NIL selections in comboboxes are not added as conditions
			{
				qco_cond = SB.prefix(co.getTableName(), co.getFieldName()) + " " + co.getOperator() + " " + SB.quote_value(co.getValue());
				conds = SB.conc_cond(conds, qco_cond);		
				valid_conds_available = true;
			}
		}	
		
		String queryString = "";
		
		if(valid_conds_available)
		{
		
			if (conds.equals(""))
			{
				// create default condition, as otherwise all spectra would be selected due to missing restrictions
				conds = SB.prefix(query.getTableName(), primary_key_name) + " = 0";
			}
			
			// get table foreign key joins
			String table_conds = SB.get_key_joins(tables.toArray(new String[tables.size()]));
			conds = SB.conc_cond(conds, table_conds);
				
			
			if (Query.COUNT_QUERY.equals(query.getQueryType())) {
				
				// assemble count query
				queryString = SB.assemble_sql_select_query("distinct count(*)", SB.conc_tables(tables), conds);
				
			} else if (Query.SELECT_QUERY.equals(query.getQueryType())) {
				
				// work out column names
				ArrayList<String> cols = new ArrayList<String>(query.getColumns().size());
				for (String col : query.getColumns()) {
					cols.add(SB.prefix(query.getTableAlias(), col));
				}
				
				// assemble plain query
				queryString = SB.assemble_sql_select_query(
						SB.conc_cols(cols),
						SB.conc_tables(tables),
						conds,
						SB.build_order_by_string(null)
					);
					
			} else {
				
				throw new SQLException("Invalid query type.");
				
			}
		}
		
		return queryString;
		
	}	
	
	
	/**
	 * Get the number of spectra that match a given query.
	 * 
	 * @param query		the query
	 * 
	 * @return the number of spectra in the database that match the given query
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public int countIdsMatchingQuery(Query query) throws SPECCHIOFactoryException {
		
		// sanity check
		if (!query.getQueryType().equals(Query.COUNT_QUERY)) {
			throw new SPECCHIOFactoryException("Not a count query.");
		}
		
		return getSpectraMatchingQuery(query).get(0);
		
	}
	
	
	/**
	 * Delete target-reference links.
	 * 
	 * @param target_id	the target identifier
	 * 
	 * @return the number of links deleted
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public int deleteTargetReferenceLinks(int target_id) throws SPECCHIOFactoryException {
		
		int n = 0;
		
		try {
			Statement stmt = getStatementBuilder().createStatement();
			String query = "delete from spectrum_datalink_view where spectrum_id=" + Integer.toString(target_id);
			n = stmt.executeUpdate(query);
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return n;
		
	}
	
	
	/**
	 * Helper method for countIdsMatchQuery() and getIdsMatchingQuery().
	 * 
	 * @param query	the query
	 * 
	 * @return a list of identifiers for a select query, or a "list" containing one record count for a count query
	 *
	 * @throws SPECCHIOFactoryException	database error
	 */
	private ArrayList<Integer> getSpectraMatchingQuery(Query query) throws SPECCHIOFactoryException {
	
		int count = 0;
		ArrayList<Integer> ids = new ArrayList<Integer>();	
		
		try {
			SQL_StatementBuilder SB = getStatementBuilder();
			Statement stmt = getStatementBuilder().createStatement();
			
			// iterative querying
			// ==================
			Integer current_collection_table_no = 0;
			String current_current_collection_target_table = SB.prefix(getTempDatabaseName(), "spectrum_collection" + current_collection_table_no.toString());
			String current_current_collection_source_table = "";	
			
			// create temporary tables
			String primary_key_name = SB.get_primary_key_name(query.getTableName()); 
			String ddl_string = "CREATE TEMPORARY TABLE IF NOT EXISTS " +
					SB.prefix(getTempDatabaseName(), "spectrum_collection0") +
					"(" + primary_key_name + " INT NOT NULL, PRIMARY KEY (" + primary_key_name + "))";
			stmt.executeUpdate(ddl_string);
			
			ddl_string = "CREATE TEMPORARY TABLE IF NOT EXISTS " +
					SB.prefix(getTempDatabaseName(), "spectrum_collection1") +
					"(" + primary_key_name + " INT NOT NULL, PRIMARY KEY (" + primary_key_name + "))";
			stmt.executeUpdate(ddl_string);
			
			// clear temporary tables (in case they already existed)
			String delete_string = "delete from " + SB.prefix(getTempDatabaseName(), "spectrum_collection0");
			stmt.executeUpdate(delete_string);
			delete_string = "delete from " + SB.prefix(getTempDatabaseName(), "spectrum_collection1");
			stmt.executeUpdate(delete_string);			
			
			Boolean iteration_result_exists = true;

			
			// step 1: use standard field query, if there are any standard conditions at all
			if(query.getStandardConditionFields().size() > 0)
			{
				String org_query_type = query.getQueryType();
				query.setQueryType(Query.SELECT_QUERY); // we want to get ids!
				String standardQueryString = buildNonEAVQuery(query);
				
				if(!standardQueryString.equals(""))
				{
					standardQueryString = "insert into " + current_current_collection_target_table + "("+ primary_key_name + ") " + standardQueryString;
					stmt.executeUpdate(standardQueryString);
					
					// count ids for that standard query
					ResultSet rs = stmt.executeQuery("select count(*) from " + current_current_collection_target_table);
					while (rs.next()) {
					count = rs.getInt(1);
					}
					rs.close();	
				
				}
				else
				{
					iteration_result_exists = false;
				}
				
				query.setQueryType(org_query_type);
				
			}
			else
			{
				iteration_result_exists = false;
			}
			
			// step 2: EAV iterations over all conditions
			
			for (QueryCondition cond : query.getEAVConditionFields())
			{
				// switch between source and target temp tables
				current_collection_table_no = (current_collection_table_no + 1) % 2;
				current_current_collection_source_table = current_current_collection_target_table;
				current_current_collection_target_table = SB.prefix(getTempDatabaseName(), "spectrum_collection" + current_collection_table_no.toString());
				
				
				EAVQueryConditionObject co = (EAVQueryConditionObject)cond;
				
				// build query for current cond
				ArrayList<String> tables = co.get_tablenames();
				if (!tables.contains(query.getTableName())) {
					tables.add(query.getTableName());
				}
				
				String eav_table_name = "eav";
				String spectrum_x_eav_table_name = "spectrum_x_eav";
				if (!tables.contains(eav_table_name)) {
					tables.add(eav_table_name);
				}
				if (!tables.contains(spectrum_x_eav_table_name)) {
					tables.add(spectrum_x_eav_table_name);
				}
				
				
				String curr_cond;
				
				curr_cond = SB.prefix(eav_table_name, "attribute_id") + " = " + getAttributes().get_attribute_id(co.getAttributeName()) +
						" and " + SB.prefix(eav_table_name, co.getFieldName()) + " " + co.getOperator() + " " + SB.quote_string(co.getStringValue()) +
						" and " + eav_table_name + ".eav_id = " + spectrum_x_eav_table_name + ".eav_id" +
						" and " + SB.prefix(query.getTableName(), primary_key_name) + " = " + spectrum_x_eav_table_name + "." + primary_key_name
						;

				if(iteration_result_exists)
				{
					curr_cond = curr_cond + " and " + SB.prefix(query.getTableName(), primary_key_name) + " = " + current_current_collection_source_table + "." + primary_key_name;
					
					tables.add(current_current_collection_source_table);
					
				}
				
				iteration_result_exists = true; // true for all further queries
				
				String queryString = getStatementBuilder().assemble_sql_select_query(
						SB.prefix(query.getTableName(), primary_key_name),
						getStatementBuilder().conc_tables(tables),
						curr_cond);
				
				
				// clear target temp table
				delete_string = "delete from " + current_current_collection_target_table;
				stmt.executeUpdate(delete_string);				
				
				
				// put results into current target table
				queryString = "insert into " + current_current_collection_target_table + "("+ primary_key_name + ") " + queryString;
				stmt.executeUpdate(queryString);

			}
			
			if(query.getQueryType().equals(Query.COUNT_QUERY))
			{				
				ResultSet rs = stmt.executeQuery("select count(*) from " + current_current_collection_target_table);
				while (rs.next()) {
					count = rs.getInt(1);
				}
				rs.close();	
			}
			else
			{
				// build query of the final table
				StringBuffer queryBuffer = new StringBuffer();
				queryBuffer.append(
					"select " +
					SB.prefix(current_current_collection_target_table, primary_key_name) +
					" from " +
					current_current_collection_target_table
				);
				
				if (query.getOrderBy() != null) {
					// join to the attribute by which ordering will be done
					int order_by_attribute_id = getAttributes().get_attribute_id(query.getOrderBy());
					String order_by_storage_field = getAttributes().get_default_storage_field(order_by_attribute_id);
					queryBuffer.append(
						" left join (" +
							"select spectrum_x_eav.spectrum_id, eav.eav_id, eav." + order_by_storage_field + " " +
							"from spectrum_x_eav, eav " +
							"where spectrum_x_eav.eav_id = eav.eav_id " +
								"and eav.attribute_id = " + order_by_attribute_id +
						") t " +
						"on " +
							SB.prefix("t", primary_key_name) +
							"=" +
							SB.prefix(current_current_collection_target_table, primary_key_name)
					);
					
					// add "order by" clause
					queryBuffer.append(" order by " + SB.prefix("t", order_by_storage_field));
				}
				
				
				ResultSet rs = stmt.executeQuery(queryBuffer.toString());
				while (rs.next()) {
					ids.add(rs.getInt(1));
				}
				rs.close();					
			}			
			
 			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}		
		
		
		if(query.getQueryType().equals(Query.COUNT_QUERY))
		{
			ArrayList<Integer> counts = new ArrayList<Integer>(1);
			counts.add(count);
			return counts;
		}
		else
		{
			return ids;			
		}
	
	
	}
		
	/**
	 * Get the identifiers of all spectra that match a given query.
	 * 
	 * @param query		the query
	 * 
	 * @return an array of identifiers
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	public ArrayList<Integer> getIdsMatchingQuery(Query query) throws SPECCHIOFactoryException {
		
		// sanity check
		if (!query.getQueryType().equals(Query.SELECT_QUERY)) {
			throw new SPECCHIOFactoryException("Not a select query.");
		}
		
		return getSpectraMatchingQuery(query);
		
	}

	
	/**
	 * Get the pictures associated with a given spectrum.
	 * 
	 * @param spectrum_id	the spectrum identifier
	 * 
	 * @return a PictureTable object containing the picture data
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public PictureTable getPictures(int spectrum_id) throws SPECCHIOFactoryException {
		
		PictureTable pictures = new PictureTable();
		
		try {
			
			Statement stmt = getStatementBuilder().createStatement();
			String query = "select picture.picture_id,picture.caption,picture.image_data from spectrum_x_picture,picture " +
					"where picture.picture_id = spectrum_x_picture.picture_id " +
					"and spectrum_x_picture.spectrum_id = " + Integer.toString(spectrum_id);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Integer picture_id = rs.getInt(1);
				String caption = rs.getString(2);
				Blob image_data = rs.getBlob(3);
				Picture picture = new Picture(picture_id, spectrum_id, caption, image_data.getBytes(1, (int)image_data.length()));
				pictures.put(picture_id, picture);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return pictures;
		
	}

	
	/**
	 * Get a spectrum object.
	 * 
	 * @param spectrum_id		the identifier of the desired spectrum
	 * @param prepare_metadata	load the spectrum's metadata?
	 * 
	 * @return a Spectrum object representing the desired spectrum
	 * 
	 * @throws SPECCHIOFactoryException	spectrum_id does not exist
	 */
	public Spectrum getSpectrum(int spectrum_id, boolean prepare_metadata) throws SPECCHIOFactoryException {
		
		Spectrum s = new Spectrum(spectrum_id);
		
		try {
			
			SQL_StatementBuilder SQL = getStatementBuilder();
			SpectrumQueryBank SQB = new SpectrumQueryBank(SQL);
			Statement stmt = getConnection().createStatement();
			String query;
			ResultSet rs;
			
			query = "select is_reference, campaign_id, hierarchy_level_id from spectrum where spectrum_id = " + Integer.toString(spectrum_id);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				s.setIsReference(new MetaDatatype<Boolean>("Is reference", rs.getBoolean(1)));
				s.setCampaignId(rs.getInt(2));
				s.setHierarchyLevelId(rs.getInt(2));
			}
			rs.close();
			
			String id_fieldnames[] = new String[Spectrum.METADATA_FIELDS.length];
			for (int i = 0; i < Spectrum.METADATA_FIELDS.length; i++) {
				id_fieldnames[i] = Spectrum.METADATA_FIELDS[i] + "_id";
			}
			query = "select "  + SQL.conc_cols(id_fieldnames) + " from spectrum where spectrum_id = " + Integer.toString(spectrum_id);
			rs = stmt.executeQuery(query);
			int row_cnt = 1;
			while (rs.next()) {
				for (String fieldname : Spectrum.METADATA_FIELDS) {
					s.setMetadataId(fieldname, rs.getInt(fieldname + "_id"));
				}
				
			}	
			rs.close();	
			
			// get EAV
			s.setMetadata(MF.getMetadataForSpectrum(spectrum_id));	
			
			if(prepare_metadata)
			{
				// set file name and comments from metadata
				MetaParameter mp;
				mp = s.getMetadata().get_first_entry("File Name");
				if (mp != null) {
					MetaDatatype<String> mds = new MetaDatatype<String>("File Name");
					mds.set_value((String)mp.getValue());
					s.setFileName(mds);
				}
				mp = s.getMetadata().get_first_entry("File Comments");
				if (mp != null) {
					MetaDatatype<String> mds = new MetaDatatype<String>("File Comments");
					mds.set_value((String)mp.getValue());
					s.setFileComment(mds);
				}
			
				// get the capture date from date and time
				s.setCaptureDate(new MetaDatatype<Date>("Capture Date", SQL.get_java_date_time(s.getCaptureDateTime())));		
					
				// get the loading date from date and time
				s.setLoadingDate(new MetaDatatype<Date>("Loading Date", SQL.get_java_date_time(s.getLoadDateTime())));
				
				
				ArrayList<Integer> id = new ArrayList<Integer>();
				id.add(spectrum_id);
				
				// file format
				s.setFileFormat(new MetaDatatype<String>("File Format"));
				if (s.getFileFormatId() != 0)
				{
					query = SQB.get_file_format_query(id);
					rs = stmt.executeQuery(query);
					while (rs.next()) {
						s.getFileFormat().set_value(rs.getString(1));
					}
					rs.close();
				}

				
				
				// quality levels
				s.setRequiredQualityLevel(new MetaDatatype<String>("Required quality level"));
				s.setAttainedQualityLevel(new MetaDatatype<String>("Attained quality level"));
				if(s.getQualityLevelId() != 0)
				{
					query = SQB.get_quality_query(id);
					
					rs = stmt.executeQuery(query);
					
					while (rs.next()) {
						s.getAttainedQualityLevel().set_value(rs.getString(1));
					}
					rs.close();						
				}
				
				if(s.getRequiredQualityLevelId() != 0)
				{
					query = SQB.get_required_quality_query(id);
					
					rs = stmt.executeQuery(query);
					
					while (rs.next()) {
						s.getRequiredQualityLevel().set_value(rs.getString(1));
					}
					rs.close();						
				}
				
				
				// create position attributes
				s.setLatitude(new MetaDatatype<Double>("Latitude"));
				s.setLongitude(new MetaDatatype<Double>("Longitude"));
				s.setAltitude(new MetaDatatype<Double>("Altitude"));	
				s.setLocationName(new MetaDatatype<String>("Location"));
				
				if(s.getPositionId() != 0)
				{
					query = SQB.get_position_query(id);				
					rs = stmt.executeQuery(query);
					
					while (rs.next()) {
						s.getLatitude().value = rs.getDouble(1);
						s.getLongitude().value = rs.getDouble(2);
						s.getAltitude().value = rs.getDouble(3);
						s.getLocationName().value = rs.getString(4);
					}
					rs.close();						
				}
				
				
				if(s.getSensorId() != 0)
					s.setSensor(getDataCache().get_sensor(s.getSensorId()));
				else
					s.setSensor(new Sensor()); // dummy sensor
					
				
				// campaign
				s.setCampaignName(new MetaDatatype<String>("Campaign name"));
				s.setCampaignDescription(new MetaDatatype<String>("Campaign desc"));
				
				if(s.getCampaignId() != 0)
				{
					query = SQB.get_campaign_query(id);				
					rs = stmt.executeQuery(query);
					
					while (rs.next()) {
						s.getCampaignName().value = rs.getString(1);
						s.getCampaignDescription().value = rs.getString(2);
					}
					rs.close();						
				}		
				
				// landcover
				s.setLandcover(new MetaDatatype<String>("Landcover"));
				if(s.getLandcoverId() != 0)
				{
					query = SQB.get_landcover_query(id);				
					rs = stmt.executeQuery(query);
					
					while (rs.next()) {
						s.getLandcover().value = rs.getString(1);
					}
					rs.close();						
				}		
				
				// environ conds
				s.setCloudCover(new MetaDatatype<String>("Cloud cover [octas]"));
				s.setAmbientTemperature(new MetaDatatype<String>("Ambient temp. [ºC]"));
				s.setAirPressure(new MetaDatatype<String>("Air pressure"));
				s.setRelativeHumidity(new MetaDatatype<String>("Rel. humidity"));
				s.setWindDirection(new MetaDatatype<String>("Wind direction"));
				s.setWindSpeed(new MetaDatatype<String>("Wind speed"));
				if(s.getEnvironmentalConditionId() != 0)
				{
					query = SQB.get_env_cond_query(spectrum_id);				
					rs = stmt.executeQuery(query);
					row_cnt = 1;
					while (rs.next()) {				
						s.getAmbientTemperature().value = rs.getString(row_cnt++);
						s.getAirPressure().value = rs.getString(row_cnt++);
						s.getRelativeHumidity().value = rs.getString(row_cnt++);
						s.getCloudCover().value = rs.getString(row_cnt++);
						s.getWindDirection().value = rs.getString(row_cnt++);
						s.getWindSpeed().value = rs.getString(row_cnt++);
					}
					rs.close();						
				}		
				
				
				// geometry
				s.setSensorZenith(new MetaDatatype<String>("Sensor zenith"));
				s.setSensorAzimuth(new MetaDatatype<String>("Sensor azimuth"));
				s.setIlluminationZenith(new MetaDatatype<String>("Illumination zenith"));
				s.setIlluminationAzimuth(new MetaDatatype<String>("Illumination azimuth"));
				s.setSensorDistance(new MetaDatatype<String>("Sensor distance"));
				s.setIlluminationDistance(new MetaDatatype<String>("Illumination distance"));		
				if(s.getSamplingGeometryId() != 0)
				{
					query = SQB.get_geometry_query(id);				
					rs = stmt.executeQuery(query);
					row_cnt = 1;
					while (rs.next()) {
						s.getSensorZenith().value = rs.getString(row_cnt++);
						s.getSensorAzimuth().value = rs.getString(row_cnt++);
						s.getIlluminationZenith().value = rs.getString(row_cnt++);
						s.getIlluminationAzimuth().value = rs.getString(row_cnt++);
						s.getSensorDistance().value = rs.getString(row_cnt++);
						s.getIlluminationDistance().value = rs.getString(row_cnt++);
					}
					rs.close();						
				}		
				
				// measurement_unit
				s.setMeasurementUnit(new MetaDatatype<String>("Measurement unit"));		
				if(s.getMeasurementUnitId() != 0)
				{
					query = SQB.get_measurement_unit_query(id);				
					rs = stmt.executeQuery(query);
					row_cnt = 1;
					while (rs.next()) {
						s.getMeasurementUnit().value = rs.getString(row_cnt++);
					}
					rs.close();						
				}		
				
				// measurement_type
				s.setMeasurementType(new MetaDatatype<String>("Beam geometry"));		
				if(s.getMeasurementTypeId() != 0)
				{
					query = SQB.get_measurement_type_query(id);				
					rs = stmt.executeQuery(query);
					row_cnt = 1;
					while (rs.next()) {
						s.getMeasurementType().value = rs.getString(row_cnt++);
					}
					rs.close();						
				}				
		
				// illumination_source
				s.setIlluminationSource(new MetaDatatype<String>("Illumination source"));		
				if(s.getIlluminationSourceId() != 0)
				{
					query = SQB.get_illumination_source_query(id);				
					rs = stmt.executeQuery(query);
					row_cnt = 1;
					while (rs.next()) {
						s.getIlluminationSource().value = rs.getString(row_cnt++);
					}
					rs.close();						
				}				
		
				// sampling_environment
				s.setSamplingEnvironment(new MetaDatatype<String>("Sampling environment"));		
				if(s.getSamplingEnvironmentId() != 0)
				{
					query = SQB.get_sampling_environment_query(id);				
					rs = stmt.executeQuery(query);
					row_cnt = 1;
					while (rs.next()) {
						s.getSamplingEnvironment().value = rs.getString(row_cnt++);
					}
					rs.close();						
				}
				
				
				// foreoptic
				s.setForeoptic(new MetaDatatype<String>("FOV [degrees]"));		
				if(s.getForeopticId() != 0)
				{
					query = SQB.get_foreoptic_query(id);				
					rs = stmt.executeQuery(query);
					row_cnt = 1;
					while (rs.next()) {
						s.getForeoptic().value = rs.getString(row_cnt++);
					}
					rs.close();						
				}	
				
				if(s.getInstrumentId() != 0)
				{
					SpaceFactory sf = new SpaceFactory(this);
					
					space_sorting_ident_struct ssi = new space_sorting_ident_struct();
					ssi.spectrum_id = spectrum_id;
					ArrayList<space_sorting_ident_struct> ssi_list = new ArrayList<space_sorting_ident_struct>();
					ssi_list.add(ssi);
					
					ArrayList<Integer> ids = new ArrayList<Integer>();
					ids.add(spectrum_id);
					
					ssi_list = sf.getCalibrationIds(ssi_list, ids);
					
					
					s.setInstrument(getDataCache().get_instrument(s.getInstrumentId(), ssi_list.get(0).calibration_id));
				}
				else
				{
					s.setInstrument(new Instrument()); // dummy instrument, only existing to provide empty attributes
													// for reports
					s.getInstrument().setSensor(s.getSensor()); // set sensor so we can have a uniform call during the get_channels
														// when plotting
				}
			
			}
		}
		catch (SQLException ex) {
			// bad SQL
			throw new SPECCHIOFactoryException(ex);
		}
		catch (NoSuchMethodException ex) {
			// bad field name in Spectrum.METADATA_FIELDS; should never happen
			throw new SPECCHIOFactoryException(ex);
		}
			
		return s;
			
	}
	
	
	/**
	 * Get the spectrum factor table.
	 * 
	 * @param spectrum_ids		the spectrum identifiers to include in the table
	 * @param calibration_ids	the calibration identifiers to include in the table
	 * 
	 * @return a table mapping spectra to factors
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	public SpectrumFactorTable getSpectrumFactorTable(
			ArrayList<Integer> spectrum_ids,
			ArrayList<Integer> calibration_ids
		) throws SPECCHIOFactoryException {
		
		SpectrumFactorTable table = new SpectrumFactorTable();

		try {
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			String query = "select s.spectrum_id, c.cal_factors from spectrum s, calibration c, reference r where " +
					"spectrum_id in (" + SQL.conc_ids(spectrum_ids) + ") and c.reference_id = r. reference_id " +
					"and c.cal_factors in ("+ SQL.conc_ids(calibration_ids) + ")";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) 
			{
				table.put(rs.getInt(1), rs.getInt(2));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return table;
	}
	
	
	/**
	 * Get the file name for a given spectrum.
	 * 
	 * @param spectrum_id	the spectrum identifier
	 * 
	 * @return	the name of the file from which this spectrum was loaded, or null if the spectrum does not exist
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public String getSpectrumFilename(int spectrum_id) throws SPECCHIOFactoryException {
		
		String filename = null;
		
		try {
			Statement stmt = getStatementBuilder().createStatement();
			String query = "select eav.string_val from spectrum_x_eav,eav" +
					" where spectrum_x_eav.spectrum_id=" + Integer.toString(spectrum_id) +
					" and eav.eav_id=spectrum_x_eav.eav_id" +
					" and eav.attribute_id=" + Integer.toString(getAttributes().get_attribute_id("File Name"));
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				filename = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return filename;
		
	}
	
	
	/**
	 * Get the spectrum data links that refer to a given target and/or reference.
	 * 
	 * @param target_id		the identifier of the target spectrum (0 to match all targets)
	 * @param reference_id	the identifier of the reference spectrum (0 to match all references)
	 * @param is_admin		perform the query as the admin user
	 * 
	 * @return an array of identifiers of linked spectra
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public SpectrumDataLink[] getTargetReferenceLinks(int target_id, int reference_id, boolean is_admin) throws SPECCHIOFactoryException {
		
		try {
			// work out the correct table name
			String tablename = (is_admin)? "spectrum_datalink" : "spectrum_datalink_view";
			
			// create SQL-building objects
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			
			// build a query that will return all matching datalinks
			StringBuffer query = new StringBuffer();
			query.append("select spectrum_id,linked_spectrum_id, name");
			query.append(" from " + tablename + ", datalink_type ");
			query.append(" where " + SQL.prefix(tablename, "datalink_type_id") + "=" + SQL.prefix("datalink_type", "datalink_type_id"));
			if (target_id != 0) {
				query.append(" and spectrum_id=" + Integer.toString(target_id));
			}
			if (reference_id != 0) {
				query.append(" and linked_spectrum_id=" + Integer.toString(reference_id));
			}
			
			// build a list of matching datalinks
			ArrayList<SpectrumDataLink> datalinks = new ArrayList<SpectrumDataLink>();
			ResultSet rs = stmt.executeQuery(query.toString());
			while (rs.next()) {
				int referencing_spectrum_id = rs.getInt(1);
				int referenced_spectrum_id = rs.getInt(2);
				String dl_type = rs.getString(3);
				datalinks.add(new SpectrumDataLink(referencing_spectrum_id, referenced_spectrum_id, dl_type));
			}
			rs.close();
			stmt.close();
			
			// convert the list into an array
			return datalinks.toArray(new SpectrumDataLink[datalinks.size()]);
			
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
	}
	
	
	
	/**
	 * Insert links from a target to a set of references.
	 * 
	 * @param target_id		the identifier of the target node
	 * @param reference_ids	the identifiers of the reference nodes
	 * 
	 * @return the number of links created
	 * 
	 * @throws IllegalArgumentException	this target cannot be linked to any of the proposed references
	 * @throws SPECCHIOFactoryException	database error
	 */
	public int insertTargetReferenceLinks(Integer target_id, ArrayList<Integer> reference_ids) throws IllegalArgumentException, SPECCHIOFactoryException {
		
		int num = 0;
		
		try {
			// set up SQL-building objects
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			String query;
			
			// check that the request makes sense
			SpectrumDataLink links[];
			if (reference_ids.contains(target_id)) {
				// trying to link target to itself
				throw new IllegalArgumentException("Cannot link a target to itself.");
			}
			links = getTargetReferenceLinks(target_id, 0, false);
			if (links.length > 0) {
				// target already has a reference
				throw new IllegalArgumentException("Target " + links[0].getReferencingId() + " is already linked to reference " + links[0].getReferencedId());
			}
			links = getTargetReferenceLinks(0, target_id, false);
			if (links.length > 0) {
				// target is already a reference
				throw new IllegalArgumentException("The proposed target " + target_id + " is in use as a reference.");
			}
			

			// get target acquisition time for easier query formulation below
			ArrayList<Integer> time_ids = this.getEavServices().get_eav_ids(target_id, "Acquisition Time");
			MetaParameter target_time = this.getEavServices().load_metaparameter(time_ids.get(0));
			
			String target_time_str = target_time.valueAsString();

			// get corresponding reference spectrum id
			int reference_id = 0;
			int ASD_coding = 0;
			query = "select refs.spectrum_id, ASD_coding from spectrum refs " + 
					"left outer join spectrum t on t.measurement_unit_id = refs.measurement_unit_id, spectrum_x_eav sxe, eav, measurement_unit mu " +
					"where refs.spectrum_id in (" + SQL.conc_ids(reference_ids) + ")" + " and t.spectrum_id = " + target_id +
					" and refs.spectrum_id = sxe.spectrum_id and eav.eav_id = sxe.eav_id and eav.attribute_id = (select attribute_id from attribute where name = 'Acquisition Time') and mu.measurement_unit_id = t.measurement_unit_id order by abs(timeDIFF(eav.datetime_val, '" + target_time_str + "')) limit 1";
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				reference_id = rs.getInt(1);	
				ASD_coding = rs.getInt(2);
			}			
			rs.close();	
			
			// work out data link name
			String datalink_name = "";
			if(ASD_coding == 2) {
				datalink_name = "Spectralon data";
			}
			if(ASD_coding == 4) {
				datalink_name = "Cosine receptor data";
			}		
			
			// only create datalink if we found a reference and a link type
			if(reference_id != 0 && datalink_name.length() > 0)
			{
				// create a new spectrum datalink entry 
				query = "insert into spectrum_datalink_view (spectrum_id, linked_spectrum_id, datalink_type_id) " +
						"values (" + SQL.conc_ids(target_id, reference_id)  + ", " +
						"(select datalink_type_id from datalink_type where name = '" + datalink_name + "'))";
				stmt.executeUpdate(query); // execute update
				num = 1;
			}
			
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return num;
		
	}
	
	
	/**
	 * Remove a spectrum from the database.
	 * 
	 * @param spectrum_id	the identifier of the spectrum to be removed
	 * @param is_admin		is the requesting user an administator?
	 * 
	 * @throws SPECCHIOFactoryException	the spectrum could not be removed
	 */
	public void removeSpectrum(int spectrum_id, boolean is_admin) throws SPECCHIOFactoryException {
		
		try {
			
			Statement stmt = getStatementBuilder().createStatement();
			String table_name;
			String cmd;
			
			// remove datalinks
			table_name = (is_admin)? "spectrum_datalink" : "spectrum_datalink_view";
			cmd = "delete from "+table_name+" where " +
			"spectrum_id = " + Integer.toString(spectrum_id) + " OR linked_spectrum_id = " + Integer.toString(spectrum_id);	
			stmt.executeUpdate(cmd); 
			
			// EAV
			// remove entries from eav x table
			table_name = (is_admin)? "spectrum_x_eav" : "spectrum_x_eav_view";
			cmd = "delete from "+table_name+" where " +
			"spectrum_id = " + Integer.toString(spectrum_id);		
			stmt.executeUpdate(cmd); 	
			
			String spectrum_x_eav_table_or_view = table_name;
	
			// remove zombie eav
			table_name = (is_admin)? "eav" : "eav_view";
			
			cmd = "delete from "+table_name+" where eav_id not in (select eav_id from " +spectrum_x_eav_table_or_view+");";
			stmt.executeUpdate(cmd);					
			
			// remove entries from hierarchy_level_x_spectrum
			table_name = (is_admin)? "hierarchy_level_x_spectrum" : "hierarchy_level_x_spectrum_view";
			cmd = "delete from "+table_name+" where " +
			"spectrum_id = " + Integer.toString(spectrum_id);		
			stmt.executeUpdate(cmd); 				
					
			// remove spectrum itself
			table_name = (is_admin)? "spectrum" : "spectrum_view";
			cmd = "delete from "+table_name+" where spectrum_id = " + Integer.toString(spectrum_id);
			stmt.executeUpdate(cmd);
			
			stmt.close();
		}
		catch (SQLException ex) {
			// bad SQL
			throw new SPECCHIOFactoryException(ex);
		}
					
	}
	
	
	/**
	 * Update a metadata field for a given list of spectra.
	 * 
	 * @param ids		the identifiers of the spectra to be updated
	 * @param field		the field to be updated
	 * @param value		the new value for the field
	 * @param is_admin	is the user an administrator?
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database
	 */
	public void updateMetadata(Integer[] ids, String field, Integer value, boolean is_admin) throws SPECCHIOFactoryException {
		
		try {
			
			Statement stmt = getStatementBuilder().createStatement();
			String valueString = (value != 0)? value.toString() : "null";
			String query = getStatementBuilder().assemble_sql_update_query(
					field + "_id" + "=" + valueString,
					(is_admin)? "spectrum" : "spectrum_view",
					"spectrum_id in (" + getStatementBuilder().conc_ids(ids) + ")"
				);
			stmt.executeUpdate(query);
			stmt.close();
			
		} catch (SQLException ex) {
			// bad SQL
			throw new SPECCHIOFactoryException(ex);
		}
		
	}

}
