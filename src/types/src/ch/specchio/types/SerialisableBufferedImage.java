package ch.specchio.types;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.xml.bind.annotation.*;

/**
 *  An image that can be serialised by both Java and Jersey.
 */
@XmlRootElement(name="serialisable_buffered_image")
public class SerialisableBufferedImage implements Serializable {

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	/** internal image encoding */
	private static final String INTERNAL_ENCODING = "image/jpeg";

	/** image name */
	private String        name;
	
	/** the image data */
	private byte image_data[];
	
	/** the decoded image */
	private BufferedImage image;
	 
	/**
	 * Constructor.
	 */
	public SerialisableBufferedImage() {
		
		name = "";
		image_data = null;
		image = null;
		
	}
	  
	@XmlElement(name="name")
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	@XmlElement(name="image_data")
	public byte[] getImageData() { return this.image_data; }
	public void setImageData(byte image_data[]) { this.image_data = image_data; }
	
	 
	/**
	 * Get a BufferedImage object representing this image.
	 * 
	 * @return a BufferedImage object, or null if this object has not been initialised
	 * 
	 * @throws ClassNotFoundException	no decoder found for the internal image data
	 * @throws IOException 				could not decode the image data
	 */
	@XmlTransient
	public BufferedImage getImage() throws IOException, ClassNotFoundException {
		
		if (image != null) {
			
			// the image has already been decoded
			return image;
			
		} else if (image_data != null) {
			
			// get an image reader for the internal image encoding
			ImageReader reader = getInternalImageReader();
			
			// read the image data from our instance variable
			ImageInputStream imis = new MemoryCacheImageInputStream(new ByteArrayInputStream(image_data));
			reader.setInput(imis);
			image = reader.read(0);
			
			// clean up
			imis.close();
			reader.dispose();
			
			return image;
			
		} else {
			
			// no image data
			return null;
			
		}
		
		
	}
	
	
	/**
	 * Get an image reader for the internal encoding.
	 * 
	 * @return an ImageReader object the reads images using the internal encoding
	 * 
	 * @throws ClassNotFoundException	no reader found
	 */
	@XmlTransient
	private ImageReader getInternalImageReader() throws ClassNotFoundException {
		
		ImageReader reader = null;
		
		// get the first writer that can write our internal encoding
		Iterator<ImageReader> iter = ImageIO.getImageReadersByMIMEType(INTERNAL_ENCODING);
		if (iter.hasNext()) {
			reader = iter.next();
		} else {
			throw new ClassNotFoundException("No reader found for image type " + INTERNAL_ENCODING + ".");
		}
		  
		return reader;
	}
	
	
	/**
	 * Get an image writer for the internal encoding.
	 *
	 * @return an ImageWriter object that writes images using the internal encoding, or null if no writers are available
	 * *
	 * @throws ClassNotFoundException	no writer found
	 */
	@XmlTransient
	private ImageWriter getInternalImageWriter() throws ClassNotFoundException {
		
		ImageWriter writer = null;
		
		// get the first writer that can write our internal encoding
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType(INTERNAL_ENCODING);
		if (iter.hasNext()) {
			writer = iter.next();
		} else {
			throw new ClassNotFoundException("No writer found for image type " + INTERNAL_ENCODING + ".");
		}
		  
		return writer;
	}
	
	
	/**
	 * Read an image from an input steam.
	 * 
	 * @param in		the input stream
	 * @param mimeType	the MIME type of the image
	 * 
	 * @throws ClassNotFoundException	no usable decoder/encoder found
	 * @throws IOException				input error
	 */
	public void readImage(InputStream in, String mimeType) throws ClassNotFoundException, IOException {
		
		Iterator<ImageReader> iter = ImageIO.getImageReadersByMIMEType(mimeType);
		if (iter.hasNext()) {
			
			// read the image using the first reader found
			ImageReader reader = iter.next();
			reader.setInput(ImageIO.createImageInputStream(in));
			setImage(reader.read(0));
			reader.dispose();
				
			
		} else {
			
			// unknown MIME type
			throw new ClassNotFoundException("Unknown image type: " + mimeType);
			
		}
	}

	
	/**
	 * De-serialise an image.
	 * 
	 * @param in	the input stream
	 * 
	 * @throws IOException				input error
	 * @throws ClassNotFoundException	unrecognised class in the input stream
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
	
		// read the image name
		name = (String)in.readObject();
		
		// read the image data
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int n = in.read(buf);
		while (n >= 0) {
			baos.write(buf, 0, n);
			n = in.read(buf);
		}
		image_data = baos.toByteArray();
		
		// no decoded image
		image = null;
	    
	}
	
	
	/**
	 * Set the image.
	 * 
	 * @param imageIn	the image
	 * 
	 * @throws ClassNotFoundException	no encoder found for the image data
	 * @throws IOException				output error
	 */
	public void setImage(BufferedImage imageIn) throws ClassNotFoundException, IOException {
		
		// save a reference to the unencoded image
		image = imageIn;
	
		// get an image writer for the default encoding type
		ImageWriter writer = getInternalImageWriter();
		
		// set up writing parameters
		ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.85f);
			
		// write the image data to a byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream imos = new MemoryCacheImageOutputStream(baos);
		writer.setOutput(imos);
		IIOImage iioi = new IIOImage(image, null, null);
        writer.write(null, iioi, param);
			
		// save the encoded bytes in our instance variable
		image_data = baos.toByteArray();
			
		// clean up
		writer.dispose();
		imos.close();
			
	}
	  
	
	/**
	 * Write an image to an output stream.
	 * 
	 * @param out	the output stream
	 * 
	 * @throws IOException	output error
	 */
	public void writeImage(OutputStream out) throws IOException {
		
		if (image_data != null) {
			out.write(image_data);
		}
		
	}
	 
	
	/**
	 * Serialise an image.
	 * 
	 * @param out	the output stream
	 * 
	 * @throws IOException	output error
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		
		// write the image name
		out.writeObject(name);
		
		// write the image
		out.write(image_data);
	}
	
}
