package ch.specchio.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.types.CalibrationMetadata;
import ch.specchio.types.Institute;
import ch.specchio.types.Instrument;
import ch.specchio.types.Picture;
import ch.specchio.types.PictureTable;
import ch.specchio.types.Reference;
import ch.specchio.types.ReferenceBrand;
import ch.specchio.types.Sensor;

public abstract class InstrumentationMetadataEditorGroup extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private InstrumentationMetadataEditor editor;
	private String object_title;
	private boolean changed = false;
	private boolean editable = true;

	protected GridBagConstraints constraints;
	protected GridbagLayouter l;
	private JPanel reset_update_panel;
	private JButton update = null;
	private JButton reset = null;
	private Color update_foreground_colour;
	
	protected SPECCHIOClient specchio_client;
	protected Frame parent;
	
	public InstrumentationMetadataEditorGroup(InstrumentationMetadataEditor editor, String object_title, SPECCHIOClient specchio_client) {
		
		this.editor = editor;
		this.object_title = object_title;
		this.specchio_client = specchio_client;
		parent = editor;
		
		constraints = new GridBagConstraints();		
		constraints.gridwidth = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
	}
	

	public void actionPerformed(ActionEvent e)
	{
		if ("reset".equals(e.getActionCommand())) {
			// reload the data from the database
			reset();
		}
		
		if ("update".equals(e.getActionCommand())) {
			try {
				// update the database
				update();
				changed(false);
			}
			catch (SPECCHIOClientException ex) {
				JOptionPane.showMessageDialog(
		    			editor,
		    			ex.getMessage(),
		    			"Error",
		    			JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
		    		);
		    }
			catch (SPECCHIOUserInterfaceException ex) {
				JOptionPane.showMessageDialog(
		    			editor,
		    			ex.getMessage(),
		    			"Error",
		    			JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
		    		);
		    }
		}
	}
	
	
	public void changed(boolean is_changed)
	{
		changed = is_changed;
		
		if(update != null) // prevents null pointer exception when no buttons were created
		{
			if(is_changed)
			{				
				update.setEnabled(true);
				update.setForeground(Color.RED);
			}
			else
			{
				update.setEnabled(false);
				update.setForeground(update_foreground_colour);
			}
		}
		
		
	}
	
	
	public String get_object_title() {
		
		return object_title;
		
	}
	
	JPanel get_reset_update_panel()
	{
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder tb;
		
		// Reset and update buttons for campaign, hierarchy and spectrum
		constraints.gridwidth = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		
		
		reset_update_panel = new JPanel();
		l = new GridbagLayouter(reset_update_panel);
		tb = BorderFactory.createTitledBorder(blackline, object_title);
		reset_update_panel.setBorder(tb);

		reset = new JButton("Reset");
		reset.setActionCommand("reset");
		reset.addActionListener(this);		
		
		update = new JButton("Update");
		update.setEnabled(false); // by default not enabled
		update.setActionCommand("update");
		update.addActionListener(this);	
		update_foreground_colour = update.getForeground();
		
		constraints.gridx = 0;
		constraints.gridy = 0;		
		l.insertComponent(reset, constraints);
		constraints.gridx = 1;
		l.insertComponent(update, constraints);
		
		
		return reset_update_panel;
						
	}
	
	public void setEditable(boolean editable)
	{
		this.editable = editable;
		reset.setEnabled(editable);
		update.setEnabled(changed && editable);
	}
	
	
	public abstract void reset();
	
	
	public void update() throws SPECCHIOClientException, SPECCHIOUserInterfaceException {
		
		// notify the editor
		editor.update_done();
		
	}
	

}

class InstrumentMetadataGroup extends InstrumentationMetadataEditorGroup {
	
	private static final long serialVersionUID = 1L;
	
	private Instrument instrument = null;
	private InstrumentMetadataPanel instrument_data;
	private PictureMetadataPanel picture_data;
	private CalibrationListMetadataPanel cal_data;
	private Institute institutes[];
	private Sensor sensors[];

