package ch.specchio.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;


/**
 * A panel that represent progress with a text counter.
 */
public class ProgressReportTextPanel extends ProgressReportPanel {
	
	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;

	/** operation description */
	private JLabel operation_desc;
	
	/** current operation */
	private JLabel curr_op;
	
	/** description of the progress counter */
	private JLabel number_desc;
	
	/** the progress counter */
	private JLabel number;
	
	/** current progress value */
	private Number progress;
	
	
	public ProgressReportTextPanel(String op_desc_string, String number_desc_string) 
	{
		super();
		
		// initialise variables
		progress = null;
		
		// set surrounding border
		setBorder(BorderFactory.createLineBorder(Color.black));
				
		// set up grid bag layout
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		
		operation_desc = new JLabel(op_desc_string);
		add(operation_desc, constraints);
		
		constraints.gridy = 1;
		curr_op = new JLabel("");
		add(curr_op, constraints);		
		
		constraints.gridwidth = 1;
		constraints.gridy = 2;
		number_desc = new JLabel(number_desc_string);
		add(number_desc, constraints);		
		
		constraints.gridx = 1;
		number = new JLabel("");
		add(number, constraints);			
		
	}
	
	
	public void set_component(String component_string)
	{
		number_desc.setText(component_string);
	}
	
	
	public void set_indeterminate(boolean indeterminate)
	{
		if (indeterminate) {
			number.setText("Unknown");
		} else if (number != null) {
			number.setText(progress.toString());
		} else {
			number.setText("");
		}
	}
	
	
	public void set_min_max(int min, int max)
	{
		// ignore
	}
	
	public void set_operation_description(String op_string)
	{	
		operation_desc.setText(op_string);	
	}
	
	
	public void set_operation(String curr_op_string)
	{	
		curr_op.setText(curr_op_string);	
	}
	
	public boolean set_progress(double n)
	{
		progress = new Double(n);
		number.setText(progress.toString());
		
		return true;
	}
	
	public boolean set_progress(int n)
	{
		progress = new Integer(n);
		number.setText(progress.toString());
		
		return true;
	}

}
