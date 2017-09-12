package ch.specchio.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.types.MetaTaxonomy;

/**
 * Taxonomoy selection dialogue.
 */
public class TaxonomySelectionDialog extends JDialog implements ActionListener {

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	/** the taxonomy browser */
	private TaxonomyBrowser browser;
	
	/** the selected taxonomy identifier */
	private int taxonomyId;
	
	/** the "apply" button */
	private JButton applyButton;
	
	/** text for the "apply" button */
	private static final String APPLY = "Apply";
	
	
	/**
	 * Constructor.
	 * 
	 * @param owner				the frame to which this dialogue belongs
	 * @param specchioClient	the client object to use for connecting to the server
	 * @param mp				the meta-parameter to select for
	 */
	public TaxonomySelectionDialog(Frame owner, SPECCHIOClient specchioClient, MetaTaxonomy mp) {
		
		super(owner, "Taxonomy Selection", true);
		
		// initialise member variables
		taxonomyId = 0;
		
		// set up the root panel with a border layout
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BorderLayout());
		getContentPane().add(rootPanel);
		
		// add the taxonomy browser
		browser = new TaxonomyBrowser(specchioClient, mp);
		rootPanel.add(browser, BorderLayout.CENTER);
		
		// add the "apply" button
		applyButton = new JButton(APPLY);
		applyButton.setActionCommand(APPLY);
		applyButton.addActionListener(this);
		rootPanel.add(applyButton, BorderLayout.SOUTH);
		
		// lay out the dialgoue
		pack();
		
	}
	
	
	/**
	 * Button handler.
	 * 
	 * @param event	the event to be handled
	 */
	public void actionPerformed(ActionEvent event) {
		
		if (APPLY.equals(event.getActionCommand())) {
			
			// save the selected taxonoy id for later
			taxonomyId = browser.get_selected_taxonomy_id();
			
			// close the dialogue
			setVisible(false);
			
		}
		
	}
	
	
	/**
	 * Get the selected taxonomy identifier.
	 * 
	 * @return the selected identifier, or 0 if the dialogue was cancelled
	 */
	public int getSelectedTaxonomyId() {
		
		return taxonomyId;
		
	}

}
