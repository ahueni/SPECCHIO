package ch.specchio.types;

import java.util.Hashtable;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.specchio.jaxb.XmlMapAdapter;

@XmlRootElement(name="spectrum_factor_table")
public class SpectrumFactorTable {
	
	private Hashtable<Integer, Integer> table;
	
	public SpectrumFactorTable() {
		this.table = new Hashtable<Integer, Integer>();
	}
	
	public SpectrumFactorTable(Hashtable<Integer, Integer> table) {
		this.table = table;
	}
	
	@XmlElement(name="table")
	@XmlJavaTypeAdapter(XmlMapAdapter.class)
	public Hashtable<Integer, Integer> getHashtable() { return this.table; }
	public void setHashtable(Hashtable<Integer, Integer> table) { this.table = table; }
	
	public Integer getFactor(Integer spectrum_id) { return this.table.get(spectrum_id); }
	public void put(Integer spectrum_id, Integer factor) { this.table.put(spectrum_id, factor); }

}
