package ch.specchio.types;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="av_matching_list_collection")
public class AVMatchingListCollection {
	
	private ArrayList<AVMatchingList> spectrum_id_lists = new ArrayList<AVMatchingList>();

	@XmlElement(name="spectrum_id_lists")
	public ArrayList<AVMatchingList> getSpectrum_id_lists() {
		return spectrum_id_lists;
	}

	public void setSpectrum_id_lists(ArrayList<AVMatchingList> spectrum_id_lists) {
		this.spectrum_id_lists = spectrum_id_lists;
	}
	
	public void addSpectrum_id_list(AVMatchingList spectrum_id_list) {
		spectrum_id_lists.add(spectrum_id_list);
	}
	
	

}
