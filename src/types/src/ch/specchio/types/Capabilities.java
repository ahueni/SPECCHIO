package ch.specchio.types;

import java.util.Hashtable;

import javax.xml.bind.annotation.*;

/**
 * Server capabilities descriptor
 */
@XmlRootElement(name="capabilities")
public class Capabilities {
	
	/** mapping of capability names to values */
	private Hashtable<String, String> table;
	
	/** capability name for version number */
	public static final String VERSION = "version";
	
	/** capability name for the maximum object size */
	public static final String MAX_OBJECT_SIZE = "max_object_size";
	
	
	/**
	 * Default constructor. Constructs an empty capabilities object.
	 */
	public Capabilities() {
		
		this.table = new Hashtable<String, String>();
		
	}
	
	
	@XmlElement(name="table")
	public Hashtable<String, String> getTable() { return this.table; }
	public void setTable(Hashtable<String, String> table) { this.table = table; }
	
	
	/**
	 * Get the value of a capability.
	 * 
	 * @param capability	the capability
	 * 
	 * @return the value of the capability, or null if the capability is not known
	 */
	public String getCapability(String capability) {
		
		return table.get(capability);
		
	}
	
	
	/**
	 * Set the value of a capability.
	 * 
	 * @param capability	the capability
	 * @param value			the new value
	 */
	public void setCapability(String capability, String value) {
		
		table.put(capability, value);
		
	}
	

}
