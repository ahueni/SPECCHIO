package ch.specchio.types;

public class EAVTableAndRelationsInfoStructure {

		public String primary_x_eav_tablename = "";
		public String primary_x_eav_viewname = "";
		public String primary_id_name = "";	
		public String primary_table_name = "";

		public EAVTableAndRelationsInfoStructure() {};
		
		public EAVTableAndRelationsInfoStructure(EAVTableAndRelationsInfoStructure info) 
		{
			this.primary_id_name = info.primary_id_name;
			this.primary_table_name = info.primary_table_name;
			this.primary_x_eav_tablename = info.primary_x_eav_tablename;
			this.primary_x_eav_viewname = info.primary_x_eav_viewname;	
		}
		
}
