package ch.specchio.gui;

import javax.swing.JOptionPane;

import ch.specchio.metadata.MD_Field;


public class SharedMD_Dialog {
	
	final static int DELETE = 0;
	final static int UPDATE = 1;	
	
	final static int APPLY_TO_ALL = 1;
	final static int APPLY_TO_SELECTION = 2;
	final static int APPLY_TO_NONE = 3;
	
	int decision = APPLY_TO_ALL;
	
	public int cx_shared_operation(int op, 	MD_Field context)
	{

		if(context.getNoOfSharingRecords() > 1)
		{
			String apply_to_shared_record = "Apply to shared record";
			String selection_op = "";
			String cancel_op = "";
			String op_desc = "";

			switch (op)
			{
			case UPDATE :
				selection_op = "Create new record for selected spectra";
				cancel_op = "Cancel update action";
				op_desc = "update";
				break;
			case DELETE :
				selection_op = "Delete record of selected spectra";
				cancel_op = "Cancel delete action";		
				op_desc = "delete";
				break;
			}

			Object[] options;
			int opt_cnt = 0;
			int default_selection;

			if(context.getNoOfSharingRecords() != context.getSelectedRecords())
			{
				options = new Object[3];
				options[opt_cnt++] = selection_op;
				default_selection = 1;
			}
			else
			{
				options = new Object[2];
				default_selection = 0;
			}

			options[opt_cnt++] = apply_to_shared_record;
			options[opt_cnt++] = cancel_op;

			Object selectedValue = JOptionPane.showInputDialog(null,
					"You are about to " + op_desc + " a shared data record for " +
					context.getSelectedRecords() + " of " + context.getNoOfSharingRecords() + " spectra:\n" +
					"Please select one of the following actions:",
					"Shared data operation for attribute " + context.getLabel(),
					JOptionPane.INFORMATION_MESSAGE,
					null,
					options,
					options[default_selection]);


			if(selectedValue == null || selectedValue.equals(cancel_op))
			{
				decision = SharedMD_Dialog.APPLY_TO_NONE;
			}
			else if(selectedValue.equals(selection_op))
			{
				decision = SharedMD_Dialog.APPLY_TO_SELECTION;
			}
			else
				decision = SharedMD_Dialog.APPLY_TO_ALL;

			
		}		
		
		return decision;
	}

}
