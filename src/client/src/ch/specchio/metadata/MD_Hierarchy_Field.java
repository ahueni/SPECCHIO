package ch.specchio.metadata;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;

public class MD_Hierarchy_Field extends MD_Field {

	String db_field_name;
	String label;
	String text;
	
	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public MD_Hierarchy_Field(SPECCHIOClient specchio_client, String db_field_name, String label) throws SPECCHIOClientException
	{
		this.db_field_name = db_field_name;
		this.label = label;
		
		//category_values = specchio_client.getMetadataCategoriesForIdAccess(db_field_name);
	}
	

	public String getLabel()
	{
		return label;
	}

	@Override
	public String getLabelWithUnit() {
		return label;
	}
	
}
