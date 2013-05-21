package ch.specchio.model;

import javax.xml.bind.annotation.XmlElement;

public class Address {

	@XmlElement(name = "electronic")
	private ElectronicAddress electronicAddress;
	
	@XmlElement(name = "physical")
	private PhysicalAddress physicalAddress;
	
	public void setElectronicAddress(ElectronicAddress electronicAddress) {
		this.electronicAddress = electronicAddress;
	}
	public void setPhysicalAddress(PhysicalAddress physicalAddress) {
		this.physicalAddress = physicalAddress;
	}
	
}
