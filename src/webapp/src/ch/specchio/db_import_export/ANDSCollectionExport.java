package ch.specchio.db_import_export;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import au.ands.org.researchdata.RDACollectionDescriptor;
import ch.specchio.eav_db.EAVDBServices;
import ch.specchio.factories.MetadataFactory;
import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.factories.SpecchioCampaignFactory;
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
import ch.specchio.types.User;
import ch.specchio.types.attribute;

public class ANDSCollectionExport {

	private static final String ANDS_COLLECTION_FILE_NAME_PREFIX = "spectral-collection-";
	private static final String XML_FILE_POSTFIX = ".xml";
	private static final String COLLECTION_PREFIX = "uow.edu.au/SL/COL/";
	
	private UserFactory userFactory;
	private SpecchioCampaignFactory specchioCampaignFactory;
	private String andsXMLFileLocation;
	private MetadataFactory metadataFactory;
	private EAVDBServices eavDBServices;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//	private String ANDSCollectionFileNameDirString;
	private File andsCollectionFile;
//	private FileReader andsFileReader;
	
	public void initialize( String dbUser, String dbPassword, String _andsXMLFileLocation) {
		try {
			userFactory = new UserFactory( dbUser, dbPassword);
			specchioCampaignFactory = new SpecchioCampaignFactory(dbUser, dbPassword);
			metadataFactory = new MetadataFactory(dbUser, dbPassword);
			eavDBServices = new EAVDBServices( userFactory.getStatementBuilder() , userFactory.getAttributes(), userFactory.getDatabaseUserName());
			andsXMLFileLocation = _andsXMLFileLocation;
		} catch (SPECCHIOFactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean createFileAndDirectory( String baseDirectoryString, User userSpecchio, String collectionIdString) 
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
	 * This method creates the file in the xml file location that is taken in from the web.xml. If the directory supplied does not exist then the 
	 * file is written to the home directory.
	 * @param userSpecchio Specchio user object
	 * @param collectionIdString id string of a collection
	 */
	private void setANDSCollectionFilenameDir( User userSpecchio, String collectionIdString)
	{	
		if(!createFileAndDirectory( andsXMLFileLocation + System.getProperty("file.separator"), userSpecchio, collectionIdString))
		{
		    createFileAndDirectory( System.getProperty("user.home") + andsXMLFileLocation + System.getProperty("file.separator"), userSpecchio, collectionIdString);
		} 
	}
	
	/**
	 * @param rdaCollectionDescriptor 
	 * @return an ordered list of Dates based on acquisition dates for a spectra.
	 * @throws SPECCHIOFactoryException
	 */
	private List<java.util.Date> obtainFirstLastDates (RDACollectionDescriptor rdaCollectionDescriptor) throws SPECCHIOFactoryException
	{
		List<java.util.Date> fromToDates = new ArrayList<java.util.Date> ();
		int [] spectrumIds = rdaCollectionDescriptor.getSpectrumIds();

		// add all dates
		for (int spectrumId: spectrumIds)
		{
			System.out.println("spectrumIds is: " + spectrumId);
			fromToDates.add(((java.util.Date) metadataFactory.getMetadataForSpectrum(spectrumId).get_entry("Acquisition Time").getValue()));
		}
		
		java.util.Collections.sort(fromToDates);

		return fromToDates;
	}
	
	private String obtainCollectionIdString(RDACollectionDescriptor rdaCollectionDescriptor) throws MetaParameterFormatException, SPECCHIOFactoryException
	{
		attribute attr = eavDBServices.ATR.get_attribute_info((eavDBServices.ATR.get_attribute_id("ANDS Collection Key")));
		MetaParameter mp = MetaParameter.newInstance(attr);
		int collectionId = metadataFactory.updateMetadata(mp, convertIntArray( rdaCollectionDescriptor.getSpectrumIds()));
		String collectionIdString = COLLECTION_PREFIX + collectionId;
		mp.setValueFromString(collectionIdString);
		metadataFactory.updateMetadata(mp, convertIntArray( rdaCollectionDescriptor.getSpectrumIds()));
		
		return collectionIdString;
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
	
	public String exportCollectionXML(RDACollectionDescriptor rdaCollectionDescriptor) throws MetaParameterFormatException
	{
		try {
			List<java.util.Date> fromToDateList = obtainFirstLastDates (rdaCollectionDescriptor);
			
			User userSpecchio = userFactory.getUser(rdaCollectionDescriptor.getPrincipalInvestigator().getUsername());
			RegistryObject ro = new RegistryObject();
			ro.setGroup(userSpecchio.getInstitute().getInstituteName());
			String collectionIdString = obtainCollectionIdString(rdaCollectionDescriptor);
			ro.setKey( collectionIdString);
			ro.setOriginatingSource("http://researchdata.ands.org.au/registry/orca/register_my_data");
			ArrayList<RegistryObject> registryObjectList = new ArrayList<RegistryObject>();
			registryObjectList.add(ro);
			RegistryObjects ros = new RegistryObjects();
			ros.setRegistryObjectList(registryObjectList);
			Collection collection = new Collection();
			collection.setType("collection");
			collection.setDateModified(sdf.format(new java.util.Date()));
			
			ArrayList<Identifier> identifierList = new ArrayList<Identifier>();
			Identifier identifier = new Identifier();
			identifier.setType("uri");
			identifier.setValue("http://hdl.handle.net/102.100.100/9338");
			identifierList.add(identifier);
			
			Identifier doiIdentifier = new Identifier();	
			int spectrumId = -1;
			if( rdaCollectionDescriptor.getSpectrumIds().length > 0)
			{
				int [] spectrumIds = rdaCollectionDescriptor.getSpectrumIds();
				for (int i=0; i < spectrumIds.length; i++)
				{
					System.out.println("spectrumIds is: " + spectrumIds[i]);
					spectrumId = spectrumIds[i];
				}
			}
			// check if doi has a value and if it does include it in the rif-cs collection file as an identifer
			String doiIdentifierString = "";
			try {
				doiIdentifierString = (String) metadataFactory.getMetadataForSpectrum(spectrumId).get_entry("Digital Object Identifier").getValue();
			} catch (Exception e) {
				// dont do anything, just means no data to retrieve
			}
			
			if (doiIdentifierString != null && doiIdentifierString.length() > 0 )
			{
				doiIdentifier.setType("doi");
				doiIdentifier.setValue( doiIdentifierString );
				identifierList.add(doiIdentifier);
			}
			collection.setIdentifierList(identifierList);
			
			Date date = new Date();
			date.setType("dateFrom");
			date.setDateFormat("W3CDTF");
			date.setValue(sdf.format(fromToDateList.get(0)));
			Dates dates = new Dates();
			dates.setType("dc.dateSubmitted");
			dates.setDate(date);
			collection.setDates(dates);
			Name name = new Name();
			name.setType("primary");
//			name.setGiven(userSpecchio.getFirstName());
//			name.setFamily(userSpecchio.getLastName());
			NamePart namePart = new NamePart();
			namePart.setType("primary");
			namePart.setValue("Leaf spectral reflectance of seven Australian native vegetation species");
			ArrayList<NamePart> namePartList = new ArrayList<NamePart>();
			namePartList.add(namePart);
			name.setNamePartList(namePartList);
			collection.setName(name);
			Location location = new Location();
			location.setDateFrom(sdf.format(fromToDateList.get(0)));
			location.setDateTo(sdf.format(fromToDateList.get(fromToDateList.size()-1)));
			Address address = new Address();
			ElectronicAddress electronicAddress = new ElectronicAddress();
			electronicAddress.setType("email");
			electronicAddress.setValue(userSpecchio.getEmailAddress());
			address.setElectronicAddress(electronicAddress);
			PhysicalAddress physicalAddress = new PhysicalAddress();
			physicalAddress.setType("streetAddress");
			AddressPart addressPart = new AddressPart();
			addressPart.setType("text");
			addressPart.setValue(userSpecchio.getInstitute().getDepartment() + " " + userSpecchio.getInstitute().getInstituteName());
			physicalAddress.setAddressPart(addressPart);
			address.setPhysicalAddress(physicalAddress);
			location.setAddress(address);

			collection.setLocation(location);
//			party.setType("person");
//			party.setDateModified(new Date());
//			party.setName(name);
//			party.setLocation(location);
			Relation relation = new Relation();
			relation.setType("hasCollector");
			Campaign [] campaigns = specchioCampaignFactory.getCampaigns();
			Campaign campaign = specchioCampaignFactory.getCampaign(campaigns[0].getId());
			relation.setDescription(campaign.getDescription());
//			relation.setDescription("Leaf spectral reflectance of seven Australian native vegetation species");
			RelatedObject relatedObject = new RelatedObject();
			relatedObject.setKey(rdaCollectionDescriptor.getPrincipalInvestigator().getExternalId());
			relatedObject.setRelation(relation);
			ArrayList<RelatedObject> relatedObjectList = new ArrayList<RelatedObject>();
			relatedObjectList.add(relatedObject);
			collection.setRelatedObjectList(relatedObjectList);
			ArrayList<Subject> subjectList = new ArrayList<Subject>();
			Subject subject1 = new Subject();
			subject1.setType("anzsrc-for");
			subject1.setValue( metadataFactory.getTaxonomyObject(((Long) metadataFactory.getMetadataForSpectrum(spectrumId).get_entry("FOR Code").getValue()).intValue()).getCode());
			subjectList.add(subject1);
			Subject subject2 = new Subject();
			subject2.setType("local");
			subject2.setValue("eucalyptus");
			subjectList.add(subject2);
			Subject subject3 = new Subject();
			subject3.setType("local");
			subject3.setValue("vegetation");
			subjectList.add(subject3);
			Subject subject4 = new Subject();
			subject4.setType("local");
			subject4.setValue("hyperspectral");
			subjectList.add(subject4);
			Subject subject5 = new Subject();
			subject5.setType("local");
			subject5.setValue("remote sensing");
			subjectList.add(subject5);
			collection.setSubjectList(subjectList);
			Description description = new Description();
			description.setType("brief");
			description.setValue("&lt;p class=\"p1\"&gt; Xiao Shang is currently undertaking his PhD at the University of Wollongong under the supervision of Dr. Laurie Chisholm. His research is in the field of conservation biology and environmental management. Xiao&amp;#39;s thesis title is &amp;quot;Discrimination of Australian native vegetation species using hyper-spectral remote sensing and object-oriented analysis, Beecroft Peninsula, Jervis Bay&amp;quot;.&lt;/p&gt;");
			collection.setDescription(description);
			Spatial spatial = new Spatial();
			spatial.setType("text");
			spatial.setValue( (String) metadataFactory.getMetadataForSpectrum(spectrumId).get_entry("Location Name").getValue());
			Temporal temporal = new Temporal();
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
			Coverage coverage = new Coverage();
			coverage.setSpatial(spatial);
			coverage.setTemporal(temporal);
			collection.setCoverage(coverage);
			ArrayList<RelatedInfo> relatedInfoList = new ArrayList<RelatedInfo>();
			Identifier identifier1 = new Identifier();
			identifier1.setType("local");
			identifier1.setValue( (String) metadataFactory.getMetadataForSpectrum(spectrumId).get_entry("Publication").getValue() );
			RelatedInfo relatedInfo1 = new RelatedInfo();
			relatedInfo1.setType("publication");
			relatedInfo1.setIdentifier(identifier1);
			relatedInfoList.add(relatedInfo1);
			collection.setRelatedInfoList(relatedInfoList);
			FullCitation fullCitation = new FullCitation();
			fullCitation.setStyle("Harvard");
//			fullCitation.setValue("Shang, X 2012 Leaf Spectral reflectance of seven Australian native vegetation species. School of Earth and Environmental Sciences, University of Wollongong, Wollongong, N.S.W. http://hdl.handle.net/102.100.100/9338");
			fullCitation.setValue( (String) metadataFactory.getMetadataForSpectrum(spectrumId).get_entry("Citation").getValue());
			CitationInfo citationInfo = new CitationInfo();
			citationInfo.setFullCitation(fullCitation);
			collection.setCitationInfo(citationInfo);
			Rights rights = new Rights();
			rights.setRightsStatement((String) metadataFactory.getMetadataForSpectrum(spectrumId).get_entry("Data Usage Policy").getValue());
			collection.setRights(rights);
			ro.setCollection(collection);
			
		    // create JAXB context and instantiate marshaller
		    JAXBContext context = JAXBContext.newInstance(RegistryObjects.class);
		    Marshaller m = context.createMarshaller();
		    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://ands.org.au/standards/rif-cs/registryObjects http://services.ands.org.au/documentation/rifcs/schema/registryObjects.xsd");
		    m.setProperty("com.sun.xml.bind.namespacePrefixMapper",new MyNsPrefixMapper());
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		    // Write to System.out
//		    m.marshal(ros, System.out);

		    setANDSCollectionFilenameDir( userSpecchio, collectionIdString);
		    
		    // Write to File
		    m.marshal( ros, andsCollectionFile);

		    // get variables from our xml file, created before
//		    Unmarshaller um = context.createUnmarshaller();
//		    RegistryObjects registryObjects = (RegistryObjects) um.unmarshal(andsFileReader);
//		    ArrayList<RegistryObject> list = registryObjects.getRegistryObjectsList();
//		    for (RegistryObject registryObject : list) {
//		      System.out.println("RegistryObject -> key: " + registryObject.getKey() + " originating source "
//		          + registryObject.getOriginatingSource());
//		    }
		    
		    return collectionIdString;
		  
			
		} catch (SPECCHIOFactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
