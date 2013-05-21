package ch.specchio.types;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="reference_brand")
public class ReferenceBrand {
	
	private int reference_brand_id;
	private int reference_type_id;
	private int manufacturer_id;
	private String name;
	
	public ReferenceBrand() {};
	
	public ReferenceBrand(int reference_brand_id, int reference_type_id, int manufacturer_id, String name)
	{
		this.reference_brand_id = reference_brand_id;
		this.reference_type_id = reference_type_id;
		this.manufacturer_id = manufacturer_id;
		this.name = name;
	}

	@XmlElement(name="reference_brand_id")
	public int getReferenceBrandId() { return this.reference_brand_id; }
	public void setReferenceBrandId(int reference_brand_id) { this.reference_brand_id = reference_brand_id; }
	
	@XmlElement(name="reference_type_id")
	public int getReferenceTypeId() { return this.reference_type_id; }
	public void setReferenceTypeId(int reference_type_id) { this.reference_type_id = reference_type_id; }
	
	@XmlElement(name="manufacturer_id")
	public int getManufacturerId() { return this.manufacturer_id; }
	public void setManufacturerId(int manufacturer_id) { this.manufacturer_id = manufacturer_id; }
	
	@XmlElement(name="name")
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	public String toString()
	{	
		return name;
	}

}
