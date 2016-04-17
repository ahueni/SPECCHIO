package ch.specchio.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import au.ands.org.researchdata.RDACollectionDescriptor;
import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.Campaign;
import ch.specchio.types.ConflictInfo;
import ch.specchio.types.ConflictStruct;
import ch.specchio.types.ConflictTable;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.Metadata;
import ch.specchio.types.Spectrum;
import ch.specchio.types.User;
import ch.specchio.types.attribute;


/**
 * Dialogue box for publishing a collection.
 */
public class PublishCollectionDialog extends JDialog implements ActionListener {

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	/** the client object */
	private SPECCHIOClient specchioClient;
	
	/** the campaign from which to publish */
	private Campaign campaign;
	
	/** the identifiers to be published */
	private ArrayList<Integer> ids;
	
	/** the label for the collection name */
	private JLabel primaryNameLabel;
	
	/** the field for the collection name */
	private JTextField primaryNameField;
	
	/** the label for the collection description */
	private JLabel briefDescriptionLabel;
	
	/** the field for the collection description */
	private JTextArea briefDescriptionField;
	
	/** the label for the research group member list */
	private JLabel researchGroupLabel;
	
	/** the list of research group members */
	private JList researchGroupList;
	
	/** the list model for researchGroupList */
	private DefaultListModel researchGroupListModel;
	
	/** the "submit" button */
	private JButton submitButton;
	
	/** the "cancel" button */
	private JButton cancelButton;
	
	/** the collection descriptor represented by this dialogue */
	private RDACollectionDescriptor collection_d;
	
	/** text for the "submit" button */
	private static final String SUBMIT = "Submit";
	
