package ch.specchio.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "key", "originatingSource", "party", "collection" })
public class RegistryObject {
	
	@XmlAttribute(name="group")
	private String group;
	private String key;
	private String originatingSource;
	private Party party;
	private Collection collection;
	
	public void setGroup(String group) {
		this.group = group;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getOriginatingSource() {
		return originatingSource;
	}
	public void setOriginatingSource(String originatingSource) {
		this.originatingSource = originatingSource;
	}
	public Party getParty() {
		return party;
	}
	public void setParty(Party party) {
		this.party = party;
	}
	public Collection getCollection() {
		return collection;
	}
	public void setCollection(Collection collection) {
		this.collection = collection;
	}
}
