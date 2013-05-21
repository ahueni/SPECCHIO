package ch.specchio.spaces;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.bind.annotation.*;

/**
 * This class encapsulates data from the getReferenceSpace service.
 */
@XmlRootElement(name="reference_space_struct")
public class ReferenceSpaceStruct {
	
	@XmlElement(name="is_spectralon") public boolean is_spectralon;
	@XmlElement(name="reference_space") public SpectralSpace reference_space;
	@XmlElement(name="spectrum_reference_table") public Hashtable<Integer, Integer> spectrum_reference_table;
	@XmlElement(name="spectrum_ids") public ArrayList<Integer> spectrum_ids;
	
	/** default constructor */
	public ReferenceSpaceStruct() {
		spectrum_ids = new ArrayList<Integer>();
		spectrum_reference_table = new Hashtable<Integer, Integer>();
	}

}
