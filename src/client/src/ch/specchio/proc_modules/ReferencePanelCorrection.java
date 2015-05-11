package ch.specchio.proc_modules;

import java.awt.Frame;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.spaces.SpectralSpace;
import ch.specchio.types.SpectrumFactorTable;


/* Convention:
 * input space 0 = spectra to be corrected
 * input space 1 = correction factors
 */

public class ReferencePanelCorrection extends SpectralProcessingModule {
	
	public ReferencePanelCorrection(Frame owner, SPECCHIOClient specchio_client) {
		super(owner, specchio_client);
	}

	@Override
	public void configure() {
		// TODO Auto-generated method stub

	}

	
	public MeasurementUnit get_measurement_unit() {
		// identical to input space unit
		return ((SpectralSpace)get_main_input_space().getSpace()).getMeasurementUnit();
	}

	public double[] get_output_space_vector() {
		// Output space is identical to input space		
		return ((SpectralSpace)get_main_input_space().getSpace()).getAverageWavelengths();
	}
	
	void define_required_input_spaces()
	{
		required_input_spaces.clear();
		required_input_spaces.add(new RequiredInputSpaceStruct("Primary Spectra", null));
		required_input_spaces.add(new RequiredInputSpaceStruct("Correction Factors", null));
	}
	
	public void define_output_spaces()
	{
		output_space_definitions.add(new OutputSpaceStruct("Ref. Panel corr. " + ((SpectralSpace)get_main_input_space().getSpace()).getMeasurementUnit().getUnitName()));
	}
	
	public ArrayList<SpaceProcessingChainComponent> create_output_spaces() throws SPECCHIOClientException
	{
		// dimensionality check
		int dim1 = input_spaces.get(0).getSpace().getDimensionality();
		int dim2 = input_spaces.get(1).getSpace().getDimensionality();
		
		if(dim1 != dim2)
		{
			JOptionPane.showMessageDialog(owner,"Cannot create output space because the input spaces have differing number of bands. \nPlease use a waveband filter to cut superfluous bands.");
			valid_configuration = false;
		}
		else
		{
			return super.create_output_spaces();
		}
		
		return null;
	}


	public void transform() throws SPECCHIOClientException
	{
		if(valid_configuration)
		{
			ArrayList<Integer> spectrum_ids = input_spaces.get(0).getSpace().getSpectrumIds();
			ArrayList<double[]> vectors = input_spaces.get(0).getSpace().getVectors();

			System.out.println("Correcting for panel non-idealness");

			SpectrumFactorTable spectrum_factor_table = specchio_client.getSpectrumFactorTable(
					input_spaces.get(0).getSpace().getSpectrumIds(),
					input_spaces.get(1).getSpace().getSpectrumIds()
					);


			// loop over all vectors
			for(int i = 0; i < vectors.size(); i++)
			{
				double[] spectrum_vector = vectors.get(i);
				double[] out_vector = new double[input_spaces.get(0).getSpace().getDimensionality()]; // new vector dimension is equal to output space dimension

				int spectrum_id = spectrum_ids.get(i);
				int curr_factor_id = spectrum_factor_table.getFactor(spectrum_id);

				double[] factor_vector = input_spaces.get(1).getSpace().getVector(curr_factor_id);

				// convert each vector

				for(int band = 0; band < out_vector.length;band++)
				{
					out_vector[band] = spectrum_vector[band]*factor_vector[band];
				}


				// add output vector to output space
				get_main_output_space().getSpace().addVector(out_vector);

				// update progress bar
				set_progress((i+1)*100.0/vectors.size());

			}
		}
		
	}
	

	public JPanel get_info_panel()
	{
		module_name_label = new JLabel("Correct for Panel");
		settings = new JLabel("Automatic selection from DB");
		super.get_info_panel();

		return info_panel;
		
	}


}
