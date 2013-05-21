package ch.specchio.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;

public class ProgressReport_type1 extends ProgressReport_ {
	private static final long serialVersionUID = 1L;

	
	JLabel number_desc;
	JLabel number;
	
	
	public ProgressReport_type1 () 
	{
		super(); // super class constructor
				
		// build GUI
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 0;
		constraints.gridy = 0;
		operation_desc = new JLabel("Hiya mate");
		l.insertComponent(operation_desc, constraints);
		
		constraints.gridy = 1;
		curr_op = new JLabel("");
		l.insertComponent(curr_op, constraints);		
		
		constraints.gridwidth = 1;
		constraints.gridy = 2;
		number_desc = new JLabel("");
		l.insertComponent(number_desc, constraints);		
		
		constraints.gridx = 1;
		number = new JLabel("");
		l.insertComponent(number, constraints);			
		
	}
	
	public void set_number(int n)
	{
		Integer num = n;
		number.setText(num.toString());
	}
	
	public void set_number_desc(String desc)
	{
		number_desc.setText(desc);
	}
	
	


}
