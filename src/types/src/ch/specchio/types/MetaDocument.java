package ch.specchio.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * Meta-parameter type for documents.
 */
@XmlRootElement(name="meta_document")
@XmlSeeAlso({PdfDocument.class})
public class MetaDocument extends MetaFile {
	
	/** default constructor */
	protected MetaDocument() {
		
		super();
		
	}
	
	
	/** constructor from an attribute definition */
	protected MetaDocument(attribute attr) {
		
		super(attr);
		
	}
	
	
	/** constructor from a category name and value */
	protected MetaDocument(String category_name, String category_value, Object meta_value) {
		
		super(category_name, category_value, meta_value);
		
	}
	

	/** returns "true" to indicate that documents can be inserted monolithic statements */
	@Override
	public boolean allows_multi_insert() {
		
		return true;
		
	}
	
	
	/** returns ".pdf" since this is the only type of file we support */
	@Override
	public String getDefaultFilenameExtension() {
		
		return ".pdf";
		
	}
	
	
	/** read the meta-parameter value from an input stream */
	@Override
	public void readValue(InputStream is, String mimeType) throws IOException
	{
		PdfDocument pdf = new PdfDocument();
		pdf.readDocument(is);
		setValue(pdf);
	}
	
	
	/** initialise the meta-parameter with an empty value */
	@Override
	public void setEmptyValue() {
		
		setValue(new PdfDocument());
		
	}
	
	
	public static boolean supportsValue(Object value)
	{
		return value != null && value instanceof PdfDocument;	
	}
	

	@Override
	public String valueAsString() {
		
		return getValue().toString();
		
	}
	
	
	/** write the document to an output stream */
	@Override
	public void writeValue(OutputStream os) throws IOException
	{
		PdfDocument pdf = (PdfDocument)getValue();
		pdf.writeDocument(os);
	}

}
