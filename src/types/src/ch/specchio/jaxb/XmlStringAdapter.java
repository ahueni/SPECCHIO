package ch.specchio.jaxb;

import java.util.Collection;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Converts XmlString objects used by JAXB into normal String objects used
 * by everyone else.
 */
public class XmlStringAdapter extends XmlAdapter<XmlString, String> {

	/**
	 * Convert a String into an XmlString.
	 * 
	 * @param s	the string
	 * 
	 * @return a new XmlString object containing the input string
	 */
	@Override
	public XmlString marshal(String s)  {
		
		return new XmlString(s);
	}
	
	
	/**
	 * Convert an array of strings into an array of XmlStrings.
	 * 
	 * @param s	the array
	 * 
	 * @return a new array of XmlString objects
	 */
	public XmlString[] marshalArray(String s[]) {
		
		XmlString xml[] = new XmlString[s.length];
		for (int i = 0; i < s.length; i++) {
			xml[i] = marshal(s[i]);
		}
		
		return xml;
		
	}
	
	
	/**
	 * Convert a collection of strings into an array of XmlStrings.
	 * 
	 * @param c	the collection
	 * 
	 * @return a new array of XmlString objects
	 */
	public XmlString[] marshalArray(Collection<String> c) {
		
		XmlString xml[] = new XmlString[c.size()];
		int i = 0;
		for (String s : c) {
			xml[i] = marshal(s);
			i++;
		}
		
		return xml;
		
	}
	

	/**
	 * Convert an XmlString into a String.
	 * 
	 * @param xml	the XML string
	 * 
	 * @return a reference to the string contained in the input object
	 */
	@Override
	public String unmarshal(XmlString xml)  {
		
		return xml.getString();
		
	}
	
	
	/**
	 * Convert an array of XmlString objects in an array of String objects.
	 * 
	 * @param xml	the XML string array
	 * 
	 * @return a new array of Sring objects
	 */
	public String[] unmarshalArray(XmlString[] xml) {
		
		String s[] = new String[xml.length];
		for (int i = 0; i < xml.length; i++) {
			s[i] = xml[i].getString();
		}
		
		return s;
		
	}
			

}
