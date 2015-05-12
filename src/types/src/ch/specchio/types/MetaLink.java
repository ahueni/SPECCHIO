package ch.specchio.types;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * Meta-parameter type for primitive types.
 */
@XmlRootElement(name="meta_link")
public class MetaLink extends MetaParameter {
	
	/** default constructor */
	public MetaLink()
	{
		super();
		setDefaultStorageField("spectrum_val");
	}
	
	
	/** constructor from an attribute definition */
	public MetaLink(attribute attr)
	{
		super(attr);
		setDefaultStorageField("spectrum_val");
	}

	
	/** constructor from a category name and value */
	protected MetaLink(String category_name, String category_value, Object meta_value) throws MetaParameterFormatException {
		super(category_name, category_value, meta_value);
		setDefaultStorageField("spectrum_val");
	}
	
	/**
	 * Constructor from attribute and value
	 * 
	 * @param attr	the attribute
	 * @param meta_value		the metaparameter value
	 * 
	 * @throws MetaParameterFormatException	meta_value cannot be assigned to a file meta-parameter
	 */
	
	protected MetaLink(attribute attr, Object meta_value) throws MetaParameterFormatException
	{
		super(attr, meta_value);
	}					

	@Override
	public boolean allows_multi_insert() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEmptyValue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String valueAsString() {
		if (getValue() != null)
			return getValue().toString();
		else
			return null;
	}

	public static boolean supportsValue(Object value)
	{
		return value != null && value instanceof Long;	
	}	
	
}
