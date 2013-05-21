package ch.specchio.jaxb;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Converts XMLGregorianCalendar objects used by JAXB into Date objects used
 * by the SPECCHIO application.
 */
public class XmlDateAdapter extends XmlAdapter<XMLGregorianCalendar, Date> {

	@Override
	public XMLGregorianCalendar marshal(Date date) {
		
		XMLGregorianCalendar xmlCal = null;
		
		try {
			// convert the date object into a calendar object
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
		
			// create an XML calendar object
			DatatypeFactory factory = DatatypeFactory.newInstance();
			xmlCal = factory.newXMLGregorianCalendar(cal);
		}
		catch (DatatypeConfigurationException ex) {
			// XMLGregorianCalendarImpl isn't available, but that should never happen
			ex.printStackTrace();
		}
		
		return xmlCal;
	}

	@Override
	public Date unmarshal(XMLGregorianCalendar xmlCal) {
		
		// convert the XML calendar into a normal Java calendar type
		Calendar cal = xmlCal.toGregorianCalendar();
		
		// get the Date object corresponding the calendar object
		return cal.getTime();
		
	}

}
