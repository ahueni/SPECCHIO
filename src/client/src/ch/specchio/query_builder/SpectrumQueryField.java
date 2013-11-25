package ch.specchio.query_builder;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.CategoryTable;

public class SpectrumQueryField extends QueryField{
	
	String label;
	Integer id;
	CategoryTable category_values;

	public SpectrumQueryField(SPECCHIOClient specchio_client, String db_field_name, String label) throws SPECCHIOClientException
	{
		this.fieldname = db_field_name;
		this.label = label;
		
		category_values = specchio_client.getMetadataCategoriesForIdAccess(db_field_name);
	
	}
	
	public CategoryTable getCategoryValues()
	{
		return category_values;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public Integer getId()
	{
		return id;
	}	

	@Override
	public void textReport() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLabel() {
		return label;
	}



}
