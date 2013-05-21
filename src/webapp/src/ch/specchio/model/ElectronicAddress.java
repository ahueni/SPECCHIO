package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;

public class ElectronicAddress {
	
	@XmlAttribute(name="type")
	private String type;
	
	private String value;
	
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
