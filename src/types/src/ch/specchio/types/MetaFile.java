package ch.specchio.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Meta-parameter type for values that represent files.
 */
@XmlRootElement(name="meta_file")
@XmlSeeAlso({MetaDocument.class,MetaImage.class})
public abstract class MetaFile extends MetaParameter {
	
	
	/**
	 * Default constructor.
	 */
	public MetaFile() {
		
		super();
		setDefaultStorageField("binary_val");
		
	}
	
	
	/**
	 * Constructor from an attribute.
	 * 
	 * @param attr	the attribute
	 */
	protected MetaFile(attribute attr) {
		
		super(attr);
		setDefaultStorageField("binary_val");
		
	}
	
	
	/**
	 * Constructor from a category name and value
	 * 
	 * @param category_name		the category name
	 * @param category_value	the category value
	 * @param meta_value		the metaparameter value
	 * 
	 * @throws MetaParameterFormatException	meta_value cannot be assigned to a file meta-parameter
	 */
	protected MetaFile(String category_name, String category_value, Object meta_value) throws MetaParameterFormatException {
		
		super(category_name, category_value, meta_value);
		setDefaultStorageField("binary_val");
		
	}
	
	/**
	 * Constructor from attribute and value
	 * 
	 * @param attr	the attribute
	 * @param meta_value		the metaparameter value
	 * 
	 * @throws MetaParameterFormatException	meta_value cannot be assigned to a file meta-parameter
	 */
	
	protected MetaFile(attribute attr, Object meta_value) throws MetaParameterFormatException
	{
		super(attr, meta_value);
	}		
	
	
	/**
	 * Get the default filename extension for this type of file.
	 * 
	 * @return the default filename extension, including the "."
	 */
	public abstract String getDefaultFilenameExtension();
	
	
	/**
	 * Read the value of the meta-parameter from an input stream.
	 * 
	 * @param is		the input stream
	 * @param mimeType	the MIME type of the input data
	 * 
	 * @throws IOException	read error
	 */
	public abstract void readValue(InputStream is, String mimeType) throws IOException;
	
	
	/**
	 * Write the value of the meta-parameter to an output stream.
	 * 
	 * @param os	the output stream
	 * 
	 * @throws IOException	write error
	 */
	public abstract void writeValue(OutputStream os) throws IOException;

}
