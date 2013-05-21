package ch.specchio.types;

import javax.xml.bind.annotation.*;

/**
 * This class represents the top-level node of a campaign in the spectral data hierarchy.
 */
@XmlRootElement(name="campaign_node")
public class campaign_node extends spectral_node_object {
	
	/**
	 * Default constructor.
	 */
	public campaign_node() {
		
		super();
		
	}
	
	
	/**
	 * Constructor.
	 */
	public campaign_node(int campaign_id, String name, boolean restrict_to_view, String order_by) {
		
		super(campaign_id, name, restrict_to_view, order_by);
		
	}

}
