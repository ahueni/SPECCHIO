package ch.specchio.query_builder;

import java.util.Comparator;
import java.util.ListIterator;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.gui.SPECCHIOApplication;
import ch.specchio.metadata.MD_FormDescriptor;
import ch.specchio.types.Category;
import ch.specchio.types.attribute;


public class QueryFormFactory {
	
	/** the client object for contacting the server */
	private SPECCHIOClient specchioClient;
	
	/** the comparator with which to order metadata categories */
	private Comparator<Category> categoryComparator;
	
	/** the template holds all possible query fields; new forms are just re-composed based on the existing components of the template 
	 * Note: query conditions field values are stored in this template, allowing to add and remove categories while retaining
	 * existing conditions in the fields of the categories.
	 * */
	private QueryForm template;
	private static QueryFormFactory instance = null;
	
	
	/**
	 * Constructor.
	 * 
	 * @param specchioClientIn	the client object for contacting the server
	 */
	private QueryFormFactory(SPECCHIOClient specchioClientIn, Comparator<Category> categoryComparatorIn, MD_FormDescriptor d) {
		
		// save input parameters for later
		specchioClient = specchioClientIn;
		categoryComparator = categoryComparatorIn;
		
		template = this.getTemplateForm(specchioClient, d);
	}
	
	public static QueryFormFactory getInstance(SPECCHIOClient specchioClientIn, Comparator<Category> categoryComparatorIn, MD_FormDescriptor d) 
	{
		if(instance == null) {
			instance = new QueryFormFactory(specchioClientIn, categoryComparatorIn, d);
		}
		return instance;
	}    
	
	public static QueryFormFactory getInstance()
	{
		return instance;
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
	
	/**
	 * Initial call to generate the template consisting of all possible query fields
	 */	
	protected QueryForm getTemplateForm(SPECCHIOClient specchio_client, MD_FormDescriptor d) throws SPECCHIOClientException
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


	public QueryForm getForm(SPECCHIOClient specchio_client, MD_FormDescriptor d) {
		
		// build form based on template
		QueryForm f = new QueryForm();
		for (Category category : d.getCategories()) {
			f.addCategoryContainer(this.template.getCategoryContainer(category.name));
		}
		
		return f;	

	}

}
