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
import ch.specchio.types.ApplicationDomainCategories;
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
	public attribute[] attributes(@PathParam("category_name") String category_name) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
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
	public attribute[] all_attributes() {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		ArrayList<attribute> attrs = factory.getAttributes().getAttributes();
		factory.dispose();
		
		return attrs.toArray(new attribute[0]);
		
	}	
	
	/**
	 * Get the metadata categories per application domain
	 * 
	 * @return a ApplicationDomainCategories object, or null if the information does not exist
	 *
	 * @throws SPECCHIOFactoryException
	 */
//	@GET
//	@Path("")
//	@Produces(MediaType.APPLICATION_XML)
	@POST
	@Path("application_domain_categories")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)	
	public ApplicationDomainCategories[] application_domain_categories(MetadataSelectionDescriptor dummy) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		ApplicationDomainCategories[] adcs = factory.getMetadataCategoriesForApplicationDomains();
		factory.dispose();
		
		return adcs;
		
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
	public Category[] categories_info() {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
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
	public CategoryTable categories(@PathParam("category") String category) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
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
	public String clear_redundancy_list() {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
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
	public XmlInteger count_existing_metaparameters(MetadataSelectionDescriptor ms_d)  {

		// TODO : a count query would be more efficient than reading all values ...
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
		if(ms_d.getAttribute_id() == 0)
		{
			ms_d.setAttribute_id(factory.getAttributes().get_attribute_id(ms_d.getAttributeName()));			
		}
		
		
		List<MetaParameter> mp_list =  factory.getMetaParameters(ms_d.getLevel(), ms_d.getIds(), ms_d.getAttribute_id(), false);
		factory.dispose();

		// only count non-null entries
		int cnt = 0;
		for(MetaParameter mp : mp_list)
		{
			if(mp.getEavId() != 0)
				cnt++;
		}

		return new XmlInteger(cnt);
		
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
	public XmlInteger[] getCalibrationIds(MetadataSelectionDescriptor msd) {

		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
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
	public MetaParameter[] get_list_of_metaparameter_vals(MetadataSelectionDescriptor ms_d) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
		if(ms_d.getAttribute_id() == 0)
		{
			ms_d.setAttribute_id(factory.getAttributes().get_attribute_id(ms_d.getAttributeName()));			
		}		
		
		List<MetaParameter> mp_list = factory.getMetaParameters(ms_d.getLevel(), ms_d.getIds(), ms_d.getAttribute_id(), ms_d.getDistinct());
		factory.dispose();
		
		return mp_list.toArray(new MetaParameter[mp_list.size()]);
		
	}	
	
	/**
	 * Get the list of list of metaparameter values for given spectrum ids and attributes.
	 * 
	 * @param ms_d	metadata selection descriptor
	 * 
	 * @return an array of all the values
	 * 
	 * @throws SPECCHIOFactoryException	could not connect to the database, or invalid value for attribute name
	 */
	@POST
	@Path("get_list_of_multiple_metaparameter_vals")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public MetaParameter[] get_list_of_multiple_metaparameter_vals(MetadataSelectionDescriptor ms_d) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
				
		ArrayList<ArrayList<MetaParameter>> mp_lists = factory.getMetaParameters(ms_d.getLevel(), ms_d.getIds(), ms_d.getAttribute_ids());
		factory.dispose();
		
		
		MetaParameter[] mp_array= new MetaParameter[mp_lists.size()*ms_d.getIds().size()];
		
		int i = 0;
		for(ArrayList<MetaParameter>mp_list: mp_lists)
		{
			//MetaParameter[] tmp = mp_list.toArray(new MetaParameter[mp_list.size()]);
			for(MetaParameter mp : mp_list)
				mp_array[i++] = mp;
		}
		
		return mp_array;
		
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
	public ConflictTable conflicts(ConflictDetectionDescriptor cd_d) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
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
	public ConflictTable conflicts_eav(ConflictDetectionDescriptor cd_d) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		ConflictTable conflicts = factory.detectEavConflicts(cd_d.getLevel(), (ArrayList<Integer>) cd_d.getIds());
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
	public XmlString[] getPoliciesForSpace(Space space) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		List<String> policies = factory.getPoliciesForSpace(space);
		factory.dispose();
		
		XmlStringAdapter adapter = new XmlStringAdapter();
		return adapter.marshalArray(policies);
		
	}
	
	
	/**
	 * Get the meta-parameter of the given metaparameter identifier.
	 * 
	 * @param id		the metaparameter identifier for which to retrieve metadata
	 * 
	 * @return the meta-parameter object corresponding to the desired id
	 *
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Path("load_metaparameter/{metaparameter_id: [0-9]+}")
	@Produces(MediaType.APPLICATION_XML)
	public MetaParameter load_metaparameter(@PathParam("metaparameter_id")	int metaparameter_id) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
		MetaParameter mp = factory.loadMetaParameter(metaparameter_id);

		factory.dispose();
		
		return mp;
		
	}	
	
	
	/**
	 * Get distinct values of an attribute
	 * 
	 * @param attribute_id	id of the required attribute
	 * 
	 * @return array of metaparameters
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@POST
	@Path("getDistinctValuesOfAttribute")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)	
	public MetaParameter[] getDistinctValuesOfAttribute(XmlInteger attribute_id) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
		ArrayList<MetaParameter> mp_list = factory.getDistinctValuesOfAttribute(attribute_id.getInteger());
		factory.dispose();
		
		
//		MetaParameter[] mp_array= new MetaParameter[mp_lists.size()];
		

		MetaParameter[] mp_array = mp_list.toArray(new MetaParameter[mp_list.size()]);

		
		return mp_array;		
		
		
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
	public TaxonomyNodeObject get_taxonomy_root(@PathParam("attribute_id")	int attribute_id) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
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
	public Taxonomy get_taxonomy(@PathParam("attribute_id")	int attribute_id) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
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
	public TaxonomyNodeObject get_taxonomy_object(@PathParam("taxonomy_id")	int taxonomy_id) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
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
	public TaxonomyNodeObject[] getChildrenOfTaxonomyNode(TaxonomyNodeObject parent) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
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
	public MeasurementUnit get_measurement_unit_from_coding(@PathParam("coding") int coding) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		MeasurementUnit mu = factory.getDataCache().get_measurement_unit(coding);
		
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
	public XmlInteger[] getInstrumentIds(MetadataSelectionDescriptor msd) {

		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
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
	public XmlInteger remove(MetadataUpdateDescriptor update_d) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
		ArrayList<Integer> ids = update_d.getIds();
		if (ids != null && ids.size() > 0) {
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
	public XmlInteger remove_metaparameters_of_given_attribute(MetadataUpdateDescriptor update_d) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());

		// delete metadata from selected spectra
		factory.removeMetadata(update_d.getLevel(), update_d.getMetaParameter().getAttributeId(), update_d.getIds());

		
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
	public Units units(attribute attr) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
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
	public XmlInteger update(MetadataUpdateDescriptor update_d) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
		int eavId = 0;
		if (update_d.hasOldMetaParameter()) {
			eavId = factory.updateMetadataWithNewId(update_d.getMetaParameter(), (ArrayList<Integer>) update_d.getIds());
		} else {
			eavId = factory.updateMetadata(update_d.getMetaParameter(), (ArrayList<Integer>) update_d.getIds());
		}
		
		factory.dispose();
		
		return new XmlInteger(eavId);
		
	}
	
	
	/**
	 * Update EAV metadata annotation.
	 *  
	 * @param update_d	the update descriptor
	 * 
	 * @return the identifier of the inserted metadata
	 * 
	 * @throws SPECCHIOFactoryException could not perform the update
	 */
	@POST
	@Path("update_annotation")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public XmlInteger update_annotation(MetadataUpdateDescriptor update_d) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		
		int eavId = 0;
		if (update_d.hasOldMetaParameter()) {
			//eavId = factory.updateMetadataWithNewId(update_d.getMetaParameter(), update_d.getIds());
		} else {
			eavId = factory.updateMetadataAnnotation(update_d.getMetaParameter(), update_d.getIds());
		}
		
		factory.dispose();
		
		return new XmlInteger(eavId);
		
	}	
	
	/**
	 * Update or insert EAV metadata. Will automatically update existing entries or insert a new metaparameter if not existing.
	 * 
	 * @param update_d	the update descriptor
	 * 
	 * @return the identifier of the inserted or updated metadata
	 */	
	@POST
	@Path("update_or_insert")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public XmlInteger update_or_insert(MetadataUpdateDescriptor update_d) {
		
		MetadataFactory factory = new MetadataFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());		
		
		int eavId = factory.updateOrInsertEavMetadata(update_d.getMetaParameter(), (ArrayList<Integer>) update_d.getIds(), update_d.getLevel());
		
		factory.dispose();
		
		return new XmlInteger(eavId);
		
	}	
	
			

}
