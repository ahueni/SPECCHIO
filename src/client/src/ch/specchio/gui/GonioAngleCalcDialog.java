package ch.specchio.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.CelestialAngle;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.Metadata;
import ch.specchio.types.Spectrum;
import ch.specchio.types.attribute;
import ch.specchio.types.hierarchy_node;
import ch.specchio.types.spectral_node_object;

/**
 * Dialogue for calculating goniometer angles.
 */
public class GonioAngleCalcDialog extends JDialog implements ActionListener, TreeSelectionListener {

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	/** client object */
	private SPECCHIOClient specchioClient;
	
	/** azimuth attribute to be filled in by this dialogue */
	private attribute azimuthAttribute;
	
	/** zenith attribute to be filled in by this dialogue */
	private attribute zenithAttribute;
	
	/** the selected spectra download from the server */
	private List<Spectrum> selectedSpectra;
	
	/** the angles calculated for the selected spectra */
	private List<CelestialAngle> calculatedAngles;
	
	/** spectral data browser */
	private SpectralDataBrowser sdb;
	
	/** label for the selected hierarchy */
	private JLabel selectedHierarchyLabel;
	
	/** text field for the selected hierarchy */
	private JTextField selectedHierarchyField;
	
	/** label for the number of selected spectra */
	private JLabel numSelectedLabel;
	
	/** text field for the number of selected spectra */
	private JTextField numSelectedField;
	
	/** label for the gaps */
	private JLabel gapsLabel;
	
	/** text field for the gaps */
	private JTextField gapsField;
	
	/** label for the dummies */
	private JLabel dummiesLabel;
	
	/** text field for the dummies */
	private JTextField dummiesField;
	
	/** the "insert gaps" button */
	private JButton insertGapsButton;
	
	/** the list field */
	private JList listField;
	
	/** the list model */
	private DefaultListModel listModel;
	
	/** "calculate" button */
	private JButton calculateButton;
	
	/** "okay" button */
	private JButton submitButton;
	
	/** "dismiss" button */
	private JButton dismissButton;
	
	/** text for the "insert gaps" button */
	private static final String INSERT_GAPS = "Insert gaps";
	
	/** text for the "calculate" button */
	private static final String CALCULATE = "Calculate";
	
	/** text for the "apply" button */
	private static final String SUBMIT = "Apply";
	
	/** text for the "dismiss" button */
	private static final String DISMISS = "Dismiss";
	
	
	/**
	 * Constructor.
	 * 
	 * @param owner	the frame that owns this dialogue
	 * @param modal	true if the dialogue should be modal
	 * 
	 * @throws SPECCHIOClientException	error contacting server
	 */
	public GonioAngleCalcDialog(Frame owner, boolean modal) throws SPECCHIOClientException {
		
		super(owner, "Gonio Angle Calculation", modal);
		
		// initialise member variables
		selectedSpectra = new ArrayList<Spectrum>();
		calculatedAngles = new ArrayList<CelestialAngle>();
		
		// get a reference to the application's client object
		specchioClient = SPECCHIOApplication.getInstance().getClient();
		
		// get the attribute descriptors to be filled in
		Hashtable<String, attribute> attributes = specchioClient.getAttributesNameHash();
		azimuthAttribute = attributes.get("Sensor Azimuth");
		zenithAttribute = attributes.get("Sensor Zenith");
		if (azimuthAttribute == null || zenithAttribute == null) {
			throw new SPECCHIOClientException("The application server does not support the sensor azimuth and sensor zenith attributes.");
		}
		
		// set up the root panel with a border layout
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BorderLayout());
		getContentPane().add(rootPanel);
		
		// add a spectral data browser for selecting nodes
		sdb = new SpectralDataBrowser(specchioClient, true);
		sdb.build_tree();
		sdb.tree.addTreeSelectionListener(this);
		rootPanel.add(sdb, BorderLayout.CENTER);
		
