package ch.specchio.metadata;

import ch.specchio.types.ConflictInfo;
import ch.specchio.types.MetaParameter;



public abstract class MD_Field implements Comparable<MD_Field> {
	
	protected ConflictInfo conflict;


	private Object new_value;
	private String description; // help string
	private String annotation = null; // currently used for binary content

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}	

	public ConflictInfo getConflict() {
		return conflict;
	}

	public void setConflict(ConflictInfo conflict) {
		this.conflict = conflict;
	}	
	
	public int getLevel() {
		return MetaParameter.SPECTRUM_LEVEL; // default level
	}	
	
//	public boolean matches_db_field(String field){
//		return false;
//	}
	

}
