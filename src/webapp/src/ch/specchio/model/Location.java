package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Location {

	@XmlAttribute(name="dateFrom")
	private String dateFrom;
	
	@XmlAttribute(name="dateTo")
	private String dateTo;
	
	@XmlElement(name = "address")
	Address address;

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	
}
