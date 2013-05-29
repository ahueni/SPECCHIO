package ch.specchio.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendar;
import org.freixas.jcalendar.JCalendarCombo;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.metadata.MDE_Form;
import ch.specchio.metadata.MD_CategoryContainer;
import ch.specchio.metadata.MD_ChangeListener;
import ch.specchio.metadata.MD_EAV_Field;
import ch.specchio.metadata.MD_Field;
import ch.specchio.metadata.MD_Spectrum_Field;
import ch.specchio.types.Capabilities;
import ch.specchio.types.CategoryTable;
import ch.specchio.types.MetaDate;
import ch.specchio.types.MetaDocument;
import ch.specchio.types.MetaFile;
import ch.specchio.types.MetaImage;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.MetaTaxonomy;
import ch.specchio.types.SerialisableBufferedImage;
import ch.specchio.types.TaxonomyNodeObject;
import ch.specchio.types.attribute;

/**
 * Spectrum metadata panel. This panel displays all of the metadata for a spectrum.
 */
public class SpectrumMetadataPanel extends JPanel {

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	/** the frame that owns this panel */
	private Frame owner;
	
	/** the client object */
	private SPECCHIOClient specchioClient;
	
	/** the metadata form */
	private MDE_Form form;
	
	/** the list of metadata change listeners */
	private List<MD_ChangeListener> listeners;
	
	/** is the panel editable? */
	private boolean editable;
	
	
	/**
	 * Constructor.
	 * 
	 * @param owner				the frame that owns this panel
	 * @param specchioClient	the client object to use for contacting the server
	 */
	public SpectrumMetadataPanel(Frame owner, SPECCHIOClient specchioClient) {
		
		super();
		
		// save a reference to the parameters for later
		this.owner = owner;
		this.specchioClient = specchioClient;
		
		// initialise member variables
		form = null;
		listeners = new LinkedList<MD_ChangeListener>();
		editable = true;
		
		
		// set up vertical box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
	}
	
	
	/**
	 * Register a metadata change listener.
	 * 
	 * @param listener	the listener to be added
	 */
	public void addMetadataChangeListener(MD_ChangeListener listener) {
		
		listeners.add(listener);
		
	}
	
	
	/**
	 * Fire the "metadata added" event.
	 * 
	 * @param field	the field that was added
	 */
	private void fireMetadataFieldAdded(MD_Field field) {
		
		// notify all listeners
		for (MD_ChangeListener listener : listeners) {
			listener.metadataFieldAdded(field);
		}
		
	}
	
	
	/**
	 * Fire the "metadata changed" event.
	 * 
	 * @param field	the field that changed
	 * @param value	the new value of the field
	 */
	private void fireMetadataFieldChanged(MD_Field field, Object value) {
		
		// notify all listeners
		for (MD_ChangeListener listener : listeners) {
			listener.metadataFieldChanged(field, value);
		}
		
	}
	
	
	/**
	 * Fire the "metadata removed" event.
	 * 
	 * @param field	the field that was removed
	 */
	private void fireMetadataFieldRemoved(MD_Field field) {
		
		// notify all listeners
		for (MD_ChangeListener listener : listeners) {
			listener.metadataFieldRemoved(field);
		}
		
	}
	
	
	/**
	 * Get the metadata form associated with this panel.
	 * 
	 * @return a reference to the form
	 */
	public MDE_Form getForm() {
		
		return form;
		
	}
	
	
	/**
	 * Set whether or not the metadata in the panel can be edited.
	 * 
	 * @param editable	whether or not the fields in the panel should be editable
	 */
	public void setEditable(boolean editable) {
		
		// save the value of the flag for later
		this.editable = editable;
		
		// propagate the setting to every category container
		for (Component c : getComponents()) {
			if (c instanceof SpectrumMetadataCategoryContainer) {
				((SpectrumMetadataCategoryContainer)c).setEditable(editable);
			}
		}
		
	}
	
	
	/**
	 * Set the metadata form to be displayed by the panel.
	 * 
	 * @param form	the form
	 * 
	 * @throws SPECCHIOClientException	could not contact the server
	 */
	public void setForm(MDE_Form form) throws SPECCHIOClientException {
		
		// remove all of the existing components
		removeAll();
		
		if (form != null) {
			
			// create and add panels for each category container
			for (MD_CategoryContainer mdcc : form.getContainers()) {
				SpectrumMetadataCategoryContainer panel = new SpectrumMetadataCategoryContainer(mdcc);
				panel.setEditable(editable);
				add(panel);
			}
			
		}
		
		// save a reference to the new form
		this.form = form;
		
		// force re-draw
		revalidate();
		repaint();
		
	}
	
	
	/**
	 * Category container panel.
	 */
	private class SpectrumMetadataCategoryContainer extends JPanel implements ActionListener, MouseListener {

		/** serialisation version identifier */
		private static final long serialVersionUID = 1L;
		
		/** metadata category container */
		private MD_CategoryContainer mdcc;
		
		/** inner panel */
		private JPanel fieldPanel;
		
		/** component factory */
		private SpectrumMetadataComponentFactory factory;
		
