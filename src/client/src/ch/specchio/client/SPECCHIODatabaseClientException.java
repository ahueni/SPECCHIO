package ch.specchio.client;

public class SPECCHIODatabaseClientException extends SPECCHIOClientException {

	/** serialisation version ID */
	private static final long serialVersionUID = 1L;
	
	/** the user message */
	private final String userMessage;
	
	
	/**
	 * Constructor for throwing a new exception.
	 * 
	 * @param message	the message to be reported by the exception
	 */
	public SPECCHIODatabaseClientException(String message) {
		
		super(message);
		
		userMessage = message;
		
	}
	
	
	/**
	 * Get an error message suitable for displaying to the user.
	 *
	 * @return a brief user-readable error message describing the error
	 */
	public String getUserMessage() {
		
		return (userMessage != null)? userMessage : super.getUserMessage();
		
	}

}
