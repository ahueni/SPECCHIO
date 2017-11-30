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
	
	
	protected MetaTaxonomy(String category_name, String category_value, Object meta_value) throws MetaParameterFormatException {
		
		super(category_name, category_value, meta_value);
		setDefaultStorageField("taxonomy_id");
		
	}
	
	protected MetaTaxonomy(attribute attr, Object meta_value) throws MetaParameterFormatException
	{
		super(attr, meta_value);
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
	public boolean hasEqualValue(MetaParameter mp)
	{
		return mp.getValue().equals(getValue());
	}		
	
	
	@Override
	public void setEmptyValue() {
		
		try {
			setValue(new Long(0));
		}
		catch (MetaParameterFormatException ex) {
			// never happens
			ex.printStackTrace();
		}
		
	}
	
	
	@Override
	public void setValue(Object value) throws MetaParameterFormatException {
		
		if (value instanceof Number) {
			super.setValue(new Long(((Number)value).longValue()));
		} else if (value instanceof String) {
			throw new MetaParameterFormatException("Conversion of strings into taxonomy attributes is not supported.");
		} else {
			throw new MetaParameterFormatException("Cannot assign object of type " + value.getClass() + " to a MetaTaxonomy parameter.");
		}
		
	}
	
	
	public static boolean supportsValue(Object value) {
		
		return value != null && value instanceof Long;
		
	}

	@Override
	public String valueAsString() {
		return getValue().toString();
	}

}
