package ch.specchio.metadata;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.Category;

public class MDE_FormFactory {
	
	/** the client */
	private SPECCHIOClient specchioClient;
	
	
	/**
	 * Constructor.
	 * 
	 * @param specchioClient		the client object to use for connecting to the server
	 * 
	 */
	public MDE_FormFactory(SPECCHIOClient specchioClient) {
		
		// save references to the parameters
		this.specchioClient = specchioClient;
		
	}
	
	
	/**
	 * Add a category to a form.
	 *
	 * @param form	the form
	 * @param name	the name of the category to add
	 * 
	 * @throws SPECCHIOClientException	could not download the category information from the server
	 */
	private void addCategory(MDE_Form form, String name) throws SPECCHIOClientException {
		
		MD_CategoryContainer mdcc = form.addCategoryContainer(name);
		
		// add non-EAV fields to the container if appropriate
		if (name.equals("General")) {
			form.addFieldToContainer(mdcc, new MD_Spectrum_Field(specchioClient, "measurement_unit_id", "Measurement Unit"));
			form.addFieldToContainer(mdcc, new MD_Spectrum_Field(specchioClient, "file_format_id", "File Format"));
		} else if (name.equals("Instrument")) {
			form.addFieldToContainer(mdcc, new MD_Spectrum_Field(specchioClient, "sensor_id", "Sensor"));
			form.addFieldToContainer(mdcc, new MD_Spectrum_Field(specchioClient, "instrument_id", "Instrument"));
		}
		 else if (name.equals("Instrumentation")) {
			form.addFieldToContainer(mdcc, new MD_Spectrum_Field(specchioClient, "reference_id", "Reference"));
		}	
		
	}
	
	
	/**
	 * Get a form.
	 * 
	 * @param d		the descriptor of the desired form
	 * 
	 * @returns a new MDE_Form object of the named type
	 * 
	 * @throws SPECCHIOClientException	could not download category information from the server
	 */
	public MDE_Form getForm(MD_FormDescriptor d) throws SPECCHIOClientException
	{
		// create the form
		MDE_Form f = new MDE_Form(specchioClient);
		
		// add the categories to the form
		for (Category category : d.getCategories()) {
			addCategory(f, category.name);
		}
		
		return f;
		
	}
	
	
	/**
	 * Get a form descriptor.
	 * 
	 * @returns a new MDE_FormDescriptor object containing all of the categories known to the server
	 * 
	 * @throws SPECCHIOClientException	error contacting the server
	 */
	public MD_FormDescriptor getDefaultFormDescriptor() throws SPECCHIOClientException {
		
		return new MD_FormDescriptor(specchioClient.getCategoriesInfo(), new MD_CategoryComparator());
		
	}

}
