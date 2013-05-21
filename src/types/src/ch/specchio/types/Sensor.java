package ch.specchio.types;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="sensor")
public class Sensor {
	
	private int sensor_id;
	private int manufacturer_id = 0;
	private MetaDatatype<String> name;
	private MetaDatatype<String> description;
	private MetaDatatype<String> manufacturer_name;
	private MetaDatatype<String> manufacturer_short_name;
	private MetaDatatype<String> manufacturer_www;
	private MetaDatatype<Integer>  no_of_channels;
	private MetaDatatype<String> wavelength_range;
	private double[] avg_wavelengths;
	private short[] element_types;
	private int sensor_type; // narrowband, broadband or mixed
	private int sensor_type_no; // sensor model number given by manufacturer
	boolean is_valid; // can be invalid if it is a dummy (i.e. not loaded from db)
	
	public Sensor()
	{
		
		name = new MetaDatatype<String>("Sensor name");
		no_of_channels = new MetaDatatype<Integer>("No of bands");
		description = new MetaDatatype<String>("Description");
		manufacturer_name = new MetaDatatype<String>("Manufacturer");
		manufacturer_short_name = new MetaDatatype<String>("Manufacturer Short Name");
		manufacturer_www = new MetaDatatype<String>("Manufacturer WWW");
		wavelength_range = new MetaDatatype<String>("Wavelength range");

		is_valid = false;
		
	}
	
	public Sensor(int sensor_id) {
		
		this();
		
		this.sensor_id = sensor_id;
		
	}
	
	@XmlElement(name="avg_wavelengths")
	public double[] getAverageWavelengths() { return this.avg_wavelengths; }
	public void setAverageWavelengths(double[] avg_wavelengths) { this.avg_wavelengths = avg_wavelengths; }
	public double getAverageWavelength(int i) { return this.avg_wavelengths[i]; }
	public void setAverageWavelength(int i, double avg_wavelength) { this.avg_wavelengths[i] = avg_wavelength; }
	
	@XmlElement(name="description")
	public MetaDatatype<String> getDescription() { return this.description; }
	public void setDescription(MetaDatatype<String> description) { this.description = description; }
	public void setDescription(String description) { this.description.value = description; }
	
	@XmlElement(name="element_types")
	public short[] getElementTypes() { return this.element_types; }
	public void setElementTypes(short[] element_types) { this.element_types = element_types; }
	public double getElementType(int i) { return this.element_types[i]; }
	public void setElementType(int i, short element_type) { this.element_types[i] = element_type; }
	
	@XmlElement(name="is_valid")
	public boolean getValid() { return this.is_valid; }
	public void setValid(boolean is_valid) { this.is_valid = is_valid; }
	
	@XmlElement(name="manufacturer_id")
	public int getManufacturerId() { return this.manufacturer_id; }
	public void setManufacturerId(int manufacturer_id) { this.manufacturer_id = manufacturer_id; }
	
	@XmlElement(name="manufacturer_name")
	public MetaDatatype<String> getManufacturerName() { return this.manufacturer_name; }
	public void setManufacturerName(MetaDatatype<String> manufacturer_name) { this.manufacturer_name = manufacturer_name; }
	public void setManufacturerName(String manufacturer_name) { this.manufacturer_name.value = manufacturer_name; }
	
	@XmlElement(name="manufacturer_short_name")
	public MetaDatatype<String> getManufacturerShortName() { return this.manufacturer_short_name; }
	public void setManufacturerShortName(MetaDatatype<String> manufacturer_short_name) { this.manufacturer_short_name = manufacturer_short_name; }
	public void setManufacturerShortName(String manufacturer_short_name) { this.manufacturer_short_name.value = manufacturer_short_name; }
	
	@XmlElement(name="manufacturer_www")
	public MetaDatatype<String> getManufacturerWWW() { return this.manufacturer_www; }
	public void setManufacturerWWW(MetaDatatype<String> manufacturer_www) { this.manufacturer_www = manufacturer_www; }
	public void setManufacturerWWW(String manufacturer_www) { this.manufacturer_www.value = manufacturer_www; }
	
	@XmlElement(name="name")
	public MetaDatatype<String> getName() { return this.name; }
	public void setName(MetaDatatype<String> name) { this.name = name; }
	public void setName(String name) { this.name.value = name; }
	
	@XmlElement(name="no_of_channels")
	public MetaDatatype<Integer> getNumberOfChannels() { return this.no_of_channels; }
	public void setNumberOfChannels(MetaDatatype<Integer> no_of_channels) { this.no_of_channels = no_of_channels; }
	public void setNumberOfChannels(int no_of_channels) { this.no_of_channels.value = no_of_channels; }
	
	@XmlElement(name="sensor_id")
	public int getSensorId() { return this.sensor_id; }
	public void setSensorId(int sensor_id) { this.sensor_id = sensor_id; }
	
	@XmlElement(name="sensor_type")
	public int getSensorType() { return this.sensor_type; }
	public void setSensorType(int sensor_type) { this.sensor_type = sensor_type; }
	
	@XmlElement(name="sensor_type_no")
	public int getSensorTypeNumber() { return this.sensor_type_no; }
	public void setSensorTypeNumber(int sensor_type_no) { this.sensor_type_no = sensor_type_no; }
	
	@XmlElement(name="wavelength_range")
	public MetaDatatype<String> getWavelengthRange() { return this.wavelength_range; }
	public void setWavelengthRange(MetaDatatype<String> wavelength_range) { this.wavelength_range = wavelength_range; }
	public void setWavelengthRange(String wavelength_range) { this.wavelength_range.value = wavelength_range; }
	
	
	public boolean matches(Sensor other) {
		
		return this.sensor_id == other.getSensorId();
		
	}
	
	
	public String toString() {
		
		return this.name.value;
		
	}

}




