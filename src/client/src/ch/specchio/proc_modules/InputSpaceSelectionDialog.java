package ch.specchio.proc_modules;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.specchio.gui.GridbagLayouter;
import ch.specchio.gui.ModalDialog;
import ch.specchio.spaces.Space;

public class InputSpaceSelectionDialog extends ModalDialog implements ActionListener, ListSelectionListener
{

	private static final long serialVersionUID = 1L;
	
	DefaultListModel list_model;
	
	JButton OK;
	
	Space chosen_space = null;
	ArrayList<RequiredInputSpaceStruct> required_spaces;
	ArrayList<JList> listboxes = new ArrayList<JList>();
	
	public InputSpaceSelectionDialog(ArrayList<RequiredInputSpaceStruct> required_spaces, Point loc) {
		super("Input Space Selection", loc);
		
		this.required_spaces = required_spaces;
		
		GridbagLayouter l;
		GridBagConstraints constraints;
		
		constraints = new GridBagConstraints();
		l = new GridbagLayouter(this);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// build GUI
		constraints.gridx = 0;	
		JList listbox;
		
		for(int s=0;s < required_spaces.size();s++)
		{
			constraints.gridy = 0;	
			constraints.gridheight = 1;
			
			l.insertComponent(new JLabel(required_spaces.get(s).space_description), constraints);
			
			list_model = new DefaultListModel();
			
			for(int i=0;i < required_spaces.get(s).spaces.size();i++)
			{		
				list_model.addElement(required_spaces.get(s).spaces.get(i).getNumber());
			}
						
			listbox = new JList(list_model);
			
			listbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listbox.addListSelectionListener(this);
			listboxes.add(listbox);
			
			JScrollPane listScroller = new JScrollPane(listbox);
			listScroller.setPreferredSize(new Dimension(100, 80));
			
			constraints.gridy = 1;		
			constraints.gridheight = 4;
			l.insertComponent(listScroller, constraints);			
						
			constraints.gridx++;
		}
		


		OK = new JButton("OK");
		OK.setActionCommand("Ok");
		OK.addActionListener(this);	
		
		constraints.gridx++;
		constraints.gridy = 0;
		l.insertComponent(OK, constraints);	
		
		JButton Cancel = new JButton("Cancel");
		Cancel.setActionCommand("Cancel");
		Cancel.addActionListener(this);	
		
		constraints.gridy++;
		l.insertComponent(Cancel, constraints);	
		
		pack();
		

	}
	
	public ArrayList<RequiredInputSpaceStruct> get_selected_spaces()
	{
		return required_spaces;
	}

	synchronized public void actionPerformed(ActionEvent e) {
		if ("Ok".equals(e.getActionCommand())) {
			
			// set the selected spaces
			for(int s=0;s < required_spaces.size();s++)
			{
				required_spaces.get(s).chosen_space = required_spaces.get(s).spaces.get(listboxes.get(s).getSelectedIndex());	
			}

			this.confirmed = true;
			done = true;
			this.notifyAll();
		}		
		
		if ("Cancel".equals(e.getActionCommand())) {
			done = true;
			this.notifyAll();
		}
		
	}
	public void valueChanged(ListSelectionEvent e) 
	{
		 if (e.getValueIsAdjusting() == false) 
		 {			 
			 boolean selection = true;
			  
			 // make sure all listboxes have a selection
			 for(int s=0;s < required_spaces.size();s++)
			 {
				 selection = selection & listboxes.get(s).getSelectedIndex() != -1;
			 }
	 
			 OK.setEnabled(selection);
			 
		 }
		
	}

}
