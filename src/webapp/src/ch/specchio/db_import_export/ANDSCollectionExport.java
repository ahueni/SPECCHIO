package ch.specchio.db_import_export;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import au.ands.org.researchdata.RDACollectionDescriptor;
import ch.specchio.eav_db.EAVDBServices;
import ch.specchio.factories.MetadataFactory;
import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.factories.SpecchioCampaignFactory;
import ch.specchio.factories.SpectrumFactory;
import ch.specchio.factories.UserFactory;
import ch.specchio.model.Address;
import ch.specchio.model.AddressPart;
import ch.specchio.model.CitationInfo;
import ch.specchio.model.Collection;
import ch.specchio.model.Coverage;
import ch.specchio.model.Date;
import ch.specchio.model.Dates;
import ch.specchio.model.Description;
import ch.specchio.model.ElectronicAddress;
import ch.specchio.model.FullCitation;
import ch.specchio.model.Identifier;
import ch.specchio.model.Location;
import ch.specchio.model.Name;
import ch.specchio.model.NamePart;
import ch.specchio.model.PhysicalAddress;
import ch.specchio.model.RegistryObject;
import ch.specchio.model.RegistryObjects;
import ch.specchio.model.RelatedInfo;
import ch.specchio.model.RelatedObject;
import ch.specchio.model.Relation;
import ch.specchio.model.Rights;
import ch.specchio.model.Spatial;
import ch.specchio.model.Subject;
import ch.specchio.model.Temporal;
import ch.specchio.types.Campaign;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.Spectrum;
import ch.specchio.types.User;
import ch.specchio.types.attribute;

public class ANDSCollectionExport {

	private static final String ANDS_COLLECTION_FILE_NAME_PREFIX = "spectral-collection-";
	private static final String XML_FILE_POSTFIX = ".xml";
	private static final String COLLECTION_PREFIX = "uow.edu.au/SL/COL/";
	
	private static final String ACQUISITION_TIME_METAPARAMETER = "Acquisition Time";
	private static final String ANDS_COLLECTION_KEY_METAPARAMETER = "ANDS Collection Key";
	private static final String CITATION_METAPARAMETER = "Citation";
	private static final String DATA_USAGE_POLICY_METAPARAMETER = "Data Usage Policy";
	private static final String DOI_METAPARAMETER = "Digital Object Identifier";
	private static final String FOR_CODE_METAPARAMETER = "FOR Code";
	private static final String LOCATION_NAME_METAPARAMETER = "Location Name";
	private static final String PUBLICATION_METAPARAMETER = "Publication";
	
	/** user factory */
	private UserFactory userFactory;
	
	/** campaign factory */
	private SpecchioCampaignFactory specchioCampaignFactory;
	
	/** spectrum factory */
	private SpectrumFactory spectrumFactory;
	
	/** output file location */
	private String andsXMLFileLocation;
	
	/** metadata factory */
	private MetadataFactory metadataFactory;
	
	/** EAV services */
	private EAVDBServices eavDBServices;
	
	/** ANDS party exporter */
	private ANDSPartyExport andsPartyExport;
	
	/** date format for RIF-CS files */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	/** output file */
	private File andsCollectionFile;
	
