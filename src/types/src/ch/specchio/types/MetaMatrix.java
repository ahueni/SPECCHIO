package ch.specchio.types;

import javax.xml.bind.annotation.XmlRootElement;

import org.ujmp.core.Matrix;


/**
 * Meta-parameter class for matrix types.
 */
@XmlRootElement(name="meta_matrix")
public class MetaMatrix extends MetaParameter {
	
	/** default constructor */
	public MetaMatrix()
	{
		super();
		setDefaultStorageField("binary_val");
	}
	
	
	/** constructor from an attribute definition */
	public MetaMatrix(attribute attr)
	{
		super(attr);
		setDefaultStorageField("binary_val");
	}

	
	/** constructor from a category name and value */
	protected MetaMatrix(String category_name, String category_value, Object meta_value) throws MetaParameterFormatException {
		super(category_name, category_value, meta_value);
		setDefaultStorageField("binary_val");
	}
	
	
	public boolean allows_multi_insert()
	{
		return false;
	}

	@Override
	public void setEmptyValue() {
		
		try {
			setValue(Matrix.factory.zeros(1,1));
		}
		catch (MetaParameterFormatException ex) {
			// never happens
			ex.printStackTrace();
		}
		
	}
	
	
	public void setValue(Object value) throws MetaParameterFormatException {
		
		if (supportsValue(value)) {
			super.setValue(value);
		} else {
			throw new MetaParameterFormatException("Cannot assign object of type " + value.getClass() + " to a MetaMatrix parameter.");
		}
		
	}
	
	
	public static boolean supportsValue(Object value)
	{
		return value != null && value instanceof Matrix;	
	}

	/** returns null: matrix values must be inserted by update statements externally */
	@Override
	public String valueAsString() {
		return null;
		
	}

}
