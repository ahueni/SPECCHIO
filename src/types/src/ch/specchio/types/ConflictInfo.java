package ch.specchio.types;

import java.util.Hashtable;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="conflict_info")
public class ConflictInfo {
	
	Hashtable<Integer, ConflictStruct> conflict_structs = new Hashtable<Integer, ConflictStruct>();
	
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
	
	public ConflictStruct getConflict(Integer eav_id)
	{
		return conflict_structs.get(eav_id);
	}
	
	public void removeConflict(Integer eav_id)
	{
		conflict_structs.remove(eav_id);
	}

	@XmlElement(name="conflict_structs")
	public Hashtable<Integer, ConflictStruct> getConflictStructs() { return this.conflict_structs; }
	public void setConflictStructs(Hashtable<Integer, ConflictStruct> conflict_structs) { this.conflict_structs = conflict_structs; }

}
