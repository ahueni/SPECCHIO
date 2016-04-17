package ch.specchio.model;

import javax.xml.bind.annotation.XmlElement;

public class Address {

	private ElectronicAddress electronicAddress;
	
	private PhysicalAddress physicalAddress;

	@XmlElement(name = "electronic")
	public ElectronicAddress getElectronicAddress() {
		return this.electronicAddress;
	}
	
	public void setElectronicAddress(ElectronicAddress electronicAddress) {
		this.electronicAddress = electronicAddress;
	}

	@XmlElement(name = "physical")
	public PhysicalAddress getPhysicalAddress() {
		return this.physicalAddress;
	}
	
	public void setPhysicalAddress(PhysicalAddress physicalAddress) {
		this.physicalAddress = physicalAddress;
	}
	
}
