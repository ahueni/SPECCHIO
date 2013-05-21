package ch.specchio.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * XML-serialisable wrapper for meta-parameter values.
 */
@XmlRootElement(name="metaparameter_value")
public class XmlMetaParameterValue {

	@XmlElement(name="object") public Object object;
	
	public XmlMetaParameterValue() {};
	
	public XmlMetaParameterValue(Object object) { this.object = object; }
	
}
