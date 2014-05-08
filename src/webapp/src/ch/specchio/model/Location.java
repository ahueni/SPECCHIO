package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Location {

	@XmlAttribute(name="dateFrom")
	private String dateFrom;
	
	@XmlAttribute(name="dateTo")
	private String dateTo;
	
	private Address address;


	@XmlElement(name = "address")
	public Address getAddress() {
		return this.address;
	}
	
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
