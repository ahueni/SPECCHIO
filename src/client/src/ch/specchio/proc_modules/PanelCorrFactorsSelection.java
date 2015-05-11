package ch.specchio.proc_modules;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.constants.SpaceTypes;
import ch.specchio.gui.SPECCHIOApplication;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.spaces.MeasurementUnitFactory;
import ch.specchio.spaces.Space;
import ch.specchio.spaces.SpectralSpace;

public class PanelCorrFactorsSelection extends SpectralProcessingModule {
	
	int cal_factors_sensor_id = 0;
	Space cal_spaces[] = null;
	
	public PanelCorrFactorsSelection(Frame owner, SPECCHIOClient specchio_client)
	{
		super(owner, specchio_client);
		config_menuItem.setEnabled(false);
	}
		
	
	@Override
	public void configure() {
		// TODO Auto-generated method stub

	}

	// these factors can be regarded as reflectance values
	public MeasurementUnit get_measurement_unit() {
		MeasurementUnitFactory muf = MeasurementUnitFactory.getInstance();
		return muf.create_from_coding(MeasurementUnit.Reflectance);
	}
	
	public JPanel get_info_panel()
	{
		
		module_name_label = new JLabel("Get Panel Correction Factors");
		settings = new JLabel("");

		super.get_info_panel();

		return info_panel;
		
	}
	
	public void define_output_spaces()
	{
		try {
			cal_spaces = specchio_client.getSpectrumCalibrationSpaces(get_main_input_space().getSpace().getSpectrumIds());
		} catch (SPECCHIOClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		output_space_definitions.add(new OutputSpaceStruct("Ref. panel corr factors"));
	}
	
	public int get_output_space_type() {
		return SpaceTypes.RefPanelCalSpace;
	}	
	
	public double[] get_output_space_vector() throws SPECCHIOClientException 
	{
		cal_spaces = specchio_client.getSpectrumCalibrationSpaces(get_main_input_space().getSpace().getSpectrumIds());
		
		// only considering one space!
		if(cal_spaces.length == 0) // return null if there was no calibration information
		{
			JOptionPane.showMessageDialog(null, "No calibration data found. \nCheck that a reference panel is set for the input spectra and \ncalibration data exists for the reference panel.", "Error",
	    			JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon);
			set_operation("No data found!");
			return null;			
		}
		
		SpectralSpace tmp_space = (SpectralSpace)cal_spaces[0];
		
		//cal_factors_sensor_id = ((RefPanelCalSpace) cal_spaces[0]).getSensor().getSensorId();
		
		return tmp_space.getAverageWavelengths();

	}
	
	public ArrayList<SpaceProcessingChainComponent> create_output_spaces() throws SPECCHIOClientException
	{
		output_spaces = new ArrayList<SpaceProcessingChainComponent>();
		
		define_output_spaces();
		
		SpaceProcessingChainComponent spcc = null;
		if (cal_spaces.length > 0) {
			spcc = new SpaceProcessingChainComponent(owner, cal_spaces[0]);
		}
		
		// check if the new space is valid
		// if a module cannot create a space, then it sets the dimension to zero
		if (spcc != null && cal_spaces[0] != null && cal_spaces[0].getDimensionality() > 0)
		{
			spcc.set_space_name(output_space_definitions.get(0).space_name);
			set_spectrum_ids_in_output_space(cal_spaces[0]);
			output_spaces.add(spcc);
		}
		else
		{
			JOptionPane.showMessageDialog(owner,"Cannot create output space because the input spectra have no wavelength reference. \nPlease set a sensor with the correct wvls in the metadata editor.", "Error",
	    			JOptionPane.ERROR_MESSAGE, SPECCHIOApplication.specchio_icon);
		}
		
		return output_spaces;
	}
	
	
	
	public int get_sensor_id()
	{
		return cal_factors_sensor_id;
	}
	
	// override original method as the spectrum_ids are the ones containing the panel correction factors
	void set_spectrum_ids_in_output_space(Space space)
	{
		// build the list of unique spectrum ids in the calibration spaces
		ArrayList<Integer> cal_factor_ids = new ArrayList<Integer>();
		if (cal_spaces != null) {
			for (Space cal_space : cal_spaces) {
				for (Integer id : cal_space.getSpectrumIds()) {
					if (!cal_factor_ids.contains(id)) {
						cal_factor_ids.add(id);
					}
				}
			}
		}
			
		space.setSpectrumIds(cal_factor_ids);
	}


	public void transform() throws SPECCHIOClientException {
		
		// as the ids are already set, we just need to load the data
		SpaceProcessingChainComponent output_space = get_main_output_space();
		if(output_space != null) {
			Space new_output_space = specchio_client.loadSpace(output_space.getSpace());
			output_space.setSpace(new_output_space);
		}
		
	}

}
