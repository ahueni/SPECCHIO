package ch.specchio.processing_plane;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;

public class PPOViewFactory extends DefaultCellViewFactory
{
	private static final long serialVersionUID = 1L;

	protected VertexView createVertexView(Object cell) { 
			// Return a ProcessingPlaneObjectView for ProcessingPlaneObject 
			if (cell instanceof ProcessingPlaneObject) 
			{
				ProcessingPlaneObjectView v = new ProcessingPlaneObjectView(cell);
				return v; 
			}
			
			// Else Call Superclass 
			return super.createVertexView(cell); 
			} 
}
