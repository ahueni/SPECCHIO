package ch.specchio.types;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="instrument_setting")
public class InstrumentSetting {
	
	private  String sql_name;
	private Float value;
	
	public InstrumentSetting()
	{
	}
	
	public InstrumentSetting(String name, Float value)
	{
		this.sql_name = name;
		this.value = value;
	}
	
	@XmlElement(name="sql_name")
	public String getSqlName() { return this.sql_name; }
	public void setSqlName(String sql_name) { this.sql_name = sql_name; }
	
	@XmlElement(name="value")
	public Float getValue() { return this.value; }
	public void setValue(Float value) { this.value = value; }
	
}

