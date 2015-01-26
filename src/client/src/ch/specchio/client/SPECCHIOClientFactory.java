package ch.specchio.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;
import java.util.LinkedList;

import ch.specchio.gui.SPECCHIOApplication;

public class SPECCHIOClientFactory {
	
	/** singleton instance of the web client factory */
	private static SPECCHIOClientFactory instance = null;
	
	/** the configuration file name */
	private static final String config_file = "db_config.txt";
	
	/** the list of known servers */
	private List<SPECCHIOServerDescriptor> apps;
	
	private boolean reload_db_config = false;

	private long config_file_last_modified;
	
	/**
	 * Constructor.
	 * 
	 * @throws SPECCHIOClientException	the configuration file is invalid
	 */
	private SPECCHIOClientFactory() throws SPECCHIOClientException {
		
		// read the configuration file
		try {
			read_config_file();
		}
		catch (FileNotFoundException ex) {
			throw new SPECCHIOClientException(ex);
		}
		catch (IOException ex) {
			throw new SPECCHIOClientException(ex);
		}
		
		// set up SSL trust store
		System.setProperty("javax.net.ssl.trustStore", getApplicationConfFilename("specchio.keystore"));
		System.setProperty("javax.net.ssl.trustStorePassword", "specchio");
		
	}
	
	
	/**
	 * Add the configuration for an account to the configuration file.
	 * 
	 * @param d		the descriptor of the server on which the new account exists
	 * 
	 * @throws IOException	file error
	 */
	public void addAccountConfiguration(SPECCHIOServerDescriptor d) throws IOException {
			
		// open the configuration file
		FileWriter w = new FileWriter(getApplicationConfFilename(config_file), true);
		
		// start a new line
		w.write("\n");
		
		// write out the account configuration line
		w.write(d.getAccountConfigurationString());
		
		// close file
		w.close();
		
		// add the server descriptor to the list of known descriptors
		apps.add(d);
		
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
		
		return new SPECCHIOClientCache(app.createClient());
	
	}
	
	public static String getDBConfigFilename()
	{
		return getApplicationConfFilename(config_file);
	}
	
	public void reloadDBConfigFile()
	{
		reload_db_config = true;
	}
	
	
	public static String getApplicationConfFilename(String name)
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
	
//	public void readDBConfigFile() throws FileNotFoundException, IOException, SPECCHIOClientException
//	{
//		instance.read_config_file();
//	}
	
	
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
	 * @throws SPECCHIOClientException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public List<SPECCHIOServerDescriptor> getAllServerDescriptors() throws FileNotFoundException, IOException, SPECCHIOClientException {
		
		File temp = new File(SPECCHIOClientFactory.getDBConfigFilename());		
		
		if (config_file_last_modified != temp.lastModified())
			reload_db_config = true;
		
		if (reload_db_config)
		{
			instance.read_config_file();
			reload_db_config = false;
		}
		
		return apps;
		
	}
	
	   
	   private void read_config_file() throws FileNotFoundException, IOException, SPECCHIOClientException
	   {
			apps = new LinkedList<SPECCHIOServerDescriptor>();
		
			boolean not_eof = true;
			String line;
		   
			FileInputStream file_input = null;
			DataInputStream data_in = null;
						
			File temp = new File(SPECCHIOClientFactory.getDBConfigFilename());
			this.config_file_last_modified = temp.lastModified();
			
			file_input = new FileInputStream (getApplicationConfFilename(config_file));
				
			data_in = new DataInputStream(file_input);			
				
			BufferedReader d = new BufferedReader(new InputStreamReader(data_in));

			while(not_eof)
			{
				not_eof = skip_comments(d);
					
				if(not_eof)
				{
					line = d.readLine();	
						
					// process line and fill it into the connection details list
					process_line(line);	
				}
			}
				
			d.close();						
			data_in.close ();
	   
	   }
	   
	   private boolean skip_comments(BufferedReader d) throws IOException
	   {
		
		   int ret;
	   	    d.mark(150);
	   	    ret = d.read();

			while(ret != -1 && ((char)ret) == '#')
			{
				d.readLine(); // read whole line
				d.mark(150);
				ret = d.read(); // read next char (new line)
			}
			
			// return to last mark (start of the next valid line)
			d.reset();	 
			
			if(ret == -1)
				return false; // eof
			else
				return true; // not eof
	   }
	   
	   private void process_line(String line) throws SPECCHIOClientException
	   {
		   if(line.length() > 0)
		   {
			   
			   SPECCHIOServerDescriptor app = null;
			   
				// tokenise the line
				String[] tokens = line.split("\\s*,\\s*");
				
				// create server descriptor depending on the first token
				if (tokens[0].equalsIgnoreCase("https") || tokens[0].equalsIgnoreCase("http")) {
					// web service
					app = new SPECCHIOWebAppDescriptor(tokens);
				} else if (tokens[0].equalsIgnoreCase("sql")) {
					// direct database connection
					// TODO
		   		} else if (tokens[0].equalsIgnoreCase("sql+ssh")) {
		   			// database via SSH
		   			// TODO
		   		} else {
		   			throw new SPECCHIOClientException("Unrecognised connection protocol in configuration file: " + tokens[0]);
		   		}
				
				// add the new descriptor to the list
				this.apps.add(app);
		   }
	   }

}
