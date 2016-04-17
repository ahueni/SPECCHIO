package ch.specchio.db_import_export;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	private List<String> errors;
	
	
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
		errors = new ArrayList<String>();
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
	 * Build the description element for a user.
	 * 
	 * @param user	the user
	 * 
	 * @return a new Description element, or null if the user does not have a suitable description
	 */
	private Description obtainDescription(User user) {

		if(user.getDescription() != null && user.getDescription().length() > 0)
		{	
			Description description = new Description();
			description.setType("brief");
			description.setValue(user.getDescription());
			return description;
		} else {
			return null;
		}
		
	}
	
	/**
	 * Build the Location element for a user.
	 * 
	 * @param user	the user
	 * 
	 * @return a new Location element, or null if the user does not have any location data
	 */
	private Location obtainLocation(User user) {

		// build the Address element
		Address address = new Address();
		if( user.getEmailAddress() != null && user.getEmailAddress().length() > 0)
		{
			ElectronicAddress electronicAddress = new ElectronicAddress();
			electronicAddress.setType("email");
			electronicAddress.setValue(user.getEmailAddress());
			address.setElectronicAddress(electronicAddress);
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
		}

		// build the Location element
		if(address.getPhysicalAddress() != null || address.getElectronicAddress() != null)
		{
			Location location = new Location();
			location.setAddress(address);
			return location;
		}
		else
		{
			return null;
		}
		
	}
	
	/**
	 * Build the Name element for a user.
	 * 
	 * @param user	the user
	 * 
	 * @return a new Name element, or null if the user does not have a name
	 */
	private Name obtainName(User user) {
		
		// build the NamePartList element for this user
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
		
		// put the name part list into a Name element
		if(namePartList.size() > 0)
		{
			Name name = new Name();
			name.setType("primary");
			name.setNamePartList(namePartList);
			return name;
		} else {
			return null;
		}
		
	}
	
	/**
	 * Export a RIF-CS file describing the collector of a collection.
	 * 
	 * @param user			the SPECCHIO user to be exported
	 * @param collectionId	the collection identifier
	 * @param collection	the ANDS collection object
	 * 
	 * @return the user's ANDS party identifier, or null if there was an error
	 * 
	 * @throws JAXBException could not create JAXB instance
	 */
	public String exportPartyXML(User user, String collectionId, Collection collection)
		throws JAXBException
	{
		// reset the error ist
		errors.clear();
		
		// check that the user has an ANDS party identifier, and give up if not
		if (user.getExternalId() == null || user.getExternalId().length() == 0) {
			errors.add(obtainMissingFieldString(user, "an ANDS party identifier."));
			return null;
		}

		// create a party element
		Party party = new Party();
		party.setType("person");
		party.setDateModified(new Date());
		
		// set the party name
		Name name = obtainName(user);
		party.setName(name);
		if (name == null) {
			errors.add(obtainMissingFieldString(user, "a name"));
		}
		
		// set the party description
		Description description = obtainDescription(user);
		party.setDescription(description);
		if (description == null) {
			errors.add(obtainMissingFieldString(user, "a description"));
		}
		
		// set the party location
		Location location = obtainLocation(user);
		party.setLocation(location);
		if (location == null || location.getAddress().getElectronicAddress() == null) {
			errors.add(obtainMissingFieldString(user, "an e-mail address"));
		}
		if (location == null || location.getAddress().getPhysicalAddress() == null) {
			errors.add(obtainMissingFieldString(user, "an institution"));
		}
		
		// add the relation with the collection
		Relation relation = new Relation();
		relation.setType("isCollectorOf");
		relation.setDescription(collection.getName().toString());
		RelatedObject relatedObject = new RelatedObject();
		relatedObject.setKey(collectionId);
		relatedObject.setRelation(relation);
		party.setRelatedObject(relatedObject);
		party.setSubjectList(collection.getSubjectList());
		party.setRelatedInfoList(collection.getRelatedInfoList());
		
		if (!hasErrors()) {
			
			// create registry object for the party
			RegistryObject ro = new RegistryObject();
			ro.setGroup(user.getInstitute().getInstituteName());
			ro.setKey(user.getExternalId());
			ro.setOriginatingSource("http://researchdata.ands.org.au/registry/orca/register_my_data");
			ro.setParty(party);
			
			// put the registry object into a list
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
			setANDSPartyFilenameDir(user);
			m.marshal(ros, andsPartyFile);
			
			return ro.getKey();
			
		} else {
			
			return null;
			
		}
	  
	}
	
	/**
	 * Get the list of errors from the last invocation of exportPartyXML().
	 * 
	 * @return a list of strings describing all of the error found
	 */
	public List<String> getErrors() {
		
		return errors;
		
	}
	
	/**
	 * Check whether or not there were errors in the last invocation of exportPartyXML().
	 * 
	 * @return true or false
	 */
	public boolean hasErrors() {
		
		return !errors.isEmpty();
		
	}
	
	/**
	 * Build an error string for a missing field.
	 * 
	 * @param user		the user for which the error is to be reported
	 * @param field		the field name that is missing, including "a" or "an"
	 * 
	 * @return a string describing the error and how to fix it
	 */
	private String obtainMissingFieldString(User user, String field) {
		
		return String.format(
				"User \"%s\" does not have %s. Please add one.",
				user.getUsername(),
				field
			);
		
	}

}
