package ch.specchio.client;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import au.ands.org.researchdata.RDACollectionDescriptor;
import ch.specchio.interfaces.ProgressReportInterface;
import ch.specchio.plots.GonioSamplingPoints;
import ch.specchio.queries.Query;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.spaces.ReferenceSpaceStruct;
import ch.specchio.spaces.Space;
import ch.specchio.spaces.SpectralSpace;
import ch.specchio.types.Calibration;
import ch.specchio.types.CalibrationMetadata;
import ch.specchio.types.Campaign;
import ch.specchio.types.Category;
import ch.specchio.types.CategoryTable;
import ch.specchio.types.ConflictTable;
import ch.specchio.types.Country;
import ch.specchio.types.Institute;
import ch.specchio.types.Instrument;
import ch.specchio.types.InstrumentDescriptor;
import ch.specchio.types.MatlabAdaptedArrayList;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.Picture;
import ch.specchio.types.PictureTable;
import ch.specchio.types.Reference;
import ch.specchio.types.ReferenceBrand;
import ch.specchio.types.ReferenceDescriptor;
import ch.specchio.types.Sensor;
import ch.specchio.types.SpectralFile;
import ch.specchio.types.Spectrum;
import ch.specchio.types.SpectrumDataLink;
import ch.specchio.types.SpectrumFactorTable;
import ch.specchio.types.TaxonomyNodeObject;
import ch.specchio.types.Units;
import ch.specchio.types.User;
import ch.specchio.types.attribute;
import ch.specchio.types.campaign_node;
import ch.specchio.types.database_node;
import ch.specchio.types.spectral_node_object;


/**
 * Client object implementing a cache for another client object.
 */
public class SPECCHIOClientCache implements SPECCHIOClient {
	
	/** the client for which to act as a cache */
	private SPECCHIOClient realClient;
	
	/** attributes cache */
	private attribute[] attributes;
	
	/** cache of attributes by category */
	private Hashtable<String, attribute[]> attributesByCategory;
	
	/** index of attributes by id */
	private Hashtable<Integer, attribute> attributesById;
	
	/** index of attributes by name */
	private Hashtable<String, attribute> attributesByName;
	
	/** metadata categories */
	private Hashtable<String, CategoryTable> metadataCategories;
	
	/** the progress report with which to indicate progress */
	private ProgressReportInterface pr;
	
	/**
	 * Constructor.
	 * 
	 * @param client	the client object to be cached
	 */
	public SPECCHIOClientCache(SPECCHIOClient client) {
		
		/** save a reference to the real client */
		realClient = client;
		
		/** initialise member variables */
		pr = null;
		
		// initialise caches
		attributes = null;
		attributesByCategory = new Hashtable<String, attribute[]>();
		attributesById = null;
		attributesByName = null;
		metadataCategories = new Hashtable<String, CategoryTable>();
		
	}
	
	
	/**
	 * Connect to the server.
	 * 
	 * @throws SPECCHIOClientException could not log in
	 */
	public void connect() throws SPECCHIOClientException {
		
		// make connection
		if (pr != null) {
			pr.set_progress(0);
		}
		realClient.connect();
		
		if (realClient.getLoggedInUser() != null) {
		
			// pre-populate attribute cache
			if (pr != null) {
				pr.set_progress(50);
				pr.set_operation("Downloading attributes");
			}
			attributes = realClient.getAttributes();
			attributesById = new Hashtable<Integer, attribute>();
			attributesByName = new Hashtable<String, attribute>();
			Set<String> categories = new HashSet<String>();
			for (attribute attr : attributes) {
				attributesById.put(attr.id, attr);
				attributesByName.put(attr.name, attr);
				if (attr.cat_name != null && attr.cat_name.length() > 0) {
					categories.add(attr.cat_name);
				}
			}
			if (pr != null) {
				pr.set_progress(75);
			}
			for (String category : categories) {
				attributesByCategory.put(category, realClient.getAttributesForCategory(category));
			}
		}
		
		// finished
		if (pr != null) {
			pr.set_progress(100);
			pr.set_operation("Done");
		}
		
	}
	
