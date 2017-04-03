package ch.specchio.plots.swing;


import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JSlider;


import ptolemy.plot.Plot;

import ch.specchio.interfaces.ProgressReportInterface;
import ch.specchio.plots.VectorStatistics;
import ch.specchio.proc_modules.VectorRegion;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.spaces.SpectralSpace;
import com.quinncurtis.chart3djava.AxisTitle;
import com.quinncurtis.chart3djava.CartesianCoordinates;
import com.quinncurtis.chart3djava.ChartAttribute;
import com.quinncurtis.chart3djava.SimpleBarPlot;
import com.quinncurtis.chart3djava.SimpleDataset;
import com.quinncurtis.chart3djava.SimplePlot;
import com.quinncurtis.chart3djava.LinearAxis;

public abstract class SpectralPlot extends JPanel
{

	private static final long serialVersionUID = 1L;
	SpectralSpace space;
	double wavelength;
	int band;
	int no_of_chunks;

	//ChartPoint2D pointarray[];
//	ChartView cv;
//	SimpleDataset datasets[];
//	SimpleDataset dataset; // homogenous dataset (no chunks)
	//z_statistics z_stats; 
	SimplePlot SpectrumPlots[];
	int numcontourlevels = 13;
	double contourlevels[];
	ChartAttribute attribs[];
	CartesianCoordinates pTransform1;
	LinearAxis xAxis, yAxis;
	AxisTitle yaxistitle;
	Font AxisFont;
	JSlider band_slider;
	SimpleDataset wvl_bar;
	SimpleBarPlot wvl_indicator;
	
	
	// Ptolemy plotting
	Plot plot;
	
	ProgressReportInterface pr;
	private int currentDataset;
	private int wvl_indicator_dataset_no;
	private boolean wvl_indicator_on = false;
	private boolean show_wvl_indicator = false;
	
