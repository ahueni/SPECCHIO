package ch.specchio.types;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.bind.annotation.*;

/**
 * This class represents a table of metadata conflicts. It is basically
 * a wrapper around Hashtable<String, ConflictInfo>.
 */
@XmlRootElement(name="conflict_table")
public class ConflictTable {
	
	/** mapping of field names and attribute ids to conflict information structures */
	private Hashtable<String, ConflictInfo> table;
	
	public ConflictTable() { this.table = new Hashtable<String, ConflictInfo>(); }
	
	
	@XmlElement(name="table")
	public Hashtable<String, ConflictInfo> getHashtable() { return this.table; }
	public void setHashtable(Hashtable<String, ConflictInfo> table) { this.table = table; }
	
	public ConflictInfo get(String fieldname) { return this.table.get(fieldname); }
	public void put(String fieldname, ConflictInfo info) { this.table.put(fieldname, info); }
	
	public ConflictInfo get(int attribute_id) { return this.table.get(Integer.toString(attribute_id)); }
	public void put(int attribute_id, ConflictInfo info) { this.table.put(Integer.toString(attribute_id), info); }
	
	public Enumeration<String> conflicts() { return this.table.keys(); }

}
