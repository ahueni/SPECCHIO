package ch.specchio.proc_modules;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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



public class VisualisationSelectionDialog extends ModalDialog implements ActionListener, ListSelectionListener{

	private static final long serialVersionUID = 1L;
	
	DefaultListModel list_model;
	JList listbox;
	JButton OK;
	String vis_module_type;
	
	public static String gonio_hem_expl = "Gonio Hemisphere Explorer";
	public static String time_line_plot = "Time Line Plot";
	public static String time_line_expl = "Time Line Explorer";
	public static String sampling_points_plot = "Gonio Sampling Points Plot";
	public static String spectral_multiplot = "Spectral Line Plot";
	public static String spectral_scatter_multiplot = "Spectral Scatter Plot";
	
	public VisualisationSelectionDialog(Point loc) {
		super("Visualisation Module Selection", loc);
		
		GridbagLayouter l;
		GridBagConstraints constraints;
		
		constraints = new GridBagConstraints();
		l = new GridbagLayouter(this);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// build GUI
		constraints.gridy = 0;	
		constraints.gridx = 0;		
		l.insertComponent(new JLabel("Modules:"), constraints);
		
		list_model = new DefaultListModel();
		

		list_model.addElement(gonio_hem_expl);
		list_model.addElement(time_line_plot);
		list_model.addElement(time_line_expl);
		list_model.addElement(sampling_points_plot);
//		list_model.addElement(simple_spectral_plot);
		list_model.addElement(spectral_multiplot);
		list_model.addElement(spectral_scatter_multiplot);

		
		
		listbox = new JList(list_model);
		
		listbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listbox.addListSelectionListener(this);
		
		JScrollPane listScroller = new JScrollPane(listbox);
		listScroller.setPreferredSize(new Dimension(200, 150));
		
		constraints.gridy = 1;		
		constraints.gridheight = 4;
		l.insertComponent(listScroller, constraints);

		OK = new JButton("OK");
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

	public String get_vis_module_type()
	{
		return this.vis_module_type;
	}


	synchronized public void actionPerformed(ActionEvent e) {
		
		if ("Ok".equals(e.getActionCommand())) {
		
			vis_module_type = (String)listbox.getSelectedValue();
			
			this.confirmed = true;
			done = true;
			this.notifyAll();
		}		
		
		if ("Cancel".equals(e.getActionCommand())) {
			done = true;
			this.notifyAll();
		}
	}



	public void valueChanged(ListSelectionEvent e) {
		 if (e.getValueIsAdjusting() == false) {

		        if (listbox.getSelectedIndex() == -1) {
		        //No selection, disable fire button.
		            OK.setEnabled(false);

		        } else {
		        //Selection, enable the fire button.
		        	OK.setEnabled(true);
		        }
		    }
		
	}


}
