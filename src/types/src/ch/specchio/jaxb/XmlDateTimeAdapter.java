package ch.specchio.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Convert between joda datetime and XML-serialisable millis represented as long
 */
public class XmlDateTimeAdapter  extends XmlAdapter<XmlDateTime, DateTime> {

	@Override
	public XmlDateTime marshal(DateTime v) throws Exception {
		
		if(v != null)
			return new XmlDateTime(v.getMillis());
		else
			return new XmlDateTime(0); 

		
	}

	@Override
	public DateTime unmarshal(XmlDateTime v) throws Exception {
		
		return new DateTime(v.millis, DateTimeZone.UTC);
	}
	
	// are these ever called?
	public XmlDateTime[] marshal(DateTime[] v) throws Exception {
		
		XmlDateTime xml[] = new XmlDateTime[v.length];
		
		for (int i = 0; i < v.length; i++) {
			xml[i] = new XmlDateTime(v[i].getMillis());
		}

		return xml;
	}

	// are these ever called?
	public DateTime[] unmarshal(XmlDateTime[] v) throws Exception {
		
		DateTime dt[] = new DateTime[v.length];
		for (int i = 0; i < v.length; i++) {
			dt[i] = new DateTime(v[i].millis);
		}
		
		return dt;
	}
	


}