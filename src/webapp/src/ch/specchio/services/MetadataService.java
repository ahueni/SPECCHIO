package ch.specchio.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.annotation.security.*;

import ch.specchio.constants.UserRoles;
import ch.specchio.factories.MetadataFactory;
import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.jaxb.XmlInteger;
import ch.specchio.jaxb.XmlIntegerAdapter;
import ch.specchio.jaxb.XmlString;
import ch.specchio.jaxb.XmlStringAdapter;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.spaces.Space;
import ch.specchio.types.Category;
import ch.specchio.types.CategoryTable;
import ch.specchio.types.ConflictDetectionDescriptor;
import ch.specchio.types.ConflictTable;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetadataSelectionDescriptor;
import ch.specchio.types.MetadataUpdateDescriptor;
import ch.specchio.types.Taxonomy;
import ch.specchio.types.TaxonomyNodeObject;
import ch.specchio.types.attribute;
import ch.specchio.types.Units;


/**
 * Metadata service.
 */
@Path("/metadata")
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
public class MetadataService extends SPECCHIOService {
	
	
	/**
	 * Get the list of attributes in a given category.
	 * 
	 * @param category_name		the name of the category
	 * 
	 * @return an array of all the attributes in the given category
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database, or invalid value for category_name
	 */
	@GET
	@Path("attributes/{category_name}")
	@Produces(MediaType.APPLICATION_XML)
	public attribute[] attributes(@PathParam("category_name") String category_name) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		List<attribute> attrs = factory.getAttributesForCategory(category_name);
		factory.dispose();
		
