package ch.specchio.model;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "name", "description", "location", "relatedObject", "subjectList",  "relatedInfoList" })
public class Party {

	@XmlAttribute(name="type")
	private String type;
	
	@XmlAttribute(name="dateModified")
	private Date dateModified;
	
	private Name name;
	
	private Location location;
	
	private RelatedObject relatedObject;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "subject")
	private ArrayList<Subject> subjectList;
	
	private Description description;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "relatedInfo")
	private ArrayList<RelatedInfo> relatedInfoList;
	
	public void setType(String type) {
		this.type = type;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	public Name getName() {
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public RelatedObject getRelatedObject() {
		return relatedObject;
	}
	public void setRelatedObject(RelatedObject relatedObject) {
		this.relatedObject = relatedObject;
	}
	public void setSubjectList(ArrayList<Subject> subjectList) {
		this.subjectList = subjectList;
	}
	public Description getDescription() {
		return description;
	}
	public void setDescription(Description description) {
		this.description = description;
	}
	public void setRelatedInfoList(ArrayList<RelatedInfo> relatedInfoList) {
		this.relatedInfoList = relatedInfoList;
	}

}
