package ch.specchio.metadata;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.CategoryTable;

public class MD_Spectrum_Field extends MD_Field {
	
	String db_field_name;
	String label;
	Integer id;
	CategoryTable category_values;
	
	
	public MD_Spectrum_Field(SPECCHIOClient specchio_client, String db_field_name, String label) throws SPECCHIOClientException
	{
		this.db_field_name = db_field_name;
		this.label = label;
		
		category_values = specchio_client.getMetadataCategories(db_field_name);
	}
	
	
	public CategoryTable getCategoryValues()
	{
		return category_values;
	}
	
//	public boolean matches_db_field(String field){
//		return this.db_field_name.equals(field);
//	}	
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public Integer getId()
	{
		return id;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void textReport() {
		
		System.out.println(this.db_field_name + ": id=" + id + ", value=" + category_values.get(id) + ",conflict status: " + conflict.getConflictData(0).getStatus());
		
	}


	@Override
	public String getLabelWithUnit() {
		return getLabel(); // are there any units here?
	}


	

}
