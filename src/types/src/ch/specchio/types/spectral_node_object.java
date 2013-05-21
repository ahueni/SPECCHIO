package ch.specchio.types;

import javax.xml.bind.annotation.*;

/**
 * This class represents a node in the spectral data hierarchy.
 */
@XmlRootElement(name="spectral_node")
@XmlSeeAlso({database_node.class, campaign_node.class, hierarchy_node.class, spectrum_node.class})
public abstract class spectral_node_object
{
	
	/** database id */
	private int id;
	
	/** node name */
	private String name;
	
	/** show this user's children only? */
	private boolean restrict_to_view;
	
	/** how to order the children of this node */
	private String order_by;
	
	
	/**
	 * Default constructor.
	 */
	public spectral_node_object() {
		
		this.order_by = "Acquisition Time";
		
	}
	
	/**
	 * Constructor.
	 */
	public spectral_node_object(int id, String name, boolean restrict_to_view, String order_by)
	{	
		this.id = id;
		this.name = name;
		this.restrict_to_view = restrict_to_view;
		this.order_by = order_by;
	}
	
	/**
	 * Constructor.
	 */
	public spectral_node_object(int id, String name, boolean restrict_to_view)
	{
		this(id, name, restrict_to_view, "Acquisition Time");
	}
	
	public String toString()
	{
		return name ;
	}
	
	
	@XmlElement(name="id")
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	
	@XmlElement(name="name")
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	@XmlElement(name="order_by")
	public String getOrderBy() { return this.order_by; }
	public void setOrderBy(String order_by) { this.order_by = order_by; }
	
	@XmlElement(name="restrict_to_view")
	public boolean getRestrictToView() { return this.restrict_to_view; }
	public void setRestrictToView(boolean restrict_to_view) { this.restrict_to_view = restrict_to_view; }
	
}