		// create a panel for the selection information
		JPanel selectionPanel = new JPanel();
		rootPanel.add(selectionPanel, BorderLayout.EAST);
		
		// set up grid bag contraints for the selection panel
		selectionPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridy = 0;
		
		// add a field for displaying the selected hierarchy
		constraints.gridx = 0;
		selectedHierarchyLabel = new JLabel("Selected hierarchy:");
		selectionPanel.add(selectedHierarchyLabel, constraints);
		constraints.gridx = 1;
		selectedHierarchyField = new JTextField(20);
		selectedHierarchyField.setEditable(false);
		selectionPanel.add(selectedHierarchyField, constraints);
		constraints.gridy++;
		
		// add a field for displaying the number of selected spectra
		constraints.gridx = 0;
		numSelectedLabel = new JLabel("Number of Selected Spectra:");
		selectionPanel.add(numSelectedLabel, constraints);
		constraints.gridx = 1;
		numSelectedField = new JTextField(20);
		numSelectedField.setEditable(false);
		selectionPanel.add(numSelectedField, constraints);
		constraints.gridy++;
		
		// add a field for the gaps
		constraints.gridx = 0;
		gapsLabel = new JLabel("Gaps:");
		selectionPanel.add(gapsLabel, constraints);
		constraints.gridx = 1;
		gapsField = new JTextField(20);
		selectionPanel.add(gapsField, constraints);
		constraints.gridy++;
		
		// add a field for the dummies
		constraints.gridx = 0;
		dummiesLabel = new JLabel("Spectra + dummies:");
		selectionPanel.add(dummiesLabel, constraints);
		constraints.gridx = 1;
		dummiesField = new JTextField(20);
		dummiesField.setEditable(false);
		selectionPanel.add(dummiesField, constraints);
		constraints.gridy++;
		
		// add the "insert gaps" button
		constraints.gridx = 1;
		insertGapsButton = new JButton(INSERT_GAPS);
		insertGapsButton.setActionCommand(INSERT_GAPS);
		insertGapsButton.addActionListener(this);
		selectionPanel.add(insertGapsButton, constraints);
		constraints.gridy++;
		
		// add the list field
		constraints.gridx = 1;
		listModel = new DefaultListModel();
		listField = new JList(listModel);
		listField.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane listScrollPane = new JScrollPane(listField);
		listScrollPane.setPreferredSize(new Dimension(200, 80));
		selectionPanel.add(listScrollPane, constraints);
		constraints.gridy++;
		
		// add the "calculate" button
		constraints.gridx = 1;
		calculateButton = new JButton(CALCULATE);
		calculateButton.setActionCommand(CALCULATE);
		calculateButton.addActionListener(this);
		calculateButton.setEnabled(false);
		selectionPanel.add(calculateButton, constraints);
		constraints.gridy++;
		
		// create a panel for the buttons
		JPanel buttonPanel = new JPanel();
		rootPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		// create the "okay" button
		submitButton = new JButton(SUBMIT);
		submitButton.setActionCommand(SUBMIT);
		submitButton.addActionListener(this);
		submitButton.setEnabled(false);
		buttonPanel.add(submitButton);
		
		// crate the "dismiss" button
		dismissButton = new JButton(DISMISS);
		dismissButton.setActionCommand(DISMISS);
		dismissButton.addActionListener(this);
		buttonPanel.add(dismissButton);
		
