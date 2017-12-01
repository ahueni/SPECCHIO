package ch.specchio.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;


/**
 * This class encapsulates data required for the metadata conflict-detection
 * services.
 */
@XmlRootElement(name="conflict_detection_descriptor")
public class ConflictDetectionDescriptor {
	
	/** the ids to be tested */
	private ArrayListWrapper<Integer> ids;
	
	/** the fields to be tested */
	private String[] md_fields;
	
	private int level = MetaParameter.SPECTRUM_LEVEL; // default value
	
	
	/**
	 * Default constructor.
	 */
	public ConflictDetectionDescriptor() {
		
		this.ids = new ArrayListWrapper<Integer>();
		this.md_fields = null;
		
	}
	
	
	/**
	 * Constructor.
	 */
	public ConflictDetectionDescriptor(ArrayListWrapper<Integer> ids) {
		
		this.ids = ids;
		this.md_fields = null;
		
	}
	
	
	/**
	 * Constructor.
	 */
	public ConflictDetectionDescriptor(ArrayList<Integer> ids) {
		
		this(new ArrayListWrapper<Integer>(ids));
		
	}
	
	
	@XmlElement(name="ids")
	public List<Integer> getIds() { return ids.getList(); }
	public void setIds(ArrayList<Integer> ids) { this.ids.setList(ids); }
	
	@XmlElement(name="md_fields")
	public String[] getMetadataFields() { return this.md_fields; }
	public void setMetadataFields(String[] md_fields) { this.md_fields = md_fields; }
	
	
	@XmlElement(name="level")
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}			

}