	/**
	 * Clears the known metaparameter list held by the server for this user
	 */
	public void clearMetaparameterRedundancyList() throws SPECCHIOClientException {
		
		realClient.clearMetaparameterRedundancyList();
		
	}
	
	
	/**
	 * Create a user account.
	 * 
	 * @param user	a user object describing the new user account
	 * 
	 * @return a new user object containing the complete account details
	 * 
	 * @throws SPECCHIOClientException
	 */
	public User createUserAccount(User user) throws SPECCHIOClientException {
		
		return realClient.createUserAccount(user);
		
	}
	
	
	/**
	 * Create a new instrument.
	 * 
	 * @param name	the name of the new instrument
	 * @throws SPECCHIOClientException 
	 */
	public void createInstrument(String name) throws SPECCHIOClientException {
		
		realClient.createInstrument(name);
		
	}
	
	
	/**
	 * Create a new reference.
	 * 
	 * @param name	the name of the new reference
	 * @throws SPECCHIOClientException 
	 */
	public void createReference(String name) throws SPECCHIOClientException {
		
		realClient.createReference(name);
		
	}
	
	
	/**
	 * Delete calibration data from the database
	 * 
	 * @param calibration_id	the calibration identifier
	 * @throws SPECCHIOClientException 
	 */
	public void deleteCalibration(int calibration_id) throws SPECCHIOClientException {
		
		realClient.deleteCalibration(calibration_id);
		
	}
	
	
	/**
	 * Delete an instrument from the database.
	 * 
	 * @param instrument_id	the instrument identifier
	 */
	public void deleteInstrument(int instrument_id) throws SPECCHIOClientException {
		
		realClient.deleteInstrument(instrument_id);
		
	}
	
	
	/**
	 * Delete a picture of an instrument from the database.
	 * 
	 * @param picture_id	the picture identifier
	 */
	public void deleteInstrumentPicture(int picture_id) throws SPECCHIOClientException {
		
		realClient.deleteInstrumentPicture(picture_id);
		
	}
	
	/**
	 * Delete a reference from the database.
	 * 
	 * @param reference_id	the reference identifier
	 */
	public void deleteReference(int reference_id) throws SPECCHIOClientException {
		
		realClient.deleteReference(reference_id);
		
	}
	
	
	/**
	 * Delete a picture of a reference from the database.
	 * 
	 * @param picture_id	the picture identifier
	 */
	public void deleteReferencePicture(int picture_id) throws SPECCHIOClientException {
		
		realClient.deleteReferencePicture(picture_id);
		
	}
	
	
	/**
	 * Delete target-reference links from the database.
	 * 
	 * @param target_id		the target identifier
	 * 
	 * @return the number of links deleted
	 */
	public int deleteTargetReferenceLinks(int target_id) throws SPECCHIOClientException {
		
		return realClient.deleteTargetReferenceLinks(target_id);
		
	}


	/**
	 * Disconnect from the server.
	 */
	public void disconnect() throws SPECCHIOClientException {
		
		realClient.disconnect();
		
	}
	
	
	/**
	 * Get the attributes for a metadata category.
	 * 
	 * @param category	the category name
	 * 
	 * @return an array of attribute objects
	 */
	public attribute[] getAttributesForCategory(String category) throws SPECCHIOClientException {
		
		if (!attributesByCategory.containsKey(category)) {
			attributesByCategory.put(category, realClient.getAttributesForCategory(category));
		}
		
		return attributesByCategory.get(category);
		
	}

	/**
	 * Get the attributes object containing information on all attributes, units and categories.
	 * 
	 * @return Attributes
	 */
	public attribute[] getAttributes() throws SPECCHIOClientException {
		
		if (attributes == null) {
			attributes = realClient.getAttributes();
		}
		
		return attributes;
		
	}
	
	
	/**
	 * Get the attributes hashtable
	 * 
	 * @return Attributes stored in hashtable, indexed by attribute_id
	 */
	public Hashtable<Integer, attribute> getAttributesIdHash() throws SPECCHIOClientException {
		
		if (attributesById == null) {
			attributesById = realClient.getAttributesIdHash();
		}
		
		return attributesById;
		
	}
	
	/**
	 * Get the attributes hashtable
	 * 
	 * @return Attributes stored in hashtable, indexed by attribute name
	 */
	public Hashtable<String, attribute> getAttributesNameHash() throws SPECCHIOClientException {
		
		if (attributesByName == null) {
			attributesByName = realClient.getAttributesNameHash();
		}
		
		return attributesByName;
		
	}
	
	
	/**
	 * Get the units for an attribute.
	 * 
	 * @param attr	the attribute
	 * 
	 * @return a units object representing the attribute's units
	 */
	public Units getAttributeUnits(attribute attr) throws SPECCHIOClientException {
		
		return realClient.getAttributeUnits(attr);
		
	}
	