		// lay out the dialogue
		pack();
		
	}
	
	
	/**
	 * Button handler.
	 * 
	 * @param event	the event to be handled
	 */
	public void actionPerformed(ActionEvent event) {
		
		if (SUBMIT.equals(event.getActionCommand())) {
			
			// launch a thread to perform the update
			GonioAngleUpdateThread thread = new GonioAngleUpdateThread(selectedSpectra, calculatedAngles);
			thread.start();
			
		} else if (INSERT_GAPS.equals(event.getActionCommand())) {
			
			String gapString = gapsField.getText();
			
			// only give it a try if string not empty
			if(!gapString.equals(""))
			{
				gapString = gapString.replace(" ", "");	
				String[] tokens = gapString.split(",");
				
				// insert dummies into position list
				for(int i=0; i < tokens.length; i++)
				{
					int no = Integer.valueOf(tokens[i]);
					if (no < selectedSpectra.size()) {
						selectedSpectra.add(no - 1, null);
					}
					if (no < calculatedAngles.size()) {
						calculatedAngles.add(no - 1, null);
					}
					
				}
				
				// update display
				dummiesField.setText(Integer.toString(selectedSpectra.size()));
				selectionUpdated();
			}
			
		} else if (CALCULATE.equals(event.getActionCommand())) {
			
			// launch a thread to perform the calculation
			GonioAngleCalcThread thread = new GonioAngleCalcThread(selectedSpectra);
			thread.start();
		
		} else if (DISMISS.equals(event.getActionCommand())) {
			
			// close the dialogue
			setVisible(false);
			
		}
		
	}
	
	
	/**
	 * Handle completion of a calculation thread.
	 * 
	 * @param angles	the angles that were calculated by the thread
	 */
	private void anglesCalculated(List<CelestialAngle> angles) {
		
		// save a reference to the list for later
		calculatedAngles = angles;
		
		// update the list box
		selectionUpdated();
		
	}
	
	
	/**
	 * Handle a change in the selection.
	 */
	private void selectionUpdated() {
		
		listModel.clear();
		ListIterator<CelestialAngle> angleIterator = calculatedAngles.listIterator();
		ListIterator<Spectrum> spectrumIterator = selectedSpectra.listIterator();
		int i = 0;
		while (angleIterator.hasNext() && spectrumIterator.hasNext()) {
			
			// get the next items from the lists
			CelestialAngle a = angleIterator.next();
			Spectrum s = spectrumIterator.next();
			
			// build a string representation of the data in the list
			StringBuffer sbuf = new StringBuffer();
			
			// start with the row number
			sbuf.append(Integer.toString(i + 1) + ": ");
			
			// add the angle information
			if (a != null) {
				sbuf.append(Integer.toString((int)a.azimuth) + "/" + Integer.toString((int)a.zenith) + " ");
			} else {
				sbuf.append("0/0 ");
			}
			
			// get the spectrum file name
			if (s != null) {
				MetaParameter mp = s.getMetadata().get_entry("File Name");
				if (mp != null) {
					sbuf.append(mp.valueAsString());
				}
			} else {
				sbuf.append("gap dummy");
			}
			
			// add a string representation of the data to the list
			listModel.add(i, sbuf.toString());
			
			// increment counter
			i++;
		}
		
		// enable the "apply" button
		submitButton.setEnabled(true);
		
	}
	
	
	/**
	 * Handle completion of a selection thread.
	 * 
	 * @param spectra	the spectra that were downloaded by the thread
	 */
	private void spectraDownloaded(List<Spectrum> spectra) {
		
		// save a reference to the list for later
		selectedSpectra = spectra;
		
	}
	
	
	/**
	 * Tree selection handler.
	 * 
	 * @param event	the event to be handled
	 */
	public void valueChanged(TreeSelectionEvent event) {
		
		try {
			
			// get the selected items
			spectral_node_object sn = sdb.get_selected_node();
			List<Integer> spectrumIds = sdb.get_selected_spectrum_ids();
			
			// only accept hierarchy selections
			if (sn instanceof hierarchy_node) {
				
				// display the name of the selected hierarchy
				selectedHierarchyField.setText(sn.getName());
			
				// display the number of selected spectra
				if (spectrumIds.size() > 0) {
					numSelectedField.setText(Integer.toString(spectrumIds.size()));
				} else {
					numSelectedField.setText(null);
				}
				
				// enable the "calculate" buttons
				if(spectrumIds.size() <= 66)
				{
					// launch a thread to populate the list box
					GonioAngleSelectThread thread = new GonioAngleSelectThread(spectrumIds);
					thread.start();
					
					calculateButton.setEnabled(true);
				}
				else
				{
					submitButton.setEnabled(false);
					calculateButton.setEnabled(false);				
				}
				
				// update the list selection
				selectionUpdated();
				
			} else {
				
				// clear the selection fields
				selectedHierarchyField.setText(null);
				numSelectedField.setText(null);
				submitButton.setEnabled(false);
				calculateButton.setEnabled(false);
				selectionUpdated();
				
			}
			
		} catch (SPECCHIOClientException ex) {
			ErrorDialog error = new ErrorDialog((Frame)this.getOwner(), "Error", ex.getUserMessage(), ex);
			error.setVisible(true);
		}
		
	}
	
	
	/**
	 * Thread for performing angle calculations.
	 */
	private class GonioAngleCalcThread extends Thread {
		
		/** the spectra for which angles are to be calculated */
		private Spectrum spectra[];
		
		/** the list of calculated angles */
		private List<CelestialAngle> angles;
		
		/**
		 * Constructor.
		 * 
		 * @param spectraIn	the spectra for which angles are to be calculated
		 */
		public GonioAngleCalcThread(List<Spectrum> spectraIn) {
			
			super();
			
			// save input parameters for later
			spectra = spectraIn.toArray(new Spectrum[spectraIn.size()]);
			
			// initialise member variables
			angles = new ArrayList<CelestialAngle>(spectraIn.size());
			
		}
		
		
		/**
		 * Thread entry point.
		 */
		public void run() {
			
			// initialise counters
			CelestialAngle angle = new CelestialAngle(0, 75);
			CelestialAngle delta = new CelestialAngle(30, -15);
			int opposite = 1;
			
			// create and display a progress report
			ProgressReport pr = new ProgressReport("Gonio Angle Calculator", false);
			pr.setVisible(true);
			pr.set_operation("Calculating sensor angles");
			
			// update angles for each position
			int cnt = 0;
			double tot = new Double(spectra.length);
				
			// calculate the new angle for each spectrum
			while (cnt < spectra.length && angle.azimuth <= 330) {
				while (cnt < spectra.length && angle.zenith >= 0 && angle.zenith <= 75) {
					
					angles.add(new CelestialAngle(angle));
					
					angle.zenith += delta.zenith;
					
					// update progress meter and counter
					pr.set_progress(cnt * 100.0 / tot);
					cnt++;
				}
				
				delta.zenith *= -1;
				
				// azimuth increases when we are about to count down the zenith angles again
				if(delta.zenith < 0)
					angle.azimuth += delta.azimuth;
				
				// brings us back to the last measured zenith angle
				angle.zenith += delta.zenith;
				
				
				// there is always a switch in azimuth by 180 on top of hemisphere
				if(angle.zenith == 0)
				{
					angle.azimuth += 180 * opposite;
					opposite *= -1;	
					// move zenith to next sampling position
					angle.zenith += delta.zenith;			
				}
			}
			
			// close progress report
			pr.setVisible(false);
			
			// notify the dialogue that angles have been calculated
			anglesCalculated(angles);
			
		}
		
	}
	
	
	/**
	 * Thread for downloading spectrum metadata.
	 */
	private class GonioAngleSelectThread extends Thread {
		
		/** the spectrum identifiers to be downloaded */
		private Integer spectrumIds[];
		
		/** the downloaded spectra */
		private List<Spectrum> spectra;
		
		
		/**
		 * Constructor.
		 * 
		 * @param spectrumIdsIn	the list of spectrum identifiers to be processed
		 */
		public GonioAngleSelectThread(List<Integer> spectrumIdsIn) {
			
			super();
			
			// save the input parameters for later
			spectrumIds = spectrumIdsIn.toArray(new Integer[spectrumIdsIn.size()]);
			
			// initialise member variables
			spectra = new LinkedList<Spectrum>();
			
		}
		
		
		/**
		 * Thread entry point.
		 */
		public void run() {
			
			// create and display a progress report
			ProgressReport pr = new ProgressReport("Gonio Angle Calculator", false);
			pr.setVisible(true);
			pr.set_operation("Reading spectrum metadata");
			
			
			int cnt = 0;
			double tot = new Double(spectrumIds.length);
			try {
				for (Integer id : spectrumIds) {
					
					// download spectrum
					spectra.add(specchioClient.getSpectrum(id, true));
					
					// update progress meter
					pr.set_progress(++cnt * 100.0 / tot);
				}
				
			}
			catch (SPECCHIOClientException ex) {
				// error contacting server
				ErrorDialog error = new ErrorDialog((Frame)GonioAngleCalcDialog.this.getOwner(), "Error", ex.getUserMessage(), ex);
				error.setVisible(true);
			}
			
			// close progress report
			pr.setVisible(false);
			
			// notify the dialogue that spectra have bee downloaded
			spectraDownloaded(spectra);
			
		}
		
	}
	
	
	/**
	 * Thread for updating gonio angles.
	 */
	private class GonioAngleUpdateThread extends Thread {
		
		/** the spectra to be updated */
		private Spectrum[] spectra;
		
		/** the new sensor angles */
		private CelestialAngle[] angles;
		
		
		/**
		 * Constructor.
		 * 
		 * @param spectraIn	the list of spectra to be updated
		 * @param anglesIn	the list of new sensor angles
		 */
		public GonioAngleUpdateThread(List<Spectrum> spectraIn, List<CelestialAngle> anglesIn) {
			
			// save input parameters for later
			spectra = spectraIn.toArray(new Spectrum[spectraIn.size()]);
			angles = anglesIn.toArray(new CelestialAngle[anglesIn.size()]);
			
		}
		
		
		/**
		 * Thread entry point.
		 */
		public void run() {
			
			// create and display a progress report
			ProgressReport pr = new ProgressReport("Gonio Angle Calculator", false);
			pr.setVisible(true);
			pr.set_operation("Updating sensor angles");
			
			// update angles for each position
			int cnt = 0;
			double tot = new Double(spectra.length);
			try {
				
				// update each non-gap position
				for (int i = 0; i < spectra.length; i++) {
					if (spectra[i] != null) {
						
						Metadata md = spectra[i].getMetadata();
						
						// build the list of identifiers to be updated
						ArrayList<Integer> updateIds = new ArrayList<Integer>();
						updateIds.add(spectra[i].getSpectrumId());
						
						// update azimuth
						MetaParameter azimuthParameter = md.get_entry(azimuthAttribute.getId());
						if (azimuthParameter == null) {
							azimuthParameter = MetaParameter.newInstance(azimuthAttribute);
						}
						azimuthParameter.setValue(new Double(angles[i].azimuth));
						specchioClient.updateEavMetadata(azimuthParameter, updateIds);
						
						// update zenith
						MetaParameter zenithParameter = md.get_entry(zenithAttribute.getId());
						if (zenithParameter == null) {
							zenithParameter = MetaParameter.newInstance(zenithAttribute);
						}
						zenithParameter.setValue(new Double(angles[i].zenith));
						specchioClient.updateEavMetadata(zenithParameter, updateIds);
						
						// update progress meter
						pr.set_progress(++cnt * 100.0 / tot);
						
					}
					
				}
				
			}
			catch (SPECCHIOClientException ex) {
				// error contacting server
				ErrorDialog error = new ErrorDialog((Frame)GonioAngleCalcDialog.this.getOwner(), "Error", ex.getUserMessage(), ex);
				error.setVisible(true);
			}
			
			// close progress report
			pr.setVisible(false);
			
			// show a success message
			if (cnt > 0) {
				JOptionPane.showMessageDialog(GonioAngleCalcDialog.this, "Sensor angles of " + Integer.toString(cnt) + " spectra updated.");
			}
			
		}
		
	}

}