		/** popup menu */
		private JPopupMenu popupMenu;
		
		/** is the container editable? */
		private boolean editable;
		
		/** string for "add" menu item */
		private static final String ADD = "Add";
		
		/** string "attribute" client property */
		private static final String ATTRIBUTE = "Attribute";

		
		/**
		 * Constructor.
		 * 
		 * @param mdcc	the category container to be displayed in this panel
		 * 
		 * @throws SPECCHIOClientException	could not contact the server
		 */
		public SpectrumMetadataCategoryContainer(MD_CategoryContainer mdcc) throws SPECCHIOClientException {
			
			super();
			
			// save a reference to the parameters
			this.mdcc = mdcc;
			
			// initialise member variables
			editable = true;
			
			// add a border with the category name
			Border blackline = BorderFactory.createLineBorder(Color.BLACK);
			TitledBorder tb = BorderFactory.createTitledBorder(blackline, mdcc.getCategoryName());
			setBorder(tb);
			
			// create a panel to hold the fields
			fieldPanel = new JPanel();
			fieldPanel.setLayout(new AlignedBoxLayout(fieldPanel, AlignedBoxLayout.Y_AXIS));
			add(fieldPanel);
			
			// set up popup menu
			popupMenu = new JPopupMenu();
			for (attribute a : mdcc.getPossibleEAVFields()) {
				addMenuItem(a);
			}
			addMouseListener(this);
			
			// add fields
			factory = new SpectrumMetadataComponentFactory(this);
			for (MD_Field field : mdcc.getFields()) {
				addField(field);
			}
			
		}
		
		
		/**
		 * Menu selection handler.
		 *
		 * @param event	the event to be handled
		 */
		public void actionPerformed(ActionEvent event) {
			
			JMenuItem menuItem = (JMenuItem)event.getSource();
			
			if (ADD.equals(event.getActionCommand())) {
				
				try {
				
					// create a new field for this attribute
					attribute a = (attribute)menuItem.getClientProperty(ATTRIBUTE);
					MetaParameter mp = MetaParameter.newInstance(a);
					mp.setUnits(specchioClient.getAttributeUnits(a));
					MD_EAV_Field field = getForm().createEAVField(mp);
					
					Object value = null;
					if (mp instanceof MetaFile) {
						// need to load the value from a file
						value = loadMetaFileValue((MetaFile)mp);
						if (value != null) {
							// copy the value into the field
							field.getMetaParameter().setValue(value);
						} else {
							// loading was aborted; prevent addition of the field
							field = null;
						}
						
					}
				
					if (field != null) {
						
						// add the new field to the category container
						mdcc.addField(field);
							
						// add the new field to the panel
						addField(field);
						
						// notify the metadata change listeners
						fireMetadataFieldAdded(field);
						if (mp instanceof MetaFile) {
							// adding a file changes the value as well
							fireMetadataFieldChanged(field, value);
						}
						
					}
				
					// force re-draw
					revalidate();
					repaint();
				
				}
				catch (SPECCHIOClientException ex) {
					// error contacting the server
					ErrorDialog error = new ErrorDialog(owner, "Could not create field", ex.getUserMessage(), ex);
					error.setVisible(true);
				}
				catch (MetaParameterFormatException ex) {
					// should never happen
					ex.printStackTrace();
				}
				
			}
			
		}
		
		
		/**
		 * Add a field to the panel.
		 * 
		 * @param field	the field
		 * 
		 * @return the component that represents the new field
		 * 
		 * @throws SPECCHIOClientException error contacting the server
		 */
		private SpectrumMetadataComponent addField(MD_Field field) throws SPECCHIOClientException {
			
			// create a component for this field
			SpectrumMetadataComponent c = factory.newComponent(field);
			fieldPanel.add(c);
			
			if (field instanceof MD_EAV_Field) {
				// remove this field from the pop-up menu
				MetaParameter mp = ((MD_EAV_Field)field).getMetaParameter();
				for (int i = 0; i < popupMenu.getComponentCount(); i++) {
					JMenuItem menuItem = (JMenuItem)popupMenu.getComponent(i);
					attribute a = (attribute)menuItem.getClientProperty(ATTRIBUTE);
					if (a.id == mp.getAttributeId()) {
						popupMenu.remove(i);
						break;
					}
				}
			}
			
			if(field.get_conflict_status() == 2)
			{
				c.setEnabled(false); // ambiguous field
			}
			
			return c;
			
		}
		
		
		/**
		 * Add an option to add an attribute to the pop-up menu.
		 * 
		 * @param a	the attribute
		 */
		private void addMenuItem(attribute a) {
			
			JMenuItem menuItem = new JMenuItem(ADD + " " + a.getName());
			menuItem.setActionCommand(ADD);
			menuItem.addActionListener(this);
			menuItem.putClientProperty(ATTRIBUTE,  a);
			popupMenu.add(menuItem);
			
		}
			
			
		
		
		/**
		 * Load the value of a file-type meta-parameter
		 * 
		 * @param mp	the meta-parameter to be loaded
		 * 
		 * @return the object read from the file, or null if the user cancelled or there was an error
		 */
		private Object loadMetaFileValue(MetaFile mp)  {
			
			// create a file chooser
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				
				try {
					File file = fc.getSelectedFile();
					
					// check that the file will be accepted by the server
					Long maxObjectSize = Long.parseLong(specchioClient.getCapability(Capabilities.MAX_OBJECT_SIZE));
					if (file.length() > maxObjectSize) {
						String message =
								"Your file (" + file.length() + " bytes) exceeds " +
								"the maximum size permitted by the server (" + maxObjectSize + " bytes).";
						JOptionPane.showMessageDialog(owner, message, "File too big", JOptionPane.ERROR_MESSAGE);
						return null;
					}
					
					// read the value from the file
					MimetypesFileTypeMap mimetypes = new MimetypesFileTypeMap();
					FileInputStream fis = new FileInputStream(file);
					mp.readValue(fis, mimetypes.getContentType(file));
					
					return mp.getValue();
				}
				catch (IOException ex) {
					// read error
					JOptionPane.showMessageDialog(owner, ex.getMessage(), "Could not read file", JOptionPane.ERROR_MESSAGE);
					return null;
				}
				catch (SPECCHIOClientException ex) {
					// could not establish the maximum file size
					ErrorDialog error = new ErrorDialog(owner, "Could not get the maximum upload size", ex.getUserMessage(), ex);
					error.setVisible(true);
					return null;
				}
				
			} else {
				
				// the user cancelled the file chooser
				return null;
				
			}
			
		}
		
		
		/**
		 * Respond to a mouse click. Does nothing.
		 * 
		 * @param event	the event to be handled
		 */
		public void mouseClicked(MouseEvent event) {
			
			// do nothing
			
		}
		
		
		/**
		 * Respond to the mouse entering the panel. Does nothing.
		 * 
		 * @param event	the event to be handled
		 */
		public void mouseEntered(MouseEvent event) {
			
			// do nothing
			
		}
		
		
		/**
		 * Respond to the mouse exiting the panel. Does nothing.
		 * 
		 * @param event	the event to be handled
		 */
		public void mouseExited(MouseEvent event) {
			
			// do nothing
			
		}
		
		
		/**
		 * Respond to a mouse button press.
		 * 
		 * @param event	the event to be handled
		 */
		public void mousePressed(MouseEvent event) {
			
			if (editable && event.isPopupTrigger()) {
				popupMenu.show(this, event.getX(), event.getY());
			}
			
		}
		
		
		/**
		 * Respond to a mouse button release.
		 * 
		 * @param event	the event to be handled
		 */
		public void mouseReleased(MouseEvent event) {
			
			if (editable && event.isPopupTrigger()) {
				popupMenu.show(this, event.getX(), event.getY());
			}
			
		}
		
		
		/**
		 * Remove a field from the category.
		 * 
		 * @param field	the field to be removed
		 */
		public void removeField(MD_Field field) {
			
			// remove the field's GUI component
			for (int i = 0; i < fieldPanel.getComponentCount(); i++) {
				SpectrumMetadataComponent c = (SpectrumMetadataComponent)fieldPanel.getComponent(i);
				if (c.getField() == field) {
					fieldPanel.remove(i);
					break;
				}
			}
			
			if (field instanceof MD_EAV_Field) {
				// add an option to re-add the field to the pop-up menu
				MetaParameter mp = ((MD_EAV_Field)field).getMetaParameter();
				for (attribute a : mdcc.getPossibleEAVFields()) {
					if (a.id == mp.getAttributeId()) {
						addMenuItem(a);
						break;
					}
				}
				
			}
			
			// notify the metadata change listeners
			fireMetadataFieldRemoved(field);
			
			// force re-draw
			revalidate();
			repaint();
			
		}
		
		
		/**
		 * Make the panel editable or not editable.
		 * 
		 * @param editable	true to make the panel editable, false otherwise
		 */
		public void setEditable(boolean editable) {
			
			// save the flag for later
			this.editable = editable;
			
			// propagate the setting to every field
			for (Component c : fieldPanel.getComponents()) {
				if (c instanceof SpectrumMetadataComponent) {
					((SpectrumMetadataComponent)c).setEditable(editable);
				}
			}
			
		}
		
	}
	
	
	/**
	 * Factory class for creating spectrum metadata components
	 */
	private class SpectrumMetadataComponentFactory {
		
		/** the category container panel */
		private SpectrumMetadataCategoryContainer container;
		
		
		/**
		 * Constructor.
		 * 
		 * @param container	the category container panel to which components will belong
		 */
		public SpectrumMetadataComponentFactory(SpectrumMetadataCategoryContainer container) {
			
			// save a reference to the parameiters for later
			this.container = container;
			
		}
		
		
		/**
		 * Create a new component for a spectrum field.
		 * 
		 * @param field	the metadata field to be represented by the new component
		 *
		 * @return a SpectrumMetadataComponent corresponding to the input field
		 * 
		 * @throws SPECCHIOClientException	could not contact the server
		 */
		public SpectrumMetadataComponent newComponent(MD_Field field) throws SPECCHIOClientException {
			
			if (field instanceof MD_Spectrum_Field) {
				return newFieldComponent((MD_Spectrum_Field)field);
			} else if (field instanceof MD_EAV_Field) {
				return newEavComponent((MD_EAV_Field)field);
			} else {
				// this should never happen
				return null;
			}
			
		}
		
		
		/**
		 * Create a new component for an EAV field.
		 * 
		 * @param field	the metadata field to be represented by the new component
		 *
		 * @return a new SpectrumEavMetadataComponent corresponding to the new field
		 * 
		 * @throws SPECCHIOClientException	could not contact the server
		 */
		private SpectrumEavMetadataComponent newEavComponent(MD_EAV_Field field) throws SPECCHIOClientException {
			
			// the component type depends on the meta-parameter's type
			MetaParameter mp = field.getMetaParameter();
			if (mp instanceof MetaTaxonomy) {
				return new SpectrumTaxonomyEavMetadataComponent(container, field);
			} else if (mp instanceof MetaDate) {
				return new SpectrumDateEavMetadataComponent(container, field);
			} else if (mp instanceof MetaImage) {
				return new SpectrumImageEavMetadataComponent(container, field);
			} else if (mp instanceof MetaDocument) {
				return new SpectrumFileEavMetadataComponent(container, field);
			} else {
				return new SpectrumSimpleEavMetadataComponent(container, field);
			}
			
		}
		
		
		/**
		 * Create a new component for a non-EAV field.
		 * 
		 * @param field	the metadata field to be represented by the new component
		 *
		 * @return a new SpectrumFieldMetadataComponent corresponding to the new field
		 */
		private SpectrumFieldMetadataComponent newFieldComponent(MD_Spectrum_Field field) {
			
			return new SpectrumFieldMetadataComponent(container, field);
			
		}
		
	}
	
	
	/**
	 * Base class for all spectrum metadata fields.
	 */
	private abstract class SpectrumMetadataComponent extends JPanel implements AlignedBoxLayout.AlignedBox {
		
		/** serialisation version identifier */
		private static final long serialVersionUID = 1L;

		/** the category container panel to which this component belongs */
		private SpectrumMetadataCategoryContainer container;
		
		/** the metadata field represented by this component */
		private MD_Field field;
		
		/** the label of this component */
		private JLabel label;
		
		/**
		 * Constructor.
		 * 
		 * @param container	the category container panel to which this component belongs
		 * @param field		the metadata field represented by this component
		 */
		public SpectrumMetadataComponent(SpectrumMetadataCategoryContainer container, MD_Field field) {
			
			// save a reference to the parameters
			this.container = container;
			this.field = field;
						
			// create a label for this component
			label = new JLabel(field.getLabelWithUnit());
			label.setHorizontalAlignment(JLabel.RIGHT);
			add(label);
			
		}
		
		
		/**
		 * Get the category container panel to which this component belongs
		 * 
		 * @return a reference to the category container panel
		 */
		public SpectrumMetadataCategoryContainer getCategoryContainerPanel() {
			
			return container;
			
		}
		
		
		/**
		 * Get the label of this panel.
		 * 
		 * @return the label
		 */
		public JLabel getLabel() {
			
			return label;
			
		}
		
		
		/**
		 * Get the field represented by this component.
		 * 
		 * @return a reference to the MD_Field object with which this component was contructed
		 */
		public MD_Field getField() {
			
			return field;
			
		}
		
		
		/**
		 * Get the horizontal alignment position. 
		 * 
		 * @return the preferred width of the label
		 */
		public int getXAlignmentPosition() {
			
			Dimension dim = label.getPreferredSize();
			
			return dim.width;
			
		}
		
		/**
		 * Get the vertical alignment position. Not used.
		 * 
		 * @return 0
		 */
		public int getYAlignmentPosition() {
			
			return 0;
			
		}
		
		
		/**
		 * Make the field editable or not.
		 * 
		 * @param editable true to make the field editable, false otherwise
		 */
		public abstract void setEditable(boolean editable);
		
		
		/**
		 * Enable or disable the field.
		 *
		 * @param enabled	true or false
		 */
		public abstract void setEnabled(boolean enabled);
		
	}
	
	
	/**
	 * Component for non-EAV metadata fields.
	 */
	private class SpectrumFieldMetadataComponent extends SpectrumMetadataComponent implements ActionListener {
		
		/** serialisation version identifier */
		private static final long serialVersionUID = 1L;
		
		/** the combo box, used for an editable field */
		private JComboBox box;
		
		/** the text field, used for a non-editable field */
		private JTextField text;
		
		/**
		 * Constructor.
		 * 
		 * @param container	the category container panel to which this component belongs
		 * @param field	the field to be represented by this component
		 */
		public SpectrumFieldMetadataComponent(SpectrumMetadataCategoryContainer container, MD_Spectrum_Field field) {
			
			super(container, field);
			
			// build combobox
			box = new JComboBox();
			combo_table_data nil_item = new combo_table_data("NIL", 0);
			box.addItem(nil_item);
			
			// fill the box with tiems
			CategoryTable items = field.getCategoryValues();
			Enumeration<Integer> e = items.keys();
			while(e.hasMoreElements())
			{
				Integer key = e.nextElement();					
				String value = items.get(key);
				combo_table_data cdt = new combo_table_data(value, key);
				box.addItem(cdt);
				if(key == field.getId())
				{
					box.setSelectedItem(cdt);
				}
			}
			
			if(field.get_conflict_status() != 1)
			{
				box.setSelectedItem(nil_item); // NIL field
			}
			
			// add the box to the panel
			add(box);
			box.addActionListener(this);
			
			// create the label but don't display it yet
			text = new JTextField(box.getSelectedItem().toString(), 20);
			text.setEditable(false);
			
		}
		
		
		/**
		 * Combo box selection handler.
		 * 
		 * @param	the event
		 */
		public void actionPerformed(ActionEvent event) {
			
			combo_table_data cdt = (combo_table_data) box.getSelectedItem();
			fireMetadataFieldChanged(getField(), cdt);	
			getField().setNewValue(cdt.id);
			
		}
		
		
		/**
		 * Make the field editable or not.
		 * 
		 * @param editable true to make the field editable, false otherwise
		 */
		public void setEditable(boolean editable) {
			
			if (editable) {
				// display the combo box
				remove(text);
				add(box);
			} else {
				// display the label
				remove(box);
				add(text);
			}
			
			// force re-draw
			revalidate();
			repaint();
			
		}
		
		
		/**
		 * Enable or disable the field.
		 *
		 * @param enabled	true or false
		 */
		public void setEnabled(boolean enabled) {
			
			box.setEnabled(enabled);
			text.setEnabled(enabled);
			
		}
		
	}
	
	
	/**
	 * Base class for EAV metadata components.
	 */
	private abstract class SpectrumEavMetadataComponent extends SpectrumMetadataComponent implements ActionListener, MouseListener {
		
		/** serialisation version identifier */
		private static final long serialVersionUID = 1L;

		/** pop-up menu for deleting the field */
		private JPopupMenu popupMenu;
		
		/** is the component editable? */
		private boolean editable;
		
		/** text for the "delete" menu item */
		private static final String DELETE = "Delete";
		
		/**
		 * Constructor.
		 * 
		 * @param container	the category container panel to which this component belongs
		 * @param field		the metadata field represented by this component
		 */
		public SpectrumEavMetadataComponent(SpectrumMetadataCategoryContainer container, MD_EAV_Field field) {
			
			super(container, field);
			
			// initialise member variables
			editable = true;
			
			// create the popup menu and listen for mouse clicks
			popupMenu = new JPopupMenu();
			getLabel().addMouseListener(this);
			
			// add the "delete" menu option
			JMenuItem menuItem = new JMenuItem(DELETE);
			menuItem.addActionListener(this);
			popupMenu.add(menuItem);				
			
		}
		
		
		/**
		 * Menu selection handler.
		 *
		 * @param event	the event to be handled
		 */
		public void actionPerformed(ActionEvent event) {
			
			if (DELETE.equals(event.getActionCommand())) {
				
				// remove the component from its container
				getCategoryContainerPanel().removeField(getField());
				
			}
			
		}
		
		
		/**
		 * Get the metaparameter corresponding to this component.
		 * 
		 * @return a reference to the field's MetaParameter object
		 */
		public MetaParameter getMetaParameter() {
			
			return ((MD_EAV_Field)getField()).getMetaParameter();
			
		}
		
		
		/**
		 * Test whether or not the associated field has multiple values.
		 * 
		 * @return true if the field's conflict status is 1 or 3
		 */
		public boolean fieldHasMultipleValues() {
			
			return getField().get_conflict_status() != 1 && getField().get_conflict_status() != 3;
			
		}
		
		
		/**
		 * Respond to a mouse click. Does nothing.
		 * 
		 * @param event	the event to be handled
		 */
		public void mouseClicked(MouseEvent event) {
			
			// do nothing
			
		}
		
		
		/**
		 * Respond to the mouse entering the panel. Does nothing.
		 * 
		 * @param event	the event to be handled
		 */
		public void mouseEntered(MouseEvent event) {
			
			// do nothing
			
		}
		
		
		/**
		 * Respond to the mouse exiting the panel. Does nothing.
		 * 
		 * @param event	the event to be handled
		 */
		public void mouseExited(MouseEvent event) {
			
			// do nothing
			
		}
		
		
		/**
		 * Respond to a mouse button press.
		 * 
		 * @param event	the event to be handled
		 */
		public void mousePressed(MouseEvent event) {
			
			if (editable && event.isPopupTrigger()) {
				popupMenu.show(this, event.getX(), event.getY());
			}
			
		}
		
		
		/**
		 * Respod to a mouse button release.
		 * @param event	the event to be handled
		 */
		public void mouseReleased(MouseEvent event) {
			
			if (editable && event.isPopupTrigger()) {
				popupMenu.show(this, event.getX(), event.getY());
			}
			
		}
		
		
		/**
		 * Make the component editable or not.
		 * 
		 * @param editable	true if the component can be edited
		 */
		public void setEditable(boolean editable) {
			
			this.editable = editable;
			
		}
		
		
		/**
		 * Enable or disable the component.
		 * 
		 * @param enabled	true if the component is enabled
		 */
		public void setEnabled(boolean enabled) {
			
			// disable pop-up menus if the component is disabled
			if (!enabled) {
				this.editable = false;
			}
			
		}
	
	}


	/**
	 * Component for manipulating date-type metadata field.
	 */
	private class SpectrumDateEavMetadataComponent extends SpectrumEavMetadataComponent implements DateListener {
		
		/** serialisation version identifier */
		private static final long serialVersionUID = 1L;
		
		/** calendar control, used for editable components */
		JCalendarCombo cal_combo;
		
		/** text field, user for non-editable components */
		JTextField text;
			
		/**
		 * Constructor.
		 *
		 * @param container	the category container panel to which this component belongs
		 * @param field	the metadata field represented by this component
		 */
		public SpectrumDateEavMetadataComponent(SpectrumMetadataCategoryContainer container, MD_EAV_Field field) {
				
			super(container, field);
			
			// set up date and format
			Date date = (Date)field.getMetaParameter().getValue();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			// build the calendar control
			cal_combo = new JCalendarCombo(JCalendar.DISPLAY_DATE | JCalendar.DISPLAY_TIME,  false);			
			cal_combo.setDateFormat(format);
			cal_combo.setDate(date);
			cal_combo.addDateListener(this);
			
			// build the text field
			text = new JTextField(format.format(date), 20);
			text.setEditable(false);
			
			// start with the combo box on display
			add(cal_combo);
			
		}
				
		
		/**
		 * Date changed handler.
		 * 
		 * @param the event to be handled
		 */
		public void dateChanged(DateEvent event) {	
			
			java.util.Date date = cal_combo.getDate();
			fireMetadataFieldChanged(getField(), date);	
			
		}
		
		
		/**
		 * Make the field editable or not.
		 * 
		 * @param editable true to make the field editable, false otherwise
		 */
		public void setEditable(boolean editable) {
			
			super.setEditable(editable);
			if (editable) {
				// display the calendar combo
				remove(text);
				add(cal_combo);
			} else {
				// display the text field
				remove(cal_combo);
				add(text);
			}
			
			// force re-draw
			revalidate();
			repaint();
			
		}
		
		
		/**
		 * Enable or disable the field.
		 *
		 * @param enabled	true or false
		 */
		public void setEnabled(boolean enabled) {

			super.setEnabled(enabled);
			cal_combo.setEnabled(enabled && !fieldHasMultipleValues());
			text.setEnabled(enabled && !fieldHasMultipleValues());
			
		}
		
	}
	
	
	/**
	 * Component for file-type metadata.
	 */
	private class SpectrumFileEavMetadataComponent extends SpectrumEavMetadataComponent implements ActionListener {
		
		/** serialisation version identifier */
		private static final long serialVersionUID = 1L;
		
		/** the "view" button */
		private JButton viewButton;
		
		/** text for the "view" button */
		private static final String VIEW = "View";
		
		
		/**
		 * Constructor.
		 * 
		 * @param container	the category container panel to which this component belongs
		 * @param field	the metadata field represented by this component
		 */
		public SpectrumFileEavMetadataComponent(SpectrumMetadataCategoryContainer container, MD_EAV_Field field) {
			
			super(container, field);
			
			if(!fieldHasMultipleValues())
			{
				viewButton = new JButton(VIEW);
				viewButton.setActionCommand(VIEW);
				viewButton.addActionListener(this);
			}
			else
			{
				viewButton = new JButton("-- multiple documents --");
				viewButton.setEnabled(false);
			}
			
			add(viewButton);
			
		}
		
		
		/**
		 * Button handler.
		 * 
		 * @param event	the event to be handled
		 */
		public void actionPerformed(ActionEvent event) {
			
			if (VIEW.equals(event.getActionCommand())) {
				
				try {
					MetaFile mp_file = (MetaFile)getMetaParameter();
						
					// write the meta-parameter value to a temporary file
					File temp = File.createTempFile("specchio", mp_file.getDefaultFilenameExtension());
					temp.deleteOnExit();
					FileOutputStream fos = new FileOutputStream(temp);
					mp_file.writeValue(fos);
					fos.close();
						
					// launch the external viewer
					Desktop.getDesktop().open(temp);
				}
				catch (IllegalArgumentException ex) {
					// something wrong with the temporary file
					ErrorDialog error = new ErrorDialog(owner, "Could not start viewer", ex.getMessage(), ex);
					error.setVisible(true);
				}
				catch (IOException ex) {
					// no viewer found for this file type
					ErrorDialog error = new ErrorDialog(owner, "Could not start viewer", ex.getMessage(), ex);
					error.setVisible(true);
				}
			
			} else {
				
				// pass pop-up menu actions to the super-class
				super.actionPerformed(event);
				
			}
		}
		
		
		/**
		 * Make the field editable or not.
		 * 
		 * @param editable true to make the field editable, false otherwise
		 */
		public void setEditable(boolean editable) {
			
			super.setEditable(editable);
			
		}
		
		
		/**
		 * Enable or disable the field.
		 *
		 * @param enabled	true or false
		 */
		public void setEnabled(boolean enabled) {

			super.setEnabled(enabled);
			viewButton.setEnabled(enabled && !fieldHasMultipleValues());
			
		}
		
	}
	
	
	
	/**
	 * Component for image-type metadata.
	 */
	private class SpectrumImageEavMetadataComponent extends SpectrumEavMetadataComponent {
		
		/** serialisation version identifier */
		private static final long serialVersionUID = 1L;
		
		/** the image panel */
		private ImagePanel ip;
		
		/** the label for multiple images */
		private JLabel label;
		
		
		/**
		 * Constructor.
		 * 
		 * @param container	the category container panel to which this component belongs
		 * @param field	the metadata field represented by this component
		 */
		public SpectrumImageEavMetadataComponent(SpectrumMetadataCategoryContainer container, MD_EAV_Field field) {
			
			super(container, field);
			
			// initialise member variables
			ip = null;
			label = null;
			
			if(!fieldHasMultipleValues()) {
				
				try {
					ip = new ImagePanel();
					ip.image = (SerialisableBufferedImage)field.getMetaParameter().getValue();
					BufferedImage image = ip.image.getImage();
					if (image != null) {
						int im_width = ip.image.getImage().getWidth();
						int im_height = ip.image.getImage().getHeight();
						int width = 300;
						float factor = im_height / (im_width*1.0f);
						
						int heigth = (int) (width*factor);
						ip.setSize(width, heigth);
						ip.setPreferredSize(new Dimension(width , heigth));
						
						add(ip);
					} else {
						label = new JLabel("-- image data not available --");
						add(label);
					}
				}
				catch (ClassNotFoundException ex) {
					// no image decoder found; replace the image with an error label
					label = new JLabel("-- no image decoder found --");
					add(label);
				}
				catch (IOException ex) {
					// error decoding the image; replace it with an error label
					label = new JLabel("-- error decoding image --");
					add(label);
					
				}
				
			} else {
				
				label = new JLabel("-- multiple pictures --");
				add(label);
				
			}
			
		}
		
		
		/**
		 * Make the field editable or not.
		 * 
		 * @param editable true to make the field editable, false otherwise
		 */
		public void setEditable(boolean editable) {
			
			super.setEditable(editable);
			
		}
		
		
		/**
		 * Enable or disable the field.
		 *
		 * @param enabled	true or false
		 */
		public void setEnabled(boolean enabled) {

			super.setEnabled(enabled);
			if (ip != null) {
				ip.setEnabled(enabled);
			}
			
		}
		
	}
	
	
	/**
	 * Component for simple metadata fields.
	 */
	private class SpectrumSimpleEavMetadataComponent extends SpectrumEavMetadataComponent implements DocumentListener {
		
		/** serialisation version identifier */
		private static final long serialVersionUID = 1L;
		
		/** the text field */
		private JTextComponent text;
		
		
		/**
		 * Constructor.
		 * 
		 * @param container	the category container panel to which this component belongs
		 * @param field	the metadata field represented by this component
		 */
		public SpectrumSimpleEavMetadataComponent(SpectrumMetadataCategoryContainer container, MD_EAV_Field field) {
			
			super(container, field);
			
			// create a text component of type appropriate to the value
			Object value = getMetaParameter().getValue();
			if (value instanceof Integer || value instanceof Double) {
				
				// value is a number; use a text field
				text = new KeylistenerTextFieldNumeric(20);
				((KeylistenerTextFieldNumeric)text).setAllowFloats(value instanceof Double);
				
			} else if (value instanceof String) {
				
				// value is a string; use a text area
				text = new JTextArea(1, 30);
				((JTextArea) text).setLineWrap(true);
				
			} else {
				
				// don't know what to do with this; let's just use a text field
				text = new JTextField(20);
				
			}
			
			// initialise the text component's contents
			if(!fieldHasMultipleValues())
			{
				text.setText((value != null)? value.toString() : null);
			}
			else
			{
				text.setText("-- multiple values --");
			}
			
			// set up event handler
			text.getDocument().addDocumentListener(this);
			
			// add the text component to the panel
			add(text);
			
		}


		/**
		 * Handle updates to the text component.
		 * 
		 * @param event	the event to handle
		 */
		public void changedUpdate(DocumentEvent event) {
			
			Object newValue = getValidatedInput();
			if (newValue != null) {
				fireMetadataFieldChanged(getField(), newValue);
			}
			
		}
		
		
		/**
		 * Convert the contents of the input field into the appropriate Java type.
		 * 
		 * @return an Object containing the parsed value, or null if it is not legal input
		 */
		private Object getValidatedInput() {
			
			Object oldValue = getMetaParameter().getValue();
			Object newValue = null;
			String input = text.getText();
			if (oldValue instanceof Integer) {
				try {
					newValue = Integer.parseInt(input);
				}
				catch (NumberFormatException ex) {
					// nothing to do because newValue is already null
				}
			} else if (oldValue instanceof Double) {
				try {
					newValue = Double.parseDouble(input);
				}
				catch (NumberFormatException ex) {
					// nothing to do because newValue is already null
				}
			} else {
				// should be a string
				newValue = input;
			}
			
			return newValue;
			
		}


		/**
		 * Handle insertions in the text component.
		 * 
		 * @param event	the event to handle
		 */
		public void insertUpdate(DocumentEvent event) {
			
			Object newValue = getValidatedInput();
			if (newValue != null) {
				fireMetadataFieldChanged(getField(), newValue);
			}
			
		}


		/**
		 * Handle deletions from the text component.
		 * 
		 * @param event	the event to handle
		 */
		public void removeUpdate(DocumentEvent event) {
			
			Object newValue = getValidatedInput();
			if (newValue != null) {
				fireMetadataFieldChanged(getField(), newValue);
			}
			
		}
		
		
		/**
		 * Make the field editable or not.
		 * 
		 * @param editable true to make the field editable, false otherwise
		 */
		public void setEditable(boolean editable) {
			
			super.setEditable(editable);
			text.setEditable(editable);
			
		}
		
		
		/**
		 * Enable or disable the field.
		 *
		 * @param enabled	true or false
		 */
		public void setEnabled(boolean enabled) {

			super.setEnabled(enabled);
			text.setEnabled(enabled && !fieldHasMultipleValues());
			
		}
		
	}
	
	
	/**
	 * Component for manipulating taxonomies.
	 */
	private class SpectrumTaxonomyEavMetadataComponent extends SpectrumEavMetadataComponent implements ActionListener {
		
		/** serialisation version identifier */
		private static final long serialVersionUID = 1L;
		
		/** the "select" button */
		private JButton selectButton;
		
		/** the text field for non-editable display */
		private JTextField text;
		
		/** action command for the "select" button */
		private static final String SELECT = "Select";
		
		
		/**
		 * Constructor.
		 * 
		 * @param container	the category container panel to which this component belongs
		 * @param field	the metadata field represented by this component
		 * 
		 * @throws SPECCHIOClientException	could not contact the server
		 */
		public SpectrumTaxonomyEavMetadataComponent(SpectrumMetadataCategoryContainer container, MD_EAV_Field field) throws SPECCHIOClientException {
			
			super(container, field);

			String displayString = "NIL";
			if(!fieldHasMultipleValues()) {
				
				// get the taxonomy name
				MetaTaxonomy mp = (MetaTaxonomy)field.getMetaParameter();
				Integer id = ((Long) mp.getValue()).intValue();
				if(id != 0)
				{
					TaxonomyNodeObject taxonomy = specchioClient.getTaxonomyNode(id);
					displayString = taxonomy.getName();
				}
				selectButton = new JButton(displayString);
			
			}
			else
			{
				displayString = "-- multiple taxa --";
				selectButton = new JButton(displayString);
				selectButton.setEnabled(false);
			}
												
			// create a button for selecting the taxonomy
			selectButton.setActionCommand(SELECT);
			selectButton.addActionListener(this);
			add(selectButton);
			
			// create a text field but don't display it yet
			text = new JTextField(displayString, 30);
			text.setEditable(false);
			
			
		}
		
		
		/**
		 * Button handler.
		 * 
		 * @param	the event
		 */
		public void actionPerformed(ActionEvent event) {
			
			if (SELECT.equals(event.getActionCommand())) {
			
				try {
					
					// get the meta-paramater to be edited
					MetaTaxonomy mp = (MetaTaxonomy)getMetaParameter();
								
					// show the taxonomy selection dialog
					TaxonomySelectionDialog d = new TaxonomySelectionDialog(owner, specchioClient, mp);
					d.setLocation(selectButton.getLocationOnScreen());
					d.setVisible(true);

					int tax_id = d.getSelectedTaxonomyId();
					if (tax_id > 0) {
						Long taxonomy_id = (long) tax_id;
					
						// update value of taxonomy button
						TaxonomyNodeObject taxonomy;
						taxonomy = specchioClient.getTaxonomyNode(tax_id);
						selectButton.setText(taxonomy.getName());
						
						// notify listeners of the change
						fireMetadataFieldChanged(getField(), taxonomy_id);
					}
					
				} catch (SPECCHIOClientException ex) {
					ErrorDialog error = new ErrorDialog(owner, "Could not retrieve taxonomy", ex.getUserMessage(), ex);
					error.setVisible(true);
				}
				
			} else {
				
				// pass pop-up menu actions to the super class
				super.actionPerformed(event);
				
			}
			
		}
		
		
		/**
		 * Make the field editable or not.
		 * 
		 * @param editable true to make the field editable, false otherwise
		 */
		public void setEditable(boolean editable) {
			
			if (editable) {
				// display the combo box
				remove(text);
				add(selectButton);
			} else {
				// display the label
				remove(selectButton);
				add(text);
			}
			
			// force re-draw
			revalidate();
			repaint();
			
		}
		
		
		/**
		 * Enable or disable the field.
		 *
		 * @param enabled	true or false
		 */
		public void setEnabled(boolean enabled) {
			
			super.setEnabled(enabled);
			selectButton.setEnabled(enabled && !fieldHasMultipleValues());
			
		}
		
	}

}
