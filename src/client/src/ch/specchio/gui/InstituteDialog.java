package ch.specchio.gui;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.Country;
import ch.specchio.types.Institute;

public class InstituteDialog extends JDialog implements ActionListener {
	
	/** serialisation version ID */
	private static final long serialVersionUID = 1L;
	
	/** the SPECCHIO client used by this dialogue */
	private SPECCHIOClient specchio_client;
	
	/** the institute represented by this dialogue */
	private Institute institute;
	
	/** department name label */
	private JLabel departmentLabel;
	
	/** department name field */
	private JTextField departmentField;
	
	/** institute name label */
	private JLabel instituteLabel;
	
	/** institute name field */
	private JTextField instituteField;
	
	/** street number label */
	private JLabel streetNumberLabel;
	
	/** street nuber field */
	private JTextField streetNumberField;
	
	/** street name label */
	private JLabel streetNameLabel;
	
	/** street name field */
	private JTextField streetNameField;
	
	/** city name label */
	private JLabel cityNameLabel;
	
	/** city name field */
	private JTextField cityNameField;
	
	/** post office code label */
	private JLabel postOfficeCodeLabel;
	
	/** post office code field */
	private JTextField postOfficeCodeField;
	
	/** web address label */
	private JLabel wwwAddressLabel;
	
	/** web address field */
	private JTextField wwwAddressField;
	
	/** country label */
	private JLabel countryLabel;
	
	/** country combo box */
	private JComboBox countryField;
	
	/** button for submitting the dialogue */
	private JButton submitButton;
	
	/** button for cancelling the dialogue */
	private JButton cancelButton;
	
	/** string for the submission button */
	private static final String CREATE = "Create";
	
	/** string for the cancel button */
	private static final String CANCEL = "Cancel";
	
	
	/**
	 * Constructor
	 * 
	 * @param owner				the dialogue's owner
	 * @param specchio_client	the client
	 * 
	 * @throws SPECCHIOClientException	error contacting the server
	 */
	public InstituteDialog(Frame owner, SPECCHIOClient specchio_client) throws SPECCHIOClientException {
		
		super(owner, "Create new institute", true);
		
		// initialise member variables
		this.specchio_client = specchio_client;
		this.institute = null;
		
		// set up a panel with a grid bag layout
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new GridBagLayout());
		getContentPane().add(rootPanel);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		
		// add label and field for the department name
		constraints.gridx = 0;
		departmentLabel = new JLabel("Department:");
		rootPanel.add(departmentLabel, constraints);
		constraints.gridx = 1;
		departmentField = new JTextField(30);
		rootPanel.add(departmentField, constraints);
		constraints.gridy++;
		
		// add label and field for the institute name
		constraints.gridx = 0;
		instituteLabel = new JLabel("Institute:");
		rootPanel.add(instituteLabel, constraints);
		constraints.gridx = 1;
		instituteField = new JTextField(30);
		rootPanel.add(instituteField, constraints);
		constraints.gridy++;
		
		// add label and field for the street number
		constraints.gridx = 0;
		streetNumberLabel = new JLabel("Street Number:");
		rootPanel.add(streetNumberLabel, constraints);
		constraints.gridx = 1;
		streetNumberField = new JTextField(5);
		rootPanel.add(streetNumberField, constraints);
		constraints.gridy++;
		
		// add label and field the street name
		constraints.gridx = 0;
		streetNameLabel = new JLabel("Street:");
		rootPanel.add(streetNameLabel, constraints);
		constraints.gridx = 1;
		streetNameField = new JTextField(30);
		rootPanel.add(streetNameField, constraints);
		constraints.gridy++;
		
		// add label and field for the city name
		constraints.gridx = 0;
		cityNameLabel = new JLabel("City:");
		rootPanel.add(cityNameLabel, constraints);
		constraints.gridx = 1;
		cityNameField = new JTextField(30);
		rootPanel.add(cityNameField, constraints);
		constraints.gridy++;
		
