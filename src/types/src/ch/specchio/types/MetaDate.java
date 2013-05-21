package ch.specchio.types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.specchio.jaxb.XmlDateAdapter;

@XmlRootElement(name="meta_date")
public class MetaDate extends MetaParameter {
	
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	protected MetaDate() {
		super();
		setDefaultStorageField("datetime_val");
	}
	

	protected MetaDate(String category_name, String category_value, Object meta_value) {
		super(category_name, category_value, meta_value);
		setUnitName("Date");
		setDefaultStorageField("datetime_val");
	}
	
	
	protected MetaDate(attribute attr) {
		super(attr);
		setDefaultStorageField("datetime_val");
	}
	
	
	protected MetaDate(int eav_id) {
		super(eav_id);
		setDefaultStorageField("datetime_val");
	}
	

	@Override
	public boolean allows_multi_insert() {
		return true;
	}
	
	
	@Override
	public void setValue(Object value)
	{
		if (value instanceof XMLGregorianCalendar) {
			// Jersey (de-)serialises dates as XMLGregorianCalendar objects, but we want a Date object
			XmlDateAdapter adapter = new XmlDateAdapter();
			super.setValue(adapter.unmarshal((XMLGregorianCalendar)value));
		} else {
			super.setValue(value);
		}
		
	}
	
	
	@Override
	public void setEmptyValue()
	{
		setValue(new Date());
	}
	
	
	@Override
	public void setValueFromString(String s) throws MetaParameterFormatException {
		
		try {
			DateFormat format = DateFormat.getDateInstance();
			setValue(format.parse(s));
		}
		catch (ParseException ex) {
			// malformed date
			throw new MetaParameterFormatException(ex);
		}
		
	}
		
	
	@Override
	public String valueAsString()
	{
		return valueAsString(DEFAULT_DATE_FORMAT); // default
	}
	
	
	public static boolean supportsValue(Object value)
	{
		return value != null && (value instanceof Date || value instanceof XMLGregorianCalendar);	
	}
	
	
	public String valueAsString(String format_specifier) {
		
		if (getValue() != null) {
			TimeZone tz = TimeZone.getTimeZone("UTC");
			Calendar cal = Calendar.getInstance(tz);

			SimpleDateFormat formatter = new SimpleDateFormat(format_specifier);
			//formatter.setTimeZone(tz);
		
			cal.setTime((Date)getValue());
		
			//return formatter.format(cal.getTime());
			return formatter.format((Date)getValue());
		} else {
			return "null";
		}
		
	}

}
