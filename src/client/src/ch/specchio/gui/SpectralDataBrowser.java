package ch.specchio.gui;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.Campaign;
import ch.specchio.types.campaign_node;
import ch.specchio.types.database_node;
import ch.specchio.types.hierarchy_node;
import ch.specchio.types.spectral_node_object;

public class SpectralDataBrowser extends JScrollPane implements ActionListener
{
	private static final long serialVersionUID = 1L;
	public JTree tree;
	public SpectralDataBrowserNode root;
	
	boolean restrict_to_view; // restrict shown nodes to views (i.e. user sees only own data)
	
	public JComboBox order_by_box;
	
	String order_by = "Acquisition Time";
	private JPanel panel;
	JScrollPane tree_scroll_pane;
	
	private SPECCHIOClient specchio_client;
	
	public SpectralDataBrowser(SPECCHIOClient specchio_client, boolean restrict_to_view)
	{
		this.specchio_client = specchio_client;
		this.restrict_to_view = restrict_to_view;
		
		
		// build GUI (without the tree)
		
		// create panel for level selection and for tree
		tree_scroll_pane = new JScrollPane();
				
		panel = new JPanel();		
		order_by_box = new JComboBox();
		
		combo_table_data c = new combo_table_data("Sampling Date", "Acquisition Time");
		order_by_box.addItem(c);
		
		c = new combo_table_data("Filename", "File Name");
		order_by_box.addItem(c);	
		
		c = new combo_table_data("Insert order", "Loading Time");
		order_by_box.addItem(c);	
		
		order_by_box.addActionListener(this);
		
		JPanel order_by_panel = new JPanel();
		order_by_panel.setLayout(new BorderLayout());
		order_by_panel.add(order_by_box, BorderLayout.EAST);
		order_by_panel.add(new JLabel("Order by:"), BorderLayout.WEST);
		
		
		JButton refresh = new JButton("Refresh");
		refresh.setActionCommand("refresh");
		refresh.addActionListener(this);				
		
		panel.setLayout(new BorderLayout());
		panel.add(order_by_panel, BorderLayout.NORTH);
		panel.add(tree_scroll_pane, BorderLayout.CENTER);
		panel.add(refresh, BorderLayout.SOUTH);		
		
		this.getViewport().add(panel);	

	}
	
	public void set_view_restriction(boolean restrict_to_view) throws SPECCHIOClientException
	{
		this.restrict_to_view = restrict_to_view;
		build_tree();
	}
	
	public void build_tree(int campaign_id) throws SPECCHIOClientException
	{	
		// this call recursively builds the tree!
		campaign_node cn = specchio_client.getCampaignNode(campaign_id, order_by, restrict_to_view);
		build_tree(cn);	
	}
	
	// show all campaigns of the database
	public void build_tree() throws SPECCHIOClientException
	{
		database_node dn = specchio_client.getDatabaseNode(order_by, restrict_to_view);
		build_tree(dn);		
	}
	
	void build_tree(spectral_node_object node)
	{
		root = new SpectralDataBrowserNode(this, node);
		// create tree using the above node as top node
		tree = new JTree(root);	
	
		tree_scroll_pane.getViewport().add(tree);
		
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		
	}

	public void treeCollapsed(TreeExpansionEvent arg0) {
		
	}
	
	
	public Campaign get_selected_campaign() throws SPECCHIOClientException
	{
		Campaign campaign = null;
		
		TreePath path = tree.getSelectionPath();
		if (path != null) {
			// find the campaign node at the root of the selection
			SpectralDataBrowserNode bn = (SpectralDataBrowserNode)path.getLastPathComponent();
			while (bn != null && !(bn.getNode() instanceof campaign_node)) {
				path = path.getParentPath();
				bn = (path != null)? (SpectralDataBrowserNode)path.getLastPathComponent() : null;
			}
			
			if (bn != null) {
				campaign = specchio_client.getCampaign(bn.getNodeId());
			}
		}
		
		return campaign;
	}
	
	
	public spectral_node_object get_selected_node() {
		
		TreePath path = tree.getSelectionPath();
		if (path != null) {
			SpectralDataBrowserNode bn = (SpectralDataBrowserNode)path.getLastPathComponent();
			return (bn != null)? bn.getNode() : null;
		} else {
			return null;
		}
		
	}

	
	public ArrayList<Integer> get_selected_spectrum_ids() throws SPECCHIOClientException
	{
		
		Set<Integer> ids = new TreeSet<Integer>();
		
		// process all selected nodes
		TreePath[] paths = tree.getSelectionPaths();
		
		// paths can be null when collapsing tree event happened
		if(paths != null) 
		{
			for(int i = 0; i < paths.length; i++)
			{
				SpectralDataBrowserNode bn = (SpectralDataBrowserNode)paths[i].getLastPathComponent();
				ids.addAll(specchio_client.getSpectrumIdsForNode(bn.getNode()));
			}
		}
		
		return new ArrayList<Integer>(ids);
	}	
	
