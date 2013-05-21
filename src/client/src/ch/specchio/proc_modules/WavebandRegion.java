package ch.specchio.proc_modules;

public class WavebandRegion
{
	float start;
	float end;	
	
	public WavebandRegion(float start, float end)
	{
		set_range(start, end);
	}
	
	public String toString()
	{
		return Float.toString(start) + " - " + Float.toString(end);
	}
	
	public void set_range(float start, float end)
	{
		this.start = start;
		this.end = end;
	}
}
