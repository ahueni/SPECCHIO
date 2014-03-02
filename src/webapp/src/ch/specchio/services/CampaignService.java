package ch.specchio.services;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.annotation.security.*;

import com.sun.jersey.api.client.ClientResponse;

import ch.specchio.constants.UserRoles;
import ch.specchio.factories.CampaignFactory;
import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.factories.SpectralFileFactory;
import ch.specchio.jaxb.XmlInteger;
import ch.specchio.types.Campaign;

/**
 * Campaign services.
 */
@Path("/campaign")
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
public class CampaignService extends SPECCHIOService {
	
	
	/**
	 * Export a campaign.
	 * 
	 * @param campaign_type	the type of campaign to be exported
	 * @param campaign_id	the identifier of the campaign to be exported
	 * 
	 * @throws SPECCHIOFactoryException	the campaign does not exist
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("export/{campaign_type}/{campaign_id: [0-9]+}")
	public Response exportCampaign(
			@PathParam("campaign_type") String campaign_type,
			@PathParam("campaign_id") int campaign_id
		) throws SPECCHIOFactoryException {

		Response response;

		CampaignFactory factory = CampaignFactory.getInstance(getClientUsername(), getClientPassword(), campaign_type);
		try {
			OutputStream os = getResponse().getOutputStream();
			factory.exportCampaign(campaign_id, os);
			os.close();
			response = Response.ok().build();
		}
		catch (IOException ex) {
			// not sure what might cause this
			response = Response.status(ClientResponse.Status.INTERNAL_SERVER_ERROR).build();
		}
		factory.dispose();
		
		return response;
		
	}
	
	
	/**
	 * Get the campaign object for a campaign.
	 * 
	 * @param campaign_type	the type of campaign to retrieve
	 * @param campaign_id	the identifier of the campaign to retrieve
	 * 
	 * @return a new campaign object, or null if the identifier does not exit
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("get/{campaign_type}/{campaign_id: [0-9]+}")
	public Campaign get(
			@PathParam("campaign_type") String campaign_type,
			@PathParam("campaign_id") int campaign_id
		) throws SPECCHIOFactoryException {
		
		CampaignFactory factory = CampaignFactory.getInstance(getClientUsername(), getClientPassword(), campaign_type);
		Campaign campaign = factory.getCampaign(campaign_id);
		factory.dispose();
		
		return campaign;
		
	}
	
	
	/**
	 * Get the identifier of a node in a campaign's hierarchy.
	 * 
	 * @param campaign_type		the type of campaign to test
	 * @param campaign_id		the identifier of the campaign to be tested
	 * @param name				the name of the node to test
	 * @param parent_id			the identifier of the node's parent
	 * 
	 * @returns the node's identifier, or -1 if the node does not exist
	 * 
	 * @throws SPECCHIOFactoryException	the database could not accessed
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("getHierarchyId/{campaign_type}/{campaign_id: [0-9]+}/{name}/{parent_id: [0-9]+}")
	public XmlInteger getHierarchyId(
			@PathParam("campaign_type") String campaign_type,
			@PathParam("campaign_id") int campaign_id,
			@PathParam("name") String name,
			@PathParam("parent_id") int parent_id
		) throws SPECCHIOFactoryException {
		
		CampaignFactory factory = CampaignFactory.getInstance(getClientUsername(), getClientPassword(), campaign_type);
		int id = factory.getHierarchyNodeId(campaign_id, name, parent_id);
		factory.dispose();
		
		return new XmlInteger(id);
		
	}
	
	
	/**
	 * Import a campaign.
	 * 
	 * @param campaign_type	the type of campaign to be import
	 * 
	 * @throws SecurityException		a non-admin user tried to import a campaign
	 * @throws SPECCHIOFactoryException	the request body is not in the correct format
	 */
	@POST
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Path("import/{campaign_type}/{user_id: [0-9]+}")
	public Response importCampaign(
			@PathParam("campaign_type") String campaign_type,
			@PathParam("user_id") int user_id
		) throws SPECCHIOFactoryException {
		
		Response response;
		
		if (!getSecurityContext().isUserInRole(UserRoles.ADMIN)) {
			response = Response.status(ClientResponse.Status.FORBIDDEN).build();
		} else {
		
			CampaignFactory factory = CampaignFactory.getInstance(getClientUsername(), getClientPassword(), campaign_type);
			try {
				factory.importCampaign(user_id, getRequest().getInputStream());
				response = Response.ok().build();
			}
			catch (IOException ex) {
				// malformed input
				response = Response.status(ClientResponse.Status.BAD_REQUEST).build();
			}
			factory.dispose();
			
		}
		
		return response;
		
	}


