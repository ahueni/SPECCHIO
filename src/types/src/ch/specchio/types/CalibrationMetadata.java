package ch.specchio.types;

import javax.xml.bind.annotation.*;


/**
 * Calibration metadata.
 */
@XmlRootElement(name="calibration_metadata")
public class CalibrationMetadata extends Calibration {
	

	private CalibrationPlotsMetadata cpm_cal_fact;
	private CalibrationPlotsMetadata cpm_uncertainty;
	
	public CalibrationMetadata() {};
	public CalibrationMetadata(Integer calibration_id)
	{
		this.calibration_id = calibration_id;
	};
	
	@XmlElement(name="cpm_cal_fact")
	public CalibrationPlotsMetadata getCalibrationFactorsPlot() { return this.cpm_cal_fact; }
	public void setCalibrationFactorsPlot(CalibrationPlotsMetadata cpm_cal_fact) { this.cpm_cal_fact = cpm_cal_fact; }
	
	@XmlElement(name="cpm_uncertainty")
	public CalibrationPlotsMetadata getCalibrationUncertaintyPlot() { return this.cpm_uncertainty; }
	public void setCalibrationUncertaintyPlot(CalibrationPlotsMetadata cpm_uncertainty) { this.cpm_uncertainty = cpm_uncertainty; }


}
