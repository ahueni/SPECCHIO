package ch.specchio.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ch.specchio.client.SPECCHIODatabaseDescriptor;
import ch.specchio.client.SPECCHIOServerDescriptor;
import ch.specchio.client.SPECCHIOWebAppDescriptor;


/**
 * Abstract base class for all server descriptor panels in the database connection dialogue.
 */
public abstract class ServerDescriptorPanel extends JPanel {

	/** serialisation version ID */
	private static final long serialVersionUID = 1L;
	
	/** server name field */
	private JTextField server_field;
	
	/** path or schema name field */
	private JTextField path_field;
	
	/* port number field */
	private JTextField port_field;
	
	private JTextField datasource_name_field;	
	
	
	/** username field */
	private JTextField user_field;
	
	/** password field */
	private JTextField password_field;
  
	/** layout constraints */
	private GridBagConstraints constraints = new GridBagConstraints();
	
	/** layout utility */
	private GridbagLayouter l;
	
	/** length of text fields */
	private int field_length = 20;
	
	/** is this panel anonymous? */
	private boolean anonymous;

	/**
	 * Constructor.
	 * 
	 * @param app		the server descriptor with which to initialise the panel (may be null)
	 * @param anonymous	if true, do not display the username and password fields
	 */
	protected ServerDescriptorPanel(SPECCHIOServerDescriptor app, String server_label, String path_label, String datasource_name_label, boolean anonymous)
	{
	  	super();
		   	
	   	this.anonymous = anonymous;
			
		constraints.gridwidth = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 0;
		constraints.gridy = 0;
						
		l = new GridbagLayouter(this);
			
		l.insertComponent(new JLabel(server_label), constraints);	
		
		server_field = new JTextField(field_length);   
		constraints.gridx = 1;	
		l.insertComponent(server_field, constraints);
									
		constraints.gridx = 0;
		constraints.gridy++;	
		l.insertComponent(new JLabel("Port"), constraints);
			
		port_field = new JTextField(field_length);   
		constraints.gridx = 1;
		l.insertComponent(port_field, constraints);
				
		constraints.gridx = 0;
		constraints.gridy++;
		l.insertComponent(new JLabel(path_label), constraints);
			
		path_field = new JTextField(field_length);   
		constraints.gridx = 1;	
		l.insertComponent(path_field, constraints);
		
		constraints.gridx = 0;
		constraints.gridy++;
		l.insertComponent(new JLabel(datasource_name_label), constraints);		
		
		datasource_name_field = new JTextField(field_length);   
		constraints.gridx = 1;	
		l.insertComponent(datasource_name_field, constraints);		
		
		
		if (!anonymous) {
			
			constraints.gridx = 0;
			constraints.gridy++;
			l.insertComponent(new JLabel("Username"), constraints);
			
			user_field = new JTextField(field_length);   
			constraints.gridx = 1;
			l.insertComponent(user_field, constraints);
				
			constraints.gridx = 0;
			constraints.gridy++;
			l.insertComponent(new JLabel("Password"), constraints);
			
			password_field = new JPasswordField(field_length);   
			constraints.gridx = 1;
			l.insertComponent(password_field, constraints);
			
		}
		
		// pre-fill values if we were given an application server descriptor
		if (app != null) {
			setServerDescriptor(app);
		}
		   
	}
	
	
	/**
	 * Get the contents of the password field.
	 * 
	 * @return the contents of the password field.
	 */
	public String getPassword() {
		
		return password_field.getText();
		
	}
	
	
	/**
	 * Get the contents of the path field.
	 * 
	 * @return the contents of the path field.
	 */
	public String getPath() {
		
		return path_field.getText();
		
	}
	
	
	/**
	 * Get the port number.
	 * 
	 * @return	the number currently specified in the "port" field, or zero if it is empty
	 * 
	 * @throws NumberFormatException	the port number field does not contain a valid port number
	 */
	public int getPortNumber() throws NumberFormatException {
		
		return Integer.parseInt(port_field.getText());
		
	}
	
	
	/**
	 * Get the server descriptor described by this panel
	 * 
	 * @return a new server descriptor configured according the values in this panel
	 */
	public abstract SPECCHIOServerDescriptor getServerDescriptor();
	
	
	/**
	 * Get the contents of the data source name field.
	 * 
	 * @return the contents of the data source name field
	 */
	public String getDataSourceName() {
		
		return datasource_name_field.getText();
		
	}	
	
	/**
	 * Get the contents of the server name field.
	 * 
	 * @return the contents of the server name field
	 */
	public String getServerName() {
		
		return server_field.getText();
		
	}
	
	
	/**
	 * Get the contents of the username field.
	 * 
	 * @return the contents of the username field.
	 */
	public String getUsername() {
		
		return user_field.getText();
		
	}
	
	
	/**
	 * Return "true" if the panel is for an anonymous connection.
	 * 
	 * @return "true" if the panel is anonymous, "false" otherwise
	 */
	public boolean isAnonymous() {
		
		return anonymous;
		
	}
	
	
	/**
	 * Set the contents of the dataSourceName field.
	 * 
	 * @param	dataSourceName	dataSourceName
	 */
	public void setDataSourceName(String dataSourceName) {
		
		datasource_name_field.setText(dataSourceName);
		
	}	
	
	
	/**
	 * Set the contents of the password field.
	 * 
	 * @param password	the password
	 */
	public void setPassword(String password) {
		
		password_field.setText(password);
		
	}
	
	
	/**
	 * Set the contents of the path field.
	 * 
	 * @param	path	Path
	 */
	public void setPath(String path) {
		
		path_field.setText(path);
		
	}
	
	
	/**
	 * Set the contents of the port field.
	 * 
	 * @param port	the port (0 to clear the field)
	 */
	public void setPort(int port) {
		
		if (port != 0) {
			port_field.setText(Integer.toString(port));
		} else {
			port_field.setText(null);
		}
		
	}
	
	   
	/**
	 * Fill the panel fields using data from a server descriptor.
	 * 
	 * @param d	the descriptor
	 */
	public abstract void setServerDescriptor(SPECCHIOServerDescriptor d);
	
	
	/**
	 * Set the contents of the server name field.
	 * 
	 * @param server	the server name
	 */
	public void setServerName(String server) {
		
		server_field.setText(server);
		
	}
	
	
	/**
	 * Set the contents of the username field.
	 * 
	 * @param username	the user name
	 */
	public void setUsername(String username) {
		
		user_field.setText(username);
		
	}

}


