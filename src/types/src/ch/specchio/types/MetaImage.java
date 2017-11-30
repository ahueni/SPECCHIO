package ch.specchio.types;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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
	
	protected MetaImage(attribute attr, Object meta_value) throws MetaParameterFormatException
	{
		super(attr, meta_value);
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

	@Override
	public boolean hasEqualValue(MetaParameter mp)
	{
		return mp.getValue().equals(getValue());
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
			BufferedImage bimg = image.getImage();			
			BufferedImage bimg_resized = resizeImage(bimg, bimg.getWidth(), bimg.getHeight());			
			image.setImage(bimg_resized);
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

	/**
	 * This function resizes the image file and returns a BufferedImage object
	 * Based on: http://stackoverflow.com/questions/24745147/java-resize-image-without-losing-quality
	 */
	public static BufferedImage resizeImage(final Image image, int width, int height) {

		float ratio = width / (float) (height);

		int targeth;
		int targetw;
		
		if (width > height)
		{
			targeth = 400;
			targetw = (int) (targeth * ratio);
		}
		else
		{
			targeth = (int) (400/ratio); // maintain same resolution as for landscape pics
			targetw = (int) (targeth * ratio);			
		}

		final BufferedImage bufferedImage = new BufferedImage(targetw, targeth, BufferedImage.TYPE_INT_RGB);
		final Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setComposite(AlphaComposite.Src);
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(image, 0, 0, targetw, targeth, null);
		graphics2D.dispose();

		return bufferedImage;
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
