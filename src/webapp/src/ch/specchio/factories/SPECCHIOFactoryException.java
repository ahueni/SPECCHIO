package ch.specchio.factories;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
		this.setMessage(ex.getMessage());
	}
	
	
	/**
	 * Constructor for throwing a new exception.
	 * 
	 * @param message	the message to be reported by the exception
	 */
	public SPECCHIOFactoryException(String message) {
		
		super();
		this.setMessage(message);
		
	}

	
	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	
    public SPECCHIOFactoryException() {
    }

    public SPECCHIOFactoryException(int status) {
        super(status);
    }

    public SPECCHIOFactoryException(Response response) {
        super(response);
    }

    public SPECCHIOFactoryException(Status status) {
        super(status);
    }

    public SPECCHIOFactoryException(String message, Response response) {
        super(message, response);
    }

    public SPECCHIOFactoryException(int status, String message) {
        super(message, Response.status(Response.Status.BAD_REQUEST).entity(message).build());
    }

    public SPECCHIOFactoryException(Status status, String message) {
        this(status.getStatusCode(), message);
    }

//    public SPECCHIOFactoryException(String message) {
//        this(500, message);
//    }	
	
}
