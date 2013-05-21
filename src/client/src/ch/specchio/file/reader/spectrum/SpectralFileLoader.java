/* SPECCHIO Project
 * (c) RSL 2006-2011
 * written by ahueni 
 */

package ch.specchio.file.reader.spectrum;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.client.SPECCHIOClientFactory;
import ch.specchio.gui.SPECCHIOApplication;
import ch.specchio.types.SpectralFile;
import ch.specchio.types.attribute;



public abstract class SpectralFileLoader {
	
	FileInputStream file_input = null;
	DataInputStream data_in = null;
	public int file_format_id;
	public String file_format_name;
	private SPECCHIOClient specchio_client;
	Hashtable<String, attribute> attributes_name_hash;
	
	public SpectralFileLoader(String file_format_name)
	{
		this.file_format_name = file_format_name;
//		SPECCHIOClientFactory cf;
		try {
			//cf = SPECCHIOClientFactory.getInstance();
			specchio_client = SPECCHIOApplication.getInstance().getClient();
			attributes_name_hash = specchio_client.getAttributesNameHash();
		} catch (SPECCHIOClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	abstract public SpectralFile load(File file) throws IOException;
	
	// read a number of bytes from the binary file and return them as string
	protected String read_string(DataInputStream in, int no_of_chars) throws IOException
	{		
		byte[] bytes = new byte[no_of_chars];
		in.read(bytes);
		return new String(bytes);
	}
	
	
	public String get_file_format_name()
	{
		return this.file_format_name;
	}
	
	
	public void set_file_format_id(int file_format_id)
	{
		this.file_format_id = file_format_id;
	}
		
	

	protected Integer read_short(DataInputStream in) throws IOException
	{
		byte[] b = new byte[2];
		in.read(b);
		return (new Integer(arr2int(b, 0)));
	}

	protected Integer read_int(DataInputStream in) throws IOException
	{
		byte[] b = new byte[4];
		in.read(b);
		Integer n = arr4int(b, 0); // strange why reading just an integer wont work (uint not existing in Java???)
		return n;
	}

	protected Integer read_uint(DataInputStream in) throws IOException
	{
		byte[] b = new byte[4];
		in.read(b);
		Integer n = (int) arr4uint(b, 0); // strange why reading just an integer wont work (uint not existing in Java???)
		return n;
	}	
	
	protected Integer read_long(DataInputStream in) throws IOException
	{
		byte[] b = new byte[4];
		in.read(b);
		long n = arr2long(b, 0);
		
		int as_int = (int) n;
		
		return as_int;
	}	
	
	protected Float read_float(DataInputStream in) throws IOException
	{
		byte[] b = new byte[4];
		in.read(b);
		return (new Float(arr2float(b, 0)));
	}	
	
	protected Double read_double(DataInputStream in) throws IOException
	{
		byte[] b = new byte[8];
		in.read(b);
		return (new Double(arr2double(b, 0)));
	}		
	
	protected void skip(DataInputStream in, int no_of_bytes)
	{
		try {
			in.skipBytes(no_of_bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
	
	protected void whitespace(BufferedReader d)
	{
		try {
					
			// mark
			d.mark(10);
			
			char c = (char) d.read();
			

			while(c == ' ' || c == '\t')
			{
				d.mark(10);
				c = (char) d.read();			
			}
			
			
			d.reset();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
	
	
	protected String remove_trailing_spaces(String input)
	{
		int start = input.length() -1;
		while(start > 0 && input.substring(start-1, start).equals(" "))
		{
			start--;			
		}
		input = input.substring(0, start);
		
		return input;
	}	
	
	protected String remove_leading_spaces(String input)
	{
		int start = 0;
		while(start+1 < input.length() && (input.substring(start, start+1)).equals(" ")) start++;
		input = input.substring(start);
		
		return input;
	}
	
	protected String remove_trailing_tabs(String input)
	{
		int start = 0;
		while(start+1 < input.length() && (input.substring(start, start+1)).equals("\t")) start++;
		input = input.substring(start);
		
		
		
		return input;
	}	
	
	
	
	/*
	 * binary to double, float, int and short taken from http://www.captain.at/howto-java-convert-binary-data.php
	 */
	
	public static double arr2double (byte[] arr, int start) {
		int i = 0;
		int len = 8;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			//System.out.println(java.lang.Byte.toString(arr[i]) + " " + i);
			cnt++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 64; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return Double.longBitsToDouble(accum);
	}
	
	public static float arr2float (byte[] arr, int start) {
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		int accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return Float.intBitsToFloat(accum);
	}
	
	public static long arr2long (byte[] arr, int start) {
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return accum;
	}
	
	public static int arr2int (byte[] arr, int start) {
		int low = arr[start] & 0xff;
		int high = arr[start+1] & 0xff;
		return (int)( high << 8 | low );
	}
	
	public static int arr4int (byte[] arr, int start) {
		int b1 = arr[start] & 0xff;
		int b2 = arr[start+1] & 0xff;
		int b3 = arr[start+1] & 0xff;
		int b4 = arr[start+1] & 0xff;
		return (int)( b4 << 24 | b3 << 16 | b2 << 8 | b1 );
	}	

	public static long arr4uint (byte[] by, int start) {
		
		
		long value = 0;
		for (int i = 0; i < by.length; i++)
		{
		   value += (by[i] & 0xff) << (8 * i);
		}		
		
		
		return value;
	}		

}

