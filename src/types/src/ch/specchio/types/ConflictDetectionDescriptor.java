package ch.specchio.types;

import java.util.List;

import javax.xml.bind.annotation.*;


/**
 * This class encapsulates data required for the metadata conflict-detection
 * services.
 */
@XmlRootElement(name="conflict_detection_descriptor")
public class ConflictDetectionDescriptor {
	
	/** the ids to be tested */
	private Integer[] ids;
	
	/** the fields to be tested */
	private String[] md_fields;
	
	
	/**
	 * Default constructor.
	 */
	public ConflictDetectionDescriptor() {
		
		this.ids = new Integer[0];
		this.md_fields = null;
		
	}
	
	
	/**
	 * Constructor.
	 */
	public ConflictDetectionDescriptor(Integer[] ids) {
		
		this.ids = ids;
		this.md_fields = null;
		
	}
	
	
	/**
	 * Constructor.
	 */
	public ConflictDetectionDescriptor(List<Integer> ids) {
		
		this(ids.toArray(new Integer[0]));
		
	}
	
	
	@XmlElement(name="ids")
	public Integer[] getIds() { return this.ids; }
	public void setIds(Integer[] ids) { this.ids = ids; }
	
	@XmlElement(name="md_fields")
	public String[] getMetadataFields() { return this.md_fields; }
	public void setMetadataFields(String[] md_fields) { this.md_fields = md_fields; }

}
