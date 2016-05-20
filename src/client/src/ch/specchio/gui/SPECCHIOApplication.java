package ch.specchio.gui;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.client.SPECCHIOClientFactory;
import ch.specchio.client.SPECCHIOServerDescriptor;


public class SPECCHIOApplication {

	
	private static JFrame frame;
	private static SPECCHIOApplication instance = null;
	private SPECCHIOClient client = null;
	public static Float min_db_version = 3.2F;
	public static ImageIcon specchio_icon;
	
	/* progress report in the operations pane */
	static ProgressReportTextPanel p_rep = null;
	
	protected SPECCHIOApplication()
	{
	}
	
	public SPECCHIOClient getClient()
	{
		return client;
	}
	
	public static SPECCHIOApplication getInstance() 
	{
		if(instance == null) {
			instance = new SPECCHIOApplication();
		}
		return instance;
	}      
	
	public JFrame get_frame()
	{
		return frame;
	}
	

	public Component createComponents() {

		JPanel pane = new JPanel(new BorderLayout());
		OperationsPane op = OperationsPane.getInstance();
		specchio_icon =new ImageIcon(SPECCHIOClientFactory.getApplicationFilepath("SPECCHIO_Icon_Mid_Res_small.jpg"));
		  
		JLabel test = new JLabel("Spectral Database System", specchio_icon, JLabel.CENTER);
		test.setVerticalTextPosition(JLabel.BOTTOM);
		test.setHorizontalTextPosition(JLabel.CENTER);
		test.setOpaque(true);
		test.setBackground(Color.WHITE);
		  
		JPanel mid_pane = new JPanel(new BorderLayout());
			
		mid_pane.add(BorderLayout.CENTER, test);

		pane.add(mid_pane, BorderLayout.WEST);
		pane.add(op, BorderLayout.CENTER);
			
		op.validate();
	      
		pane.validate();

		return pane;
	}


	   /**
	    * Create the GUI and show it.  For thread safety,
	    * this method should be invoked from the
	    * event-dispatching thread.
	    */
	   private static void createAndShowGUI() 
	   {
	   System.out.println("Welcome to " + SPECCHIO_ReleaseInfo.getVersion());
	      //Create and set up the window.
	      frame = new JFrame(SPECCHIO_ReleaseInfo.getVersion());
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	      SPECCHIOApplication app = SPECCHIOApplication.getInstance();
	      Component contents = app.createComponents();
	      frame.getContentPane().add(contents, BorderLayout.CENTER);

	      /////////////// add the menu //////////////
	      MainMenu menu = MainMenu.getInstance();
	      try {
	    	  menu.enable_tools_for_client(null);
	      }
	      catch (SPECCHIOClientException ex) {
	    	  // enable_tools_for_client() never throws an exception for a null argument
	    	  ex.printStackTrace();
	      }
	     
	      frame.setJMenuBar(menu.getMenu());

	      //Display the window.
	      frame.pack();
	      frame.setSize(new Dimension(700, 350));

	      frame.setVisible(true);
	   }

	   
	   public static void main(String[] args) {
		   //Schedule a job for the event-dispatching thread:
		   //creating and showing this application's GUI.
		   try {
			   UIManager.setLookAndFeel(
					   UIManager.getSystemLookAndFeelClassName());
		   } catch (Exception e) {
		   }
		   
		   javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() {
				   createAndShowGUI();
			   }
		   }
		   );
	   }
	   
	   
	   public void setClient(SPECCHIOClient client) {
		   
		   this.client = client;

		   OperationsPane op = OperationsPane.getInstance();
		   if (client != null) {
			   SPECCHIOServerDescriptor d = client.getServerDescriptor();
			   
			   	// add connection details to the operations pane
			   if (p_rep == null) {
				   p_rep = new ProgressReportTextPanel("Database connection status", d.getDisplayName(false, false));
	  		  		op.add_report(p_rep);	
			   }
			   p_rep.set_operation("Connected as " + d.getDisplayUser() + " to '" + d.getDataSourceName() + "' on:");
			   p_rep.set_component(d.getDisplayName(false, false));
			   
			   try {
				   // enable menu items appropriate for this connection
				   MainMenu.getInstance().enable_tools_for_client(client);
			   }
			   catch (SPECCHIOClientException ex) {
				   // lost the connection to the server already!
				   ErrorDialog error = new ErrorDialog(frame, "Could not establish user privileges", ex.getUserMessage(), ex);
				   error.setVisible(true);
			   }
		   } else {
			   // remove connection details from the operations pane
			   if (p_rep != null) {
				   op.remove_report(p_rep);
				   p_rep = null;
			   }
			   
			   // disable menu items
			   try {
				   MainMenu.getInstance().enable_tools_for_client(null);
			   }
			   catch (SPECCHIOClientException ex) {
				   // enable_tools_for_client() never throws an exception for a null argument
				   ex.printStackTrace();
			   }
			   
		   }
		   
	   }

}
