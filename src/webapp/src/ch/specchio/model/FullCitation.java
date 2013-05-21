package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class FullCitation {
	
	@XmlAttribute(name="style")
	private String style;
	
	private String value;

	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
}
