package ch.specchio.proc_modules;

/**
 * Exception class for problems with modules.
 */
public class ModuleException extends Exception {

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;


	/**
	 * Constructor for throwing a new exception with a message.
	 * 
	 * @param message	the message
	 */
	public ModuleException(String message) {
		
		super(message);
		
	}
	
	
	/**
	 * Constructor for re-throwing an exception.
	 * 
	 * @param ex	the original exception
	 */
	public ModuleException(Exception ex) {
		
		super(ex);
		setStackTrace(ex.getStackTrace());
		
	}

}
