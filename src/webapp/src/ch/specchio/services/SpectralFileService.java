package ch.specchio.services;


import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.annotation.security.*;

import ch.specchio.constants.UserRoles;
import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.factories.SpectralFileFactory;
import ch.specchio.jaxb.XmlBoolean;
import ch.specchio.jaxb.XmlInteger;
import ch.specchio.types.SpectralFile;
import ch.specchio.types.SpectralFileInsertResult;


/**
 * Spectral file services.
 */
@Path("/spectral_file")
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
public class SpectralFileService extends SPECCHIOService {
	
	/**
	 * Test whether or not a given spectral file exists in the database.
	 * 
	 * @param descriptor	the descriptor o the spectrum file to test
	 * 
	 * @return true if the spectrum exists, or false if not
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@Path("exists")
	public XmlBoolean exists(SpectralFile spec_file) throws SPECCHIOFactoryException {
		
		SpectralFileFactory factory = new SpectralFileFactory(
				getClientUsername(),
				getClientPassword(),
				spec_file.getCampaignType(),
				spec_file.getCampaignId()				
			);
				
		
		boolean b = factory.spectrumExists(spec_file, spec_file.getHierarchyId());
		factory.dispose();
		
		return  new XmlBoolean(b);
	}	
	
	
	/**
	 * Get the identifier for a given file format.
	 * 
	 * @param file_format_name	the name of the file format
	 * 
	 * @return the identifier of the given file format
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("file_format_id/{file_format_name}")
	public XmlInteger file_format_id(@PathParam("file_format_name") String file_format_name) throws SPECCHIOFactoryException {
		
		SpectralFileFactory factory = new SpectralFileFactory(
				getClientUsername(),
				getClientPassword()
			);
		int id = factory.getIdForFileFormat(file_format_name);
		factory.dispose();
		
		return new XmlInteger(id);
		
	}
	
	
	/**
	 * Insert a spectral file into the database.
	 * 
	 * @param spec_file	the file to be inserted
	 * 
	 * @return a list of spectrum identifiers created by the insertion
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	@POST
	@Path("insert")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public SpectralFileInsertResult insert(SpectralFile spec_file) throws SPECCHIOFactoryException {

		SpectralFileFactory factory = new SpectralFileFactory(
				getClientUsername(),
				getClientPassword(),
				spec_file.getCampaignType(),
				spec_file.getCampaignId()
			);
		SpectralFileInsertResult insert_result = factory.insertSpectralFile(spec_file, spec_file.getHierarchyId());
		factory.dispose();
		
//		XmlIntegerAdapter adapter = new XmlIntegerAdapter();
//		return adapter.marshalArray(ids);
		return insert_result;
	}

}