	/**
	 * Get a campaign descriptor.
	 * 
	 * @param campaign_id	the campaign identifier
	 * 
	 * @return a new campaign object
	 */
	public Campaign getCampaign(int campaign_id) throws SPECCHIOClientException {
		
		return realClient.getCampaign(campaign_id);
		
	}
	
	
	/**
	 * Get the ids and names of all metadata categories.
	 * 
	 * @return an array list of category information
	 */
	public ArrayList<Category> getCategoriesInfo() throws SPECCHIOClientException {
		
		return realClient.getCategoriesInfo();
		
	}
	
	
	/**
	 * Export a campaign.
	 * 
	 * @param c		the campaign to be exported
	 * 
	 * @return an input stream connected to the exported campaign date
	 */
	public InputStream getCampaignExportInputStream(Campaign c) throws SPECCHIOClientException {
		
		return realClient.getCampaignExportInputStream(c);
		
	}
	
	
	/**
	 * Get a campaign node for the spectral data browser.
	 * 
	 * @param campaign_id		the campaign identifier
	 * @param order_by			the attribute by which to order the campaign's descendents
	 * @param restrict_to_view	show user's data only
	 * 
	 * @return a new campaign node object, or null if the campaign does not exist
	 */
	public campaign_node getCampaignNode(int campaign_id, String order_by, boolean restrict_to_view) throws SPECCHIOClientException {
		
		return realClient.getCampaignNode(campaign_id, order_by, restrict_to_view);
		
	}
	
	
	/**
	 * Get all of the campaigns in the database.
	 *
	 * @return an array of campaign objects descriving each campaign in the database
	 */
	public Campaign[] getCampaigns() throws SPECCHIOClientException {
		
		return realClient.getCampaigns();
		
	}
	
	
	/**
	 * Get the value of a capability.
	 * 
	 * @param capability	the capability name
	 * 
	 * @return the value of the capability, or null if the capability is not recognised
	 */
	public String getCapability(String capability) throws SPECCHIOClientException {
		
		return realClient.getCapability(capability);
		
	}
	
	
	/**
	 * Get the children of a node of the spectral data browser.
	 * 
	 * @param sn	the node
	 * 
	 * @return a list of the node's children
	 */
	public List<spectral_node_object> getChildrenOfNode(spectral_node_object sn) throws SPECCHIOClientException {
		
		return realClient.getChildrenOfNode(sn);
		
	}
	
	
	/**
	 * Get the children of a node of a taxonomy.
	 * 
	 * @param tn	the node
	 * 
	 * @return a list of the node's children
	 */
	public List<TaxonomyNodeObject> getChildrenOfTaxonomyNode(TaxonomyNodeObject tn) throws SPECCHIOClientException {
		
		return realClient.getChildrenOfTaxonomyNode(tn);
		
	}
	
	
	/**
	 * Get the list of countries known to the server.
	 * 
	 * @return an array of Country objects
	 */
	public Country[] getCountries() throws SPECCHIOClientException {
		
		return realClient.getCountries();
		
	}
	
	
	/**
	 * Get a database node for the spectral data browser.
	 * 
	 * @param order_by			the attribute to order by
	 * @param restrict_to_view	display the current user's data only
	 * 
	 * @return a database_node object
	 */
	public database_node getDatabaseNode(String order_by, boolean restrict_to_view) throws SPECCHIOClientException {
		
		return realClient.getDatabaseNode(order_by, restrict_to_view);
		
	}
	
	
	/**
	 * Get a conflicts in the EAV metadata for a set of spectra.
	 * 
	 * @param spectrum_ids	the spectrum identifiers
	 * 
	 * @return a ConflictTable object containing all of the conflicts
	 */
	public ConflictTable getEavMetadataConflicts(ArrayList<Integer> spectrum_ids) throws SPECCHIOClientException {
		
		return realClient.getEavMetadataConflicts(spectrum_ids);
		
	}

	
	/**
	 * Get the count of existing metaparameters for the supplied spectrum ids and attribute id
	 * 
	 * @param attribute_id	id of the attribute
	 * @param ids	spectrum ids
	 * 
	 * @return Integer
	 */
	
	public Integer getExistingMetaparameterCount(Integer attribute_id, ArrayList<Integer> ids) throws SPECCHIOClientException {
		
		return realClient.getExistingMetaparameterCount(attribute_id, ids);
		
	}
	
