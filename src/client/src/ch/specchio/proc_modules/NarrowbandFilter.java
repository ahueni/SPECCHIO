package ch.specchio.proc_modules;

import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.constants.SensorElementType;

public class NarrowbandFilter  extends BandTypeFilter{

	public NarrowbandFilter(Frame owner, SPECCHIOClient specchio_client) {
		super(owner, specchio_client);
		ElementToFilter = SensorElementType.NB;
	}

	public JPanel get_info_panel()
	{
		
		module_name_label = new JLabel("Narrowband filtering");

		super.get_info_panel();

		return info_panel;
		
	}
}
