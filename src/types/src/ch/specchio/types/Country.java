package ch.specchio.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * This class represents a country.
 */
@XmlRootElement(name="country")
public class Country {
	
	/** country identifier */
	private int id;
	
	/** country name */
	private String name;
	
	
	/**
	 * Default constructor for JAXB.
	 */
	public Country() {
		
		this.id = 0;
		this.name = "";
		
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param id	the country's identifier
	 * @param name	the country's name
	 */
	public Country(int id, String name) {
		
		this.id = id;
		this.name = name;
		
	}
	
	
	@XmlElement(name="id")
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	
	@XmlElement(name="name")
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	
	public String toString() {
		
		return getName();
		
	}

}
