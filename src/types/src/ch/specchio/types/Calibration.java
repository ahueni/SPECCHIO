package ch.specchio.types;

import javax.xml.bind.annotation.*;


/**
 * This class represents an instrument calibration.
 */
@XmlRootElement(name="calibration")
public class Calibration {
	
	private boolean includes_uncertainty = true;
	private int reference_id;
	private SpectralFile spec_file = null;
	
	public Calibration() {}
	
	
	@XmlElement(name="includes_uncertainty")
	public boolean getIncludesUncertainty() { return this.includes_uncertainty; }
	public void setIncludesUncertainty(boolean includes_uncertainty) { this.includes_uncertainty = includes_uncertainty; }
	
	@XmlElement(name="reference_id")
	public int getReferenceId() { return this.reference_id; }
	public void setReferenceId(int reference_id) { this.reference_id = reference_id; }
	
	@XmlElement(name="spec_file")
	public SpectralFile getSpectralFile() { return this.spec_file; }
	public void setSpectralFile(SpectralFile spec_file) { this.spec_file = spec_file; }

}
