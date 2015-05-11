package ch.specchio.proc_modules;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.client.SPECCHIOWebClientException;
import ch.specchio.gui.SPECCHIOApplication;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.spaces.ReferenceSpaceStruct;
import ch.specchio.spaces.Space;
import ch.specchio.spaces.SpectralSpace;

import static java.lang.Math.*;

public class RadianceToReflectance extends SpectralProcessingModule
{
	Hashtable<Integer, Integer> spectrum_reference_hash = null;
	SpectralSpace reference_space;
	
	ArrayList<Integer> output_spectrum_ids = new ArrayList<Integer>(); // can be different from input space because some spectralon links may be missing
	
	boolean is_spectralon = true; // otherwise it is cosine data
	
	public RadianceToReflectance(Frame owner, SPECCHIOClient specchio_client) {
		super(owner, specchio_client);
		config_menuItem.setEnabled(false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure() {
		// TODO Auto-generated method stub
		
	}

	
	public double[] get_output_space_vector() {
		// Output space is identical to input space		
		return ((SpectralSpace)get_main_input_space().getSpace()).getAverageWavelengths();
	}
	
	

	public void transform()
	{
		ArrayList<Integer> spectrum_ids = get_main_input_space().getSpace().getSpectrumIds();
		ArrayList<double[]> vectors = get_main_input_space().getSpace().getVectors();
		
		// check if dimensions of spectralon space and target space match
		if(reference_space != null)
		{
			int ref_space_dim = reference_space.getDimensionality();
			int input_space_dim = get_main_input_space().getSpace().getDimensionality();
			
			if(ref_space_dim != input_space_dim)
			{
				SPECCHIOApplication app = SPECCHIOApplication.getInstance();
				JOptionPane.showMessageDialog(app.get_frame(),"Spectral space dimensions of spectralon and target do not match!\n" +
						"Make sure that target space dimensionality is not changed by preceeding modules.\n" +
						"Radiance to Reflectance transformation will not be carried out.", "Error",
		    			JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon);
		
				reference_space = null;
			}
		}
		
		// loop over all vectors
		for(int i = 0; i < vectors.size(); i++)
		{
			// only calculate for targets
			if(!reference_space.getSpectrumIds().contains(spectrum_ids.get(i)))
			{
			
				
				double[] vector = vectors.get(i);
				double[] out_vector = new double[get_main_output_space().getSpace().getDimensionality()]; // new vector dimension is equal to output space dimension
				
				// convert each vector
				if(reference_space != null)
				{
					// get the spectralon vector for the current target spectrum id (via hash table)
					int spectrum_id = spectrum_ids.get(i);
					if(spectrum_reference_hash.containsKey(spectrum_id))
					{
						int index = spectrum_reference_hash.get(spectrum_id);
						
						
						double[] spectralon_vector = reference_space.getVector(index);
						
						for(int band = 0; band < out_vector.length;band++)
						{
							if(this.is_spectralon)
							{
								out_vector[band] = vector[band]/spectralon_vector[band];
							}
							else
							{
								out_vector[band] = vector[band]/(spectralon_vector[band]/PI);
							}
						}
					
					}
			
				}
				else
				{
					// simple copy if spectralon space could not be filled
					System.arraycopy(vector, 0, out_vector, 0, out_vector.length);
				}
			
				
				// add output vector to output space
				get_main_output_space().getSpace().addVector(out_vector);
			}
			
			// update progress bar
			set_progress((i+1)*100.0/vectors.size());
			
		}
		
	}
	

	public JPanel get_info_panel()
	{
		module_name_label = new JLabel("Radiance to Reflectance");
		settings = new JLabel("Automatic selection from DB");
		super.get_info_panel();

		return info_panel;
		
	}


	public MeasurementUnit get_measurement_unit() 
	{
		MeasurementUnit mu = null;
		try {
			mu = this.specchio_client.getMeasurementUnitFromCoding(MeasurementUnit.Reflectance);
		} catch (SPECCHIOWebClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mu;
	}

	void set_spectrum_ids_in_output_space(Space space) throws SPECCHIOClientException
	{
		// ask the server to build the necessary spaces and tables
		ReferenceSpaceStruct rss = specchio_client.getReferenceSpace(get_main_input_space().getSpace().getSpectrumIds());
		
		// save the data in which we're interested
		this.is_spectralon = rss.is_spectralon;
		this.reference_space = rss.reference_space;
		this.output_spectrum_ids = rss.spectrum_ids;
		this.spectrum_reference_hash = rss.spectrum_reference_table;
		
		if (this.reference_space == null)
		{
			JOptionPane.showMessageDialog(owner,
					"Reference spectra are not contained in one space: check if they are of the same measurement unit!\n" +
					"Radiance to Reflectance transformation will not be carried out.", "Error",
	    			JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon
				);	
		}
		
		space.setSpectrumIds(this.output_spectrum_ids);
	}	
	

}
