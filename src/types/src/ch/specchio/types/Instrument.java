package ch.specchio.types;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="instrument")
public class Instrument {
	
	private Sensor sensor;
	
	private int instrument_id;
	private int calibration_id = 0;
	private int sensor_id;
	
	private double[] avg_wavelengths = null; // filled for calibrated instruments only, otherwise the sensor channels are used
	
	private MetaDatatype<String> instr_name;
	private MetaDatatype<String> instr_owner;
	private MetaDatatype<String> instr_no;
	
	private boolean is_valid; // can be invalid if it is a dummy (i.e. not loaded from db)
	
	public Instrument()
	{
		instr_name = new MetaDatatype<String>("Instrument name");
		instr_owner = new MetaDatatype<String>("Instrument owner");
		instr_no = new MetaDatatype<String>("Instrument number");
		
		is_valid = false;
	}
	
	public Instrument(int instrument_id)
	{
		this();
		
		this.instrument_id = instrument_id;
	}

	
	public int getNoOfBands()
	{
		return sensor.getNumberOfChannels().get_value();
	}

	// avg_wavelengths filled for calibrated instruments only, otherwise the sensor channels are used
	public double[] getCentreWavelengths()
	{
		if(avg_wavelengths != null)
			return avg_wavelengths;
		else
			return sensor.getAverageWavelengths();
	}
	
	public short[] getChannelTypes()
	{
		return this.sensor.getElementTypes();
	}
	
	@XmlElement(name="avg_wavelengths")
	public double[] getAverageWavelengths() { return this.avg_wavelengths; }
	public void setAverageWavelengths(double[] avg_wavelengths) { this.avg_wavelengths = avg_wavelengths; }
	public double getAverageWavelength(int i) { return this.avg_wavelengths[i]; }
	public void setAverageWavelength(int i, double avg_wavelength) { this.avg_wavelengths[i] = avg_wavelength; }
	
	@XmlElement(name="calibration_id")
	public int getCalibrationId() { return this.calibration_id; }
	public void setCalibrationId(int calibration_id) { this.calibration_id = calibration_id; }
	
	@XmlElement(name="instrument_id")
	public int getInstrumentId() { return this.instrument_id; }
	public void setInstrumentId(int instrument_id) { this.instrument_id = instrument_id; }
	
	@XmlElement(name="instr_name")
	public MetaDatatype<String> getInstrumentName() { return this.instr_name; }
	public void setInstrumentName(MetaDatatype<String> instr_name) { this.instr_name = instr_name; }
	public void setInstrumentName(String instr_name) { this.instr_name.value = instr_name; }
	
	@XmlElement(name="instr_no")
	public MetaDatatype<String> getInstrumentNumber() { return this.instr_no; }
	public void setInstrumentNumber(MetaDatatype<String> instr_no) { this.instr_no = instr_no; }
	public void setInstrumentNumber(String instr_no) { this.instr_no.value = instr_no; }
	
	@XmlElement(name="instr_owner")
	public MetaDatatype<String> getInstrumentOwner() { return this.instr_owner; }
	public void setInstrumentOwner(MetaDatatype<String> instr_owner) { this.instr_owner = instr_owner; }
	public void setInstrumentOwner(String instr_owner) { this.instr_owner.value = instr_owner; }
	
	@XmlElement(name="is_valid")
	public boolean getValid() { return this.is_valid; }
	public void setValid(boolean is_valid) { this.is_valid = is_valid; }
	
	@XmlElement(name="sensor")
	public Sensor getSensor() { return this.sensor; }
	public void setSensor(Sensor sensor) { this.sensor = sensor; this.sensor_id = (sensor != null)? sensor.getSensorId() : 0; }
	
	@XmlElement(name="sensor_id")
	public int getSensorId() { return this.sensor_id; }
	public void setSensorId(int sensor_id) { this.sensor_id = sensor_id; }
	
	
	public String toString() {
		
		return instr_name.value;
		
	}

}
