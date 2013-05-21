package ch.specchio.constants;


/**
 * File type identifiers.
 */
public class FileTypes
{
	/** comma-separted value */
	public final static int CSV = 0;
	
	/** ENVI SLB */
	public final static int ENVI_SLB = 1;
	
	/** XML-formatteed campaign export */
	public final static int CAMPAIGN_EXPORT_XML = 2;
	
	/** file type names */
	public static String[] descr_strs = {"CSV", "ENVI SLB", "XML"};
	
	
	/**
	 * Get the filename extension for a given file type.
	 * 
	 * @param file_type		a FileTypes.* constant
	 * @param hdr_or_body	HeaderBody.Header or HeaderBody.Body
	 *
	 * @return the filename extension for the desired file type
	 */
	public static String get_filename_extension(int file_type, int hdr_or_body)
	{
		// get the filename extension for a combination of file type and contents
		switch (file_type)
		{
			case FileTypes.CSV:
				if (hdr_or_body == HeaderBody.Header) {
					return "_HDR.csv";
				} else if (hdr_or_body == HeaderBody.Body) {
					return "_BODY.csv";
				} else {
					return ".csv";
				}
			
			case FileTypes.ENVI_SLB:
				return (hdr_or_body == HeaderBody.Header)? ".hdr" : ".slb";
				
			case FileTypes.CAMPAIGN_EXPORT_XML:
				return ".xml";		
		}
		
		return "";
	}
	
}
