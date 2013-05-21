package ch.specchio.proc_modules;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.specchio.gui.GridbagLayouter;
import ch.specchio.spaces.Space;

public class SpaceProcessingChainComponent extends ProcessingChainComponent {
	
	// space states
	public static final Integer DATA_READY = 0;
	public static final Integer DATA_NOT_READY = 1;
	public static final Integer SPACE_INVALID = 2;
	
	private Space space;

	protected Integer internal_state = DATA_NOT_READY;
	
	ArrayList<Module> following_modules = new ArrayList<Module>();
	ArrayList<Integer> following_modules_data_ready_status = new ArrayList<Integer>(); // true means data can be read, false means no data available (or already processed)
	
	private JLabel no_of_datapoints_label;
	private JLabel space_name;
	private JLabel space_number;
	Integer number = -1; // numbers are assigned by the processing plane
	
	public SpaceProcessingChainComponent(Frame owner, Space space) {
		
		super(owner);
		this.space = space;
		this.space_name = new JLabel("");
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// nothing to do

	}
	
	
	public Integer getNumber()
	{
		return this.number;
	}
	
	
	public void setNumber(Integer number)
	{
		this.number = number;
	}
	
	
	public Space getSpace()
	{
		return space;	
	}
	
	
	public void setSpace(Space space)
	{
		this.space = space;
	}
	
	public void add_following_module(Module m)
	{
		following_modules.add(m);
		following_modules_data_ready_status.add(DATA_NOT_READY);
	}
	
	// called by the following module when it has transformed all data from this space
	public synchronized void all_data_read(Module m)
	{
		following_modules_data_ready_status.set(following_modules.indexOf(m), DATA_NOT_READY);	
	}
	
	// lets the caller wait till data is ready, i.e. the previous processing module signal that the space is finalized
	public synchronized Integer data_ready(Module m)
	{

		if(following_modules_data_ready_status.get(following_modules.indexOf(m)) == DATA_NOT_READY)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return following_modules_data_ready_status.get(following_modules.indexOf(m));		
	}
	
	// called by the preceeding process when all data have been transformed into this space
	public synchronized void finalized()
	{
		internal_state = DATA_READY;
		
		// set the flags for modules to true
		for(int i=0;i < following_modules_data_ready_status.size();i++)
			following_modules_data_ready_status.set(i, DATA_READY);
		
		// all data has been transferred into this space, thus update the label
		this.update_no_of_datapoints_label();
		
		// notify waiting processing threads
		notifyAll();
	}
	
	// to be called by threads that are not registered with the space (e.g. Fileoutputmanager)
	public synchronized boolean get_data_ready_status()
	{
		if(internal_state == DATA_READY)
		{
			return true;
		}
		return false;
	}
	
	public ArrayList<Module> get_following_modules()
	{
		return following_modules;
	}

	public JPanel get_info_panel()
	{
		
		if(info_panel == null)
		{
		
			super.get_info_panel();
			info_panel.setSize(new Dimension(220, 70)); // override size set by super class
			
			GridbagLayouter space_panel_l = new GridbagLayouter(info_panel);
			GridBagConstraints constraints = new GridBagConstraints();
			
			info_panel.setBorder(BorderFactory.createLineBorder(Color.black));
			Font number_label_font = new Font("Arial", Font.ITALIC,10);
			Font label_font = new Font("Arial", Font.PLAIN,12);
			
			constraints.ipadx = 5;
			constraints.ipady = 5;
			constraints.gridx = 0;
			constraints.gridy = 0;
			
			space_number = new JLabel(getNumber().toString());
			space_number.setFont(number_label_font);
			space_panel_l.insertComponent(space_number, constraints);

			constraints.gridx++;
			space_name.setText(space.getSpaceNameString());
			space_name.setFont(number_label_font);
			space_panel_l.insertComponent(space_name, constraints);
			
			constraints.gridx = 0;
			constraints.gridy++;
			JLabel space_type = new JLabel("Type:");
			space_type.setFont(label_font);
			space_panel_l.insertComponent(space_type, constraints);
			
			constraints.gridx = 1;
			JLabel space_type_name = new JLabel(space.getSpaceTypeName());
			space_type_name.setFont(label_font);
			space_panel_l.insertComponent(space_type_name, constraints);
			
			constraints.gridx = 0;
			constraints.gridy++;
			JLabel D = new JLabel("Dim:");
			D.setFont(label_font);
			space_panel_l.insertComponent(D , constraints);
	
			constraints.gridx = 1;
			if(space.getDimensionalityIsSet())
			{
				JLabel dim = new JLabel(space.getDimensionality().toString());
				dim.setFont(label_font);
				space_panel_l.insertComponent(dim, constraints);
			}
	
			constraints.gridx = 0;
			constraints.gridy++;
			JLabel n_vec = new JLabel("# Vectors:");
			n_vec.setFont(label_font);
			space_panel_l.insertComponent(n_vec, constraints);
			
			constraints.gridx = 1;
			no_of_datapoints_label = new JLabel("");
			no_of_datapoints_label.setFont(label_font);
			space_panel_l.insertComponent(no_of_datapoints_label, constraints);
			update_no_of_datapoints_label();
		}
			
		return info_panel;
		
	}
	
	// to be called if this space is about to be invalid. 
	// this makes sure that threads (modules) waiting on it are woken up and can
	// wait on a new space
	public synchronized void set_invalid()
	{
		internal_state = SPACE_INVALID;
		
		for(int i=0;i < following_modules_data_ready_status.size();i++)
			following_modules_data_ready_status.set(i, SPACE_INVALID);		

		//invalid = true;
		notifyAll();
	}
	
	
	public synchronized void set_space_name(String space_name)
	{
		this.space.setSpaceNameString(space_name);
		this.space_name.setText(space_name);
	}
	
	
	public synchronized void set_valid()
	{
		internal_state = DATA_NOT_READY;
		
		for(int i=0;i < following_modules_data_ready_status.size();i++)
			following_modules_data_ready_status.set(i, DATA_NOT_READY);		
		
		System.out.println("should store previous state of space!");

		notifyAll();		
	}
	
	public void update_no_of_datapoints_label()
	{
		if(no_of_datapoints_label != null)
		{
			String no_of_datapoints = "";
			
			if(space.getNumberOfDataPoints() == 0 && space.getSpectrumIds().size() == 0)
			{
				no_of_datapoints = "Yet unknown";
			}
	
			if(space.getNumberOfDataPoints() == 0 && space.getSpectrumIds().size() >	0)
			{
				no_of_datapoints = Integer.toString(space.getSpectrumIds().size());
			}
			
			if(space.getNumberOfDataPoints() > 0)
			{
				no_of_datapoints = Integer.toString(space.getNumberOfDataPoints());
			}
			
			no_of_datapoints_label.setText(no_of_datapoints);
			
			if(processing_plane != null)
			{				
				processing_plane.redraw_object(ppo);
			}

		}
		
	}

}
