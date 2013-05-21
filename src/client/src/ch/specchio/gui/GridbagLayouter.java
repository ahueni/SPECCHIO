package ch.specchio.gui;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;



public class GridbagLayouter {
	GridBagLayout gridbag;	
	Container comp;
	
	public GridbagLayouter(Container comp)
	{
		this.comp = comp;		
		gridbag = new GridBagLayout();
		comp.setLayout(gridbag);
	}
	
	public void insertComponent(Component component, GridBagConstraints constraints)
	{
		gridbag.setConstraints(component, constraints);
		comp.add(component);
	}
}
