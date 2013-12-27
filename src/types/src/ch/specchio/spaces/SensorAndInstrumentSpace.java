package ch.specchio.spaces;

import javax.xml.bind.annotation.*;

import ch.specchio.types.Instrument;
import ch.specchio.types.Sensor;

@XmlRootElement(name="sensor_and_instrument_space")
public class SensorAndInstrumentSpace extends SpectralSpace {
	
	int instrument_id;
	int sensor_id;
	int calibration_id;

//	String instr_name = "";
//	String sensor_name = "";
//	String calib_no = "";
	
	Sensor sensor;
	Instrument instrument;
	
	public SensorAndInstrumentSpace()
	{
		super();
		this.SpaceTypeName = "Sensor/Instrument Space";
	}
	
	public SensorAndInstrumentSpace(int sensor_id, int instrument_id, int calibration_id, MeasurementUnit measurement_unit)
	{
		super(measurement_unit);
		this.sensor_id = sensor_id;
		this.instrument_id = instrument_id;
		this.calibration_id = calibration_id;
	}
	
	
	@XmlElement(name="instrument_id")
	public int getInstrumentId() { return this.instrument_id; }
	public void setInstrumentId(int instrument_id) { this.instrument_id = instrument_id; }
	
	@XmlElement(name="sensor_id")
	public int getSensorid() { return this.sensor_id; }
	public void setSensorId(int sensor_id) { this.sensor_id = sensor_id; }
	
	@XmlElement(name="calibration_id")
	public int getCalibrationId() { return this.calibration_id; }
	public void setCalibrationId(int calibration_id) { this.calibration_id = calibration_id; }
	
//	@XmlElement(name="instr_name")
//	public String getInstrumentName() { return this.instr_name; }
//	public void setInstrumentName(String instr_name) { this.instr_name = instr_name; }
	
//	@XmlElement(name="sensor_name")
//	public String getSensorName() { return this.sensor_name; }
//	public void seSensorName(String sensor_name) { this.sensor_name = sensor_name; }
	
//	@XmlElement(name="calib_no")
//	public String getCalibrationNumber() { return this.calib_no; }
//	public void setCalibrationNumber(String calib_no) { this.calib_no = calib_no; }
	
	@XmlElement(name="sensor")
	public Sensor getSensor() { return this.sensor; }
	public void setSensor(Sensor sensor) { this.sensor = sensor; }
	
	@XmlElement(name="instrument")
	public Instrument getInstrument() { return this.instrument; }
	public void setInstrument(Instrument instrument) { this.instrument = instrument; }
	
	// returns true if the space definition is identical
	public boolean matches(int instrument_id, int sensor_id, int calibration_id, int measurement_unit_id)
	{
		if(this.instrument_id == instrument_id && this.sensor_id == sensor_id && this.calibration_id == calibration_id && this.unit.getUnitId() == measurement_unit_id)
			return true;
		else
			return false;
	}
	
	// returns true if the space definition is identical
	public boolean matches(int sensor_id, int measurement_unit_id)
	{
		if(this.sensor_id == sensor_id && this.unit.getUnitId() == measurement_unit_id)
			return true;
		else
			return false;
	}	
	
	public boolean matches(int sensor_id)
	{
		if(this.sensor_id == sensor_id)
			return true;
		else
			return false;
	}	
	
	
	// used to build output filenames in the FileOutputManager
	public String get_filename_addon()
	{
		return instrument.getInstrumentName() + "_" + sensor.getName();
	}

}
