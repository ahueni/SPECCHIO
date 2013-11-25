package ch.specchio.types;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="av_matching_list")
public class AVMatchingList {
	
	private ArrayList<MetaParameter> metaparameters = new ArrayList<MetaParameter>();
	private ArrayList<Integer> spectrum_ids = new ArrayList<Integer>();
	private ArrayList<String> attribute_names;
	
	public AVMatchingList(){
		
	}
	
	public AVMatchingList(ArrayList<Integer> spectrum_ids, String... attribute_names_){
		this.spectrum_ids = spectrum_ids;
		this.attribute_names = new ArrayList<String>();
		
		for(int i=0;i<attribute_names_.length;i=i+1)
		{
			attribute_names.add(attribute_names_[i]);
		}
	}
	
	
	@XmlElement(name="spectrum_ids")
	public ArrayList<Integer> getSpectrumIds() { return this.spectrum_ids; }
	public void setSpectrumIds(ArrayList<Integer> spectrum_ids) { this.spectrum_ids = spectrum_ids; }
	
	@XmlElement(name="attribute_names")
	public ArrayList<String> getAttributeNames() { return this.attribute_names; }
	public void setAttributeNames(ArrayList<String> attribute_names) { this.attribute_names = attribute_names; }
	
	@XmlElement(name="metaparameters")
	public ArrayList<MetaParameter> getMetaparameters() {
		return metaparameters;
	}
	public void setMetaparameters(ArrayList<MetaParameter> metaparameters) {
		this.metaparameters = metaparameters;
	}
	
	
	public String get_properties_summary()
	{
		String summary = "";
		String sep = "";
		
		ListIterator<MetaParameter> prop_li = metaparameters.listIterator();
		
		while(prop_li.hasNext())
		{
			MetaParameter curr_prop = prop_li.next();
			
			summary = summary + sep + curr_prop.getAttributeName() + ":" + curr_prop.valueAsString();
			sep = ", ";
			
		}		
		
		return summary;
	}		

}
