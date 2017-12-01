package ch.specchio.metadata;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ListIterator;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.ConflictInfo;
import ch.specchio.types.ConflictStruct;
import ch.specchio.types.ConflictTable;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetadataInterface;
import ch.specchio.types.Spectrum;
import ch.specchio.types.attribute;


public class MDE_Form {
	
	SPECCHIOClient specchio_client;
	ArrayList<MD_CategoryContainer> containers = new ArrayList<MD_CategoryContainer>();
	ArrayList<Integer> ids;
	boolean add_spectrum_table_fields = false;
	


	Hashtable<String, MD_Spectrum_Field> spectrum_fields = new Hashtable<String, MD_Spectrum_Field>();

	
	
	public MDE_Form(SPECCHIOClient specchio_client)
	{
		this.specchio_client = specchio_client;
		this.ids = null;
	}
	
	public MD_CategoryContainer addCategoryContainer(String category, int index) throws SPECCHIOClientException
	{
		// create a new category container
		MD_CategoryContainer cc = new MD_CategoryContainer(category);
		containers.add(index, cc);
		
		// populate the attributes for this category
		attribute[] attr_array = specchio_client.getAttributesForCategory(cc.getCategoryName());
		for (attribute attr : attr_array) {
			cc.addPossibleEAVField(attr);
		}
		
		return cc;
		
	}	
	
	
	public MD_CategoryContainer addCategoryContainer(String category) throws SPECCHIOClientException
	{
		// create a new category container
		MD_CategoryContainer cc = new MD_CategoryContainer(category);
		containers.add(cc);
		
		// populate the attributes for this category
		attribute[] attr_array = specchio_client.getAttributesForCategory(cc.getCategoryName());
		for (attribute attr : attr_array) {
			cc.addPossibleEAVField(attr);
		}
		
		return cc;
		
	}
	
	
	public void addEavParameterIntoExistingContainer(MetaParameter mp, ConflictInfo info)
	{
		boolean found = false;
		MD_EAV_Field field;
		ListIterator<MD_CategoryContainer> li  = containers.listIterator();
		while (li.hasNext() && !found) {
			MD_CategoryContainer cc = li.next();
			if (cc.getCategoryName().equals(mp.getCategoryName())) {
				
				if(mp.getAttributeName().equals("Reference Data Link") || mp.getAttributeName().equals("Target Data Link") || mp.getAttributeName().equals("Provenance Data Link") )
				{
					field = new MD_EAV_Link_Field(mp, info);
				}					
				else
				{				
					field = new MD_EAV_Field(mp, info);
				}
				cc.addField(field);
				found = true;
			}
		}
	
	}
	
	
	public MetaParameter getEavParameterFromContainer(String attribute_name, String category_name)
	{	
		MetaParameter mp = null;
		boolean found = false;
		
		MD_EAV_Field field;
		ListIterator<MD_CategoryContainer> li  = containers.listIterator();
		while (li.hasNext() && !found) {
			MD_CategoryContainer cc = li.next();
			if (cc.getCategoryName().equals(category_name)) {

				field = cc.getField(attribute_name);
				
				if(field != null && field.get_conflict_status() == ConflictInfo.no_conflict) // only returned for non-conflicting fields
				{
					mp = field.getMetaParameter();
					found = true;
				}

			}
		}
		
		return mp;
	}
	
	public void addParametersIntoExistingContainers(MetadataInterface s,
			String[] md_fields, ConflictTable spectrum_md_conflict_stati) {
		
		
		for (int i=0;i<md_fields.length;i++)
		{		
		
			MD_Spectrum_Field field = spectrum_fields.get(md_fields[i]);
			
			if (field != null)
			{
				// generic way of finding the fields in the Spectrum class, based on the assumption that the fields are named the same ...

				Class<Spectrum>  aClass = Spectrum.class;
				try {
					java.lang.reflect.Field relevant_spectrum_field = aClass.getField(md_fields[i] + "_id");
					
					Integer value = (Integer) relevant_spectrum_field.get(s);
					
					field.setId(value);
					
					field.set_conflict_status(spectrum_md_conflict_stati.get(md_fields[i]));				
					
					
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					
					System.out.println(md_fields[i] + " field not found in Spectrum class");
					
					
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		
		}
		
	}
	
	
	public void removeField(MD_Field field)
	{
		
		// iterate over all metadata entries
		ListIterator<MD_CategoryContainer> li = containers.listIterator();
		
		
		while(li.hasNext())
		{
			MD_CategoryContainer cc = li.next();
			
			cc.removeField(field);
		}
		
	}
	
	
	public void addFieldToContainer(MD_CategoryContainer c,
			MD_Spectrum_Field md_Spectrum_Field) {
		// TODO Auto-generated method stub
		
		c.addField(md_Spectrum_Field);
		
		this.spectrum_fields.put(md_Spectrum_Field.db_field_name, md_Spectrum_Field); // reference list to speed up assignments
		
	}
	
	
	public Hashtable<String, MD_Spectrum_Field> getStaticFields()
	{
		return this.spectrum_fields;
	}
	
	
	public ArrayList<MD_CategoryContainer> getContainers()
	{
		return this.containers;
	}

	
	public void textReport()
	{
		ListIterator<MD_CategoryContainer> li = containers.listIterator();
		
		
		while(li.hasNext())
		{			
			li.next().textReport();
					
		}
	}
	
	
	public void set_ids(ArrayList<Integer> ids)
	{
		this.ids = ids;
	}

	
	public MD_EAV_Field createEAVField(MetaParameter mp) {
		
		ConflictStruct cs = new ConflictStruct(1, ids.size(), ids.size()); // configure as no conflict with no of sharing and selected records equal to number of currently selected spectra
		if(mp.getLevel() == MetaParameter.HIERARCHY_LEVEL)
			cs.setInherited(true);
		ConflictInfo conflict = new ConflictInfo();
		conflict.addConflict(mp.getEavId(), cs);

		return new  MD_EAV_Field(mp, conflict);
		
	}
	
	public boolean DoAdd_spectrum_table_fields() {
		return add_spectrum_table_fields;
	}

	public void setAdd_spectrum_table_fields(boolean add_spectrum_table_fields) {
		this.add_spectrum_table_fields = add_spectrum_table_fields;
	}
	
	

}
