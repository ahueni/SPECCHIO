package ch.specchio.query_builder;

import java.util.ArrayList;
import java.util.ListIterator;

import ch.specchio.queries.EAVQueryConditionObject;
import ch.specchio.queries.QueryCondition;
import ch.specchio.queries.SpectrumQueryCondition;


public class QueryForm {
	
	ArrayList<QueryCategoryContainer> containers = new ArrayList<QueryCategoryContainer>();
	

	public QueryForm() {
	}

	public QueryCategoryContainer addCategoryContainer(String category) {
	
		QueryCategoryContainer cc = new QueryCategoryContainer(category);
		
		containers.add(cc);
		
		return cc;		
		
	}
	
	public void textReport()
	{
		// loop over containers
		ListIterator<QueryCategoryContainer> li = containers.listIterator();
		
		while(li.hasNext())
		{
			QueryCategoryContainer cc = li.next();

			cc.textReport();	
		}
		
	}

	public ArrayList<QueryCategoryContainer> getContainers() {
		return containers;
	}
	
	
	public ArrayList<QueryCondition> getListOfConditions()
	{
		ArrayList<QueryCondition> conds = new  ArrayList<QueryCondition>();
		
		// loop over containers
		ListIterator<QueryCategoryContainer> li = containers.listIterator();
		
		while(li.hasNext())
		{
			EAVQueryConditionObject cond = null;
			QueryCategoryContainer cc = li.next();
			
			ArrayList<QueryField> fields = cc.getFields();
			
			ListIterator<QueryField> field_li = fields.listIterator();
			
			while(field_li.hasNext())
			{
				QueryField field = field_li.next();
				
				// add condition if not empty
				if(field.isSet())
				{
					
					// create new condition
					if(field instanceof EAVQueryField)
					{	
						cond = new EAVQueryConditionObject("eav", "spectrum_x_eav", field.getLabel(), field.get_fieldname());
						cond.setValue(field.getValue());	
						cond.setOperator(field.getOperator());
					}
					else
					{
						cond = new SpectrumQueryCondition("spectrum", field.get_fieldname());
						cond.setValue(field.getValue());
						
					}	
					
					conds.add(cond);					
				}
			}
			
		}
		
		return conds;
	}

	public void addFieldToContainer(QueryCategoryContainer c,
			SpectrumQueryField spectrum_field) {
		
		c.addField(spectrum_field);
		
	}

}
