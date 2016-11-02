package ch.specchio.gui;


import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.prefs.BackingStoreException;

import javax.swing.JCheckBox;
import javax.swing.JFrame;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.client.SPECCHIOPreferencesStore;

public class Preferences extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	SPECCHIOClient specchio_client;
	private JCheckBox asd_unit_folder;
	private JCheckBox db_config_file_creation_and_editing;
	SPECCHIOPreferencesStore prefs;
	
	GridbagLayouter l;
	GridBagConstraints constraints;
	

	public Preferences() throws SPECCHIOClientException {
		super("SPECCHIO Preferences");
		
		try {
			prefs = new SPECCHIOPreferencesStore();
			
			asd_unit_folder = new JCheckBox("Create Unit Folders for old ASD file version");
			asd_unit_folder.setActionCommand("asd_unit_folder");
			asd_unit_folder.addActionListener((ActionListener) this);
			asd_unit_folder.setSelected(prefs.getBooleanPreference("CREATE_UNIT_FOLDER_FOR_OLD_ASD_FILES"));
			
			db_config_file_creation_and_editing = new JCheckBox("Enable editing of db_config file");
			db_config_file_creation_and_editing.setActionCommand("db_config_file_creation_and_editing");
			db_config_file_creation_and_editing.addActionListener((ActionListener) this);
			db_config_file_creation_and_editing.setSelected(prefs.getBooleanPreference("DB_CONFIG_FILE_CREATION_AND_EDITING"));
			
			
			constraints = new GridBagConstraints();
			
			// some default values. subclasses can always overwrite these
			constraints.gridwidth = 1;
			constraints.insets = new Insets(4, 4, 4, 4);
			constraints.gridheight = 1;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			l = new GridbagLayouter(this);
			
			
			// build GUI
			constraints.gridx = 1;
			l.insertComponent(asd_unit_folder, constraints);
			
			constraints.gridy = 2;	
			l.insertComponent(db_config_file_creation_and_editing, constraints);

			pack();
			
			this.setVisible(true);
		
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	synchronized public void actionPerformed(ActionEvent e) 
	{

		if(e.getActionCommand().equals("asd_unit_folder"))
		{
			
			try {
				prefs.setBooleanPreference(this.asd_unit_folder.isSelected(), "CREATE_UNIT_FOLDER_FOR_OLD_ASD_FILES");				

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(e.getActionCommand().equals("db_config_file_creation_and_editing"))
		{
			
			try {
				prefs.setBooleanPreference(this.db_config_file_creation_and_editing.isSelected(), "DB_CONFIG_FILE_CREATION_AND_EDITING");				

				MainMenu menu = MainMenu.getInstance();
				menu.enable_db_config_tool(prefs.getBooleanPreference("DB_CONFIG_FILE_CREATION_AND_EDITING"));				
				
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		


	}

}
