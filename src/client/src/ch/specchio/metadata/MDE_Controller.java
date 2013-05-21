package ch.specchio.metadata;

import java.util.ArrayList;
import java.util.ListIterator;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.Campaign;
import ch.specchio.types.ConflictTable;
import ch.specchio.types.MetaParameter;
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
	
	
	public MDE_Controller(SPECCHIOClient specchio_client) throws SPECCHIOClientException
	{
		this.form_factory = new MDE_FormFactory(specchio_client);
		this.specchio_client = specchio_client;
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
			
			// get metadata for the first spectrum
			// the decision if it can be displayed is based on the conflict detection

			// get spectrum metadata and eav metadata
			Spectrum s;
			s = specchio_client.getSpectrum(ids.get(0), false);
				
			eav_md = s.getEavMetadata();

			// deal with EAV
			ConflictTable eav_conflict_stati = specchio_client.getEavMetadataConflicts(ids);

			// add parameters including their conflict status
			form.addEavParametersIntoExistingContainers(eav_md, eav_conflict_stati);

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
			MD_EAV_Field eav_field = (MD_EAV_Field) field;
			MetaParameter mp = eav_field.getMetaParameter();
			mp.setValue(field.getNewValue());
			int eav_id = specchio_client.updateEavMetadata(mp, ids);
			eav_field.setEavId(eav_id);
			
		}	
		
		if(field instanceof MD_Spectrum_Field)
		{
			MD_Spectrum_Field spectrum_field = (MD_Spectrum_Field)field;
			spectrum_field.setId((Integer) field.getNewValue());
			specchio_client.updateSpectraMetadata(ids, spectrum_field.db_field_name, spectrum_field.getId());			
		}
		
		
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
			MD_EAV_Field eav_field = (MD_EAV_Field) field;
			MetaParameter mp = eav_field.getMetaParameter();
			mp.setValue(field.getNewValue());
			int eav_id = specchio_client.updateEavMetadata(mp, ids, mp);
			eav_field.setEavId(eav_id);
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
