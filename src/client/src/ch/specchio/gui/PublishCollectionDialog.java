package ch.specchio.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import au.ands.org.researchdata.RDACollectionDescriptor;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.Campaign;
import ch.specchio.types.Spectrum;
import ch.specchio.types.User;


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
		
		// set up the root panel with a box layout
		JPanel rootPanel = new JPanel();
		getContentPane().add(rootPanel);
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		rootPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		// add the label for the research group list
		researchGroupLabel = new JLabel("Please select the principal investigator:");
		researchGroupLabel.setAlignmentX(0.5f);
		rootPanel.add(researchGroupLabel);
		
		// add the research group list
		researchGroupListModel = new DefaultListModel();
		if (campaign != null) {
			for (User member : campaign.getResearchGroup().getMembers()) {
				researchGroupListModel.addElement(member);
			}
		}
		researchGroupList = new JList(researchGroupListModel);
		JScrollPane researchGroupScrollPane = new JScrollPane(researchGroupList);
		researchGroupScrollPane.setAlignmentX(0.5f);
		rootPanel.add(researchGroupScrollPane);
		
		// add a panel for the buttons
		JPanel buttonPanel = new JPanel();
		rootPanel.add(buttonPanel);
		
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

}
