package ch.specchio.eav_db;


/**
 * Interface for classes that display the progress of long database operations.
 */
public interface ProgressListener {
	
	
	/**
	 * Called to make the progress report visible or not.
	 * 
	 * @param visible	true if the report should be visible, false otherwise
	 */
	public void setVisible(boolean visible);
	
	
	/**
	 * Called to set the operation to be displayed by the progress report.
	 * 
	 * @param operaton	the operation
	 */
	public void set_operation(String operation);
	
	
	/**
	 * Called to set the progress level displayed by the progress report.
	 * 
	 * @param progress	the progress levelas a percentage
	 */
	public void set_progress(double progress);

}
