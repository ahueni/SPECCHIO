package ch.specchio.gui;

import javax.swing.JPanel;

import ch.specchio.plots.swing.SpectralLinePlot;
import ch.specchio.plots.swing.SpectralPlot;
import ch.specchio.spaces.SpectralSpace;

public class FactorsPlotField extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private SpectralPlot sp;

	
	public FactorsPlotField(SpectralSpace ss, int spectrum_id)
	{
		if (ss != null) {
			sp = new SpectralLinePlot(ss, 300, 200, null);
			add(sp);
			sp.plot(spectrum_id);
		}
		
		setPreferredSize(new java.awt.Dimension(300, 200));
	}
	

	public void clear() {
		removeAll();
	}

}
