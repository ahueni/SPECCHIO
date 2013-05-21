package ch.specchio.types;

import java.util.ArrayList;

@SuppressWarnings("hiding")
public class MatlabAdaptedArrayList<Object> extends ArrayList<Object>
{

	private static final long serialVersionUID = 1L;

	public MatlabAdaptedArrayList()
	{
		super();
	}
	
	public double[] get_as_double_array()
	{
		double [] out = new double [this.size()];
		
		System.out.println("Class: " + this.get(0).getClass());
		
		if(this.get(0).getClass().getSimpleName().equals("Integer"))
		{
			for (int i=0;i<this.size();i++)
			{
				out[i] = (Integer) this.get(i);
			}				
			
		}
		
		if(this.get(0).getClass().getSimpleName().equals("Double"))
		{
			for (int i=0;i<this.size();i++)
			{
				out[i] = (Double) this.get(i);
			}						
		}		
		
		return out;
	}

	
	
}

