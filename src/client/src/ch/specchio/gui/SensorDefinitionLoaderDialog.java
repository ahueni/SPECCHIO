package ch.specchio.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;

public class SensorDefinitionLoaderDialog extends JFrame implements ActionListener
{
	
	private static final long serialVersionUID = 1L;
	
	GridbagLayouter l;
	GridBagConstraints constraints;
	JTextField filename;
	JButton ok;
	
	File f = null;
	
	final JFileChooser fc;
	SPECCHIOClient specchio_client;
	
	public SensorDefinitionLoaderDialog() throws SPECCHIOClientException
	{
		
		super("Read Sensor Defintion File");

		// get a reference to the application's client object
		this.specchio_client = SPECCHIOApplication.getInstance().getClient();
		
		fc = new JFileChooser();
		
		constraints = new GridBagConstraints();
		l = new GridbagLayouter(this);
		
		// some default values. subclasses can always overwrite these
		constraints.gridwidth = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		// build GUI		
		
		constraints.gridx = 0;
		constraints.gridy = 0;				
		l.insertComponent(new JLabel("Sensor definition file:"), constraints);					
		filename = new JTextField(40);
		filename.setEditable(false);
		constraints.gridx = 1;
		l.insertComponent(filename, constraints);
		
		JButton browse = new JButton("Browse");
		browse.setActionCommand("browse");
		browse.addActionListener(this);
		constraints.gridx = 2;
		l.insertComponent(browse, constraints);
		
		
		// create new panel for ok and cancel buttons
		JPanel control = new JPanel();
		GridbagLayouter control_l = new GridbagLayouter(control);
		
		ok = new JButton("OK");
		ok.setActionCommand("ok");
		//ok.setEnabled(false); // default: not enabled
		ok.addActionListener(this);		
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		
		control_l.insertComponent(ok, constraints);
		
		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);	
		
		constraints.gridx = 1;
		control_l.insertComponent(cancel, constraints);		
		
		// insert control panel
		constraints.gridx = 1;
		constraints.gridy = 3;
		l.insertComponent(control, constraints);
		
		pack();
		
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if ("ok".equals(e.getActionCommand())) {
			
			try {
				// open the input file
				FileInputStream fis = new FileInputStream(filename.getText());
				
				// post it to the web service
				specchio_client.loadSensor(fis);
				 
				// message box to announce completion
				JOptionPane.showMessageDialog(null,"Sensor file successfully processed.");
			}
			catch (IOException ex) {
		  		ErrorDialog error = new ErrorDialog(
				    	SPECCHIOApplication.getInstance().get_frame(),
			    		"Error",
			    		ex.getMessage(),
			    		ex
				    );
			  		error.setVisible(true);
			}
			catch (SPECCHIOClientException ex) {
		  		ErrorDialog error = new ErrorDialog(
				    	SPECCHIOApplication.getInstance().get_frame(),
			    		"Error",
			    		ex.getUserMessage(),
			    		ex
				    );
			  		error.setVisible(true);
			}
			
			
			setVisible(false);
		}
		
		if ("cancel".equals(e.getActionCommand())) {
			setVisible(false);
		}
		
		
		if ("browse".equals(e.getActionCommand())) {
			int returnVal = fc.showOpenDialog(this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				this.filename.setText(file.getAbsolutePath());
			}	    	
		} 	    
		
		
	}
	
}
