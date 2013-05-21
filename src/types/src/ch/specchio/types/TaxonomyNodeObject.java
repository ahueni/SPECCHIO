package ch.specchio.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="taxonomy_node_object")
public class TaxonomyNodeObject {
	
	TaxonomyNodeObject parent = null;
	
	private String name;
	private String code;
	private String description;
	
	/** database id */
	private int id;	
	
	/** attribute id */
	private int attribute_id;		
	
	/**
	 * Default constructor.
	 */
	public TaxonomyNodeObject()	{		
	}
	
	public TaxonomyNodeObject(String name, int attribute_id)
	{
		this(name, attribute_id, 0, null);
	}

	public TaxonomyNodeObject(String name, int attribute_id, int id)
	{
		this(name, attribute_id, id, null);
	}
	
	public TaxonomyNodeObject(String name, int attribute_id, int id, TaxonomyNodeObject parent)
	{
		this.name = name;
		this.id = id;
		this.attribute_id = attribute_id;
		this.parent = parent;
	}	
	
	public String toString()
	{
		return name ;
	}	

	@XmlElement(name="id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement(name="attribute_id")
	public int getAttribute_id() {
		return attribute_id;
	}

	public void setAttribute_id(int attribute_id) {
		this.attribute_id = attribute_id;
	}

	@XmlElement(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlElement(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

}
