package ch.specchio.types;

import javax.xml.bind.annotation.*;

/**
 * Class representing a link between two spectra.
 */
@XmlRootElement(name="spectrum_data_link")
public class SpectrumDataLink {
	
	/** the identifier of the eav entry storing the link */
	private int eav_id = 0;	
	
	/** the identifier of the referencing spectrum */
	private int referencing_spectrum_id;
	
	/** the identifier of the referenced spectrum */
	private int referenced_spectrum_id;
	
	/** the data link type */
	private String dl_type;
	
	
	/**
	 * Default constructor.
	 */
	public SpectrumDataLink() {
		
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param eav_id					the identifier of the eav storing the link
	 * @param referencing_spectrum_id	the identifier of the spectrum that is making the reference
	 * @param referenced_spectrum_id	the identifier of the spectrum that is being referenced
	 * @param dl_type					the data link type
	 */
	public SpectrumDataLink(int eav_id, int referencing_spectrum_id, int referenced_spectrum_id, String dl_type) {
		
		this.eav_id = eav_id;
		this.referencing_spectrum_id = referencing_spectrum_id;
		this.referenced_spectrum_id = referenced_spectrum_id;
		this.dl_type = dl_type;
		
	}
	
	/**
	 * Constructor.
	 * 
	 * @param referencing_spectrum_id	the identifier of the spectrum that is making the reference
	 * @param referenced_spectrum_id	the identifier of the spectrum that is being referenced
	 * @param dl_type					the data link type
	 */
	public SpectrumDataLink(int referencing_spectrum_id, int referenced_spectrum_id, String dl_type) {
		
		this.referencing_spectrum_id = referencing_spectrum_id;
		this.referenced_spectrum_id = referenced_spectrum_id;
		this.dl_type = dl_type;
		
	}	
	
	
	/**
	 * Get the identifier of the eav entry storing the reference
	 * 
	 * @return the identifier of the eav that is storing the reference
	 */
	@XmlElement(name="eav_id")
	public int getEAVId() {
		
		return this.eav_id;
		
	}
	
	
	/**
	 * Set the identifier of the eav entry storing the reference
	 * 
	 * @param eav_id	the new identifier
	 */
	public void setEAVId(int eav_id) {
		
		this.eav_id = eav_id;
		
	}
		
	
	/**
	 * Get the identifier of the spectrum that is making the reference
	 * 
	 * @return the identifier of the spectrum that is making the reference
	 */
	@XmlElement(name="referencing_id")
	public int getReferencingId() {
		
		return this.referencing_spectrum_id;
		
	}
	
	
	/**
	 * Set the identifier of the spectrum that is making the reference
	 * 
	 * @param referencing_spectrum_id	the new identifier
	 */
	public void setReferencingId(int referencing_spectrum_id) {
		
		this.referencing_spectrum_id = referencing_spectrum_id;
		
	}
	
	
	/**
	 * Get the identifier of the spectrum that is being referred to
	 * 
	 * @return the identifier of the spectrum that is being referred to
	 */
	@XmlElement(name="referenced_id")
	public int getReferencedId() {
		
		return this.referenced_spectrum_id;
		
	}
	
	
	/**
	 * Set the identifier of the spectrum that is being referred to
	 * 
	 * @param referenced_spectrum_id	the new identifier
	 */
	public void setReferencedId(int referenced_spectrum_id) {
		
		this.referenced_spectrum_id = referenced_spectrum_id;
		
	}
	
	
	/**
	 * Get the link type.
	 * 
	 * @return the link type
	 */
	@XmlElement(name="link_type")
	public String getLinkType() {
		
		return this.dl_type;
		
	}
	
	
	/**
	 * Set the link type.
	 * 
	 * @param dl_type	the new type
	 */
	public void setLinkType(String dl_type) {
		
		this.dl_type = dl_type;
		
	}
		

}
