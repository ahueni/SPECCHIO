package ch.specchio.spaces;

import java.util.Hashtable;

import javax.xml.bind.annotation.*;

import ch.specchio.constants.SpaceTypes;

@XmlRootElement(name="spectral_space")
@XmlSeeAlso({RefPanelCalSpace.class,SensorAndInstrumentSpace.class})
public class SpectralSpace extends Space {
	
		double[] avg_wavelengths;
		Hashtable<Double, Integer> wvl_hash = null;
		MeasurementUnit unit;
		
		public SpectralSpace()
		{
			super();
		}
		
		public SpectralSpace(MeasurementUnit unit)
		{
			super();

			this.SpaceTypeName = "Spectral Space";
			this.unit = unit;
		}
		
		public int getSpaceType()
		{
			return SpaceTypes.SpectralSpace;
		}

		
		@XmlElement(name="avg_wavelengths")
		public double[] getAverageWavelengths()
		{
			
			if(!this.wvls_are_known && this.dimensionality!= null)
			{
				avg_wavelengths = new double[this.dimensionality];
				
				for(int i=1;i<=this.dimensionality; i++)
				{
					avg_wavelengths[i-1] = i;
				}
				
			}
			
			return avg_wavelengths;
		}
		
		
		public void setAverageWavelengths(double[] avg_wvl)
		{
			// if the avg wvl are null, then the space is invalid!
			// this can happen if a processing module cannot create a valid output space (e.g. no data found in the DB)
			if(avg_wvl != null)
			{
				avg_wavelengths = new double[avg_wvl.length];			
				System.arraycopy(avg_wvl, 0, avg_wavelengths, 0, avg_wvl.length);			
				dimensionality = avg_wavelengths.length;
				this.dimensionality_is_set = true;
				this.wvls_are_known = true;
			}
			else
			{
				dimensionality = 0;
			}
		}
		
		
		@XmlElement(name="unit")
		public MeasurementUnit getMeasurementUnit() { return this.unit; }
		public void setMeasurementUnit(MeasurementUnit unit) { this.unit = unit; }
		
		public double get_dimension_number(int dim_index)
		{
			return avg_wavelengths[dim_index]; 
		}
		
		public void build_wavelength_hash()
		{
			wvl_hash = new Hashtable<Double, Integer>();
			
			for(Integer i = 0; i < this.avg_wavelengths.length;i++)
			{								
				wvl_hash.put(avg_wavelengths[i], i);
			}	
		}
		
		public int get_index_of_band(Double band_wvl)
		{
			if(wvl_hash == null)
				build_wavelength_hash();
			
			Integer index = wvl_hash.get(band_wvl);
			
			if(index == null)
			{
				index = get_nearest_band(band_wvl);
			}
			
			return index;
		}
		
		public int get_nearest_band(Double band_wvl)
		{
			int index = 0;
			int search_dir = 0;
			
			// restrict to wavelength interval of the sensor
			if(band_wvl < this.avg_wavelengths[0])
				band_wvl = this.avg_wavelengths[0];		
			if(band_wvl > this.avg_wavelengths[avg_wavelengths.length-1])
				band_wvl = this.avg_wavelengths[avg_wavelengths.length-1];
			
			
			// educated first guess via position ratio
			double pos_ratio = (band_wvl - avg_wavelengths[0])/(avg_wavelengths[avg_wavelengths.length-1] -avg_wavelengths[0]);
			
			Double band_pos_float = avg_wavelengths.length*pos_ratio;
			
			int band_pos_guess = Math.min(band_pos_float.intValue(), avg_wavelengths.length-1); // restrict to the maximal number of bands (for sensors that only sample smaller portion of spectrum)
			
			// define further search direction by calculating the wavelength delta of guess band and one band up (if existing, else, search one band down)
			double wavl1 = get_wvl_of_band(band_pos_guess);
			if(band_pos_guess == avg_wavelengths.length-1)
			{
				// guess band is last band, thus search downward
				search_dir = -1;
			}
			else
			{
				// search upward
				search_dir = 1;
			}
			
			double wavl2 = get_wvl_of_band(band_pos_guess + search_dir);
			
			double delta1 = Math.abs(band_wvl - wavl1);
			double delta2 = Math.abs(band_wvl - wavl2);
			
			if(delta1 > delta2)
			{
				// search in direction of wavl2
				// means the current search direction is correct
				
			}
			else
			{
				// search in direction of wavl1 (away from wavl2)
				// means a switch in search direction is needed
				search_dir*=-1;
			}
			
			double delta = delta1;
			double new_delta = delta1;
			int band = band_pos_guess + search_dir;
			int closest_band = band_pos_guess;
			boolean stop =false;
			
			// search for minimum
			while(!stop && band >0 && band < avg_wavelengths.length)
			{
				new_delta = Math.abs(band_wvl - get_wvl_of_band(band));
				
				if(new_delta >= delta)
				{
					stop = true;
					closest_band = band;
				}
				else
				{
					delta = new_delta;
					band+=search_dir;
				}
			}
			
			index = closest_band;		
			
			return index;
		}
		
		// input band number starts at 0
		public double get_wvl_of_band(int band)
		{
			return this.avg_wavelengths[band];
		}



		public int space_type() {
			return SpaceTypes.SpectralSpace;
		}
		
		
//		public boolean isWavenumber()
//		{
//			if(this.avg_wavelengths[0] < this.avg_wavelengths[this.dimensionality-1])
//				return false;
//			else
//				return true;			
//			
//		}


		public String get_x_axis_label() {
			
			String x_label = "Wavelength [nm]";
			
//			if (isWavenumber())
//				x_label = "Wavenumber [cm-1]";
			
			if(!this.wvls_are_known)
			{
				x_label = "Band number";
			}
			
			return x_label;
		}

}
