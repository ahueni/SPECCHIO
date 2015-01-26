package ch.specchio.factories;

import javax.ws.rs.WebApplicationException;

public class SPECCHIOFactoryException extends WebApplicationException {

	/** serialisation version ID */
	private static final long serialVersionUID = 1L;
	
	private String message = "";
	
	/**
	 * Constructor for re-throwing an exception.
	 */
	public SPECCHIOFactoryException(Exception ex) {
		
		super(ex);
		setStackTrace(ex.getStackTrace());
		
	}
	
	
	/**
	 * Constructor for throwing a new exception.
	 * 
	 * @param message	the message to be reported by the exception
	 */
	public SPECCHIOFactoryException(String message) {
		
		super();
		this.message = message;
		
	}

}
