package ch.specchio.gui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class ErrorDialog extends JDialog implements ActionListener {

	/** serialisation ID */
	private static final long serialVersionUID = 1L;
	
	/** constraints for laying out the dialogue */
	private GridBagConstraints constraints;
	
	/** the root panel of the dialogue */
	private JPanel panel;
	
	/** the label containing the icon */
	private JLabel iconComponent;
	
	/** the label containing the error message */
	private JLabel messageComponent;
	
	/** the button for dismissing the dialogue */
	private JButton dismissButton;
	
	/** the button for requesting more details */
	private JButton detailsButton;
	
	/** the text area for displaying a detailed error message */
	private JTextArea detailsArea;
	
	/** the scroll pane for the detailed error message */
	private JScrollPane detailsPane;
	
	/** the string for the dismiss button */
	private static final String DISMISS = "Dismiss";
	
	/** the string for the details button when details are not displayed */
	private static final String GET_DETAILS = "Details...";
	
	/** the string for the details button when details are displayed */
	private static final String REMOVE_DETAILS = "Less...";
	
	
	/**
	 * Constructor.
	 * 
	 * @param owner		the owner of this dialogue
	 * @param title		the title of the dialogue
	 * @param message	the message for this dialogue
	 * @param ex		the exception that caused the error (may be null)
	 */
	public ErrorDialog(Frame owner, String title, String message, Exception ex) {
		
		super(owner, title, true);
		init(message, ex);
		
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param owner		the owner of this dialogue
	 * @param title		the title of the dialogue
	 * @param message	the message for this dialogue
	 * @param ex		the exception that caused the error (may be null)
	 */
	public ErrorDialog(Dialog owner, String title, String message, Exception ex) {
		
		super(owner, title, true);
		init(message, ex);
		
	}
	
	
	/**
	 * Initialise the dialogue for a given message and exception.
	 * 
	 * @param message	the message for the dialogue
	 * @param ex		the exception that caused the error (may be null)
	 */
	private void init(String message, Exception ex) {
		
		// create a panel with a grid bag layout to contain all of the components
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		panel = new JPanel(new GridBagLayout());
		getContentPane().add(panel);
		
		// place the icon at the top left
		Icon icon = UIManager.getIcon("OptionPane.errorIcon");
		iconComponent = new JLabel(icon);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		panel.add(iconComponent, constraints);
		
		// place the message at the top right
		messageComponent = new JLabel(message);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 3;
		panel.add(messageComponent, constraints);
		
		// place the "dismiss" button underneath and to the right of the message
		dismissButton = new JButton(DISMISS);
		dismissButton.setActionCommand(DISMISS);
		dismissButton.addActionListener(this);
		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add(dismissButton, constraints);
		
		if (ex != null) {
			
			// create a button for requesting further details beneath the message
			detailsButton = new JButton(GET_DETAILS);
			detailsButton.setActionCommand(GET_DETAILS);
			detailsButton.addActionListener(this);
			constraints.gridx = 2;
			constraints.gridy = 1;
			constraints.gridheight = 1;
			constraints.gridwidth = 1;
			constraints.anchor = GridBagConstraints.EAST;
			panel.add(detailsButton, constraints);
			
			// create the details text area but don't display it yet
			detailsArea = new JTextArea(ex.getMessage());
			for (StackTraceElement elem : ex.getStackTrace()) {
				detailsArea.append("\n  " + elem.toString());
			}
			detailsPane = new JScrollPane(detailsArea);
			detailsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			detailsPane.setMinimumSize(detailsArea.getPreferredSize());
			constraints.gridx = 1;
			constraints.gridy = 2;
			constraints.gridheight = 1;
			constraints.gridwidth = 3;
			constraints.anchor = GridBagConstraints.CENTER;
			
		}
		
		pack();
		
	}
	
	
	/**
	 * Handle button clicks.
	 */
	public void actionPerformed(ActionEvent event) {
		
		if (GET_DETAILS.equals(event.getActionCommand())) {
			
			// add the details pane
			panel.add(detailsPane, constraints);
			
			// re-label the button
			detailsButton.setText(REMOVE_DETAILS);
			detailsButton.setActionCommand(REMOVE_DETAILS);
			
			// re-draw the panel
			pack();
			panel.revalidate();
			panel.repaint();
			
		} else if (REMOVE_DETAILS.equals(event.getActionCommand())) {
			
			// remove the details pane
			panel.remove(detailsPane);
			
			// re-label the button
			detailsButton.setText(GET_DETAILS);
			detailsButton.setActionCommand(GET_DETAILS);
			
			// re-draw the panel
			pack();
			panel.revalidate();
			panel.repaint();
			
		} else if (DISMISS.equals(event.getActionCommand())) {
			
			// dismiss the dialogue
			setVisible(false);
			
		}
		
	}

}
