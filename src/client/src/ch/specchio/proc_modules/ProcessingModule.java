package ch.specchio.proc_modules;

import java.awt.Frame;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jgraph.graph.GraphConstants;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;


public abstract class ProcessingModule extends Module {

	
	public ProcessingModule(Frame owner, SPECCHIOClient specchio_client)
	{
		super(owner, specchio_client);
	}
	
	public JPanel get_info_panel()
	{
		module_type_label = new JLabel("Proc. module: ");
		super.get_info_panel();
		return info_panel;
	}
	
	public void create_output_spaces_and_add_to_processing_plane() throws SPECCHIOClientException
	{
		// insert position on plane
		Rectangle2D r = GraphConstants.getBounds(this.get_ppo().getAttributes());
		Double x_pos = r.getCenterX();
		Double y_pos = r.getMaxY();
		
		int x = x_pos.intValue();
		int y = y_pos.intValue() + 40;
		
		create_output_spaces_and_add_to_processing_plane(x,y);		
	}
	

	
	public void user_data_provided(DialogThread dt) throws SPECCHIOClientException {
		boolean ret_val = dt.ret_val;
		
		
		if(ret_val == true)
		{
			if(dt.callback_value == ProcessingModule.INPUT_SPACE_DEF)
			{
			
				super.user_data_provided(dt);
				create_output_spaces_and_add_to_processing_plane();
	
			}
		}
		
	}
	

}


