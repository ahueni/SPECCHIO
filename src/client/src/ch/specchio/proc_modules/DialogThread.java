package ch.specchio.proc_modules;

import javax.swing.JOptionPane;

import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.gui.ModalDialog;
import ch.specchio.gui.SPECCHIOApplication;

public class DialogThread extends Thread{
	
	public ModalDialog md;
	ModuleCallback cd;
	public boolean ret_val;
	public int callback_value;
	
	public DialogThread(ModalDialog md, ModuleCallback cd, int cv)
	{
		this.md = md;
		this.cd = cd;
		callback_value = cv;
	}
	
	public void run()
	{
		ret_val = md.get_user_input();
		if(ret_val == true) {
			try {
				cd.user_data_provided(this);
			}
	  		catch (SPECCHIOClientException ex) {
				JOptionPane.showMessageDialog(
		    			SPECCHIOApplication.getInstance().get_frame(),
		    			ex.getMessage(),
		    			"Error",
		    			JOptionPane.ERROR_MESSAGE
		    		);
		    }
		}
	}

}
