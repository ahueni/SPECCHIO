package ch.specchio.processing_plane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.gui.SPECCHIOApplication;
import ch.specchio.proc_modules.DialogThread;
import ch.specchio.proc_modules.Module;
import ch.specchio.proc_modules.ModuleCallback;
import ch.specchio.proc_modules.ModuleSelection;
import ch.specchio.proc_modules.ProcessingChainComponent;
import ch.specchio.proc_modules.SpaceProcessingChainComponent;
import ch.specchio.spaces.Space;

public class ProcessingPlane implements ActionListener, ModuleCallback
{
	
	ProcThread proc_thread = new ProcThread();
	boolean processing_done = false;
	boolean process = false;
	
	protected JPopupMenu popup;
	MouseListener popupListener;
	
	GraphModel model = new DefaultGraphModel();
	public JGraph graph = new JGraph(model);
	public JPanel main_panel = new JPanel();
	
	ArrayList<SpaceProcessingChainComponent> input_spaces = new ArrayList<SpaceProcessingChainComponent>();
	ArrayList<SpaceProcessingChainComponent> spaces = new ArrayList<SpaceProcessingChainComponent>();
	
	int last_menu_click_x, last_menu_click_y;
	int space_cnt = 0;
	JScrollPane scroll_pane;
	
	Frame owner;
	SPECCHIOClient specchio_client;
	
	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {		
				// Get Cell under Mousepointer 
				int x = e.getX(), y = e.getY(); 
				Object cell = graph.getFirstCellForLocation(x, y); 
				// show plane menu if not over cell 
				if (cell == null) { 
					last_menu_click_x = x;
					last_menu_click_y = y;
					popup.show(e.getComponent(),
							x, y); 
				} 
				else
				{
					ProcessingChainComponent pcc = (ProcessingChainComponent)((ProcessingPlaneObject) cell).getUserObject();
					pcc.show_menu(e.getComponent(), x, y);
				}


				
			}
		}
	}	
	
	
	class ProcThread extends Thread
	{
		
		public synchronized void run()
		{
			try {
				while(true)
				{
					// wait for process signal
					if(!process)
					{
						try {
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}				
					}
					
					// cause the input spaces to load the data from the database
					for(int i =0;i<input_spaces.size();i++)
					{
						Space curr_space = input_spaces.get(i).getSpace();
						SpaceProcessingChainComponent spcc = input_spaces.get(i);												
						Space space = specchio_client.loadSpace(curr_space);
						
						spcc.setSpace(space);
						spcc.finalized();
					}
	
					process = false;
					
					processing_done = true;
					notifyAll(); // wake up all threads waiting for chain to finish
					
				}
			}
	  		catch (SPECCHIOClientException ex) {
				JOptionPane.showMessageDialog(
		    			SPECCHIOApplication.getInstance().get_frame(),
		    			ex.getMessage(),
		    			"Error",
		    			JOptionPane.ERROR_MESSAGE
		    		);
		    }
			
		}
		
		public synchronized void start_processing()
		{
			process = true;
			this.notify();
		}
	}
	
	
	
	public ProcessingPlane(Frame owner, SPECCHIOClient specchio_client)
	{
		this.owner = owner;
		this.specchio_client = specchio_client;
		
		proc_thread.start(); // start processing thread; this keeps it waiting for processing orders
		
		popup = new JPopupMenu();		
		JMenuItem menuItem = new JMenuItem("Add Module");
	    menuItem.addActionListener(this);
	    popup.add(menuItem);
	    
		popupListener = new PopupListener();
		graph.addMouseListener(popupListener);
		graph.setEditable(false);
		graph.setDisconnectable(false);
		
		main_panel.setLayout(new BorderLayout());
		
		Dimension d = new Dimension();		
		d.height = 1500;
		d.width = 1000;
		graph.setPreferredSize(d);
		graph.setSize(graph.getPreferredSize());

		scroll_pane = new JScrollPane(graph);
		scroll_pane.getVerticalScrollBar().setUnitIncrement(10);
		scroll_pane.getHorizontalScrollBar().setUnitIncrement(10);
		main_panel.add(scroll_pane, BorderLayout.CENTER);
		
	}
	
	
	public JPanel get_panel()
	{
		return main_panel;	
	}
	
	
	public synchronized void set_input_spectra_ids(ArrayList<Integer> ids) throws SPECCHIOClientException
	{
		Space spaces[] = specchio_client.getSpaces(ids, false, false, null);		
		set_input_spaces(spaces);
	}
	
	
	public synchronized void set_input_spaces(Space spaces[])
	{
		input_spaces = new ArrayList<SpaceProcessingChainComponent>();
		for (Space space : spaces) {
			input_spaces.add(new SpaceProcessingChainComponent(owner, space));
		}
		add_input_spaces(input_spaces);
	}
	
	public synchronized void add_input_spaces(ArrayList<SpaceProcessingChainComponent> spaces)
	{
		input_spaces = spaces;
		
		// add every space as new vertex to the graph
		for(int i =0;i<input_spaces.size();i++)
		{			
			SpaceProcessingChainComponent spcc = input_spaces.get(i);
					
			register_space(spcc);
			Dimension d = spcc.get_info_panel().getSize();
			
			add_processing_plane_object(spcc, 40, (d.height+20)*i+10);	
			
		}
		
	}
	
	public synchronized int get_no_of_input_spaces()
	{
		return input_spaces.size();
	}
	
	public synchronized void register_space(SpaceProcessingChainComponent space)
	{
		spaces.add(space);
		space.setNumber(space_cnt++);
	}
	
	public synchronized void deregister_space(SpaceProcessingChainComponent space)
	{
		spaces.remove(space);
	}	
	
	public synchronized ArrayList<SpaceProcessingChainComponent> get_spaces()
	{
		return spaces;
	}
	
	
	public synchronized void  add_edge(ProcessingPlaneObject source, ProcessingPlaneObject target)
	{
		// Create Edge
		DefaultEdge edge = new DefaultEdge();
		// Fetch the ports from the new vertices, and connect them with the edge
		if(source.getChildCount() == 0) 
			source.addPort();
		edge.setSource(source.getChildAt(0));
		edge.setTarget(target.getChildAt(0));	
		
		int arrow = GraphConstants.ARROW_CLASSIC;
		GraphConstants.setLineEnd(edge.getAttributes(), arrow);
		GraphConstants.setEndFill(edge.getAttributes(), true);
		
		graph.getGraphLayoutCache().insert(edge);			
		redraw_object(edge);

	}
	
	
	public synchronized void add_module(Module m)
	{		
		add_module(m, last_menu_click_x, last_menu_click_y);		
	}
	
	public synchronized void add_module(Module m, int x, int y)
	{		
		add_processing_plane_object(m, x, y);
		m.start();		
	}
		
	
	public synchronized void add_processing_plane_object(ProcessingChainComponent pcc, int x, int y)
	{
		
		ProcessingPlaneObject ppo = new ProcessingPlaneObject(pcc);
		ppo.addPort();
		pcc.set_processing_plane(this); // set the pointer to this plane in the component
		
		Dimension d = pcc.get_info_panel().getSize();
		
		GraphConstants.setBounds(ppo.getAttributes(), new Rectangle2D.Double(
				x, y, d.getWidth(), d.getHeight()));
		
		graph.getGraphLayoutCache().setFactory(new PPOViewFactory());
		
		graph.getGraphLayoutCache().insert(ppo);		
		
		// make sure the graph is updated
		redraw_object(ppo);
		
	}
	
	
	public synchronized void remove_processing_plane_object(ProcessingChainComponent pcc)
	{
		Object[] remove_array = new Object[1];
		ArrayList<Object> remove_list = new ArrayList<Object>();
		
		
		// remove all edges of this object
		int numChildren = model.getChildCount(pcc.get_ppo());
		
		for (int i = 0; i < numChildren; i++) 
		{ 
			Object port = model.getChild(pcc.get_ppo(), i); 
			if (model.isPort(port)) 
			{ 
				Iterator<Object> iter = model.edges(port); 
				while (iter.hasNext()) 
				{ 
					remove_list.add(iter.next());
				} 
			} 
			
			Iterator<Object> it = remove_list.listIterator();
			while(it.hasNext())
			{
				remove_array[0] = it.next();
				graph.getGraphLayoutCache().remove(remove_array);
			}

			
		} 
		
		//remove object
		remove_array[0] = pcc.get_ppo();		
		graph.getGraphLayoutCache().remove(remove_array);

	}
			
	
	public synchronized void redraw_object(DefaultGraphCell ppo)
	{
		if(ppo instanceof ProcessingPlaneObject)
		{
			((ProcessingChainComponent)((ProcessingPlaneObject)ppo).getUserObject()).get_info_panel().validate();
		}
		
		CellView v = graph.getGraphLayoutCache().getMapping(ppo, false);
		if(v != null)
		{
			graph.addOffscreenDirty(v.getBounds());	
			scroll_pane.repaint();
		}
				
	}
	
	
	public void start_processing()
	{
		proc_thread.start_processing();
	}
	

	public synchronized void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Add Module"))
		{
			// bring up the module selection dialog
			Point loc = new Point(last_menu_click_x,last_menu_click_y);
			Point abs_loc = main_panel.getLocationOnScreen();
			abs_loc.x += loc.x;
			abs_loc.y += loc.y;
			
			DialogThread dt = new DialogThread(new ModuleSelection(specchio_client, abs_loc, this), this, 1);
			dt.start();			
		}		
	}

	public synchronized void user_data_provided(DialogThread dt) {
		
		// special handling to prevent the adding of null modules (happens e.g. with the 'proof of concept' module)
		
		Module m = ((ModuleSelection)dt.md).get_module();
		
		if(m != null) add_module(m);
		
	}

}
