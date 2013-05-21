package ch.specchio.proc_modules;

import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.constants.SensorElementType;

public class BroadbandFilter extends BandTypeFilter{

	public BroadbandFilter(Frame owner, SPECCHIOClient specchio_client) {
		super(owner, specchio_client);
		ElementToFilter = SensorElementType.BB;
	}

	public JPanel get_info_panel()
	{	
		module_name_label = new JLabel("Broadband filtering");
		super.get_info_panel();

		return info_panel;		
	}


}
