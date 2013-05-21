package ch.specchio.types;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="reference")
public class Reference {
	
	private int reference_id;
	
	private MetaDatatype<String> ref_name = new MetaDatatype<String>("Reference name");
	private MetaDatatype<String> serial_no = new MetaDatatype<String>("Serial number");
	private MetaDatatype<String> comments = new MetaDatatype<String>("Comments");
	private MetaDatatype<String> brand_name = new MetaDatatype<String>("Brand name");
	private MetaDatatype<String> type_name = new MetaDatatype<String>("Reference type");
	private MetaDatatype<String> ref_owner = new MetaDatatype<String>("Owner");
	private MetaDatatype<String> manufacturer = new MetaDatatype<String>("Manufacturer");
	
	
	public Reference() {};
	public Reference(int reference_id) { this.reference_id = reference_id; }
	
	@XmlElement(name="brand_name")
	public MetaDatatype<String> getBrandName() { return brand_name; }
	public void setBrandName(MetaDatatype<String> brand_name) { this.brand_name = brand_name; }
	public void setBrandName(String brand_name) { this.brand_name.value = brand_name; }
	
	@XmlElement(name="comments")
	public MetaDatatype<String> getComments() { return comments; }
	public void setComments(MetaDatatype<String> comments) { this.comments = comments; }
	
	@XmlElement(name="manufacturer")
	public MetaDatatype<String> getManufacturer() { return manufacturer; }
	public void setManufacturer(MetaDatatype<String> manufacturer) { this.manufacturer = manufacturer; }
	
	@XmlElement(name="reference_id")
	public int getReferenceId() { return reference_id; }
	public void setReferenceId(int reference_id) { this.reference_id = reference_id; }
	
	@XmlElement(name="ref_owner")
	public MetaDatatype<String> getReferenceOwner() { return ref_owner; }
	public void setReferenceOwner(MetaDatatype<String> ref_owner) { this.ref_owner = ref_owner; }
	public void setReferenceOwner(String ref_owner) { this.ref_owner.value = ref_owner; }
	
	@XmlElement(name="ref_name")
	public MetaDatatype<String> getReferenceName() { return ref_name; }
	public void setReferenceName(MetaDatatype<String> ref_name) { this.ref_name = ref_name; }
	public void setReferenceName(String ref_name) { this.ref_name.value = ref_name; }
	
	@XmlElement(name="serial_no")
	public MetaDatatype<String> getSerialNumber() { return serial_no; }
	public void setSerialNumber(MetaDatatype<String> serial_no) { this.serial_no = serial_no; }
	public void setSerialNumber(String serial_no) { this.serial_no.value = serial_no; }
	
	@XmlElement(name="type_name")
	public MetaDatatype<String> getTypeName() { return type_name; }
	public void setTypeName(MetaDatatype<String> type_name) { this.type_name = type_name; }

}
