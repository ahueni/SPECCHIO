package ch.specchio.types;

import javax.xml.bind.annotation.*;


/**
 * This class represents a picture.
 */
@XmlRootElement(name="picture")
public class Picture {
	
	/** picture identifier */
	private int picture_id;
	
	/** identifier of the object with the picture is associated */
	private int object_id;
	
	/** caption */
	private String caption;
	
	/** raw image data */
	private byte[] image_data;
	
	/** default constructor */
	public Picture() {}
	
	/** constructor */
	public Picture(int picture_id, int object_id, String caption, byte[] image_data)
	{
		setPictureId(picture_id);
		setObjectId(object_id);
		setCaption(caption);
		setImageData(image_data);
	}
	
	@XmlElement(name="caption")
	public String getCaption() { return this.caption; }
	public void setCaption(String caption) { this.caption = caption; }
	
	@XmlElement(name="image_data")
	public byte[] getImageData() { return this.image_data; }
	public void setImageData(byte[] image_data) { this.image_data = image_data; }
	
	@XmlElement(name="object_id")
	public int getObjectId() { return this.object_id; }
	public void setObjectId(int object_id) { this.object_id = object_id; }
	
	@XmlElement(name="picture_id")
	public int getPictureId() { return this.picture_id; }
	public void setPictureId(int picture_id) { this.picture_id = picture_id; }

}
