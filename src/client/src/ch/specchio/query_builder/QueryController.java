package ch.specchio.query_builder;

import java.util.ArrayList;
import java.util.ListIterator;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.metadata.MD_CategoryComparator;
import ch.specchio.queries.QueryConditionChangeInterface;
import ch.specchio.queries.QueryCondition;

public class QueryController {
	
	QueryFormFactory form_factory;
	
	QueryForm form;
	SPECCHIOClient specchio_client;
	
	private ArrayList<QueryConditionChangeInterface> change_listeners = new ArrayList<QueryConditionChangeInterface>();
	

	public QueryController(SPECCHIOClient specchio_client, String form_name) throws SPECCHIOClientException {
		
		this.specchio_client = specchio_client;
		form_factory = new QueryFormFactory(specchio_client, new MD_CategoryComparator());
		form = form_factory.getForm(this.specchio_client, form_factory.getDefaultFormDescriptor());
		form.textReport();
	}

	public void ConditionChange(QueryField field, Object new_value)
	{
		
		String string_value = (new_value != null)? new_value.toString() : "";
		
		System.out.println("Change:" + field.getLabel() + ", value = " + string_value);
		
		field.set_value(string_value);
		
		// inform all registered listeners
		ListIterator<QueryConditionChangeInterface> li = change_listeners.listIterator();
		
		while(li.hasNext())
		{
			QueryConditionChangeInterface listener = li.next();
			
			listener.changed(this);
		}
		
	}
	
	public void addChangeListener(QueryConditionChangeInterface listener)
	{
		this.change_listeners.add(listener);
	}
	
	public QueryForm getForm() {
		
		return form;
		
	}
	
	public ArrayList<QueryCondition> getListOfConditions()
	{		
		return form.getListOfConditions();
	}
	
}
