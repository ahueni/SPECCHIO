package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Relation {
	
	@XmlAttribute
	private String type;
	
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
