package ch.specchio.types;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Meta-parameter type for primitive types.
 */
@XmlRootElement(name="meta_boolean")
public class MetaBoolean extends MetaParameter {

	protected MetaBoolean() {
		// TODO Auto-generated constructor stub
	}

	protected MetaBoolean(int eav_id) {
		super(eav_id);
		// TODO Auto-generated constructor stub
	}

	protected MetaBoolean(String category_name, String category_value,
			Object meta_value) throws MetaParameterFormatException {
		super(category_name, category_value, meta_value);
		// TODO Auto-generated constructor stub
	}

	protected MetaBoolean(attribute attr, Object meta_value)
			throws MetaParameterFormatException {
		super(attr);
		this.setValue(meta_value);
	}

	public MetaBoolean(attribute attr) {
		super(attr);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean allows_multi_insert() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setEmptyValue() {
		try {
			setValue(new Boolean(false));
		} catch (MetaParameterFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public Object getEAVValue() {
		
		int x = (Boolean) super.getValue() ? 1 : 0;
		return new Integer(x);
		
	}		
	
	@Override
	public void setValue(Object value) throws MetaParameterFormatException {
		
		// if the default storage field isn't already set, set it according to the the value's type
		if (getDefaultStorageField() == null) {
			setDefaultStorageField(attribute.INT_VAL);
		}
		
		// convert the input value to a type matching the established storage field
			if (value instanceof Integer) {
				Boolean b = ((Integer) value != 0);
					super.setValue(b);

			} else if (value instanceof Boolean) {
				super.setValue(((Boolean)value));
			} else {
				throw new MetaParameterFormatException("Cannot assign object of type " + value.getClass() + " to an integer parameter.");
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
