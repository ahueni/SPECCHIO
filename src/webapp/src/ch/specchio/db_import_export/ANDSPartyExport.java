package ch.specchio.db_import_export;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import au.ands.org.researchdata.RDACollectionDescriptor;
import ch.specchio.factories.MetadataFactory;
import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.factories.SpecchioCampaignFactory;
import ch.specchio.factories.UserFactory;
import ch.specchio.model.Address;
import ch.specchio.model.AddressPart;
import ch.specchio.model.Description;
import ch.specchio.model.ElectronicAddress;
import ch.specchio.model.Identifier;
import ch.specchio.model.Location;
import ch.specchio.model.Name;
import ch.specchio.model.NamePart;
import ch.specchio.model.Party;
import ch.specchio.model.PhysicalAddress;
import ch.specchio.model.RegistryObject;
import ch.specchio.model.RegistryObjects;
import ch.specchio.model.RelatedInfo;
import ch.specchio.model.RelatedObject;
import ch.specchio.model.Relation;
import ch.specchio.model.Subject;
import ch.specchio.services.BadRequestException;
import ch.specchio.types.Campaign;
import ch.specchio.types.User;

public class ANDSPartyExport {

	private static final String ANDS_PARTY_FILE_NAME_PREFIX = "spectral-party-";
	private static final String XML_FILE_POSTFIX = ".xml";
	private static final String PARTY_PREFIX = "uow.edu.au/PTY/SL/";
	private static final String COLLECTION_PREFIX = "uow.edu.au/SL/COL/";
	
	private UserFactory userFactory;
	private SpecchioCampaignFactory specchioCampaignFactory;
	private String andsXMLFileLocation;
	private MetadataFactory metadataFactory;
//	private String ANDSPartyFileNameDirString;
	private File andsPartyFile;
//	private FileReader andsFileReader;
	
