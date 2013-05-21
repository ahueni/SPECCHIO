package ch.specchio.client;


public class SPECCHIOClientException extends Exception {

	/** serialisation version ID */
	private static final long serialVersionUID = 1L;
	
	/** the ultimate cause of the exception */
	protected Throwable cause;
	
	
	/**
	 * Constructor for re-throwing an exception.
	 */
	public SPECCHIOClientException(Exception ex) {
		
		super(ex);
		
		cause = ex.getCause();
		setStackTrace(ex.getStackTrace());
		
	}
	
	
	/**
	 * Constructor for re-throwing an exception with a new detail message.
	 */
	public SPECCHIOClientException(String message, Exception ex) {
		
		super(message, ex);
		
		cause = ex.getCause();
		setStackTrace(ex.getStackTrace());
		
	}
	
	
	/**
	 * Constructor for throwing a new exception.
	 * 
	 * @param message	the message to be reported by the exception
	 */
	public SPECCHIOClientException(String message) {
		
		super(message);
		
	}
	
	
	/**
	 * Get an error message suitable for displaying to the user.
	 *
	 * @return a brief user-readable error message describing the error
	 */
	public String getUserMessage() {
		
		return getMessage();
		
	}

}
