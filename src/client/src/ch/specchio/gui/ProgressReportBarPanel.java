package ch.specchio.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressReportBarPanel extends ProgressReportPanel {
	
	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;

	/** progress bar */
	private JProgressBar progressBar;
	
	/** operation description */
	private JLabel operation_value;
	
	/** component description */
	private JLabel component_value;
	
	
	/**
	 * Constructor.
	 */
	public ProgressReportBarPanel(boolean include_component_label, int columns)
	{
		super();
		
		
		// initialise components
		operation_value = new JLabel();
		component_value = null;
	    progressBar = new JProgressBar(0,100);
	    progressBar.setValue(0);
	    progressBar.setStringPainted(true);

		// set up grid bag layout
	    setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		if(include_component_label)
		{
			component_value = new JLabel();
			add(new JLabel("Component:"), constraints);
			constraints.gridx = 1;
			add(component_value, constraints);
		}

		constraints.gridx = 0;
		constraints.gridy++;
		add(new JLabel("Operation:"), constraints);

		constraints.gridx = 1;
		add(operation_value, constraints);
		
		constraints.gridx = 0;
		constraints.gridy++;
		add(new JLabel("Status:"), constraints);

		constraints.gridx = 1;		
		add(progressBar, constraints);	
		
		// work out the preferred size of the operation and component fields
		if (columns > 0) {
			FontMetrics fm = operation_value.getFontMetrics(operation_value.getFont());
			Dimension preferredLabelSize = new Dimension(fm.charWidth('M') * columns, fm.getHeight());
			operation_value.setPreferredSize(preferredLabelSize);
			if (component_value != null) {
				component_value.setPreferredSize(preferredLabelSize);
			}
		}
		
	}
	
	
	public void set_component(String c)
	{
		if (component_value != null) {
			component_value.setText(c);
		}
	}
	
	
	public void set_indeterminate(boolean indeterminate)
	{
		progressBar.setIndeterminate(indeterminate);
		progressBar.setStringPainted(!indeterminate);
	}
	

	public void set_min_max(int min, int max)
	{
		progressBar.setMinimum(min);
		progressBar.setMaximum(max);		
	}
	
	
	public void set_operation(String op)
	{
		operation_value.setText(op);
	}

	
	public boolean set_progress(double value)
	{
		progressBar.setValue((int)value);
		String str = String.format("%.2f%%", value);
		progressBar.setString(str);
		return true;
	}

	
	public boolean set_progress(int value) {
		progressBar.setValue(value);
		return true;
	}

}
