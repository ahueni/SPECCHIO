package ch.specchio.plots;

/**
 * Exception class for problems with plotting data.
 */
public class SPECCHIOPlotException extends Exception {
	
	/** serialisation version identifer */
	private static final long serialVersionUID = 1L;


	/**
	 * Constructor for throwing a new exception with a message.
	 * 
	 * @param message	the message
	 */
	public SPECCHIOPlotException(String message) {
		
		super(message);
		
	}
	
	
	/**
	 * Constructor for re-throwing an exception.
	 * 
	 * @param ex	the original exception
	 */
	public SPECCHIOPlotException(Exception ex) {
		
		super(ex);
		setStackTrace(ex.getStackTrace());
		
	}
	
	
	/**
	 * Constructor for re-throwing an exception with a new message.
	 * 
	 * @param message	the message
	 * @param ex		the original exception
	 */
	public SPECCHIOPlotException(String message, Exception ex) {
		
		super(message, ex);
		setStackTrace(ex.getStackTrace());
		
	}

}