		// add label and field for the post office code
		constraints.gridx = 0;
		postOfficeCodeLabel = new JLabel("Post Office Code:");
		rootPanel.add(postOfficeCodeLabel, constraints);
		constraints.gridx = 1;
		postOfficeCodeField = new JTextField(10);
		rootPanel.add(postOfficeCodeField, constraints);
		constraints.gridy++;
		
		// add label and field for the country
		constraints.gridx = 0;
		countryLabel = new JLabel("Country:");
		rootPanel.add(countryLabel, constraints);
		constraints.gridx = 1;
		countryField = new JComboBox(specchio_client.getCountries());
		countryField.insertItemAt(null, 0);
		countryField.setSelectedIndex(0);
		rootPanel.add(countryField, constraints);
		constraints.gridy++;
		
		// add label and field for the web address
		constraints.gridx = 0;
		wwwAddressLabel = new JLabel("WWW:");
		rootPanel.add(wwwAddressLabel, constraints);
		constraints.gridx = 1;
		wwwAddressField = new JTextField(30);
		rootPanel.add(wwwAddressField, constraints);
		constraints.gridy++;
		
		// add a panel for the buttons
		constraints.gridx = 1;
		JPanel buttonPanel = new JPanel();
		rootPanel.add(buttonPanel, constraints);
		
		// add the "submit" button
		submitButton = new JButton(CREATE);
		submitButton.setActionCommand(CREATE);
		submitButton.addActionListener(this);
		buttonPanel.add(submitButton);
		
		// add the "cancel" button
		cancelButton = new JButton(CANCEL);
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		
		// lay out the dialogue
		pack();
		
	}

	
	/**
	 * Button handler.
	 */
	public void actionPerformed(ActionEvent event) {
		
		if (CREATE.equals(event.getActionCommand())) {
			
			// sanity check
			if (instituteField.getText().length() == 0 || departmentField.getText().length() == 0) {
				JOptionPane.showMessageDialog(
						this,
						"You must specify a department and institute",
						"Invalid institute",
						JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
					);
				return;
			}

			startOperation();
			try {
				// construct an institute object from the data in the dialogue
				institute = new Institute(instituteField.getText(), departmentField.getText());
				institute.setStreet((streetNameField.getText().length() > 0)? streetNameField.getText() : null);
				institute.setStreetNumber((streetNumberField.getText().length() > 0)? streetNumberField.getText() : null);
				institute.setPostOfficeCode((postOfficeCodeField.getText().length() > 0)? postOfficeCodeField.getText() : null);
				institute.setCity((cityNameField.getText().length() > 0)? cityNameField.getText() : null);
				institute.setWWWAddress((wwwAddressField.getText().length() > 0)? wwwAddressField.getText() : null);
				institute.setCountry((Country)countryField.getSelectedItem());
				
				// ask the server to create the institute and save its identifier
				institute.setInstituteId(specchio_client.insertInstitute(institute));
				
				// dismiss the dialogue
				setVisible(false);
			}
			catch (SPECCHIOClientException ex) {
				// the server failed to create the institute
				ErrorDialog error = new ErrorDialog(
						(Frame)getOwner(),
						"Institute creation failed",
						ex.getMessage(),
						ex
					);
				error.setVisible(true);
				institute = null;
			}
			endOperation();
			
		} else if (CANCEL.equals(event.getActionCommand())) {
			
			// set institute to null to indicate that the dialogue was cancelled
			institute = null;
			
			// dismiss the dialogue
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
	 * Get the institute described by this dialogue.
	 * 
	 * @return an institute object, or null if the dialogue was cancelled
	 */
	public Institute getInstitute() {
		
		return institute;
		
	}
	
	
	/**
	 * Handler for starting a potentially long-running operation.
	 */
	private void startOperation() {
		
		// change the cursor to its "wait" state
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
	}

}
