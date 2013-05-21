package ch.specchio.processors;


import javax.swing.JPanel;

import ch.specchio.gui.GridbagLayouter;


public class SpaceAndProcessContainer extends JPanel{

	private static final long serialVersionUID = 1L;

	public GridbagLayouter l;
	
	public SpaceAndProcessContainer ()
	{
		l = new GridbagLayouter(this);	
	}
}