	public InstrumentMetadataGroup(InstrumentationMetadataEditor editor, SPECCHIOClient specchio_client) throws SPECCHIOClientException {
		
		super(editor, "Instrument Data", specchio_client);
		
		int pos_cnt = 0;
		
		// create a scroll pane to put in all metadata objects
		JScrollPane scroll_pane = new JScrollPane();
		this.setLayout(new BorderLayout());
		add(scroll_pane, "Center");		
		
		// create another panel to put into the scroll pane (because we want gridbag layout here!)
		JPanel main_panel = new JPanel();
		scroll_pane.getViewport().add(main_panel);
		
		l = new GridbagLayouter(main_panel);
		
		// get lists of known institutes and sensors from the server
		institutes = specchio_client.getInstitutes();
		sensors = specchio_client.getSensors();
		
		// build gui from blocks	
		instrument_data = new InstrumentMetadataPanel(parent, specchio_client, institutes, sensors);
		instrument_data.show_border(true);
		constraints.gridx = 0;
		constraints.gridy = 0;
		l.insertComponent(instrument_data, constraints);
				
		picture_data = new PictureMetadataPanel(parent, specchio_client);
		picture_data.show_border(false);
		constraints.gridx = 1;
		constraints.gridy = pos_cnt++;
		l.insertComponent(picture_data, constraints);		
		
		cal_data = new CalibrationListMetadataPanel(parent, specchio_client, "instrument");
		cal_data.show_border(false);
		constraints.gridwidth = 2;
		constraints.gridx = 0;
		constraints.gridy = pos_cnt++;
		l.insertComponent(cal_data, constraints);
		
	}
	
	
	public void setEditable(boolean editable) {
		
		super.setEditable(editable);
		instrument_data.setEditable(editable);
		picture_data.setEditable(editable);
		cal_data.setEditable(editable);
		
	}
	
	
	public void setInstrument(Instrument instrument) throws SPECCHIOClientException {
		
		// save a reference to the new instrument
		this.instrument = instrument;
		
		// populate the instrument metadata components
		this.instrument_data.setInstrumentData(this.instrument);
		
		if (instrument != null) {
			
			// populate the picture metadata
			PictureTable pictures = specchio_client.getInstrumentPictures(instrument.getInstrumentId());
			picture_data.setPictureTable(pictures);
			
			// populate the calibration list metadata
			CalibrationMetadata[] cm = specchio_client.getInstrumentCalibrationMetadata(instrument.getInstrumentId());
			cal_data.setCalibrationMetadata(cm);
			cal_data.setObjectId(instrument.getInstrumentId());
			
		} else {
			
			picture_data.setPictureTable(null);
			cal_data.setCalibrationMetadata(null);
			cal_data.setObjectId(0);
			
		}
		
	}
	
	
	public void reset() {
		
		try {
			setInstrument(instrument);
		}
		catch (SPECCHIOClientException ex) {
			// should never happen
			ex.printStackTrace();
		}
		
	}
	
	
	public void update() throws SPECCHIOClientException, SPECCHIOUserInterfaceException {
		
		// update instrument data
		instrument.setInstrumentName(instrument_data.getInstrumentName());
		if (instrument_data.getInstrumentOwner() != null) {
			instrument.setInstrumentOwner(instrument_data.getInstrumentOwner().toString());
		} else {
			instrument.setInstrumentOwner((String)null);
		}
		instrument.setInstrumentNumber(instrument_data.getInstrumentNumber());
		instrument.setSensor(instrument_data.getSensor());
		specchio_client.updateInstrument(instrument);
		instrument_data.updateComplete();
		
		// remove pictures that were deleted
		for (Integer picture_id : picture_data.getDeletedPictureIds()) {
			specchio_client.deleteInstrumentPicture(picture_id);
		}
		
		// insert pictures that were added
		for (Picture picture : picture_data.getNewPictures()) {
			picture.setObjectId(instrument.getInstrumentId());
			specchio_client.insertInstrumentPicture(picture);
		}
		
		// update pictures that were changed
		for (Picture picture : picture_data.getChangedPictures())  {
			specchio_client.updateInstrumentPicture(picture);
		}
		picture_data.updateComplete();
		
		// update calibration data
		for (CalibrationMetadata cm : cal_data.getChangedCalibrationMetadata()) {
			specchio_client.updateInstrumentCalibrationMetadata(cm);
		}
		cal_data.updateComplete();
		
		// perform super-class actions
		super.update();
		
	}

}

class ReferenceMetadataGroup extends InstrumentationMetadataEditorGroup {
	
	private static final long serialVersionUID = 1L;
	
