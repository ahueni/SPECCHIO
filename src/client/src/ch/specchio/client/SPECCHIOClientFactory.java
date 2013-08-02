package ch.specchio.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.LinkedList;

public class SPECCHIOClientFactory {
	
	/** singleton instance of the web client factory */
	private static SPECCHIOClientFactory instance = null;
	
	/** the configuration file name */
	private static final File config_file = new File("db_config.txt");
	
	/** the list of known servers */
	private List<SPECCHIOServerDescriptor> apps;
	
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
		System.setProperty("javax.net.ssl.trustStore", "specchio.keystore");
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
		FileWriter w = new FileWriter(config_file, true);
		
		// ask the server descriptor to write an appropriate line
		d.writeAccountConfiguration(w);
		
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
	
	   
	   private void read_config_file() throws FileNotFoundException, IOException, SPECCHIOClientException
	   {
			apps = new LinkedList<SPECCHIOServerDescriptor>();
		
			boolean not_eof = true;
			String line;
		   
			FileInputStream file_input = null;
			DataInputStream data_in = null;
			
			file_input = new FileInputStream (config_file);
				
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
