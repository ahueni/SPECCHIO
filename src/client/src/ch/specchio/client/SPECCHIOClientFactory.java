package ch.specchio.client;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.prefs.BackingStoreException;

import ch.specchio.gui.SPECCHIOApplication;

public class SPECCHIOClientFactory {
	
	/** singleton instance of the web client factory */
	private static SPECCHIOClientFactory instance = null;
	
	/** the legacy configuration file name */
	private static final File legacyConfigFile = new File(SPECCHIOClientFactory.getApplicationFilepath("db_config.txt"));
	
	/** the list of known servers */
	private List<SPECCHIOServerDescriptor> apps;
	
	/** the current client object */
	private SPECCHIOClient current_client;
	
	/**
	 * Constructor.
	 * 
	 * @throws SPECCHIOClientException	the configuration data is invalid or inaccessible
	 */
	private SPECCHIOClientFactory() throws SPECCHIOClientException {
		
		// initialise the server descriptor list
		apps = new LinkedList<SPECCHIOServerDescriptor>();
		
		// set up SSL trust store
		System.setProperty("javax.net.ssl.trustStore", SPECCHIOClientFactory.getApplicationFilepath("specchio.keystore"));
		System.setProperty("javax.net.ssl.trustStorePassword", "specchio");
		
		
		// load server descriptors from the legacy db_config.txt file
		if (legacyConfigFile.exists()) {
			try {
				SPECCHIOServerDescriptorStore s = new SPECCHIOServerDescriptorLegacyStore(legacyConfigFile);
				Iterator<SPECCHIOServerDescriptor> iter = s.getIterator();
				while (iter.hasNext()) {
					apps.add(iter.next());
				}
			}
			catch (IOException ex) {
				// read error; re-throw as a SPECCHIO client exception
				throw new SPECCHIOClientException(ex);
			}
		}		
		else // load server descriptors from the preferences store
		{
			try {
				SPECCHIOServerDescriptorStore s = new SPECCHIOServerDescriptorPreferencesStore();
				Iterator<SPECCHIOServerDescriptor> iter = s.getIterator();
				while (iter.hasNext()) {
					apps.add(iter.next());
				}
			}
			catch (BackingStoreException ex) {
				// the backing store failed; re-throw as a SPECCHIO client exception
				throw new SPECCHIOClientException(ex);
			}	
		}
		
		
	}
	
	
	/**
	 * Add the configuration for an account to the configuration file.
	 * 
	 * @param d		the descriptor of the server on which the new account exists
	 * 
	 * @throws IOException file error
	 * @throws SPECCHIOClientException invalid configuration data
	 */
	public void addAccountConfiguration(SPECCHIOServerDescriptor d) throws IOException, SPECCHIOClientException {
		
		try {
			// all new accounts are saved to the Java preferences store
			SPECCHIOServerDescriptorStore store = new SPECCHIOServerDescriptorPreferencesStore();
			store.addServerDescriptor(d);
		}
		catch (BackingStoreException ex) {
			// the backing store failed; re-throw as an IOException
			throw new IOException(ex);
		}
		
		// update the internal list of server descriptors
		apps.add(d);
		
	}
	
	
	public static String getApplicationFilepath(String name)
	{
		File conf_file = null;
		// check if the file is found in the current directory
		
		conf_file = new File(name);
		
		if(conf_file.isFile())
		{
			//System.out.println(name + " found in current dir.");
			return conf_file.getPath();
		}
		else
		{
			// 
			try {
				File app_dir = new File(SPECCHIOApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI());
				
				conf_file = new File(app_dir.getParent() + File.separator + name);
				
				//System.out.println(name + " not found in current dir but here: " + conf_file);
				
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return conf_file.getPath();
	}
	
	/**
	 * Connect to a SPECCHIO web application server.
	 * 
	 * @param app		the descriptor of the SPECCHIO web application to which to connect
	 *
	 * @return a web client object connected to the new application server
	 * 
	 * @throws SPECCHIOClientConnection	could not create the client
	 */
	public SPECCHIOClient createClient(SPECCHIOServerDescriptor app) throws SPECCHIOClientException {
		
		current_client = new SPECCHIOClientCache(app.createClient());
		return current_client;
	
	}
	
	
	/**
	 * Return the single instance of the SPECCHIO web client factory.
	 * 
	 * @return the single instance of the SPECCHIO web client factory
	 * 
	 * @throws SPECCHIOWebClientException	the configuration file is invalid
	 */
	public static SPECCHIOClientFactory getInstance() throws SPECCHIOClientException {
		
		if (instance == null) {
			instance = new SPECCHIOClientFactory();
		}
		
		return instance;
		
	}
	
	
	/**
	 * Return the list of known web application servers.
	 *
	 * @return the list of known web application servers
	 */
	public List<SPECCHIOServerDescriptor> getAllServerDescriptors() {
		
		return apps;
		
	}


	public void reloadDBConfigFile() {
		// initialise the server descriptor list
		apps = new LinkedList<SPECCHIOServerDescriptor>();
		
		// load server descriptors from the legacy db_config.txt file
		if (legacyConfigFile.exists()) {
			try {
				SPECCHIOServerDescriptorStore s = new SPECCHIOServerDescriptorLegacyStore(legacyConfigFile);
				Iterator<SPECCHIOServerDescriptor> iter = s.getIterator();
				while (iter.hasNext()) {
					apps.add(iter.next());
				}
			}
			catch (IOException ex) {
				// read error; re-throw as a SPECCHIO client exception
				throw new SPECCHIOClientException(ex);
			}
		}		
	}

}
