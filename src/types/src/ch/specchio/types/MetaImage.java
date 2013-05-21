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
	}
	
	
	/** constructor from an attribute definition */
	protected MetaImage(attribute attr)
	{
		super(attr);
	}
	
	
	/** constructor from category name and value */
	protected MetaImage(String category_name, String category_value, Object meta_value)
	{
		super(category_name, category_value, meta_value);
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
	}
	
	
	/** initialise the meta-parameter with an empty value */
	@Override
	public void setEmptyValue()
	{
		setValue(new SerialisableBufferedImage());
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