	/** errors */
	private List<String> errors;
	
	
	/**
	 * Constructor.
	 * 
	 * @param dbUser				the database username
	 * @param dbPassword			the database password
	 * @param _andsXMLFileLocation	the location of the output file
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public ANDSCollectionExport( String dbUser, String dbPassword, String _andsXMLFileLocation)
		throws SPECCHIOFactoryException {

		// create factories, using the same database connection for each
		userFactory = new UserFactory(dbUser, dbPassword);
		specchioCampaignFactory = new SpecchioCampaignFactory(userFactory);
		metadataFactory = new MetadataFactory(userFactory);
		spectrumFactory = new SpectrumFactory(userFactory);
		
		// initialise EAV services
		eavDBServices = new EAVDBServices( userFactory.getStatementBuilder() , userFactory.getAttributes(), userFactory.getDatabaseUserName());
		
		// initialise ANDS XML stuff
		andsXMLFileLocation = _andsXMLFileLocation;
		andsPartyExport = new ANDSPartyExport(andsXMLFileLocation);
		
		// initialise error list
		errors = new ArrayList<String>();
	}
	
	private boolean createFileAndDirectory( String baseDirectoryString, User pi, String collectionIdString) 
	{
		
		File andsCollectionFileNameBaseDir = new File( baseDirectoryString);
		andsCollectionFileNameBaseDir.mkdirs();
	    boolean directoryExists = andsCollectionFileNameBaseDir.exists();
	    System.out.println("fileName 0.11: directoryCreated.exists() is: " + directoryExists);
	    if(!directoryExists)
	    	return false;

	    String fullyQualifiedFileName = baseDirectoryString + ANDS_COLLECTION_FILE_NAME_PREFIX + (collectionIdString.substring(COLLECTION_PREFIX.length(), collectionIdString.length())) + XML_FILE_POSTFIX;
		andsCollectionFile = new File( fullyQualifiedFileName);

		return true;

	}
	
	
	/**
	 * Get the list of errors from the last invocation of exportCollectionXML().
	 * 
	 * @return a list of strings describing all of the error found
	 */
	public List<String> getErrors() {
		
		return errors;
		
	}
	
	/**
	 * Check whether or not there were errors in the last invocation of exportCollectionXML().
	 * 
	 * @return true or false
	 */
	public boolean hasErrors() {
		
		return !errors.isEmpty();
		
	}
	
	/**
	 * This method creates the file in the xml file location that is taken in from the web.xml. If the directory supplied does not exist then the 
	 * file is written to the home directory.
	 * @param pi Specchio user object
	 * @param collectionIdString id string of a collection
	 */
	private void setANDSCollectionFilenameDir( User pi, String collectionIdString)
	{	
		if(!createFileAndDirectory( andsXMLFileLocation + System.getProperty("file.separator"), pi, collectionIdString))
		{
		    createFileAndDirectory( System.getProperty("user.home") + andsXMLFileLocation + System.getProperty("file.separator"), pi, collectionIdString);
		} 
	}
	
	/**
	 * Get a list of campaigns from which the collection is drawn.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * 
	 * @return a list of Campaign objects
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private List<Campaign> obtainCampaigns(RDACollectionDescriptor rdaCollectionDescriptor)
		throws SPECCHIOFactoryException {
		
		// build a set of campaign identifiers
		Set<Integer> campaignIds = new TreeSet<Integer>();
		for (int spectrumId : rdaCollectionDescriptor.getSpectrumIds()) {
			Spectrum s = spectrumFactory.getSpectrum(spectrumId, false);
			campaignIds.add(s.getCampaignId());
		}
		
		// build a list of Campaign objects
		List<Campaign> campaignList = new ArrayList<Campaign>(campaignIds.size());
		for (int campaignId : campaignIds) {
			campaignList.add(specchioCampaignFactory.getCampaign(campaignId));
		}
		
		return campaignList;
		
	}
	
	/**
	 * @param rdaCollectionDescriptor 
	 * @return an ordered list of Dates based on acquisition dates for a spectra.
	 * @throws SPECCHIOFactoryException
	 */
	private List<java.util.Date> obtainFirstLastDates (RDACollectionDescriptor rdaCollectionDescriptor) throws SPECCHIOFactoryException
	{
		// get the acquisition times for all spectra in the collection
		List<MetaParameter> acquisitionTimes = metadataFactory.getMetaParameterValues(
				rdaCollectionDescriptor.getSpectrumIds(),
				ACQUISITION_TIME_METAPARAMETER,
				true
			);
		
		// copy the data values into a sorted list
		List<java.util.Date> fromToDates = new ArrayList<java.util.Date>();
		for (MetaParameter mp : acquisitionTimes) {
			fromToDates.add((java.util.Date)mp.getValue());
		}
		java.util.Collections.sort(fromToDates);

		return fromToDates;
	}
	
