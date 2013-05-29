package ch.specchio.gui;

import javax.swing.table.DefaultTableModel;


/**
 * Non-editable table model, based on advice from Stack Overflow.
 * 
 * http://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
 */
public class NonEditableTableModel extends DefaultTableModel {

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Return whether or not a row is editable.
	 * 
	 * @return false
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		
		return false;
		
	}

}
