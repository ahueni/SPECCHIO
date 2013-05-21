package ch.specchio.proc_modules;

public class VectorRegion {
	public int start;
	public int end;	
	
	public VectorRegion(int start, int end)
	{
		set_range(start, end);
	}
	
	public void set_range(int start, int end)
	{
		this.start = start;
		this.end = end;
	}
	
}