	/**
	 * Build the citation information.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * 
	 * @return a list of CitationInfo objects, or null if no citation information is available
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private ArrayList<CitationInfo> obtainCitationInfo(RDACollectionDescriptor rdaCollectionDescriptor) 
		throws SPECCHIOFactoryException {
		
		// get the citations associated with every spectrum in the collection
		ArrayList<MetaParameter> citationMetaParameters = metadataFactory.getMetaParameterValues(
				rdaCollectionDescriptor.getSpectrumIds(),
				CITATION_METAPARAMETER,
				true
			);
		
		if (citationMetaParameters.size() > 0) {
		
			// create a CitationInfo object for each citation in the list
			ArrayList<CitationInfo> citationInfoList = new ArrayList<CitationInfo>(citationMetaParameters.size());
			for (MetaParameter mp : citationMetaParameters) {
				CitationInfo citationInfo = new CitationInfo();
				FullCitation fullCitation = new FullCitation();
				fullCitation.setStyle("Harvard");
				fullCitation.setValue((String)mp.getValue());
				citationInfo.setFullCitation(fullCitation);
				citationInfoList.add(citationInfo);
			}
			
			return citationInfoList;
			
		} else {
			
			// no citation information
			return null;
			
		}
		
	}
	
	
	/**
	 * Construct a collection identifier. This method creates a new identifier
	 * and associates it with every spectrum in the collection. So, it should only
	 * be called if the collection is ready to be created.
	 * 
	 * The collection identifier is COLLECTION_PREFIX followed by the EAV ID of
	 * the new ANDS Collection Key metadata item.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * 
	 * @return the new collection identifier
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	private String obtainCollectionIdString(RDACollectionDescriptor rdaCollectionDescriptor) throws  SPECCHIOFactoryException
	{
		String collectionIdString;
		
		try {
			attribute attr = eavDBServices.ATR.get_attribute_info((eavDBServices.ATR.get_attribute_id(ANDS_COLLECTION_KEY_METAPARAMETER)));
			MetaParameter mp = MetaParameter.newInstance(attr);
			int collectionId = metadataFactory.updateMetadata(mp, convertIntArray( rdaCollectionDescriptor.getSpectrumIds()));
			collectionIdString = COLLECTION_PREFIX + collectionId;
			mp.setValue(collectionIdString);
			metadataFactory.updateMetadata(mp, convertIntArray( rdaCollectionDescriptor.getSpectrumIds()));
		}
		catch (MetaParameterFormatException e) {
			// collection key attribute does not have type string; re-throw as a database error
			throw new SPECCHIOFactoryException(e);
		}
		
		return collectionIdString;
	}
	
	/**
	 * Build the description element for a collection.
	 * 
	 * If there is no description available, an error will be added to the current
	 * error list.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * @param campaigns					the campaigns from which the collection is drawn
	 * 
	 * @return a Description object representing the collection
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private Description obtainCollectionDescription(RDACollectionDescriptor rdaCollectionDescriptor, List<Campaign> campaigns)
		throws SPECCHIOFactoryException {

		// build a string containing the descriptions of all of the campaigns in the collection
		StringBuffer sb = new StringBuffer();
		for (Campaign campaign : campaigns) {
			if (campaign.getDescription() != null && campaign.getDescription().length() > 0) {
				if (sb.length() > 0) {
					// insert a blank line between descriptions
					sb.append("\n\n");
				}
				sb.append(campaign.getDescription());
			} else {
				errors.add(
					"The campaign \"" + campaign.getName() + "\" does not have a description. " +
					"Please add one."
				);
			}
		}

		// build the description object
		Description description = new Description();
		description.setType("brief");
		description.setValue(sb.toString());
		
		return description;
		
	}
	
	/**
	 * Build the name element for a collection.
	 * 
	 * If there is no suitable name or description available, an error will be added
	 * the current error list.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * @param campaigns					the campaigns from which the collection is drawn
	 * 
	 * @return a Name object describing the collection
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private Name obtainCollectionNames(RDACollectionDescriptor rdaCollectionDescriptor, List<Campaign> campaigns)
		throws SPECCHIOFactoryException {
		
		// build a string containing the descriptions of all of the campaigns in the collection
		StringBuffer sb = new StringBuffer();
		for (Campaign campaign : campaigns) {
			if (campaign.getName() != null && campaign.getName().length() > 0) {
				if (sb.length() > 0) {
					// insert a comma between names
					sb.append(", ");
				}
				sb.append(campaign.getName());
			} else {
				errors.add(
					"The campaign with ID " + campaign.getId() + " does not have a name. " +
					"Please add one."
				);
			}
		}

		// build the Name object
		Name name = new Name();
		name.setType("primary");
		NamePart namePart = new NamePart();
		namePart.setValue(sb.toString());
		ArrayList<NamePart> namePartList = new ArrayList<NamePart>();
		namePartList.add(namePart);
		name.setNamePartList(namePartList);
		
		return name;
		
	}
	
	/**
	 * Build the related object for the collector.
	 * 
	 * The collector is the principal investigator.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * 
	 * @return a RelatedObject instance describing the collector.
	 */
	private RelatedObject obtainCollector(RDACollectionDescriptor rdaCollectionDescriptor) {

		// get a reference to the principal investigator's details
		User pi = rdaCollectionDescriptor.getPrincipalInvestigator();
		
		// put the create a relation object with type "hasCollector"
		RelatedObject relatedObject = new RelatedObject();
		relatedObject.setKey(pi.getExternalId());
		Relation relation = new Relation();
		relation.setType("hasCollector");
		relation.setDescription(pi.getDescription());
		relatedObject.setRelation(relation);
		
		return relatedObject;
	}
	
