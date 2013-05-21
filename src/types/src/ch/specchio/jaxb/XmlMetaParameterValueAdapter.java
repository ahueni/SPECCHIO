package ch.specchio.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Convert between arbitrary objects and XML-serialisable meta-parameter values.
 */
public class XmlMetaParameterValueAdapter extends XmlAdapter<XmlMetaParameterValue, Object> {

	@Override
	public XmlMetaParameterValue marshal(Object v) {
		
		return new XmlMetaParameterValue(v);
		
	}

	@Override
	public Object unmarshal(XmlMetaParameterValue v) {
		
		return v.object;
	}

}
