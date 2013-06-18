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
import ch.specchio.types.Spectrum;
import ch.specchio.types.attribute;


public class MDE_Form {
	
	SPECCHIOClient specchio_client;
	ArrayList<MD_CategoryContainer> containers = new ArrayList<MD_CategoryContainer>();
	ArrayList<Integer> ids;
	
	Hashtable<String, MD_Spectrum_Field> spectrum_fields = new Hashtable<String, MD_Spectrum_Field>();

	
	
	public MDE_Form(SPECCHIOClient specchio_client)
	{
		this.specchio_client = specchio_client;
		this.ids = null;
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
		ListIterator<MD_CategoryContainer> li  = containers.listIterator();
		while (li.hasNext() && !found) {
			MD_CategoryContainer cc = li.next();
			if (cc.getCategoryName().equals(mp.getCategoryName())) {
				MD_EAV_Field field = new MD_EAV_Field(mp, info);
				cc.addField(field);
				found = true;
			}
		}
	
	}
	
	
	public void addParametersIntoExistingContainers(Spectrum s,
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
	
	
	public void set_spectrum_ids(ArrayList<Integer> spectrum_ids)
	{
		ids = spectrum_ids;
	}

	
	public MD_EAV_Field createEAVField(MetaParameter mp) {
		
		ConflictStruct cs = new ConflictStruct(1, ids.size(), ids.size()); // configure as no conflict with no of sharing and selected records equal to number of currently selected spectra
		ConflictInfo conflict = new ConflictInfo();
		conflict.addConflict(mp.getEavId(), cs);

		return new  MD_EAV_Field(mp, conflict);
		
	}
	

}
