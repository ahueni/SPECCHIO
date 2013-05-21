package ch.specchio.types;


/**
 * Exception class for malformed metaparameters.
 */
public class MetaParameterFormatException extends Exception {

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Construct an exception from a message.
	 * 
	 * @param message	the message
	 */
	public MetaParameterFormatException(String message) {
		
		super(message);
		
	}
	
	
	/**
	 * Construct an exception from a message to be re-thrown.
	 * 
	 * @param ex	the exception to be re-thrown
	 */
	public MetaParameterFormatException(Exception ex) {
		
		super(ex);
		setStackTrace(ex.getStackTrace());
		
	}

}
