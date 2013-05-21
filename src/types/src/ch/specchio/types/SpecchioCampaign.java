package ch.specchio.types;

import javax.xml.bind.annotation.*;

/**
 * SPECCHIO campaign.
 */
@XmlRootElement(name="specchio_campaign")
public class SpecchioCampaign extends Campaign {
	
	/**
	 * Default constructor.
	 */
	public SpecchioCampaign() {
		
		super();
		
	}
	
	
	/**
	 * Constructor.
	 */
	public SpecchioCampaign(int campaign_id, String name, String path) {
		
		super(campaign_id, name, path);
		
	}
	
	
	/**
	 * Get the campaign type.
	 * 
	 * @return "specchio"
	 */
	@XmlElement(name="campaign_type")
	public String getType() {
		
		return "specchio";
		
	}

}
