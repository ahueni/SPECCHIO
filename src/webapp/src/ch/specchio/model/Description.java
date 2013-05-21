package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Description {
	
	@XmlAttribute(name="type")
	private String type;
	
	private String value;

	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
