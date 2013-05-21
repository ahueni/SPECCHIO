package ch.specchio.metadata;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.gui.MetaDataFromTabView;
import ch.specchio.gui.SPECCHIOApplication;
import ch.specchio.gui.combo_table_data;
import ch.specchio.types.Category;
import ch.specchio.types.MatlabAdaptedArrayList;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.attribute;

import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MetaDataFromTabController implements PropertyChangeListener {
	
	MetaDataFromTabView view;
	MetaDataFromTabModel model = new MetaDataFromTabModel();
	SPECCHIOClient specchio_client;
	
	private ProgressMonitor progressMonitor;
	private Task task;
	 
	class Task extends SwingWorker<Void, Void> {
		
		
		
		@Override
		public Void doInBackground() throws SPECCHIOClientException {

			int progress = 0;
			
			setProgress(progress);
			
				// insert data
				// insert each meta element separately
				ArrayList<Integer> assigned_cols = model.getColumnNumbersOfAssignedColumns();

				Hashtable<Integer, Integer> LUT = model.getMatchingLUT();				

				int col_index = 0;
				int delta_progress = 100 / assigned_cols.size();
				

				while (col_index < assigned_cols.size()  && !isCancelled()) 
				{

					Integer assigned_col = assigned_cols.get(col_index);

					attribute attr = model.getAttributeOfColumn(assigned_col);

					progressMonitor.setNote("Inserting " + attr.getName());


					ArrayList<Integer> spectrum_ids = new  ArrayList<Integer>();				
					for(int index : LUT.keySet())
					{
						spectrum_ids.add(model.getSpectrum_ids().get(index));			
					}


					// check for attribute existance and warn user if exists for some spectra
					int existing_count = check_attribute_existance(attr, spectrum_ids);

					int decision = MetaDataFromTabView.INSERT;

					if(existing_count > 0)
					{

						decision = view.get_user_decision_on_existing_fields(attr.getName());					

						if(decision == MetaDataFromTabView.DELETE_EXISTING_AND_INSERT_NEW)
						{						
							specchio_client.removeEavMetadata(attr, spectrum_ids);

							decision = MetaDataFromTabView.INSERT;					

						}										

					}



					if(decision == MetaDataFromTabView.INSERT)
					{
						Enumeration<Integer> li = LUT.keys();

						ArrayList<String> table_values = model.getValuesOfTableColumn(assigned_col);

						// loop over all spectra in matching LUT
						while(li.hasMoreElements())
						{
							Integer spectrum_index = li.nextElement();

							Integer spectrum_id = model.getSpectrum_ids().get(spectrum_index);

							Integer row = LUT.get(spectrum_index);
							String table_value = table_values.get(row);

							//System.out.println(table_value);

							// create new metaparameter for attribute
							if(!table_value.equals(""))
							{
								try {
									MetaParameter mp = MetaParameter.newInstance(attr);
									mp.setValueFromString(table_value);
			
									ArrayList<Integer> ids = new ArrayList<Integer>();
									ids.add(spectrum_id);
			
									specchio_client.updateEavMetadata(mp, ids);
								}
								catch (MetaParameterFormatException ex) {
									// could not convert the table value to a meta-parameter of appropriate type
									String message = "Skipped column " + (assigned_col+1) + ", row " + (row+1) + " : " + ex.getMessage();
									progressMonitor.setNote(message);
									System.err.println(message);
								}
							}
							else
							{
								// empty value
								// System.out.println("empty value at row " + row);
							}

						}

					}
					

					progress += delta_progress;
					setProgress(progress);
					col_index++;
				}
				return null;

			}

		@Override
		public void done() {
			progressMonitor.setProgress(0);
			progressMonitor.close();
		}
	}
 	

	public void loadFile(File f) throws BiffException, IOException
	{

		// get a reference to the application's client object
		this.specchio_client = SPECCHIOApplication.getInstance().getClient();
				
	    this.clearModel();
	    
	    model.setWorkbook(Workbook.getWorkbook(f));
		
	}


	public MetaDataFromTabModel getModel() {
		return model;
	}
	
	public void clearModel() {
		model.reset();
	}


	public void setMatchingColumn(int col, int index) {
		
		// actual column depends on the selected index, a NIL (index 0) value is not a valid matching column
		if(index == 0)
		{
			model.matchingColumnIsSet(false);
		}
		else
		{
			model.matchingColumnIsSet(true);
			model.setMatchingColumn(col);
		}
		
	}


	public void setCategory(int col, int index) {
		
		model.setCategoryOfColumn(model.getPossible_categories().get(index), col);
		
	}


	public void setSpectrumIds(ArrayList<Integer> spectrum_ids) {
		// TODO Auto-generated method stub
		model.setSpectrumIds(spectrum_ids);
	}


	public ArrayList<Object> getDbValuesforMatchingCol() throws SPECCHIOClientException {
		
		if(model.matchingColumnIsSet())
		{
			//Category cat = model.getCategoryOfColumn(model.getMatching_col());
			
			String attr_name = model.getAttributeNameOfColumn(model.getMatching_col());
			
			if(!attr_name.equals("NIL"))
			{
				MatlabAdaptedArrayList<Object> values = specchio_client.getMetaparameterValues(model.getSpectrum_ids(), attr_name);			
				model.setMatching_col_db_values(values);		 
				return values;
				
			}
			
		}
		
		return null;
	}


	public void setAttributeAtColumn(int col, combo_table_data itemAt) {
			
		model.setAttributeOfColumn(col, itemAt.id);
		
	}


	public void match() {
		
		// carry out matching for currently selected matching column
		if(model.matchingColumnIsSet())
		{
			Hashtable<Integer, Integer> spectrum_to_table_val_matches = new Hashtable<Integer, Integer>();
			
			ArrayList<String> table_values = model.getValuesOfTableColumn(model.getMatching_col());
			
			ArrayList<Object> matched_table_values = new ArrayList<Object>();
			
			int i =0;
			for(Object db_val : model.getMatching_col_db_values())
			{
				int first_table_pos = -1;
				int last_table_pos = -1;
				
				if(model.regexIsSet())
				{
					ArrayList<Integer> indices = getRegexMatchingIndices(table_values, db_val, model.getRegex_start(), model.getRegex_end());
					
					if(indices.size() > 0)
					{
						first_table_pos = indices.get(0);
						last_table_pos = indices.get(indices.size()-1);						
					}
				}
				else
				{
					first_table_pos =table_values.indexOf(db_val);
					last_table_pos =table_values.lastIndexOf(db_val);					
				}

				
				if(first_table_pos >= 0 && first_table_pos == last_table_pos)
				{
					spectrum_to_table_val_matches.put(i, first_table_pos);
					
					matched_table_values.add(table_values.get(first_table_pos));
				}
				else if(first_table_pos == last_table_pos)
				{
					//System.out.println("Warning: Ambiguous values!");
					matched_table_values.add(null);
				}
				else
				{
					// no match
					matched_table_values.add(null);
				}
				
				i++;
			}
			
			model.setMatchingLUT(spectrum_to_table_val_matches);
			model.setMatchedTableValues(matched_table_values);
			model.setNumber_of_matches(spectrum_to_table_val_matches.size());
			model.setNumber_of_missing_matches((table_values.size() - spectrum_to_table_val_matches.size()));
			
//			System.out.println("Number of matches: " + spectrum_to_table_val_matches.size());
//			System.out.println("Missing matches: " + (table_values.size() - spectrum_to_table_val_matches.size()));
			
		}
		
	}
	
	
	private ArrayList<Integer> getRegexMatchingIndices(ArrayList<String> table_values, Object db_val, String regex_start, String regex_end)
	{
		ArrayList<Integer> indices = new ArrayList<Integer>(); 
		int i = 0;
		for(String value : table_values)
		{
			String regex = regex_start + value + regex_end;
			
			if(db_val.toString().matches(regex)) indices.add(i);
			
			i++;
		}

		return indices;
	}
	
	
	
	public ArrayList<Object> getMatchedTableValues()
	{		
		return model.getMatchedTableValues();
	}
	


	public boolean insert() {
		
		try {
			// ensure that a possible re-insert is really resulting in duplicated rows under all circumstances
			specchio_client.clearMetaparameterRedundancyList();
		} catch (SPECCHIOClientException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		progressMonitor = new ProgressMonitor(view,
                "Inserting selected Metadata Parameters from XLS ...",
                "", 0, 100);
		progressMonitor.setProgress(0);
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();		
		
		return true;

	}


	private int check_attribute_existance(attribute attr, ArrayList<Integer> spectrum_ids) throws SPECCHIOClientException {
				
		int count = specchio_client.getExistingMetaparameterCount(attr.getId(), spectrum_ids);
			
			
//		System.out.println("Number of existing records:" + count);	
			
		return count;
		
	}


	public MetaDataFromTabView getView() {
		return view;
	}


	public void setView(MetaDataFromTabView view) {
		this.view = view;
	}


	public void load_matching_table(File file) {
				
		model.setMatchingTable(file);
			
	}


	public void autoMatching() throws SPECCHIOClientException {
		
		Hashtable<String, String> LUT = model.getColumn_element_matching_LUT();
		Hashtable<String, attribute> attr_hash = specchio_client.getAttributesNameHash();
		
		
		// loop over all table column names and try to find match in LUT
		int col = 0;
		for(String col_name : model.getListOfTableColumnNames())
		{
			String matching_attribute_name = LUT.get(col_name);
			
			System.out.println(col_name + "-> " + matching_attribute_name);
			
			if(matching_attribute_name != null && !matching_attribute_name.equals("NIL"))
			{
				// try and find attribute in database
				attribute attr = attr_hash.get(matching_attribute_name);
				
				// assign category and element for current column
				if(attr != null)
				{
					Category cat = model.getCategories_hash().get(attr.cat_name);
					
					model.setCategoryOfColumn(cat, col);
					model.setAttributeOfColumn(col, attr.getId());					
				}
				
				
			}

			col++;
		}
	
		
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	    if ("progress" == evt.getPropertyName() ) {
            int progress = (Integer) evt.getNewValue();
            progressMonitor.setProgress(progress);

            if (progressMonitor.isCanceled() || task.isDone()) {

                if (progressMonitor.isCanceled()) {
                    task.cancel(true);
                } 
            }
        }
		
	}



	

}
