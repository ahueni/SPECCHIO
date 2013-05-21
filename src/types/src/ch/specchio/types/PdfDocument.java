package ch.specchio.types;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.xml.bind.annotation.*;


/**
 * This class represents a PDF document.
 */
@XmlRootElement(name="pdf_docment")
public class PdfDocument implements Serializable {
	
	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	/** the document bytes */
	private byte[] bytes;
	
	
	/** default constructor */
	public PdfDocument() { this.bytes = new byte[0]; };
	
	
	@XmlElement(name="bytes")
	public byte[] getBytes() { return this.bytes; }
	public void setBytes(byte[] bytes) { this.bytes = bytes; }
	
	
	/**
	 * Get the size of the document.
	 * 
	 * @return the size of the document in bytes
	 */
	public int getSize() {
		
		return bytes.length;
		
	}
	
	
	/**
	 * Read a document from an input stream.
	 * 
	 * @param is	the input stream
	 * 
	 * @throws IOException	read error
	 */
	public void readDocument(InputStream is) throws IOException {
		
		// copy the input stream into an in-memory stream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte b[] = new byte[1024];
		int n = is.read(b);
		while (n != -1) {
			baos.write(b, 0, n);
			n = is.read(b);
		}
		baos.close();
		
		// convert the output stream into a byte array
		bytes = baos.toByteArray();
		
	}
	
	
	/**
	 * Write a document to an output stream.
	 * 
	 * @param os	the output stream
	 * 
	 * @throws IOException	write error
	 */
	public void writeDocument(OutputStream os) throws IOException {
		
		os.write(bytes);
		
	}

}
