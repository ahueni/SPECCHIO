package ch.specchio.gui;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.constants.UserRoles;
import ch.specchio.types.MetaDate;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.Spectrum;
import ch.specchio.types.spectral_node_object;


/**
 * Time shift dialogue. Used to convert local times to UTC for a group of spectra.
 */
public class TimeShiftDialog extends JFrame implements ActionListener, TreeSelectionListener {

	/** serialisation version ID */
	private static final long serialVersionUID = 1L;
	
	/** the client object */
	private SPECCHIOClient specchioClient;
	
	/** the currently-seleced spectrum identifiers */
	private List<Integer> selectedIds;
	
	/** spectral data browser */
	private SpectralDataBrowser sdb;
	
	/** hierarchy name label */
	private JLabel hierarchyLabel;
	
	/** hierarchy name field */
	private JTextField hierarchyField;
	
	/** number of spectra label */
	private JLabel numSpectraLabel;
	
	/** number of spectra field */
	private JTextField numSpectraField;
	
	/** time zone label */
	private JLabel timezoneLabel;
	
	/** time zone field */
	private JTextField timezoneField;
	
	/** "submit" button */
	private JButton submitButton;
	
	/** "cancel" button */
	private JButton cancelButton;
	
	/** text for the "submit" button */
	private static final String SUBMIT = "Apply";
	
	/** text for the "cancel" button */
	private static final String CANCEL = "Close";
	
	
	/**
	 * Constructor.
	 * 
	 * @throws SPECCHIOClientException	error contacting the server
	 */
	public TimeShiftDialog() throws SPECCHIOClientException {
		
		super("UTC Time Correction");
		
		// save a reference to the client object
		specchioClient = SPECCHIOApplication.getInstance().getClient();
		
		// initialise member variables
		selectedIds = null;
		
		// set up the main panel with a flow layout
		JPanel rootPanel = new JPanel();
		getContentPane().add(rootPanel);
		
		// add the spectral data browser
		sdb = new SpectralDataBrowser(specchioClient, !specchioClient.isLoggedInWithRole(UserRoles.ADMIN));
		sdb.build_tree();
		sdb.tree.addTreeSelectionListener(this);
		rootPanel.add(sdb);
		
		// create a panel for the other controls with a gridbag layout
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridBagLayout());
		rootPanel.add(controlPanel);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridy = 0;
		
		// add the hierarchy name
		constraints.gridx = 0;
		hierarchyLabel = new JLabel("Selected hierarchy:");
		controlPanel.add(hierarchyLabel, constraints);
		constraints.gridx = 1;
		hierarchyField = new JTextField(20);
		hierarchyField.setEditable(false);
		controlPanel.add(hierarchyField, constraints);
		constraints.gridy++;
		
		// add the number of spectra
		constraints.gridx = 0;
		numSpectraLabel = new JLabel("No. of spectra:");
		controlPanel.add(numSpectraLabel, constraints);
		constraints.gridx = 1;
		numSpectraField = new JTextField(20);
		numSpectraField.setEditable(false);
		controlPanel.add(numSpectraField, constraints);
		constraints.gridy++;
		
		// add the time zone
		constraints.gridx = 0;
		timezoneLabel = new JLabel("Hours East of GMT:");
		controlPanel.add(timezoneLabel, constraints);
		constraints.gridx = 1;
		timezoneField = new JTextField(20);
		controlPanel.add(timezoneField, constraints);
		constraints.gridy++;
		
		// create a panel for the buttons with a flow layout
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		JPanel buttonPanel = new JPanel();
		controlPanel.add(buttonPanel, constraints);
		
		// create the "submit" button
		submitButton = new JButton(SUBMIT);
		submitButton.setActionCommand(SUBMIT);
		submitButton.addActionListener(this);
		submitButton.setEnabled(false);
		buttonPanel.add(submitButton);
		