	/**
	 * Create a new campaign.
	 * 
	 * @param c		the campaign to be created
	 * 
	 * @return the identifier of the new campaign
	 */
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@Path("insert")
	public XmlInteger insert(Campaign c) throws SPECCHIOFactoryException {
		
		CampaignFactory factory = CampaignFactory.getInstance(getClientUsername(), getClientPassword(), c.getType());
		factory.insertCampaign(c);
		factory.dispose();
		
		return new XmlInteger(c.getId());
		
	}
	
	
	/**
	 * Insert a node into a campaign's hierarchy.
	 * 
	 * @param campaign_type		the type of campaign into which the node will be inserted
	 * @param campaign_id		the identifier of the campaign to which the node is to be added
	 * @param name				the name of the new node
	 * @param parent_id			the identifier of the new node's parent
	 * 
	 * @returns the identifier of the new node
	 * 
	 * @throws SPECCHIOFactoryException	the node could not be inserted
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("insertHierarchy/{campaign_type}/{campaign_id: [0-9]+}/{name}/{parent_id: [0-9]+}")
	public XmlInteger insertHierarchy(
			@PathParam("campaign_type") String campaign_type,
			@PathParam("campaign_id") int campaign_id,
			@PathParam("name") String name,
			@PathParam("parent_id") int parent_id
		) throws SPECCHIOFactoryException {
		
		CampaignFactory factory = CampaignFactory.getInstance(getClientUsername(), getClientPassword(), campaign_type);
		int id = factory.insertHierarchyNode(campaign_id, name, parent_id);
		factory.dispose();
		
		return new XmlInteger(id);
		
	}
	
	/**
	 * Get the identifier of a sub-hierarchy with a given name, creating the
	 * hierarchy if it doesn't exist.
	 * 
	 * @param campaign	the campaign into which to insert the hierarchy
	 * @param parent_id			the identifier of the the parent of the hierarchy
	 * @param hierarchy_name	the name of the desired hierarchy
	 * 
	 * @return the identifier of the child of parent_id with the name hierarchy_name
	 *
	 * @throws SPECCHIOFactoryException
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("getSubHierarchyId/{campaign_type}/{campaign_id: [0-9]+}/{parent_id: [0-9]+}/{hierarchy_name}")	
	public XmlInteger getSubHierarchyId(
			@PathParam("campaign_type") String campaign_type,
			@PathParam("campaign_id") int campaign_id,			
			@PathParam("parent_id") int parent_id,
			@PathParam("hierarchy_name") String hierarchy_name
			) throws SPECCHIOFactoryException {
		
		SpectralFileFactory factory = new SpectralFileFactory(
				getClientUsername(),
				getClientPassword(),
				getDataSourceName(),
				campaign_type,
				campaign_id
			);		
		
		int id = factory.getSubHierarchyId(parent_id, hierarchy_name);
		
		factory.dispose();
		
		return new XmlInteger(id);		
	}
	
	
	
	/**
	 * Get a list of the campaigns in the database.
	 * 
	 * @param campaign_type	the type of campaigns to list
	 * 
	 * @throws SPECCHIOFactoryException	the database could not accessed
	 * 
	 * @return a list of the campaigns in the database
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("list/{campaign_type}")
	public Campaign[] list(@PathParam("campaign_type") String campaign_type) throws SPECCHIOFactoryException {
		
		CampaignFactory factory = CampaignFactory.getInstance(getClientUsername(), getClientPassword(), campaign_type);
		Campaign[] campaigns = factory.getCampaigns();
		factory.dispose();
		
		return campaigns;
		
	}
	
	
	/**
	 * Remove a campaign from the database.
	 * 
	 * @param campaign_type	the type of the campaign to be removed
	 * @param campaign_id	the identifier of the campaing to be removed
	 * 
	 * @return 1
	 * 
	 * @throws SPECCHIOFactoryException	the campaign could not be removed
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("remove/{campaign_type}/{campaign_id: [0-9]+}")
	public XmlInteger remove(
			@PathParam("campaign_type") String campaign_type,
			@PathParam("campaign_id") int campaign_id
		) throws SPECCHIOFactoryException {
		
		CampaignFactory factory = CampaignFactory.getInstance(getClientUsername(), getClientPassword(), campaign_type);
		factory.removeCampaign(campaign_id, getSecurityContext().isUserInRole(UserRoles.ADMIN));
		factory.dispose();
		
		return new XmlInteger(1);
		
	}
	
	
	/**
	 * Remove a sub-hierarchy from the database.
	 * 
	 * @param campaign_type		the type of the campaign from which the sub-hierarchy will be removed
	 * @param hierarchy_id		the identifier of the node at the root of the sub-hierarchy to be removed
	 * @param hierarchy_name	the name of the hierarchy to be removed
	 * 
	 * @return 1
	 * 
	 * @throws SPECCHIOFactoryException	the sub-hierarchy could not be removed
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("removeHierarchy/{campaign_type}/{hierarchy_id: [0-9]+}/{hierarchy_name}")
	public XmlInteger removeHierarchy(
			@PathParam("campaign_type") String campaign_type,
			@PathParam("hierarchy_id") int hierarchy_id,
			@PathParam("hierarchy_name") String hierarchy_name
		) throws SPECCHIOFactoryException {
		
		CampaignFactory factory = CampaignFactory.getInstance(getClientUsername(), getClientPassword(), campaign_type);
		factory.removeHierarchyNode(hierarchy_id, hierarchy_name, getSecurityContext().isUserInRole(UserRoles.ADMIN));
		factory.dispose();
		
		return new XmlInteger(1);
		
	}
	
	
	/**
	 * Update campaign information.
	 * 
	 * @param campaign	the new campaign information
	 * 
	 * @return an empty string
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@Path("update")
	public String update(Campaign campaign) throws SPECCHIOFactoryException {
		
		CampaignFactory factory = CampaignFactory.getInstance(getClientUsername(), getClientPassword(), campaign.getType());
		factory.updateCampaign(campaign);
		factory.dispose();
		
		return "";
		
	}
		
		

}
