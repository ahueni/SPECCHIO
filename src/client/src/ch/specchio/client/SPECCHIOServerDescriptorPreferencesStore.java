package ch.specchio.client;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


/**
 * This class represents database connection data stored in the Java
 * user preferences store. Accounts are stored in the node with the
 * name defined by ACCOUNT_CONFIGURATION_PATH.
 * 
 * Each child node of the root node contains one server descriptor.
 * The type of the server descriptor is determined by the value of
 * the "protocol" key. Other keys store the configuration of the
 * server descriptor according to the documentation associated with
 * the factory for that descriptor.
 */
public class SPECCHIOServerDescriptorPreferencesStore extends SPECCHIOServerDescriptorStore {
	
	/** the path to the root node of the account configuration data */
	private static final String ACCOUNT_CONFIGURATION_PATH = "ch.specchio.client/accounts";
	
	/** the name of the key that stores the protocol */
	private static final String PREFS_KEY_PROTOCOL = "protocol";
	
	/** the root node of the account configuration data */
	private Preferences accountConfigurationPreferences;
	
	/** the collection of server descriptors */
	private Collection<SPECCHIOServerDescriptor> descriptors;
	
	/**
	 * Constructor.
	 * 
	 * @throws BackingStoreException the preferences store could not be accessed
	 */
	public SPECCHIOServerDescriptorPreferencesStore() throws BackingStoreException {
		
		// get the root node of the account configuration data
		accountConfigurationPreferences = Preferences.userRoot().node(ACCOUNT_CONFIGURATION_PATH);
		
		// load account configuration data for all accounts
		descriptors = new LinkedList<SPECCHIOServerDescriptor>();
		for (String childName : accountConfigurationPreferences.childrenNames()) {
			Preferences childNode = accountConfigurationPreferences.node(childName);
			
			// build a server descriptor depending on the protocol associated with the node
			SPECCHIOServerDescriptor d = null;
			String protocol = childNode.get(PREFS_KEY_PROTOCOL, "");
			if (protocol.equals("http") || protocol.equals("https")) {
				SPECCHIOWebAppDescriptorFactory f = new SPECCHIOWebAppDescriptorFactory();
				d = f.buildServerDescriptor(childNode);
			}
			
			if (d != null) {
				descriptors.add(d);
			}
		}
		
	}
	
	
	/**
	 * Add the configuration for an account to the configuration file. The
	 * new preferences node will be named with an integer one higher than
	 * any of the child names so far.
	 * 
	 * @param d		the descriptor of the server on which the new account exists
	 * 
	 * @throws IOException	the backing store failed
	 * 
	 */
	public void addServerDescriptor(SPECCHIOServerDescriptor d) throws IOException {
		
		String nodeName;
		try {
			int highestChild = 0;
			for (String childName : accountConfigurationPreferences.childrenNames()) {
				int childInt = Integer.parseInt(childName);
				if (childInt > highestChild) {
					highestChild = childInt;
				}
			}
			nodeName = Integer.toString(highestChild + 1);
		}
		catch (BackingStoreException ex) {
			// the backing store failed; re-throw as an IOException
			throw new IOException(ex);
		}
		
		// create a node in the preferences store
		Preferences node = accountConfigurationPreferences.node(nodeName);
		if (d instanceof SPECCHIOWebAppDescriptor) {
			SPECCHIOWebAppDescriptorFactory f = new SPECCHIOWebAppDescriptorFactory();
			f.fillPreferencesNode(node, (SPECCHIOWebAppDescriptor)d);
		}
		
		// add the server descriptor to the list of known descriptors
		descriptors.add(d);
		
	}
	
	

	
	/**
	 * Get an iterator through all of the descriptors in the store.
	 * 
	 * @return an iterator
	 */
	public Iterator<SPECCHIOServerDescriptor> getIterator() {
		
		return descriptors.iterator();
		
	}
	
	
	/**
	 * Update the configuration for an account in the configuration file. 
	 * 
	 * @param d		the descriptor of the server on which the new account exists
	 * 
	 * @throws IOException	the backing store failed
	 * 
	 */
	public void updateServerDescriptor(SPECCHIOServerDescriptor d) throws IOException {
		
		// get the root node of the account configuration data
		accountConfigurationPreferences = Preferences.userRoot().node(ACCOUNT_CONFIGURATION_PATH);
		
		// load account configuration data for all accounts
		descriptors = new LinkedList<SPECCHIOServerDescriptor>();
		try {
			for (String childName : accountConfigurationPreferences.childrenNames()) {
				Preferences childNode = accountConfigurationPreferences.node(childName);
				
				// check if names match
				if(d.getPreferenceNodeName().equals(childName))
				{
					// update this preference 
					SPECCHIOWebAppDescriptorFactory f = new SPECCHIOWebAppDescriptorFactory();
					f.fillPreferencesNode(childNode, (SPECCHIOWebAppDescriptor)d);
				}
			}
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}	
	
	
	/**
	 * Web application server descriptor factory. The preferences node
	 * corresponding to a descriptor contains keys-value pairs as
	 * follows:
	 * 
	 * PREFS_KEY_PROTOCOL			the protocol ("http" or "https")
	 * PREFS_KEY_WEBAPP_SERVER		the server name
	 * PREFS_KEY_WEBAPP_PORT		the port number
	 * PREFS_KEY_WEBAPP_PATH		the path
	 * PREFS_KEY_WEBAPP_USERNAME	the username
	 * PREFS_KEY_WEBAPP_PASSWORD	the password
	 * PREFS_KEY_WEBAPP_DATASOURCE	the datasource
	 */
	private class SPECCHIOWebAppDescriptorFactory {
		
		/** key for the server name */
		private static final String PREFS_KEY_WEBAPP_SERVER = "server";
		
		/** key for the port number */
		private static final String PREFS_KEY_WEBAPP_PORT = "port";
		
		/** key for the path */
		private static final String PREFS_KEY_WEBAPP_PATH = "path";
		
		/** key for the username */
		private static final String PREFS_KEY_WEBAPP_USERNAME = "username";
		
		/** key for the password */
		private static final String PREFS_KEY_WEBAPP_PASSWORD = "password";

		/** key for the jdbc datasource name */
		private static final String PREFS_KEY_WEBAPP_DATASOURCE = "datasource";
		
		/** key for the trust store setting name */
		private static final String PREFS_KEY_WEBAPP_TRUST_STORE_SETTING = "truststore_setting";		

		
		/**
		 * Build a server descriptor object from a preferences node.
		 * 
		 * @param node	the node
		 * 
		 * @return a new server descriptor object corresponding to the node
		 */
		public SPECCHIOServerDescriptor buildServerDescriptor(Preferences node) {
			
			
			
			return new SPECCHIOWebAppDescriptor(
				node.get(PREFS_KEY_PROTOCOL, ""),
				node.get(PREFS_KEY_WEBAPP_SERVER, ""),
				node.getInt(PREFS_KEY_WEBAPP_PORT, 0),
				node.get(PREFS_KEY_WEBAPP_PATH, ""),
				node.get(PREFS_KEY_WEBAPP_USERNAME, ""),
				node.get(PREFS_KEY_WEBAPP_PASSWORD, ""),
				node.get(PREFS_KEY_WEBAPP_DATASOURCE, ""),
				node.getBoolean(PREFS_KEY_WEBAPP_TRUST_STORE_SETTING, false), // default trust store is not used by default
				node.name()
			);
			
		}
		
		
		/**
		 * Fill a preferences node with data corresponding to a web application
		 * descriptor.
		 * 
		 * @param node	the node to be filled
		 * @param d		the web application descriptor
		 */
		public void fillPreferencesNode(Preferences node, SPECCHIOWebAppDescriptor d) {
			
			node.put(PREFS_KEY_PROTOCOL, d.getProtocol());
			node.put(PREFS_KEY_WEBAPP_SERVER, d.getServer());
			node.putInt(PREFS_KEY_WEBAPP_PORT, d.getPort());
			node.put(PREFS_KEY_WEBAPP_PATH, d.getPath());
			node.put(PREFS_KEY_WEBAPP_USERNAME, d.getDisplayUser());
			node.put(PREFS_KEY_WEBAPP_PASSWORD, d.getPassword());
			node.put(PREFS_KEY_WEBAPP_DATASOURCE, d.getDataSourceName());
			node.putBoolean(PREFS_KEY_WEBAPP_TRUST_STORE_SETTING, d.usesDefaultTrustStore());
			
		}
		
	}
	
	

}
