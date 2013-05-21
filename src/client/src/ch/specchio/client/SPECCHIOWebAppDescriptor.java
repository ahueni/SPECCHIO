package ch.specchio.client;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import ch.specchio.types.User;


/**
 * This class encapsulates all of the information necessary to connect to a
 * SPECCHIO web application server.
 */
public class SPECCHIOWebAppDescriptor implements SPECCHIOServerDescriptor {
	
	/** protocol */
	private String protocol;
	
	/** server host name */
	private String server;
	
	/** application path */
	private  String path;
	
	/** port number */
	private int port;
	
	/** database user name */
	private String user;
	
	/** database password */
	private String password;
	   
	
	/**
	 * Constructor.
	 * 
	 * @param protocol	"http" or "https"
	 * @param server	the server host name
	 * @param path		the path to the web application
	 * @param port		the port number
	 * @param user		the username
	 * @param password	the password
	 */
	public SPECCHIOWebAppDescriptor(String protocol, String server, int port, String path, String user, String password) {
		   
		this.protocol = protocol;
		this.server = server;
		this.port = port;
		this.path = path;
		this.user = user;
		this.password = password;
		
	}
	
	
	/**
	 * Construct an anonymous descriptor.
	 * 
	 * @param protocol	"http" or "https"
	 * @param server	the server host name
	 * @param path		the path to the web application
	 * @param port		the port number
	 */
	public SPECCHIOWebAppDescriptor(String protocol, String server, int port, String path) {
		
		this(protocol, server, port, path, null, null);
		
	}
	
	
	/**
	 * Constructor for SPECCHIOClientFactory.process_line().
	 * 
	 * @param tokens	the array of strings read from the input file
	 * 
	 * @throws SPECCHIOWebClientException	the tokens are not correctly formatted
	 */
	public SPECCHIOWebAppDescriptor(String tokens[]) throws SPECCHIOWebClientException {
		
		// check that we have the correct number of tokens
		if (tokens.length < 6) {
			throw new SPECCHIOWebClientException("Insufficient configuration informaton provided for a SPECCHIO web application server.");
		}
		
		try {
			this.protocol = tokens[0];
			this.server = tokens[1];
			this.port = Integer.parseInt(tokens[2]);
			this.path = tokens[3];
			this.user = tokens[4];
			this.password = tokens[5];
		}
		catch (NumberFormatException ex) {
			// invalid port number
			throw new SPECCHIOWebClientException("Invalid port number provided for a SPECCHIO web application server.");
		}
		
	}
		
	
	/**
	 * Create a web client that can connect to the server represented by
	 * this descriptor.
	 * 
	 * @return a new SPECCHIOWebClient object
	 */
	public SPECCHIOClient createClient() {
		
		if (user == null) {
			// create an anonymous client
			return new SPECCHIOWebClient(getUrl());
		} else {
			// create a named client
			return new SPECCHIOWebClient(getUrl(), getDisplayUser(), getPassword());
		}
		
	}
	
	
	/**
	 * Get the display name of this server.
	 * 
	 * @return a string describing the server, suitable for display to the user
	 */
	public String getDisplayName() {
		
		return protocol + "://" + user + "@" + server + ":" + port + path;
		
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
	 * Get the application path.
	 * 
	 * @return the application path
	 */
	public String getPath() {
		
		return path;
		
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
	 * Get the protocol used to connect to the server.
	 * 
	 * @return "http" or "https"
	 */
	public String getProtocol() {
		
		return protocol;
		
	}
	
	
	/**
	 * Get the web application server name.
	 * 
	 * @return the web application server name
	 */
	public String getServer() {
		
		return server;
		
	}
	
	
	/**
	 * Get the URL of the application server.
	 */
	public URL getUrl() {
		
		URL url = null;
		try {
			url = new URL(protocol, server, port, path);
		}
		catch (MalformedURLException ex) {
			// should never happen
			ex.printStackTrace();
		}
		
		return url;
	
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
	 * Get a string representation of the server.
	 */
	public String toString() {
		
		return protocol + "://" + user + "@" + server + ":" + port + path;
		
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
	 * Write the configuration of a user account.
	 * 
	 * @param w		the writer with which to write
	 * 
	 * @throws IOException	I/O error
	 */
	public void writeAccountConfiguration(Writer w) throws IOException {
		
		// format is: protocol, server, port, path, username, password
		w.write(
			protocol + ", " +
			server + ", " +
			port + ", " +
			path + ", " +
			user + ", " +
			password +
			"\n"
		);
		
	}

}
