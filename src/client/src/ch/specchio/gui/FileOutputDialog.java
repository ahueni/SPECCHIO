package ch.specchio.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import ch.specchio.constants.TimeFormats;

public class FileOutputDialog extends ModalDialog  implements ActionListener
{
	private static final long serialVersionUID = 1L;

	GridbagLayouter l;
	GridBagConstraints constraints;

	public JTextField base_name;
	public JTextField target_dir;
	JComboBox file_formats;
	public JCheckBox split_header_and_body;
	public JRadioButton formatted_time_Button;
	public JRadioButton seconds_time_Button;
	
	public int file_type;
	
	
	JButton ok;
	final JFileChooser fc;
	
	private boolean done;
	private boolean ok_choice;
	
	public FileOutputDialog (Point loc)
	{
		super("File output", loc);
		
		fc = new JFileChooser();
		
		constraints = new GridBagConstraints();
		l = new GridbagLayouter(this);
		
		constraints.gridwidth = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		
		// build GUI
		constraints.gridy = 0;	
		constraints.gridx = 0;		
		l.insertComponent(new JLabel("File format:"), constraints);
		file_formats = new JComboBox(new String[]{"CSV", "ENVI SLB"});
		file_formats.addActionListener(this);
		file_formats.setActionCommand("fileformat_changed");
		constraints.gridx = 1;		
		l.insertComponent(file_formats, constraints);
		
		constraints.gridy = 1;
		constraints.gridx = 0;		
		l.insertComponent(new JLabel("Target directory:"), constraints);
		target_dir = new JTextField(40);
		target_dir.setEditable(false);
		constraints.gridx = 1;		
		l.insertComponent(target_dir, constraints);
		
		JButton browse = new JButton("Browse");
		browse.setActionCommand("browse");
		browse.addActionListener(this);
		constraints.gridx = 2;
		l.insertComponent(browse, constraints);
		
		constraints.gridy = 2;
		constraints.gridx = 0;			
		l.insertComponent(new JLabel("Base name:"), constraints);
		base_name = new JTextField(40);
		constraints.gridx = 1;
		l.insertComponent(base_name, constraints);
		
		// panel for options
		JPanel options = new JPanel();
		GridbagLayouter options_l = new GridbagLayouter(options);
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder tb = BorderFactory.createTitledBorder(blackline, "Options");
		options.setBorder(tb);
		
		split_header_and_body = new JCheckBox("Split header and body");
		constraints.gridx = 0;
		constraints.gridy = 0;
		options_l.insertComponent(split_header_and_body, constraints);

		constraints.gridy = 1;
		options_l.insertComponent(new JLabel("Time format:"), constraints);		
		formatted_time_Button = new JRadioButton("yyyy.MM.dd HH:mm:ss");
		formatted_time_Button.setSelected(true); // default is formatted time
		constraints.gridy = 2;
		options_l.insertComponent(formatted_time_Button, constraints);
		seconds_time_Button = new JRadioButton("Milliseconds since 1st Jan 1970, 00:00:00");
		constraints.gridy = 3;
		options_l.insertComponent(seconds_time_Button, constraints);		
	    
		ButtonGroup group = new ButtonGroup();
	    group.add(formatted_time_Button);
	    group.add(seconds_time_Button);
		
		// insert options panel
		constraints.gridwidth = 2;
		constraints.gridx = 0;
		constraints.gridy = 3;
		l.insertComponent(options, constraints);
		
		
		// create new panel for ok and cancel buttons
		JPanel control = new JPanel();
		GridbagLayouter control_l = new GridbagLayouter(control);
		
		ok = new JButton("OK");
		ok.setActionCommand("ok");
		ok.setEnabled(false); // default: not enabled
		ok.addActionListener(this);		
		
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;


		control_l.insertComponent(ok, constraints);
		
		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);	
		
		constraints.gridx = 1;
		control_l.insertComponent(cancel, constraints);		
		
		// insert control panel
		constraints.gridx = 2;
		constraints.gridy = 4;
		l.insertComponent(control, constraints);
				
		
		pack();
		validate();
		setVisible(true);
		repaint();
		
	}
	
	
	public int get_time_format()
	{
		int time_format = 0;
		
		if(formatted_time_Button.isSelected())
			time_format = TimeFormats.Formatted;
		
		if(seconds_time_Button.isSelected())
			time_format = TimeFormats.Seconds;
		
		
		return time_format;
	}
	
	synchronized public boolean get_fileoutput_info() {
		done = false;
		this.setVisible(true);
		//this.repaint();
		
        if (!done) {   
            try {
                wait ();
            } catch (InterruptedException e) { }
        }
        
        this.setVisible(false);
        return ok_choice;
    }
	
	
	synchronized public void actionPerformed(ActionEvent e) 
	{

		if(e.getActionCommand().equals("fileformat_changed"))
		{
			switch(file_formats.getSelectedIndex())
			{
			case 0:
				// CSV
				this.split_header_and_body.setEnabled(true);
				
				// enable time button and set default to formatted time
				this.seconds_time_Button.setEnabled(true);
				this.seconds_time_Button.setSelected(false);
				this.formatted_time_Button.setEnabled(true);
				this.formatted_time_Button.setSelected(true);
				
				break;
			case 1:
				// ENVI SLB: these are always split
				this.split_header_and_body.setEnabled(false);
				this.split_header_and_body.setSelected(true);
				
				// no selection of time possible for ENVI
				this.seconds_time_Button.setEnabled(false);
				//this.seconds_time_Button.setSelected(false); // one button is always selected!
				this.formatted_time_Button.setEnabled(false);
				//this.formatted_time_Button.setSelected(false);
				
			}
	
		}

		
		if(e.getActionCommand().equals("browse"))
		{
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				this.target_dir.setText(file.getAbsolutePath());
				ok.setEnabled(true);
			}	    	

		}
	
		if(e.getActionCommand().equals("ok"))
		{
			ok_choice = true;
			done = true;
			this.confirmed = true;

			// this works because we insert the strings in the same order as they are given
			// constants in FileTypes
			this.file_type = this.file_formats.getSelectedIndex();

			this.notifyAll();	
		}
		
		if(e.getActionCommand().equals("cancel"))
		{
			ok_choice = false;
			done = true;
			this.notifyAll();	
		}

	}

	

}
