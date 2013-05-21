package ch.specchio.types;

import javax.xml.bind.annotation.*;

/**
 * This class represents the root node of the database in the spectral data hierarchy.
 */
@XmlRootElement(name="database_node")
public class database_node extends spectral_node_object {
	
	/**
	 * Default constructor.
	 */
	public database_node() {
		
		super();
		
	}
	
	
	/**
	 * Constructor.
	 */
	public database_node(int campaign_id, String name, boolean restrict_to_view, String order_by) {
		
		super(campaign_id, name, restrict_to_view, order_by);
		
	}

}
