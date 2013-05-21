package ch.specchio.db_import_export;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import au.ands.org.researchdata.RDACollectionDescriptor;
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
import ch.specchio.types.Campaign;
import ch.specchio.types.User;

public class ANDSPartyExport {

	private static final String ANDS_PARTY_FILE_NAME_PREFIX = "spectral-party-";
	private static final String XML_FILE_POSTFIX = ".xml";
	private static final String PARTY_PREFIX = "uow.edu.au/PTY/SL/";
	
	private UserFactory userFactory;
	private SpecchioCampaignFactory specchioCampaignFactory;
	private String andsXMLFileLocation;
//	private String ANDSPartyFileNameDirString;
	private File andsPartyFile;
//	private FileReader andsFileReader;
	
	public void initialize( String dbUser, String dbPassword, String _andsXMLFileLocation) {
		try {
			userFactory = new UserFactory( dbUser, dbPassword);  
			specchioCampaignFactory = new SpecchioCampaignFactory(dbUser, dbPassword);
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
		try {
System.out.println("###############################################################The exportPartyXML() ");
//			User user = userFactory.getUser("sdb_admin");
			User userSpecchio = userFactory.getUser(rdaCollectionDescriptor.getPrincipalInvestigator().getUsername());
			RegistryObject ro = new RegistryObject();
			ro.setGroup(userSpecchio.getInstitute().getInstituteName());
			ro.setKey(userSpecchio.getExternalId());
			ro.setOriginatingSource("http://researchdata.ands.org.au/registry/orca/register_my_data");
			ArrayList<RegistryObject> registryObjectList = new ArrayList<RegistryObject>();
			registryObjectList.add(ro);
			RegistryObjects ros = new RegistryObjects();
			ros.setRegistryObjectList(registryObjectList);
			Name name = new Name();
			name.setType("primary");
			NamePart namePart = new NamePart();
			namePart.setType("family");
			namePart.setValue(userSpecchio.getLastName());
			NamePart namePart1 = new NamePart();
			namePart1.setType("given");
			namePart1.setValue(userSpecchio.getFirstName());
			ArrayList<NamePart> namePartList = new ArrayList<NamePart>();
			namePartList.add(namePart);
			namePartList.add(namePart1);
			name.setNamePartList(namePartList);
			Location location = new Location();
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
			Party party = new Party();
			party.setType("person");
			party.setDateModified(new Date());
			party.setName(name);
			party.setLocation(location);
			Relation relation = new Relation();
			relation.setType("isCollectorOf");
			Campaign [] campaigns = specchioCampaignFactory.getCampaigns();
			Campaign campaign = specchioCampaignFactory.getCampaign(campaigns[0].getId());
			relation.setDescription(campaign.getDescription());
//			relation.setDescription("Leaf spectral reflectance of seven Australian native vegetation species");
			RelatedObject relatedObject = new RelatedObject();
			relatedObject.setKey("uow.edu.au/SL/COL/01");
			relatedObject.setRelation(relation);
			party.setRelatedObject(relatedObject);
			ArrayList<Subject> subjectList = new ArrayList<Subject>();
			Subject subject1 = new Subject();
			subject1.setType("anzsrc-for");
			subject1.setValue("0501");
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
			party.setSubjectList(subjectList);
			Description description = new Description();
			description.setType("brief");
			description.setValue("&lt;p class=\"p1\"&gt; Xiao Shang is currently undertaking his PhD at the University of Wollongong under the supervision of Dr. Laurie Chisholm. His research is in the field of conservation biology and environmental management. Xiao&amp;#39;s thesis title is &amp;quot;Discrimination of Australian native vegetation species using hyper-spectral remote sensing and object-oriented analysis, Beecroft Peninsula, Jervis Bay&amp;quot;.&lt;/p&gt;");
			party.setDescription(description);
			ArrayList<RelatedInfo> relatedInfoList = new ArrayList<RelatedInfo>();
			Identifier identifier1 = new Identifier();
			identifier1.setType("local");
			identifier1.setValue("Shang, X. Chisholm LA, Datt B 2011 'Assessment of multiple approaches to forest vegetation classification', in Proceedings of IAG, University of Wollongong, Wollongong");
			RelatedInfo relatedInfo1 = new RelatedInfo();
			relatedInfo1.setType("publication");
			relatedInfo1.setIdentifier(identifier1);
			relatedInfoList.add(relatedInfo1);
			Identifier identifier2 = new Identifier();
			identifier2.setType("local");
			identifier2.setValue("Shang, X. Chisholm LA, Datt B 2011 'Classification of vegetation species at leaf level using hyperspectral reflectance data and SVM', paper presented at the 34th ISRSE, Sydney");
			RelatedInfo relatedInfo2 = new RelatedInfo();
			relatedInfo2.setIdentifier(identifier2);
			relatedInfoList.add(relatedInfo2);
			party.setRelatedInfoList(relatedInfoList);
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

}
