package ch.specchio.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Point2D")
public class Point2D {

		private double x;
		private double y;
	
	public Point2D() {
		// TODO Auto-generated constructor stub
	}
	
	// specify coordinates as lat=y and lon=x
	public Point2D(double y, double x) {
		this.setX(x);
		this.setY(y);
	}

	@XmlElement(name="x")
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	@XmlElement(name="y")
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}	

}