	public SpectralPlot(SpectralSpace space, int x_size, int y_size, ProgressReportInterface pr)
	{
		this.space = space;
		this.pr = pr;
		
		if(pr != null) pr.set_component("Spectral Plot");
		
		if (space.getAverageWavelengths() != null) {
			if (space.getAverageWavelengths().length > 0) {
				wavelength = space.getAverageWavelengths()[0];
			}
		}
		
//		cv = new ChartView();
//		cv.setPreferredSize(x_size, y_size);
//		
//
//		get_no_of_chunks();
//
//		datasets = new SimpleDataset[space.getNumberOfDataPoints()];
//		SpectrumPlots = new SimplePlot[space.getNumberOfDataPoints()];
		
        plot = new Plot();    	
    	
        plot.setSize(x_size, y_size);
        plot.setButtons(true);
        plot.setTitle("Spectral Plot");
//        leftPlot.setYRange(-4, 4);
//        leftPlot.setXRange(0, 100);
        plot.setXLabel(space.get_x_axis_label());
//        leftPlot.setYLabel("value");
        plot.setMarksStyle("none");
		
		setup_plot();

	}
	
	
	public void setup_plot()
	{
				
		if(pr != null) pr.set_operation("Setting up plot.");		
		
		
		
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		this.add(plot, BorderLayout.CENTER);		
		
		
//		dataset = new SimpleDataset();
//		pTransform1 = new CartesianCoordinates( ChartConstants.LINEAR_SCALE, ChartConstants.LINEAR_SCALE);
//		pTransform1.setGraphBorderDiagonal(0.20, .10, .85, 0.8) ;
//	
//		
//		Background background = new Background( pTransform1, ChartConstants.GRAPH_BACKGROUND, Color.white);
//		cv.addChartObject(background);
//
//		xAxis = new LinearAxis(pTransform1, ChartConstants.X_AXIS);
//		cv.addChartObject(xAxis);
//
//
//        yAxis = new LinearAxis(pTransform1, ChartConstants.Y_AXIS);
//	    cv.addChartObject(yAxis);
//		
//		NumericAxisLabels xAxisLab = new NumericAxisLabels(xAxis );  
//		cv.addChartObject(xAxisLab);
//
//		NumericAxisLabels yAxisLab = new NumericAxisLabels(yAxis);
//		cv.addChartObject(yAxisLab);
		


//		// Title and axis titles		
//		Font TitleFont = new Font("SansSerif", Font.BOLD,12);
//
//		ChartTitle mainTitle = new ChartTitle( TitleFont, "Spectral plot");
//		mainTitle.setTitleType(ChartConstants.CHART_HEADER);
//		mainTitle.setTitlePosition( ChartConstants.CENTER_GRAPH);
//		cv.addChartObject(mainTitle);
//		
//		AxisFont = new Font("SansSerif", Font.PLAIN,10);
//		
//		
//		
//		AxisTitle xaxistitle = new AxisTitle( xAxis, AxisFont, space.get_x_axis_label());
//		cv.addChartObject(xaxistitle);
//
//
//		
//		yaxistitle = new AxisTitle( yAxis, AxisFont, "");
//		cv.addChartObject(yaxistitle);
//		  
//		
//		for(int i= 0; i < space.getNumberOfDataPoints();i++)
//		{
//			int rgb = 255/space.getNumberOfDataPoints()*i;
//			ChartAttribute attrib =  new ChartAttribute (new Color(rgb,50,255 - rgb), 1,ChartConstants.LS_SOLID);
//			
//			
//			datasets[i] = new SimpleDataset();
//			SpectrumPlots[i] = get_plot_object(attrib);
//			cv.addChartObject(SpectrumPlots[i]);
//		}		
		
		
		
//		double[] x = new double[1]; 
//		double[] y = new double[1]; 
//		x[0] = 0;
//		y[0] = 0;
//		
//		wvl_bar = new SimpleDataset();
//		
//		ChartAttribute attrib3 = new ChartAttribute (Color.red, 1,ChartConstants.LS_SOLID, Color.red);
//		attrib3.setFillFlag (true);
//		wvl_indicator = new SimpleBarPlot(pTransform1, wvl_bar, 0.1, 0.0,  attrib3, ChartConstants.JUSTIFY_CENTER);
//		cv.addChartObject(wvl_indicator);
//
//		
//		BorderLayout bl = new BorderLayout();
//		this.setLayout(bl);
//		this.add(cv, BorderLayout.CENTER);
		
	}
	
	
//	abstract SimplePlot get_plot_object(ChartAttribute attrib);
	
	
	public void plot_all()
	{
		ArrayList<double[]> vectors = space.getVectors();		
    	ArrayList<Integer> ids = space.getSpectrumIds();
    	double[] vector;
    	
//    	System.out.println(space.getNumberOfDataPoints());
//    	System.out.println(ids.size());

    	for(currentDataset= 0; currentDataset < space.getNumberOfDataPoints();currentDataset++)
    	{
    		vector = space.getVector(ids.get(currentDataset));
    		
    		for ( int i = 0; i < vector.length; i++ ) {
    			plot.addPoint(currentDataset,space.get_wvl_of_band(i), vector[i], true);
    		}   
    		//System.out.println(vector);
    		//System.out.println(space.get_wvl_of_band(0));
    	}
    	
    	wvl_indicator_dataset_no = currentDataset;
		
//		Vector<VectorRegion> chunks = get_chunks();
		
//		dataset = new SimpleDataset("",wvl, vectors.get(0)); // one homogenous data just for the scaling ....
//		
//		for(int i= 0; i < space.getNumberOfDataPoints();i++)
//		{
//			datasets[i] = new SimpleDataset("", wvl, vectors.get(i));
//			SpectrumPlots[i].setDataset(datasets[i]);
//		}
		
		// adjust axis for new spectrum
		
		
		if(space.getMeasurementUnit().getUnitNumber() == MeasurementUnit.Reflectance)
		{
			// do statistics for VNIR
			double vis_nir_start = 300;
			double vis_nir_end = 1300;
			
			int start_ind = space.get_index_of_band(vis_nir_start);
			int end_ind = space.get_index_of_band(vis_nir_end);
			
			if (start_ind >= 0 & end_ind > 0)
			{
			
				VectorStatistics stats = new VectorStatistics();
			
				stats.calc_stats(vectors, start_ind, end_ind);	
				
				if (stats.standardDeviation() > 0)
					plot.setYRange(0, stats.mean()+1*stats.standardDeviation());
			
			}
//			else
//				plot.setYRange(0, stats.max());

//			pTransform1.autoScale(dataset, ChartConstants.AUTOAXES_FAR, ChartConstants.AUTOAXES_FAR);
//			pTransform1.setScaleY(0, stats.mean()+1*stats.standardDeviation());
			
		}
		else
		{
//			pTransform1.autoScale(dataset, ChartConstants.AUTOAXES_FAR, ChartConstants.AUTOAXES_FAR);
		}
		
//		xAxis.calcAutoAxis();
//		yAxis.calcAutoAxis();
		
		// set y axis title
//		yaxistitle.setAxisTitle(yAxis, this.AxisFont, this.space.getMeasurementUnit().getUnitName());
		
		plot.setYLabel(this.space.getMeasurementUnit().getUnitName());

		set_wvl_indicator(wavelength); // make sure the wavelength indicator is updated ...
		
		//plot.resetAxes();
	}
	
	
	
