package ch.specchio.metadata;


/**
 * The interface to be implemented by classes that listen for changes in metadata fields.
 */
public interface MD_ChangeListener {
	
	
	/**
	 * Respond to the addition of a metadata field.
	 * 
	 * @param field	the field
	 */
	public void metadataFieldAdded(MD_Field field);
	
	/**
	 * Respond to a change in a metadata field.
	 * 
	 * @param field	the field
	 * @param value the new value of the field
	 */
	public void metadataFieldChanged(MD_Field field, Object value);
	
	
	/**
	 * Respond to the removal of a metadata field.
	 * 
	 * @param field	the field
	 */
	public void metadataFieldRemoved(MD_Field field);
	
	
	/**
	 * Respond to the change of an annotation.
	 * 
	 * @param field	the field
	 * @param annotation 
	 */
	public void metadataFieldAnnotationChanged(MD_Field field, String annotation);	

}
