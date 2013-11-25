package ch.specchio.types;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SpectralFileInsertResult")
public class SpectralFileInsertResult {
	
	private ArrayList<Integer> spectrum_ids;
	private ArrayList<SpecchioMessage> errors;
	
	/** default constructor */
	public SpectralFileInsertResult()
	{
		spectrum_ids = new ArrayList<Integer>();
		errors = new ArrayList<SpecchioMessage>();
	}

	@XmlElement(name="spectrum_ids")
	public ArrayList<Integer> getSpectrumIds() {
		return spectrum_ids;
	}
	public void setSpectrumIds(ArrayList<Integer> spectrum_ids) {
		this.spectrum_ids = spectrum_ids;
	}
	public void addSpectrumId(int id) {
		spectrum_ids.add(id);
	}


	@XmlElement(name="errors")
	public ArrayList<SpecchioMessage> getErrors() {
		return errors;
	}
	public void setErrors(ArrayList<SpecchioMessage> errors) {
		this.errors = errors;
	}
	public void addError(SpecchioMessage error) {
		errors.add(error);
	}
	public void addErrors(ArrayList<SpecchioMessage> errors) {
		this.errors.addAll(errors);
	}	
	
	
	public void add(SpectralFileInsertResult result) {
		spectrum_ids.addAll(result.getSpectrumIds());
		errors.addAll(result.getErrors());
	}	
	
	
	public ArrayList<SpecchioMessage> get_nonredudant_errors()
	{
		ArrayList<SpecchioMessage> nonred_errors = new ArrayList<SpecchioMessage>();
		
		ArrayList<String> msgs = new ArrayList<String>();
		
		for (SpecchioMessage error : errors)
		{
			if (!msgs.contains(error.getMessage())) {
				nonred_errors.add(error);	
				msgs.add(error.getMessage());
			}
		}
		
		return nonred_errors;
	}
	

}
