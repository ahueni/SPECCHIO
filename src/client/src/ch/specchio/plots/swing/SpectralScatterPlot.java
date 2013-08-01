package ch.specchio.plots.swing;


import ch.specchio.gui.ProgressReportDialog;
import ch.specchio.spaces.SpectralSpace;

public class SpectralScatterPlot extends SpectralPlot {
	private static final long serialVersionUID = 1L;

	public SpectralScatterPlot(SpectralSpace space, int x_size, int y_size,
			ProgressReportDialog pr) {
		super(space, x_size, y_size, pr);
		
		plot.setMarksStyle("points");
		plot.setConnected(false);

	}
}
