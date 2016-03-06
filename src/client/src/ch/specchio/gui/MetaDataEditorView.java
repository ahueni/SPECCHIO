package ch.specchio.gui;

import java.awt.BorderLayout;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.client.SPECCHIOWebClientException;
import ch.specchio.metadata.MDE_Controller;
import ch.specchio.metadata.MDE_Form;
import ch.specchio.metadata.MD_ChangeListener;
import ch.specchio.metadata.MD_Field;
import ch.specchio.types.Campaign;

public class MetaDataEditorView extends MetaDataEditorBase implements ListSelectionListener, MD_ChangeListener, TreeSelectionListener {
	
	MDE_Controller mdec;
	JTabbedPane metadata_tabs;
	CampaignMetadataPanel campaign_panel;
	SpectrumMetadataPanel metadata_panel;
	SpectrumMetadataCategoryList category_list;
	int metadata_tab_index;
	int campaign_tab_index;
	MDE_Form form;
	ArrayList<JPanel> category_panels;
	GridBagConstraints constraints;
	GridBagConstraints category_container_constraints;
	JButton update, reset;
	Boolean update_reset_state = false;
	
	ArrayList<MD_Field> changed_fields = new ArrayList<MD_Field>();
	ArrayList<MD_Field> removed_fields = new ArrayList<MD_Field>();
	ArrayList<MD_Field> added_fields = new ArrayList<MD_Field>();
	ArrayList<MD_Field> changed_annotations = new ArrayList<MD_Field>();


	public MetaDataEditorView() throws SPECCHIOClientException {
		super("Metadata Editor V3");
		
		// set up controller
		mdec = new MDE_Controller(specchio_client);
		form = mdec.getForm();
		
		// create panels
		metadata_panel = new SpectrumMetadataPanel(this, specchio_client);
		campaign_panel = new CampaignMetadataPanel(this, specchio_client);
		metadata_panel.addMetadataChangeListener(this);
		
		category_list = new SpectrumMetadataCategoryList(mdec.getFormFactory());
		category_list.addListSelectionListener(this);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		constraints = new GridBagConstraints();
		
		constraints.gridwidth = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.BOTH;
		
		category_container_constraints = (GridBagConstraints) constraints.clone();
			
		// set border layout for this dialog
		setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
			
		// create scroll panes to hold metadata
		JScrollPane campaign_scroll_pane = new JScrollPane();
		campaign_scroll_pane.getViewport().add(campaign_panel);
		campaign_scroll_pane.getVerticalScrollBar().setUnitIncrement(10);
		JScrollPane metadata_scroll_pane = new JScrollPane();
		metadata_scroll_pane.getViewport().add(metadata_panel);
		metadata_scroll_pane.getVerticalScrollBar().setUnitIncrement(10);
					
			
		JPanel control_panel = new JPanel();
		GridbagLayouter control_panel_l = new GridbagLayouter(control_panel);	
			
		// create tabbed pane
		metadata_tabs = new JTabbedPane();

		int tab = 0;
		metadata_tabs.addTab("Campaign", campaign_scroll_pane);
		campaign_tab_index = tab++;
		metadata_tabs.addTab("Metadata", metadata_scroll_pane);
		metadata_tab_index = tab++;
			
		// create browser and add to control panel			
		sdb = new SpectralDataBrowser(specchio_client, true);
		
		if (specchio_client.isLoggedInWithRole("admin"))
		{
			sdb.restrict_to_view = false;
		}
			
		// load currently selected campaign
		sdb.build_tree();
					
		JPanel sdb_panel = new JPanel();
		sdb_panel.add(sdb);
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.gridwidth = 2;
		control_panel_l.insertComponent(sdb_panel, constraints);

			// add tree listener
		sdb.tree.addTreeSelectionListener(this);
		sdb.tree.putClientProperty("browser", sdb);
			
		// add update and reset buttons
		constraints.gridwidth = 1;
		constraints.gridy++;
		constraints.gridx = 0;
		update = new JButton("Update");	
		update.addActionListener(this);
		control_panel_l.insertComponent(update, constraints);
		constraints.gridx++;
		reset = new JButton("Reset");
		reset.addActionListener(this);
		control_panel_l.insertComponent(reset, constraints);	

		setUpdateResetButtonsState();
		
		add("West", new JScrollPane(control_panel));
		add("Center", metadata_tabs);
		add("East", category_list);
		pack();
		

		
		setVisible(true);
		
		
		
		
	}

