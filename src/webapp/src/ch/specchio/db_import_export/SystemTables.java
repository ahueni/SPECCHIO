package ch.specchio.db_import_export;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ch.specchio.eav_db.SQL_StatementBuilder;


public class SystemTables {
	
	private SQL_StatementBuilder SQL;
	private String schema;
	static ArrayList<String> system_tables = new ArrayList<String>();
	static ArrayList<String> system_table_end_nodes = new ArrayList<String>();
	
	protected SystemTables(SQL_StatementBuilder SQL, String schema)
	{
		this.SQL = SQL;
		this.schema = schema;
		
		define_system_tables();
		define_system_table_end_nodes();
	}
	
	
	protected void define_system_tables()
	{		
		Statement stmt;
		
		// get the referenced table name
		try {
			stmt = SQL.createStatement();
			
			String query;
			ResultSet rs;	
	
			query = "SELECT table_name FROM information_schema.`TABLES` T " +
					"where table_schema = '" + this.schema + "' and table_type = 'BASE TABLE' " +
					"and table_name not in " +
					"(SELECT table_name FROM information_schema.`COLUMNS` C " +
					"where table_schema = '" + this.schema + "' and (column_name = 'user_id' or column_name = 'campaign_id') and column_key = 'MUL')";
			
			rs = stmt.executeQuery(query);
		
			while (rs.next()) {	
				system_tables.add(rs.getString(1));
			}
			
			rs.close();
			stmt.close();
			
			 // exceptions to the above rule
			system_tables.remove("research_group");
			system_tables.remove("research_group_members");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
	}
	
	protected void define_system_table_end_nodes()
	{		
		Statement stmt;
		
		// get the referenced table name
		try {
			stmt = SQL.createStatement();
			
			String query;
			ResultSet rs;	
	
			query = "SELECT table_name FROM information_schema.`TABLES` T " +
					"where table_schema = '" + this.schema + "' " +
							"and table_type = 'BASE TABLE' and table_name not in " +
							"(SELECT table_name FROM information_schema.`COLUMNS` C " +
							"where table_schema = '" + this.schema + "' " +
									"and column_name = 'user_id' and column_key = 'MUL') " +
									"and table_name not in (SELECT distinct table_name FROM information_schema.`COLUMNS` C " +
									"where table_schema = '" + this.schema + "' and column_key = 'MUL' group by table_name)";
			
			rs = stmt.executeQuery(query);
		
			while (rs.next()) {	
				system_table_end_nodes.add(rs.getString(1));
			}
			
			rs.close();
			stmt.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
	}
	
	public boolean isSystemTable(String tablename)
	{
		if(system_tables.contains(tablename))
		{
			return true;
		}
		else
			return false;
	}
	
	public boolean isEndNodeSystemTable(String tablename)
	{
		if(system_table_end_nodes.contains(tablename))
		{
			return true;
		}
		else
			return false;
	}	
	
	public boolean isIRefException(String table, String referencing_table)
	{
		boolean iref_exc = false;
		
//		System.out.println("refexcep" + table + " " + referencing_table);
		
		if(table.equals("institute"))
			iref_exc = true;
		
		if(table.equals("research_group"))
			iref_exc = true;	
		
		if(table.equals("eav"))
			iref_exc = true;		
		
		
		if(table.equals("sensor") && (referencing_table.equals("foreoptic") || referencing_table.equals("instrument") || referencing_table.equals("instrumentation_factors")))
			iref_exc = true;
		
		if(table.equals("reference_brand"))
			iref_exc = true;

		
		// these following tables are just included to speed things up .....
		if(table.equals("picture"))
			iref_exc = true;
		
		if(table.equals("spectrum_x_eav"))
			iref_exc = true;
		
		if(table.equals("campaign") && (referencing_table.equals("eav")))
			iref_exc = true;			
		
		if(table.equals("campaign") && (referencing_table.equals("spectrum_x_eav")))
			iref_exc = true;	
		
		
		if(table.equals("campaign") && (referencing_table.equals("hierarchy_level_x_spectrum")))
			iref_exc = true;			
		
// wrong: datalink iref is needed, otherwise the linked spectrum is not available during insert!!!!!	
		// bullcrap, there is foreign keys anyway, which are resolved ...
		if(table.equals("spectrum_datalink"))
			iref_exc = true;
		
		if(table.equals("environmental_condition"))
			iref_exc = true;
		
		if(table.equals("sampling_geometry"))
			iref_exc = true;		
		
//		if(table.equals("attribute"))
//			iref_exc = true;		
//		
//		if(table.equals("attribute") && (referencing_table.equals("taxonomy")))
//			iref_exc = false;						
		
		return iref_exc;
	}

}
