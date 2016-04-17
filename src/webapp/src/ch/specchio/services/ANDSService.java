package ch.specchio.services;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import au.ands.org.researchdata.RDACollectionDescriptor;
import ch.specchio.db_import_export.ANDSCollectionExport;
import ch.specchio.factories.SPECCHIOFactoryException;

/**
 * Services supporting the features for use with the Australian National Data Service.
 */
@Path("/ands")
public class ANDSService extends SPECCHIOService {

	private ANDSCollectionExport andsCollectionExport;
	
	/**
	 * Submit a collection for inclusion in ResearchDataAustralia
	 *
	 * @param collection_d	the collection descriptor
	 * 
	 * @return the new collection identifier, or an empty string if publication failed
	 * 
	 * @throws JAXBException could not create JAXB instance
	 * @throws SPECCHIOFactoryException	database error
	 */
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Path("submitCollection")
	public String submitCollection(RDACollectionDescriptor collection_d) throws JAXBException, SPECCHIOFactoryException {
		
		String collectionId = null;
		String andsXMLFileLocation = getRequest().getServletContext().getInitParameter("ANDSXMLFileLocation");
		
		// create and export the collection
		andsCollectionExport = new ANDSCollectionExport(getClientUsername(), getClientPassword(), andsXMLFileLocation);
		collectionId = andsCollectionExport.exportCollectionXML(collection_d);
		if (collectionId == null) {
			// build a string containing all of the error messages
			StringBuffer sb = new StringBuffer();
			for (String error : andsCollectionExport.getErrors()) {
				sb.append(error);
				sb.append("\n");
			}
			throw new BadRequestException(sb.toString());
		}
		
		return collectionId;
	}

}