	/** text for the "cancel" button */
	private static final String CANCEL = "Cancel";
	
	
	/**
	 * Constructor.
	 *
	 * @param owner		the frame that owns this dialog
	 * @param ids		the identifiers to be published
	 * @param model		is the dialogue modal?
	 * 
	 * @throws SPECCHIOClientException	error contacting the server
	 */
	public PublishCollectionDialog(Frame owner, ArrayList<Integer> ids, boolean modal) throws SPECCHIOClientException {
		
		super(owner, "Publish Collection", modal);
		
		// save references to parameters for later
		this.ids = ids;
		
		// get a reference to the application's client object
		specchioClient = SPECCHIOApplication.getInstance().getClient();
		
		// get the campaign object to which the selected identifiers belong (assumes that all identifiers belong to the same campaign)
		campaign = null;
		if (ids.size() > 0) {
			Spectrum s = specchioClient.getSpectrum(ids.get(0), false);
			campaign = specchioClient.getCampaign(s.getCampaignId());
		}
		
		// set up the root panel with a grid bag layout
		JPanel rootPanel = new JPanel();
		getContentPane().add(rootPanel);
		rootPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridy = 0;
		
		// add collection name field
		constraints.gridx = 0;
		primaryNameLabel = new JLabel("Collection Name:");
		rootPanel.add(primaryNameLabel, constraints);
		constraints.gridx = 1;
		primaryNameField = new JTextField(20);
		rootPanel.add(primaryNameField, constraints);
		constraints.gridy++;
		
		// add collection description field
		constraints.gridx = 0;
		briefDescriptionLabel = new JLabel("Collection Description:");
		rootPanel.add(briefDescriptionLabel, constraints);
		constraints.gridx = 1;
		briefDescriptionField = new JTextArea(5, 20);
		briefDescriptionField.setLineWrap(true);
		briefDescriptionField.setWrapStyleWord(true);
		rootPanel.add(new JScrollPane(briefDescriptionField), constraints);
		constraints.gridy++;
		
		// research group list
		constraints.gridx = 0;
		researchGroupLabel = new JLabel("Principal investigator:");
		rootPanel.add(researchGroupLabel, constraints);
		
		// add the research group list
		constraints.gridx = 1;
		researchGroupListModel = new DefaultListModel();
		if (campaign != null) {
			for (User member : campaign.getResearchGroup().getMembers()) {
				researchGroupListModel.addElement(member);
			}
		}
		researchGroupList = new JList(researchGroupListModel);
		JScrollPane researchGroupScrollPane = new JScrollPane(researchGroupList);
		rootPanel.add(researchGroupScrollPane, constraints);
		constraints.gridy++;
		
		// add a panel for the buttons
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		JPanel buttonPanel = new JPanel();
		rootPanel.add(buttonPanel, constraints);
		
		// add the "submit" button
		submitButton = new JButton(SUBMIT);
		submitButton.setActionCommand(SUBMIT);
		submitButton.addActionListener(this);
		buttonPanel.add(submitButton);
		
		// add the "cancel" button
		cancelButton = new JButton(CANCEL);
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		
		// initialise the fields with existing data
		prepopulate();
		
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
			
			try {
				collection_d = buildCollectionDescriptor();
				setVisible(false);
			}
			catch (SPECCHIOUserInterfaceException ex) {
				// missing input data
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid collection information", JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon);
				collection_d = null;
			}
			
		} else if (CANCEL.equals(event.getActionCommand())) {
			
			collection_d = null;
			setVisible(false);
			
		}
		
	}
	
	
	/**
	 * Construct a new collection descriptor corresponding to the
	 * information in the dialogue.
	 * 
	 * @return a new RDACollectionDescriptor object
	 * 
	 * @throws SPECCHIOUserInterfaceException	the contents of the dialogue are not valid
	 */
	private RDACollectionDescriptor buildCollectionDescriptor() throws SPECCHIOUserInterfaceException {
		
		// construct a new collection
		RDACollectionDescriptor d = new RDACollectionDescriptor(ids);
		
		// get the collection name
		String primaryName = primaryNameField.getText();
		if (primaryName == null || primaryName.length() == 0) {
			throw new SPECCHIOUserInterfaceException("You must provide a collection name.");
		}
		d.setPrimaryName(primaryName);
		
		// get the collection description
		String briefDescription = briefDescriptionField.getText();
		if (briefDescription == null || briefDescription.length() == 0) {
			throw new SPECCHIOUserInterfaceException("You must provide a collection description.");
		}
		d.setBriefDescription(briefDescription);
		
		// get the principal investigator
		User principalInvestigator = (User) researchGroupList.getSelectedValue();
		if (principalInvestigator == null) {
			throw new SPECCHIOUserInterfaceException("You must select a principal investigator.");
		}
		d.setPrincipalInvestigator(principalInvestigator);
		
		return d;
		
	}
	
	
	/**
	 * Get a collection descriptor that represents the information in the
	 * dialogue.
	 * 
	 * @return a reference to an RDACollectionDescriptor object, or null if the dialogue was cancelled
	 */
	public RDACollectionDescriptor getCollectionDescriptor() {
		
		return collection_d;
		
	}
	
	
	/**
	 * Pre-populate the dialogue with available data.
	 *
	 * @throws SPECCHIOClientException server error
	 */
	private void prepopulate() throws SPECCHIOClientException {

		// get the conflict data for the selected spectra
		ConflictTable conflicts = specchioClient.getEavMetadataConflicts(ids);
		
		// get the first spectrum in the list for possible use in pre-population
		Spectrum s = specchioClient.getSpectrum(ids.get(0), true);
		
		// populate the collection name and description, if possible
		primaryNameField.setText(prepopulateStringAttribute(conflicts, s.getMetadata(), "ANDS Collection Name"));
		briefDescriptionField.setText(prepopulateStringAttribute(conflicts, s.getMetadata(), "ANDS Collection Description"));
		
	}

	/**
	 * Get the existing non-conflicting value for a string-valued attribute.
	 * 
	 * @param conflicts	the conflict data for the set of spectra
	 * @param attr		the attribute
	 *
	 * @return the value of attribute shared by all spectra, or null
	 * 
	 * @throws SPECCHIOClientException server error
	 */
	private String prepopulateStringAttribute(ConflictTable conflicts, Metadata md, String attributeName)
		throws SPECCHIOClientException {
		
		String value = null;
		attribute attr = specchioClient.getAttributesNameHash().get(attributeName);
		if (attr != null) {
			
			// get the conflict data for the given attribute
			ConflictInfo collectionNameConflicts = conflicts.get(attr.id);
			
			// search the conflict list for an EAV without conflicts
			Enumeration<Integer> eavIds = collectionNameConflicts.eavIds();
			while (eavIds.hasMoreElements()) {
				Integer eavId = eavIds.nextElement();
				ConflictStruct conflict = collectionNameConflicts.getConflictData(eavId);
				if (conflict.getNumberOfSharingRecords() == conflict.getNumberOfSelectedRecords()) {
					// found it; extract the value from the first spectrum in the list
					for (MetaParameter mp : md.get_all_entries(attr.id)) {
						if (mp.getEavId().equals(eavId)) {
							value = (String)mp.getValue();
						}
					}	
				}
			}
			
		}
		
		return value;
	}

}
