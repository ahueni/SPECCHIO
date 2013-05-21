package ch.specchio.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.metadata.MD_FormDescriptor;
import ch.specchio.metadata.MDE_FormFactory;
import ch.specchio.types.Category;


/**
 * A panel that displays a list of metadata categories and allows the
 * user to select from them.
 */
public class SpectrumMetadataCategoryList extends JPanel implements ActionListener {

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	/** the list selection listeners */
	private List<ListSelectionListener> listeners;
	
	/** the metadata form decriptor represented by the current state of the list */
	private MD_FormDescriptor descriptor;
	
	/** the panel containing the category checkboxes */
	private JPanel categoryPanel;
	
	/** the lowest category identifier */
	private int minCategoryId;
	
	/** the highest category identifier */
	private int maxCategoryId;
	
	/** the panel containing the "select all" and "select none" checkboxes */
	private JPanel selectPanel;
	
	/** the "select all" checkbox */
	private JCheckBox selectAll;
	
	/** the "select none" checkbox */
	private JCheckBox selectNone;
	
	/** the text for the "select all" checkbox */
	private static final String SELECT_ALL = "Select All";
	
	/** the text for the "select none" checkbox */
	private static final String SELECT_NONE = "Select None";
	
	
	/**
	 * Constructor.
	 * 
	 * @param mdff		the metadata form factory from which to obtain categories and forms
	 * @param columns	the number of columns in which to display the items
	 * 
	 * @throws SPECCHIOClientException	error downloading categories from the server
	 */
	public SpectrumMetadataCategoryList(MDE_FormFactory mdff, int columns) throws SPECCHIOClientException {
		
		super();
		
		// initialise member variables
		listeners = new LinkedList<ListSelectionListener>();
		descriptor = mdff.getDefaultFormDescriptor();
		
		// set up a vertical box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// create a panel with a grid bag layout for the category checkboxes
		categoryPanel = new JPanel();
		categoryPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		add(categoryPanel);
		
		// work out the number of rows required in the category panel
		int rows = descriptor.getCategories().size() / columns;
		if (descriptor.getCategories().size() % columns != 0) {
			// need an extra row to compensate for truncation in the division above
			rows++;
		}
		
		// add a checkbox for each category
		minCategoryId = Integer.MAX_VALUE;
		maxCategoryId = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		for (Category category : descriptor.getCategories()) {
			
			// create and add the check box
			JCheckBox checkbox = new JCheckBox(category.name);
			checkbox.setActionCommand(Integer.toString(category.category_id));
			checkbox.addActionListener(this);
			checkbox.setSelected(true);
			categoryPanel.add(checkbox, constraints);
			
			// update category identifier range
			if (category.category_id < minCategoryId) {
				minCategoryId = category.category_id;
			}
			if (category.category_id > maxCategoryId) {
				maxCategoryId = category.category_id;
			}
			
			// increment row and column counters
			constraints.gridy++;
			if (constraints.gridy >= rows) {
				constraints.gridx++;
				constraints.gridy = 0;
			}
			
		}
		
		// create a panel with a flow layout for the "select all" and "select none" checkboxes
		selectPanel = new JPanel();
		add(selectPanel);
		
		// add the "select all" checkbox
		selectAll = new JCheckBox(SELECT_ALL);
		selectAll.setActionCommand(SELECT_ALL);
		selectAll.addActionListener(this);
		selectAll.setSelected(true);
		selectPanel.add(selectAll);
		
		// add the "select none" checkbox
		selectNone = new JCheckBox(SELECT_NONE);
		selectNone.setActionCommand(SELECT_NONE);
		selectNone.addActionListener(this);
		selectNone.setSelected(false);
		selectPanel.add(selectNone);
		
	}
	
	
	/**
	 * Constructor for a list with one column.
	 * 
	 * @param mdef		the metadata form factory from which to obtain categories and forms
	 * 
	 * @throws SPECCHIOClientException	error downloading categories from the server
	 */
	public SpectrumMetadataCategoryList(MDE_FormFactory mdef) throws SPECCHIOClientException {
		
		this(mdef, 1);
		
	}
	
	
	/**
	 * Check box handler.
	 * 
	 * @param event	the event to be handled
	 */
	public void actionPerformed(ActionEvent event) {
		
		if (SELECT_ALL.equals(event.getActionCommand())) {
			
			// check all category checkboxes
			setAllSelected(true);
			
			// add all of the categories to the form
			for (Component c : categoryPanel.getComponents()) {
				if (c instanceof JCheckBox) {
					descriptor.addCategory(((JCheckBox)c).getText());
				}
			}
			
			// make sure that "select none" is uncheced
			selectNone.setSelected(false);
			
			// notify the list selection listeners
			fireListSelectionChanged(minCategoryId, maxCategoryId);
			
		} else if (SELECT_NONE.equals(event.getActionCommand())) {
			
			// uncheck all category checkboxes
			setAllSelected(false);
			
			// remove all categories from the form
			descriptor.clear();
			
			// make sure that "select all" is unchecked
			selectAll.setSelected(false);
			
			// notify the list selection listeners
			fireListSelectionChanged(minCategoryId, maxCategoryId);
			
		} else {
		
			// get the identifier of the category that generated the event
			int categoryId = Integer.parseInt(event.getActionCommand());
			
			// update the form object
			JCheckBox checkbox = (JCheckBox)event.getSource();
			if (checkbox.isSelected()) {
				descriptor.addCategory(checkbox.getText());
			} else {
				descriptor.removeCategory(checkbox.getText());
			}
			
			// update "select all" and "select none" checkboxes
			if (checkbox.isSelected()) {
				selectNone.setSelected(false);
			} else {
				selectAll.setSelected(false);
			}
			
			// notify the list selection listeners
			fireListSelectionChanged(categoryId);
		}
		
	}
	
	
	/**
	 * Register a list selected event listener for notifications from this list.
	 * 
	 * @param listener	the listener to be registered
	 */
	public void addListSelectionListener(ListSelectionListener listener) {
		
		listeners.add(listener);
		
	}
	
	
	/**
	 * Notify all listeners of a list selection change to one item.
	 * 
	 * @param categoryId	the identifier of the category that changed
	 */
	private void fireListSelectionChanged(int categoryId) {
		
		fireListSelectionChanged(categoryId, categoryId);
		
	}
	
	
	/**
	 * Notify all listeners of a list selection change to multiple items.
	 * 
	 * @param firstCategoryId	the lowest category identifier to have changed
	 * @param lastCategoryId	the highest category identifier to have changed
	 */
	private void fireListSelectionChanged(int firstCategoryId, int lastCategoryId) {
		
		ListSelectionEvent event = new ListSelectionEvent(this, firstCategoryId, lastCategoryId, false);
		for (ListSelectionListener listener : listeners) {
			listener.valueChanged(event);
		}
			
	}
	
	
	/**
	 * Get a form descriptor representing the current selection.
	 * 
	 * @return  an MDE_Form object representing the current selection
	 */
	public MD_FormDescriptor getFormDescriptor() {
		
		return descriptor;
		
	}
	
	
	/**
	 * Set the selection state of all of the category checkboxes.
	 * 
	 * @param selected	true or false
	 */
	public void setAllSelected(boolean selected) {

		for (Component c : categoryPanel.getComponents()) {
			if (c instanceof JCheckBox) {
				((JCheckBox)c).setSelected(selected);
			}
		}
		
	}

}
