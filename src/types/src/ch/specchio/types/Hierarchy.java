package ch.specchio.types;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="hierarchy")
@XmlSeeAlso({ArrayList.class,SerialisableBufferedImage.class})
public class Hierarchy implements MetadataInterface {
	
	/** campaign id */
	int campaign_id;
	
	/** hierarchy_level id */
	int hierarchy_level_id;
	
	/** hierarchy name */
	String hierarchy_name;


	Metadata smd;

	/**
	 * Default constructor.
	 */	
	public Hierarchy()
	{

	}	
	
	public Hierarchy(int hierarchy_level_id)
	{
		this.hierarchy_level_id = hierarchy_level_id;
	}
	
	@XmlElement(name="eav_metadata")
	public Metadata getEavMetadata() { return this.smd; }
	public void setEavMetadata(Metadata smd) { this.smd = smd; }

	@XmlElement(name="campaign_id")
	public int getCampaignId() { return this.campaign_id; }
	public void setCampaignId(int campaign_id) { this.campaign_id = campaign_id; }

	@XmlElement(name="hierarchy_level_id")
	public int getHierarchyLevelId() { return this.hierarchy_level_id; }
	public void setHierarchyLevelId(int hierarchy_level_id) { this.hierarchy_level_id = hierarchy_level_id; }
	
	@XmlElement(name="smd")
	public Metadata getMetadata() { return this.smd; }
	public void setMetadata(Metadata smd) { this.smd = smd; }
	
	
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return hierarchy_level_id;
	}
	
	@XmlElement(name="hierarchy_name")
	public String getHierarchy_name() {
		return hierarchy_name;
	}
	public void setHierarchy_name(String hierarchy_name) {
		this.hierarchy_name = hierarchy_name;
	}
	

}
