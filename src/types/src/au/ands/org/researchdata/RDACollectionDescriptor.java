package au.ands.org.researchdata;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.specchio.types.User;


/**
 * Description of a collection for Research Data Australia.
 */
@XmlRootElement(name="rda_collection_descriptor")
public class RDACollectionDescriptor {
	
	/** the identifiers of the spectra in the collection */
	private int[] spectrumIds;
	
	/** the principal investigator of the collection */
	private User principalInvestigator;
	
	/** the primary collection name */
	private String primaryName;
	
	/** the breif collection description */
	private String briefDescription;
	
	
	/**
	 * Default constructor.
	 */
	public RDACollectionDescriptor() {
		
		// construct a collection with no spectra
		this(new int[0]);
		
	}
	
	
	/**
	 * Constructor using an array of spectrum identifiers.
	 * 
	 * @param spectrumIdsArray	the identifiers of the spectra in the collection
	 */
	public RDACollectionDescriptor(int[] spectrumIdsArray) {
		
		spectrumIds = spectrumIdsArray;
		
	}
	
	
	/**
	 * Constructor using a list of spectrum identifiers.
	 * 
	 * @param spectrumIdsList	the list of identifiers
	 */
	public RDACollectionDescriptor(List<Integer> spectrumIdsList) {
		
		spectrumIds = new int[spectrumIdsList.size()];
		int i = 0;
		for (Integer id : spectrumIdsList) {
			spectrumIds[i] = id;
			i++;
		}
		
	}
	
	
	/**
	 * Get the brief description of the collection.
	 * 
	 * @return the brief description of the collection
	 */
	@XmlElement(name="brief_description")
	public String getBriefDescription() { return briefDescription; }
	
	
	/**
	 * Set the brief description of the collection.
	 * 
	 * @param briefDescriptionIn	a brief description
	 */
	public void setBriefDescription(String briefDescriptionIn) { briefDescription = briefDescriptionIn; }
	
	
	/**
	 * Get the primary name of the collection.
	 * 
	 * @return the primary name of the collection
	 */
	@XmlElement(name="primary_name")
	public String getPrimaryName() { return primaryName; }
	
	
	/**
	 * Set the primary name of the collection.
	 * 
	 * @param primaryNameIn	the primary name
	 */
	public void setPrimaryName(String primaryNameIn) { primaryName = primaryNameIn; }
	
	
	/**
	 * Get the principal investigator of the collection.
	 * 
	 * @return a User object representing the principal investigator
	 */
	@XmlElement(name="principal_investigator")
	public User getPrincipalInvestigator() { return principalInvestigator; }
	
	
	/**
	 * Set the principal investigator of the collection.
	 * 
	 * @param principalInvestigatorIn	the principal investigator
	 */
	public void setPrincipalInvestigator(User principalInvestigatorIn) { principalInvestigator = new User(principalInvestigatorIn); }
	
	
	/**
	 * Get the spectrum identifiers in the collection.
	 * 
	 * @return a reference to an array containing all of the spectrum identifiers in the collection
	 */
	@XmlElement(name="spectrum_ids")
	public int[] getSpectrumIds() { return spectrumIds; }
	
	
	/**
	 * Set the spectrum identifiers in the collection. This method is required by JAXB and
	 * should not be used otherwise.
	 */
	public void setSpectrumIds(int[] spectrumIdsIn) { spectrumIds = spectrumIdsIn; }

}
