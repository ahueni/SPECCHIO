package ch.specchio.types;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="conflict_struct")
public 	class ConflictStruct
{
	private int status;
	private int no_of_sharing_records;
	private int no_of_selected_records;	
	
	public ConflictStruct()
	{
		
	}
	
	public ConflictStruct(int status, int no_of_sharing_records, int no_of_selected_records)
	{
		this.status = status;
		this.no_of_sharing_records = no_of_sharing_records;
		this.no_of_selected_records = no_of_selected_records;			
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
	
}