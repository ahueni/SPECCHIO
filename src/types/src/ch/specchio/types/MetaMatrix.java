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
	protected MetaMatrix(String category_name, String category_value, Object meta_value) {
		super(category_name, category_value, meta_value);
		setDefaultStorageField("binary_val");
	}
	
	
	public boolean allows_multi_insert()
	{
		return false;
	}

	@Override
	public void setEmptyValue() {
		
		setValue(Matrix.factory.zeros(1,1));
		
	}
	
	
	public static boolean supportsValue(Object value)
	{
		return value != null && value instanceof Matrix;	
	}
	
	
	/**
	 * Set the value of the meta-parameter from a string. Always throws an exception
	 * because this does not make sense for matrix types.
	 * 
	 * @param s	the string
	 * 
	 * @throws MetaParameterFormatException	always thrown
	 */
	public void setValueFromString(String s) throws MetaParameterFormatException {
		
		throw new MetaParameterFormatException("Cannot convert a string into a matrix type.");
		
	}

	/** returns null: matrix values must be inserted by update statements externally */
	@Override
	public String valueAsString() {
		return null;
		
	}

}
