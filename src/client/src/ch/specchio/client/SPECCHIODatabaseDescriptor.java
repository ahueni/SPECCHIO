package ch.specchio.client;

import ch.specchio.types.User;

/**
 * This class encapsulates all of the information necessary to connect to a
 * local database via a JDBC driver.
 */
public class SPECCHIODatabaseDescriptor implements SPECCHIOServerDescriptor {
	
	/** database host name */
	private String server;
	
	/** database port number */
	private int port;
	
	/** database schema name */
	private String schema;
	
	/** database user name */
	private String user;
	
	/** database password */
	private String password;
	   
	
	/**
	 * Constructor.
	 * 
	 * @param server	the database host name
	 * @param port		the database port number
	 * @param schema	the database schema name
	 * @param user		the username
	 * @param password	the password
	 */
	public SPECCHIODatabaseDescriptor(String server, int port, String schema, String user, String password) {
		
		this.server = server;
		this.port = port;
		this.schema = schema;
		this.user = user;
		this.password = password;
		
	}
	   
	
	/**
	 * Constructor for an anonymous connection to the database.
	 * 
	 * @param server	the database host name
	 * @param port		the database port number
	 * @param schema	the database schema name
	 * @param user		the username
	 * @param password	the password
	 */
	public SPECCHIODatabaseDescriptor(String server, int port, String schema) {
		
		this.server = server;
		this.port = port;
		this.schema = schema;
		
	}
	
	
	/**
	 * Constructor for SPECCHIOClientFactory.process_line().
	 * 
	 * @param tokens	the array of strings read from the input file
	 * 
	 * @throws SPECCHIODatabaseClientException	the tokens are not correctly formatted
	 */
	public SPECCHIODatabaseDescriptor(String tokens[]) throws SPECCHIODatabaseClientException {
		
		// check that we have the correct number of tokens
		if (tokens.length < 6) {
			throw new SPECCHIODatabaseClientException("Insufficient configuration informaton provided for a SPECCHIO web application server.");
		}
		
		try {
			this.server = tokens[1];
			this.port = Integer.parseInt(tokens[2]);
			this.schema = tokens[3];
			this.user = tokens[4];
			this.password = tokens[5];
		}
		catch (NumberFormatException ex) {
			// invalid port number
			throw new SPECCHIODatabaseClientException("Invalid port number provided for a SPECCHIO database.");
		}
		
	}
	
	
	/**
	 * Create a web client that can connect to the server represented by
	 * this descriptor.
	 * 
	 * @return a new SPECCHIODatabaseClient object
	 */
	public SPECCHIOClient createClient() {
		
		return null;
	
	}
	
	
	/**
	 * Get the string describing the account configuration for db_config.txt
	 */
	public String getAccountConfigurationString() {
		
		// format is: protocol, server, port, schema, username, password
		return
			"jdbc" + ", " +
			server + ", " +
			port + ", " +
			schema + ", " +
			user + ", " +
			password;
		
	}


	/**
	 * The the database name.
	 * 
	 * @return the database schema name.
	 */
	public String getDatabaseName() {
		
		return schema;
		
	}
	
	
	/**
	 * Get the display name of this server.
	 * 
	 * @param showUser	include the user account details in the display?
	 * 
	 * @return a string describing the server, suitable for display to the user
	 */
	public String getDisplayName(boolean showUser) {
		
		return "jdbc:mysql://" + (showUser ? user + "@" : "") + server + ":" + port + "/" + schema;
		
	}
	
	
	/**
	 * Get the user name.
	 * 
	 * @return the user name
	 */
	public String getDisplayUser() {
		
		return (user != null)? user : "anonymous";
		
	}


	/**
	 * Get the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		
		return password;
		
	}
	
	/**
	 * Get the port number.
	 * 
	 * @return the port number
	 */
	public int getPort() {
		
		return port;
		
	}
	
	
	/**
	 * Get the database server name.
	 * 
	 * @return the database server name
	 */
	public String getServer() {
		
		return server;
		
	}
	
	
	/**
	 * Set the user information associated with this account.
	 * 
	 * @param user	the user information
	 */
	public void setUser(User user) {
		
		this.user = user.getUsername();
		this.password = user.getPassword();
		
	}
	
	
	/**
	 * Get a string representation of the server.
	 */
	public String toString() {
		
		return getDisplayName(true);
		
	}

}
