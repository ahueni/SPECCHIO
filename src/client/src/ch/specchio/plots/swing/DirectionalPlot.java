package ch.specchio.plots.swing;

import java.awt.Color;
import java.awt.Font;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOWebClientException;
import ch.specchio.interfaces.ProgressReportInterface;
import ch.specchio.plots.GonioPosition;
import ch.specchio.plots.GonioSamplingPoints;
import ch.specchio.spaces.SpectralSpace;

//import com.quinncurtis.chart3djava.*;

import javax.swing.JPanel;

public class DirectionalPlot extends JPanel{

	private static final long serialVersionUID = 1L;
	
	SpectralSpace space;
	int band;
	GonioSamplingPoints sampling_points;
//	ChartView cv;
//	ContourDataset dataset;
//	ContourPlot thePlot1;
	int numcontourlevels = 50;
	double contourlevels[];
//	ChartAttribute attribs[];
//	CartesianCoordinates pTransform1;
//	LinearAxis xAxis, yAxis, zAxis;
	ProgressReportInterface pr;
	
	public DirectionalPlot(SpectralSpace space, int x_size, int y_size, ProgressReportInterface pr, SPECCHIOClient specchio_client)
	{
		this.space = space;
		this.pr = pr;
		pr.set_component("Directional Plot");
		
//		cv = new ChartView();
//		cv.setPreferredSize(x_size, y_size);
		
		pr.set_operation("Loading sampling points");
		try {
			sampling_points = specchio_client.getSensorSamplingGeometry(space);
//			attribs = new ChartAttribute[numcontourlevels+1];
			
			pr.set_operation("Preparing Z coords");
			sampling_points.prepare_z_coords(band);

			plot();			
			
		} catch (SPECCHIOWebClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
	
	public void plot()
	{	
	
//		dataset = getContourDataset();
//
//		pTransform1 = new CartesianCoordinates(-1, -1, sampling_points.get_z_statistics().min, 1, 1, sampling_points.get_z_statistics().max);
//		
//		   pTransform1.setGraphBorderDiagonal(0.10, .10, .8, 0.85) ;
//		   Background background = new Background( pTransform1, ChartConstants.GRAPH_BACKGROUND, Color.white);
//		   cv.addChartObject(background);
//
//		  xAxis = new LinearAxis(pTransform1, ChartConstants.X_AXIS);
//		  cv.addChartObject(xAxis);
//
//
//		  yAxis = new LinearAxis(pTransform1, ChartConstants.Y_AXIS);
//		  cv.addChartObject(yAxis);
//		  
//		  zAxis = new LinearAxis(pTransform1, ChartConstants.Z_AXIS);
//		  cv.addChartObject(zAxis);
//
//		  NumericAxisLabels xAxisLab = new NumericAxisLabels(xAxis );
//		  cv.addChartObject(xAxisLab);
//
//		  NumericAxisLabels yAxisLab = new NumericAxisLabels(yAxis);
//		  cv.addChartObject(yAxisLab);
//		  
//		  NumericAxisLabels zAxisLab = new NumericAxisLabels(zAxis);
//
//		  // dataset specific intervals
//		  create_interval_classes();
//		  
//		  // colouring according to number of intervals
//		  create_colour_table(false);
//
//		  boolean lineflags[] = new boolean[numcontourlevels];
//		  for (int i=0; i < numcontourlevels; i++)
//		    lineflags[i] = false;
//
//		  boolean labelflags[] = new boolean[numcontourlevels];
//		  for (int i=0; i < numcontourlevels; i++)
//		    labelflags[i] = false;
//
//		  thePlot1 = new ContourPlot(pTransform1, dataset, contourlevels, attribs, lineflags, labelflags, numcontourlevels, ChartConstants.CONTOUR_FILL);
//
//		  
//		cv.addChartObject(thePlot1);
//		
//		
//		Font theTitleFont = new Font("SansSerif", Font.BOLD,14);
//
//		ChartTitle mainTitle = new ChartTitle( theTitleFont, "Hemispherical " + space.getMeasurementUnit().getUnitName() + " Plot");
//		  mainTitle.setTitleType(ChartConstants.CHART_HEADER);
//		  mainTitle.setTitlePosition( ChartConstants.CENTER_GRAPH);
//
//		  cv.addChartObject(mainTitle);
//		  
//		// Add a rotate control button to the upper left corner
//		RotateButtonUserControl rotatebutton = new RotateButtonUserControl(pTransform1);
//		cv.setLayout(null);
//		rotatebutton.setSize ( 32,32);
//		rotatebutton.setLocation (8,8);
//		cv.add(rotatebutton);  
//
//
//		  this.add(cv);
		  
	}
	
	
	void create_colour_table(boolean rg_switch)
	{  
		  int red = 0;
		  Color color;
		  
		  int red_delta = 255/(numcontourlevels+1);
		  
		  for (int i=0; i <= numcontourlevels; i++)
		  {
			  if(rg_switch)
			  {
				  color = new Color(0, red, 0);
			  }
			  else
				  color = new Color(red, 0, 0);
			  
			  //color = new Color(50, 255, 10);
			  
			  red += red_delta;
			  //attribs[i] = new ChartAttribute(Color.black,1,ChartConstants.LS_SOLID,color);
//			  attribs[i] = new ChartAttribute(Color.black,1,ChartConstants.LS_SOLID,Color.ORANGE);
//			  //attribs[i].setFillFlag(false);
//			  attribs[i].setFillFlag(true);
//			  attribs[i].setLineFlag(true);
		  }	
	}
	
	void create_interval_classes()
	{
		  // get statistics of z values and build equal intervals classes  
		  contourlevels = new double[numcontourlevels];
		  double class_value = sampling_points.get_z_statistics().min;
		  double interval = sampling_points.get_z_statistics().get_interval_size(numcontourlevels);
		  for (int i=0; i < numcontourlevels; i++)
		  {
			  contourlevels[i] = class_value;
			  class_value += interval;
		  }	
	}
	
	
	public void change_colour()
	{
//		 create_colour_table(true);
//
//		  thePlot1.setContourPlotAttributes(contourlevels, attribs, numcontourlevels, ChartConstants.CONTOUR_FILL);
//		  
//		  cv.updateDraw();
	}
	
	public void change_wavelength(int band)
	{
		this.band = band;
		
		sampling_points.prepare_z_coords(band);		
		
//		dataset = getContourDataset();
//		
//		create_interval_classes();
//		
//		thePlot1.setDataset(dataset);
//		
//		update_coord_system();
//		change_colour();
//		
//		cv.updateDraw();
	}
	
//	private ContourDataset getContourDataset()
//	{
//		GonioPosition[] pos = sampling_points.get_positions();
//		ChartPoint3D[] pointarray = new ChartPoint3D[pos.length];
//		
//		for(int i=0;i<pos.length;i++)
//		{
//			GonioPosition cur_pos = pos[i];
//			ChartPoint3D p = new ChartPoint3D();
//			p.setLocation(cur_pos.x, cur_pos.y);
//			pointarray[i] = p;
//		}
//		
//		return new ContourDataset("Contour Dataset",pointarray);
//	}
	

	class graph_animation extends Thread
	{
		
		public void run()
		{
			
			for(int i = 0;i < space.getDimensionality();i++)
			{
				//band_slider.setValue(i);
				//this.band_slider.
				
				change_wavelength(i);
				
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}		
			
			
		}
		
		
		
	}
	
	
	public void loop_wavelength()
	{
		graph_animation ga = new graph_animation();
		ga.start();

	
	}
	
	void update_coord_system()
	{
//		this.pTransform1.setCoordinateBounds(-1, -1, sampling_points.get_z_statistics().min, 1, 1, sampling_points.get_z_statistics().max);
//		//zAxis = new LinearAxis(pTransform1, ChartConstants.Z_AXIS);
//		zAxis.calcAutoAxis(sampling_points.get_z_statistics().min, sampling_points.get_z_statistics().max);
//		xAxis.calcAutoAxis();
//		yAxis.calcAutoAxis();
	}

	


	
	/*
	
	int get_r1_sign(float alpha)
	{
		if(alpha > 180)
			return -1;
		else
			return 1;
	}
	
	int get_r2_sign(float alpha)
	{
		if(alpha > 90 && alpha < 270)
			return -1;
		else
			return 1;	
	}
	*/

}
