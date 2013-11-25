package ch.specchio.query_builder;

import java.util.Comparator;
import java.util.ListIterator;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.metadata.MD_FormDescriptor;
import ch.specchio.types.Category;
import ch.specchio.types.attribute;


public class QueryFormFactory {
	
	/** the client object for contacting the server */
	private SPECCHIOClient specchioClient;
	
	/** the comparator with which to order metadata categories */
	private Comparator<Category> categoryComparator;
	
	
	/**
	 * Constructor.
	 * 
	 * @param specchioClientIn	the client object for contacting the server
	 */
	public QueryFormFactory(SPECCHIOClient specchioClientIn, Comparator<Category> categoryComparatorIn) {
		
		// save input parameters for later
		specchioClient = specchioClientIn;
		categoryComparator = categoryComparatorIn;
		
	}
	
	
	/**
	 * Add a category to a form.
	 *
	 * @param form	the form
	 * @param name	the name of the category to add
	 * 
	 * @throws SPECCHIOClientException	could not download the category information from the server
	 */
	private void addCategory(QueryForm form, String name) throws SPECCHIOClientException {
		
		QueryCategoryContainer c = form.addCategoryContainer(name);
		
		// add non-EAV fields to the container if appropriate
		if (name.equals("General")) {
			form.addFieldToContainer(c, new SpectrumQueryField(specchioClient, "measurement_unit", "Measurement Unit"));
		} else if (name.equals("Instrument")) {
			form.addFieldToContainer(c, new SpectrumQueryField(specchioClient, "sensor", "Sensor"));
			form.addFieldToContainer(c, new SpectrumQueryField(specchioClient, "instrument", "Instrument"));
			form.addFieldToContainer(c, new SpectrumQueryField(specchioClient, "calibration", "Calibration"));
		}
		
	}
	
	
	public QueryForm getForm(SPECCHIOClient specchio_client, MD_FormDescriptor d) throws SPECCHIOClientException
	{
		
		// build form
		QueryForm f = new QueryForm();
		for (Category category : d.getCategories()) {
			addCategory(f, category.name);
		}
		
		// get the EAV parameters for each category
		if (f != null) {
			ListIterator<QueryCategoryContainer> li = f.containers.listIterator();
			while(li.hasNext())
			{
				QueryCategoryContainer cc = li.next();
				attribute attrs[] = specchio_client.getAttributesForCategory(cc.getCategoryName());
				cc.addFields(attrs);
			}
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
		
		return new MD_FormDescriptor(specchioClient.getCategoriesInfo(), categoryComparator);
		
	}

}
