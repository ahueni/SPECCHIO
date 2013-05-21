package ch.specchio.types;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.bind.annotation.*;

/**
 * This class represents a table of picture data.
 */
@XmlRootElement(name="picture_table")
public class PictureTable {
	
	/** picture */
	private Hashtable<Integer, Picture> pictures;
	
	/** default constructor */
	public PictureTable()
	{
		this.pictures = new Hashtable<Integer, Picture>();
	}
	
	@XmlElement(name="pictures")
	public Hashtable<Integer, Picture> getPictures() { return this.pictures; }
	public void setPictures(Hashtable<Integer, Picture> pictures) { this.pictures = pictures; }

	/** get a picture from the table */
	public Picture get(Integer picture_id) { return this.pictures.get(picture_id); }
	
	/** get an enumeration of the pciture identifiers in the table */
	public Enumeration<Integer> getIdEnumeration() { return this.pictures.keys(); }
	
	/** add or replace a picture */
	public void put(Integer picture_id, Picture picture) { this.pictures.put(picture_id, picture); }
	
	/** get the number of pictures in the table */
	public int size() { return this.pictures.size(); }

}
