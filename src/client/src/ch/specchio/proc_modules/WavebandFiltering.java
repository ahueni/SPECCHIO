package ch.specchio.proc_modules;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.spaces.SpectralSpace;
		

public class WavebandFiltering extends SpectralProcessingModule implements ModuleCallback
{

		
		Vector<WavebandRegion> filters = new Vector<WavebandRegion>();
		Vector<VectorRegion> chunks;
		//int no_of_chunks;
		
		ArrayList<Boolean> mask;
		
		public WavebandFiltering(Frame owner, SPECCHIOClient specchio_client) {
			super(owner, specchio_client);
			
			WavebandRegion wf =  new WavebandRegion(1400, 1500);
			filters.add(wf);

		}

		public JPanel get_info_panel()
		{
			
			module_name_label = new JLabel("Waveband filtering");
			settings = new JLabel("");

			super.get_info_panel();

			return info_panel;
			
		}

		// return a vector with the wavelengths that passed the filters
		// also calculate a filtering mask to be applied to the data vectors
		public double[] get_output_space_vector() 
		{
			boolean filtered = false;
			//boolean chunk_ended = false;
			//no_of_chunks = 0;
			chunks = new Vector<VectorRegion>();
			int start_ind = -1;
			int end_ind = -1;
			
			double[] in_wvl = ((SpectralSpace)get_main_input_space().getSpace()).getAverageWavelengths();
			int dim = 0;
			
			mask = new ArrayList<Boolean>();
			
			// prepare mask and calculate new dimension
			for(int i = 0; i < in_wvl.length; i++)
			{
				for(int j = 0; j < filters.size();j++)
				{
					if(in_wvl[i] >= filters.get(j).start && in_wvl[i] <= filters.get(j).end)
					{
						filtered = true;
						//chunk_ended = true;
						
						// store new chunk if indices are ready
						if(start_ind != -1)
						{
							this.chunks.add(new VectorRegion(start_ind, end_ind));
							start_ind = -1;
						}
						
					}				
				}
				
				if(filtered == false)
				{
					dim++;
					
					// chunk stuff ...
					if(start_ind == -1)
					{
						start_ind = dim-1;
					}
					end_ind = dim-1;
					
					// update the chunk count
					//if(chunk_ended == true || no_of_chunks == 0)
					{
					//	no_of_chunks++;
					//	chunk_ended = false;
					}
				}
				
				// fill the mask
				mask.add(filtered);
				
				filtered = false;
							
			}
			
			// final update of the chunks if still needed (happens if there is no filter at the very end)
			if(start_ind != -1)
			{
				this.chunks.add(new VectorRegion(start_ind, end_ind));
			}

			
			// create output array with new dimension
			double[] out_wvl = new double[dim];
			
			this.filter_vector(in_wvl, out_wvl);
			
			
			return out_wvl;
		}


		public void configure() {
		
			// bring up the config window
			DialogThread dt = new DialogThread(new WavebandFilterDialog(this.filters, get_coords_for_popups()), this, 1);
			dt.start();
		}

		public void user_data_provided(DialogThread dt) throws SPECCHIOClientException {
			boolean ret_val = dt.ret_val;
			
			super.user_data_provided(dt);
			
			if(ret_val == true)
			{
				if(dt.callback_value == Module.CONFIG)
				{
					// reconfigure
					exchange_filter(((WavebandFilterDialog)dt.md).get_filters());
				}
			}
			
		}
		
		
		public void exchange_filter(Vector<WavebandRegion> filters) throws SPECCHIOClientException
		{
			this.filters = filters;
			update_output_spaces();
		}


		public void transform() 
		{
			
			ArrayList<double[]> vectors = get_main_input_space().getSpace().getVectors();

			System.out.println("Filtering wavebands");
			
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
		
		void filter_vector(double[] in_vector, double[] out_vector)
		{
			int out_index = 0;
			for(int j=0; j<in_vector.length; j++)
			{
				if(mask.get(j) == false)
				{
					// wavelength not filtered
					out_vector[out_index++] = in_vector[j];
				}
			}
		
		}

	
		public MeasurementUnit get_measurement_unit() {
			
			
			return ((SpectralSpace)get_main_input_space().getSpace()).getMeasurementUnit();
		}
		
		
		public int get_no_of_chunks()
		{
			return chunks.size();			
		}
		
		public Vector<VectorRegion> get_chunk_regions()
		{
			return chunks;
		}

		
	}
