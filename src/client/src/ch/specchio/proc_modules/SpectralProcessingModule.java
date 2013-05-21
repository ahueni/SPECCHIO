package ch.specchio.proc_modules;

import java.awt.Frame;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.constants.SpaceTypes;

public abstract // expects spectral space as input
class SpectralProcessingModule extends ProcessingModule
{

	public SpectralProcessingModule(Frame owner, SPECCHIOClient specchio_client) {
		super(owner, specchio_client);
		// TODO Auto-generated constructor stub
	}
	
	public int get_output_space_type() {
		return SpaceTypes.SpectralSpace;
	}


	public int get_required_input_space_type() {
		return SpaceTypes.SpectralSpace;
	}

	
}