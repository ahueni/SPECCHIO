package ch.specchio.gui;

import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ch.specchio.client.SPECCHIOClientException;

public class ErrorDialog extends JDialog implements ActionListener {

	/** serialisation ID */
	private static final long serialVersionUID = 1L;
	
	/** the exception being displayed */
	private Exception exception;
	
	/** constraints for laying out the dialogue */
	private GridBagConstraints constraints;
	
	/** the root panel of the dialogue */
	private JPanel panel;
	
	/** the label containing the icon */
	private JLabel iconComponent;
	
	/** the panel containing the error message */
	private JPanel messageComponent;
	
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
		
		// save the input parameters for later
		exception = ex;
		
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
		messageComponent = new JPanel();
		messageComponent.setLayout(new BoxLayout(messageComponent, BoxLayout.Y_AXIS));
		for (String line : message.split("\n")) {
			messageComponent.add(new JLabel(line));
		}
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
		
		if (exception != null) {
			
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
			detailsArea = new JTextArea(25, 80);
			detailsPane = new JScrollPane(detailsArea);
			detailsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			constraints.gridx = 1;
			constraints.gridy = 2;
			constraints.gridheight = 1;
			constraints.gridwidth = 3;
			constraints.anchor = GridBagConstraints.CENTER;
			
		}
		
		pack();
		
	}
	
	
	/**
	 * Format the details text area.
	 * 
	 * @param ex	the exception whose details are to be displayed
	 */
	private void initDetailsArea(Exception ex) {

		if (ex instanceof SPECCHIOClientException) {
			// use the details message stored inside the exception
			String details = ((SPECCHIOClientException)ex).getDetails();
					
			if (details != null && details.length() > 0 && details.charAt(0) == '<') {
				
				// the details message may contain an XHTML response from the web server; try to parse it
				try {
					// create a document factory with all validation disabled (http://stackoverflow.com/questions/6204827/xml-parsing-too-slow)
					DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
					documentFactory.setNamespaceAware(false);
					documentFactory.setValidating(false);
					documentFactory.setFeature("http://xml.org/sax/features/namespaces", false);
					documentFactory.setFeature("http://xml.org/sax/features/validation", false);
					documentFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
					documentFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

					// try to parse the document as XML
					StringReader reader = new StringReader(details);
					DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
					Document document = documentBuilder.parse(new InputSource(reader));
					reader.close();
					
					// now output it to a string with nice indenting
					StringWriter writer = new StringWriter();
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.METHOD, "html");
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
					transformer.transform(new DOMSource(document), new StreamResult(writer));
					writer.close();
					
					// display it in the text area
					detailsArea.setText(writer.toString());
				
				}
				catch (IOException ex2) {
					// not sure why this would happen; display the unparsed message
					detailsArea.setText(details);
				}
				catch (ParserConfigurationException ex2) {
					// could not find an XML parser; display the error and the unparsed message
					detailsArea.setText(ex2.getMessage() + "\n" + details);
				}
				catch (SAXException ex2) {
					// not a valid XML document; assume the details are in plain text
					detailsArea.setText(details);
				}
				catch (TransformerConfigurationException ex2) {
					// could not an XML transformer; display the error and the unparsed message
					detailsArea.setText(ex2.getMessage() + "\n" + details);
				}
				catch (TransformerException ex2) {
					// transformation failed; display the error the unparsed message
					detailsArea.setText(ex2.getMessage() + "\n" + details);
				}
				
			} else {
				
				// display the details message
				detailsArea.setText(details);
				
			}
			
		} else {
			// show the stack trace
			detailsArea.append(ex.getMessage());
			for (StackTraceElement elem : ex.getStackTrace()) {
				detailsArea.append("\n  " + elem.toString());
			}
		}
		
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
			
			// make sure the details area is initialised
			if (detailsArea.getText().length() == 0) {
				startOperation();
				initDetailsArea(exception);
				endOperation();
			}
			
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

}
