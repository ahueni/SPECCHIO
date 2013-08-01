package ch.specchio.plots.swing;


import ch.specchio.interfaces.ProgressReportInterface;
import ch.specchio.spaces.SpectralSpace;

public class SpectralScatterPlot extends SpectralPlot {
	private static final long serialVersionUID = 1L;

	public SpectralScatterPlot(SpectralSpace space, int x_size, int y_size,
			ProgressReportInterface pr) {
		super(space, x_size, y_size, pr);
		
		plot.setMarksStyle("points");
		plot.setConnected(false);

	}
}
