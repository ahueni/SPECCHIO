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
	
	
	protected MetaSimple(String category_name, String category_value, Object meta_value) throws MetaParameterFormatException {
		
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
		
		try {
			if (getDefaultStorageField().equals(attribute.INT_VAL)) setValue(new Integer(0));
			else if (getDefaultStorageField().equals(attribute.DOUBLE_VAL)) setValue(new Double(0));
			else if (getDefaultStorageField().equals(attribute.STRING_VAL)) setValue(new String());
			else setValue(null);
		}
		catch (MetaParameterFormatException ex) {
			// never happens
			ex.printStackTrace();
		}
	
	}
	
	
	@Override
	public void setValue(Object value) throws MetaParameterFormatException {
		
		// if the default storage field isn't already set, set it according to the the value's type
		if (getDefaultStorageField() == null) {
			if (value instanceof Integer)
				setDefaultStorageField(attribute.INT_VAL);
			else if (value instanceof Double || value instanceof Float)
				setDefaultStorageField(attribute.DOUBLE_VAL);
			else if (value instanceof String)
				setDefaultStorageField(attribute.STRING_VAL);
			else
				setDefaultStorageField(attribute.BINARY_VAL);
		}
		
		// convert the input value to a type matching the established storage field
		if (attribute.INT_VAL.equals(getDefaultStorageField())) {
			
			if (value instanceof Number) {
				super.setValue(((Number)value).intValue());
			} else if (value instanceof String) {
				try {
					super.setValue(Integer.parseInt((String)value));
				}
				catch (NumberFormatException ex) {
					throw new MetaParameterFormatException(ex);
				}
			} else {
				throw new MetaParameterFormatException("Cannot assign object of type " + value.getClass() + " to an integer parameter.");
			}
			
		} else if (attribute.DOUBLE_VAL.equals(getDefaultStorageField())) {
			
			if (value instanceof Number) {
				super.setValue(((Number)value).doubleValue());
			} else if (value instanceof String) {
				try {
					super.setValue(Double.parseDouble((String)value));
				}
				catch (NumberFormatException ex) {
					throw new MetaParameterFormatException(ex);
				}
			} else {
				throw new MetaParameterFormatException("Cannot assign object of type " + value.getClass() + " to a floating point parameter.");
			}
			
		} else if (attribute.STRING_VAL.equals(getDefaultStorageField())) {
			
			if (value instanceof String) {
				super.setValue(value);
			} else {
				super.setValue(value.toString());
			}
			
		} else if (attribute.BINARY_VAL.equals(getDefaultStorageField())) {
			
			super.setValue(value);
			
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