	public void plot(int spectrum_id)
	{

		double[] vector = space.getVector(spectrum_id);
//		double[] wvl = space.getAverageWavelengths();
		
//		Vector<VectorRegion> chunks = get_chunks();
		
//		vector = space.getVector(spectrum_id);
		
		currentDataset= 0;
		plot.clear(true);
		
		for ( int i = 0; i < vector.length; i++ ) {
			plot.addPoint(currentDataset,space.get_wvl_of_band(i), vector[i], true);
		}   
		
		wvl_indicator_dataset_no = 1;
		
//		dataset = new SimpleDataset("",wvl, vector); // one homogenous data just for the scaling ....
		
//		for(int i= 0; i < this.no_of_chunks;i++)
//		{
//			
//			if(chunks == null) // no chunks
//			{
////				datasets[i] = new SimpleDataset("", wvl, vector);
//				
//				
				
//				
//				
//			}
//			else
//			{
//				
//				VectorRegion chunk = chunks.get(i);
//				int chunk_size = chunk.end - chunk.start + 1; // always one element more than the subtraction of indices
//				double[] data_chunk = new double[chunk_size]; // always one element more than the subtraction of indices
//				double[] data_chunk_wvl = new double[chunk_size];
//				
//				for(int j = 0;j < chunk_size;j++)
//				{
//					data_chunk[j] = vector[chunk.start + j];
//					data_chunk_wvl[j] = wvl[chunk.start + j];
//				}
//				
////				datasets[i] = new SimpleDataset("",data_chunk_wvl, data_chunk);
//			}
//			
////			SpectrumPlots[i].setDataset(datasets[i]);
//		}
		
		// adjust axis for new spectrum
		
		
		if(space.getMeasurementUnit().getUnitNumber() == MeasurementUnit.Reflectance)
		{

			double vis_nir_start = 500;
			double vis_nir_end = 1700;
			
			VectorStatistics stats = new VectorStatistics();
			
			stats.calc_stats(vector, space.get_index_of_band(vis_nir_start), space.get_index_of_band(vis_nir_end));
			
			plot.setYRange(0, stats.mean()+1*stats.standardDeviation());
			
//			pTransform1.autoScale(dataset, ChartConstants.AUTOAXES_FAR, ChartConstants.AUTOAXES_FAR);
//			pTransform1.setScaleY(0, stats.mean()+1*stats.standardDeviation());
			
		}
		else
		{
//			pTransform1.autoScale(dataset, ChartConstants.AUTOAXES_FAR, ChartConstants.AUTOAXES_FAR);
		}
		
		plot.setXLabel(space.get_x_axis_label());
		plot.setYLabel(this.space.getMeasurementUnit().getUnitName());

//		
//		xAxis.calcAutoAxis();
//		yAxis.calcAutoAxis();
//		
//		// set y axis title
//		yaxistitle.setAxisTitle(yAxis, this.AxisFont, this.space.getMeasurementUnit().getUnitName());

		set_wvl_indicator(wavelength); // make sure the wavelength indicator is updated ...
		
		plot.repaint();
//		plot.invalidate();
//		plot.revalidate();
//		this.invalidate();
//		this.repaint();
		
//		cv.updateDraw();
	}
	
	
	
	public void set_wvl_indicator(double wvl)
	{
		if(show_wvl_indicator)
		{
			//System.out.println("show_wvl_indicator");
			
			wavelength = wvl;
			double[] x = new double[1]; 
			double[] y = new double[1]; 
			x[0] = wvl;
			double[] tmp = plot.getYRange();
			y[0] = tmp[1];
			y[0] -= y[0]/10;
			
			
			if(wvl_indicator_on)
			{
				// remove previous wvl indicator
				plot.clear(wvl_indicator_dataset_no);
			}
			
			wvl_indicator_on = true;
						
			plot.addPoint(this.wvl_indicator_dataset_no,wvl, 0, true);
			plot.addPoint(this.wvl_indicator_dataset_no,wvl, y[0], true);	
		
		}
		
	}
	
	
	int get_no_of_chunks()
	{
		no_of_chunks = 1;
		
		return no_of_chunks;
	}
	
	Vector<VectorRegion> get_chunks()
	{
		return null;
	}


	public boolean isShowing_wvl_indicator() {
		return show_wvl_indicator;
	}


	public void setShow_wvl_indicator(boolean show_wvl_indicator) {
		this.show_wvl_indicator = show_wvl_indicator;
	}

}
