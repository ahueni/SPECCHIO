package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;

public class PhysicalAddress {

	@XmlAttribute(name="type")
	private String type;
	
	private AddressPart addressPart;
	
	public void setType(String type) {
		this.type = type;
	}
	public AddressPart getAddressPart() {
		return addressPart;
	}
	public void setAddressPart(AddressPart addressPart) {
		this.addressPart = addressPart;
	}
	
}