	/**
	 * Get the file format identifier for a file format name.
	 * 
	 * @param format	the file format name
	 * 
	 * @return the identifier associated with the specified file format, or -1 if file format is not recognised
	 */
	public int getFileFormatId(String format) throws SPECCHIOClientException {
		
		return realClient.getFileFormatId(format);
		
	}
	
	
	/**
	 * Get the identifier of a hierarchy node.
	 * 
	 * @param campaign	the campaign in which the node is located
	 * @param name		the name of the node
	 * @param parent_id	the parent of the node
	 * 
	 * @return the identifier of the child of parent_id with the given name, or -1 if the node does not exist
	 */
	public int getHierarchyId(Campaign campaign, String name, int parent_id) throws SPECCHIOClientException {
		
		return realClient.getHierarchyId(campaign, name, parent_id);
		
	}
	
	
	/**
	 * Get all of the institutes in the database.
	 * 
	 * @rerturn an array of Institute objects
	 */
	public Institute[] getInstitutes() throws SPECCHIOClientException {
		
		return realClient.getInstitutes();
		
	}
	
	
	/**
	 * Get an instrument.
	 * 
	 * @param instrument_id	the instrument identifier
	 * 
	 * @return a new Instrument object, or null if the instrument does not exist
	 */
	public Instrument getInstrument(int instrument_id) throws SPECCHIOClientException {
		
		return realClient.getInstrument(instrument_id);
		
	}
	
	
	/**
	 * Get the calibration metadata for an instrument.
	 * 
	 * @param instrument_id	the instrument identifier
	 * 
	 * @return an array of CalibrationMetadata objects, or null if the instrument does not exist
	 */
	public CalibrationMetadata[] getInstrumentCalibrationMetadata(int instrument_id) throws SPECCHIOClientException {
		
		return realClient.getInstrumentCalibrationMetadata(instrument_id);
		
	}
	
	
	/**
	 * Get descriptors for all of the instruments in the database.
	 * 
	 * @return an array of InstrumentDescriptor objects
	 */
	public InstrumentDescriptor[] getInstrumentDescriptors() throws SPECCHIOClientException {
		
		return realClient.getInstrumentDescriptors();
		
	}
	
	
	/**
	 * Get all of the pictures for an instrument.
	 * 
	 * @param instrument_id	the instrument identifier
	 * 
	 * @return a PictureTable object containing all of the pictures for this instrument, or null if the instrument doesn't exist
	 */
	public PictureTable getInstrumentPictures(int instrument_id) throws SPECCHIOClientException {
		
		return realClient.getInstrumentPictures(instrument_id);
		
	}
	
	
	/**
	 * Get a user object representing the user under which the client is logged in.
	 * 
	 * @return a reference to a user object, or null if the client is not connected
	 */
	public User getLoggedInUser() {
		
		return realClient.getLoggedInUser();
		
	}
	
	
	/**
	 * Get the metadata categories for a metadata field.
	 * 
	 * @param field	the field name
	 * 
	 * @return a CategoryTable object, or null if the field does not exist
	 */
	public CategoryTable getMetadataCategories(String field) throws SPECCHIOClientException {
		
		if (!metadataCategories.containsKey(field)) {
			metadataCategories.put(field, realClient.getMetadataCategories(field));
		}
		
		return metadataCategories.get(field);
		
	}
	
	
	/**
	 * Get values for spectrum ids and EAV attribute
	 * 
	 * @param ids				spectrum ids
	 * @param attribute_name	attribute name
	 * 
	 * @return list of values, or null if the field does not exist
	 */	
	public MatlabAdaptedArrayList<Object> getMetaparameterValues(ArrayList<Integer> ids, String attribute_name) throws SPECCHIOWebClientException {
		
		return realClient.getMetaparameterValues(ids, attribute_name);
		
	}
	
	/**
	 * Get the metadata conflicts for a set of spectra and set of fields.
	 * 
	 * @param spectrum_ids	the spectrum identifiers
	 * @param fields		the fields to check
	 * 
	 * @return a ConflictTable object listing the conflicts found
	 */
	public ConflictTable getMetadataConflicts(ArrayList<Integer> spectrum_ids, String fields[]) throws SPECCHIOClientException {
		
		return realClient.getMetadataConflicts(spectrum_ids, fields);
		
	}
	
	/**
	 * Get measurement unit for ASD based coding.
	 * 
	 * @param coding	coding based on ASD coding
	 * 
	 * @return a new MeasurementUnit object, or null if the coding does not exist
	 */	
	public MeasurementUnit getMeasurementUnitFromCoding(int coding) throws SPECCHIOWebClientException {
		
		return realClient.getMeasurementUnitFromCoding(coding);
		
	}
	
