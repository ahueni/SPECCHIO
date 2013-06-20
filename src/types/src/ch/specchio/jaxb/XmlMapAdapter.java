package ch.specchio.jaxb;

import java.util.Hashtable;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * Class for mapping Java map objects to JAXB-friendly XmlMap objects.
 */
public class XmlMapAdapter<K, V> extends XmlAdapter<XmlMap<K, V>, Map<K, V>> {

	@Override
	public XmlMap<K, V> marshal(Map<K, V> map)  {
		
		return new XmlMap<K, V>(map);
	}

	@Override
	public Map<K, V> unmarshal(XmlMap<K, V> xmlmap)  {
		
		Map<K, V> map = new Hashtable<K, V>();
		map.putAll(xmlmap);
		
		return map;
	}

}
