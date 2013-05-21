package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Date {
	
	@XmlAttribute(name="type")
	private String type;
	
	@XmlAttribute(name="dateFormat")
	private String dateFormat;
	
	private String value;
	
	public void setType(String type) {
		this.type = type;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	@XmlValue
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
