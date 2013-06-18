package ch.specchio.types;

import javax.xml.bind.annotation.*;

/**
 * This class represents an institute.
 */
@XmlRootElement(name="institute")
public class Institute {

	private int institute_id;	
	private String name = null;
	private String department = null;
	private String street = null;
	private String streetNumber = null;
	private String poCode = null;
	private String city = null;
	private Country country = null;
	private String www = null;
	
	public Institute() {}
	public Institute(String name, String department) {
		this(0, name, department);
	}
	public Institute(int institute_id, String name, String department) {
		this.institute_id = institute_id;
		this.name = name;
		this.department = department;
	}
	public Institute(Institute other) {
		this(other.institute_id, other.name, other.department);
	}
	
	@XmlElement(name="institute_id")
	public int getInstituteId() { return this.institute_id; }
	public void setInstituteId(int institute_id) { this.institute_id = institute_id; }
	
	@XmlElement(name="name")
	public String getInstituteName() { return this.name; }
	public void setInstituteName(String name) { this.name = name; }
	
	@XmlElement(name="department")
	public String getDepartment() { return this.department; }
	public void setDepartment(String department) { this.department = department; }
	
	@XmlElement(name="street")
	public String getStreet() { return this.street; }
	public void setStreet(String street) { this.street = street; };
	
	@XmlElement(name="street_no")
	public String getStreetNumber() { return this.streetNumber; }
	public void setStreetNumber(String streetNumber) { this.streetNumber = streetNumber; };
	
	@XmlElement(name="po_code")
	public String getPostOfficeCode() { return this.poCode; }
	public void setPostOfficeCode(String poCode) { this.poCode = poCode; };
	
	@XmlElement(name="city")
	public String getCity() { return this.city; }
	public void setCity(String city) { this.city = city; };
	
	@XmlElement(name="country")
	public Country getCountry() { return this.country; }
	public void setCountry(Country country) { this.country = country; }
	
	@XmlElement(name="www")
	public String getWWWAddress() { return this.www; }
	public void setWWWAddress(String www) { this.www = www; };
	
	
	public String toString() {
		
		if (department != null && department.length() > 0) {
			return department + ", " + name;
		} else {
			return name;
		}
		
	}
				
}