	public ArrayList<Integer> get_selected_hierarchy_ids()
	{
		
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		// process all selected nodes
		TreePath[] paths = tree.getSelectionPaths();
		
		// paths can be null when collapsing tree event happened
		if(paths != null) 
		{
			for(int i = 0; i < paths.length; i++)
			{
				SpectralDataBrowserNode bn = (SpectralDataBrowserNode)paths[i].getLastPathComponent();
				spectral_node_object sn = bn.getNode();
				
				if(sn instanceof hierarchy_node)
				{
					// add this hierarchy nodes id to the list
					ids.add(sn.getId());
				}
			}
		}
		
		return ids;
	}	
	
	
	public void actionPerformed(ActionEvent e) {
		
		if("refresh".equals(e.getActionCommand()))
		{
			try {
				reload_tree();
			}
			catch (SPECCHIOClientException ex) {
	  			ErrorDialog error = new ErrorDialog(
			    		SPECCHIOApplication.getInstance().get_frame(),
		    			"Error",
		    			ex.getUserMessage(),
		    			ex
			    	);
		  			error.setVisible(true);
			}
		}
		
		if("comboBoxChanged".equals(e.getActionCommand()))
		{	 	
			try {
				this.order_by = ((combo_table_data) this.order_by_box.getSelectedItem()).getValue();
				reload_tree();
			}
			catch (SPECCHIOClientException ex) {
	  			ErrorDialog error = new ErrorDialog(
			    		SPECCHIOApplication.getInstance().get_frame(),
		    			"Error",
		    			ex.getUserMessage(),
		    			ex
			    	);
		  			error.setVisible(true);
			}
		}
	}
	
	public String get_order_by_field()
	{
		return this.order_by;
	}
	
	public SPECCHIOClient get_specchio_client() {
		
		return specchio_client;
		
	}
	
	public void reload_tree() throws SPECCHIOClientException
	{
		this.tree_scroll_pane.remove(tree);
		
		// save references to the listeners of the tree
		TreeSelectionListener[] l = tree.getTreeSelectionListeners();
		MouseListener[] ml = tree.getMouseListeners(); // set by current Matlab callbacks
		
		// save the current lead selection path
		TreePath oldLeadSelectionPath = tree.getSelectionPath();
			
		this.build_tree();
		
		// manufacture a selection event to represent the effective de-selection of the previos tree
		TreeSelectionEvent event = new TreeSelectionEvent(
				tree,
				null,
				new boolean[0],
				oldLeadSelectionPath,
				null
			);
		
		// restore listeners
		for (int i=0;i<l.length;i++)
		{
			tree.addTreeSelectionListener(l[i]);
			l[i].valueChanged(event);
		}
		
		for (int i=0;i<ml.length;i++)
		{
			tree.addMouseListener(ml[i]); 
		}			
			
	}


	/**
	 * This class represents a node in the JTree.
	 */
	public class SpectralDataBrowserNode extends DefaultMutableTreeNode {
	
		/**
		 * Serialisation version.
		 */
		private static final long serialVersionUID = 1L;
		
		/** the spectral data browser to which this node belongs */
		private SpectralDataBrowser browser;
		
		/** have this node's children been downloaded from the server? */
		private boolean areChildrenDefined = false;
		
		
		/**
		 * Constructor.
		 * 
		 * @param sn	the spectral node object for this tree node
		 */
		public SpectralDataBrowserNode(SpectralDataBrowser browser, spectral_node_object sn) {
		
			this.browser = browser;
			this.areChildrenDefined = false;
			super.setUserObject(sn);
			
		}
		
		
		/**
		 * Get the number of children of this node.
		 * 
		 * @return the number of children
		 */
		public int getChildCount() {
			
			// make sure that the children have been downloaded from the server
			if (!areChildrenDefined) {
				defineChildNodes();
			}
			
			return super.getChildCount();
			
		}
		
		
		/**
		 * Download the children of this node from the server. Does nothing
		 * if the children have already been downloaded.
		 */
		protected void defineChildNodes() {
			
			if (!areChildrenDefined) {
				
				try {
					// get a list of child nodes from the server
					List<spectral_node_object> children =
							browser.get_specchio_client().getChildrenOfNode((spectral_node_object)super.getUserObject());
					
					// insert the children into the JTree
					for (int i = 0; i < children.size(); i++) {
						SpectralDataBrowserNode treeChild = new SpectralDataBrowserNode(this.browser, children.get(i));
						super.insert(treeChild, i);
					}
					
					// set the flag that indicates that the children have been loaded
				    areChildrenDefined = true;
				}
				catch (SPECCHIOClientException ex) {
					// nothing we can do about it
					ex.printStackTrace();
				}
				
			}
		}
		
		
		/**
		 * Get the spectral node object associated with this tree node
		 * 
		 * @returns the spectral node object associated with this tree node
		 */
		public spectral_node_object getNode() {
			
			return (spectral_node_object)super.getUserObject();
			
		}
		
		
		/**
		 * Get the identifier of the spectral node object associated with this tree node
		 * 
		 * @returns the identifier of the spectra node object associated with this tree node
		 */
		public int getNodeId() {
			
			return getNode().getId();
		
		}
		
	}

}