	/**
	 * Get the data usage policies for a space.
	 * 
	 * @param space	the space
	 * 
	 * @return an array of Policy objects
	 */
	public String[] getPoliciesForSpace(Space space) throws SPECCHIOClientException {
		
		return realClient.getPoliciesForSpace(space);
		
	}
	
	
	/**
	 * Get a reference.
	 * 
	 * @param reference_id	the reference identifier
	 * 
	 * @return a new Reference object, or null if the instrument does not exist
	 */
	public Reference getReference(int reference_id) throws SPECCHIOClientException {
		
		return realClient.getReference(reference_id);
		
	}
	
	
	/**
	 * Get the calibration metadata for a reference.
	 * 
	 * @param reference_id	the reference identifier
	 * 
	 * @return an array of CalibrationMetadata objects, or null if the reference does note exist
	 */
	public CalibrationMetadata[] getReferenceCalibrationMetadata(int reference_id) throws SPECCHIOClientException {
		
		return realClient.getReferenceCalibrationMetadata(reference_id);
		
	}
	
	
	/**
	 * Get descriptors for all of the references in the database.
	 * 
	 * @return an arra of ReferenceDescriptor objects
	 */
	public ReferenceDescriptor[] getReferenceDescriptors() throws SPECCHIOClientException {
		
		return realClient.getReferenceDescriptors();
		
	}
	
	
	/**
	 * Get all of the reference brands in the database.
	 * 
	 * @return an array of ReferenceBrand objects
	 */
	public ReferenceBrand[] getReferenceBrands() throws SPECCHIOClientException {
		
		return realClient.getReferenceBrands();
		
	}
	
	
	/**
	 * Get all of the pictures associated with a reference.
	 * 
	 * @param reference_id	the reference identifier
	 * 
	 * @return a PictureTable containing all of the pictures
	 */
	public PictureTable getReferencePictures(int reference_id) throws SPECCHIOClientException {
		
		return realClient.getReferencePictures(reference_id);
		
	}
	
	
	/**
	 * Get a reference space.
	 * 
	 * @param input_ids
	 * @param local_ids
	 * 
	 * @return a Space object, or null if no space could be found
	 */
	public ReferenceSpaceStruct getReferenceSpace(ArrayList<Integer> input_ids) throws SPECCHIOClientException {
		
		return realClient.getReferenceSpace(input_ids);
		
	}
	
	
	/**
	 * Get a server descriptor that describes the server to which this client is connected.
	 * 
	 * @return a new server descriptor object
	 */
	public SPECCHIOServerDescriptor getServerDescriptor() {
		
		return realClient.getServerDescriptor();
		
	}
	
	
	/**
	 * Get all of the sensors in the database.
	 * 
	 * @return an array of Sensor objects
	 */
	public Sensor[] getSensors() throws SPECCHIOClientException {
		
		return realClient.getSensors();
		
	}
	
	
	/**
	 * Get sensor sampling geometry 
	 * 
	 * @param space		holds spectra ids of which geometry is to retrieved
	 * 
	 * @return a new GonioSamplingPoints, or null if no geometries were found
	 * @throws SPECCHIOWebClientException 
	 */		
	public GonioSamplingPoints getSensorSamplingGeometry(SpectralSpace space) throws SPECCHIOWebClientException {
		
		return realClient.getSensorSamplingGeometry(space);
		
	}


