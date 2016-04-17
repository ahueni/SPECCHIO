package ch.specchio.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;


public class Coverage {
	
	private ArrayList<Spatial> spatial;
	
	private Temporal temporal;

	@XmlElement(name="spatial")
	public ArrayList<Spatial> getSpatial() {
		return spatial;
	}

	public void setSpatial(ArrayList<Spatial> spatial) {
		this.spatial = spatial;
	}

	public Temporal getTemporal() {
		return temporal;
	}

	public void setTemporal(Temporal temporal) {
		this.temporal = temporal;
	}
	
}
