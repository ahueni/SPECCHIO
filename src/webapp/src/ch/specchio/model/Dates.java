package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Dates {
	
	@XmlAttribute(name="type")
	private String type;
	
	private Date date;
	
	public void setType(String type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
