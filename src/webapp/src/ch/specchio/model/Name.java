package ch.specchio.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Name {
	
	@XmlAttribute(name="type")
	private String type;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "namePart")
	private ArrayList<NamePart> namePartList;

	public void setNamePartList(ArrayList<NamePart> namePartList) {
		this.namePartList = namePartList;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
}