	/**
	 * Build the coverage element for a collection.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * 
	 * @return a Coverage object, or null if there was no data
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private Coverage obtainCoverage(RDACollectionDescriptor rdaCollectionDescriptor)
		throws SPECCHIOFactoryException {

		// get the spatial location for all spectra in the collection
		ArrayList<MetaParameter> locationNameMetaParameters = metadataFactory.getMetaParameterValues(
				rdaCollectionDescriptor.getSpectrumIds(),
				LOCATION_NAME_METAPARAMETER,
				true
			);
		
			
		// convert the list of location metaparameters into a list of Spatial elements
		ArrayList<Spatial> spatialList = new ArrayList<Spatial>(locationNameMetaParameters.size());
		for (MetaParameter mp : locationNameMetaParameters) {
			Spatial spatial = new Spatial();
			spatial.setType("text");
			spatial.setValue((String)mp.getValue());
			spatialList.add(spatial);
		}
		
		// create the temporal coverage, if the spectra have acquisition times
		Temporal temporal = null;
		List<java.util.Date> fromToDateList = obtainFirstLastDates(rdaCollectionDescriptor);
		if (!fromToDateList.isEmpty()) {
			temporal = new Temporal();
			ArrayList<Date> temporalDateList = new ArrayList<Date>();
			Date date1 = new Date();
			date1.setType("dateFrom");
			date1.setDateFormat("W3CDTF");
			date1.setValue(sdf.format(fromToDateList.get(0)));
			temporalDateList.add(date1);
			Date date2 = new Date();
			date2.setType("dateTo");
			date2.setDateFormat("W3CDTF");
			date2.setValue(sdf.format(fromToDateList.get(fromToDateList.size()-1)));
			temporalDateList.add(date2);
			temporal.setDateList(temporalDateList);
		}
		
		if (spatialList.size() > 0 || temporal != null) {
			
			// got data; create a coverage object
			Coverage coverage = new Coverage();
			coverage.setSpatial(spatialList);
			coverage.setTemporal(temporal);
			return coverage;
			
		} else {
			
			// no data; return null
			return null;
			
		}
		
		
	}
	
	/**
	 * Build the list of identifiers for a collection.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * 
	 * @return a list of Identifier objects
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private ArrayList<Identifier> obtainIdentifierList(RDACollectionDescriptor rdaCollectionDescriptor)
		throws SPECCHIOFactoryException {

		// start with an empty list
		ArrayList<Identifier> identifierList = new ArrayList<Identifier>();
		
		// add a URI
		Identifier identifier = new Identifier();
		identifier.setType("uri");
		identifier.setValue("http://hdl.handle.net/102.100.100/9338");
		identifierList.add(identifier);	
		
		// add DOIs
		ArrayList<MetaParameter> doiMetaParameters = metadataFactory.getMetaParameterValues(
				rdaCollectionDescriptor.getSpectrumIds(),
				DOI_METAPARAMETER,
				true
			);
		for (MetaParameter mp : doiMetaParameters) {	
			Identifier doiIdentifier = new Identifier();
			doiIdentifier.setType("doi");
			doiIdentifier.setValue((String)mp.getValue());
			identifierList.add(doiIdentifier);
		}
		
		return identifierList;
		
	}
	
	/**
	 * Build the location object for a collection.
	 * 
	 * The collection's address is the address of the principal investigator. If
	 * the principal investigator does not have an e-mail address or does not
	 * have a physical address, errors will be added to the current error list.
	 * 
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * 
	 * @returns a Location object
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private Location obtainLocation(RDACollectionDescriptor rdaCollectionDescriptor)
		throws SPECCHIOFactoryException {

		// get a reference to the principal investigator
		User pi = rdaCollectionDescriptor.getPrincipalInvestigator();
		
		// start with an empty Location object
		Location location = new Location();
		Address address = new Address();
		
		// set the dates of collection
		List<java.util.Date> fromToDateList = obtainFirstLastDates (rdaCollectionDescriptor);
		location.setDateFrom(sdf.format(fromToDateList.get(0)));
		location.setDateTo(sdf.format(fromToDateList.get(fromToDateList.size()-1)));
		
		// set e-mail address
		if( pi.getEmailAddress() != null && pi.getEmailAddress().length() > 0)
		{
			ElectronicAddress electronicAddress = new ElectronicAddress();
			electronicAddress.setType("email");
			electronicAddress.setValue(pi.getEmailAddress());
			address.setElectronicAddress(electronicAddress);
		} else {
			errors.add("The principal investigator does not have an e-mail address. Please set an e-mail address before submitting a collection.");
		}
		
		// set physical address
		if( (pi.getInstitute() != null 
				&& ( pi.getInstitute().getDepartment() != null && pi.getInstitute().getDepartment().length() > 0)
					||  (pi.getInstitute().getInstituteName() != null && pi.getInstitute().getInstituteName().length() > 0)))
		{
			PhysicalAddress physicalAddress = new PhysicalAddress();
			physicalAddress.setType("streetAddress");
			AddressPart addressPart = new AddressPart();
			addressPart.setType("text");
			String addressString = "";
			if( pi.getInstitute().getDepartment() != null)
			{
				addressString += pi.getInstitute().getDepartment();
				addressString += " ";
			}
			if( pi.getInstitute().getInstituteName() != null)
			{
				addressString += pi.getInstitute().getInstituteName();
			}
			addressPart.setValue(addressString);
			physicalAddress.setAddressPart(addressPart);
			address.setPhysicalAddress(physicalAddress);
		} else {
			errors.add("The principal investigator does not have a department or institute. Please set these before submitting a collection.");
		}
		
		
		location.setAddress(address);
		return location;
		
	}
	
	/**
	 * Build the rights element.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * 
	 * @return a list of Rights objects
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private ArrayList<Rights> obtainRights(RDACollectionDescriptor rdaCollectionDescriptor)
		throws SPECCHIOFactoryException {
		
		// start with an empty list
		ArrayList<Rights> rightsList = new ArrayList<Rights>();
		
		// get all of the data user policy metaparameters
		ArrayList<MetaParameter> dataUsagePolicyMetaParameters = metadataFactory.getMetaParameterValues(
				rdaCollectionDescriptor.getSpectrumIds(),
				DATA_USAGE_POLICY_METAPARAMETER,
				true
			);
		
		if (dataUsagePolicyMetaParameters.size() > 0) {
			
			// create one rights element for every policy
			for (MetaParameter mp : dataUsagePolicyMetaParameters) {
				Rights rights = new Rights();
				rights.setAccessRights((String)mp.getValue());
				rightsList.add(rights);
			}
			
		} else {
			
			// no existing policies; generate a standard-form one
			Rights rights = new Rights();
			rights.setAccessRights(
					"Data can be shared on a case by case basis. " +
					"For further information please contact " +
					rdaCollectionDescriptor.getPrincipalInvestigator().toString() + " " +
					"<" + rdaCollectionDescriptor.getPrincipalInvestigator().getEmailAddress() + ">"
				);
			rightsList.add(rights);
			
		}
		
		return rightsList;
		
	}
	
	/**
	 * Build the related information list.
	 * 
	 * The relation information list will contain a list of every publication associated
	 * with every spectrum in the collection.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * 
	 * @return a list of related info objects
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private ArrayList<RelatedInfo> obtainRelatedInfo(RDACollectionDescriptor rdaCollectionDescriptor)
		throws SPECCHIOFactoryException {
		
		// start with an empty related information list
		ArrayList<RelatedInfo> relatedInfoList = new ArrayList<RelatedInfo>();
		
		// get all the publications associated with spectra in the collection
		ArrayList<MetaParameter> publicationMetaParameters = metadataFactory.getMetaParameterValues(
				rdaCollectionDescriptor.getSpectrumIds(),
				PUBLICATION_METAPARAMETER,
				true
			);
		
		// convert the metaparameter values into RelatedInfo elements
		for (MetaParameter mp : publicationMetaParameters) {
			
			Identifier identifierLocal = new Identifier();
			identifierLocal.setType("local");
			identifierLocal.setValue((String)mp.getValue());
			
			RelatedInfo relatedInfoPublication = new RelatedInfo();
			relatedInfoPublication.setType("publication");
			relatedInfoPublication.setIdentifier(identifierLocal);
			
			relatedInfoList.add(relatedInfoPublication);
		}
		
		return relatedInfoList;
		
	}
	
	/**
	 * Build the subject list.
	 * 
	 * The subject list contains the FOR codes for every specrum in the collection.
	 * If no spectra have any FOR codes, an error will be added to the current error
	 * list.
	 * 
	 * @param rdaCollectionDescriptor	the collection description
	 * 
	 * @return a list of Subject objects
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private ArrayList<Subject> obtainSubjectList(RDACollectionDescriptor rdaCollectionDescriptor)
		throws SPECCHIOFactoryException {

		// start with an empty list
		ArrayList<Subject> subjectList = new ArrayList<Subject>();
		
		// get the FOR codes for every spectrum in the collection
		ArrayList<MetaParameter> forCodeMetaParameters = metadataFactory.getMetaParameterValues(
				rdaCollectionDescriptor.getSpectrumIds(),
				FOR_CODE_METAPARAMETER,
				true
			);
		
		if (forCodeMetaParameters.size() > 0) {
			
			// convert the metaparameter values into Subject elements
			for (MetaParameter mp : forCodeMetaParameters) {
				Subject subjectForCode = new Subject();
				subjectForCode.setType("anzsrc-for");
				subjectForCode.setValue(
					metadataFactory.getTaxonomyObject(((Long) mp.getValue()).intValue()).getCode()
				);
				subjectList.add(subjectForCode);
			}
			
		} else {
			
			// no FOR code available; record an error
			errors.add(
				"There are no FOR codes associated with any spectra in this collection. " + 
				"You must add codes before submitting the collection."
			);
			
		}
		
		return subjectList;
	}
	
	private Integer[] convertIntArray(int[] intArray)
	{
		Integer [] newIntegerArray = new Integer[intArray.length];
		int i = 0;
		for(int value : intArray)
		{
			newIntegerArray[i++] = Integer.valueOf(value);
		}
		return newIntegerArray;
	}
	
	
	/**
	 * Generate a RIF-CS collection document describing a collection.
	 * 
	 * @param rdaCollectionDescriptor	the collection descriptor
	 * 
	 * @return the new collection identifier, or null if insufficient data is available
	 * 
	 * @throws JAXBException could not create a JAXB instance
	 * @throws SPECCHIOFactoryException	database error
	 */
	public String exportCollectionXML(RDACollectionDescriptor rdaCollectionDescriptor)
			throws JAXBException, SPECCHIOFactoryException
	{
		// reset error list
		errors.clear();
		
		// make sure that we have the full details of the principal investigator
		User pi = userFactory.getUser(rdaCollectionDescriptor.getPrincipalInvestigator().getUsername());
		if (pi == null) {
			// the alleged principal investigator does not exist; give up now
			errors.add(
				"The principal investigator \"" +
				rdaCollectionDescriptor.getPrincipalInvestigator().getUsername() +
				"\" does not exist. This collection cannot be submitted."
			);
			return null;
		}
		rdaCollectionDescriptor.setPrincipalInvestigator(pi);
	
		// get the first and last dates of the items in the collection
		List<java.util.Date> fromToDateList = obtainFirstLastDates (rdaCollectionDescriptor);
		
		// get the campaigns from which the collection is drawn
		List<Campaign> campaignList = obtainCampaigns(rdaCollectionDescriptor);
		
		// create the "collection" element, modified at the current time
		Collection collection = new Collection();
		collection.setType("collection");
		collection.setDateModified(sdf.format(new java.util.Date()));
		
		// the collection's date of submission to be is the date of the first spectrum in the collection
		Date date = new Date();
		date.setType("dateFrom");
		date.setDateFormat("W3CDTF");
		date.setValue(sdf.format(fromToDateList.get(0)));
		Dates dates = new Dates();
		dates.setType("dc.dateSubmitted");
		dates.setDate(date);
		collection.setDates(dates);
		
		// add identifiers
		collection.setIdentifierList(obtainIdentifierList(rdaCollectionDescriptor));
		
		// set the collection name and description
		collection.setName(obtainCollectionNames(rdaCollectionDescriptor, campaignList));
		collection.setDescription(obtainCollectionDescription(rdaCollectionDescriptor, campaignList));
		
		// set location
		collection.setLocation(obtainLocation(rdaCollectionDescriptor));
		
		// set related objects
		ArrayList<RelatedObject> relatedObjectList = new ArrayList<RelatedObject>();
		relatedObjectList.add(obtainCollector(rdaCollectionDescriptor));
		collection.setRelatedObjectList(relatedObjectList);
		
		// set subjects
		collection.setSubjectList(obtainSubjectList(rdaCollectionDescriptor));
		
		// set coverage
		collection.setCoverage(obtainCoverage(rdaCollectionDescriptor));
		
		// set the citation information
		collection.setCitationInfo(obtainCitationInfo(rdaCollectionDescriptor));
		
		// set related information
		ArrayList<RelatedInfo> relatedInfoList = obtainRelatedInfo(rdaCollectionDescriptor);
		if (relatedInfoList.size() > 0)
		{
			collection.setRelatedInfoList(relatedInfoList);
		}
		
		// set the rights
		collection.setRights(obtainRights(rdaCollectionDescriptor));
		
		if (!hasErrors()) {
			// everything is okay; create a collection identifier
			String collectionId = obtainCollectionIdString(rdaCollectionDescriptor);
			
			// create a new registry object to represent the collection
			RegistryObject ro = new RegistryObject();
			ro.setCollection(collection);
			ro.setKey(collectionId);
			ro.setOriginatingSource("http://researchdata.ands.org.au/registry/orca/register_my_data");

			// the registry object's group is the institute of the PI
			if(pi.getInstitute() != null && pi.getInstitute().getInstituteName() != null && pi.getInstitute().getInstituteName().length() > 0)
			{
				ro.setGroup(pi.getInstitute().getInstituteName());
			}
			
			// put the registry object into a list of length 1
			ArrayList<RegistryObject> registryObjectList = new ArrayList<RegistryObject>();
			registryObjectList.add(ro);
			RegistryObjects ros = new RegistryObjects();
			ros.setRegistryObjectList(registryObjectList);
			
			// create JAXB context and instantiate marshaller
		    JAXBContext context = JAXBContext.newInstance(RegistryObjects.class);
		    Marshaller m = context.createMarshaller();
		    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://ands.org.au/standards/rif-cs/registryObjects http://services.ands.org.au/documentation/rifcs/schema/registryObjects.xsd");
		    m.setProperty("com.sun.xml.bind.namespacePrefixMapper",new MyNsPrefixMapper());
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);


		    // write to File
		    setANDSCollectionFilenameDir(pi, collectionId);
		    m.marshal(ros, andsCollectionFile);
		    
			// export the party information for the collection
			andsPartyExport.exportPartyXML(pi, collectionId, collection);
			
			return collectionId;
		
		} else {
			
			// something went wrong; don't create a collection identifier
			return null;
			
		}
		
	}


}
