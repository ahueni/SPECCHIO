package ch.specchio.gui;

import javax.swing.table.DefaultTableModel;

public class EditableTableModel extends DefaultTableModel {
	
	private int editDisabledCell = -1;

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;

	/**
	 * Return whether or not a row is editable.
	 * 
	 * @return false
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		
		if(column != editDisabledCell)
			return true;
		else
			return false;
		
	}
	
	public void setValueAt(Object value, int row, int col) {
		super.setValueAt(value, row, col);
//        fireTableCellUpdated(row, col);
    }

	public int getEditDisabledCell() {
		return editDisabledCell;
	}

	public void setEditDisabledCell(int editDisabledCell) {
		this.editDisabledCell = editDisabledCell;
	}	
	
	
	

}
