package ch.specchio.proc_modules;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import ch.specchio.gui.GridbagLayouter;
import ch.specchio.gui.ModalDialog;

public class WavebandFilterDialog extends ModalDialog  implements ActionListener, ModuleCallback
{
	
	private static final long serialVersionUID = 1L;

	GridbagLayouter l;
	GridBagConstraints constraints;
	Vector<WavebandRegion> filters = new Vector<WavebandRegion>();
	JList listbox;
	DefaultListModel list_model;
	public boolean confirmed = false;
	
	public WavebandFilterDialog(Vector<WavebandRegion> filters, Point loc)
	{
		super("Filter configuration", loc);
		
		this.filters = filters;
		
	
	
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
		l.insertComponent(new JLabel("Filter regions [nm]:"), constraints);
		
		list_model = new DefaultListModel();
		
		for(int i = 0; i < filters.size();i++)
		{
			list_model.addElement(filters.get(i));
		}
		
		listbox = new JList(list_model);
		
		listbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane listScroller = new JScrollPane(listbox);
		listScroller.setPreferredSize(new Dimension(200, 80));
		
		constraints.gridy = 1;		
		constraints.gridheight = 4;
		l.insertComponent(listScroller, constraints);

		JButton New = new JButton("New");
		New.setActionCommand("New");
		New.addActionListener(this);	
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		l.insertComponent(New, constraints);	
		
		JButton Modify = new JButton("Modify");
		Modify.setActionCommand("Modify");
		Modify.setEnabled(false);
		Modify.addActionListener(this);			
		constraints.gridy++;
		l.insertComponent(Modify, constraints);	

		JButton Remove = new JButton("Remove");
		Remove.setActionCommand("Remove");
		Remove.addActionListener(this);			
		constraints.gridy++;
		l.insertComponent(Remove, constraints);	
		
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
	}
	
	public Vector<WavebandRegion> get_filters()
	{
		return filters;
	}

	synchronized public void actionPerformed(ActionEvent e) {
		
		if ("Ok".equals(e.getActionCommand())) {
			this.confirmed = true;
			done = true;
			this.notifyAll();
		}		
		
		if ("Cancel".equals(e.getActionCommand())) {
			done = true;
			this.notifyAll();
		}
		
		if ("New".equals(e.getActionCommand())) {
			
			DialogThread dt = new DialogThread(new WavebandFilterRegionDialog(null, this.getLocationOnScreen()), this, 1);
			dt.start();
		}
		
		if ("Remove".equals(e.getActionCommand())) {
			int index = listbox.getSelectedIndex();
		    list_model.remove(index);
		    
		    filters.remove(index);

		}		
		
		
	}

	public void user_data_provided(DialogThread dt) 
	{
		boolean ret_val = dt.ret_val;
		
		if(ret_val == true)
		{
			if(dt.callback_value == 1)
			{
				// region added
				WavebandRegion wf =  ((WavebandFilterRegionDialog)dt.md).wf;
				filters.add(wf);
				list_model.addElement(wf);
			}
				
			
		}
		
		
	}

	synchronized public boolean get_user_input() {
		super.get_user_input();

		return this.confirmed;
	}

}