		return attrs.toArray(new attribute[0]);
		
	}
	
	
	/**
	 * Get all available attributes.
	 * 
	 * @return array of attributes
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database
	 */
	@GET
	@Path("all_attributes")
	@Produces(MediaType.APPLICATION_XML)
	public attribute[] all_attributes() throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		ArrayList<attribute> attrs = factory.getAttributes().getAttributes();
		factory.dispose();
		
		return attrs.toArray(new attribute[0]);
		
	}	
	
	/**
	 * Get the list of all known categories.
	 * 
	 * @return an array of all categories
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database
	 */
	@GET
	@Path("categories_info")
	@Produces(MediaType.APPLICATION_XML)
	public Category[] categories_info() throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		List<Category> categories = factory.getAttributes().getCategories();
		factory.dispose();
		
		return categories.toArray(new Category[0]);
		
	}	
	
	
	/**
	 * Get a hash table mapping identifiers to names.
	 * 
	 * @param category	the category
	 * 
	 * @return a table mapping identifiers to names
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database
	 */
	@GET
	@Path("categories/{category}")
	@Produces(MediaType.APPLICATION_XML)
	public CategoryTable categories(@PathParam("category") String category) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		CategoryTable table = factory.getCategoryTable(category);
		factory.dispose();
		
		return table;
		
	}
	
	
	/**
	 * Get the list of all known categories.
	 * 
	 * @return an array of all categories
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database
	 */
	@GET
	@Path("clear_redundancy_list")
	@Produces(MediaType.APPLICATION_XML)
	public String clear_redundancy_list() throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		factory.getEavServices().clear_redundancy_list();
		
		factory.dispose();
		
		return "";
		
	}		
	
	
	/**
	 * Get the count of existing metaparameters for the supplied spectrum ids and attribute id
	 * 
	 * @param ids		spectrum ids
	 * @param attribute		attribute name
	 * 
	 * @return count of existing values
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database, or invalid value for attribute name
	 */
	@POST
	@Path("count_existing_metaparameters")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public XmlInteger count_existing_metaparameters(MetadataSelectionDescriptor ms_d) throws SPECCHIOFactoryException {

		// TODO : a count query would be more efficient than reading all values ...
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		if(ms_d.getAttribute_id() == 0)
		{
			ms_d.setAttribute_id(factory.getAttributes().get_attribute_id(ms_d.getAttributeName()));			
		}
		
		
		List<MetaParameter> mp_list =  factory.getMetaParameterValues(ms_d.getIds(), ms_d.getAttribute_id(), false);
		factory.dispose();

		return new XmlInteger(mp_list.size());
		
	}
	
	
	/**
	 * Get calibration ids for a list of spectra.
	 * 
	 * @param spectrum_ids	the spectrum identifiers
	 * 
	 * @return list of calibration ids, zero where no calibration is defined
	 */	
	@POST
	@Path("getCalibrationIds")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public XmlInteger[] getCalibrationIds(MetadataSelectionDescriptor msd) throws SPECCHIOFactoryException {

		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		ArrayList<Integer> ids = factory.getCalibrationIds(msd.getIds());
		
		XmlIntegerAdapter adapter = new XmlIntegerAdapter();
		return adapter.marshalArray(ids);		
		
	}
		
	
	
	
	/**
	 * Get the list of metaparameter values for given spectrum ids and attribute.
	 * 
	 * @param ms_d	metadata selection descriptor
	 * 
	 * @return an array of all the values
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database, or invalid value for attribute name
	 */
	@POST
	@Path("get_list_of_metaparameter_vals")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public MetaParameter[] get_list_of_metaparameter_vals(MetadataSelectionDescriptor ms_d) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		if(ms_d.getAttribute_id() == 0)
		{
			ms_d.setAttribute_id(factory.getAttributes().get_attribute_id(ms_d.getAttributeName()));			
		}		
		
		List<MetaParameter> mp_list = factory.getMetaParameterValues(ms_d.getIds(), ms_d.getAttribute_id(), false);
		factory.dispose();
		
		return mp_list.toArray(new MetaParameter[mp_list.size()]);
		
	}	
	
	
	/**
	 * Check for metadata conflicts.
	 * 
	 * @param cd_d	the conflict detection descriptor
	 * 
	 * @return a hash mapping metadata field names to conflict information structures
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database
	 */
	@POST
	@Path("conflicts")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public ConflictTable conflicts(ConflictDetectionDescriptor cd_d) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		ConflictTable conflicts = factory.detectConflicts(cd_d.getIds(), cd_d.getMetadataFields());
		factory.dispose();
		
		return conflicts;
		
	}
	
	
	/**
	 * Check for conflicts in EAV metadata.
	 * 
	 * @param cd_d	the conflict detection descriptor
	 * 
	 * @return a hash mapping attribute id to conflict information structures
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database
	 */
	@POST
	@Path("conflicts_eav")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public ConflictTable conflicts_eav(ConflictDetectionDescriptor cd_d) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		ConflictTable conflicts = factory.detectEavConflicts(cd_d.getIds());
		factory.dispose();
		
		return conflicts;
		
	}
	
	
	/**
	 * Get the data policies for a collection of space.
	 * 
	 * @param spaces	the spaces
	 * 
	 * @return an array of Objects representing the policies that apply to the input space
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@POST
	@Path("getPoliciesForSpace")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public XmlString[] getPoliciesForSpace(Space space) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		List<String> policies = factory.getPoliciesForSpace(space);
		factory.dispose();
		
		XmlStringAdapter adapter = new XmlStringAdapter();
		return adapter.marshalArray(policies);
		
	}
	
	
	/**
	 * Get the root node of a taxonomy
	 * 
	 * @param attribute_id	id of the required taxonomy
	 * 
	 * @return taxonomy node
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Path("get_taxonomy_root/{attribute_id: [0-9]+}")
	@Produces(MediaType.APPLICATION_XML)
	public TaxonomyNodeObject get_taxonomy_root(@PathParam("attribute_id")	int attribute_id) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		TaxonomyNodeObject root = factory.getTaxonomyRoot(attribute_id);

		factory.dispose();
		
		return root;
		
	}	
	
	/**
	 * Get a taxonomy
	 * 
	 * @param attribute_id	id of the required taxonomy
	 * 
	 * @return taxonomy object including hashtable with elements
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Path("get_taxonomy/{attribute_id: [0-9]+}")
	@Produces(MediaType.APPLICATION_XML)
	public Taxonomy get_taxonomy(@PathParam("attribute_id")	int attribute_id) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		Taxonomy t = factory.getTaxonomy(attribute_id);

		factory.dispose();
		
		return t;
		
	}	
	
		
	
	
	
	/**
	 * Get the node of a taxonomy
	 * 
	 * @param taxonomy_id	id of the required taxonomy
	 * 
	 * @return taxonomy node
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Path("get_taxonomy_object/{taxonomy_id: [0-9]+}")
	@Produces(MediaType.APPLICATION_XML)
	public TaxonomyNodeObject get_taxonomy_object(@PathParam("taxonomy_id")	int taxonomy_id) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		TaxonomyNodeObject root = factory.getTaxonomyObject(taxonomy_id);

		factory.dispose();
		
		return root;
		
	}	
	
	
	
	
	/**
	 * Get the children nodes of a taxonomy node
	 * 
	 * @param taxonomy_id	id of the parent taxonomy
	 * 
	 * @return array of taxonomy nodes
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@POST
	@Path("getChildrenOfTaxonomyNode")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public TaxonomyNodeObject[] getChildrenOfTaxonomyNode(TaxonomyNodeObject parent) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		ArrayList<TaxonomyNodeObject> children = factory.getTaxonomyChildren(parent);

		factory.dispose();
		
		return children.toArray(new TaxonomyNodeObject[0]);
		
	}		
	
	
	/**
	 * Get measurement unit for given coding (see MeasurementUnit static codes)
	 * 
	 * @param coding	coding based on ASD coding
	 * 
	 * @return a new MeasurementUnit object
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */	
	@GET
	@Path("get_measurement_unit_from_coding/{coding: [0-9]+}")
	@Produces(MediaType.APPLICATION_XML)
	public MeasurementUnit get_measurement_unit_from_coding(@PathParam("coding") int coding) throws SPECCHIOFactoryException {
		
		MeasurementUnit mu = MetadataFactory.getDataCache().get_measurement_unit(coding);
		
		return mu;
	}	
	
	/**
	 * Get instrument ids for a list of spectra.
	 * 
	 * @param spectrum_ids	the spectrum identifiers
	 * 
	 * @return list of instrument ids, zero where no instrument is defined
	 */	
	@POST
	@Path("getInstrumentIds")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public XmlInteger[] getInstrumentIds(MetadataSelectionDescriptor msd) throws SPECCHIOFactoryException {

		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		ArrayList<Integer> ids = factory.getInstrumentIds(msd.getIds());
		
		XmlIntegerAdapter adapter = new XmlIntegerAdapter();
		return adapter.marshalArray(ids);		
		
	}
	
	
	/**
	 * Remove metadata. If the update descriptor contains an empty list of
	 * identifier, remove the metadata from all spectra.
	 * 
	 * @param udpate_d	the update descriptor
	 * 
	 * @return 0
	 *
	 * @throws SPECCHIOFactoryException the metadata could not be removed
	 */
	@POST
	@Path("remove")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public XmlInteger remove(MetadataUpdateDescriptor update_d) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		Integer[] ids = update_d.getIds();
		if (ids != null && ids.length > 0) {
			// delete metadata from selected spectra
			factory.removeMetadata(update_d.getMetaParameter(), ids);
		} else {
			// delete metadata from all spectra
			factory.removeMetadata(update_d.getMetaParameter());
		}
		
		factory.dispose();
		
		return new XmlInteger(0);
		
	}
	
	
	/**
	 * Remove metaparameters for specified attribute and spectra ids 
	 * 
	 * @param udpate_d	the update descriptor
	 * 
	 * @return 0
	 *
	 * @throws SPECCHIOFactoryException the metadata could not be removed
	 */
	@POST
	@Path("remove_metaparameters_of_given_attribute")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public XmlInteger remove_metaparameters_of_given_attribute(MetadataUpdateDescriptor update_d) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());

		// delete metadata from selected spectra
		factory.removeMetadata(update_d.getMetaParameter().getAttributeId(), update_d.getIdsAsList());

		
		factory.dispose();
		
		return new XmlInteger(0);
		
	}


	/**
	 * Get the units for an attribute.
	 * 
	 * @param attr	the attribute
	 * 
	 * @return a units object representing the attribute's units
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	@POST
	@Path("units")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Units units(attribute attr) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		Units u = factory.getAttributeUnits(attr);
		factory.dispose();
		
		return u;
		
	}

	
	
	/**
	 * Update an item of metadata for a given set of spectrum identifiers.
	 * 
	 * @param update_d	the update descriptor
	 * 
	 * @return the identifier of the inserted metadata
	 * 
	 * @throws SPECCHIOFactoryException could not perform the update
	 */
	@POST
	@Path("update")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public XmlInteger update(MetadataUpdateDescriptor update_d) throws SPECCHIOFactoryException {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName());
		
		int eavId = 0;
		if (update_d.hasOldMetaParameter()) {
			eavId = factory.updateMetadataWithNewId(update_d.getMetaParameter(), update_d.getIds());
		} else {
			eavId = factory.updateMetadata(update_d.getMetaParameter(), update_d.getIds());
		}
		
		factory.dispose();
		
		return new XmlInteger(eavId);
		
	}
			

}
