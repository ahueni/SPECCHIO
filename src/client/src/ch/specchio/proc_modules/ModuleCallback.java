package ch.specchio.proc_modules;

import ch.specchio.client.SPECCHIOClientException;


public interface ModuleCallback{
	
	
	public void user_data_provided(DialogThread dt) throws SPECCHIOClientException;
	

}
