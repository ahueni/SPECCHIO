package ch.specchio.db_import_export;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.model.Address;
import ch.specchio.model.AddressPart;
import ch.specchio.model.Collection;
import ch.specchio.model.Description;
import ch.specchio.model.ElectronicAddress;
import ch.specchio.model.Location;
import ch.specchio.model.Name;
import ch.specchio.model.NamePart;
import ch.specchio.model.Party;
import ch.specchio.model.PhysicalAddress;
import ch.specchio.model.RegistryObject;
import ch.specchio.model.RegistryObjects;
import ch.specchio.model.RelatedObject;
import ch.specchio.model.Relation;
import ch.specchio.types.User;

public class ANDSPartyExport {

	private static final String ANDS_PARTY_FILE_NAME_PREFIX = "spectral-party-";
	private static final String XML_FILE_POSTFIX = ".xml";
	private static final String PARTY_PREFIX = "uow.edu.au/PTY/SL/";
	
	private String andsXMLFileLocation;
	private File andsPartyFile;
	
	
	/**
	 *  Constructor.
	 * 
	 * @param _andsXMLFileLocation	the location of the output file
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public ANDSPartyExport(String _andsXMLFileLocation)
		throws SPECCHIOFactoryException {

		andsXMLFileLocation = _andsXMLFileLocation;
	}
	
	private boolean createFileAndDirectory( String directoryString, User user) 
	{
		File andsPartyFileNameDir = new File( directoryString);
		andsPartyFileNameDir.mkdirs();
		boolean directoryExists = andsPartyFileNameDir.exists();
	    System.out.println("fileName 0.11: directoryCreated.exists() is: " + directoryExists);
	    
	    if(!directoryExists)
	    	return false;
	    
	    String fileName = directoryString + ANDS_PARTY_FILE_NAME_PREFIX + user.getExternalId().substring(PARTY_PREFIX.length(), user.getExternalId().length()) + XML_FILE_POSTFIX;
	    andsPartyFile = new File( fileName);
	    
	    return true;
	}
	
	/**
	 * This method creates the file in the xml file location that is taken in from the web.xml. If the directory supplied does not exist then the 
	 * file is written to the home directory.
	 * @param user Specchio user object
	 * @param collectionIdString id string of a collection
	 */
	private void setANDSPartyFilenameDir( User user)
	{			
		if(!createFileAndDirectory( andsXMLFileLocation + System.getProperty("file.separator"), user))
		{
			createFileAndDirectory( System.getProperty("user.home") + andsXMLFileLocation + System.getProperty("file.separator"), user);
	    } 
	}
	
	/**
	 * Export a RIF-CS file describing the collector of a collection.
	 * 
	 * @param user			the SPECCHIO user to be exported
	 * @param collectionId	the collection identifier
	 * @param collection	the ANDS collection object
	 * 
	 * @throws JAXBException could not create JAXB instance
	 */
	public void exportPartyXML(User user, String collectionId, Collection collection)
		throws JAXBException
	{
		RegistryObject ro = new RegistryObject();
		ro.setGroup(user.getInstitute().getInstituteName());
		ro.setKey(user.getExternalId());
		ro.setOriginatingSource("http://researchdata.ands.org.au/registry/orca/register_my_data");
		ArrayList<RegistryObject> registryObjectList = new ArrayList<RegistryObject>();
		registryObjectList.add(ro);
		RegistryObjects ros = new RegistryObjects();
		ros.setRegistryObjectList(registryObjectList);
		Name name = new Name();
		name.setType("primary");
		ArrayList<NamePart> namePartList = new ArrayList<NamePart>();
		if(user.getLastName() != null && user.getLastName().length() > 0)
		{
			NamePart familyNamePart = new NamePart();
			familyNamePart.setType("family");
			familyNamePart.setValue(user.getLastName());
			namePartList.add(familyNamePart);
		}
		if(user.getFirstName() != null && user.getFirstName().length() > 0)
		{
			NamePart givenNamePart = new NamePart();
			givenNamePart.setType("given");
			givenNamePart.setValue(user.getFirstName());
			namePartList.add(givenNamePart);
		}
		if(namePartList.size() > 0)
		{
			name.setNamePartList(namePartList);
		}
		boolean userDescriptionBoolean = false;
		Description description = new Description();
		if(user.getDescription() != null && user.getDescription().length() > 0)
		{
			description.setType("brief");
			description.setValue(user.getDescription());
			userDescriptionBoolean = true;
		}

		Location location = new Location();
		Address address = new Address();
		boolean emailAddressBoolean = false;
		boolean physicalAddressBoolean = false;
		if( user.getEmailAddress() != null && user.getEmailAddress().length() > 0)
		{
			ElectronicAddress electronicAddress = new ElectronicAddress();
			electronicAddress.setType("email");
			electronicAddress.setValue(user.getEmailAddress());
			address.setElectronicAddress(electronicAddress);
			emailAddressBoolean = true;
		}
		if( (user.getInstitute() != null 
				&& ( user.getInstitute().getDepartment() != null && user.getInstitute().getDepartment().length() > 0)
					||  (user.getInstitute().getInstituteName() != null && user.getInstitute().getInstituteName().length() > 0)))
		{
			PhysicalAddress physicalAddress = new PhysicalAddress();
			physicalAddress.setType("streetAddress");
			AddressPart addressPart = new AddressPart();
			addressPart.setType("text");
			String addressString = "";
			if( user.getInstitute().getDepartment() != null)
			{
				addressString += user.getInstitute().getDepartment();
				addressString += " ";
			}
			if( user.getInstitute().getInstituteName() != null)
			{
				addressString += user.getInstitute().getInstituteName();
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
		relation.setDescription(user.getDescription());
		RelatedObject relatedObject = new RelatedObject();
		relatedObject.setKey(collectionId);
		relatedObject.setRelation(relation);
		party.setRelatedObject(relatedObject);
		party.setSubjectList(collection.getSubjectList());
		party.setRelatedInfoList(collection.getRelatedInfoList());
		
		ro.setParty(party);
			
	    // create JAXB context and instantiate marshaller
	    JAXBContext context = JAXBContext.newInstance(RegistryObjects.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://ands.org.au/standards/rif-cs/registryObjects http://services.ands.org.au/documentation/rifcs/schema/registryObjects.xsd");
	    m.setProperty("com.sun.xml.bind.namespacePrefixMapper",new MyNsPrefixMapper());
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

	    // Write to File
	    setANDSPartyFilenameDir( user);
	    
	    m.marshal( ros, andsPartyFile);
	  
	}

}
