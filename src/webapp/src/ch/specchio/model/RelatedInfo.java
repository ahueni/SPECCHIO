package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;

public class RelatedInfo {
	
	@XmlAttribute(name="type")
	private String type;

	private Identifier identifier;

	public Identifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
