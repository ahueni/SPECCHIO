package ch.specchio.gui;

import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;

import ch.specchio.interfaces.ProgressReportInterface;


/**
 * Dialogue for displaying progress of an operation.
 */
public class ProgressReportDialog extends JDialog implements ProgressReportInterface
{
	
	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	/** progress display panel */
	private ProgressReportPanel progressPanel;
	
	
	/**
	 * Constructor for calling from a frame.
	 * 
	 * @param owner						the frame that owns this dialogue
	 * @param title						the title of the dialogue
	 * @param include_component_label	include the "component" section?
	 * @param columns					the number of columns in the "operation" and "component" fields
	 */
	public ProgressReportDialog(Frame owner, String title, boolean include_component_label, int columns)
	{
		super(owner, title + " Progress", false);	
		
		init(include_component_label, columns);
	}
	
	/**
	 * Constructor for calling from a frame.
	 * 
	 * @param owner						the frame that owns this dialogue
	 * @param title						the title of the dialogue
	 * @param include_component_label	include the "component" section?
	 * @param columns					the number of columns in the "operation" and "component" fields
	 * @param modal						modal switch
	 */
	public ProgressReportDialog(Frame owner, String title, boolean include_component_label, int columns, boolean modal)
	{
		super(owner, title + " Progress", modal);	
		
		init(include_component_label, columns);
	}	
	
	
	/**
	 * Constructor for calling from a dialogue.
	 * 
	 * @param owner						the dialogue that owns this dialogue
	 * @param title						the title of the dialogue
	 * @param include_component_label	include the "component" section?
	 * @param columns					the number of columns in the "operation" and "component" fields
	 */
	public ProgressReportDialog(Dialog owner, String title, boolean include_component_label, int columns)
	{
		super(owner, title + " Progress", false);	
		
		init(include_component_label, columns);
	}
	
	
	/**
	 * Helper method for constructing the dialogue.
	 * 
	 * @param include_component_label	include the "component" section?
	 * @param columns					the number of columns in the "operation" and "component" fields
	 */
	private void init(boolean include_component_label, int columns) {
		
		// add the root panel
		JPanel rootPanel = new JPanel();
		getContentPane().add(rootPanel);
		
		// add the progress report panel
		progressPanel = new ProgressReportBarPanel(include_component_label, columns);
		rootPanel.add(progressPanel);
		
		// lay it out
		pack();
	}
	
	
	public void set_component(String c)
	{
		progressPanel.set_component(c);
	}
	
	
	public void set_indeterminate(boolean indeterminate)
	{
		progressPanel.set_indeterminate(indeterminate);
	}
	
	
	public void set_operation(String op)
	{
		progressPanel.set_operation(op);
	}
	
	public void set_min_max(int min, int max)
	{
		progressPanel.set_min_max(min, max);		
	}

	
	public boolean set_progress(double value)
	{
		return progressPanel.set_progress(value);
	}

	public boolean set_progress(int value)
	{
		return progressPanel.set_progress(value);
	}
	
	public void setToDocumentModal()
	{
		this.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
	}

}
