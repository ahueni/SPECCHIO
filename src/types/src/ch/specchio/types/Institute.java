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
	
	
	public String toString() {
		
		if (department != null && department.length() > 0) {
			return department + ", " + name;
		} else {
			return name;
		}
		
	}
				
}
