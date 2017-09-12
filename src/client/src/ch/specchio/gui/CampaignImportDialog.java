package ch.specchio.gui;


import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.prefs.BackingStoreException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.client.SPECCHIOPreferencesStore;
import ch.specchio.types.User;


public class CampaignImportDialog  extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 1L;
	Connection db_conn;
	GridbagLayouter l;
	JTextField target_file;
	JList user_list;
	JFileChooser fc;
	JButton load;
	SPECCHIOClient specchio_client;
	private JTextField target_file_on_server;
	
	public CampaignImportDialog() throws SPECCHIOClientException
	{
		this(null);
	}
	
	public CampaignImportDialog(String filepath) throws SPECCHIOClientException
	{
		
		super("Campaign Import");
		
		// get a reference to the application's client object
		this.specchio_client = SPECCHIOApplication.getInstance().getClient();
		
		fc = new JFileChooser();
		
		SPECCHIOPreferencesStore prefs;
		try {
			prefs = new SPECCHIOPreferencesStore();
			
			fc = new JFileChooser(prefs.getStringPreference("INPUT_DIRECTORY"));
			
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
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
		
		constraints.gridwidth = 1;
		l.insertComponent(new JLabel("Import source file:"), constraints);
		target_file = new JTextField(40);
		
		if(filepath != null)
		{
			target_file.setText(filepath);
		}
		
		target_file.setEditable(false);
		constraints.gridx = 1;		
		l.insertComponent(target_file, constraints);
		
		JButton browse = new JButton("Browse");
		browse.setActionCommand("browse");
		browse.addActionListener(this);
		constraints.gridx = 2;
		l.insertComponent(browse, constraints);
		
		constraints.gridy++;
		constraints.gridx = 0;	
		constraints.gridwidth = 1;
		l.insertComponent(new JLabel("Import source file from server:"), constraints);
		target_file_on_server = new JTextField(40);
		target_file_on_server.setToolTipText("Specify a filepath for a source file stored on the SPECCHIO server. Use this option for large files (> 1GB).");
		target_file_on_server.setEditable(true);
		target_file_on_server.addActionListener(this);
		constraints.gridx = 1;		
		l.insertComponent(target_file_on_server, constraints);		
		
		
		target_file_on_server.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if(e.getDocument().getLength() > 0)	load.setEnabled(true); else load.setEnabled(false);
			}
			public void removeUpdate(DocumentEvent e) {
				if(e.getDocument().getLength() > 0)	load.setEnabled(true); else load.setEnabled(false);
			}
			public void insertUpdate(DocumentEvent e) {
				if(e.getDocument().getLength() > 0)	load.setEnabled(true); else load.setEnabled(false);
			}		
		});
		
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy++;
		l.insertComponent(new JLabel("Campaign owner:"), constraints);
		constraints.gridx = 1;
		user_list = new JList(specchio_client.getUsers());
		JScrollPane user_list_scroller = new JScrollPane(user_list);
		l.insertComponent(user_list_scroller, constraints);
		
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy++;	
		load = new JButton("Import");
		l.insertComponent(load, constraints);
		load.setActionCommand("import");
		load.addActionListener(this);
		if(filepath == null)
		{
			load.setEnabled(false);
		}
		
		constraints.gridx = 1;
		JButton cancel = new JButton("Cancel");
		l.insertComponent(cancel , constraints);
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);		
		
		pack();

		
	}
	
	

	public void actionPerformed(ActionEvent e) {
		if ("cancel".equals(e.getActionCommand())) {
			this.setVisible(false);
		} 
		if ("import".equals(e.getActionCommand())) {
			System.out.println("import");
			
			User u = (User) user_list.getSelectedValue();
			if (u != null) {
				
				startOperation();
				

				try {
					
					if(this.target_file.getText().length() > 0)
					{					
					FileInputStream fis = new FileInputStream(this.target_file.getText());
					specchio_client.importCampaign(u.getUserId(), fis);
					fis.close();
					setVisible(false);
					
					// Display message that export has finished
					JOptionPane.showMessageDialog(
							(Frame)getOwner(),
							"XML file has been imported.",
							"Campaign Import has finished",
							JOptionPane.INFORMATION_MESSAGE, SPECCHIOApplication.specchio_icon
						);	
					}
					else
					{
						specchio_client.importCampaign(u.getUserId(), target_file_on_server.getText());
					}
					
				}
				catch (IOException ex) {
			  		ErrorDialog error = new ErrorDialog(this, "Error", ex.getMessage(), ex);
				  	error.setVisible(true);
			    }
				catch (SPECCHIOClientException ex) {
			  		ErrorDialog error = new ErrorDialog(this, "Error", ex.getUserMessage(), ex);
				  	error.setVisible(true);
			    }
				endOperation();
				
			} else {
				
				JOptionPane.showMessageDialog(this, "You must select a user to own this campaign", "Error", JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon);
				
			}
			
		} 	 
		if(e.getActionCommand().equals("browse"))
		{
			// from "http://www.leepoint.net/notes-java/GUI/containers/20dialogs/35filefilter.html"
			class XMLFileFilter extends javax.swing.filechooser.FileFilter {
			    public boolean accept(File f) {
			        return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
			    }
			    
			    public String getDescription() {
			        return ".xml files";
			    }
			}
			
						
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileFilter filter = new XMLFileFilter();
			fc.setFileFilter(filter);
			
			
			
			int returnVal = fc.showOpenDialog(this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				this.target_file.setText(file.getAbsolutePath());
				load.setEnabled(true);
			}	    	

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
