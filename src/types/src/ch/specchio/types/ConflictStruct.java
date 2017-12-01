package ch.specchio.types;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="conflict_struct")
public 	class ConflictStruct
{
	private Integer status;
	private int no_of_sharing_records;
	private int no_of_selected_records;	
	private int attribute_id;	
	private boolean inherited = false; 
	
	public ConflictStruct()
	{
		
	}
	
	public ConflictStruct(int status, int no_of_sharing_records, int no_of_selected_records)
	{
		this.status = status;
		this.no_of_sharing_records = no_of_sharing_records;
		this.no_of_selected_records = no_of_selected_records;			
	}
	
	public ConflictStruct(int status, int no_of_sharing_records, int no_of_selected_records, int attribute_id)
	{
		this.status = status;
		this.no_of_sharing_records = no_of_sharing_records;
		this.no_of_selected_records = no_of_selected_records;	
		this.setAttribute_id(attribute_id);
	}
	
	
	
	@XmlElement(name="status")
	public int getStatus() { return this.status; }
	public void setStatus(int status) { this.status = status; }
	
	@XmlElement(name="no_of_sharing_records")
	public int getNumberOfSharingRecords() { return this.no_of_sharing_records; }
	public void setNumberOfSharingRecords(int no_of_sharing_records) { this.no_of_sharing_records = no_of_sharing_records; }
	
	@XmlElement(name="no_of_selected_records")
	public int getNumberOfSelectedRecords() { return this.no_of_selected_records; }
	public void setNumberOfSelectedRecords(int no_of_selected_records) { this.no_of_selected_records = no_of_selected_records; }

	@XmlElement(name="attribute_id")
	public int getAttribute_id() {
		return attribute_id;
	}
	public void setAttribute_id(int attribute_id) {
		this.attribute_id = attribute_id;
	}

	public boolean isInherited() {
		return inherited;
	}

	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}
	
}