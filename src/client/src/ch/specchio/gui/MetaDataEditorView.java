package ch.specchio.gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.jfree.io.IOUtils;


import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.client.SPECCHIOWebClientException;
import ch.specchio.metadata.MDE_Controller;
import ch.specchio.metadata.MDE_Form;
import ch.specchio.metadata.MD_ChangeListener;
import ch.specchio.metadata.MD_Field;
import ch.specchio.types.Campaign;
import ch.specchio.types.MatlabAdaptedArrayList;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;

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
	private JCheckBox conflict_detection_checkbox;

	String measurement_support = "Compute measurement support";
	String altitude_augmentation = "Augment altitude (api.geonames.org/astergdem)";
	String E_W_Switch = "Switch longitude E-W";
	private JMenuBar menuBar;
	private ArrayList<MD_Field> shared_fields;
	private int shared_field_opt;

	public MetaDataEditorView() throws SPECCHIOClientException {
		super("Metadata Editor V3");
		
		// set up controller
		mdec = new MDE_Controller(specchio_client);
		form = mdec.getForm();
		
		// create menu
		JMenu menu;
		JMenuItem menuItem;

		menuBar = new JMenuBar();
		menu = new JMenu("Compute");
		
		menuItem = new JMenuItem(measurement_support);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem(altitude_augmentation);
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem(E_W_Switch);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		
		menuBar.add(menu);
		
		this.setJMenuBar(menuBar);
		
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
		//GridbagLayouter control_panel_l = new GridbagLayouter(control_panel);	
			
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
		
		control_panel.setLayout(new BorderLayout());
		control_panel.add("Center", sdb);
					
		//JPanel sdb_panel = new JPanel();
		//sdb_panel.add(sdb);
//		constraints.gridy = 0;
//		constraints.gridx = 0;
//		constraints.gridwidth = 2;
//		control_panel_l.insertComponent(sdb, constraints);

		// add tree listener
		sdb.tree.addTreeSelectionListener(this);
		sdb.tree.putClientProperty("browser", sdb);
		
		// add checkbox to toggle conflict detection
		conflict_detection_checkbox = new JCheckBox("Do conflict detection");
		conflict_detection_checkbox.setSelected(true);
		conflict_detection_checkbox.setActionCommand("conflict_detection_switch");
		conflict_detection_checkbox.addActionListener((ActionListener) this);
		conflict_detection_checkbox.setToolTipText("Switching off metadata conflict detection will increase selection speed,\n but only show metadata info for the first spectrum in the list");
		control_panel.add("North", conflict_detection_checkbox);
			
		// add update and reset buttons
		JPanel button_panel = new JPanel();
		GridbagLayouter button_panel_l = new GridbagLayouter(button_panel);
		constraints.gridwidth = 1;
		constraints.gridy++;
		constraints.gridx = 0;
		update = new JButton("Update");	
		update.addActionListener(this);
		button_panel_l.insertComponent(update, constraints);
		constraints.gridx++;
		reset = new JButton("Reset");
		reset.addActionListener(this);
		button_panel_l.insertComponent(reset, constraints);	
		control_panel.add("South", button_panel);

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
		shared_fields = new ArrayList<MD_Field>();
		for (MD_Field field : changed_fields) {
			if (field.getNoOfSharingRecords() != 1 && field.getNoOfSharingRecords() != field.getSelectedRecords()) {
				shared_fields.add(field);
			}
		}
		
		shared_field_opt = SharedMD_Dialog.APPLY_TO_ALL;
		if (shared_fields.size() > 0) {
			// ask the user what to do with the shared fields
			SharedMD_Dialog shared_decision_dialog = new SharedMD_Dialog(this, SharedMD_Dialog.UPDATE, shared_fields);
			shared_decision_dialog.setVisible(true);
			shared_field_opt = shared_decision_dialog.getSelectedAction();
		}
		
		if (shared_field_opt != SharedMD_Dialog.APPLY_TO_NONE) {
			
//			ProgressReportDialog pr = new ProgressReportDialog(this, "Metadata Update", true, 20, true);
//			pr.setToDocumentModal(); // prevent from other clicks in metadata editor during update
//			pr.set_operation("Updating metadata ...");
//			pr.setVisible(true);
//			//pr.set_min_max(1, changed_fields.size());
//			int progress = 0;
//			
//			// perform updates
//			for (MD_Field field : changed_fields) {
//				pr.set_component(field.getLabel());
//				
//				if (shared_field_opt == SharedMD_Dialog.APPLY_TO_ALL) {
//					mdec.update(field);
//				} else if (shared_field_opt == SharedMD_Dialog.APPLY_TO_SELECTION) {
//					mdec.update_selection(field);
//				}
//				pr.set_progress(++progress/changed_fields.size()*100);
//			}
//			
//			pr.setVisible(false);
			
			UpdateThread thread = new UpdateThread(this);
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
			
			// reset the redundancy buffer to avoid linking to deleted parameters.
			// ideally, this should only include the reset for the fields that were removed and not the whole buffer ...
			this.specchio_client.clearMetaparameterRedundancyList();
		}

		// update button states
		setUpdateResetButtonsState();
		
	}	
	

	public void actionPerformed(ActionEvent e) {
		
		Object c = e.getSource();
		
		if(measurement_support.equals(e.getActionCommand()))
		{
			try {
				System.out.println(e.getActionCommand());
				compute_measurement_support();
			}
			catch (SPECCHIOClientException ex) {
				ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
				error.setVisible(true);	
			}
		}    
		
		if(altitude_augmentation.equals(e.getActionCommand()))
		{
			try {
				System.out.println(e.getActionCommand());
				augment_altitude();
			}
			catch (SPECCHIOClientException ex) {
				ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
				error.setVisible(true);	
			}
		}    
		
		if(E_W_Switch.equals(e.getActionCommand()))
		{
			try {
				System.out.println(e.getActionCommand());
				switch_ew_lon();
			}
			catch (SPECCHIOClientException ex) {
				ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
				error.setVisible(true);	
			}
		}    
		
		
		
		
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
				ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
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
					ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
					error.setVisible(true);	
			    }
			}
			
			// update button states
			setUpdateResetButtonsState();
			
			endOperation();
		}
		else if(c == this.conflict_detection_checkbox)
		{
			mdec.setDo_conflict_detection(conflict_detection_checkbox.isSelected());			
		}
		
	}
	



	private void switch_ew_lon() {
		
		ArrayList<Integer> ids_with_lon = specchio_client.filterSpectrumIdsByHavingAttribute(mdec.getIds(), "Longitude");
		
		if(ids_with_lon.size() > 0)
		{
			// get values
			
			// get existing metaparameters
			ArrayList<Integer> attribute_ids = new ArrayList<Integer>();
			attribute_ids.add(specchio_client.getAttributesNameHash().get("Longitude").getId());
			
			ArrayList<MetaParameter> existing_parameters = specchio_client.getMetaparameters(ids_with_lon, "Longitude");
					
			for(int i=0;i<ids_with_lon.size();i++)
			{
				
				MetaParameter mp = existing_parameters.get(i);
				
				try {
					ArrayList<Integer> ids = new ArrayList<Integer>();
					ids.add(ids_with_lon.get(i));					
					mp.setValue((Double) mp.getValue()*(-1));
					
					specchio_client.updateEavMetadata(mp, ids);
					
					
				} catch (MetaParameterFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			
			}
			
			this.reloadGUI();
						
		
		}
		
		
	}


	private void augment_altitude() {
		
		ArrayList<Integer> ids_with_lat = specchio_client.filterSpectrumIdsByHavingAttribute(mdec.getIds(), "Latitude");
		ArrayList<Integer> ids_with_lat_lon = specchio_client.filterSpectrumIdsByHavingAttribute(ids_with_lat, "Longitude");
		
		if(ids_with_lat_lon.size() > 0)
		{
			// get values
			MatlabAdaptedArrayList<Object> lat = specchio_client.getMetaparameterValues(ids_with_lat_lon, "Latitude");
			MatlabAdaptedArrayList<Object> lon = specchio_client.getMetaparameterValues(ids_with_lat_lon, "Longitude");
			
			// get existing metaparameters
			ArrayList<Integer> attribute_ids = new ArrayList<Integer>();
			attribute_ids.add(specchio_client.getAttributesNameHash().get("Altitude").getId());
			
			ArrayList<MetaParameter> existing_parameters = specchio_client.getMetaparameters(ids_with_lat_lon, "Altitude");
			
			double prev_lat = 0;
			double prev_lon = 0;
			double prev_alt = 0;
			double altitude = 0;
			
			URL url;
			try {
				for(int i=0;i<ids_with_lat_lon.size();i++)
				{

					if(prev_lat == (Double) lat.get(i) && prev_lon == (Double) lon.get(i))
					{
						prev_alt = altitude;
						
					}
					else
					{
						prev_lat = (Double) lat.get(i);
						prev_lon = (Double) lon.get(i);

						url = new URL("http://api.geonames.org/astergdem?lat=" +lat.get(i)+"&lng="+((Double) lon.get(i)*(-1))+"&username=specchio");

						String alt = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();

						altitude = Double.valueOf(alt);
						prev_alt = altitude;
					}

					ArrayList<Integer> ids = new ArrayList<Integer>();
					try {
						ids.add(ids_with_lat_lon.get(i));
						
						MetaParameter mp = existing_parameters.get(i);
						
						if(mp.getValue() != null)
						{
							mp.setValue(altitude);
						}
						else				
							mp = MetaParameter.newInstance(specchio_client.getAttributesNameHash().get("Altitude"), altitude);
						
						specchio_client.updateEavMetadata(mp, ids);
						
					} catch (SPECCHIOClientException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MetaParameterFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}				
					
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.reloadGUI();
			
		}
		

	}


	private void compute_measurement_support() {
		
		// to compute a measurement support, we need the following parameters:
		// distance of sensor to target
		// sensor zenith
		// sensor fov
		
		ArrayList<Integer> ids_with_dist = specchio_client.filterSpectrumIdsByHavingAttribute(mdec.getIds(), "Sensor Distance");
		ArrayList<Integer> ids_with_dist_and_zenith = specchio_client.filterSpectrumIdsByHavingAttribute(ids_with_dist, "Sensor Zenith");
		ArrayList<Integer> ids_with_dist_and_zenith_and_fov = specchio_client.filterSpectrumIdsByHavingAttribute(ids_with_dist_and_zenith, "FOV");
		
		ArrayList<Double> radii = new ArrayList<Double>();
		ArrayList<Double> major_axes = new ArrayList<Double>();
		ArrayList<Double> minor_axes = new ArrayList<Double>();
		ArrayList<Double> areas_nadir = new ArrayList<Double>();
		ArrayList<Double> areas_oblique = new ArrayList<Double>();
		ArrayList<Integer> ids_nadir = new ArrayList<Integer>();
		ArrayList<Integer> ids_oblique = new ArrayList<Integer>();
		
		if(ids_with_dist_and_zenith_and_fov.size() > 0)
		{
			// get values
			MatlabAdaptedArrayList<Object> dist = specchio_client.getMetaparameterValues(ids_with_dist_and_zenith_and_fov, "Sensor Distance");
			MatlabAdaptedArrayList<Object> zen = specchio_client.getMetaparameterValues(ids_with_dist_and_zenith_and_fov, "Sensor Zenith");
			MatlabAdaptedArrayList<Object> fov = specchio_client.getMetaparameterValues(ids_with_dist_and_zenith_and_fov, "FOV");
			
			// get existing metaparameters
			ArrayList<Integer> attribute_ids = new ArrayList<Integer>();
			attribute_ids.add(specchio_client.getAttributesNameHash().get("Measurement Support Radius").getId());
			attribute_ids.add(specchio_client.getAttributesNameHash().get("Measurement Support Area").getId());
			attribute_ids.add(specchio_client.getAttributesNameHash().get("Measurement Support Major Axis").getId());
			attribute_ids.add(specchio_client.getAttributesNameHash().get("Measurement Support Minor Axis").getId());
			
			ArrayList<ArrayList<MetaParameter>> existing_parameters = specchio_client.getMetaparameters(ids_with_dist_and_zenith_and_fov, attribute_ids);
			
			// calculate the footprint, where the circle is a special form of the ellipse
			for(int i=0;i<zen.size();i++)
			{
				double d = (Double) dist.get(i);
				double phi = Math.toRadians((Double) zen.get(i));
				double c = d * Math.sin(phi);
				double fov_ = Math.toRadians(((Integer) fov.get(i)).doubleValue());
				double h = Math.cos(phi) * d;
				double e = h  * Math.tan(phi - fov_/2);				
				double a = round(c - e,3);				
				double b = round(d * Math.tan(fov_ / 2),3);
				
				
				double A = round(Math.PI * a * b, 3);
				
				MetaParameter mp;
				
				
				if(phi == 0)
				{
					ids_nadir.add(ids_with_dist_and_zenith_and_fov.get(i));
					radii.add(a);
					areas_nadir.add(A);
					
					ArrayList<Integer> ids = new ArrayList<Integer>();
					try {
						ids.add(ids_with_dist_and_zenith_and_fov.get(i));
						
						mp = existing_parameters.get(0).get(i);
						
						if(mp.getValue() != null)
						{
							mp.setValue(a);
						}
						else				
							mp = MetaParameter.newInstance(specchio_client.getAttributesNameHash().get("Measurement Support Radius"), a);
						
						specchio_client.updateEavMetadata(mp, ids);

						mp = existing_parameters.get(1).get(i);
						
						if(mp.getValue() != null)
						{
							mp.setValue(A);
						}
						else							
							mp = MetaParameter.newInstance(specchio_client.getAttributesNameHash().get("Measurement Support Area"), A);
						
						specchio_client.updateEavMetadata(mp, ids);
						
					} catch (SPECCHIOClientException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MetaParameterFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}				
					
				}
				else
				{
					ids_oblique.add(ids_with_dist_and_zenith_and_fov.get(i));				
					major_axes.add(a);
					minor_axes.add(b);
					areas_oblique.add(A);
					
					ArrayList<Integer> ids = new ArrayList<Integer>();
					try {
						ids.add(ids_with_dist_and_zenith_and_fov.get(i));
						mp = MetaParameter.newInstance(specchio_client.getAttributesNameHash().get("Measurement Support Major Axis"), a);
						specchio_client.updateEavMetadata(mp, ids);

						mp = MetaParameter.newInstance(specchio_client.getAttributesNameHash().get("Measurement Support Minor Axis"), b);
						specchio_client.updateEavMetadata(mp, ids);
						
						mp = MetaParameter.newInstance(specchio_client.getAttributesNameHash().get("Measurement Support Area"), A);
						specchio_client.updateEavMetadata(mp, ids);
						
					} catch (SPECCHIOClientException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MetaParameterFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
					
					
				}
			
			}
			
			// future batch update below ....
//			// insert 
//			if(ids_nadir.size()>0)
//			{
//				specchio_client.upda
//			}
//			
//			if(ids_oblique.size()>0)
//			{
//				
//			}
			
			
			
		}
			
			String message = "Measurement support of " + Integer.toString(ids_with_dist_and_zenith_and_fov.size()) + " spectra successfully computed.";
			
			if(ids_with_dist_and_zenith_and_fov.size() != mdec.getIds().size())
			{
				message = message + "\nMeasurement support of " + (mdec.getIds().size() - ids_with_dist_and_zenith_and_fov.size()) + " spectra could not be computed due to missing metadata.";
			}

			JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE, SPECCHIOApplication.specchio_icon);

			
			this.reloadGUI();
		
	}

	public static double round(double value, int places) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.round(new MathContext(places));
		double rounded = bd.doubleValue();				

		return rounded;
	}	

	public void valueChanged(ListSelectionEvent arg0) {
		
		startOperation();
		try {
			mdec.set_form_descriptor(category_list.getFormDescriptor());
			form = mdec.getForm();
			
			buildGUI();
		}
  		catch (SPECCHIOClientException ex) {
			ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
			error.setVisible(true);

	    }
		endOperation();
		
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		
		reloadGUI();
		
	}
	
	
	private void reloadGUI()
	{
		
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
			ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
			error.setVisible(true);			
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
		if(update_reset_state)
		{				
			update.setForeground(Color.RED);
		}
		else
		{
			update.setForeground(Color.BLACK);
		}
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

	/**
	 * Worker thread for performing the actual calculations.
	 */
	private class UpdateThread extends Thread {
		
		MetaDataEditorView mde;
		
		/**
		 * Constructor.
		 * 
		 * @param spectrumIdsIn	the list of spectrum identifiers to be processed
		 */
		public UpdateThread(MetaDataEditorView mde) {
						
			super();
			this.mde = mde;
			
		}
		
		
		/**
		 * Thread entry point.
		 */
		public void run() {
			
			sdb.tree.removeTreeSelectionListener(mde); // disable selections in the databrowser to avoid conflicts during updates
			

			ProgressReportDialog pr = new ProgressReportDialog(mde, "Metadata Update", true, 20);
			//pr.setToDocumentModal(); // prevent from other clicks in metadata editor during update
			pr.set_operation("Updating metadata ...");
			pr.setVisible(true);
			//pr.set_min_max(1, changed_fields.size());
			int progress = 0;
			
			// perform updates
			for (MD_Field field : mde.changed_fields) {
				pr.set_component(field.getLabel());
				
				if (shared_field_opt == SharedMD_Dialog.APPLY_TO_ALL) {
					mdec.update(field);
				} else if (shared_field_opt == SharedMD_Dialog.APPLY_TO_SELECTION) {
					mdec.update_selection(field);
				}
				pr.set_progress(++progress/changed_fields.size()*100);
			}
			
			pr.setVisible(false);
			
			sdb.tree.addTreeSelectionListener(mde); 
			
			// reset changed lists
			mde.changed_fields.clear();
			mde.added_fields.clear();			
			
		}
		
	}


}



