package ch.specchio.metadata;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.client.SPECCHIOWebClientException;
import ch.specchio.types.Campaign;
import ch.specchio.types.ConflictTable;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.Metadata;
import ch.specchio.types.Spectrum;
import ch.specchio.types.attribute;

public class MDE_Controller {
	
	Metadata eav_md;
	MDE_FormFactory form_factory;
	ArrayList<Integer> ids;
	Campaign campaign;
	SPECCHIOClient specchio_client;
	MDE_Form form;
	MD_FormDescriptor form_descriptor;
	Hashtable<Integer, attribute> attributes;
	
	
	public MDE_Controller(SPECCHIOClient specchio_client) throws SPECCHIOClientException
	{
		this.form_factory = new MDE_FormFactory(specchio_client);
		this.specchio_client = specchio_client;
		this.attributes = specchio_client.getAttributesIdHash();
		set_form_descriptor(this.form_factory.getDefaultFormDescriptor());
	}
	
	
	public Campaign getCampaign()
	{
		return campaign;
	}
	
	
	public ArrayList<Integer> getIds()
	{	
		return ids;
		
	}
	
	
	public MDE_FormFactory getFormFactory()
	{
		return form_factory;
	}
	
	
	public void set_campaign(Campaign campaign)
	{
		this.campaign = campaign;
	}
	
	
	public void set_form_descriptor(MD_FormDescriptor form_descriptor) throws SPECCHIOClientException
	{
		this.form_descriptor = form_descriptor;	
		update_form();
	}
	
	
	public void set_spectrum_ids(ArrayList<Integer> ids) throws SPECCHIOClientException
	{	
		this.ids = ids;	
		update_form();
	}
	
	
	private void update_form() throws SPECCHIOClientException
	{
		if (ids != null && ids.size() > 0)
		{
			// get form					
			form = form_factory.getForm(form_descriptor);
			form.set_spectrum_ids(ids);
			
			// need to get the first spectrum so that we can display non-conflicting values
			Spectrum s = specchio_client.getSpectrum(ids.get(0), false);

			// add EAV parameters including their conflict status
			ConflictTable eav_conflict_stati = specchio_client.getEavMetadataConflicts(ids);
			Enumeration<String> conflicts = eav_conflict_stati.conflicts();
			while (conflicts.hasMoreElements()) {
				try {
					
					int attribute_id = Integer.parseInt(conflicts.nextElement());
					List<MetaParameter> mps = s.getMetadata().get_all_entries(attribute_id);
					if (mps.size() > 0) {
						
						// add a field for every instance of this attribute
						for (MetaParameter mp : mps) {
							form.addEavParameterIntoExistingContainer(mp, eav_conflict_stati.get(attribute_id));
						}
						
					} else {
						
						// the first spectrum doesn't have this metadata, so it must be in conflict and we can use an empty metaparameter
						MetaParameter mp = MetaParameter.newInstance(attributes.get(attribute_id));
						mp.setEavId(eav_conflict_stati.get(attribute_id).eavIds().nextElement());
						form.addEavParameterIntoExistingContainer(mp, eav_conflict_stati.get(attribute_id));
					}
					
				}
				catch (NumberFormatException ex) {
					// the server returned a non-numeric attribute id
					throw new SPECCHIOClientException(ex);
				}
			}

			ConflictTable spectrum_md_conflict_stati = specchio_client.getMetadataConflicts(ids, Spectrum.METADATA_FIELDS);

			form.addParametersIntoExistingContainers(s, Spectrum.METADATA_FIELDS, spectrum_md_conflict_stati);
			
		} else {
			form = null;
		}
		
	}
	
	
	
	public MDE_Form getForm()
	{
		return form;
	}


	public void update(ArrayList<MD_Field> changed_fields) throws SPECCHIOClientException {
		
		ListIterator<MD_Field> li = changed_fields.listIterator();
		
		while(li.hasNext())
		{
			MD_Field field = li.next();			
			update(field);						
		}
	}
	
	
	
	public void update(MD_Field field) throws SPECCHIOClientException
	{
		
		if(field instanceof MD_EAV_Field)
		{
			try {
				MD_EAV_Field eav_field = (MD_EAV_Field) field;
				MetaParameter mp = eav_field.getMetaParameter();
				
				mp.setValue(field.getNewValue());

				int old_eav_id = mp.getEavId();
				int eav_id = specchio_client.updateEavMetadata(mp, ids);
				eav_field.setEavId(eav_id, old_eav_id);					


			}
			catch (MetaParameterFormatException ex) {
				// should never happen
				ex.printStackTrace();
			}
			
		}	
		
		if(field instanceof MD_Spectrum_Field)
		{
			MD_Spectrum_Field spectrum_field = (MD_Spectrum_Field)field;
			spectrum_field.setId((Integer) field.getNewValue());
			specchio_client.updateSpectraMetadata(ids, spectrum_field.db_field_name, spectrum_field.getId());			
		}
		
		
	}
	
	
	public void updateAnnotation(MD_Field field) throws SPECCHIOWebClientException {
		
		MD_EAV_Field eav_field = (MD_EAV_Field) field;
		MetaParameter mp = eav_field.getMetaParameter();
		
		mp.setAnnotation(field.getAnnotation());
		
		int eav_id = specchio_client.updateEavMetadataAnnotation(mp, ids);
		
		
	}
	
	
	public void updateCampaign(Campaign campaign) throws SPECCHIOClientException {
		
		this.campaign = campaign;
		specchio_client.updateCampaign(campaign);
		
	}


	public void update_selection(MD_Field field) throws SPECCHIOClientException {
		// this is the case when only a sub-set of a spectral collection are to be assigned a new value
		// the following is done to retain the old value for the non-selected spectra
		// - create a new EAV entry by substituting the value and setting the eav_id to zero
		// - link new entry with spectral subset
		// - remove links to old EAV for spectral subset
		
		if(field instanceof MD_EAV_Field)
		{
			try {
				MD_EAV_Field eav_field = (MD_EAV_Field) field;
				MetaParameter mp = eav_field.getMetaParameter();
				mp.setValue(field.getNewValue());
				int old_eav_id = mp.getEavId();
				int eav_id = specchio_client.updateEavMetadata(mp, ids, mp);
				eav_field.setEavId(eav_id, old_eav_id);
			}
			catch (MetaParameterFormatException ex) {
				// should never happen
				ex.printStackTrace();
			}
		}
	}
	
	public void remove(MD_Field field) throws SPECCHIOClientException
	{
		
		if(field instanceof MD_EAV_Field)
		{
			MD_EAV_Field eav_field = (MD_EAV_Field) field;
			MetaParameter mp = eav_field.getMetaParameter();
			specchio_client.removeEavMetadata(mp);
		}
		if (form != null) {
			form.removeField(field);
		}
	}


	public void remove_selection(MD_Field field) throws SPECCHIOClientException {
		
		MetaParameter mp = ((MD_EAV_Field) field).getMetaParameter();
		specchio_client.removeEavMetadata(mp, ids);
		if (form != null) {
			form.removeField(field);
		}
				
	}


	public void remove_all_mps_of_attribute(MD_Field field) throws SPECCHIOClientException {
		
		MetaParameter mp = ((MD_EAV_Field) field).getMetaParameter();
		attribute attr = specchio_client.getAttributesIdHash().get(mp.getAttributeId());
		specchio_client.removeEavMetadata(attr, ids);
		if (form != null) {
			form.removeField(field);
		}
	}
	
	

}