	private static final long serialVersionUID = 1L;
	
	
	
	
	private void buildGUI() throws SPECCHIOClientException
	{
		category_panels = new ArrayList<JPanel>();
		constraints.gridy = 0;
		
		// set up campaign panel
		Campaign campaign = mdec.getCampaign();
		if (campaign != null) {
			campaign_panel.setCampaign(campaign);
		} else {
			campaign_panel.setCampaign(null);
		}
		campaign_panel.revalidate();
		campaign_panel.repaint();
		
		// set up metadata panel
		metadata_panel.setForm(form);
		
	}
	
	
	private void updateCampaign() throws SPECCHIOClientException
	{
		// update the campaign on the server
		mdec.updateCampaign(campaign_panel.getCampaign());
		
		// reset the "changed" state of the campaign panel
		campaign_panel.setCampaign(mdec.getCampaign());
		
		// update button states
		setUpdateResetButtonsState();
	}


	// check all fields for multiple updates and ask user if it should be applied
	private void updateChangedFields() throws SPECCHIOClientException
	{
		
		// see if any of the metadata to be changed is shared between record
		ArrayList<MD_Field> shared_fields = new ArrayList<MD_Field>();
		for (MD_Field field : changed_fields) {
			if (field.getNoOfSharingRecords() != 1 && field.getNoOfSharingRecords() != field.getSelectedRecords()) {
				shared_fields.add(field);
			}
		}
		
		int shared_field_opt = SharedMD_Dialog.APPLY_TO_ALL;
		if (shared_fields.size() > 0) {
			// ask the user what to do with the shared fields
			SharedMD_Dialog shared_decision_dialog = new SharedMD_Dialog(this, SharedMD_Dialog.UPDATE, shared_fields);
			shared_decision_dialog.setVisible(true);
			shared_field_opt = shared_decision_dialog.getSelectedAction();
		}
		
		if (shared_field_opt != SharedMD_Dialog.APPLY_TO_NONE) {
			
			// perform updates
			for (MD_Field field : changed_fields) {
				if (shared_field_opt == SharedMD_Dialog.APPLY_TO_ALL) {
					mdec.update(field);
				} else if (shared_field_opt == SharedMD_Dialog.APPLY_TO_SELECTION) {
					mdec.update_selection(field);
				}
			}
			
			// reset changed lists
			changed_fields.clear();
			added_fields.clear();
			
		}
		
		// update button states
		setUpdateResetButtonsState();
		
	}
	
	
	private void updateChangedAnnotations() throws SPECCHIOWebClientException {
		
		for (MD_Field field : changed_annotations) {
		
			mdec.updateAnnotation(field);
		}
		
		// reset changed lists
		changed_annotations.clear();		
		
		// update button states
		setUpdateResetButtonsState();		
	}	
	
	// check all fields for multiple updates and ask user if it should be applied
	private void removeFields() throws SPECCHIOClientException
	{
		
		// see if any of the metadata to be changed is shared between record
		ArrayList<MD_Field> shared_fields = new ArrayList<MD_Field>();
		for (MD_Field field : removed_fields) {
			if (field.getNoOfSharingRecords() > 1) {
				shared_fields.add(field);
			}
		}
		
		int shared_field_opt = SharedMD_Dialog.APPLY_TO_ALL;
		if (shared_fields.size() > 0) {
			// ask the user what to do with the shared fields
			SharedMD_Dialog shared_decision_dialog = new SharedMD_Dialog(this, SharedMD_Dialog.DELETE, shared_fields);
			shared_decision_dialog.setVisible(true);
			shared_field_opt = shared_decision_dialog.getSelectedAction();
		}
		
		
		if (shared_field_opt != SharedMD_Dialog.APPLY_TO_NONE) {
			for (MD_Field field : removed_fields) {
				
				if(field.getNoOfSharingRecords() == 1 && mdec.getIds().size() == 1)
				{
					mdec.remove(field);
				}
				else if(field.getNoOfSharingRecords() == 1 && mdec.getIds().size() > 1)
				{
					// multiple metaparameters need removing (different parameters per spectrums)
					mdec.remove_all_mps_of_attribute(field);				
				}
				else
				{
					if(shared_field_opt == SharedMD_Dialog.APPLY_TO_ALL)
					{
						mdec.remove(field);
					}
					else if (shared_field_opt == SharedMD_Dialog.APPLY_TO_SELECTION)
					{
						mdec.remove_selection(field);
					}
					
				}
				
			}
			
			// reset the list of deleted fields
			removed_fields.clear();
		}

		// update button states
		setUpdateResetButtonsState();
		
	}	
	

