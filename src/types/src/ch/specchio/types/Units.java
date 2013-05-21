package ch.specchio.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="units")
public class Units {
	
	@XmlElement public int id;
	@XmlElement public String name;
	@XmlElement public String description;
	@XmlElement public String short_name;
	@XmlElement public int category_id;

}