	/**
	 * Get the space objects for a set of spectrum identifiers.
	 * 
	 * @param ids								the spectrum identifiers
	 * @param split_spaces_by_sensor
	 * @param split_spaces_by_sensor_and_unit
	 * @param order_by							the field to order by
	 */
	public Space[] getSpaces(ArrayList<Integer> ids, boolean split_spaces_by_sensor, boolean split_spaces_by_sensor_and_unit, String order_by) throws SPECCHIOClientException {
		
		return realClient.getSpaces(ids, split_spaces_by_sensor, split_spaces_by_sensor_and_unit, order_by);
		
	}
	
	
	/**
	 * Get a spectrum.
	 * 
	 * @param spectrum_id	the spectrum identifier
	 * @param load_metadata	load all spectrum metadata?
	 * 
	 * @return a Spectrum object
	 */
	public Spectrum getSpectrum(int spectrum_id, boolean load_metadata) throws SPECCHIOClientException {
		
		return realClient.getSpectrum(spectrum_id, load_metadata);
		
	}
	
	
	/**
	 * Get the calibration spaces for a set of spectra.
	 * 
	 * @param spectrum_ids	the spectrum identifiers
	 * 
	 * @return an array of Space objects
	 */
	public Space[] getSpectrumCalibrationSpaces(ArrayList<Integer> spectrum_ids) throws SPECCHIOClientException {
		
		return realClient.getSpectrumCalibrationSpaces(spectrum_ids);
		
	}
	
	
	/**
	 * Get the spectrum factor table for a set of spectra.
	 * 
	 * @param spectrum_ids_1
	 * @param spectrum_ids_2
	 * 
	 * @return a SpectrumFactorTable object
	 */
	public SpectrumFactorTable getSpectrumFactorTable(ArrayList<Integer> spectrum_ids_1, ArrayList<Integer> spectrum_ids_2) throws SPECCHIOClientException {
		
		return realClient.getSpectrumFactorTable(spectrum_ids_1, spectrum_ids_2);
		
	}
	
	
	/**
	 * Get the identifiers of all spectra beneath a given node of the spectral data browser
	 * 
	 * @param sn	the node
	 * 
	 * @return a list of spectrum identifiers
	 */
	public List<Integer> getSpectrumIdsForNode(spectral_node_object sn) throws SPECCHIOClientException {
		
		return realClient.getSpectrumIdsForNode(sn);
		
	}
	
	
	/**
	 * Get the spectrum identifiers that match a given query.
	 * 
	 * @param query	the query
	 * 
	 * @return an array list of spectrum identifiers that match the query
	 */
	public ArrayList<Integer> getSpectrumIdsMatchingQuery(Query query) throws SPECCHIOClientException {
		
		return realClient.getSpectrumIdsMatchingQuery(query);
		
	}
	
	
	/**
	 * Get the pictures associated with a spectrum.
	 * 
	 * @param spectrum_id	the spectrum identifier
	 * 
	 * @return a PictureTable containing all of the pictures associated with the spectrum
	 */
	public PictureTable getSpectrumPictures(int spectrum_id) throws SPECCHIOClientException {
		
		return realClient.getSpectrumPictures(spectrum_id);
		
	}
	
	
	/**
	 * Get the number of spectra that matach a given query.
	 * 
	 * @param query	the query
	 * 
	 * @return the number of spectra that match the query
	 */
	public int getSpectrumQueryCount(Query query) throws SPECCHIOClientException {
		
		return realClient.getSpectrumQueryCount(query);
		
	}
	
	
	/**
	 * Get the spectrum data links that refer to a given target and/or reference.
	 * 
	 * @param target_id		the identifier of the target spectrum (0 to match all targets)
	 * @param reference_id	the identifier of the reference spectrum (0 to match all references)
	 * 
	 * @returns an array of SpectrumDataLink objects
	 */
	public SpectrumDataLink[] getTargetReferenceLinks(int target_id, int reference_id) throws SPECCHIOClientException {
		
		return realClient.getTargetReferenceLinks(target_id, reference_id);
		
	}
	
		
	/**
	 * Get the top node for a given taxonomy
	 * 
	 * @param attribute_id	id of the attribute that defines the taxonomy
	 */
	public TaxonomyNodeObject getTaxonomyRootNode(int attribute_id) throws SPECCHIOClientException {
		
		return realClient.getTaxonomyRootNode(attribute_id);
		
	}
	
	
	/**
	 * Get the node for a given taxonomy
	 * 
	 * @param taxonomy_id	taxonomy_id that defines the taxonomy
	 */
	public TaxonomyNodeObject getTaxonomyNode(int taxonomy_id) throws SPECCHIOClientException {
		
		return realClient.getTaxonomyNode(taxonomy_id);
		
	}
	
	
	/**
	 * Get a list of all of the users in the database.
	 * 
	 * @return an array of User objects
	 * 
	 * @throws SPECCHIOClientException
	 */
	public User[] getUsers() throws SPECCHIOClientException {
		
		return realClient.getUsers();
		
	}
	
	
	/**
	 * Import a campaign.
	 * 
	 * @param user_id	the identifier of the user to whom the campaign will belong
	 * @param is	the input stream from which to read the campaign
	 */
	public void importCampaign(int user_id, InputStream is) throws SPECCHIOClientException {
		
		realClient.importCampaign(user_id, is);
		
	}


