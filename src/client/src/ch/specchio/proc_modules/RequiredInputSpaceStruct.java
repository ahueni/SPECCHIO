package ch.specchio.proc_modules;

import java.util.ArrayList;

import ch.specchio.constants.SpaceTypes;


public class RequiredInputSpaceStruct
{
	public ArrayList<SpaceProcessingChainComponent> spaces;
	public String space_description;
	public SpaceProcessingChainComponent chosen_space = null;
	
	public RequiredInputSpaceStruct(String space_description, ArrayList<SpaceProcessingChainComponent> spaces)
	{
		this.space_description = space_description;
		this.spaces = spaces;
	}
}

// used to define output space information in the modules
class OutputSpaceStruct
{
	public String space_name;
	public SpaceTypes space_type;
	
	public OutputSpaceStruct(String space_name)
	{
		this.space_name = space_name;
	}
}
