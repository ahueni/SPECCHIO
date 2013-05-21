package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;


public class AddressPart {
	
	@XmlAttribute(name="type")
	private String type;
	
	private String value;
	
	public void setType(String type) {
		this.type = type;
	}
	@XmlValue
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
