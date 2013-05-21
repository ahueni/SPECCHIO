package ch.specchio.proc_modules;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.spaces.SpectralSpace;

public class Delta extends SpectralProcessingModule {

	public Delta(Frame owner, SPECCHIOClient specchio_client) {
		super(owner, specchio_client);
	}

	@Override
	public void configure() {}

	@Override
	public MeasurementUnit get_measurement_unit() {
		// identical to input space unit
		return ((SpectralSpace)get_main_input_space().getSpace()).getMeasurementUnit();
	}

	@Override
	public double[] get_output_space_vector() {
		// Output space is identical to input space		
		return ((SpectralSpace)get_main_input_space().getSpace()).getAverageWavelengths();
	}
	
	void define_required_input_spaces()
	{
		required_input_spaces.clear();
		required_input_spaces.add(new RequiredInputSpaceStruct("Spectra A", null));
		required_input_spaces.add(new RequiredInputSpaceStruct("Spectra B", null));
	}
	
	public void define_output_spaces()
	{
		output_space_definitions.add(new OutputSpaceStruct("Delta"));
	}

	@Override
	public void transform() {
		ArrayList<double[]> A = input_spaces.get(0).getSpace().getVectors();
		ArrayList<double[]> B = input_spaces.get(1).getSpace().getVectors();
		
		int dim = input_spaces.get(0).getSpace().getDimensionality();
		
		// loop over all vectors
		for(int i = 0; i < A.size(); i++)
		{
			double[] a = A.get(i);
			double[] b = B.get(i);
			double[] out_vector = new double[dim]; // new vector dimension is equal to output space dimension
			
			for(int band = 0; band < out_vector.length;band++)
			{
				out_vector[band] = a[band]-b[band];
			}

			
			// add output vector to output space
			get_main_output_space().getSpace().addVector(out_vector);
			
			// update progress bar
			set_progress((i+1)*100.0/A.size());
			
		}

	}
	
	public JPanel get_info_panel()
	{
		module_name_label = new JLabel("Delta");
		settings = new JLabel("Delta = A - B");
		super.get_info_panel();
		return info_panel;		
	}

}
