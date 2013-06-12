package ch.specchio.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import ch.specchio.constants.UserRoles;
import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.factories.SpaceFactory;
import ch.specchio.factories.SpectrumFactory;
import ch.specchio.jaxb.XmlInteger;
import ch.specchio.jaxb.XmlIntegerAdapter;
import ch.specchio.plots.GonioSamplingPoints;
import ch.specchio.queries.Query;
import ch.specchio.spaces.ReferenceSpaceStruct;
import ch.specchio.spaces.Space;
import ch.specchio.spaces.SpaceQueryDescriptor;
import ch.specchio.spaces.SpectralSpace;
import ch.specchio.types.PictureTable;
import ch.specchio.types.SpectraMetadataUpdateDescriptor;
import ch.specchio.types.Spectrum;
import ch.specchio.types.SpectrumDataLink;
import ch.specchio.types.SpectrumIdsDescriptor;
import ch.specchio.types.SpectrumFactorTable;


/**
 * Spectrum service.
 */
@Path("/spectrum")
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
public class SpectrumService extends SPECCHIOService {
	
	
	/**
	 * Delete target-reference links.
	 * 
	 * @param target_id	the target identifier
	 * 
	 * @return the number of links deleted
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Path("deleteTargetReferenceLinks/{target_id: [0-9]+}")
	@Produces(MediaType.APPLICATION_XML)
	public XmlInteger deleteTargetReferenceLinks(@PathParam("target_id") int target_id)throws SPECCHIOFactoryException {
		
		SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
		int n = factory.deleteTargetReferenceLinks(target_id);
		factory.dispose();
		
		return new XmlInteger(n);
		
	}
	
	
	/**
	 * Get the name of the file from which a spectrum was loaded.
	 * 
	 * @param spectrum_id	the spectrum identifier
	 * 
	 * @return the name of the file, or null if the spectrum does not exist
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Path("filename/{spectrum_id: [0-9]+}")
	@Produces(MediaType.APPLICATION_XML)
	public String filename(@PathParam("spectrum_id") int spectrum_id) throws SPECCHIOFactoryException {
		
		SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
		String filename = factory.getSpectrumFilename(spectrum_id);
		factory.dispose();
		
		return filename;
		
	}
	
	
	/**
	 * Get a spectrum object.
	 * 
	 * @param spectrum_id		the identifier of the desired spectrum
	 * @param prepare_metadata	load the spectrum's metadata?
	 * 
	 * @return a Spectrum object representing the desired spectrum
	 * 
	 * @throws SPECCHIOFactoryException	spectrum_id does not exist
	 */
	@GET
	@Path("get/{spectrum_id: [0-9]+}/{prepare_metadata}")
	@Produces(MediaType.APPLICATION_XML)
	public Spectrum get(
			@PathParam("spectrum_id") int spectrum_id,
			@PathParam("prepare_metadata") String prepare_metadata
		) throws SPECCHIOFactoryException {
		
		SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
		Spectrum s = factory.getSpectrum(spectrum_id, Boolean.valueOf(prepare_metadata));
		factory.dispose();
		
		return s;
		
	}
	
	
	/**
	 * Get Space objects that represent calibration factors
	 * 
	 * @param query_d	the query descriptor
	 * 
	 * @return an array of Space objects representing the calibration factors identified in the query descriptor
	 * 
	 * @throws SPECCHIOFactoryException	databaase error
	 */
	@POST
	@Path("getCalibrationSpaces")
	@Produces(MediaType.APPLICATION_XML)
	public Space[] getCalibrationSpaces(SpaceQueryDescriptor query_d) throws SPECCHIOFactoryException {
		
		SpaceFactory factory = new SpaceFactory(getClientUsername(), getClientPassword());
		ArrayList<Space> spaces = factory.getCalibrationSpaces(query_d.spectrum_ids);
		factory.dispose();
		
		return spaces.toArray(new Space[spaces.size()]);
	}
	
	
	/**
	 * Get the Goniometer sampling points for a space.
	 * 
	 * @param space	the space
	 * 
	 * @return a GonioSamplingPoints object
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@POST
	@Path("getGonioSamplingPoints")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public GonioSamplingPoints getGonioSamplingPoints(SpectralSpace space) throws SPECCHIOFactoryException {
		
		SpaceFactory factory =  new SpaceFactory(getClientUsername(), getClientPassword());
		GonioSamplingPoints sampling_points = factory.getGonioSamplingPoints(space);
		factory.dispose();
		
		return sampling_points;
		
	}
	
	
	/**
	 * Get a reference space.
	 * 
	 * @param query_d	the lists of input (position 1) and output (position 2) identifiers
	 * 
	 * @return a ReferenceSpaceStruct object
	 * 
	 * @throws SPECCHIOFacotryException	database error
	 */
	@POST
	@Path("getReferenceSpace")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public ReferenceSpaceStruct getReferenceSpace(SpectrumIdsDescriptor query_d) throws SPECCHIOFactoryException {
		
		SpaceFactory factory = new SpaceFactory(getClientUsername(), getClientPassword());
		ReferenceSpaceStruct ref_space_struct = factory.getReferenceSpace(query_d.getSpectrumIds(1));
		factory.dispose();
		
		return ref_space_struct;
	}
	
	
	/**
	 * Get Space objects for a given set of spectra.
	 * 
	 * @param query_d	the query descriptor
	 * 
	 * @return an array of Space objects corresponding to the query descriptor
	 *
	 * @throws SPECCHIOFactoryException	could not connect to the database
	 */
	@POST
	@Path("getSpaces")
	@Produces(MediaType.APPLICATION_XML)
	public Space[] getSpaces(SpaceQueryDescriptor query_d) throws SPECCHIOFactoryException {
		
		SpaceFactory factory = new SpaceFactory(getClientUsername(), getClientPassword());
		factory.setMatchOnlySensor(query_d.split_spaces_by_sensor);
		factory.setMatchOnlySensorAndUnit(query_d.split_spaces_by_sensor_and_unit);
		factory.setOrderByAttribute(query_d.order_by);
		ArrayList<Space> spaces = factory.getSpaces(query_d.spectrum_ids);
		factory.dispose();
		
		return spaces.toArray(new Space[spaces.size()]);
	}
	
	
	/**
	 * Get the spectrum factor table.
	 * 
	 * @param factor_d	the spectrum (position 1) and calibration (position 2) identifiers desired
	 * 
	 * @return a table mapping spectra to factors
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	@POST
	@Path("getSpectrumFactorTable")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public SpectrumFactorTable getSpectrumFactorTable(SpectrumIdsDescriptor factor_d) throws SPECCHIOFactoryException {
		
		SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
		SpectrumFactorTable table = factory.getSpectrumFactorTable(factor_d.getSpectrumIds(1), factor_d.getSpectrumIds(2));
		factory.dispose();
		
		return table;
		
	}
	
	
	/**
	 * Get target-reference links that refer to a given target and/or reference.
	 * 
	 * @param target_id		the target identifier (0 to match all targets)
	 * @param reference_id	the reference identifier (0 to match all references)
	 * 
	 * @return an array of SpectrumDataLink objects
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Path("getTargetReferenceLinksByTarget/{target_id: [0-9]+}/{reference_id: [0-9]+}")
	@Produces(MediaType.APPLICATION_XML)
	public SpectrumDataLink[] getTargetReferenceLinksByTarget(
			@PathParam("target_id") int target_id,
			@PathParam("reference_id") int reference_id) throws SPECCHIOFactoryException {
		
		SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
		SpectrumDataLink datalinks[] = factory.getTargetReferenceLinks(target_id, reference_id, getSecurityContext().isUserInRole(UserRoles.ADMIN));
		factory.dispose();
		
		return datalinks;
		
	}
	
	
	
	/**
	 * Insert links from a target to a set of references.
	 * 
	 * @param id_d	a spectrum identifier descriptor with the target in the first list and the references in the second list
	 * 
	 * @return the number of links successfully created
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@POST
	@Path("insertTargetReferenceLinks")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public XmlInteger insertTargetReferenceLinks(SpectrumIdsDescriptor id_d) throws SPECCHIOFactoryException {
		
		int num = 0;
		if (id_d.getSpectrumIds1().size() > 0) { 
			SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
			num = factory.insertTargetReferenceLinks(id_d.getSpectrumId(1, 0), id_d.getSpectrumIds2());
			factory.dispose();
		}
		
		return new XmlInteger(num);
	}
		
	
	
	/**
	 * Load a Space object from the database.
	 * 
	 * @param space	a partially-filled Space object
	 * 
	 * @return the input Space object with all of its fields filled
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@POST
	@Path("loadSpace")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Space loadSpace(Space space) throws SPECCHIOFactoryException {
		
		SpaceFactory factory = new SpaceFactory(getClientUsername(), getClientPassword());
		factory.loadSpace(space);
		factory.dispose();
		
		return space;
		
	}
	
	
	/**
	 * Remove a spectrum from the database.
	 * 
	 * @param spectrum_id	the identifier of the spectrum to be removed
	 * 
	 * @returns 1
	 * 
	 * @throws SPECCHIOFactoryException	the spectrum could not be removed
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("remove/{spectrum_id: [0-9]+}")
	public XmlInteger remove(@PathParam("spectrum_id") int spectrum_id) throws SPECCHIOFactoryException {
		
		SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
		factory.removeSpectrum(spectrum_id, getSecurityContext().isUserInRole(UserRoles.ADMIN));
		factory.dispose();
		
		return new XmlInteger(1);
		
	}
	
	
	/**
	 * Get the pictures associated with a given spectrum.
	 * 
	 * @param spectrum_id	the spectrum identifier
	 * 
	 * @return a PictureTable object containing the picture data
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Path("pictures/{spectrum_id: [0-9]+}")
	@Produces(MediaType.APPLICATION_XML)
	public PictureTable pictures(@PathParam("spectrum_id") int spectrum_id) throws SPECCHIOFactoryException {
		
		SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
		PictureTable pictures = factory.getPictures(spectrum_id);
		factory.dispose();
		
		return pictures;
		
	}
	
	
	/**
	 * Get the identifiers of all spectra that match a given query.
	 * 
	 * @param query		the query
	 * 
	 * @return an array of identifiers
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	@POST
	@Path("query")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public XmlInteger[] query(Query query) throws SPECCHIOFactoryException {
		
		SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
		List<Integer> ids = factory.getIdsMatchingQuery(query);
		factory.dispose();
		
		XmlIntegerAdapter adapter = new XmlIntegerAdapter();
		return adapter.marshalArray(ids);
		
	}
	
	
	/**
	 * Get the number of spectra that match a given query.
	 * 
	 * @param query		the query
	 * 
	 * @return the number of spectra in the database that match the given query
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	@POST
	@Path("queryCount")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public XmlInteger queryCount(Query query) throws SPECCHIOFactoryException {
		
		SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
		int count = factory.countIdsMatchingQuery(query);
		factory.dispose();
		
		return new XmlInteger(count);
		
	}
	
	
	/**
	 * Update spectrum metadata.
	 * 
	 * @param update_d	the update descriptor
	 * 
	 * @return 0
	 * 
	 * @throws SPECCHIOFactoryException	could not access the database
	 */
	@POST
	@Path("update_metadata")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public XmlInteger update_metadata(SpectraMetadataUpdateDescriptor update_d) throws SPECCHIOFactoryException {
		
		SpectrumFactory factory = new SpectrumFactory(getClientUsername(), getClientPassword());
		factory.updateMetadata(update_d.getIds(), update_d.getField(), update_d.getValue(), getSecurityContext().isUserInRole(UserRoles.ADMIN));
		factory.dispose();
		
		return new XmlInteger(0);
		
	}

}