		// create the "cancel" button
		cancelButton = new JButton(CANCEL);
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		
		// layout and display the dialogue
		pack();
		
	}

	
	/**
	 * Button handler.
	 * 
	 * @param event	the action that caused the event
	 */
	public void actionPerformed(ActionEvent event) {
		
		if (SUBMIT.equals(event.getActionCommand())) {
			
			startOperation();
			try {
				// get the number of hours to shift by
				double shift = Double.parseDouble(timezoneField.getText());
				
				// update the time stamps
				int n = shiftTime(shift);
				
				// display a success message
				JOptionPane.showMessageDialog(this, "Acquisition times of " + Integer.toString(n) + " spectra successfully shifted.");
			}
			catch (NumberFormatException ex) {
				// the time zone field does not contain a number
				JOptionPane.showMessageDialog(this, "Please enter a valid number of hours east of GMT.", "Invalid number of hours", JOptionPane.ERROR_MESSAGE);
			}
			catch (SPECCHIOClientException ex) {
				// error contacting the server
				ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
				error.setVisible(true);
			}
			catch (MetaParameterFormatException ex) {
				// the time shift attribute has the wrong type
				ErrorDialog error = new ErrorDialog(this, "Error", "The time shift attribute has the wrong type. Please contact your system administrator.", ex);
				error.setVisible(true);
			}
			endOperation();
			
		} else if (CANCEL.equals(event.getActionCommand())) {
			
			// close the dialogue
			setVisible(false);
			
		}
		
	}
	
	
	/**
	 * Handler for ending a potentially long-running operation.
	 */
	private void endOperation() {
		
		// change the cursor to its default start
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
	}
	
	
	/**
	 * Shift the times associated with the currently-selected spectra.
	 * 
	 * @param shift	the number of hours by which to shift the time
	 * 
	 * @return the number of spectra whose timestamps were adjusted
	 *
	 * @throws SPECCHIOClientException	error contacting the server
	 */
	private int shiftTime(double shift) throws SPECCHIOClientException, MetaParameterFormatException {
		
		ArrayList<Integer> updatedIds = new ArrayList<Integer>();
		
		specchioClient.clearMetaparameterRedundancyList(); // otherwise, repeated calls of the time shift leads to Duplicate entry SQL exception
		
		// shift the capture dates of the selected spectra
		for (Integer id : selectedIds) {
			
			// get the existing spectrum with metadata
			Spectrum s = specchioClient.getSpectrum(id, true);
			
			// get the existing capture date
			MetaDate mpAcquisitionTime = (MetaDate)s.getMetadata().get_first_entry("Acquisition Time");
			if (mpAcquisitionTime != null) {
				
				// get a calendar object that represents the capture date
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//				TimeZone tz = TimeZone.getTimeZone("UTC");
//				Calendar cal = Calendar.getInstance(tz);
//				formatter.setTimeZone(tz);
//				cal.setTime((Date) mpAcquisitionTime.getValue());
				
				// remove gmt offset
				long time_in_millis = ((Date) mpAcquisitionTime.getValue()).getTime();
				long gmt_offset_in_millis = (long)(shift*3600*1000);
				time_in_millis -= gmt_offset_in_millis;
//				cal.setTimeInMillis(time_in_millis);
				
				// update the metadata object
//				mpAcquisitionTime.setValue(cal.getTime());
				mpAcquisitionTime.setValue(new Date(time_in_millis));
				ArrayList<Integer> id_list = new ArrayList<Integer>();
				id_list.add(id);
				specchioClient.updateEavMetadata(mpAcquisitionTime, id_list);
				
				// add the identifier to the list of updated identifiers
				updatedIds.add(id);
				
			}
		}

		if (updatedIds.size() > 0) {
			
			// create a metaparameter noting that the time was shifted
			MetaParameter mpShift = MetaParameter.newInstance(
					"Processing",
					"",
					"Capture time was shifted by " + shift + " hours East using the SPECCHIO timeshift function."
				);
			mpShift.setAttributeName("Time Shift");	
			
			// add the metaparameter to the database
			specchioClient.updateEavMetadata(mpShift, updatedIds);
			
		}
		
		return updatedIds.size();
		
	}
	
	
	/**
	 * Handler for starting a potentially long-running operation.
	 */
	private void startOperation() {
		
		// change the cursor to its "wait" state
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
	}
	
	
	/**
	 * Tree selection handler.
	 * 
	 * @param event	the tree selection that caused the event
	 */
	public void valueChanged(TreeSelectionEvent event) {
		
		try {
			
			spectral_node_object node = sdb.get_selected_node();
			if (node != null) {
				
				// set the hierarchy name
				hierarchyField.setText(node.getName());
				
				// save a reference to the selected spectra
				selectedIds = sdb.get_selected_spectrum_ids();
				numSpectraField.setText(Integer.toString(selectedIds.size()));
				
				// enable the "okay" button
				submitButton.setEnabled(true);
				
			} else {
				
				// blank the hierarchy name and selected spectra fields
				hierarchyField.setText(null);
				numSpectraField.setText(null);
				
				// note that we have no current selection
				selectedIds = null;
				
				// disable the "okay" button
				submitButton.setEnabled(false);
				
			}
			
		}
		catch (SPECCHIOClientException ex) {
			// error contacting the server
			ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
			error.setVisible(true);
		}
		
	}

}
