package ch.specchio.processing_plane;

import org.jgraph.graph.DefaultGraphCell;

import ch.specchio.proc_modules.ProcessingChainComponent;

public class ProcessingPlaneObject extends DefaultGraphCell{

	private static final long serialVersionUID = 1L;

	public ProcessingPlaneObject(Object chain_component)
	{
		super(chain_component);
		
		// set the ProcessingPlaneObject reference in the chain component
		((ProcessingChainComponent) chain_component).set_ppo(this);
	}
	
	
	
}
