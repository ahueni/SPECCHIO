package ch.specchio.types;

import javax.xml.bind.annotation.*;

/**
 * This class represents a spectrum in the spectral node hierarchy.
 */
@XmlRootElement(name="spectrum_node")
public class spectrum_node extends spectral_node_object {
	
	/**
	 * Default constructor.
	 */
	public spectrum_node() {
		
		super();
		
	}
	
	/**
	 * Constructor.
	 */
	public spectrum_node(int id, String name) {
		
		super(id, name, false, null);
		
	}

}
