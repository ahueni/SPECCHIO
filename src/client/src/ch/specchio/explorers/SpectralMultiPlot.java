package ch.specchio.explorers;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ch.specchio.gui.ProgressReportDialog;
import ch.specchio.plots.PlotsCallback;
import ch.specchio.plots.swing.SpectralLinePlot;
import ch.specchio.plots.swing.SpectralPlot;
import ch.specchio.plots.swing.SpectralScatterPlot;
import ch.specchio.spaces.SpectralSpace;

public class SpectralMultiPlot  extends JPanel implements PlotsCallback
{
	private static final long serialVersionUID = 1L;
	
	SpectralSpace space;
	
	SpectralPlot sp;
	ProgressReportDialog pr;
	
	public SpectralMultiPlot(SpectralSpace space, ProgressReportDialog pr, int plot_type)
	{
		
		this.space = space;
		this.pr = pr;
		
		// Spectral Plot
		if(plot_type == 0)
			sp = new SpectralLinePlot(space, 700,500, pr);	
		else
			sp = new SpectralScatterPlot(space, 700,500, pr);	
		
		sp.plot_all();
				
		this.add(sp);
		
	}

	public void band_selected(int band_id) {
		// TODO Auto-generated method stub
		
	}

	public void data_point_selected(int point_id) {
		// TODO Auto-generated method stub
		
	}
	
	

}
