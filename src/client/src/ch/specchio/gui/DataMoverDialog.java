package ch.specchio.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.constants.UserRoles;
import ch.specchio.types.hierarchy_node;
import ch.specchio.types.spectral_node_object;

public class DataMoverDialog extends JFrame implements TreeSelectionListener, ActionListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SPECCHIOClient specchio_client;
	private GridBagConstraints constraints;
	private SpectralDataBrowser sdb_source;
	private SpectralDataBrowser sdb_target;
	private JTextArea info_area;
	private boolean transfer_allowed = false;
	private boolean source_content_move = false;
	private JButton remove;

	DataMoverDialog() throws SPECCHIOClientException
	{		
		super("Data Mover");
		
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
		sdb_source = new SpectralDataBrowser(specchio_client, !specchio_client.isLoggedInWithRole(UserRoles.ADMIN));
		
		sdb_source.build_tree();
		this.add("West", sdb_source);
		
		sdb_target = new SpectralDataBrowser(specchio_client, !specchio_client.isLoggedInWithRole(UserRoles.ADMIN));
		
		sdb_target.build_tree();
		this.add("East", sdb_target);
		
		
		// add tree listener
		sdb_source.tree.addTreeSelectionListener(this);
		sdb_target.tree.addTreeSelectionListener(this);
			
		// create new panel for fields and buttons
		JPanel control = new JPanel();
		GridbagLayouter l = new GridbagLayouter(control);

		remove = new JButton("Move ------->");
		remove.setActionCommand("move");
		remove.addActionListener(this);		
		remove.setEnabled(transfer_allowed);
		
		constraints.gridy = 0;
		l.insertComponent(remove, constraints);
		
		info_area = new JTextArea(10, 30);
		info_area.setLineWrap(true);
		
		constraints.gridy++;
		l.insertComponent(info_area, constraints);

				
		// add control panel to dialog
		this.add("Center", control);
				
		pack();
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		int source_id = sdb_source.get_selected_hierarchy_ids().get(0);
		int target_id = sdb_target.get_selected_hierarchy_ids().get(0);
		
		// process selected nodes
		boolean success = specchio_client.moveHierarchy(source_id, target_id);
		
		if(success && source_content_move)
		{
			// remove the source directory, which is now empty
			ArrayList<spectral_node_object> sns = new ArrayList<spectral_node_object>();
			sns.add(new hierarchy_node(source_id, "",""));
			specchio_client.removeSpectralNodes(sns);
		}
		
		if(success)
		{
			String message = "Data were successfully moved.";
			JOptionPane.showMessageDialog(
					(Frame)getOwner(),
					message,
					"Data Mover Report",
					JOptionPane.INFORMATION_MESSAGE, SPECCHIOApplication.specchio_icon
				);
			
		}
		
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {

		String text = "";
		transfer_allowed = false;
		
		// update move button
		remove.setEnabled(transfer_allowed);		

		if(sdb_source.get_selected_hierarchy_ids().size()>1 )
		{
			info_area.setText("Only single hierarchies can be selected as source");
			return;
		}
		
		if(sdb_target.get_selected_hierarchy_ids().size()>1 || sdb_target.get_selected_hierarchy_ids().size()==0)
		{
			info_area.setText("A single hierarchy must be selected as target");
			return;
		}
		
		if(sdb_source.getCampaignNode() != null && sdb_source.getCampaignNode().getId() != sdb_target.getCampaignNode().getId())
		{
			info_area.setText("Source and target directories must be in the same campaign!");
			return;			
		}
		
		if(sdb_source.get_selected_hierarchy_ids().size() == 1)
		{
			// check if the source directory already exists in the target directory
			
			// get source name
			String source_name = this.specchio_client.getHierarchyName(sdb_source.get_selected_hierarchy_ids().get(0));
			
			// get target dir sub-directories
			List<spectral_node_object> sub_dirs = specchio_client.getChildrenOfNode(sdb_target.get_selected_node());		
			
			boolean directory_exists = false;
			for(spectral_node_object sub_dir : sub_dirs)
			{
				if(sub_dir.getName().equals(source_name)) directory_exists = true;
			}
			
			if(!directory_exists)
			{
				text = text + "The source-hierarchy '" + this.specchio_client.getHierarchyName(sdb_source.get_selected_hierarchy_ids().get(0)) + "' " + 
						"will be moved as a sub-hierarchy into ";
				source_content_move = false;
			}
			else
			{
				text = text + "The spectra and sub-hierarchies of the source-hierarchy '" + this.specchio_client.getHierarchyName(sdb_source.get_selected_hierarchy_ids().get(0)) + "' " + 
						"will be moved into the existing sub-directory '" + source_name + "' of";
				source_content_move = true;				
			}
			
			text = text + " the target-hierarchy '" + this.specchio_client.getHierarchyName(sdb_target.get_selected_hierarchy_ids().get(0)) + "'";
			
			
			if(!directory_exists)
			{
				text = text + "\n\n" + "Note: metadata inherited from parent hierarchies of '" + this.specchio_client.getHierarchyName(sdb_source.get_selected_hierarchy_ids().get(0)) + "' will NOT be moved!";
			}
			else
			{
				text = text + "\n\n" + "Note: metadata inherited from source hierarchy or from its parent hierarchies will NOT be moved!";
			}
			
			
			transfer_allowed = true;
			
		}
			
		
		info_area.setText(text);
		
		// update move button
		remove.setEnabled(transfer_allowed);
		
	}
		
	
	

}
