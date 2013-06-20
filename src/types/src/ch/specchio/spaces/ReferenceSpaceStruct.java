package ch.specchio.spaces;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.specchio.jaxb.XmlMapAdapter;

/**
 * This class encapsulates data from the getReferenceSpace service.
 */
@XmlRootElement(name="reference_space_struct")
public class ReferenceSpaceStruct {
	
	@XmlElement(name="is_spectralon") public boolean is_spectralon;
	@XmlElement(name="reference_space") public SpectralSpace reference_space;
	@XmlElement(name="spectrum_reference_table") @XmlJavaTypeAdapter(XmlMapAdapter.class) public Hashtable<Integer, Integer> spectrum_reference_table;
	@XmlElement(name="spectrum_ids") public ArrayList<Integer> spectrum_ids;
	
	/** default constructor */
	public ReferenceSpaceStruct() {
		spectrum_ids = new ArrayList<Integer>();
		spectrum_reference_table = new Hashtable<Integer, Integer>();
	}

}
