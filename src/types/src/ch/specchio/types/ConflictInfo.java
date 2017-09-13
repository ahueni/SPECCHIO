package ch.specchio.types;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.specchio.jaxb.XmlMapAdapter;

@XmlRootElement(name="conflict_info")
@XmlSeeAlso(ConflictStruct.class)
public class ConflictInfo {
	
	public static final int non_existent = 0;
	public static final int no_conflict = 1;
	public static final int conflict = 2;
	
	public int int_val_max;
	public int int_val_min;
	public double double_val_max;
	public double double_val_min;	
	
	private Hashtable<Integer, ConflictStruct> conflict_structs = new Hashtable<Integer, ConflictStruct>();
	public Double spat_val_x_max;
	public Double spat_val_y_max;
	public Double spat_val_x_min;
	public Double spat_val_y_min;
	
	public ConflictInfo()
	{
		
	}
	
	
	public ConflictInfo(ConflictStruct conflict) {
		// standard case where there is not multiple entries of attributes per spectrum
		// use a zero eav_id to insert into the conflict structure		
		conflict_structs.put(0, conflict);
	}

	public ConflictStruct getConflictData(Integer eav_id)
	{
			return conflict_structs.get(eav_id);
	}
	
	public void addConflict(Integer eav_id, ConflictStruct conflict_data)
	{
		conflict_structs.put(eav_id, conflict_data);	
	}
	
	public Enumeration<Integer> eavIds() {
		
		return conflict_structs.keys();
		
	}
	
	public ConflictStruct getConflict(Integer eav_id)
	{
		return conflict_structs.get(eav_id);
	}
	
	public void removeConflict(Integer eav_id)
	{
		conflict_structs.remove(eav_id);
	}

	@XmlElement(name="conflict_structs")
	@XmlJavaTypeAdapter(XmlMapAdapter.class) 
	public Hashtable<Integer, ConflictStruct> getConflictStructs() { return this.conflict_structs; }
	public void setConflictStructs(Hashtable<Integer, ConflictStruct> conflict_structs) { this.conflict_structs = conflict_structs; }

}