	public void initialize( String dbUser, String dbPassword, String dataSourceName, String _andsXMLFileLocation) {
		try {
			userFactory = new UserFactory( dbUser, dbPassword, dataSourceName);
			metadataFactory = new MetadataFactory(dbUser, dbPassword, dataSourceName);
			specchioCampaignFactory = new SpecchioCampaignFactory(dbUser, dbPassword, dataSourceName);
			andsXMLFileLocation = _andsXMLFileLocation;
			//setANDSPartyFilenameDir( andsXMLFileLocation);
		} catch (SPECCHIOFactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean createFileAndDirectory( String directoryString, User userSpecchio) 
	{
		File andsPartyFileNameDir = new File( directoryString);
		andsPartyFileNameDir.mkdirs();
		boolean directoryExists = andsPartyFileNameDir.exists();
	    System.out.println("fileName 0.11: directoryCreated.exists() is: " + directoryExists);
	    
	    if(!directoryExists)
	    	return false;
	    
	    String fileName = directoryString + ANDS_PARTY_FILE_NAME_PREFIX + userSpecchio.getExternalId().substring(PARTY_PREFIX.length(), userSpecchio.getExternalId().length()) + XML_FILE_POSTFIX;
	    andsPartyFile = new File( fileName);
	    
	    return true;
//		andsFileReader =  new FileReader( fileName);
	}
	
	/**
	 * This method creates the file in the xml file location that is taken in from the web.xml. If the directory supplied does not exist then the 
	 * file is written to the home directory.
	 * @param userSpecchio Specchio user object
	 * @param collectionIdString id string of a collection
	 */
	private void setANDSPartyFilenameDir( User userSpecchio)
	{			
		if(!createFileAndDirectory( andsXMLFileLocation + System.getProperty("file.separator"), userSpecchio))
		{
			createFileAndDirectory( System.getProperty("user.home") + andsXMLFileLocation + System.getProperty("file.separator"), userSpecchio);
	    } 
	}
	
	public void exportPartyXML( RDACollectionDescriptor rdaCollectionDescriptor)
	{
		String errorString = new String("");
		try {
//System.out.println("###############################################################The exportPartyXML() ");
//			User user = userFactory.getUser("sdb_admin");
			User userSpecchio = userFactory.getUser(rdaCollectionDescriptor.getPrincipalInvestigator().getUsername());
			RegistryObject ro = new RegistryObject();
			if(userSpecchio.getInstitute() != null && userSpecchio.getInstitute().getInstituteName() != null && userSpecchio.getInstitute().getInstituteName().length() > 0)
			{
				ro.setGroup(userSpecchio.getInstitute().getInstituteName());
			}
			if( userSpecchio.getExternalId() != null )
			{
				ro.setKey(userSpecchio.getExternalId());
			}
			else
			{
				errorString += "ERROR: party id MUST be provided!!!";
			}
			ro.setOriginatingSource("http://researchdata.ands.org.au/registry/orca/register_my_data");
			ArrayList<RegistryObject> registryObjectList = new ArrayList<RegistryObject>();
			registryObjectList.add(ro);
			RegistryObjects ros = new RegistryObjects();
			ros.setRegistryObjectList(registryObjectList);
			Name name = new Name();
			name.setType("primary");
			ArrayList<NamePart> namePartList = new ArrayList<NamePart>();
			if(userSpecchio.getLastName() != null && userSpecchio.getLastName().length() > 0)
			{
				NamePart familyNamePart = new NamePart();
				familyNamePart.setType("family");
				familyNamePart.setValue(userSpecchio.getLastName());
				namePartList.add(familyNamePart);
			}
			if(userSpecchio.getFirstName() != null && userSpecchio.getFirstName().length() > 0)
			{
				NamePart givenNamePart = new NamePart();
				givenNamePart.setType("given");
				givenNamePart.setValue(userSpecchio.getFirstName());
				namePartList.add(givenNamePart);
			}
			if(namePartList.size() > 0)
			{
				name.setNamePartList(namePartList);
			}
			boolean userDescriptionBoolean = false;
			Description description = new Description();
//			description.setValue("My description is this description");
			if(userSpecchio.getDescription() != null && userSpecchio.getDescription().length() > 0)
			{
				description.setType("brief");
				description.setValue(userSpecchio.getDescription());
				userDescriptionBoolean = true;
			}

			Location location = new Location();
			Address address = new Address();
			boolean emailAddressBoolean = false;
			boolean physicalAddressBoolean = false;
			if( userSpecchio.getEmailAddress() != null && userSpecchio.getEmailAddress().length() > 0)
			{
				ElectronicAddress electronicAddress = new ElectronicAddress();
				electronicAddress.setType("email");
				electronicAddress.setValue(userSpecchio.getEmailAddress());
				address.setElectronicAddress(electronicAddress);
				emailAddressBoolean = true;
			}
			if( (userSpecchio.getInstitute() != null 
					&& ( userSpecchio.getInstitute().getDepartment() != null && userSpecchio.getInstitute().getDepartment().length() > 0)
						||  (userSpecchio.getInstitute().getInstituteName() != null && userSpecchio.getInstitute().getInstituteName().length() > 0)))
			{
				PhysicalAddress physicalAddress = new PhysicalAddress();
				physicalAddress.setType("streetAddress");
				AddressPart addressPart = new AddressPart();
				addressPart.setType("text");
				String addressString = "";
				if( userSpecchio.getInstitute().getDepartment() != null)
				{
					addressString += userSpecchio.getInstitute().getDepartment();
					addressString += " ";
				}
				if( userSpecchio.getInstitute().getInstituteName() != null)
				{
					addressString += userSpecchio.getInstitute().getInstituteName();
				}
				addressPart.setValue(addressString);
				physicalAddress.setAddressPart(addressPart);
				address.setPhysicalAddress(physicalAddress);
				physicalAddressBoolean = true;
			}

			location.setAddress(address);
			Party party = new Party();
			party.setType("person");
			party.setDateModified(new Date());
			if( namePartList.size() > 0 )
			{
				party.setName(name);
			}
			if(userDescriptionBoolean)
			{
				party.setDescription(description);
			}
			if(physicalAddressBoolean || emailAddressBoolean)
			{
				party.setLocation(location);
			}
			
			Relation relation = new Relation();
			relation.setType("isCollectorOf");
			Campaign [] campaigns = specchioCampaignFactory.getCampaigns();
			Campaign campaign = specchioCampaignFactory.getCampaign(campaigns[0].getId());
			relation.setDescription(campaign.getDescription());
//			relation.setDescription("Leaf spectral reflectance of seven Australian native vegetation species");
			RelatedObject relatedObject = new RelatedObject();
			relatedObject.setKey(COLLECTION_PREFIX + campaigns[0].getId());
			relatedObject.setRelation(relation);
			party.setRelatedObject(relatedObject);
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
			ArrayList<Subject> subjectList = new ArrayList<Subject>();
			boolean addForCodeBoolean = false;
			Subject forCodeSubject = new Subject();
			try {
				forCodeSubject.setType("anzsrc-for");
				forCodeSubject.setValue( metadataFactory.getTaxonomyObject(((Long) metadataFactory.getMetadataForSpectrum(spectrumId).get_first_entry("FOR Code").getValue()).intValue()).getCode() );
				addForCodeBoolean = true;
			} catch (Exception e) {
				// don't do anything its not a mandatory field
			}
			if (addForCodeBoolean && forCodeSubject.getValue() != null && forCodeSubject.getValue().length() > 0)
			{
				subjectList.add(forCodeSubject);
				party.setSubjectList(subjectList);
			}

//			Description description = new Description();
//			description.setType("brief");
//			description.setValue("&lt;p class=\"p1\"&gt; Xiao Shang is currently undertaking his PhD at the University of Wollongong under the supervision of Dr. Laurie Chisholm. His research is in the field of conservation biology and environmental management. Xiao&amp;#39;s thesis title is &amp;quot;Discrimination of Australian native vegetation species using hyper-spectral remote sensing and object-oriented analysis, Beecroft Peninsula, Jervis Bay&amp;quot;.&lt;/p&gt;");
//			party.setDescription(description);
			ArrayList<RelatedInfo> relatedInfoList = new ArrayList<RelatedInfo>();
//			Identifier identifierLocalPublication = new Identifier();
//			identifierLocalPublication.setType("local");
//			identifierLocalPublication.setValue("Shang, X. Chisholm LA, Datt B 2011 'Assessment of multiple approaches to forest vegetation classification', in Proceedings of IAG, University of Wollongong, Wollongong");
//			RelatedInfo relatedInfoPublication = new RelatedInfo();
//			relatedInfoPublication.setType("publication");
//			relatedInfoPublication.setIdentifier(identifierLocalPublication);
//			relatedInfoList.add(relatedInfoPublication);
			Identifier identifierPublication = new Identifier();
			boolean addIdentifier2Boolean = true;
			try {
				identifierPublication.setType("local");
				identifierPublication.setValue( (String) metadataFactory.getMetadataForSpectrum(spectrumId).get_first_entry("Publication").getValue() );
				RelatedInfo relatedInfo2 = new RelatedInfo();
				relatedInfo2.setIdentifier(identifierPublication);
				relatedInfoList.add(relatedInfo2);
			} catch (Exception e) {
				addIdentifier2Boolean = false;
			}
			if (addIdentifier2Boolean && identifierPublication.getValue() != null && identifierPublication.getValue().length() > 0) 
			{
				party.setRelatedInfoList(relatedInfoList);	
			}
			
			ro.setParty(party);

			
		    // create JAXB context and instantiate marshaller
		    JAXBContext context = JAXBContext.newInstance(RegistryObjects.class);
		    Marshaller m = context.createMarshaller();
		    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://ands.org.au/standards/rif-cs/registryObjects http://services.ands.org.au/documentation/rifcs/schema/registryObjects.xsd");
		    m.setProperty("com.sun.xml.bind.namespacePrefixMapper",new MyNsPrefixMapper());
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		    // Write to System.out
//		    m.marshal(ros, System.out);

		    // Write to File
		    setANDSPartyFilenameDir( userSpecchio);
		    
		    m.marshal( ros, andsPartyFile);

		    // get variables from our xml file, created before
//		    Unmarshaller um = context.createUnmarshaller();
//		    RegistryObjects registryObjects = (RegistryObjects) um.unmarshal( andsFileReader);
//		    ArrayList<RegistryObject> list = registryObjects.getRegistryObjectsList();
//		    for (RegistryObject registryObject : list) {
//		      System.out.println("RegistryObject -> key: " + registryObject.getKey() + " originating source "
//		          + registryObject.getOriginatingSource());
//		    }
		    
		    checkErrorString(errorString);
		  
			
		} catch (SPECCHIOFactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    void checkErrorString(String errorString)
    {
        if (errorString.length() > 0)
            throw new BadRequestException(errorString);
    }

}