	/**
	 * Insert a new campaign into the database
	 * 
	 * @param campaign	the campaign
	 * 
	 * @return the identifier of the new campaign
	 */
	public int insertCampaign(Campaign campaign) throws SPECCHIOClientException {
		
		return realClient.insertCampaign(campaign);
		
	}
	
	
	/**
	 * Insert a hierarchy node.
	 * 
	 * @param campaign	the campaign into which to insert the hierarchy
	 * @param name		the name of the new hierarchy
	 * @param parent_id	the identifier of the parent of the new hierarchy
	 * 
	 * @return the identifier of the new hierarchy node
	 */
	public int insertHierarchy(Campaign campaign, String name, int parent_id) throws SPECCHIOClientException {
		
		return realClient.insertHierarchy(campaign, name, parent_id);
		
	}
	
	
	/**
	 * Insert a new institute into the database
	 * 
	 * @param institute	an Instite object describing the new institute
	 * 
	 * @return the identifier of the new institute
	 */
	public int insertInstitute(Institute institute) throws SPECCHIOClientException {
		
		return realClient.insertInstitute(institute);
		
	}
	
	
	/**
	 * Insert calibration for an instrument.
	 * 
	 * @param c		the calibration data
	 */
	public void insertInstrumentCalibration(Calibration c) throws SPECCHIOClientException {
		
		realClient.insertInstrumentCalibration(c);
		
	}
	
	
	/**
	 * Insert a picture of an instrument into the database.
	 * 
	 * @param picture	the picture
	 */
	public void insertInstrumentPicture(Picture picture) throws SPECCHIOClientException {
		
		realClient.insertInstrumentPicture(picture);
		
	}
	
	
	/**
	 * Insert calibration for a reference into the database.
	 * 
	 * @param c		the calibration data
	 */
	public void insertReferenceCalibration(Calibration c) throws SPECCHIOClientException {
		
		realClient.insertReferenceCalibration(c);
		
	}
	
	
	/**
	 * Insert a picture associated with a reference into the database.
	 * 
	 * @param picture	the picture
	 */
	public void insertReferencePicture(Picture picture) throws SPECCHIOClientException {
		
		realClient.insertReferencePicture(picture);
		
	}
	
	
	/**
	 * Insert a spectral file into the database.
	 * 
	 * @param spec_file	the file
	 * 
	 * @return a list of spectrum identifiers that were inserted into the database
	 */
	public List<Integer> insertSpectralFile(SpectralFile spec_file) throws SPECCHIOClientException {
		
		return realClient.insertSpectralFile(spec_file);
		
	}
	
	
	/**
	 * Insert a target-reference link.
	 * 
	 * @param target_id		the identifier of the target node
	 * @param reference_ids	the identifiers of the reference nodes
	 * 
	 * @return the number of links sucessfully created
	 * 
	 * @throws SPECCHIOClientException
	 */
	public int insertTargetReferenceLinks(int target_id, ArrayList<Integer> reference_ids) throws SPECCHIOClientException {
		
		return realClient.insertTargetReferenceLinks(target_id, reference_ids);
		
	}


	/**
	 * Test whether or not the client is logged in under a given role.
	 * 
	 * @param roleName	the role to be tested
	 * 
	 * @return "true" if the client is logged in as a user with role roleIn, "false" otherwise
	 */
	public boolean isLoggedInWithRole(String roleName) throws SPECCHIOClientException {
		
		return realClient.isLoggedInWithRole(roleName);
		
	}
	
	
	/**
	 * Load a sensor definition into the database from an input stream.
	 * 
	 * @param is	the input stream
	 */
	public void loadSensor(InputStream is) throws SPECCHIOClientException {
		
		realClient.loadSensor(is);
		
	}
	
	
	/**
	 * Load a Space object.
	 * 
	 * @param space	a partially-filled space object
	 * 
	 * @return a complete Space object
	 */
	public Space loadSpace(Space space) throws SPECCHIOClientException {
		
		return realClient.loadSpace(space);
		
	}
	
	
	/**
	 * Remove an item of EAV metadata.
	 * 
	 * @param mp	the meta-parameter to be removed
	 */
	public void removeEavMetadata(MetaParameter mp) throws SPECCHIOClientException {
		
		realClient.removeEavMetadata(mp);
		
	}
	
	
	/**
	 * Remove an item of EAV metadata for a collection of spectra.
	 * 
	 * @param mp			the meta-parameter to be removed
	 * @param spectrum_ids	the spectrum identifiers
	 */
	public void removeEavMetadata(MetaParameter mp, ArrayList<Integer> spectrum_ids) throws SPECCHIOClientException {
		
		realClient.removeEavMetadata(mp, spectrum_ids);
		
	}

