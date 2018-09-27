package ch.specchio.metadata;

import java.util.ArrayList;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.gui.MetaDataEditorView;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.Spectrum;

public class MDE_Spectrum_Controller extends MDE_Controller {
	
	
	ArrayList<Integer> hierarchy_ids;
	

	public MDE_Spectrum_Controller(SPECCHIOClient specchio_client) throws SPECCHIOClientException {
		super(specchio_client);
		this.metadata_level = MetaParameter.SPECTRUM_LEVEL;
	}
	
	
	public MDE_Spectrum_Controller(SPECCHIOClient specchio_client, MetaDataEditorView metaDataEditorView) {
		super(specchio_client, metaDataEditorView);
		this.metadata_level = MetaParameter.SPECTRUM_LEVEL;
	}

	public void set_hierarchy_ids(ArrayList<Integer> ids) {
		
		this.hierarchy_ids = ids;	

	}
	
	public ArrayList<Integer> get_hierarchy_ids()
	{	
		return hierarchy_ids;
	}	
	

	public void set_spectrum_ids(ArrayList<Integer> ids) throws SPECCHIOClientException
	{	
		this.ids = ids;	
		
		// get conflict info ...
		
		// need to get the first spectrum so that we can display non-conflicting values
		if(ids.size() > 0)
		{
			first_entry = specchio_client.getSpectrum(ids.get(0), false);
	
			// add EAV parameters including their conflict status
			if(do_conflict_detection)
			{
				eav_conflict_stati = specchio_client.getEavMetadataConflicts(MetaParameter.SPECTRUM_LEVEL, ids);
			}
			else
			{
				// speedup: simplify conflict detection by supplying only one spectrum
				ArrayList<Integer> first_id = new ArrayList<Integer>();
				first_id.add(ids.get(0));
				eav_conflict_stati = specchio_client.getEavMetadataConflicts(MetaParameter.SPECTRUM_LEVEL, first_id);
			}		
	
			spectrum_md_conflict_stati = specchio_client.getMetadataConflicts(ids, Spectrum.METADATA_FIELDS);	
		}
		
		update_form(false);
	}	
	
	
	protected void update_form(boolean manual_category_selection) throws SPECCHIOClientException
	{
		if (ids != null && ids.size() > 0 && first_entry.getMetadata() != null)
		{
			super.update_form(manual_category_selection);
			form.addParametersIntoExistingContainers(this.first_entry, Spectrum.METADATA_FIELDS, spectrum_md_conflict_stati);
		}
	}


	@Override
	void create_form() {
		form_descriptor.setAdd_spectrum_table_fields(true);
		form = form_factory.getForm(form_descriptor);
		form.set_ids(ids);	
	}
	
	@Override
	public void metadataFieldAdded(MD_Field field)
	{
		if(field instanceof MD_EAV_Field)
		{
			//((MD_EAV_Field) field).setLevel(MetaParameter.HIERARCHY_LEVEL);
		}
		
		this.added_fields.add(field);
	}

	public void remove_selection(MD_Field field) throws SPECCHIOClientException {

		MetaParameter mp = ((MD_EAV_Field) field).getMetaParameter();
		
		if(mp.getLevel() == MetaParameter.HIERARCHY_LEVEL)
		{
			// data must be removed at hierarchy levels of the selected spectra
			ArrayList<Integer> h_ids = this.specchio_client.getHierarchyIdsOfSpectra(this.getIds());
			specchio_client.removeEavMetadata(mp, h_ids);			
		}
		else
		{				
			specchio_client.removeEavMetadata(mp, ids);
		}
		
		if (form != null) {
			form.removeField(field);
		}

	}	

	public void setOnlyHierarchiesAreSelected(boolean onlyHierarchiesAreSelected) {
		this.onlyHierarchiesAreSelected = onlyHierarchiesAreSelected;
		
	}		

}

