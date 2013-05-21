package ch.specchio.proc_modules;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.spaces.SensorAndInstrumentSpace;
import ch.specchio.spaces.SpectralSpace;
import ch.specchio.types.Instrument;
import ch.specchio.types.Sensor;


public abstract class BandTypeFilter extends SensorAndInstrumentProcessingModule{
	
	int ElementToFilter;

	public BandTypeFilter(Frame owner, SPECCHIOClient specchio_client) {
		super(owner, specchio_client);
		config_menuItem.setEnabled(false);
	}
	
	public JPanel get_info_panel()
	{		
		if(get_main_input_space() != null && get_main_output_space() != null)
			settings = new JLabel("Filtering " + Integer.toString(get_main_input_space().getSpace().getDimensionality() - get_main_output_space().getSpace().getDimensionality()) + " bands.");
		else
			settings = new JLabel("");

		super.get_info_panel();
		return info_panel;		
	}

	@Override
	public void configure() {
		// TODO Auto-generated method stub
		
	}


	public MeasurementUnit get_measurement_unit() {
		return ((SpectralSpace)get_main_input_space().getSpace()).getMeasurementUnit();
	}

	public double[] get_output_space_vector() throws SPECCHIOClientException 
	{
		double[] in_wvl = ((SpectralSpace)get_main_input_space().getSpace()).getAverageWavelengths();
//		int instr_id = ((SensorAndInstrumentSpace)get_main_input_space().getSpace()).getInstrumentId();
//		Instrument instr = specchio_client.getInstrument(instr_id);
		int dim = 0;
		
		Sensor sensor = ((SensorAndInstrumentSpace)get_main_input_space().getSpace()).getSensor();
		
		// work out the new dimension
		for(int i = 0;i < get_main_input_space().getSpace().getDimensionality();i++)
		{
			if(sensor.getElementTypes()[i] != ElementToFilter)
			{
				dim++;
			}
		}
		
		
		// create output array with new dimension
		double[] out_wvl = new double[dim];
		
		this.filter_vector(in_wvl, out_wvl);
				
		return out_wvl;
	}

	public void transform() throws SPECCHIOClientException 
	{
		ArrayList<double[]> vectors = get_main_input_space().getSpace().getVectors();

		System.out.println("Filtering broadbands");
		
		// loop over all vectors
		for(int i = 0; i < vectors.size(); i++)
		{
			double[] vector = vectors.get(i);
			double[] out_vector = new double[get_main_output_space().getSpace().getDimensionality()]; // new vector dimension is equal to output space dimension
			
			// filter each vector
			filter_vector(vector, out_vector);
			
			// add output vector to output space
			get_main_output_space().getSpace().addVector(out_vector);	
			
			// update progress bar
			set_progress((i+1)*100.0/vectors.size());
		}
		
	}

	
	void filter_vector(double[] in_vector, double[] out_vector) throws SPECCHIOClientException
	{

		Sensor sensor = ((SensorAndInstrumentSpace)get_main_input_space().getSpace()).getSensor();
		int out_index = 0;
		for(int j=0; j<in_vector.length; j++)
		{
			if(sensor.getElementTypes()[j] != ElementToFilter)
			{
				// band not filtered
				out_vector[out_index++] = in_vector[j];
			}
		}
	
	}

}
