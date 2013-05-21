package ch.specchio.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class Temporal {
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "date")
	private ArrayList<Date> dateList;

	public void setDateList(ArrayList<Date> dateList) {
		this.dateList = dateList;
	}

}
