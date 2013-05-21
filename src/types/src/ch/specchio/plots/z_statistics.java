package ch.specchio.plots;

public class z_statistics
{
	public Double min = 0.0;
	public Double max = 0.0;
	
	public z_statistics(Double init)
	{
		this.min = init;
		this.max = init;			
	}
	
	public void update(Double curr)
	{
		if(curr > max)
			max = curr;
		
		if(curr < min)
			min = curr;
	}
	
	public double get_interval_size(int no_of_classes)
	{
		
		return (max-min)/no_of_classes;
		
	}

}