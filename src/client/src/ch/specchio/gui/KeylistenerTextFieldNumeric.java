package ch.specchio.gui;

import java.awt.event.KeyEvent;

public class KeylistenerTextFieldNumeric extends KeylistenerTextField
{
	private static final long serialVersionUID = 1L;
	
	/** allow entry of floating-point numbers? */
	private boolean floating;
	
	public KeylistenerTextFieldNumeric(int size)
	{
		super(size);
		
		floating = true;
	}
	
	
	/**
	 * Test whether or not a key stroke should be permitted in the text field.
	 * 
	 * @param e	the event to check
	 * 
	 * @return true if this key stroke should be processed, and false otherwise
	 */
	private boolean keyEventIsValid(KeyEvent e) {
		
		// action keys are always allowed
		if (e.isActionKey()) {
			return true;
		}
		
		char c = e.getKeyChar();
		
		// digits and minus signs are always allowed
		if (Character.isDigit(c) || c == KeyEvent.VK_MINUS) {
			return true;
		}
		
		// decimal points are allowed if floating-point numbers are allowed
		if (c == KeyEvent.VK_PERIOD) {
			return floating;
		}
		
		// allow deletions
		if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
			return true;
		}
		
		// allow copy and paste
		if (c == KeyEvent.VK_COPY || c == KeyEvent.VK_CUT || c == KeyEvent.VK_PASTE) {
			return true;
		}
		
		// assume not allowed
		return false;
		
	}
	
	/**
	 * Inform metadata object of change.
	 * 
	 * @param e	the event to be handled
	 */
	protected void processKeyEvent(KeyEvent e)
	{
		if (keyEventIsValid(e))
		{
			super.processKeyEvent(e);			
		}			
		
	}
	
	
	/**
	 * Set whether or not the text field will accept entry of
	 * floating point numbers.
	 * 
	 * @param floatingIn	true to allow entry of floating-point numbers, false to allow entry of integers only
	 */
	public void setAllowFloats(boolean floatingIn) {
		
		floating = floatingIn;
		
	}
	
	
}

