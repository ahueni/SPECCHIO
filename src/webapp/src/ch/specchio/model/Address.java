package ch.specchio.model;

import javax.xml.bind.annotation.XmlElement;

public class Address {

	@XmlElement(name = "electronic")
	private ElectronicAddress electronicAddress;
	
	@XmlElement(name = "physical")
	private PhysicalAddress physicalAddress;
	
	public ElectronicAddress getElectronicAddress() {
		return this.electronicAddress;
	}
	
	public void setElectronicAddress(ElectronicAddress electronicAddress) {
		this.electronicAddress = electronicAddress;
	}
	
	public PhysicalAddress getPhysicalAddress() {
		return this.physicalAddress;
	}
	
	public void setPhysicalAddress(PhysicalAddress physicalAddress) {
		this.physicalAddress = physicalAddress;
	}
	
}
