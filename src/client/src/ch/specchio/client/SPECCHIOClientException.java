package ch.specchio.client;


public class SPECCHIOClientException extends Exception {

	/** serialisation version ID */
	private static final long serialVersionUID = 1L;
	
	/** the detailed message */
	protected String details;
	
	/** the ultimate cause of the exception */
	protected Throwable cause;
	
	
	/**
	 * Constructor for re-throwing an exception with a new detail message.
	 */
	public SPECCHIOClientException(String message, Exception ex) {
		
		super(message, ex);
		
		// initialise member variables
		init(ex);
		
	}
	
	
	/**
	 * Constructor for re-throwing an exception.
	 */
	public SPECCHIOClientException(Exception ex) {
		
		super(ex);
		
		// initialise member variables
		init(ex);
		
	}
	
	
	/**
	 * Constructor for throwing a new exception.
	 * 
	 * @param message	the message to be reported by the exception
	 */
	public SPECCHIOClientException(String message) {
		
		super(message);
		
		// initialise member variables
		init(null);
		
	}
	
	
	/**
	 * Initialise member variables.
	 * 
	 * @param ex	the cause
	 */
	private void init(Exception ex) {
		
		// set the cause
		if (ex != null) {
			cause = ex.getCause();
			setStackTrace(ex.getStackTrace());
		} else {
			cause = null;
		}
		
		// put the stack trace into the detailed message
		StringBuffer sbuf = new StringBuffer();
		if (ex != null) {
			sbuf.append(ex.getMessage());
		} else if (cause != null) {
			sbuf.append(cause.getMessage());
		} else {
			sbuf.append(getMessage());
		}
		for (StackTraceElement elem : getStackTrace()) {
			sbuf.append("\n  " + elem.toString());
		}
		details = sbuf.toString();
		
	}
	
	
	/**
	 * Get a detailed error message.
	 * 
	 * @return the stack trace
	 */
	public String getDetails() {
		
		return details;
		
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
