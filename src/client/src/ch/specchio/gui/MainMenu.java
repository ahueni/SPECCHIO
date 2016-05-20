package ch.specchio.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.client.SPECCHIOClientFactory;
import ch.specchio.constants.UserRoles;
import ch.specchio.metadata.MetaDataFromTabModel;
import ch.specchio.types.Category;
import ch.specchio.types.Instrument;
import ch.specchio.types.SpecchioCampaign;
import ch.specchio.types.Units;
import ch.specchio.types.attribute;
import ch.specchio.queries.EAVQueryConditionObject;


class MainMenu implements ActionListener, ItemListener {
	URI uri;
   JMenuBar menuBar;
   String create_new_campaign = "Create new campaign";
   String load_campaign_data = "Load campaign data";
   String create_user_account = "Create a new user account";
   String connect_to_db = "Connect to database";
   String edit_user_account = "Edit user information";
   String edit_db_config_file = "Edit db_config file";
   String metadata_editor = "Edit metadata";
   String metadata_from_xls = "Get metadata from XLS";
   String data_removal = "Remove data";
   String data_browser = "Browse data hierarchy";
   String query_builder = "Build query";
   String measure_density = "Measure Metadata Space Density";
   String gonio_explorer = "Explore goniometer data";
   String data_explorer = "Explore & process data";
   String gonio_data_processor = "Process gonio data";
   String data_processor = "Interactive Data Processor";
   String cal_manager = "Manage calibrations";
   String instr_admin = "Instrumentation admin.";
   String load_sensor = "Load sensor definition";
   String campaign_export = "Export campaign";
   String campaign_import = "Import campaign";
   String add_target_reference_links = "Target-reference links";
   String time_shift = "Correct local time to UTC";
   String sun_angle_calc = "Sun angle calculation";
   String gonio_angle_calc = "Gonio angle calculation";
   String test = "test";
   String info = "About";
   String list_eav_metadata_attributes = "List available Metadata Elements";
   
   /** menu items accessible to all clients */
   Hashtable<String, JMenuItem> public_menu_items;
   
   /** menu items accessible to all logged-in users */
   Hashtable<String, JMenuItem> user_menu_items;
   
   /** menu items accessiable only to administrative users */
   Hashtable<String, JMenuItem> admin_menu_items;
   
   private static MainMenu instance = null;

