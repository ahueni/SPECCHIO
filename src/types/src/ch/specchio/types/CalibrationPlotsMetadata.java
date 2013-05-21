package ch.specchio.types;

import javax.xml.bind.annotation.*;

import ch.specchio.spaces.SpectralSpace;

@XmlRootElement(name="calibration_plots_metadata")
public class CalibrationPlotsMetadata {
	
	private SpectralSpace space;
	
	public CalibrationPlotsMetadata() {};
	
	@XmlElement(name="space")
	public SpectralSpace getSpace() { return this.space; }
	public void setSpace(SpectralSpace space) { this.space = space; }

}
