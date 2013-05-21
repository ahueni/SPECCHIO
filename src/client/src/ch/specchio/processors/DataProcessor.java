package ch.specchio.processors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.gui.SpectralDataBrowser;
import ch.specchio.processing_plane.ProcessingPlane;
import ch.specchio.queries.Query;
import ch.specchio.spaces.Space;


public class DataProcessor extends JFrame  implements ActionListener //, TreeSelectionListener 
{
	
	private static final long serialVersionUID = 1L;

	ProcessingPlane pp;

	JTabbedPane explorer_tabs;
	JTextField resulting_rows;
	SpectralDataBrowser sdb;
	JPanel processing_panel;
	//JPanel plotting_panel;
	JButton run, change_colour;
	SpaceAndProcessContainer sp_frame;
	GridBagConstraints sp_frame_constraints = new GridBagConstraints();
	
	Query query = new Query("spectrum");
	
	class proc_thread extends Thread
	{
		
		public void run()
		{
		  run.setEnabled(false);

		  pp.start_processing();
		
		  run.setEnabled(true);

		}

	}
	
	
	
	public DataProcessor(SPECCHIOClient specchio_client)
	{
	
		super("Space Network Processor (Alpha Release)");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		// create GUI
		Dimension d = new Dimension(700, 500);
		this.setPreferredSize(d);
		
		// set border layout for this dialog
		this.setLayout(new BorderLayout());
	
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;	
		
		// Processing panel		
		processing_panel = new JPanel();
		processing_panel.setLayout(new BorderLayout());
		pp = new ProcessingPlane(this, specchio_client);

		processing_panel.add(pp.get_panel(), BorderLayout.CENTER);
			
		run = new JButton("Run");
		run.setActionCommand("run");
		run.addActionListener(this);
		run.setEnabled(false);
		
		constraints.gridx++;
		processing_panel.add(run, BorderLayout.EAST);

		add("Center", processing_panel);
		pack();

	}
	
	
	public void set_ids(ArrayList<Integer> ids) throws SPECCHIOClientException
	{
		pp.set_input_spectra_ids(ids);
		if (pp.get_no_of_input_spaces() > 0)
			run.setEnabled(true);
	}
	
	public void set_input_spaces(Space spaces[]) throws SPECCHIOClientException
	{
		pp.set_input_spaces(spaces);
		if (pp.get_no_of_input_spaces() > 0)
			run.setEnabled(true);
	}	
	
	public ProcessingPlane getProcessingPlane()
	{
		return this.pp;
	}

	public void actionPerformed(ActionEvent e) {
	      
	      if("run".equals(e.getActionCommand()))
	      {
	    	  proc_thread ppt = new proc_thread();
	    	  ppt.start();
	    	  
	      }
	      
	}
}

