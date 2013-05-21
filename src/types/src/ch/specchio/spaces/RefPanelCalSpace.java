package ch.specchio.spaces;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="ref_panel_cal_space")
public class RefPanelCalSpace extends SensorAndInstrumentSpace {
	
	public RefPanelCalSpace() {
		super();
		setOrderBy(null);
	}
	
	public RefPanelCalSpace(int sensor_id, MeasurementUnit measurement_unit) {
		super(sensor_id, 0, 0, measurement_unit);
		setOrderBy(null);
	}
	
	// returns true if the space definition is identical
	public boolean matches(int sensor_id, int measurement_unit_id)
	{
		if(this.sensor_id == sensor_id && this.unit.getUnitId() == measurement_unit_id)
			return true;
		else
			return false;
	}

}