   protected MainMenu() 
   {
	   // initialise menu item tables
	   public_menu_items = new Hashtable<String, JMenuItem>();
	   user_menu_items = new Hashtable<String, JMenuItem>();
	   admin_menu_items = new Hashtable<String, JMenuItem>();

	  // create the menu bar.
      JMenu menu;
      JMenuItem menuItem;

      menuBar = new JMenuBar();
      menu = new JMenu("Database");
      
      menuItem = new JMenuItem(create_user_account);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      public_menu_items.put(create_user_account, menuItem);
      
      menuItem = new JMenuItem(connect_to_db);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      public_menu_items.put(connect_to_db, menuItem);
      
      menuItem = new JMenuItem(edit_user_account);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      user_menu_items.put(edit_user_account, menuItem);
      
      menu.addSeparator();
      
      menuItem = new JMenuItem(edit_db_config_file);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      public_menu_items.put(edit_db_config_file, menuItem);
      
      
      menuBar.add(menu);
      
      // Data Input 
      menu = new JMenu("Data Input");
      
      menuItem = new JMenuItem(create_new_campaign);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      user_menu_items.put(create_new_campaign, menuItem);

      menuItem = new JMenuItem(load_campaign_data);
      menuItem.addActionListener(this);
      menu.add(menuItem); 
      user_menu_items.put(load_campaign_data, menuItem);
      
      menuItem = new JMenuItem(metadata_editor);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      user_menu_items.put(metadata_editor, menuItem);
      
      menuItem = new JMenuItem(metadata_from_xls);
      menuItem.addActionListener(this);
      menu.add(menuItem); 
      user_menu_items.put(metadata_from_xls, menuItem);
      
      menuBar.add(menu);
           
      // Data Output 
      menu = new JMenu("Data Processing & Output");
      
      menuItem = new JMenuItem(this.data_browser);
      menuItem.addActionListener(this);
      menu.add(menuItem);   
      user_menu_items.put(data_browser, menuItem);
        
      menuItem = new JMenuItem(query_builder);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      user_menu_items.put(query_builder, menuItem);

      menuBar.add(menu);
      
      // Data Maintenance 
      menu = new JMenu("Data Maintenance");
      
      menuItem = new JMenuItem(data_removal);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      user_menu_items.put(data_removal, menuItem);
      
      menuItem = new JMenuItem(campaign_export);
      menuItem.addActionListener(this);
      menu.add(menuItem);  
      user_menu_items.put(campaign_export, menuItem);
      
      menuItem = new JMenuItem(campaign_import);
      menuItem.setEnabled(false);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      admin_menu_items.put(campaign_import, menuItem);
      
      menuItem = new JMenuItem(load_sensor);
      menuItem.setEnabled(false);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      admin_menu_items.put(load_sensor, menuItem);
      
      menuItem = new JMenuItem(instr_admin);
      menuItem.addActionListener(this);
      menu.add(menuItem);     
      user_menu_items.put(instr_admin, menuItem);
     
      menuBar.add(menu);
      
      // Special Functions
      menu = new JMenu("Special Functions");
      
      menuItem = new JMenuItem(add_target_reference_links);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      user_menu_items.put(add_target_reference_links, menuItem);
      
      menuItem = new JMenuItem(time_shift);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      user_menu_items.put(time_shift, menuItem);
      
      menuItem = new JMenuItem(sun_angle_calc);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      user_menu_items.put(sun_angle_calc, menuItem);
      
      menuItem = new JMenuItem(gonio_angle_calc);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      user_menu_items.put(gonio_angle_calc, menuItem);
      
      menuBar.add(menu);
      
      // Help 
      menu = new JMenu("Help");
      try {
		uri = new URI("http://www.specchio.ch");
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
      menuItem = new JMenuItem(list_eav_metadata_attributes);
      menuItem.addActionListener(this);
      menu.add(menuItem);
      user_menu_items.put(list_eav_metadata_attributes, menuItem);
      
      menuItem = new JMenuItem(info);
      menuItem.addActionListener(this);
      menu.add(menuItem);      
      public_menu_items.put(info, menuItem);
      
      // uncomment for sandbox
//      menuItem = new JMenuItem(test);
//      menuItem.addActionListener(this);
//      menu.add(menuItem);      
//      public_menu_items.put(test, menuItem);      
      
      menuBar.add(menu);
   }
   
	public static MainMenu getInstance() 
	{
		if(instance == null) {
			instance = new MainMenu();
		}
		return instance;
	}    
	
	public void enable_tools_for_client(SPECCHIOClient client) throws SPECCHIOClientException
	{
		// always enable tools for unconnected clients
		for (JMenuItem menuItem : public_menu_items.values()) {
			menuItem.setEnabled(true);
		}
		
		if (client != null) {
			// enable menu items for all authenticated users
			enable_user_tools(true);
			
			// enable menu items for admin users only
			enable_admin_tools(client.isLoggedInWithRole(UserRoles.ADMIN));
		} else {
			// disable all tools
			enable_user_tools(false);
			enable_admin_tools(false);
		}
	}
	
	private void enable_admin_tools(boolean enable)
	{
		for (JMenuItem menuItem : admin_menu_items.values()) {
			menuItem.setEnabled(enable);
		}
	}
	
	private void enable_user_tools(boolean enable)
	{
		for (JMenuItem menuItem : user_menu_items.values()) {
			menuItem.setEnabled(enable);
		}
	}

   JMenuBar getMenu() {
      return menuBar;
   }

   public void actionPerformed(ActionEvent e) 
   {
      if(create_new_campaign.equals(e.getActionCommand()))
      {
    	 try {
    		 NewCampaignDialog d = new NewCampaignDialog(new SpecchioCampaign(), SPECCHIOApplication.getInstance().get_frame());
    		 d.setVisible(true);
    	 }
    	 catch (SPECCHIOClientException ex) {
    		 JOptionPane.showMessageDialog(
    				 SPECCHIOApplication.getInstance().get_frame(),
    				 ex.getMessage(),
    				 "Error",
    				 JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	 }
         
      }
      if(load_campaign_data.equals(e.getActionCommand()))
      {
    	 try {
    		 LoadCampaignDataDialog d = new LoadCampaignDataDialog(SPECCHIOApplication.getInstance().get_frame(), false);
    		 d.setVisible(true);
    	 }
    	 catch (SPECCHIOClientException ex) {
    		 JOptionPane.showMessageDialog(
    				 SPECCHIOApplication.getInstance().get_frame(),
    				 ex.getMessage(),
    				 "Error",
    				 JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	 }
      }
      
      if(create_user_account.equals(e.getActionCommand()))
      {
    	  try {
    		  UserAccountDialog d = new UserAccountDialog(SPECCHIOApplication.getInstance().get_frame(), null, null);
    		  d.setVisible(true);
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  ex.getMessage(),
    				  "Could not download data from the server",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	  }
      }

      if(connect_to_db.equals(e.getActionCommand()))
      {
    	  try {
    		  DatabaseConnectionDialog d = new DatabaseConnectionDialog();
    		  d.setLocationRelativeTo(SPECCHIOApplication.getInstance().get_frame());
    		  d.setVisible(true);
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  String msg = ((ex.getMessage()==null) ? "" : ex.getMessage()) +" : " +ex.getUserMessage();
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  msg,
    				  "Invalid configuration",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	  } catch (FileNotFoundException e1) {
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  e1.getMessage(),
    				  "Could not read db_config.txt file",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
		} catch (IOException e1) {
  		  JOptionPane.showMessageDialog(
				  SPECCHIOApplication.getInstance().get_frame(),
				  e1.getMessage(),
				  "Could not read db_config.txt file",
				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
			);
		}
      }
      
      if(edit_user_account.equals(e.getActionCommand()))
      {
    	  try {
    		  SPECCHIOClient specchio_client = SPECCHIOApplication.getInstance().getClient();
    		  UserAccountDialog d = new UserAccountDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  specchio_client,
    				  specchio_client.getLoggedInUser()
    			);
    		  d.setVisible(true);
    	  }
          catch (SPECCHIOClientException ex) {
        	  JOptionPane.showMessageDialog(
        			  SPECCHIOApplication.getInstance().get_frame(),
        			  ex.getMessage(),
        			  "Error",
        			  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
        		);
          }
      }
      
      if(edit_db_config_file.equals(e.getActionCommand()))
      {
    	  File temp = new File(SPECCHIOClientFactory.getApplicationFilepath("db_config.txt"));

    	  try {
    		  Desktop.getDesktop().open(temp);
    		  
    		  //SPECCHIOClientFactory.getInstance().reloadDBConfigFile();
    		  
    	  } catch (IOException e1) {
    		  // TODO Auto-generated catch block
    		  e1.printStackTrace();
    	  } catch (SPECCHIOClientException ex) {
    		  String msg = ((ex.getMessage()==null) ? "" : ex.getMessage()) +" : " +ex.getUserMessage();
        	  JOptionPane.showMessageDialog(
        			  SPECCHIOApplication.getInstance().get_frame(),
        			  msg,
        			  "Error",
        			  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
        		);
		}		


      }
      
      

      if(metadata_editor.equals(e.getActionCommand()))
      {
    	  try {
    		  new MetaDataEditorView();
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  ex.getMessage(),
    				  "Error",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	  }
      }
      
      if(metadata_from_xls.equals(e.getActionCommand()))
      {
    	  try {
    		  new MetaDataFromTabView();
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  ex.getMessage(),
    				  "Error",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	  }
      }
      
      if (add_target_reference_links.equals(e.getActionCommand()))
      {
    	  try {
    		  TargetReferenceLinkDialog d = new TargetReferenceLinkDialog(SPECCHIOApplication.getInstance().get_frame(), false);
    		  d.setVisible(true);
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  ex.getMessage(),
    				  "Error",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	  }
      }
      
      if(data_removal.equals(e.getActionCommand()))
      {
    	  try {
    		  DataRemoverDialog d = new DataRemoverDialog();
    		  d.setVisible(true);
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  ex.getMessage(),
    				  "Error",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	  }
      }

      if(campaign_export.equals(e.getActionCommand()))
      {
    	  try {
    		  CampaignExportDialog d = new CampaignExportDialog();
    		  d.setVisible(true);
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  ex.getMessage(),
    				  "Error",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	  }
      }
      
      if(campaign_import.equals(e.getActionCommand()))
      {
    	  try {
    		  CampaignImportDialog d = new CampaignImportDialog();
    		  d.setVisible(true);
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  JOptionPane.showMessageDialog(
				  SPECCHIOApplication.getInstance().get_frame(),
				  ex.getMessage(),
				  "Error",
				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
			);
    	  }
      }
      
      
      if(this.data_browser.equals(e.getActionCommand()))
      {
		try {
			QueryBuilder d = new QueryBuilder("Data Browser (V3)", "hierarchy_browser");
			d.setVisible(true);
		}
		catch (SPECCHIOClientException ex) {
  		  JOptionPane.showMessageDialog(
				  SPECCHIOApplication.getInstance().get_frame(),
				  ex.getMessage(),
				  "Error",
				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
			);
		}
         
      }      
  
      if(query_builder.equals(e.getActionCommand()))
      {
		try {
			QueryBuilder d = new QueryBuilder("Query Builder (V3)", "mds_restrictions");
			d.setVisible(true);
		} 
		catch (SPECCHIOClientException ex) {
	  		  JOptionPane.showMessageDialog(
					  SPECCHIOApplication.getInstance().get_frame(),
					  ex.getMessage(),
					  "Error",
					  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
				);
			}
         
      }
      
      
      if(load_sensor.equals(e.getActionCommand()))
      {
    	  try {
    		  SensorDefinitionLoaderDialog d = new SensorDefinitionLoaderDialog();
    		  d.setVisible(true);
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  ex.getMessage(),
    				  "Error",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	  }
      }
 
      if(instr_admin.equals(e.getActionCommand()))
      {
    	  try {
    		  InstrumentationMetadataEditor d = new InstrumentationMetadataEditor();
    		  d.setVisible(true);
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  ex.getMessage(),
    				  "Error",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	  }
      }
      
      if(info.equals(e.getActionCommand()))
      {
//         ImageIcon icon = new ImageIcon("SPECCHIO_Icon_Mid_Res_small.jpg");
         
         
//         JOptionPane.showMessageDialog(null,
//        		 SPECCHIOApplication.version + "\n" + 
//        		 "(c) 2006-2012 by Remote Sensing Laboratories (RSL),\n" +
//        		 "Dept of Geography, " +
//        		 "University of Zurich\n" + 
//        		 "www.specchio.ch\n\n" +
//        		 "Please refer to the User Guide for more information.\n",
//        		    "About",
//        		    JOptionPane.INFORMATION_MESSAGE,
//        		    icon);
    	  
    	  BufferedImage myPicture;
		try {
			myPicture = ImageIO.read(new File(SPECCHIOClientFactory.getApplicationFilepath("SPECCHIO_Icon_Mid_Res_small.jpg")));
//			myPicture = ImageIO.read(new File("SPECCHIO_Icon_Mid_Res_small.jpg"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));	  


    	  
         
         JFrame frame = new JFrame("About");
         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         frame.setSize(550, 200);
         Container container = frame.getContentPane();
         container.setLayout(new GridBagLayout());
         
         container.add(picLabel);
         
         JButton button = new JButton();
        
         button.setText("<html>" + "Version: " + SPECCHIO_ReleaseInfo.getVersion() + "<br><br>" + 
         "Build Number: " + SPECCHIO_ReleaseInfo.getBuildNumber() + "<br>" +
         "Build Date: " + SPECCHIO_ReleaseInfo.getBuildDate()
        		 + "<br><br>" +
        		 "(c) 2006-2016 by Remote Sensing Laboratories (RSL)<br>" +
        		 "Dept. of Geography, " +
        		 "University of Zurich (CH)<br>" +
        		 "(c) 2013-2014 by University of Wollongong (AU)<br><br>" +
        		 " For more information visit: " +
        		 "<FONT color=\"#000099\"><U>www.specchio.ch</U></FONT>" +
        		 " or <br>" +
        		 "refer to the user guide." +
        		 " </HTML>");
         button.setHorizontalAlignment(SwingConstants.LEFT);
         button.setBorderPainted(false);
         button.setOpaque(false);
         button.setBackground(Color.WHITE);
         button.setToolTipText(uri.toString());
         button.addActionListener(new OpenUrlAction());
         container.add(button);
         frame.pack();
         frame.setVisible(true);     
         
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();         
         
		}
         
      }   
      
      if(list_eav_metadata_attributes.equals(e.getActionCommand()))
      {
    	  // user metadata from tabular data model to get the attributes sorted by categories
    	  MetaDataFromTabModel model = new MetaDataFromTabModel();
    	  SPECCHIOClient specchio_client = SPECCHIOApplication.getInstance().getClient();
    	  
    	// write the meta-parameter value to a temporary file
			File temp;
			try {
				temp = File.createTempFile("specchio", ".txt");
				
				temp.deleteOnExit();
				FileOutputStream fos = new FileOutputStream(temp);
				
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

				bw.write("\n");
				bw.write("List of Metadata Elements by Category available in this database");
				bw.write("\n");
				bw.write("================================================================");
				bw.write("\n");
				bw.write("\n");
				bw.write("Element Name \tElement Description \tStorage Field in Database \tUnit(short) \tUnit");
				bw.write("\n");
				bw.write("----------------------------------------------------------------");
				bw.write("\n");
				bw.write("\n");
				bw.write("\n");
				
				ArrayList<Category> categories =  model.getPossible_categories();
				
				for(Category category : categories)
				{
					bw.write(category.name);
					bw.write("\n");
					bw.write("----------------------------------------------------------------");
					bw.write("\n");
					
					// list all attributes
					attribute[] attr_array = specchio_client.getAttributesForCategory(category.name);
					
					
					for (attribute attr : attr_array) {		
						
						Units unit = specchio_client.getAttributeUnits(attr);
						
						bw.write(attr.getName());
						//bw.write("[" + attr. + "]");				// prepared to write units, but must read them first from DB		
						bw.write("\t");
						if(attr.description != null)
						{
							bw.write(attr.description);						
						}
						bw.write("\t");
						bw.write(attr.default_storage_field);
						bw.write("\t");
						if(unit.id != 0)
						{
							bw.write(unit.short_name);
							bw.write("\t");
							if(unit.name != null)
								bw.write(unit.name);
						}
						bw.write("\n");
					}
					
					
					bw.write("----------------------------------------------------------------");
					bw.write("\n");
					bw.write("\n");
					bw.write("\n");
				}
				
				
				bw.close();

				//mp_file.writeValue(fos);
				fos.close();

				// launch the external viewer
				Desktop.getDesktop().open(temp);				
				
				
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SPECCHIOClientException e1) {
				e1.printStackTrace();
			}
      }
      
      
      if(test.equals(e.getActionCommand()))
      {
    	// TODO The Sandbox is here ....
    	  // Sandbox
    	  SPECCHIOClient specchio_client = SPECCHIOApplication.getInstance().getClient();
    	  
    	  try {
    		  
    		  attribute attr = specchio_client.getAttributesNameHash().get("Altitude");

    		  Object cond = new EAVQueryConditionObject(attr);

    		  
//			String has_license = specchio_client.getCapability("END_USER_LICENSE");
//			
//			String short_license = specchio_client.getCapability("END_USER_LICENSE_SHORT_TEXT");
			
			int x = 1;
		} catch (SPECCHIOClientException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	  
//    	  try {
//			//int tax_id = specchio_client.getTaxonomyId(160, "Hemispherical-conical (CASE 8)");
////    		Instrument i = specchio_client.getCalibratedInstrument(22);
////			int x = 1;
//    		  
////    		  specchio_client.getInstrumentIds(spectrum_ids)  
//    		  
//    		  
//		} catch (SPECCHIOClientException e1) {
//			e1.printStackTrace();
//		}
    	  
      }
  	
      if(time_shift.equals(e.getActionCommand()))
      {
    	  try {
	         System.out.println(e.getActionCommand());
	         TimeShiftDialog d = new TimeShiftDialog();
	         d.setVisible(true);
    	  }
    	  catch (SPECCHIOClientException ex) {
    		  JOptionPane.showMessageDialog(
    				  SPECCHIOApplication.getInstance().get_frame(),
    				  ex.getMessage(),
    				  "Error",
    				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
    			);
    	  }
      }
      
      if (sun_angle_calc.equals(e.getActionCommand()))
      {
    	  try {
 	         SunAngleCalcDialog d = new SunAngleCalcDialog(SPECCHIOApplication.getInstance().get_frame(), false);
 	         d.setVisible(true);
     	  }
     	  catch (SPECCHIOClientException ex) {
     		  JOptionPane.showMessageDialog(
     				  SPECCHIOApplication.getInstance().get_frame(),
     				  ex.getMessage(),
     				  "Error",
     				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
     			);
     	  }
       }
      
      if (gonio_angle_calc.equals(e.getActionCommand()))
      {
    	  try {
 	         GonioAngleCalcDialog d = new GonioAngleCalcDialog(SPECCHIOApplication.getInstance().get_frame(), false);
 	         d.setVisible(true);
     	  }
     	  catch (SPECCHIOClientException ex) {
     		  JOptionPane.showMessageDialog(
     				  SPECCHIOApplication.getInstance().get_frame(),
     				  ex.getMessage(),
     				  "Error",
     				  JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
     			);
     	  }
       }
    	  
      
      

   }
   

protected ImageIcon createImageIcon(String path) {
       java.net.URL imgURL = getClass().getResource(path);
       if (imgURL != null) {
           return new ImageIcon(imgURL);
       } else {
           System.err.println("Couldn't find file: " + path);
           return null;
       }
   }

   public void itemStateChanged(ItemEvent e) {

   }
   
   
   // URL example from: http://stackoverflow.com/questions/527719/how-to-add-hyperlink-in-jlabel
   
   private static void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { /* TODO: error handling */ }
	    } else { /* TODO: error handling */ }
	  }   

   class OpenUrlAction implements ActionListener {
	      @Override public void actionPerformed(ActionEvent e) {
	        open(uri);
	      }
	    }


}