	/**
	 * Remove one or more items of EAV metadata for a collection of spectra.
	 * 
	 * @param attr			the attribute to be removed
	 * @param spectrum_ids	the spectrum identifiers
	 */	
	public void removeEavMetadata(attribute attr, ArrayList<Integer> spectrum_ids) throws SPECCHIOClientException {
		
		realClient.removeEavMetadata(attr, spectrum_ids);
		
	}
	
	
	
	
	/**
	 * Remove the data corresponding to a node of the spectral data browser.
	 * 
	 * @param sn	the node to be removed
	 */
	public void removeSpectralNode(spectral_node_object sn) throws SPECCHIOClientException {
		
		realClient.removeSpectralNode(sn);
		
	}
	
	
	/**
	 * Set the progress report interface to which progress made by this
	 * client will be reported.
	 * 
	 * @param pr	the progress report; use null to report no progress
	 */
	public void setProgressReport(ProgressReportInterface pr) {
		
		this.pr = pr;
		realClient.setProgressReport(pr);
		
	}
	
	
	/**
	 * Test for the existence of a spectral file in the database.
	 * 
	 * @param campaign		the campaign to which the file belongs
	 * @param hierarchy_id	the node under which the file belongs
	 * @param filename		the file name
	 * 
	 * @return true if the file already exists in the database, false otherwise
	 */
	public boolean spectralFileExists(SpectralFile spec_file) throws SPECCHIOClientException {
		
		return realClient.spectralFileExists(spec_file);
		
	}
	
	
	/**
	 * Submit a collection to Research Data Australia.
	 * 
	 * @param collection_d	the collection descriptor
	 * 
	 * @return the collection identifier of the new collection, or an empty string if publication failed
	 * 
	 * @throws SPECCHIOClientException	could not contact the server
	 */
	public String submitRDACollection(RDACollectionDescriptor collection_d) throws SPECCHIOClientException {
		
		return realClient.submitRDACollection(collection_d);
		
	}
		
	
	/**
	 * Update the information about a campaign
	 * 
	 * @param campaign	the new campaign data
	 */
	public void updateCampaign(Campaign campaign) throws SPECCHIOClientException {
		
		realClient.updateCampaign(campaign);
		
	}
	
	
	/**
	 * Update EAV metadata.
	 * 
	 * @param mp			the meta-parameter to update
	 * @param spectrum_ids	the identifiers for which to update the parameter
	 * 
	 * @return the identifier of the inserted metadata
	 */
	public int updateEavMetadata(MetaParameter mp, ArrayList<Integer> spectrum_ids) throws SPECCHIOClientException {
		
		return realClient.updateEavMetadata(mp, spectrum_ids);
		
	}
	
	
	/**
	 * Update EAV metadata.
	 * 
	 * @param mp			the meta-parameter to update
	 * @param spectrum_ids	the identifiers for which to update the parameter
	 * @param mp_old		the old meta-parameter
	 * 
	 * @return the identifier of the inserted metadata
	 */
	public int updateEavMetadata(MetaParameter mp, ArrayList<Integer> spectrum_ids, MetaParameter mp_old) throws SPECCHIOClientException {
		
		return realClient.updateEavMetadata(mp, spectrum_ids, mp_old);
		
	}
	
	
	/**
	 * Update an instrument.
	 * 
	 * @param instrument	the instrument
	 */
	public void updateInstrument(Instrument instrument) throws SPECCHIOClientException {
		
		realClient.updateInstrument(instrument);
		
	}
	
	
	/**
	 * Update the calibration metadata for an instrument.
	 * 
	 * @param cm	the calibration metadata
	 */
	public void updateInstrumentCalibrationMetadata(CalibrationMetadata cm) throws SPECCHIOClientException {
		
		realClient.updateInstrumentCalibrationMetadata(cm);
		
	}
	
	
	/**
	 * Update a picture associated with an instrument.
	 * 
	 * @param picture	the picture
	 */
	public void updateInstrumentPicture(Picture picture) throws SPECCHIOClientException {
		
		realClient.updateInstrumentPicture(picture);
		
	}
	
	
	/**
	 * Update an reference.
	 * 
	 * @param reference	the reference
	 */
	public void updateReference(Reference reference) throws SPECCHIOClientException {
		
		realClient.updateReference(reference);
		
	}
	
	
	/**
	 * Update the calibration metadata for a reference.
	 * 
	 * @param cm	the calibration metadata
	 */
	public void updateReferenceCalibrationMetadata(CalibrationMetadata cm) throws SPECCHIOClientException {
		
		realClient.updateReferenceCalibrationMetadata(cm);
		
	}
	
	
	/**
	 * Update a picture associated with a reference.
	 * 
	 * @param picture	the picture
	 */
	public void updateReferencePicture(Picture picture) throws SPECCHIOClientException {
		
		realClient.updateReferencePicture(picture);
		
	}
	
	
	/**
	 * Update the metadata fields for a set of spectra
	 * 
	 * @param spectrum_ids	the spectrum identifiers
	 * @param field			the name of the field to be updated
	 * @param id
	 */
	public void updateSpectraMetadata(ArrayList<Integer> ids, String field, int id) throws SPECCHIOClientException {
		
		realClient.updateSpectraMetadata(ids, field, id);
		
	}
	
	
	/**
	 * Update the information about a user.
	 * 
	 * @param user	the user data
	 * 
	 * @throws SPECCHIOClientException
	 */
	public void updateUser(User user) throws SPECCHIOClientException {
		
		realClient.updateUser(user);
		
	}

}
