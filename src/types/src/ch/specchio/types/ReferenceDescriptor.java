package ch.specchio.types;

import javax.xml.bind.annotation.*;


/**
 * Class that identifies a reference.
 */
@XmlRootElement(name="reference_descriptor")
public class ReferenceDescriptor {

	private int reference_id;
	private String reference_name;
	
	public ReferenceDescriptor() {};
	public ReferenceDescriptor(int reference_id, String reference_name) {
		this.reference_id = reference_id;
		this.reference_name = reference_name;
	}
	
	@XmlElement(name="reference_id")
	public int getReferenceId() { return this.reference_id; }
	public void setReferenceId(int reference_id) { this.reference_id = reference_id; }
	
	@XmlElement(name="reference_name")
	public String getReferenceName() { return this.reference_name; }
	public void setReferenceName(String reference_name) { this.reference_name = reference_name; }

}
