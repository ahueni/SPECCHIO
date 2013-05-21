package ch.specchio.gui;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
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
	 */
	public InstituteDialog(Frame owner, SPECCHIOClient specchio_client) {
		
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

			startOperation();
			try {
				// construct an institute object from the data in the dialogue
				institute = new Institute(instituteField.getText(), departmentField.getText());
				
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