/**
 * Server panel for web applications.
 */
class WebAppDescriptorPanel extends ServerDescriptorPanel
{
	   
	/** serialisation version ID */
	private static final long serialVersionUID = 1L;
	
	/** connection protocol */
	private String protocol;

	private SPECCHIOWebAppDescriptor app;

	
   public SPECCHIOWebAppDescriptor getApp() {
		return app;
	}


	public void setApp(SPECCHIOWebAppDescriptor app) {
		this.app = app;
	}


/**
    * Constructor.
    * 
    * @param app		the web application descriptor with which to initialise the panel (may be null)
    * @param anonymous	if true, do not display the username and password fields
    */
   public WebAppDescriptorPanel(SPECCHIOWebAppDescriptor app, boolean anonymous)
   {
	   	super(app, "Web Application Server", "Application Path", "Data Source Name", anonymous);
   }
   
   
   /**
    * Get a web application descriptor corresponding to the current values
	* of the fields in the panel.
	*
	* @return a new web application descriptor
	*/
	public SPECCHIOServerDescriptor getServerDescriptor() {
		
		int port;
		try {
			port = getPortNumber();
			if (port == 0)
				port = 443;
		}
		catch (NumberFormatException ex) {
			// use the default port number
			port = 443;
		}
		
		if (!isAnonymous()) {
			return new SPECCHIOWebAppDescriptor(
				(protocol != null) ? protocol : "https",
				getServerName(),
				port,
				getPath(),
				getUsername(),
				getPassword(),
				getDataSourceName(),
				(this.app != null ? app.getPreferenceNodeName() : "")
			);
		} else {
			return new SPECCHIOWebAppDescriptor(
				(protocol != null) ? protocol : "https",
				getServerName(),
				port,
				getPath(),
				getDataSourceName()
			);
		}
		   
	}
	   
	   
	/**
	 * Fill the panel fields using data from a web application descriptor.
	 * 
	 * @param d	the descriptor
	 */
	public void setServerDescriptor(SPECCHIOServerDescriptor d) {

	   if (d != null && d instanceof SPECCHIOWebAppDescriptor) {
		   app = (SPECCHIOWebAppDescriptor)d;
		   protocol = app.getProtocol();
		   setServerName(app.getServer());
		   setPath(app.getPath());
		   setPort(app.getPort());
		   setDataSourceName(app.getDataSourceName());
		   if (!isAnonymous()) {
			   setUsername(app.getDisplayUser());
			   setPassword(app.getPassword());
		   }
	   } else {
		   protocol = "https";
		   setServerName(null);
		   setPath(null);
		   setPort(0);
		   setDataSourceName(null);
		   if (!isAnonymous()) {
			   setUsername(null);
			   setPassword(null);
		   }
	   }
	}
	
}


/**
 * Server panel for direct database connections.
 */
class DatabaseDescriptorPanel extends ServerDescriptorPanel
{
	   
	/** serialisation version ID */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Constructor.
	 * 
	 * @param app		the web application descriptor with which to initialise the panel (may be null)
	 * @param anonymous	if true, do not display the username and password fields
	 */
	public DatabaseDescriptorPanel(SPECCHIODatabaseDescriptor app, boolean anonymous)
	{
	  	super(app, "Database Hostname", "Database Schema Name", "Data Source Name", anonymous);
			   
	}
	   
	   
	/**
	 * Get a web application descriptor corresponding to the current values
	 * of the fields in the panel.
	 *
	 * @return a new web application descriptor
	 */
	public SPECCHIOServerDescriptor getServerDescriptor() {
		
		int port;
		try {
			port = getPortNumber();
			if (port == 0)
				port = 3306;
		}
		catch (NumberFormatException ex) {
			// use the default port number
			port = 3306;
		}
		
		if (!isAnonymous()) {
			return new SPECCHIODatabaseDescriptor(
				getServerName(),
				port,
				getPath(),
				getUsername(),
				getPassword(),
				getDataSourceName()
			);
		} else {
			return new SPECCHIODatabaseDescriptor(
				getServerName(),
				port,
				getPath(),
				getDataSourceName()
			);
		}
		   
	}
	   
	   
	/**
	 * Fill the panel fields using data from a database descriptor.
	 * 
	 * @param d	the descriptor
	 */
	public void setServerDescriptor(SPECCHIOServerDescriptor d) {

	   if (d != null && d instanceof SPECCHIODatabaseDescriptor) {
		   SPECCHIODatabaseDescriptor app = (SPECCHIODatabaseDescriptor)d;
		   setServerName(app.getServer());
		   setPath(app.getDatabaseName());
		   setPort(app.getPort());
		   setDataSourceName(app.getDataSourceName());
		   if (!isAnonymous()) {
			   setUsername(app.getDisplayUser());
			   setPassword(app.getPassword());
		   }
	   } else {
		   setServerName(null);
		   setPath(null);
		   setPort(0);
		   setDataSourceName(null);
		   if (!isAnonymous()) {
			   setUsername(null);
			   setPassword(null);
		   }
	   }
	}
	
}
