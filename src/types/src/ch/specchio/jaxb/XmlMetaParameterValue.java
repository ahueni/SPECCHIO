package ch.specchio.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import ch.specchio.types.ArrayListWrapper;
import ch.specchio.types.MetaDate;

/**
 * XML-serialisable wrapper for meta-parameter values.
 */
@XmlRootElement(name="metaparameter_value")
@XmlSeeAlso({ArrayListWrapper.class})
public class XmlMetaParameterValue {

	@XmlElement(name="object") public Object object;
	
	public XmlMetaParameterValue() {};
	
	public XmlMetaParameterValue(Object object) { this.object = object; }
	
}
