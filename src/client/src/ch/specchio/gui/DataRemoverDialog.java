package ch.specchio.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.constants.UserRoles;
import ch.specchio.types.database_node;


public class DataRemoverDialog extends JFrame implements ActionListener, TreeSelectionListener{

	private static final long serialVersionUID = 1L;
	
	GridbagLayouter l;
	GridBagConstraints constraints;
	
	SpectralDataBrowser sdb;
	JTextField hierarchy_name;
	String table_name;
	
	SPECCHIOClient specchio_client;
	
	
	class removing_thread extends Thread
	{

		ProgressReportDialog pr = new ProgressReportDialog(DataRemoverDialog.this, "Removing data ...", false, 20);
		
		SpectralDataBrowser.SpectralDataBrowserNode sdb_node;
		
		public removing_thread(SpectralDataBrowser.SpectralDataBrowserNode sdb_node)
		{
			this.sdb_node = sdb_node;
		}
		
		synchronized public void run()
		{
			pr.set_indeterminate(true);
			pr.setVisible(true);
			
			try {
				// ask the server to remove the node from the database
				pr.set_operation("Deleting data. Please wait.");
				specchio_client.removeSpectralNode(sdb_node.getNode());
				pr.set_operation("Cleaning up...");
				specchio_client.clearMetaparameterRedundancyList();
				
				// remove the node from the local tree control
				removeTreeNode(sdb_node);
			}
			catch (SPECCHIOClientException ex) {
				
				JTextArea msg = new JTextArea(ex.getMessage() + "\n" + ex.getDetails());
				msg.setLineWrap(true);
				msg.setWrapStyleWord(true);
				Dimension dim = new Dimension(250,150);

				JScrollPane scrollPane = new JScrollPane(msg);		
				scrollPane.setPreferredSize(dim);
				
				
				JOptionPane.showMessageDialog(
		    			DataRemoverDialog.this,
		    			scrollPane,
		    			"Error",
		    			JOptionPane.ERROR_MESSAGE
		    		);
		    }
			
			pr.setVisible(false);
			
		}
				
		
	}

	
	DataRemoverDialog() throws SPECCHIOClientException
	{		
		super("Data Remover");
		
		// get a reference to the application's client object
		this.specchio_client = SPECCHIOApplication.getInstance().getClient();
		
		constraints = new GridBagConstraints();
		
		// some default values. subclasses can always overwrite these
		constraints.gridwidth = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// build GUI
		
		this.setLayout(new BorderLayout());
		
		// create browser and add to control panel
		sdb = new SpectralDataBrowser(specchio_client, !specchio_client.isLoggedInWithRole(UserRoles.ADMIN));
		
		sdb.build_tree();
		this.add("West", sdb);
		
		// add tree listener
		sdb.tree.addTreeSelectionListener(this);
			
		// create new panel for fields and buttons
		JPanel control = new JPanel();
		l = new GridbagLayouter(control);

		JButton remove = new JButton("Remove");
		remove.setActionCommand("remove");
		remove.addActionListener(this);		
		
		constraints.gridy = 0;
		l.insertComponent(remove, constraints);
		
		JButton cancel = new JButton("Close");
		cancel.setActionCommand("close");
		cancel.addActionListener(this);	
			
		constraints.gridy = 1;
		l.insertComponent(cancel, constraints);
				
		// add control panel to dialog
		this.add("Center", control);
				
		pack();
		
		
		
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		if ("remove".equals(e.getActionCommand())) {
			
			// process all selected nodes
			TreePath[] paths = sdb.tree.getSelectionPaths();
			
			// paths can be null when collapsing tree event happened
			if(paths != null) 
			{
				for(int i = 0; i < paths.length; i++)
				{
					SpectralDataBrowser.SpectralDataBrowserNode sdb_node =
							(SpectralDataBrowser.SpectralDataBrowserNode)paths[i].getLastPathComponent();
					
					if (sdb_node.getNode() instanceof database_node) {
						JOptionPane.showMessageDialog(this, "The root node cannot be removed.", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						removing_thread st = new removing_thread(sdb_node);
						st.start();
					}
				}
			}


				
		}
		
		if ("close".equals(e.getActionCommand())) {
			setVisible(false);
		}


		
	}
	
	
	private void removeTreeNode(SpectralDataBrowser.SpectralDataBrowserNode sdb_node)
	{
		// remove the concerned tree item
		DefaultTreeModel model = (DefaultTreeModel)sdb.tree.getModel();
		model.removeNodeFromParent(sdb_node);
	}
	

	
    /** Required by TreeSelectionListener interface. */
	public void valueChanged(TreeSelectionEvent e) {
		
	}


}


