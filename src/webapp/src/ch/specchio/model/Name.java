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
	
	public String toString() {
		
		// concatenate all name parts
		StringBuffer sb = new StringBuffer();
		for (NamePart namePart : namePartList) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(namePart.toString());
		}
		
		return sb.toString();
		
	}
	
}
