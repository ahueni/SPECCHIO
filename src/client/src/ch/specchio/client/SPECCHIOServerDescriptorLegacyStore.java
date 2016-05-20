package ch.specchio.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * This class represents database connection details stored in a text file. This
 * method was used for all versions of SPECCHIO prior to V3.0.2.
 * 
 * Database connections are stored one per line.
 * Order of fields: protocol, server, port, path, user_name, password
 * Note: apart from the server name, any of the fields can be left blank
 *
 * Examples:
 *  https, www.specchio.ch, 8181, /specchio, your_user_name, your_password
 *  https, www.specchio.ch, 8181, /specchio, , 
 *  https, www.specchio.ch, 8181, /specchio_test, , 
 */
public class SPECCHIOServerDescriptorLegacyStore extends SPECCHIOServerDescriptorStore {
	
	/** the file */
	private File configFile;
	
	/** the list of server descriptors in the file */
	private Collection<SPECCHIOServerDescriptor> descriptors;
	
	
	/**
	 * Constructor.
	 * 
	 * @param file	the file from which to read the server descriptors
	 * 
	 * @throws IOException file error
	 * @throws SPECCIOClientException invalid data found in the file
	 */
	public SPECCHIOServerDescriptorLegacyStore(File file) throws IOException, SPECCHIOClientException
	{
		configFile = new File(file.toString());
		read_config_file(configFile);
	}
	
	
	/**
	 * Add the configuration for an account to the configuration file.
	 * 
	 * @param d		the descriptor of the server on which the new account exists
	 * 
	 * @throws IOException	file error
	 */
	public void addServerDescriptor(SPECCHIOServerDescriptor d) throws IOException {
		
		// open the configuration file
		FileWriter w = new FileWriter(configFile, true);
		
		// start a new line
		w.write("\n");
		
		// write out the account configuration line
		if (d instanceof SPECCHIOWebAppDescriptor) {
			SPECCHIOWebAppDescriptorFactory f = new SPECCHIOWebAppDescriptorFactory();
			w.write(f.buildLine((SPECCHIOWebAppDescriptor)d));
			w.write("\n");
		}
		
		// close file
		w.close();
		
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
	 * Read the configuration file.
	 * 
	 * @param file	the file
	 * 
	 * @throws FileNotFoundException file does not exist
	 * @throws IOException read error
	 * @throws SPECCHIOClientException incorrect file format
	 */
	private void read_config_file(File file) throws FileNotFoundException, IOException, SPECCHIOClientException
	{

		descriptors = new LinkedList<SPECCHIOServerDescriptor>();
		
		boolean not_eof = true;
		String line;

		FileInputStream file_input = new FileInputStream (file);
		DataInputStream data_in = new DataInputStream(file_input);			
	
		BufferedReader d = new BufferedReader(new InputStreamReader(data_in));
		int line_no = 0;

		while(not_eof)
		{
			not_eof = skip_comments(d);
				
			if(not_eof)
			{
				line = d.readLine();
				line_no++;
					
				// process line and fill it into the connection details list
				process_line(line, line_no);	
			}
		}
			
		d.close();						
		data_in.close ();
	   
	}
	
	
	/**
	 * Skip comments.
	 * 
	 * @param d	the reader
	 * 
	 * @throws IOException read error
	 * 
	 * @return true if not end-of-file, false otherwise
	 */
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
	  
	/**
	 * Parse one line of the configuration file.
	 * 
	 * @param line	the line
	 * @param line_no	line number in input file
	 * 
	 * @throws SPECCHIOClientException parse error
	 */
	private void process_line(String line, int line_no) throws SPECCHIOClientException
	{
		if(line.length() > 0)
		{
			   
			System.out.println(line);
		   SPECCHIOServerDescriptor app = null;
		   
			// tokenise the line
			String[] tokens = line.split("\\s*,\\s*");
			
			// create server descriptor depending on the first token
			if (tokens[0].equalsIgnoreCase("https") || tokens[0].equalsIgnoreCase("http")) {
				// web service
				SPECCHIOWebAppDescriptorFactory f = new SPECCHIOWebAppDescriptorFactory();
				app = f.buildDescriptor(tokens, line_no);
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
			this.descriptors.add(app);
		}
	}


	/**
	 * A configuration line representing a SPECCHIO web application.
	 */
	private class SPECCHIOWebAppDescriptorFactory {
		
		
		/**
		 * Build a web application descriptor for SPECCHIOClientFactory.process_line().
		 * 
		 * @param tokens	the array of strings read from the input file
		 * @param line_no	line number in input file
		 * 
		 * @throws SPECCHIOWebClientException	the tokens are not correctly formatted
		 */
		public SPECCHIOWebAppDescriptor buildDescriptor(String tokens[], int line_no) throws SPECCHIOWebClientException {
			
			// check that we have the correct number of tokens
			if (tokens.length < 7) {
				
				StringBuilder builder = new StringBuilder();
				for(String s : tokens) {
				    builder.append(s + ",");
				}
				
				
				throw new SPECCHIOWebClientException("Insufficient configuration information provided for a SPECCHIO web application server: \n" + 
				"Please correct line number " + line_no + ":\n " + builder.toString());
			}
			
			try {
				String protocol = tokens[0];
				String server = tokens[1];
				int port = Integer.parseInt(tokens[2]);
				String path = tokens[3];
				String user = tokens[4];
				String password = tokens[5];
				String dataSourceName = tokens[6];
				
				return new SPECCHIOWebAppDescriptor(protocol, server, port, path, user, password, dataSourceName);
			}
			catch (NumberFormatException ex) {
				// invalid port number
				throw new SPECCHIOWebClientException("Invalid port number provided for a SPECCHIO web application server.");
			}
			
		}
		
		/**
		 * Create a line representing a SPECCHIO web application.
		 * 
		 * The format is: protocol, server, port, path, username, password
		 * 
		 * @param d	the descriptor
		 * 
		 * @return a string containing the line, without any line ender
		 */
		public String buildLine(SPECCHIOWebAppDescriptor d) {

			return
				d.getProtocol() + ", " +
				d.getServer() + ", " +
				d.getPort() + ", " +
				d.getPath() + ", " +
				d.getDisplayUser() + ", " +
				d.getPassword() + ", " +
				d.getDataSourceName();
			
		}
		
	}
	
}
