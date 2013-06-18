package ch.specchio.plots;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="gonio_position")
public class GonioPosition {
	
	@XmlElement(name="azimuth") public double azimuth;
	@XmlElement(name="zenith") public double zenith;
	
	@XmlElement(name="x") public double x;
	@XmlElement(name="y") public double y;
	@XmlElement(name="z") public double z;
	
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
