package ch.specchio.gui;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ch.specchio.client.SPECCHIODatabaseDescriptor;
import ch.specchio.client.SPECCHIOServerDescriptor;
import ch.specchio.client.SPECCHIOWebAppDescriptor;
import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.client.SPECCHIOClientFactory;

public class DatabaseConnectionDialog extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	
	GridbagLayouter l;
	SPECCHIOClientFactory cf;
	JTextField server, port, database, user;
	JPasswordField  password;
	JComboBox conn_combo;
	ServerDescriptorPanel descriptor_panel;
	
	JPanel db_details_panel;
	   

	
	public DatabaseConnectionDialog() throws SPECCHIOClientException
	{		
		super("Connect to database");
		
		cf = SPECCHIOClientFactory.getInstance();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		l = new GridbagLayouter(this);
		
		// create GUI
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridwidth = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		List<SPECCHIOServerDescriptor> descriptor_list = cf.getAllServerDescriptors();
		if (descriptor_list.size() > 0) {
			
			// display the connections described in the configuration files
			constraints.gridx = 0;
			l.insertComponent(new JLabel("Known connections:"), constraints);	
			
			constraints.gridx = 1;
			conn_combo = new JComboBox();	
			// insert connections
			ListIterator<SPECCHIOServerDescriptor> li = cf.getAllServerDescriptors().listIterator();
			while(li.hasNext())
			{
				conn_combo.addItem(li.next());			
			}		
			l.insertComponent(conn_combo, constraints);
			constraints.gridy++;
			if(SPECCHIOApplication.getInstance().getClient() != null)
			{
				conn_combo.setSelectedItem(SPECCHIOApplication.getInstance().getClient().getServerDescriptor());
			}
			conn_combo.addActionListener(this);
			
			descriptor_panel = getServerDescriptorPanel((SPECCHIOServerDescriptor)conn_combo.getSelectedItem());
			
		} else {
			// empty configuration file
			String message =
					"Your configuration file does not contain any accounts.\n" +
					"You can still log-in now, but your log-in details will not be saved.\n" +
					"If you need to create a new account, you can use the \"Create user account\" option.";
			JOptionPane.showMessageDialog(this, message, "No accounts configured", JOptionPane.WARNING_MESSAGE);
			descriptor_panel = getServerDescriptorPanel(null);
		}
		
		
		// show info
		constraints.gridx = 0;
		constraints.gridwidth = 2;
		db_details_panel = new JPanel();
		db_details_panel.add(descriptor_panel);
		l.insertComponent(db_details_panel, constraints);
		constraints.gridy++;
		
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		JButton load = new JButton("Connect");
		l.insertComponent(load, constraints);
		load.setActionCommand("connect");
		load.addActionListener(this);
		
		constraints.gridx = 1;
		constraints.gridwidth = 1;
		JButton cancel = new JButton("Cancel");
		l.insertComponent(cancel , constraints);
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);		
		
		pack();
		setResizable(false);
		
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
	    if ("cancel".equals(e.getActionCommand())) {
	    	this.setVisible(false);
	    } 
	    if ("connect".equals(e.getActionCommand())) {
	    	
	    	// create new connection_details_class object for standard connections
	    	startOperation();
    		SPECCHIOServerDescriptor d = descriptor_panel.getServerDescriptor();
	    	try {
	    		// connect
	    		SPECCHIOClient specchio_client = cf.connect(d);
	    		
	    		// register the new connection with the application
	    		SPECCHIOApplication.getInstance().setClient(specchio_client);
	  		  	
	  		  	// close the dialogue
	    		this.setVisible(false);
	    	}
	    	catch (SPECCHIOClientException ex) {
	    		ErrorDialog error = new ErrorDialog(
	    				this,
	    				"Could not connect",
	    				ex.getUserMessage(),
	    				ex
	    		);
	    		error.setVisible(true);
	    	}
	    	endOperation();
	    }
	    
		if (e.getSource() == conn_combo)
		{
			GridBagConstraints constraints = new GridBagConstraints();
			
			constraints.gridwidth = 2;
			constraints.insets = new Insets(4, 4, 4, 4);
			constraints.gridheight = 1;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.gridx = 0;
			constraints.gridy = 1;		

			db_details_panel.remove(descriptor_panel);
			descriptor_panel = getServerDescriptorPanel((SPECCHIOServerDescriptor) conn_combo.getSelectedItem());
			db_details_panel.add(descriptor_panel);
			db_details_panel.revalidate();
			db_details_panel.repaint();
			
			
			pack();
			
		}
	}
	
	
	/**
	 * Handler for ending a potentially long-running operation.
	 */
	private void endOperation() {
		
		// change the cursor to its default start
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
	}
	
	private ServerDescriptorPanel getServerDescriptorPanel(SPECCHIOServerDescriptor d) {
		
		if (d instanceof SPECCHIOWebAppDescriptor) {
			return new WebAppDescriptorPanel((SPECCHIOWebAppDescriptor)d, false);
		} else if (d instanceof SPECCHIODatabaseDescriptor) {
			return new DatabaseDescriptorPanel((SPECCHIODatabaseDescriptor)d, false);
		} else {
			// no descriptor; default to an empty web application panel
			return new WebAppDescriptorPanel(null, false);
		}
		
	}
	
	
	/**
	 * Handler for starting a potentially long-running operation.
	 */
	private void startOperation() {
		
		// change the cursor to its "wait" state
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
	}
	

}
