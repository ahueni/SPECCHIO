package ch.specchio.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="boolean")
public class XmlBoolean {
	
	private Boolean obj;
	
	public XmlBoolean() {};
	public XmlBoolean(Boolean obj) { this.obj = obj; }
	
	@XmlElement(name="contents")
	public Boolean getBoolean() { return this.obj; }
	public void setBoolean(Boolean obj) { this.obj = obj; }

}
