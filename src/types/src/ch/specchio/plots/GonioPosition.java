package ch.specchio.plots;

public class GonioPosition {
	
	public Double azimuth;
	public Double zenith;
	
	public Double x;
	public Double y;
	public Double z;
	
	public GonioPosition()
	{
		
	}
	
	public GonioPosition(double az, double zn)
	{
		this.azimuth = az;
		this.zenith = zn;
	}
	
	public GonioPosition(double az, double zn, double x, double y)
	{
		this(az, zn);
		this.x=x;
		this.y =y;		
	}
	
	public GonioPosition(double az, double zn, double x, double y, double z)
	{
		this(az, zn,x,y);
		this.z=z;
	}

}
