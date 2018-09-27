package ch.specchio.types;

import javax.xml.bind.annotation.*;

/**
 * This class represents an internal node of the spectral data hierarchy.
 */
@XmlRootElement(name="hierarchy_node")
public class hierarchy_node extends spectral_node_object {
	
	/**
	 * Default constructor.
	 */
	public hierarchy_node() {
		
		super();
		
	}
	
	/**
	 * Constructor.
	 */
	public hierarchy_node(int id, String name, String order_by) {
		
		super(id, name, false, order_by);
		
	}

}
