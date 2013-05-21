package ch.specchio.model;

import javax.xml.bind.annotation.XmlElement;

public class CollectionName {

	@XmlElement(name = "type")
	private String type;

	private String namePart;
	
	public void setType(String type) {
		this.type = type;
	}

	public String getNamePart() {
		return namePart;
	}

	public void setNamePart(String namePart) {
		this.namePart = namePart;
	}
	
}
