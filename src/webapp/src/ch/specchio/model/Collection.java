package ch.specchio.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "identifierList", "dates", "name", "location", "relatedObjectList", "subjectList", "description", "coverage", "relatedInfoList", "rights", "citationInfo" })
public class Collection {
	
	@XmlAttribute(name="type")
	private String type;

	@XmlAttribute(name="dateModified")
	private String dateModified;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "identifier")
	private ArrayList<Identifier> identifierList;
	
	private Dates dates;
	
	private Name name;
	
	private Location location;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "relatedObject")
	private ArrayList<RelatedObject> relatedObjectList;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "subject")
	private ArrayList<Subject> subjectList;
	
	private Description description;
	
	private Coverage coverage;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "relatedInfo")
	private ArrayList<RelatedInfo> relatedInfoList;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "rights")
	private ArrayList<Rights> rights;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "citationInfo")
	private ArrayList<CitationInfo> citationInfo;

	public void setType(String type) {
		this.type = type;
	}

	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	public void setIdentifierList(ArrayList<Identifier> identifierList) {
		this.identifierList = identifierList;
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
	
	public Description getDescription() {
		return description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

	public Coverage getCoverage() {
		return coverage;
	}

	public void setCoverage(Coverage coverage) {
		this.coverage = coverage;
	}

	public void setRelatedObjectList(ArrayList<RelatedObject> relatedObjectList) {
		this.relatedObjectList = relatedObjectList;
	}
	
	public ArrayList<Subject> getSubjectList() {
		return this.subjectList;
	}

	public void setSubjectList(ArrayList<Subject> subjectList) {
		this.subjectList = subjectList;
	}
	
	public ArrayList<RelatedInfo> getRelatedInfoList() {
		return this.relatedInfoList;
	}

	public void setRelatedInfoList(ArrayList<RelatedInfo> relatedInfoList) {
		this.relatedInfoList = relatedInfoList;
	}

	public ArrayList<Rights> getRights() {
		return rights;
	}

	public void setRights(ArrayList<Rights> rights) {
		this.rights = rights;
	}

	public ArrayList<CitationInfo> getCitationInfo() {
		return citationInfo;
	}

	public void setCitationInfo(ArrayList<CitationInfo> citationInfo) {
		this.citationInfo = citationInfo;
	}

	public Dates getDates() {
		return dates;
	}

	public void setDates(Dates dates) {
		this.dates = dates;
	}

}
