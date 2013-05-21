package ch.specchio.plots.swing;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOWebClientException;
import ch.specchio.gui.GridbagLayouter;
import ch.specchio.plots.GonioPosition;
import ch.specchio.plots.GonioSamplingPoints;
import ch.specchio.spaces.SpectralSpace;
import ch.specchio.types.MatlabAdaptedArrayList;

public class GonioAnglePointInfoPanel extends JPanel{
		
	private static final long serialVersionUID = 1L;
	
	GonioSamplingPoints points;

	SpectralSpace space;
	JTextField filename, azimuth, zenith;
	SPECCHIOClient specchio_client;
	
	public GonioAnglePointInfoPanel(SpectralSpace space, GonioSamplingPoints points, SPECCHIOClient specchio_client)
	{
		this.points = points;
		this.space = space;
		this.specchio_client = specchio_client;
		setup_panel();
		set_context(space.getSpectrumIds().get(0));
	}
	
	// spectrum number
	public void set_context(int spectrum_id)
	{
		int index = points.get_index(spectrum_id);
		GonioPosition gp = points.get_positions()[index];
		
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(2);
		
		azimuth.setText(f.format(gp.azimuth));
		zenith.setText(f.format(gp.zenith));
		
		// get filename
		try {
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			tmp.add(spectrum_id);
			
			
			MatlabAdaptedArrayList<Object> filenames = specchio_client.getMetaparameterValues(tmp, "File Name");
			
			if(filenames.size() > 0) filename.setText(filenames.get(0).toString());

			
		} catch (SPECCHIOWebClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	void setup_panel()
	{
		int field_length = 8;
		GridbagLayouter panel_l = new GridbagLayouter(this);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;	
		
		JLabel label = new JLabel("Sampling Point Information");
		label.setFont(new Font("SansSerif", Font.BOLD,12));
		constraints.gridx = 0;
		constraints.gridy = 0;	
		constraints.gridwidth = 2;
		panel_l.insertComponent(label, constraints);
		
		constraints.gridwidth = 1;
		
		label = new JLabel("Filename:", JLabel.LEFT);
		filename = new JTextField("", field_length);
		filename.setEditable(false);
		constraints.gridx = 0;
		constraints.gridy++;		
		panel_l.insertComponent(label, constraints);
		constraints.gridx++;
		panel_l.insertComponent(filename, constraints);
		
		label = new JLabel("Azimuth:", JLabel.LEFT);
		azimuth = new JTextField("", field_length);
		azimuth.setEditable(false);
		constraints.gridx = 0;
		constraints.gridy++;		
		panel_l.insertComponent(label, constraints);
		constraints.gridx++;
		panel_l.insertComponent(azimuth, constraints);
		
		
		label = new JLabel("Zenith:", JLabel.LEFT);
		zenith = new JTextField("", field_length);
		zenith.setEditable(false);
		constraints.gridx = 0;
		constraints.gridy++;		
		panel_l.insertComponent(label, constraints);
		constraints.gridx++;
		panel_l.insertComponent(zenith, constraints);
		
	}
	

}

