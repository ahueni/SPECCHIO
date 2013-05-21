package ch.specchio.factories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ch.specchio.eav_db.SQL_StatementBuilder;


public class SpectrumQueryBank {
	
	String file_format_query, position_query, campaign_query, landcover_query, env_cond_query, geometry_query,
		measurement_unit_query, measurement_type_query, illumination_source_query, 
		sampling_environment_query, spectrum_name_query, target_type_query,
		instrument_query, picture_query, picture_id_query, quality_query, required_quality_query,
		foreoptic_query;
	
	
	SQL_StatementBuilder SQL;
	
	
	protected SpectrumQueryBank(SQL_StatementBuilder SQL)
	{
		this.SQL = SQL;		
	}
	
	
	protected String get_query(String[] attr, String[] tables)
	{
		return SQL.assemble_sql_select_query(
				SQL.conc_attributes(attr),
				SQL.conc_tables(tables),
				SQL.conc_cond(SQL.get_key_joins(tables), "spectrum.spectrum_id in "));
		
	}
	
	protected String get_query(ArrayList<String> attr, String[] tables)
	{
		return SQL.assemble_sql_select_query(
				SQL.conc_attributes(attr),
				SQL.conc_tables(tables),
				SQL.conc_cond(SQL.get_key_joins(tables), "spectrum.spectrum_id in "));
		
	}	
	
	protected String add_ids_to_query(String query, ArrayList<Integer> ids)
	{
		
		return query + "(" + SQL.conc_ids(ids) + ")";
		
	}
	
	public String get_file_format_query(ArrayList<Integer> ids)
	{
		if (file_format_query == null)
		{
			String[] tables = new String[]{"spectrum", "file_format"};
			String[] attr = new String[]{"name"};
			file_format_query = get_query(attr, tables);
		}
		return add_ids_to_query(file_format_query, ids);
	}
	
	public String get_position_query(ArrayList<Integer> ids)
	{
		if(position_query == null)
		{
			String[] tables = new String[]{"spectrum", "position"};
			String[] attr = new String[]{"latitude", "longitude", "altitude", "location_name"};
			attr = SQL.prefix("position", attr);
			position_query = get_query(attr, tables);
			
		}
		return add_ids_to_query(position_query,  ids);
	}
	
	public String get_campaign_query(ArrayList<Integer> ids)
	{
		if(campaign_query == null)
		{
			String[] tables = new String[]{"spectrum", "campaign"};
			String[] attr = new String[]{"name", "description"};
			attr = SQL.prefix("campaign", attr);
			campaign_query = get_query(attr, tables);
			
		}
		return add_ids_to_query(campaign_query,  ids);
	}
	
