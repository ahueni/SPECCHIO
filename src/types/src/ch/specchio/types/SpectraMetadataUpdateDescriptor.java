package ch.specchio.types;

import java.util.List;

import javax.xml.bind.annotation.*;


/**
 * This class describes an update to spectrum metadata, for use with the spectrum services.
 */
@XmlRootElement(name="spectrum_metadata_update")
public class SpectraMetadataUpdateDescriptor {
	
	/** the identifiers of the objects to be updated */
	private Integer[] ids;
	
	/** the name of the field to be updated */
	private String field;
	
	/** the new value for the field */
	private Integer value;
	
	
	/**
	 * Default constructor.
	 */
	public SpectraMetadataUpdateDescriptor() {
		
		this.ids = new Integer[0];
		this.field = null;
		this.value = 0;
		
	}
	
	
	/**
	 * Constructor.
	 */
	public SpectraMetadataUpdateDescriptor(Integer[] ids, String field, Integer value) {
		
		this.ids = ids;
		this.field = field;
		this.value = value;
		
	}
	
	
	/**
	 * Constructor.
	 */
	public SpectraMetadataUpdateDescriptor(List<Integer> ids, String field, Integer value) {
		
		this(ids.toArray(new Integer[0]), field, value);
		
	}
		
	
	@XmlElement(name="ids")
	public Integer[] getIds() { return this.ids; }
	public void setIds(Integer[] ids) { this.ids = ids; }
	
	@XmlElement(name="field")
	public String getField() { return this.field; }
	public void setField(String field) { this.field = field; }
	
	@XmlElement(name="value")
	public Integer getValue() { return this.value; }
	public void setValue(Integer value) { this.value = value; }

}
