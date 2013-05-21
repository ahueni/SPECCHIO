package ch.specchio.types;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="attribute")
public class attribute {
	
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
