package ch.specchio.test;

import java.io.File;

import javax.xml.transform.stream.StreamSource;

import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.jaxp13.Validator;


public class MyXMLTestCase extends XMLTestCase {

	private final String myRegistryObjectsXmlSchemaFile = "/home/user/workspace_SPECCHIO_new/SPECCHIO Web Application/xsd/registryObjects.xsd";
	private final String myXmlCollectionDocument = "/home/user/workspace_SPECCHIO_new/glassfish3122eclipsedefaultdomain/config/ands-collection-export-jaxb.xml";
	private final String myXmlPartyDocument = "/home/user/workspace_SPECCHIO_new/glassfish3122eclipsedefaultdomain/config/ands-party-export-jaxb.xml";

    public MyXMLTestCase(String name) {
        super(name);
    }
	
	public void testSchemaValidity() throws Exception {
		Validator v = new Validator();
		v.addSchemaSource(new StreamSource(new File(myRegistryObjectsXmlSchemaFile)));
		boolean isValid = v.isSchemaValid();
		assertTrue( "ERROR: XML schma file is NOT valid", isValid);
	}
	
    public void testForEquality() throws Exception {
        String myControlXML = "<msg><uuid>0x00435A8C</uuid></msg>";
        String myTestXML = "<msg><localId>2376</localId></msg>";
//        assertXMLEqual("comparing test xml to control xml", myControlXML, myTestXML);

        assertXMLNotEqual("test xml not similar to control xml", myControlXML, myTestXML);
    }
	
	public void testXMLColectionFileForLocalSchemaValidity() throws Exception {
		Validator v = new Validator();
		v.addSchemaSource(new StreamSource(new File(myRegistryObjectsXmlSchemaFile)));
		StreamSource is = new StreamSource(new File(myXmlCollectionDocument));
		boolean isValid = v.isInstanceValid(is);
		assertTrue( "ERROR: XML file generated is NOT valid", isValid);
	}
	
	public void testXMLPartyFileForLocalSchemaValidity() throws Exception {
		Validator v = new Validator();
		v.addSchemaSource(new StreamSource(new File(myRegistryObjectsXmlSchemaFile)));
		StreamSource is = new StreamSource(new File(myXmlPartyDocument));
		boolean isValid = v.isInstanceValid(is);
		assertTrue( "ERROR: XML file generated is NOT valid", isValid);
	}
	
}
