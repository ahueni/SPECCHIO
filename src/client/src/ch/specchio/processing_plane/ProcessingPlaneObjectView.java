package ch.specchio.processing_plane;

import java.awt.Component;

import org.jgraph.JGraph;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

import ch.specchio.proc_modules.ProcessingChainComponent;

public class ProcessingPlaneObjectView extends VertexView
{
	private static final long serialVersionUID = 1L;
	
	protected static PPORenderer renderer = new PPORenderer();

	public ProcessingPlaneObjectView(Object cell) 
	{
		super(cell);
	}

	@Override
	public CellHandle getHandle(GraphContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	public CellViewRenderer getRenderer() {

		return renderer;
	}
	
	public static class PPORenderer extends VertexRenderer implements CellViewRenderer
	{
		private static final long serialVersionUID = 1L;

		public Component getRendererComponent(JGraph graph, CellView view,
				boolean sel, boolean focus, boolean preview) 
		{
			ProcessingChainComponent pcc = (ProcessingChainComponent)((ProcessingPlaneObject) view.getCell()).getUserObject();
		
			// Rectangle2D d = view.getBounds(); // coords of the view
			
			return pcc.get_info_panel();
		}
		
	}

}
