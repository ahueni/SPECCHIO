package ch.specchio.metadata;

import ch.specchio.types.ConflictInfo;
import ch.specchio.types.MetaParameter;


public class MD_EAV_Field extends MD_Field {
	
	MetaParameter mp;
	
	
	public MD_EAV_Field(MetaParameter mp, ConflictInfo conflictInfo)
	{
		this.mp = mp;
		this.conflict = conflictInfo;
		
		
	}
	
	public void textReport() {
		
		System.out.println(mp.getAttributeName() + ": " + mp.getValue() + ", conflict status: " + conflict.getConflictData(mp.getEavId()).getStatus());
		
	}
	
	
	public int compareTo(MD_Field other) {
		
		int cmp = super.compareTo(other);
		if (cmp == 0) {
			// fields have the same type and name; order by EAV identifier
			MetaParameter other_mp = ((MD_EAV_Field)other).getMetaParameter();
			if (mp.getEavId() < other_mp.getEavId()) {
				cmp = -1;
			} else if (mp.getEavId() > other_mp.getEavId()) {
				cmp = 1;
			}
		}
		
		return cmp;
		
	}

	public String getLabel() {

		return mp.getAttributeName();
	}	
	
	public int get_conflict_status()
	{
		return conflict.getConflictData(mp.getEavId()).getStatus();
	}	
	
	public int getNoOfSharingRecords()
	{
		return conflict.getConflictData(mp.getEavId()).getNumberOfSharingRecords();
	}
	
	public int getSelectedRecords()
	{
		return conflict.getConflictData(mp.getEavId()).getNumberOfSelectedRecords();
	}	
	
	
	public MetaParameter getMetaParameter()
	{
		return mp;
	}

	@Override
	public String getLabelWithUnit() {
		
		String unit = mp.getUnitName();
		
		if(unit == null) unit = ""; // unit can be null if there is not yet a default unit defined for that metaparameter
		
		if(!unit.equals("RAW") && !unit.equals("String") && !unit.equals("Date") && !unit.equals(""))
		{
			return mp.getAttributeName() + " [" + unit + "]";
		}
		else
		{
			return getLabel();
		}
	}
	
	
	public void setEavId(int eav_id, int old_eav_id) {
		
		if (eav_id != old_eav_id) {
			mp.setEavId(eav_id);
			conflict.addConflict(eav_id, conflict.getConflict(old_eav_id));
			conflict.removeConflict(old_eav_id);
		}
		
	}


}
