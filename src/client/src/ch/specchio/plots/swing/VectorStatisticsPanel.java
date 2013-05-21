package ch.specchio.plots.swing;


import java.awt.Font;
import java.awt.GridBagConstraints;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.specchio.gui.GridbagLayouter;
import ch.specchio.plots.VectorStatistics;
import ch.specchio.spaces.SpectralSpace;

public class VectorStatisticsPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	SpectralSpace space;
	VectorStatistics stats = new VectorStatistics();
	
	JTextField mean, min, max, stddev;
	
	public VectorStatisticsPanel(SpectralSpace space)
	{
		this.space = space;
		setup_panel();
		set_context(space.getSpectrumIds().get(0));
	}
	
	// spectrum number, start and end band of region to calculate
	public void set_context(int spectrum_id,  int start_ind, int end_ind)
	{		
		stats.calc_stats(space.getVector(spectrum_id), start_ind, end_ind);	
		
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(5);
		
		mean.setText(f.format(stats.mean()));
		min.setText(f.format(stats.min()));
		max.setText(f.format(stats.max()));
		stddev.setText(f.format(stats.standardDeviation()));
		
	}
	
	public void set_context(int spectrum_id)
	{
		set_context(spectrum_id, 0, space.getDimensionality()-1);
	}
	
	
	void setup_panel()
	{
		int field_length = 8;
		GridbagLayouter panel_l = new GridbagLayouter(this);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;	
		
		JLabel label = new JLabel("Spectrum Statistics");
		label.setFont(new Font("SansSerif", Font.BOLD,12));
		constraints.gridx = 0;
		constraints.gridy = 0;	
		constraints.gridwidth = 2;
		panel_l.insertComponent(label, constraints);
		
		constraints.gridwidth = 1;
		
		label = new JLabel("Mean:", JLabel.LEFT);
		mean = new JTextField("", field_length);
		mean.setEditable(false);
		constraints.gridx = 0;
		constraints.gridy++;		
		panel_l.insertComponent(label, constraints);
		constraints.gridx++;
		panel_l.insertComponent(mean, constraints);
		
		
		label = new JLabel("Min:", JLabel.LEFT);
		min = new JTextField("", field_length);
		min.setEditable(false);
		constraints.gridx = 0;
		constraints.gridy++;		
		panel_l.insertComponent(label, constraints);
		constraints.gridx++;
		panel_l.insertComponent(min, constraints);
		
		label = new JLabel("Max:", JLabel.LEFT);
		max = new JTextField("", field_length);
		max.setEditable(false);
		constraints.gridx = 0;
		constraints.gridy++;		
		panel_l.insertComponent(label, constraints);
		constraints.gridx++;
		panel_l.insertComponent(max, constraints);
		
		label = new JLabel("Stddev:", JLabel.LEFT);
		stddev = new JTextField("", field_length);
		stddev.setEditable(false);
		constraints.gridx = 0;
		constraints.gridy++;		
		panel_l.insertComponent(label, constraints);
		constraints.gridx++;
		panel_l.insertComponent(stddev, constraints);
		
	}
	

}
