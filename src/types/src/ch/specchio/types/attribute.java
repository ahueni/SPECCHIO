package ch.specchio.types;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="attribute")
public class attribute {
	
	public static final String INT_VAL = "int_val";
	public static final String DOUBLE_VAL = "double_val";
	public static final String STRING_VAL = "string_val";
	public static final String BINARY_VAL = "binary_val";
	public static final String DATETIME_VAL = "datetime_val";
	public static final String TAXONOMY_VAL = "taxonomy_id";
	
	@XmlElement public int id;
	@XmlElement public String name;
	@XmlElement public int category_id;
	@XmlElement public String cat_name;
	@XmlElement public String cat_string_val;
	@XmlElement public int default_unit_id;
	@XmlElement public String default_storage_field;
	@XmlElement public String description;
	
	public String getName()
	{
		return name;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getDefaultStorageField() {
		
		return default_storage_field;
		
	}

}
