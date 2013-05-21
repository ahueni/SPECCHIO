package ch.specchio.jaxb;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="integer")
public class XmlInteger {
	
	private Integer obj;
	
	public XmlInteger() {};
	public XmlInteger(Integer obj) { this.obj = obj; }
	
	@XmlElement(name="contents")
	public Integer getInteger() { return this.obj; }
	public void setInteger(Integer obj) { this.obj = obj; }

}
