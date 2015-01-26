package ch.specchio.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * XML-serialisable wrapper for joda time values.
 */
@XmlRootElement(name="joda_datetime")
public class XmlDateTime {
	
	@XmlElement(name="millis") public long millis;
	
	public XmlDateTime() {};
	
	public XmlDateTime(long millis) { this.millis = millis; }	

}