	public String get_landcover_query(ArrayList<Integer> ids)
	{
		if(landcover_query == null)
		{
			String[] tables = new String[]{"spectrum", "landcover"};
			String[] attr = new String[]{"cover_desc"};
			attr = SQL.prefix("landcover", attr);
			landcover_query = get_query(attr, tables);
			
		}
		return add_ids_to_query(landcover_query,  ids);
	}
	
	
	public String get_env_cond_query(int spectrum_id)
	{
		if(env_cond_query == null)
		{
			// this is a special case as it is a referenced table that uses references itself
			// thus, when one of the referencing fields is null, the standard query will fail.
			// We must therefore check that only tables and attributes are included in the query when
			// the corresponding foreign keys are not null
			
			ArrayList<String> table_list = new ArrayList<String>();
			ArrayList<String> attr_list = new ArrayList<String>();
			
			int cloud_cover_id = 0;
			int wind_speed_id= 0;
			int wind_direction_id = 0;
			
			String query = "select cloud_cover_id, wind_speed_id, wind_direction_id from environmental_condition where " +
					"environmental_condition_id in (select environmental_condition_id from spectrum where spectrum_id = " +
					Integer.toString(spectrum_id) + ")";
			try {
				Statement stmt = SQL.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				int row_cnt = 1;
				while (rs.next()) {
					cloud_cover_id = rs.getInt(row_cnt++);
					wind_speed_id = rs.getInt(row_cnt++);
					wind_direction_id = rs.getInt(row_cnt++);
				}
				rs.close();
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			table_list.add("spectrum");
			table_list.add("environmental_condition");
				
			attr_list.add(SQL.prefix("environmental_condition", "ambient_temperature"));
			attr_list.add(SQL.prefix("environmental_condition", "air_pressure"));
			attr_list.add(SQL.prefix("environmental_condition", "relative_humidity"));
					
			if(cloud_cover_id != 0)
			{
				table_list.add("cloud_cover");
				attr_list.add(SQL.prefix("cloud_cover", "cover_in_octas"));
			}
			else
			{
				attr_list.add("''");
			}
			
			if(wind_speed_id != 0)
			{
				table_list.add("wind_speed");
				attr_list.add(SQL.prefix("wind_speed", "speed"));
			}
			else
			{
				attr_list.add("''");
			}
			
			if(wind_direction_id != 0)
			{
				table_list.add("wind_direction");
				attr_list.add(SQL.prefix("wind_direction", "direction"));
			}
			else
			{
				attr_list.add("''");
			}
			
			
			String[] tables = new String[table_list.size()];
			tables = table_list.toArray(tables);
			
			
			String[] attr = new String[attr_list.size()];
			attr = attr_list.toArray(attr);
			
			
			env_cond_query = get_query(attr, tables);
			
		}
		return env_cond_query + "(" +  Integer.toString(spectrum_id) + ")";
	}
	
	public String get_geometry_query(ArrayList<Integer> ids)
	{
		if(geometry_query == null)
		{
			String[] tables = new String[]{"spectrum", "sampling_geometry"};
//			String[] attr = new String[]{"sensor_zenith", "sensor_azimuth", "illumination_zenith",
//					"illumination_azimuth", "sensor_distance", "illumination_distance", "spectrum_id"};
//			attr = SQL.prefix("sampling_geometry", attr);
//			String[] attr_ext = attr + {"spectrum_id"};
			
			ArrayList<String> attr = new ArrayList<String>();
			attr.add("sensor_zenith");
			attr.add("sensor_azimuth");
			attr.add("illumination_zenith");
			attr.add("illumination_azimuth");
			attr.add("sensor_distance");
			attr.add("illumination_distance");
			
			attr = SQL.prefix("sampling_geometry", attr);
			attr.add("spectrum_id");			
			
			geometry_query = get_query(attr, tables);
			
		}
		return add_ids_to_query(geometry_query,  ids);
	}
	
	public String get_measurement_unit_query(ArrayList<Integer> ids)
	{
		if(measurement_unit_query == null)
		{
			String[] tables = new String[]{"spectrum", "measurement_unit"};
			String[] attr = new String[]{"name"};
			attr = SQL.prefix("measurement_unit", attr);
			measurement_unit_query = get_query(attr, tables);
			
		}
		return add_ids_to_query(measurement_unit_query,  ids);
	}	
	
	public String get_measurement_type_query(ArrayList<Integer> ids)
	{
		if(measurement_type_query == null)
		{
			String[] tables = new String[]{"spectrum", "measurement_type"};
			String[] attr = new String[]{"name"};
			attr = SQL.prefix("measurement_type", attr);
			measurement_type_query = get_query(attr, tables);
			
		}
		return add_ids_to_query(measurement_type_query,  ids);
	}	
	

	public String get_illumination_source_query(ArrayList<Integer> ids)
	{
		if(illumination_source_query == null)
		{
			String[] tables = new String[]{"spectrum", "illumination_source"};
			String[] attr = new String[]{"name"};
			attr = SQL.prefix("illumination_source", attr);
			illumination_source_query = get_query(attr, tables);
			
		}
		return add_ids_to_query(illumination_source_query,  ids);
	}	
	
	public String get_sampling_environment_query(ArrayList<Integer> ids)
	{
		if(sampling_environment_query == null)
		{
			String[] tables = new String[]{"spectrum", "sampling_environment"};
			String[] attr = new String[]{"name"};
			attr = SQL.prefix("sampling_environment", attr);
			sampling_environment_query = get_query(attr, tables);
			
		}
		return add_ids_to_query(sampling_environment_query,  ids);
	}		
	

	public String get_spectrum_name_query(ArrayList<Integer> ids)
	{
		if(spectrum_name_query == null)
		{
			String[] tables = new String[]{"spectrum", "spectrum_name", 
					"spectrum_x_spectrum_name", "spectrum_name_type"};
			String[] attr = new String[]{
					SQL.prefix("spectrum_name", "name"),
					SQL.prefix("spectrum_name_type", "name")
			};
			spectrum_name_query = get_query(attr, tables);
			
		}
		return add_ids_to_query(spectrum_name_query,  ids);
	}	
	
	public String get_target_type_query(ArrayList<Integer> ids)
	{
		if(target_type_query == null)
		{
			String[] tables = new String[]{"spectrum", 
					"spectrum_x_target_type", "target_type"};
			String[] attr = new String[]{
					SQL.prefix("target_type", "name"),
					SQL.prefix("spectrum_x_target_type", "abundance"),
			};
			target_type_query = get_query(attr, tables);
			
		}
		return add_ids_to_query(target_type_query,  ids);
	}	
	
	public String get_instrument_query(ArrayList<Integer> ids)
	{
		if(instrument_query == null)
		{
			String[] tables = new String[]{"spectrum", "instrument"};
			String[] attr = new String[]{"name", "owner", "serial_number"};
			attr = SQL.prefix("instrument", attr);
			instrument_query = get_query(attr, tables);			
		}
		return add_ids_to_query(instrument_query,  ids);
	}	
	
	public String get_picture_id_query(ArrayList<Integer> ids)
	{
		if(picture_id_query == null)
		{
			String[] tables = new String[]{"spectrum", "picture", "spectrum_x_picture"};
			String[] attr = new String[]{"picture_id"};
			attr = SQL.prefix("picture", attr);
			picture_id_query = get_query(attr, tables);			
		}
		return add_ids_to_query(picture_id_query,  ids);
	}	
	
	public String get_quality_query(ArrayList<Integer> ids)
	{
		if(quality_query == null)
		{
			String[] tables = new String[]{"spectrum", "quality_level"};
			String[] attr = new String[]{"name"};
			attr = SQL.prefix("quality_level", attr);
			quality_query = SQL.assemble_sql_select_query(
					SQL.conc_attributes(attr),
					SQL.conc_tables(tables),
					SQL.conc_cond(SQL.get_key_joins(tables, "required_quality_level_id"), "spectrum.spectrum_id = "));			
		}
		return add_ids_to_query(quality_query,  ids);
	}		
	
	public String get_required_quality_query(ArrayList<Integer> ids)
	{
		if(required_quality_query == null)
		{
			String[] tables = new String[]{"spectrum", "quality_level"};
			String[] attr = new String[]{"name"};
			attr = SQL.prefix("quality_level", attr);
			required_quality_query = SQL.assemble_sql_select_query(
					SQL.conc_attributes(attr),
					SQL.conc_tables(tables),
					SQL.conc_cond(SQL.get_key_joins(tables, "quality_level_id"), "spectrum.spectrum_id = "));			
		}
		return add_ids_to_query(required_quality_query,  ids);
	}		
	
	public String get_foreoptic_query(ArrayList<Integer> ids)
	{
		if(foreoptic_query == null)
		{
			String[] tables = new String[]{"spectrum", "foreoptic"};
			String[] attr = new String[]{"degrees"};
			attr = SQL.prefix("foreoptic", attr);
			foreoptic_query = get_query(attr, tables);			
		}
		return add_ids_to_query(foreoptic_query,  ids);
	}		
	

}
