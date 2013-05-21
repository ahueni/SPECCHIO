package ch.specchio.metadata;

import ch.specchio.types.ConflictInfo;



public abstract class MD_Field implements Comparable<MD_Field> {
	
	ConflictInfo conflict;
	private Object new_value;

	public void textReport() {
		// TODO Auto-generated method stub
		
	}
	
	public void set_conflict_status(ConflictInfo conflictInfo)
	{
		this.conflict = conflictInfo;
	}
	
	public int get_conflict_status()
	{
		return conflict.getConflictData(0).getStatus();
	}
	
	public void setNewValue(Object value)
	{
		this.new_value = value;
	}
	
	public int getNoOfSharingRecords()
	{
		return conflict.getConflictData(0).getNumberOfSharingRecords();
	}
	
	public int getSelectedRecords()
	{
		return conflict.getConflictData(0).getNumberOfSelectedRecords();
	}
	
	public abstract String getLabel();
	
	public abstract String getLabelWithUnit();


	public Object getNewValue() {
		return new_value;
	}
	
	public int compareTo(MD_Field other) {
		
		// order by label
		return getLabel().compareTo(other.getLabel());
		
	}

	
	
//	public boolean matches_db_field(String field){
//		return false;
//	}
	

}
