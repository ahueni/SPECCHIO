package ch.specchio.plots;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

import ch.specchio.spaces.SpectralSpace;

@XmlRootElement(name="gonio_sampling_points")
public class GonioSamplingPoints 
{
	
//	ChartPoint3D pointarray[];
	GonioPosition angle_array[];
	Integer spectrum_id_array[];
	SpectralSpace space;
	z_statistics z_stats; 
	
	public GonioSamplingPoints()
	{
		
	}
	
	public GonioSamplingPoints(SpectralSpace space)
	{
		this.space = space;
		int no_of_datapoints = space.getNumberOfDataPoints();
		
//		pointarray = new ChartPoint3D[no_of_datapoints];
		angle_array = new GonioPosition[no_of_datapoints];
		spectrum_id_array = new Integer[no_of_datapoints];
	}
	
	@XmlElement(name="angle_array")
	public GonioPosition[] getAngleArray() { return this.angle_array; }
	public void setAngleArray(GonioPosition[] angle_array) { this.angle_array = angle_array; }
	public void setAngle(int i, GonioPosition angle) { this.angle_array[i] = angle; }
	
//	@XmlElement(name="pointarray")
//	public ChartPoint3D[] getPointArray() { return this.pointarray; }
//	public void setPointArray(ChartPoint3D[] pointarray) { this.pointarray = pointarray; }
//	public void setPoint(int i, ChartPoint3D point) { this.pointarray[i] = point; }
	
	@XmlElement(name="spectrum_id_array")
	public Integer[] getSpectrumIdArray() { return this.spectrum_id_array; }
	public void setSpectrumIdArray(Integer[] spectrum_id_array) { this.spectrum_id_array = spectrum_id_array; }
	public void setSpectrumId(int i, Integer spectrum_id) { this.spectrum_id_array[i] = spectrum_id; }
	
	@XmlElement(name="space")
	public SpectralSpace getSpace() {
		return space;
	}
	public void setSpace(SpectralSpace space) {
		this.space = space;
	}
	
	
	public void prepare_z_coords(int band)
	{
		ArrayList<double[]> vectors = space.getVectors();
		
		// init statistics with first vector value
		z_stats = new z_statistics(vectors.get(0)[band]);
			
		for(int s = 0; s < angle_array.length;s++)
		{
			Double z = vectors.get(s)[band];
			z_stats.update(z);
			angle_array[s].z=z;	
		}
	}
	
	public int get_spectrum_id(int point_index)
	{
		return this.spectrum_id_array[point_index];
	}
	
	
	public int get_index(int spectrum_id)
	{
		int index = -1;
		int i = 0;
		
		while(index == -1 && i < spectrum_id_array.length)
		{
			if(spectrum_id_array[i] == spectrum_id)
			{
				index = i;
			}			
			i++;
		}
		
		return index;
	}
	
//	public ChartPoint3D[] get_points()
//	{
//		return this.pointarray;
//	}
	
	public GonioPosition[] get_positions()
	{
		return this.angle_array;
	}
	
	public z_statistics get_z_statistics()
	{
		return this.z_stats;
	}


}
