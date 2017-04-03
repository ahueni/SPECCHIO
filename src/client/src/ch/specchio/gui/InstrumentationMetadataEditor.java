package ch.specchio.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Vector;


import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;

import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.constants.UserRoles;
import ch.specchio.queries.Query;
import ch.specchio.queries.SpectrumQueryCondition;
import ch.specchio.types.Instrument;
import ch.specchio.types.InstrumentDescriptor;
import ch.specchio.types.Reference;
import ch.specchio.types.ReferenceDescriptor;


public class InstrumentationMetadataEditor extends MetaDataEditorBase {
	

	private static final long serialVersionUID = 1L;
	SteppedComboBox instrument_combo, reference_combo;
	JTabbedPane metadata_tabs;
	InstrumentMetadataGroup instrument_data_group;
	ReferenceMetadataGroup reference_data_group;
	JButton insert_new_instrument, delete_instrument, insert_new_reference, delete_reference;
	
	class startup_thread extends Thread
	{

		ProgressReportDialog pr = new ProgressReportDialog(InstrumentationMetadataEditor.this, "Starting Instrumentation Metadata Editor", false, 30);
		InstrumentationMetadataEditor imde;
	
		
		public startup_thread(InstrumentationMetadataEditor imde)
		{
			this.imde = imde;
		}
		
		
		synchronized public void run()
		{
	

			int cnt = 0;
			int tot = 5;
			
			pr.setVisible(true);
			
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			
			// borders
			Border blackline = BorderFactory.createLineBorder(Color.black);
			TitledBorder tb;
			
			
			// create GUI
			
			// set border layout for this dialog
			imde.setLayout(new BorderLayout());
				
			// create tabbed pane
			metadata_tabs = new JTabbedPane();
				
			pr.set_operation("Setting up instrument tab");
			pr.set_progress(++cnt * 100.0 / tot);
			
			try {
								
				// create bordered panel for instrument
				JPanel current_instrument_panel = new JPanel();
				tb = BorderFactory.createTitledBorder(blackline, "Current Instrument");
				current_instrument_panel.setBorder(tb);
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.gridwidth = 1;
				constraints.insets = new Insets(4, 4, 4, 4);
				constraints.gridheight = 1;
				constraints.anchor = GridBagConstraints.NORTHWEST;
				constraints.fill = GridBagConstraints.BOTH;
				GridbagLayouter l;
				GridbagLayouter control_panel_l = new GridbagLayouter(current_instrument_panel);
					
				// add instrument combo
				instrument_combo = new SteppedComboBox();
				load_instruments();
				//current_instrument_panel.add(instrument_combo);
				combo_table_data c = (combo_table_data) instrument_combo.getSelectedItem();			
				instrument_combo.addActionListener(imde); // actions handled in metadata editor instance
				instrument_combo.setPreferredComboWidth(50);
								
				constraints.gridx = 0;
				constraints.gridy = 0;		
				control_panel_l.insertComponent(instrument_combo, constraints);
					
	
				instrument_data_group = new InstrumentMetadataGroup(InstrumentationMetadataEditor.this, specchio_client);
				
				JPanel instrument_panel = new JPanel();
				instrument_panel.setLayout(new BorderLayout());
				instrument_panel.add(current_instrument_panel, BorderLayout.WEST);
				instrument_panel.add(instrument_data_group, BorderLayout.CENTER);
					
				metadata_tabs.addTab(instrument_data_group.get_object_title(), instrument_panel);
	
				Instrument instrument = specchio_client.getInstrument(c.id);
				instrument_data_group.setInstrument(instrument);
				
				// get the update reset panels from  metadata object
				JPanel instrument_reset_update_panel = new JPanel();
				l = new GridbagLayouter(instrument_reset_update_panel);
				constraints.gridx = 0;
				constraints.gridy = 0;	
				JPanel reset_and_update = instrument_data_group.get_reset_update_panel();
				l.insertComponent(reset_and_update, constraints);
				
				constraints.gridy++;		
				control_panel_l.insertComponent(instrument_reset_update_panel, constraints);
				
				// create add/change and remove buttons
				insert_new_instrument = new JButton("Insert new instrument");
				insert_new_instrument.setActionCommand("new");
				insert_new_instrument.addActionListener(imde);	
				constraints.gridy++;		
				control_panel_l.insertComponent(insert_new_instrument, constraints);
				delete_instrument = new JButton("Delete instrument");
				delete_instrument.setActionCommand("delete");
				delete_instrument.addActionListener(imde);	
				constraints.gridy++;		
				control_panel_l.insertComponent(delete_instrument, constraints);
				
				// create bordered panel for reference
				pr.set_operation("Setting up reference tab");
				pr.set_progress(++cnt * 100.0 / tot);
				
				JPanel current_reference_panel = new JPanel();
				tb = BorderFactory.createTitledBorder(blackline, "Current Reference");
				current_reference_panel.setBorder(tb);
				GridbagLayouter curr_reference_panel_l = new GridbagLayouter(current_reference_panel);
				
				// add reference combo
				reference_combo = new SteppedComboBox();
				load_references();
				combo_table_data cr = (combo_table_data) reference_combo.getSelectedItem();			
				reference_combo.addActionListener(imde); // actions handled in metadata editor instance
				constraints.gridx = 0;
				constraints.gridy = 0;		
				curr_reference_panel_l.insertComponent(reference_combo, constraints);
				
	
				reference_data_group = new ReferenceMetadataGroup(InstrumentationMetadataEditor.this, specchio_client);
				
				JPanel reference_panel = new JPanel();
				reference_panel.setLayout(new BorderLayout());
				reference_panel.add(current_reference_panel, BorderLayout.WEST);
				reference_panel.add(reference_data_group, BorderLayout.CENTER);
				
				metadata_tabs.addTab(reference_data_group.get_object_title(), reference_panel);
				
				if(cr != null) // just in case there are no references in the database
				{
					Reference ref = specchio_client.getReference(cr.id);
					reference_data_group.setReference(ref);	
				}
				
				// get the update reset panels from  metadata object
				JPanel reference_reset_update_panel = new JPanel();
				l = new GridbagLayouter(reference_reset_update_panel);
				constraints.gridx = 0;
				constraints.gridy = 0;	
				JPanel ref_reset_and_update = reference_data_group.get_reset_update_panel();
				l.insertComponent(ref_reset_and_update, constraints);
				
				constraints.gridy++;		
				curr_reference_panel_l.insertComponent(reference_reset_update_panel, constraints);
				
				// create add/change and remove buttons
				insert_new_reference = new JButton("Insert new reference");
				insert_new_reference.setActionCommand("new");
				insert_new_reference.addActionListener(imde);	
				constraints.gridy++;		
				curr_reference_panel_l.insertComponent(insert_new_reference, constraints);
				delete_reference = new JButton("Delete reference");
				delete_reference.setActionCommand("delete");
				delete_reference.addActionListener(imde);	
				constraints.gridy++;		
				curr_reference_panel_l.insertComponent(delete_reference, constraints);				
				
				// disable add/change buttons for non-admin users
				if (!specchio_client.isLoggedInWithRole(UserRoles.ADMIN)) {
					insert_new_instrument.setEnabled(false);
					delete_instrument.setEnabled(false);
					instrument_data_group.setEditable(false);
					insert_new_reference.setEnabled(false);
					delete_reference.setEnabled(false);
					reference_data_group.setEditable(false);
				}
	
				add("Center", metadata_tabs);
				pack();
				
				imde.setVisible(true);
			
			}
			catch (SPECCHIOClientException ex) {
				// error contacting the server
				ErrorDialog error = new ErrorDialog(InstrumentationMetadataEditor.this, "Error", ex.getUserMessage(), ex);
				error.setVisible(true);
				imde.setVisible(false);
		    }
			finally {
				pr.setVisible(false);
			}
											
		}	
		

	}
	

