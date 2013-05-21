package ch.specchio.gui;

import java.awt.AWTEvent;
import javax.swing.JTextField;


//here some trick to catch the key events on the text field ....
public class KeylistenerTextField extends JTextField {
	
	
	private static final long serialVersionUID = 1L;
	
	public KeylistenerTextField(int size)
	{
		super(size);
		this.enableEvents(AWTEvent.KEY_EVENT_MASK);
	}
	
	void enable_event(boolean enable)
	{
		if(enable)
		{
			this.enableEvents(AWTEvent.KEY_EVENT_MASK);
		}
		else
		{
			this.disableEvents(AWTEvent.KEY_EVENT_MASK);
		}
	}
		
}



