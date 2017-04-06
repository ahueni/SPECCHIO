package ch.specchio.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import ch.specchio.metadata.MD_Field;


/**
 * Dialogue for selecting the action to be taken when updating or deleting metadata items
 * that are shared between more than one record.
 */
public class SharedMD_Dialog extends JDialog implements ActionListener {
	
	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	/** list of fields */
	private JList fieldList;
	
	/** "apply to all" button */
	private JButton applyToAllButton;

	/** "apply to selection" button */
	private JButton applyToSelectionButton;
	
	/** "apply to none" button */
	private JButton applyToNoneButton;
	
	/** constant for the "delete" action */
	public final static int DELETE = 0;
	
	/** constant for the "update" action */
	public final static int UPDATE = 1;	
	
	/** constant for the "apply to all" decision */
	public final static int APPLY_TO_ALL = 1;
	
	/** constant for the "apply to selection" decision */
	public final static int APPLY_TO_SELECTION = 2;
	
	/** constant for cancelling the dialogue */
	public final static int APPLY_TO_NONE = 3;
	
	/** text for the "apply to all" button */
	private final static String APPLY_TO_ALL_CMD = "Apply to all spectra";
	
	/** text for the "apply to selection" button */
	private final static String APPLY_TO_SELECTION_CMD = "Selected spectra only";
	
	/** text for the "apply to none" button */
	private final static String APPLY_TO_NONE_CMD = "Cancel";
	
	/** the option selected by the user */
	private int decision = APPLY_TO_ALL;
	
	/**
	 * Constructor.
	 * 
	 * @param owner		the owner of this dialogue
	 * @param op		SharedMD_Dialog.UPDATE or SharedMD_Dialog.DELETE
	 * @param fields	the fields to be updated or deleted
	 */
	public SharedMD_Dialog(Frame owner, int op, List<MD_Field> fields) {
		
		super(owner, ((op == DELETE)? "Delete" : "Update") + " shared record", true);
		
		// set up the root pane with a border layout
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BorderLayout());
		getContentPane().add(rootPanel);
		
		// create a panel for the explanatory text
		JPanel textPanel = new JPanel();
		textPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		String verb = (op == DELETE)? "delete" : "update";
		textPanel.add(new JLabel("The data in the fields listed below are referred to by multiple records."));
		textPanel.add(new JLabel("You can " + verb + " metadata for all of these records, or only the records selected in the navigator."));
		rootPanel.add(textPanel, BorderLayout.NORTH);
		
		// add the list of fields
		String fieldDescriptions[] = new String[fields.size()];
		int i = 0;
		for (MD_Field field : fields) {
			fieldDescriptions[i++] = field.getLabel() + " (shared by " + field.getNoOfSharingRecords() + " records)";
		}
		fieldList = new JList(fieldDescriptions);
		fieldList.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		rootPanel.add(fieldList, BorderLayout.CENTER);
		
		// create a panel for the buttons
		JPanel buttonPanel = new JPanel();
		rootPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		// create the "apply to all" button
		applyToAllButton = new JButton(APPLY_TO_ALL_CMD);
		applyToAllButton.setActionCommand(APPLY_TO_ALL_CMD);
		applyToAllButton.addActionListener(this);
		buttonPanel.add(applyToAllButton);
		
		// create the "apply to selection" button
		applyToSelectionButton = new JButton(APPLY_TO_SELECTION_CMD);
		applyToSelectionButton.setActionCommand(APPLY_TO_SELECTION_CMD);
		applyToSelectionButton.addActionListener(this);
		buttonPanel.add(applyToSelectionButton);
		
		// create the "apply to none" button
		applyToNoneButton = new JButton(APPLY_TO_NONE_CMD);
		applyToNoneButton.setActionCommand(APPLY_TO_NONE_CMD);
		applyToNoneButton.addActionListener(this);
		buttonPanel.add(applyToNoneButton);
		
		// lay out the dialogue
		pack();
			
		
	}
	
	
	/**
	 * Button handler.
	 * 
	 * @param event	the event to be handled
	 */
	public void actionPerformed(ActionEvent event) {
		
		if (APPLY_TO_ALL_CMD.equals(event.getActionCommand())) {
			
			decision = APPLY_TO_ALL;
			setVisible(false);
			
		} else if (APPLY_TO_SELECTION_CMD.equals(event.getActionCommand())) {
			
			decision = APPLY_TO_SELECTION;
			setVisible(false);
			
		} else if (APPLY_TO_NONE_CMD.equals(event.getActionCommand())) {
			
			decision = APPLY_TO_NONE;
			setVisible(false);
			
		}
		
	}
	
	
	/**
	 * Get the action selected by the user.
	 * 
	 * @return SharedMD_Dialog.APPLY_TO_ALL, SharedMD_Dialog.APPLY_TO_SELECTION or SharedMdDialog.APPLY_TO_NONE
	 */
	public int getSelectedAction() {
		
		return decision;
		
	}

}
