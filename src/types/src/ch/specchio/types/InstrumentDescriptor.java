package ch.specchio.types;

import javax.xml.bind.annotation.*;


/**
 * Class that identifies an instrument.
 */
@XmlRootElement(name="instrument_descriptor")
public class InstrumentDescriptor {

	private int instrument_id;
	private String instrument_name;
	
	public InstrumentDescriptor() {};
	public InstrumentDescriptor(int instrument_id, String instrument_name) {
		this.instrument_id = instrument_id;
		this.instrument_name = instrument_name;
	}
	
	@XmlElement(name="instrument_id")
	public int getInstrumentId() { return this.instrument_id; }
	public void setInstrumentId(int instrument_id) { this.instrument_id = instrument_id; }
	
	@XmlElement(name="instrument_name")
	public String getInstrumentName() { return this.instrument_name; }
	public void setInstrumentName(String instrument_name) { this.instrument_name = instrument_name; }

}
