package ch.specchio.services;

import java.util.List;

import javax.annotation.security.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import ch.specchio.constants.UserRoles;
import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.factories.SpectralBrowserFactory;
import ch.specchio.jaxb.XmlInteger;
import ch.specchio.jaxb.XmlIntegerAdapter;
import ch.specchio.types.campaign_node;
import ch.specchio.types.database_node;
import ch.specchio.types.spectral_node_object;


/**
 * Spectral browser services.
 */
@Path("/browser")
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
public class SpectralBrowserService extends SPECCHIOService {
	
	
	/**
	 * Get a campaign node.
	 * 
	 * @param campaign_id		the campaign identifier
	 * @param order_by			the attribute by which to order the campaign's descendents
	 * @param restrict_to_view	show this user's data only?
	 * 
	 * @returns a campaign node
	 * 
	 * @throws SPECCHIOFactoryException	could not find a campaign with this identifier
	 */
	@GET
	@Path("campaign/{campaign_id: [0-9]+}/{order_by}/{restrict_to_view}")
	@Produces(MediaType.APPLICATION_XML)
	public campaign_node campaign(
			@PathParam("campaign_id") String campaign_id,
			@PathParam("order_by") String order_by,
			@PathParam("restrict_to_view") String restrict_to_view
		) throws SPECCHIOFactoryException {
		
		SpectralBrowserFactory factory = new SpectralBrowserFactory(
				getClientUsername(),
				getClientPassword(), getDataSourceName(), isAdmin()
			);
		campaign_node node = factory.getCampaignNode(Integer.valueOf(campaign_id), Boolean.valueOf(restrict_to_view), order_by);
		factory.dispose();
		
		return node;
	}
	
	
	
	/**
	 * Get the children of a given node.
	 * 
	 * @param node	the parent
	 * 
	 * @return an array containing a spectral node object for each child of the given node
	 * 
	 * @throws SPECCHIOFactoryException invalid node
	 */
	@POST
	@Path("children")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public spectral_node_object[] children(spectral_node_object node) throws SPECCHIOFactoryException {
		
		SpectralBrowserFactory factory = new SpectralBrowserFactory(
				getClientUsername(),
				getClientPassword(), getDataSourceName(), isAdmin()
			);
		List<spectral_node_object> nodes = factory.getChildNodes(node);
		factory.dispose();
		
		return  nodes.toArray(new spectral_node_object[0]);
		
	}
	
	
	/**
	 * Get a database node.
	 * 
	 * @param order_by			the criterion to order children by
	 * @param restrict_to_view	show this user's data only?
	 * 
	 * @returns a campaign node
	 * 
	 * @throws SPECCHIOFactoryException	could not create the node
	 */
	@GET
	@Path("database/{order_by}/{restrict_to_view}")
	@Produces(MediaType.APPLICATION_XML)
	public database_node database(
			@PathParam("order_by") String order_by,
			@PathParam("restrict_to_view") String restrict_to_view
		) throws SPECCHIOFactoryException {
		
		SpectralBrowserFactory factory = new SpectralBrowserFactory(
				getClientUsername(),
				getClientPassword(), getDataSourceName(), isAdmin()
			);
		database_node node = factory.getDatabaseNode(order_by, Boolean.valueOf(restrict_to_view));
		factory.dispose();
	
		return node;
	}
	
	
	/**
	 * Get the parent_id for a given hierarchy_id
	 * 
	 * @param hierarchy_id	the hierarchy_id identifying the required node
	 * 
	 * @return id of the parent of given hierarchy
	 * 
	 * @throws SPECCHIOFactoryException	could not get the node
	 */
	@GET
	@Path("get_hierarchy_parent_id/{hierarchy_id}")
	@Produces(MediaType.APPLICATION_XML)
	public XmlInteger get_hierarchy_parent_id(
			@PathParam("hierarchy_id") Integer hierarchy_id
		) throws SPECCHIOFactoryException {
		
		SpectralBrowserFactory factory = new SpectralBrowserFactory(
				getClientUsername(),
				getClientPassword(), getDataSourceName(), isAdmin()
			);
		
		int parent_id = factory.getHierarchyParentId(hierarchy_id);
		factory.dispose();
		
		
		return new XmlInteger(parent_id);
		
	}	
	
	
	/**
	 * Get the identifiers of all the spectra that descend from a given node 
	 * 
	 * @param node	the node
	 * 
	 * @returns an array containing the identifier of every spectrum that descends from the given node
	 * 
	 * @throws SPECCHIOFactoryException invalid node
	 */
	@POST
	@Path("spectrum_ids")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public XmlInteger[] spectrum_ids(spectral_node_object node) throws SPECCHIOFactoryException {
		
		SpectralBrowserFactory factory = new SpectralBrowserFactory(
				getClientUsername(),
				getClientPassword(), getDataSourceName(), isAdmin()
			);
		List<Integer> ids = factory.getDescendentSpectrumIds(node);
		factory.dispose();
		
		XmlIntegerAdapter adapter = new XmlIntegerAdapter();
		return adapter.marshalArray(ids);
	}

}