	public void actionPerformed(ActionEvent arg0) {
		
		Object c = arg0.getSource();
		
		if(c == update)
		{
			startOperation();
			try {
				if (metadata_tabs.getSelectedIndex() == campaign_tab_index) {
					updateCampaign();
				} else if (metadata_tabs.getSelectedIndex() == metadata_tab_index) {
					updateChangedFields();
					updateChangedAnnotations();
					removeFields();
				}
			}
	  		catch (SPECCHIOClientException ex) {
	  			ErrorDialog error = new ErrorDialog(
		    		SPECCHIOApplication.getInstance().get_frame(),
	    			"Error",
	    			ex.getUserMessage(),
	    			ex
		    	);
	  			error.setVisible(true);
		    }
			endOperation();
		}
		else if(c == reset)
		{
			startOperation();
			if (metadata_tabs.getSelectedIndex() == campaign_tab_index) {
				
				// reset the campaign panel with the original campaign object
				campaign_panel.setCampaign(mdec.getCampaign());
			
			} else if (metadata_tabs.getSelectedIndex() == metadata_tab_index) {
				
				try {
			
					// remove all new fields from the form
					ListIterator<MD_Field> li = added_fields.listIterator();
					
					while(li.hasNext())
					{
						MD_Field field = li.next();
						form.removeField(field);
					}
					
					changed_fields.clear();
					removed_fields.clear();
					added_fields.clear();
					changed_annotations.clear();
					
					buildGUI();
					
				}
		  		catch (SPECCHIOClientException ex) {
		  			ErrorDialog error = new ErrorDialog(
			    		SPECCHIOApplication.getInstance().get_frame(),
		    			"Error",
		    			ex.getUserMessage(),
		    			ex
			    	);
		  			error.setVisible(true);
			    }
			}
			
			// update button states
			setUpdateResetButtonsState();
			
			endOperation();
		}
		
	}
	



	public void valueChanged(ListSelectionEvent arg0) {
		
		startOperation();
		try {
			mdec.set_form_descriptor(category_list.getFormDescriptor());
			form = mdec.getForm();
			
			buildGUI();
		}
  		catch (SPECCHIOClientException ex) {
			JOptionPane.showMessageDialog(
	    			this,
	    			ex.getMessage(),
	    			"Error",
	    			JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
	    		);
	    }
		endOperation();
		
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		
		startOperation();
		try {
			ArrayList<Integer> spectrum_ids = sdb.get_selected_spectrum_ids();
			Campaign campaign = sdb.get_selected_campaign();
	
			mdec.set_campaign(campaign);
			mdec.set_spectrum_ids(spectrum_ids);		
			form = mdec.getForm();
	
			changed_fields.clear();
			removed_fields.clear();
			added_fields.clear();
			campaign_panel.setCampaign(campaign);
			this.setUpdateResetButtonsState();		
				
			buildGUI();
		}
  		catch (SPECCHIOClientException ex) {
			JOptionPane.showMessageDialog(
	    			this,
	    			ex.getMessage(),
	    			"Error",
	    			JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
	    		);
	    }
		endOperation();
		
		
		
	}
	
	
	public void campaignDataChanged() {
		
		setUpdateResetButtonsState();
		
	}
	
	
	private void setUpdateResetButtonsState()
	{
		if (metadata_tabs.getSelectedIndex() == campaign_tab_index) {
			update_reset_state = campaign_panel.hasUnsavedChanges();
		} else if (metadata_tabs.getSelectedIndex() == metadata_tab_index) {
			update_reset_state = this.changed_fields.size()>0 || this.removed_fields.size()>0 || this.changed_annotations.size()>0;
		}
		
		update.setEnabled(update_reset_state);
		reset.setEnabled(update_reset_state);
	}
	
	
	public void metadataFieldAdded(MD_Field field)
	{
		this.added_fields.add(field);
		setUpdateResetButtonsState();
	}
	
	
	public void metadataFieldChanged(MD_Field field, Object new_value)
	{	
		field.setNewValue(new_value);
		
		if(!changed_fields.contains(field))
		{
			changed_fields.add(field);
		}	
		
		setUpdateResetButtonsState();
	}
	
	public void metadataFieldRemoved(MD_Field field)
	{
				
		if(!removed_fields.contains(field))
		{
			removed_fields.add(field);
		}	
		
		setUpdateResetButtonsState();
	}
	
	
	@Override
	public void metadataFieldAnnotationChanged(MD_Field field, String annotation) {
		
		field.setAnnotation(annotation);
		
		if(!changed_annotations.contains(field))
		{
			changed_annotations.add(field);
		}	
		
		setUpdateResetButtonsState();
		
	}	
	
	
	/**
	 * Handler for ending a potentially long-running operation.
	 */
	private void endOperation() {
		
		// change the cursor to its default start
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
	}
	
	
	/**
	 * Handler for starting a potentially long-running operation.
	 */
	private void startOperation() {
		
		// change the cursor to its "wait" state
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
	}




}



