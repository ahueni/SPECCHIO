package ch.specchio.types;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Meta-parameter type for primitive types.
 */
@XmlRootElement(name="meta_simple")
public class MetaSimple extends MetaParameter {
	
	protected MetaSimple(attribute attr) {
		
		super(attr);
		
	}
	
	
	protected MetaSimple(String category_name, String category_value, Object meta_value) {
		
		super(category_name, category_value, meta_value);
		
	}
		

	protected MetaSimple() {
		
		super();
	}


	@Override
	public boolean allows_multi_insert() {
		
		return true;
		
	}
	
	
	@Override
	public void setEmptyValue() {
		
		if (getDefaultStorageField().equals("int_val")) setValue(new Integer(0));
		else if (getDefaultStorageField().equals("double_val")) setValue(new Double(0));
		else if (getDefaultStorageField().equals("string_val")) setValue(new String());
		else setValue(null);
	
	}
	
	
	@Override
	public void setValue(Object value) {

		if (value instanceof Integer) setDefaultStorageField("int_val");
		else if (value instanceof Float) setDefaultStorageField("double_val");
		else if (value instanceof Double) setDefaultStorageField("double_val");
		else if (value instanceof String) setDefaultStorageField("string_val"); 
		else setDefaultStorageField("binary_val");
		
		super.setValue(value);
		
	}
	
	@Override
	public void setValueFromString(String s) throws MetaParameterFormatException {
		
		try {
			if ("int_val".equals(getDefaultStorageField())) {
				setValue(Integer.parseInt(s));
			} else if ("double_val".equals(getDefaultStorageField())) {
				setValue(Double.parseDouble(s));
			} else if ("string_val".equals(getDefaultStorageField())) {
				setValue(s);
			} else {
				// don't know what to to with this
				throw new MetaParameterFormatException("Cannot convert a string into a binary value.");
			}
		}
		catch (NumberFormatException ex) {
			// malformed integer or double
			throw new MetaParameterFormatException(ex);
		}
		
	}

	@Override
	public String valueAsString() {
		if (getValue() != null)
			return getValue().toString();
		else
			return null;
	}

}
