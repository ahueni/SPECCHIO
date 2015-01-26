package ch.specchio.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;

/**
 * Convert between arbitrary objects and XML-serialisable meta-parameter values.
 */
public class XmlMetaParameterValueAdapter extends XmlAdapter<XmlMetaParameterValue, Object> {

	@Override
	public XmlMetaParameterValue marshal(Object v) {
		
		if(v instanceof DateTime)
		{
			try {
				v = ((DateTime) v).getMillis();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return new XmlMetaParameterValue(v);
	
		
	}

	@Override
	public Object unmarshal(XmlMetaParameterValue v) {
		
		return v.object;
	}

}
