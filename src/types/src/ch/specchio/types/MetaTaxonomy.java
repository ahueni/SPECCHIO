package ch.specchio.types;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Meta-parameter that represents a string from a taxonomy.
 */
@XmlRootElement(name="meta_taxonomy")
public class MetaTaxonomy extends MetaParameter {
	
	protected MetaTaxonomy(attribute attr) {
		
		super(attr);
		setDefaultStorageField("taxonomy_id");
		
	}
	
	
	protected MetaTaxonomy(String category_name, String category_value, Object meta_value) {
		
		super(category_name, category_value, meta_value);
		setDefaultStorageField("taxonomy_id");
		
	}
		

	protected MetaTaxonomy() {
		
		super();
		setDefaultStorageField("taxonomy_id");
	}


	@Override
	public boolean allows_multi_insert() {
		
		return true;
		
	}
	
	
	@Override
	public void setEmptyValue() {
		
		setValue(new Long(0));
		
	}
	
	
	@Override
	public void setValueFromString(String s) throws MetaParameterFormatException {
		
		throw new MetaParameterFormatException("Conversion of strings into taxonomy elements is not supported.");
		
	}
	
	
	public static boolean supportsValue(Object value) {
		
		return value != null && value instanceof Long;
		
	}

	@Override
	public String valueAsString() {
		return getValue().toString();
	}

}