	public InstrumentationMetadataEditor() throws SPECCHIOClientException
	{
		super("Instrumentation Metadata Editor");
		
		setVisible(false);
		startup_thread st = new startup_thread(this);
		st.start();		
	}
	
	
	private void load_instruments() throws SPECCHIOClientException {
		
		// clear the combo box
		instrument_combo.removeAllItems();
		
		// get the list of instruments from the server
		InstrumentDescriptor instruments[] = specchio_client.getInstrumentDescriptors();
		
		// add them to the combo box
		for (InstrumentDescriptor instrument : instruments) {
			combo_table_data c = new combo_table_data(
					instrument.getInstrumentName(),
					instrument.getInstrumentId()
				);
			instrument_combo.addItem(c);
		}
		
	}
	
	
	private void load_references() throws SPECCHIOClientException {
		
		// clear the combo box
		reference_combo.removeAllItems();
		
		// get the list of references from the server
		ReferenceDescriptor references[] = specchio_client.getReferenceDescriptors();
		
		// add them to the combo box
		for (ReferenceDescriptor reference : references) {
			combo_table_data c = new combo_table_data(
					reference.getReferenceName(),
					reference.getReferenceId()
				);
			reference_combo.addItem(c);
		}
		
	}
	
	
	public void update_done() throws SPECCHIOClientException {
		int index = instrument_combo.getSelectedIndex();
		load_instruments();
		instrument_combo.setSelectedIndex(index);
		
		index = reference_combo.getSelectedIndex();
		load_references();
		reference_combo.setSelectedIndex(index);
	}
	

	
	public void actionPerformed(ActionEvent e) 
	{
		
		if (e.getSource() == instrument_combo)
		{
			combo_table_data c = (combo_table_data) instrument_combo.getSelectedItem();
								
			// reload instrument metadata 
			if(c != null)
			{
				try {
					Instrument instrument = specchio_client.getInstrument(c.id);
					instrument_data_group.setInstrument(instrument);
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
		}
		
		if (e.getSource() == insert_new_instrument) 
		{
			try {
				// insert the instrument
				specchio_client.createInstrument("dummy");
				
				// reload and focus on new instrument, this is always the last one inserted in the list
				load_instruments();
				instrument_combo.setSelectedIndex(instrument_combo.getItemCount() - 1);		
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
		
		if (e.getSource() == delete_instrument) 
		{
			try {
				combo_table_data c = (combo_table_data) instrument_combo.getSelectedItem();
			
				if (!idInUse("instrument", c.id))
				{
					// instrument id is not in use; ask user for confirmation
					if(JOptionPane.showConfirmDialog(this,"Do you really want to delete the instrument '" + c.toString() + "'?", "Delete instrument", JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION)
					{
						// delete the instrument
						specchio_client.deleteInstrument(c.id);
					
						// reload and focus on new instrument, this is always the last one inserted in the list
						load_instruments();
						instrument_combo.setSelectedIndex(-1);
						instrument_data_group.setInstrument(null);
					}
				}
				else
				{
					// some spectra are still using this instrument; deletion is not allowed
					JOptionPane.showMessageDialog(this, "This instrument is still used by spectra in the database. It cannot be deleted.", "Still in use", JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon);
				}
				
			}
			catch (SPECCHIOClientException ex) {
				ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
			  	error.setVisible(true);
			}

			
		}
		
		if (e.getSource() == reference_combo)
		{
			try {
				combo_table_data c = (combo_table_data) reference_combo.getSelectedItem();

				if(c != null)
				{
						Reference ref = specchio_client.getReference(c.id);
						reference_data_group.setReference(ref);
						this.validate();
				}
			}
			catch (SPECCHIOClientException ex) {
				ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
				  error.setVisible(true);
			}
		}
		
		if (e.getSource() == insert_new_reference) 
		{
			try {
				// insert the reference
				specchio_client.createReference("dummy");
				
				// reload and focus on new instrument, this is always the last one inserted in the list
				load_references();
				reference_combo.setSelectedIndex(reference_combo.getItemCount() - 1);
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
		
		if (e.getSource() == delete_reference) 
		{
			try {
				combo_table_data c = (combo_table_data) reference_combo.getSelectedItem();
			
				if (!idInUse("reference", c.id))
				{
					if(JOptionPane.showConfirmDialog(this,"Do you really want to delete the reference '" + c.toString() + "'?", "Delete reference", JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION)
					{
						// delete the reference
						specchio_client.deleteReference(c.id);
					
						// reload references
						load_references();
						reference_combo.setSelectedIndex(-1);
						reference_data_group.setReference(null);
					}
				}
				else
				{
					// some spectra are still using this reference; deletion is not allowed
					JOptionPane.showMessageDialog(this, "This reference is still used by spectra in the database. It cannot be deleted.", "Still in use", JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon);
				}
			}
			catch (SPECCHIOClientException ex) {
		  		ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
				error.setVisible(true);
			}

			
		}

	}
	
	
	/**
	 * Test whether or not an instrument or reference identifier is in use by spectra in
	 * the database.
	 * 
	 * @param type	"instrument" or "reference"
	 * @param id	the identifier
	 * 
	 * @return true if the given identifier is referred to in the spectrum table
	 * 
	 * @throws SPECCHIOClientException	error contacting the server
	 */
	private boolean idInUse(String type, int id) throws SPECCHIOClientException {
		
		// build the column name in which we're interested
		String column = type + "_id";
		
		// create a query that will return spectra using the given identifier
		Query query = new Query("spectrum");
		query.setQueryType(Query.COUNT_QUERY);
		query.addColumn("spectrum_id");
		SpectrumQueryCondition cond = new SpectrumQueryCondition("spectrum", column);
		cond.setOperator("=");
		cond.setValue(id);
		query.add_condition(cond);
		
		// get a count of spectra that match the query
		int count = specchio_client.getSpectrumQueryCount(query);
		
		return count > 0;
		
	}
	
	

}





