package ch.specchio.types;

import java.util.Date;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.specchio.jaxb.XmlDateAdapter;


/**
 * Calibration metadata.
 */
@XmlRootElement(name="calibration_metadata")
public class CalibrationMetadata {
	
	private Date calibration_date;
	private Integer calibration_id;
	private int cal_factors_id;
	private int uncertainty_id;		
	private int calibration_no;
	private String comments;
	private CalibrationPlotsMetadata cpm_cal_fact;
	private CalibrationPlotsMetadata cpm_uncertainty;
	
	public CalibrationMetadata() {};
	public CalibrationMetadata(Integer calibration_id)
	{
		this.calibration_id = calibration_id;
	};
	
	@XmlElement(name="calibration_date")
	@XmlJavaTypeAdapter(XmlDateAdapter.class)
	public Date getCalibrationDate() { return this.calibration_date; }
	public void setCalibrationDate(Date calibration_date) { this.calibration_date = calibration_date; }
	
	@XmlElement(name="calibration_id")
	public Integer getCalibrationId() { return this.calibration_id; }
	public void setCalibrationId(Integer calibration_id) { this.calibration_id = calibration_id; }
	
	@XmlElement(name="calibration_no")
	public int getCalibrationNumber() { return this.calibration_no; }
	public void setCalibrationNumber(int calibration_no) { this.calibration_no = calibration_no; }
	
	@XmlElement(name="comments")
	public String getComments() { return this.comments; }
	public void setComments(String comments) { this.comments = comments; }
	
	@XmlElement(name="cpm_cal_fact")
	public CalibrationPlotsMetadata getCalibrationFactorsPlot() { return this.cpm_cal_fact; }
	public void setCalibrationFactorsPlot(CalibrationPlotsMetadata cpm_cal_fact) { this.cpm_cal_fact = cpm_cal_fact; }
	
	@XmlElement(name="cpm_uncertainty")
	public CalibrationPlotsMetadata getCalibrationUncertaintyPlot() { return this.cpm_uncertainty; }
	public void setCalibrationUncertaintyPlot(CalibrationPlotsMetadata cpm_uncertainty) { this.cpm_uncertainty = cpm_uncertainty; }
	public int getUncertainty_id() {
		return uncertainty_id;
	}
	public void setUncertainty_id(int uncertainty_id) {
		this.uncertainty_id = uncertainty_id;
	}
	public int getCalFactorsId() {
		return cal_factors_id;
	}
	public void setCalFactorsId(int cal_factors_id) {
		this.cal_factors_id = cal_factors_id;
	}

}
