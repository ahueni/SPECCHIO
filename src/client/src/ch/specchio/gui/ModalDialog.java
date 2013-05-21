package ch.specchio.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

abstract public class ModalDialog extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	protected boolean done;
	public boolean confirmed = false;
	
	public ModalDialog(String title, Point loc)
	{
		super(title);
		setLocation(loc.x, loc.y);
	}
	
	public synchronized boolean get_user_input()
	{
		done = false;
		setVisible(true);		
		
        if (!done) {   
            try {
                wait ();
            } catch (InterruptedException e) { }
        }
		this.setVisible(false);
		
		return this.confirmed;
	}
	
	abstract public void actionPerformed(ActionEvent e);

}
