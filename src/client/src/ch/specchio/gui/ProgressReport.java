package ch.specchio.gui;


import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class ProgressReport implements ProgressReportInterface
{

	private static final long serialVersionUID = 1L;
	
	JProgressBar progressBar;
	JLabel operation_value, component_value;
	boolean update = false;
	JFrame frame;
	int value;
	boolean include_component_label;
	
	public ProgressReport(String title, boolean include_component_label)
	{
		this.include_component_label = include_component_label;
		create_frame(title);		
	}
	
	public JFrame get_frame()
	{
		return frame;
	}
	
	
	void create_frame(String title)
	{
		frame = new JFrame(title + " Progress");
		JPanel panel = new JPanel();
		
		//panel.setPreferredSize(preferredSize);
		
		operation_value = new JLabel();
		component_value = new JLabel();
	    progressBar = new JProgressBar(0,100);
	    progressBar.setValue(0);
	    progressBar.setStringPainted(true);

		
		GridbagLayouter panel_l = new GridbagLayouter(panel);
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.ipadx = 15;
		constraints.ipady = 15;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		if(include_component_label)
		{
			panel_l.insertComponent(new JLabel("Component:"), constraints);
	
			constraints.gridx = 1;
			panel_l.insertComponent(component_value, constraints);
		}

		constraints.gridx = 0;
		constraints.gridy++;
		panel_l.insertComponent(new JLabel("Operation:"), constraints);

		constraints.gridx = 1;
		panel_l.insertComponent(operation_value, constraints);
		
		constraints.gridx = 0;
		constraints.gridy++;
		panel_l.insertComponent(new JLabel("Status:"), constraints);

		constraints.gridx = 1;		
		panel_l.insertComponent(progressBar, constraints);	
		
		frame.getContentPane().add(panel);
		
		frame.pack();	
		
		//frame.setVisible(true);
	}
	
	public void setVisible(boolean b)
	{
		if(frame != null)
			frame.setVisible(b);
	}
	
	
	public void set_component(String comp)
	{			
		this.component_value.setText(comp);
	}
	
	
	public void set_operation(String op)
	{
		this.operation_value.setText(op);
		frame.pack();
		frame.invalidate();
	}
	
	
	public String get_operation()
	{
		return operation_value.getText();
	}
	
	public void set_min_max(int min, int max) {
		progressBar.setMinimum(min);
		progressBar.setMaximum(max);		
	}

	
	public void set_progress(Double value)
	{
		this.progressBar.setValue(value.intValue());
		String str = String.format("%.2f%%", value);
		this.progressBar.setString(str);
	}

	public boolean set_progress(int value) {
		this.progressBar.setValue(value);	
		return true;
	}

}
