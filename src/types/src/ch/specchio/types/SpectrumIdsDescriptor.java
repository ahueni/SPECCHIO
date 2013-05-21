package ch.specchio.types;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

/**
 * This class represents a pair of lists of spectrum identifiers, used for
 * requests to the getSpectrumFactorTable and getReferenceSpace services.
 */
@XmlRootElement(name="spectrum_ids_descriptor")
public class SpectrumIdsDescriptor {
	
	private ArrayList<Integer> spectrum_ids_1;
	private ArrayList<Integer> spectrum_ids_2;
	
	/** default constructor */
	public SpectrumIdsDescriptor() {
		
		this(new ArrayList<Integer>(), new ArrayList<Integer>());
		
	}
	
	/** constructor with two id lists */
	public SpectrumIdsDescriptor(ArrayList<Integer> spectrum_ids_1, ArrayList<Integer> spectrum_ids_2) {
		
		this.spectrum_ids_1 = spectrum_ids_1;
		this.spectrum_ids_2 = spectrum_ids_2;
		
	}
	
	/** constructor with one id list */
	public SpectrumIdsDescriptor(ArrayList<Integer> spectrum_ids_1) {
		
		this(spectrum_ids_1, new ArrayList<Integer>());
		
	}
	
	/** constructor with one id and one list */
	public SpectrumIdsDescriptor(Integer spectrum_ids_1, ArrayList<Integer> spectrum_ids_2) {
		
		this.spectrum_ids_1 = new ArrayList<Integer>();
		this.spectrum_ids_1.add(spectrum_ids_1);
		this.spectrum_ids_2 = spectrum_ids_2;
		
	}
	
	@XmlElement(name="spectrum_ids_1")
	public ArrayList<Integer> getSpectrumIds1() { return this.spectrum_ids_1; }
	public void setSpectrumIds1(ArrayList<Integer> spectrum_ids) { this.spectrum_ids_1 = spectrum_ids; }
	
	@XmlElement(name="spectrum_ids_2")
	public ArrayList<Integer> getSpectrumIds2() { return this.spectrum_ids_2; }
	public void setSpectrumIds2(ArrayList<Integer> spectrum_ids) { this.spectrum_ids_2 = spectrum_ids; }
	
	
	public ArrayList<Integer> getSpectrumIds(int i) {
		
		if (i == 1) {
			return this.spectrum_ids_1;
		} else if (i == 2) {
			return this.spectrum_ids_2;
		} else {
			throw new ArrayIndexOutOfBoundsException(i);
		}
		
	}
	
	
	public Integer getSpectrumId(int i, int j) {
		
		return getSpectrumIds(i).get(j);
		
	}
		
}
