package ch.specchio.client;

import ch.specchio.types.User;


/**
 * This interface must be implemented by all server descriptors.
 */
public interface SPECCHIOServerDescriptor {
	
	/**
	 * Create a client object suitable for connecting to the server
	 * described by this descriptor.
	 * 
	 * @return a new SPECCHIOClient object
	 * 
	 * @throws SPECCHIOClientException	could not create the client
	 */
	public SPECCHIOClient createClient() throws SPECCHIOClientException;


	/**
	 * Get the display name of this server.
	 * 
	 * @param showUser	include the user account details in the display?
	 * @param show_datasource_name	include the JNDI details in the display?
	 * 
	 * @return a string describing the server, suitable for display to the user
	 */
	public String getDisplayName(boolean showUser, boolean show_datasource_name);
	
	
	/**
	 * Get a description of the user account under which we are to log in.
	 * 
	 * @return a string describing the user account under which this client is to log in
	 */
	public String getDisplayUser();
	
	/**
	 * Get a name of the datasource (JNDI).
	 * 
	 * @return a string holding the name of the datasource (JNDI)
	 */
	public String getDataSourceName();	
	
	
	/**
	 * Set the user information associated with this account.
	 * 
	 * @param user	the user information
	 */
	public void setUser(User user);

}
