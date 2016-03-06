package ch.specchio.services;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import au.ands.org.researchdata.RDACollectionDescriptor;
import ch.specchio.db_import_export.ANDSCollectionExport;
import ch.specchio.db_import_export.ANDSPartyExport;
import ch.specchio.types.MetaParameterFormatException;

/**
 * Services supporting the features for use with the Australian National Data Service.
 */
@Path("/ands")
public class ANDSService extends SPECCHIOService {

	private ANDSPartyExport andsPartyExport;
	private ANDSCollectionExport andsCollectionExport;
	
	/**
	 * Submit a collection for inclusion in ResearchDataAustralia
	 *
	 * @param collection_d	the collection descriptor
	 * @param is_admin	is the user an administrator?
	 * 
	 * @return the new collection identifier, or an empty string if publication failed
	 */
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Path("submitCollection")
	public String submitCollection(RDACollectionDescriptor collection_d, boolean is_admin) {
		
		String collectionId = null;
		String andsXMLFileLocation = getRequest().getServletContext().getInitParameter("ANDSXMLFileLocation");
		
		andsPartyExport = new ANDSPartyExport();
		andsPartyExport.initialize( getClientUsername(), getClientPassword(), getDataSourceName(), andsXMLFileLocation);
		andsPartyExport.exportPartyXML( collection_d, is_admin);

		andsCollectionExport = new ANDSCollectionExport();
		andsCollectionExport.initialize( getClientUsername(), getClientPassword(), getDataSourceName(), andsXMLFileLocation);
		try {
			collectionId = andsCollectionExport.exportCollectionXML( collection_d, is_admin);
		} catch (MetaParameterFormatException e) {
			e.printStackTrace();
		}
		
		return (collectionId != null)? collectionId : "";
	}

}
