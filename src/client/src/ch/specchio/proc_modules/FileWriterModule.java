package ch.specchio.proc_modules;

import java.awt.Frame;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.constants.FileTypes;
import ch.specchio.constants.SpaceTypes;
import ch.specchio.gui.FileOutputDialog;
import ch.specchio.spaces.MeasurementUnit;


public class FileWriterModule extends Module{

	String target_dir;
	String base_name;
	int file_type;
	boolean split_hdr_and_body;
	boolean configured = false;
	int time_format;
	
	String fw_module_type = "No writer selected yet.";
	String settings_str = "";
	
	
	public FileWriterModule(Frame owner, SPECCHIOClient specchio_client) {
		super(owner, specchio_client);
	}

	@Override
	public void configure() {
		
		// bring up the config window
		DialogThread dt = new DialogThread(new FileOutputDialog(get_coords_for_popups()), this, Module.CONFIG);
		dt.start();
	}
	
	// return a substring of length len or shorter depending on if the character c was found. if not found with the len characters
	// from the end, the substring with len chars is returned
	private static String intelligent_substr(String input, char c, int len)
	{
		int ind = input.length() - 1;
		int last_char_ind = input.length();
		
		while(ind > input.length() - len)
		{
			if(input.charAt(ind) == c)
				last_char_ind = ind;
			
			ind--;
		}
		
		int ret_ind = last_char_ind;
		
		if(ret_ind == input.length())
			ret_ind = ind;		 
		
		return input.substring(ret_ind);
		
	}
	
	public void user_data_provided(DialogThread dt) throws SPECCHIOClientException {
		boolean ret_val = dt.ret_val;
		
		super.user_data_provided(dt);
		
		if(ret_val == true)
		{
			if(dt.callback_value == Module.CONFIG)
			{				
				base_name = ((FileOutputDialog)dt.md).base_name.getText();
				target_dir = ((FileOutputDialog)dt.md).target_dir.getText();
				file_type = ((FileOutputDialog)dt.md).file_type;
				split_hdr_and_body = ((FileOutputDialog)dt.md).split_header_and_body.isSelected();
				time_format = ((FileOutputDialog)dt.md).get_time_format();
				configured = true;
				
				fw_module_type = FileTypes.descr_strs[file_type] + " Writer";
				
								
				String target_dir_short = "(...)" + intelligent_substr(target_dir, File.separatorChar, 20);
				
				this.settings_str = target_dir_short;
				
				configured = true;

				processing_plane.redraw_object(ppo);
			}
		}
		
		
		
	}
	
	public JPanel get_info_panel()
	{
		module_type_label = new JLabel("File Exp. module:");
		module_name_label = new JLabel(fw_module_type);
		this.settings = new JLabel(settings_str);
		
		super.get_info_panel();

		return info_panel;
		
	}


	@Override
	public MeasurementUnit get_measurement_unit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int get_output_space_type() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] get_output_space_vector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int get_required_input_space_type() {
		return SpaceTypes.SpectralSpace;
	}

	@Override
	public void transform() {
		
		// create new output manager and init with settings
		// only if properly configured!
		if(configured)
		{
			FileOutputManager fom = new FileOutputManager(specchio_client, input_spaces, target_dir, base_name, file_type, split_hdr_and_body, time_format);
			fom.set_progress_report(this);
			fom.start();
			
			// wait for fom to end
			fom.done();
			
		}

	}
	
	

}
