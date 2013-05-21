package ch.specchio.proc_modules;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ch.specchio.gui.GridbagLayouter;
import ch.specchio.gui.ModalDialog;

public class WavebandFilterRegionDialog extends ModalDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	WavebandRegion wf = null;
	JTextField upper, lower;
	
	
	public WavebandFilterRegionDialog(WavebandRegion wf, Point loc)
	{
		super("Filter Definition", loc);
		
		if(wf == null)
		{
			this.wf = new WavebandRegion(0,0);
		}
		else
		{
			this.wf = wf;
		}
		
		GridbagLayouter l;
		GridBagConstraints constraints;
		
		constraints = new GridBagConstraints();
		l = new GridbagLayouter(this);
		
		
		constraints.gridwidth = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
		
		// build GUI
		constraints.gridy = 0;	
		constraints.gridx = 0;		
		l.insertComponent(new JLabel("Lower wvl [nm]:"), constraints);

		lower = new JTextField(10);
		constraints.gridx++;
		l.insertComponent(lower, constraints);

		constraints.gridy++;	
		constraints.gridx = 0;		
		l.insertComponent(new JLabel("Upper wvl [nm]:"), constraints);
	

		upper = new JTextField(10);
		constraints.gridx++;
		l.insertComponent(upper, constraints);
		
		JButton OK = new JButton("OK");
		OK.setActionCommand("Ok");
		OK.addActionListener(this);	
		
		constraints.gridx = 2;
		constraints.gridy = 0;
		l.insertComponent(OK, constraints);	
		
		JButton Cancel = new JButton("Cancel");
		Cancel.setActionCommand("Cancel");
		Cancel.addActionListener(this);	
		
		constraints.gridy++;
		l.insertComponent(Cancel, constraints);	
		
		pack();
		validate();
		
	}
	
	public WavebandRegion get_range() 
	{
		return wf;
    }

	synchronized public boolean get_user_input() 
	{
		super.get_user_input();
        
		wf.set_range(Float.valueOf(lower.getText()), Float.valueOf(upper.getText()));
		
		return this.confirmed;
    }	
	
	synchronized public void actionPerformed(ActionEvent e) 
	{
		if ("Ok".equals(e.getActionCommand())) {			
			//setVisible(false);
			this.confirmed = true;
			done = true;
			this.notifyAll();
		}		
		
		if ("Cancel".equals(e.getActionCommand())) {
			//setVisible(false);
			done = true;
			this.notifyAll();
		}
		
	}

}
