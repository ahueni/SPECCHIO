package ch.specchio.proc_modules;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.gui.GridbagLayouter;
import ch.specchio.gui.ModalDialog;
import ch.specchio.processing_plane.ProcessingPlane;

public class ModuleSelection extends ModalDialog  implements ActionListener, ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	
	JList listbox;
	DefaultListModel list_model;
	JButton OK;
	Module m;
	ProcessingPlane pp;
	SPECCHIOClient specchio_client;
	
	// modules
	String waveband_filter = "Waveband Filter";
	String broadband_filter = "Broadband Filter";
	String narrowband_filter = "Narrowband Filter";	
	String rad_to_refl = "Radiance to Reflectance Transformation";
	String get_panel_corr_factors = "Ref. Panel Corr Factors";
	String corr_for_panel = "Correct for Ref. Panel";
	String delta = "Delta";
	String visualisation = "Visualisation Module";
	String file_export = "File Export Module";
	String proof_of_concept = "Proof of Concept";

	public ModuleSelection(SPECCHIOClient specchio_client, Point loc, ProcessingPlane pp)
	{
		super("Module Selection", loc);
		
		this.specchio_client = specchio_client;
		this.pp = pp;
		
		//this.setLocation(x, y);
		
		GridbagLayouter l;
		GridBagConstraints constraints;
		
		constraints = new GridBagConstraints();
		l = new GridbagLayouter(this);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// build GUI
		constraints.gridy = 0;	
		constraints.gridx = 0;		
		l.insertComponent(new JLabel("Modules:"), constraints);
		
		list_model = new DefaultListModel();
		

		list_model.addElement(rad_to_refl);
		list_model.addElement(get_panel_corr_factors);
		list_model.addElement(corr_for_panel);
		list_model.addElement(delta);
		
		list_model.addElement(waveband_filter);
		list_model.addElement(broadband_filter);
		list_model.addElement(narrowband_filter);
		list_model.addElement(visualisation);
		list_model.addElement(file_export);
		
		

		
		listbox = new JList(list_model);
		
		listbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listbox.addListSelectionListener(this);
		
		JScrollPane listScroller = new JScrollPane(listbox);
		listScroller.setPreferredSize(new Dimension(250, 200));
		
		constraints.gridy = 1;		
		constraints.gridheight = 4;
		l.insertComponent(listScroller, constraints);

		OK = new JButton("OK");
		OK.setActionCommand("Ok");
		OK.addActionListener(this);
		OK.setEnabled(false);
		
		constraints.gridx = 2;
		constraints.gridy = 0;
		l.insertComponent(OK, constraints);	
		
		JButton Cancel = new JButton("Cancel");
		Cancel.setActionCommand("Cancel");
		Cancel.addActionListener(this);	
		
		constraints.gridy++;
		l.insertComponent(Cancel, constraints);	
		
		pack();
	}

	synchronized public void actionPerformed(ActionEvent e) {
		
		if ("Ok".equals(e.getActionCommand())) {
			
			
			
			String mod = (String)listbox.getSelectedValue();
			
			if(mod.equals(waveband_filter))
			{
				m = new WavebandFiltering(this, specchio_client);
			}

			if(mod.equals(broadband_filter))
			{
				m = new BroadbandFilter(this, specchio_client);
			}
			
			if(mod.equals(narrowband_filter))
			{
				m = new NarrowbandFilter(this, specchio_client);
			}

			
			if(mod.equals(rad_to_refl))
			{
				m = new RadianceToReflectance(this, specchio_client);
			}		
			
			if(mod.equals(get_panel_corr_factors))
			{
				m = new PanelCorrFactorsSelection(this, specchio_client);
			}			
			
			if(mod.equals(corr_for_panel))
			{
				m = new ReferencePanelCorrection(this, specchio_client);
			}			
			
			if(mod.equals(delta))
			{
				m = new Delta(this, specchio_client);
			}			
			
						
			
			if(mod.equals(visualisation))
			{
				m = new VisualisationModule(this, specchio_client);
			}		
			
			if(mod.equals(file_export))
			{
				m = new FileWriterModule(this, specchio_client);
			}
			
		
			
			
			this.confirmed = true;
			done = true;
			this.notifyAll();
		}		
		
		if ("Cancel".equals(e.getActionCommand())) {
			this.confirmed = false;
			done = true;
			this.notifyAll();
		}
	}
	
	public Module get_module()
	{
		return m;
	}
	
	synchronized public boolean get_user_input() {
		super.get_user_input();

		return this.confirmed;
	}

	public void valueChanged(ListSelectionEvent e) {
		 if (e.getValueIsAdjusting() == false) {

		        if (listbox.getSelectedIndex() == -1) {
		        //No selection, disable fire button.
		            OK.setEnabled(false);

		        } else {
		        //Selection, enable the fire button.
		        	OK.setEnabled(true);
		        }
		    }
		
	}


}
