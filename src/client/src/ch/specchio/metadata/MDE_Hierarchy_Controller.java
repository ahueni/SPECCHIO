package ch.specchio.metadata;

import java.util.ArrayList;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.gui.MetaDataEditorView;
import ch.specchio.types.ConflictInfo;
import ch.specchio.types.ConflictStruct;
import ch.specchio.types.Hierarchy;
import ch.specchio.types.MetaParameter;

public class MDE_Hierarchy_Controller extends MDE_Controller {

	public MDE_Hierarchy_Controller(SPECCHIOClient specchio_client) throws SPECCHIOClientException {
		super(specchio_client);
		// TODO Auto-generated constructor stub
	}

	public MDE_Hierarchy_Controller(SPECCHIOClient specchio_client, MetaDataEditorView metaDataEditorView) {
		super(specchio_client, metaDataEditorView);
	}
	
	
	public void set_hierarchy_ids(ArrayList<Integer> ids) {
		
		this.ids = ids;	
		
		// get conflict info ...
		
		// need to get the first spectrum so that we can display non-conflicting values
		if(ids.size() > 0)
		{
			
			first_entry = specchio_client.getHierarchy(ids.get(0));
			
	
			// add EAV parameters including their conflict status
			if(do_conflict_detection)
			{
				eav_conflict_stati = specchio_client.getEavMetadataConflicts(MetaParameter.HIERARCHY_LEVEL, ids);
			}
			else
			{
				// speedup: simplify conflict detection by supplying only one spectrum
				ArrayList<Integer> first_id = new ArrayList<Integer>();
				first_id.add(ids.get(0));
				eav_conflict_stati = specchio_client.getEavMetadataConflicts(MetaParameter.HIERARCHY_LEVEL, first_id);
			}		

		}
		
		update_form(false);
		
	}	
	
	public ArrayList<Integer> get_hierarchy_ids()
	{	
		return ids;
	}		
	
	
	protected void update_form(boolean manual_category_selection) throws SPECCHIOClientException
	{
		if (ids != null && ids.size() > 0)
		{

			super.update_form(manual_category_selection);
			
			if(form == null) 
			{
				// can happen if there are no metadata stored for this hierarchy
				form = form_factory.getForm(form_descriptor);
				form.set_ids(ids);
			}
			


			MD_CategoryContainer c = form.addCategoryContainer("Hierarchy Information", 0);

			MD_Hierarchy_Field f = new MD_Hierarchy_Field(this.specchio_client, "name", "Hierarchy Name");
			
			Hierarchy h = (Hierarchy) this.first_entry;
			
			ConflictInfo ci = new ConflictInfo();
			ConflictStruct cs;
			
			if( ids.size() == 1)
			{
				f.setText(h.getHierarchy_name());		
				// faked conflict till server can deliver hierarchy conflicts
				cs = new ConflictStruct(ConflictInfo.no_conflict, 1, ids.size());
				
			}
			else
			{
				f.setText("-- multiple values --");
				// faked conflict till server can deliver hierarchy conflicts
				cs = new ConflictStruct(ConflictInfo.conflict, 1, ids.size());
			}

			ci.addConflict(0, cs);
			f.set_conflict_status(ci);
			c.addField(f);

		}
		
	}
	
	
	@Override
	void create_form() {
		form = form_factory.getForm(form_descriptor);
		form.set_ids(ids);
		
	}	
	
	@Override
	public void metadataFieldAdded(MD_Field field)
	{
		if(field instanceof MD_EAV_Field)
		{
			((MD_EAV_Field) field).setLevel(MetaParameter.HIERARCHY_LEVEL);
		}
		
		this.added_fields.add(field);
	}	
	
		

}
