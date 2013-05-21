package ch.specchio.query_builder;


public class EAVQueryField extends QueryField{
	
	String attribute_name;


	public EAVQueryField(String name, String default_storage_field) {
		
		this.attribute_name = name;
		this.fieldname = default_storage_field;
	}

	public String get_fieldname() {

		return fieldname;
	}


	

	@Override
	public void textReport() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLabel() {
		return attribute_name;
	}



}
