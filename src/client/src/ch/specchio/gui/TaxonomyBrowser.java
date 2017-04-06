package ch.specchio.gui;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.TaxonomyNodeObject;

public class TaxonomyBrowser extends JScrollPane implements ActionListener {

	private static final long serialVersionUID = 1L;
	public JTree tree;
	int attribute_id;
	public TaxonomyNode root;

	//JScrollPane tree_scroll_pane;
	
	private SPECCHIOClient specchio_client;
	
	public TaxonomyBrowser(SPECCHIOClient specchio_client, int attribute_id)
	{
		this.specchio_client = specchio_client;
		this.attribute_id = attribute_id;		
				
		// create panel for level selection and for tree
		//tree_scroll_pane = new JScrollPane();				
		
		//this.getViewport().add(tree_scroll_pane);	
		
		try {
			build_tree();
		} catch (SPECCHIOClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

//	public void build_tree(int taxonomy_id) throws SPECCHIOClientException
//	{	
//		// this call recursively builds the tree!
////		TaxonomyNodeObject tn = specchio_client.getTaxonomyNode(taxonomy_id);
////		build_tree(cn);	
//	}
	
	// show all campaigns of the database
	public void build_tree() throws SPECCHIOClientException
	{
		TaxonomyNodeObject root_object = specchio_client.getTaxonomyRootNode(attribute_id);
		
		System.out.println(root_object.getName());
		
		root = new TaxonomyNode(this, root_object);
		
		tree = new JTree(root);	
		tree.setVisibleRowCount(8);
		//tree_scroll_pane.getViewport().add(tree);
		this.getViewport().add(tree);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

	}
	

	public void treeCollapsed(TreeExpansionEvent arg0) {
		
	}

	
//	public ArrayList<Integer> get_selected_spectrum_ids() throws SPECCHIOClientException
//	{
//		
//		ArrayList<Integer> ids = new ArrayList<Integer>();
//		
//		// process all selected nodes
//		TreePath[] paths = tree.getSelectionPaths();
//		
//		// paths can be null when collapsing tree event happened
//		if(paths != null) 
//		{
//			for(int i = 0; i < paths.length; i++)
//			{
//				SpectralDataBrowserNode bn = (SpectralDataBrowserNode)paths[i].getLastPathComponent();
//				ids.addAll(specchio_client.getSpectrumIdsForNode(bn.getNode()));
//			}
//		}
//		
//		return ids;
//	}	
	
	public int get_selected_taxonomy_id()
	{
		int id = 0;
				
		// process all selected nodes
		TreePath[] paths = tree.getSelectionPaths();
		
		// paths can be null when collapsing tree event happened
		if(paths != null) 
		{
			TaxonomyNode bn = (TaxonomyNode)paths[0].getLastPathComponent();
			TaxonomyNodeObject tn = bn.getNode();
			
			id = tn.getId();
		}
		
		return id;
	}	
	
	
	public void actionPerformed(ActionEvent e) {
		

	}
	
	
	public SPECCHIOClient get_specchio_client() {
		
		return specchio_client;
		
	}
	
//	public void reload_tree() throws SPECCHIOClientException
//	{
//		this.tree_scroll_pane.remove(tree);
//		
//		TreeSelectionListener[] l = tree.getTreeSelectionListeners();
//		System.out.println(tree.getMouseListeners().length);;
//		
//		MouseListener[] ml = tree.getMouseListeners(); // set by current Matlab callbacks
//			
//		this.build_tree();
//		
//		// restore listeners
//		for (int i=0;i<l.length;i++)
//		{
//			tree.addTreeSelectionListener(l[i]); 
//		}
//		
//		for (int i=0;i<ml.length;i++)
//		{
//			tree.addMouseListener(ml[i]); 
//		}			
//			
//	}


	/**
	 * This class represents a node in the JTree.
	 */
	public class TaxonomyNode extends DefaultMutableTreeNode {
	
		/**
		 * Serialisation version.
		 */
		private static final long serialVersionUID = 1L;
		
		/** the data browser to which this node belongs */
		private TaxonomyBrowser browser;
		
		/** have this node's children been downloaded from the server? */
		private boolean areChildrenDefined = false;
		
		
		/**
		 * Constructor.
		 * 
		 * @param tn	the taxonomy node object for this tree node
		 */
		public TaxonomyNode(TaxonomyBrowser browser, TaxonomyNodeObject tn) {
		
			this.browser = browser;
			this.areChildrenDefined = false;
			super.setUserObject(tn);
			
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
					List<TaxonomyNodeObject> children =
							browser.get_specchio_client().getChildrenOfTaxonomyNode((TaxonomyNodeObject)super.getUserObject());
					
					// insert the children into the JTree
					for (int i = 0; i < children.size(); i++) {
						TaxonomyNode treeChild = new TaxonomyNode(this.browser, children.get(i));
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
		 * Get the taxonomy node object associated with this tree node
		 * 
		 * @return the taxonomy node object associated with this tree node
		 */
		public TaxonomyNodeObject getNode() {
			
			return (TaxonomyNodeObject)super.getUserObject();
			
		}
		
		
		/**
		 * Get the identifier of the spectral node object associated with this tree node
		 * 
		 * @returns the identifier of the spectra node object associated with this tree node
		 */
//		public int getNodeId() {
//			
//			return getNode().getId();
//		
//		}
//		
	}

}
