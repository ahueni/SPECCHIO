package ch.specchio.jaxb;


import javax.xml.bind.annotation.*;

@XmlRootElement(name="string")
public class XmlString {
	
	private String s;
	
	public XmlString() {};
	public XmlString(String s) { this.s = s; }
	
	@XmlElement(name="contents")
	public String getString() { return this.s; }
	public void setString(String s) { this.s = s; }

}
