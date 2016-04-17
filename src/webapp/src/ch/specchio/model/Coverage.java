package ch.specchio.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;


public class Coverage {
	
	@XmlElement(name="spatial")
	private ArrayList<Spatial> spatial;
	
	private Temporal temporal;

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
