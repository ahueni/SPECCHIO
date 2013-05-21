package ch.specchio.gui;


/**
 * Exception for user interface errors.
 */
public class SPECCHIOUserInterfaceException extends Exception {

	/** serialisation version ID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construct a new exception with a message.
	 * 
	 * @param message	the message
	 */
	public SPECCHIOUserInterfaceException(String message) {
		
		super(message);
		
	}
	
	
	/**
	 * Constructor for re-throwing an exception with a new message.
	 * 
	 * @param ex		the exception to be re-thrown
	 * @param message	the new message
	 */
	public SPECCHIOUserInterfaceException(String message, Exception ex) {
		
		super(message, ex);
		setStackTrace(ex.getStackTrace());
		
	}

}
