package ch.specchio.types;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.specchio.constants.TimeFormats;

@XmlRootElement(name="meta_datatype")
public class MetaDatatype<E>
{
	@XmlElement(name="name") public String name; // used for reporting
	@XmlElement(name="value") public E value;
	@XmlElement(name="output_format") int output_timeformat = TimeFormats.Formatted;
	SimpleDateFormat formatter = null;
	java.text.DecimalFormat df = new java.text.DecimalFormat("###.########");
	
	public MetaDatatype()
	{
	}
	
	public MetaDatatype(String name)
	{
		this.name = name;
		this.value = null;	
	}
	
	public MetaDatatype(String name, E value)
	{
		this.name = name;
		this.value = value;
	}
	
	public E get_value()
	{
		return value;
	}
	
	public void set_value(E value)
	{
		this.value = value;
	}
	
	public String toString()
	{
		if(value != null)
		{
			if(value instanceof Date)
			{
				TimeZone tz = TimeZone.getTimeZone("UTC");
				Calendar cal = Calendar.getInstance(tz);
				cal.setTime((Date)value);
				if(this.output_timeformat == TimeFormats.Formatted)
				{
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
					formatter.setTimeZone(tz);
					return formatter.format(cal.getTime());	
				}
				if(this.output_timeformat == TimeFormats.Seconds)
				{
					Long time_in_millis = cal.getTimeInMillis();
					return time_in_millis.toString();
				}
						
			}
			
			
			if(value instanceof ArrayList)
			{
				StringBuffer out = new StringBuffer("");
				// we expect to find a list of strings here
				// the output should be a concatenated string of all values
				ListIterator<String> li = ((ArrayList<String>)value).listIterator();
				// perform load on all metadata fields
				while(li.hasNext())
				{
					if (out.length() > 0) {
						out.append(" / ");
					}
					out.append(li.next());
				}
				
				return out.toString();
			}
			
			if(value instanceof Double)
			{
				return df.format(value);
			}
			
			return value.toString();
			
			
		}
		else
			return "";
	}
	
	public String colon_name()
	{
		return name + ":";
	}
	
	public void set_timeformat(int format)
	{
		this.output_timeformat = format;
	}

	
}
