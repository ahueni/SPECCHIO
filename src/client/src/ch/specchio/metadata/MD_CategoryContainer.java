package ch.specchio.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.TreeSet;

import ch.specchio.types.MetaParameter;
import ch.specchio.types.attribute;

public class MD_CategoryContainer {
	
	private String category_name;
	private ArrayList<attribute> possible_eav_attributes;
	private Collection<MD_Field> fields = new TreeSet<MD_Field>();
	
	public MD_CategoryContainer(String category_name)
	{
		this.category_name = category_name;
		this.possible_eav_attributes = new ArrayList<attribute>();
	}
	
	public String getCategoryName()
	{
		return this.category_name;
	}
	
	
	public void addField(MD_Field field)
	{
		fields.add(field);				
	}
	
	public void removeField(MD_Field field)
	{
		fields.remove(field);
	}
	
	
	public Collection<MD_Field> getFields()
	{
		return fields;
	}
	
	
	public MD_EAV_Field getField(String attribute_name)
	{
		MD_EAV_Field field = null;
		MD_Field tmp = null;
		boolean found = false;
		
		Iterator<MD_Field> li  = fields.iterator();
		while (li.hasNext() && !found) {
			tmp = li.next();
			if(tmp.getClass() == MD_EAV_Field.class)
			{
				
				MetaParameter mp = ((MD_EAV_Field) tmp).getMetaParameter();
	
				if(mp.getAttributeName().equals(attribute_name))
				{
					found = true;
					field = (MD_EAV_Field) tmp;
				}
			}
		}
		
		return field;
	}
	
	
	public void textReport()
	{
		
		System.out.println("");
		System.out.println("Category: " + this.category_name);
		System.out.println("----------------------------");
		
		for (MD_Field field : fields)
		{			
			field.textReport();
					
		}		
		
		System.out.println("----------------------------");
		System.out.println("");
		
	}


	public void addPossibleEAVField(attribute attr) {
		possible_eav_attributes.add(attr);
		
	}
	
	public ArrayList<attribute> getPossibleEAVFields()
	{
		return possible_eav_attributes;
	}


	

}