	private Reference reference;
	private ReferenceMetadataPanel reference_data;
	private PictureMetadataPanel picture_data;
	private CalibrationListMetadataPanel cal_data;

	public ReferenceMetadataGroup(InstrumentationMetadataEditor editor, SPECCHIOClient specchio_client) throws SPECCHIOClientException {
		
		super(editor, "Reference Data", specchio_client);
		
		int pos_cnt = 0;
		
		// create a scroll pane to put in all metadata objects
		JScrollPane scroll_pane = new JScrollPane();
		this.setLayout(new BorderLayout());
		add(scroll_pane, "Center");		
		
		// create another panel to put into the scroll pane (because we want gridbag layout here!)
		JPanel main_panel = new JPanel();
		scroll_pane.getViewport().add(main_panel);
		
		l = new GridbagLayouter(main_panel);
		
		// download the list of possible owners and brands
		Institute institutes[] = specchio_client.getInstitutes();
		ReferenceBrand brands[] = specchio_client.getReferenceBrands();
		
		// build gui from blocks	
		reference_data = new ReferenceMetadataPanel(parent, specchio_client, institutes, brands);
		reference_data.show_border(true);
		constraints.gridx = 0;
		constraints.gridy = 0;
		l.insertComponent(reference_data, constraints);
				
		picture_data = new PictureMetadataPanel(parent, specchio_client);
		picture_data.show_border(false);
		constraints.gridx = 1;
		constraints.gridy = pos_cnt++;
		l.insertComponent(picture_data, constraints);	
		
		cal_data = new CalibrationListMetadataPanel(parent, specchio_client, "reference");
		cal_data.show_border(false);
		constraints.gridwidth = 2;
		constraints.gridx = 0;
		constraints.gridy = pos_cnt++;
		l.insertComponent(cal_data, constraints);			
		
		this.validate();
		
	}
	
	
	public void setEditable(boolean editable) {
		
		super.setEditable(editable);
		reference_data.setEditable(editable);
		picture_data.setEditable(editable);
		cal_data.setEditable(editable);
		
	}
	
	
	public void setReference(Reference reference) throws SPECCHIOClientException {
		
		// set the reference object
		this.reference = reference;
		this.reference_data.setReferenceData(reference);
		
		if (reference != null) {
			
			// populate the picture metadata
			PictureTable pictures = specchio_client.getReferencePictures(reference.getReferenceId());
			picture_data.setPictureTable(pictures);
			
			// populate the calibration list metadata
			CalibrationMetadata[] cm = specchio_client.getReferenceCalibrationMetadata(reference.getReferenceId());
			cal_data.setCalibrationMetadata(cm);
			cal_data.setObjectId(reference.getReferenceId());
			
		} else {
			
			picture_data.setPictureTable(null);
			cal_data.setCalibrationMetadata(null);
			cal_data.setObjectId(0);
			
		}
		
	}
	
	
	public void reset() {
		
		try {
			setReference(reference);
		}
		catch (SPECCHIOClientException ex) {
			// should never happen
			ex.printStackTrace();
		}
		
	}
	
	
	public void update() throws SPECCHIOClientException, SPECCHIOUserInterfaceException {
		
		// update reference metadata
		reference.setReferenceName(reference_data.getReferenceName());
		reference.setReferenceOwner(reference_data.getReferenceOwner());
		reference.setSerialNumber(reference_data.getSerialNumber());
		reference.setBrandName(reference_data.getReferenceBrand());
		specchio_client.updateReference(reference);
		reference_data.updateComplete();
		
		// remove pictures that were deleted
		for (Integer picture_id : picture_data.getDeletedPictureIds()) {
			specchio_client.deleteReferencePicture(picture_id);
		}
		
		// insert pictures that were added
		for (Picture picture : picture_data.getNewPictures()) {
			picture.setObjectId(reference.getReferenceId());
			specchio_client.insertReferencePicture(picture);
		}
		
		// update pictures that were changed
		for (Picture picture : picture_data.getChangedPictures())  {
			specchio_client.updateReferencePicture(picture);
		}
		picture_data.updateComplete();
		
		// update calibration data
		for (CalibrationMetadata cm : cal_data.getChangedCalibrationMetadata()) {
			specchio_client.updateReferenceCalibrationMetadata(cm);
		}
		cal_data.updateComplete();
		
		// perform super-class actions
		super.update();
		
		
	}

}

