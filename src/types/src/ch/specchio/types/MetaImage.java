package ch.specchio.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Meta-parameter class for image types.
 */
@XmlRootElement(name="meta_image")
public class MetaImage extends MetaFile {
	
	/** default constructor */
	protected MetaImage()
	{
		super();
		this.setAnnotation(""); // enforces the creation of an empty text field in the metadata editor to add annotations
	}
	
	
	/** constructor from an attribute definition */
	protected MetaImage(attribute attr)
	{
		super(attr);
		this.setAnnotation(""); // enforces the creation of an empty text field in the metadata editor to add annotations
	}
	
	
	/** constructor from category name and value */
	protected MetaImage(String category_name, String category_value, Object meta_value) throws MetaParameterFormatException
	{
		super(category_name, category_value, meta_value);
		this.setAnnotation(""); // enforces the creation of an empty text field in the metadata editor to add annotations
	}
	

	/** returns "true" to indicate that images can be inserted monolithic statements */
	@Override
	public boolean allows_multi_insert()
	{
		return true;
	}


	/** returns ".jpg" because we only write JPEG files */
	@Override
	public String getDefaultFilenameExtension()
	{
		return ".jpg";
	}
	
	
	
	/** read the meta-parameter value from an input stream */
	@Override
	public void readValue(InputStream is, String mimeType) throws IOException
	{
		try {
			SerialisableBufferedImage image = new SerialisableBufferedImage();
			image.readImage(is, mimeType);
			setValue(image);
		}
		catch (ClassNotFoundException ex) {
			// missing image decoder
			throw new IOException(ex);
		}
		catch (MetaParameterFormatException ex) {
			// never happens
			ex.printStackTrace();
		}
	}
	
	
	/** initialise the meta-parameter with an empty value */
	@Override
	public void setEmptyValue()
	{
		try {
			setValue(new SerialisableBufferedImage());
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
			throw new MetaParameterFormatException("Cannot assign object of type " + value.getClass() + " to a MetaImage parameter.");
		}
		
	}
	
	
	public static boolean supportsValue(Object value)
	{
		return value != null && value instanceof SerialisableBufferedImage;	
	}
	

	/** return the value as a string */
	@Override
	public String valueAsString() {
		return getValue().toString();
	}
	
	
	/** write the meta-parameter value to an output stream */
	@Override
	public void writeValue(OutputStream os) throws IOException
	{
		SerialisableBufferedImage image = (SerialisableBufferedImage)getValue();
		image.writeImage(os);
	}

}
