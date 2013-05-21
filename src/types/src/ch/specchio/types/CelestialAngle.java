package ch.specchio.types;

/**
 * Class representing celestion angles.
 */
public class CelestialAngle {
	
	/** angle to azimuth */
	public double azimuth;
	
	/** angle to zenith */
	public double zenith;
	
	/**
	 * Default constructor.
	 */
	public CelestialAngle() {
		
		this(0.0, 0.0);
	
	}
	
	/**
	 * Constructor.
	 * 
	 * @param azimuthIn	the angle to the azimuth
	 * @param zenithIn	the angle to the zenith
	 */
	public CelestialAngle(double azimuthIn, double zenithIn) {
		
		azimuth = azimuthIn;
		zenith = zenithIn;
		
	}
	
	
	/**
	 * Copy constructor.
	 * 
	 * @param other	the object to be copied
	 */
	public CelestialAngle(CelestialAngle other) {
		
		this(other.azimuth, other.zenith);
		
	}

}
