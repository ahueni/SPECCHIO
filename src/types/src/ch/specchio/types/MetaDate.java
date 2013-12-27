package ch.specchio.types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.specchio.jaxb.XmlDateAdapter;

@XmlRootElement(name="meta_date")
public class MetaDate extends MetaParameter {
	
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	
	protected MetaDate() {
		super();
		setDefaultStorageField("datetime_val");
	}
	

	protected MetaDate(String category_name, String category_value, Object meta_value) throws MetaParameterFormatException {
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
	public void setValue(Object value) throws MetaParameterFormatException
	{
		if (value instanceof Date) {
			super.setValue(value);
		} else if (value instanceof XMLGregorianCalendar) {
			// Jersey (de-)serialises dates as XMLGregorianCalendar objects, but we want a Date object
			XmlDateAdapter adapter = new XmlDateAdapter();
			super.setValue(adapter.unmarshal((XMLGregorianCalendar)value));
		} else if (value instanceof String) {
			try {
				DateFormat format = DateFormat.getDateInstance();
				setValue(format.parse((String)value));
			}
			catch (ParseException ex) {
				// malformed date
				throw new MetaParameterFormatException(ex);
			}
		} else {
			throw new MetaParameterFormatException("Cannot assign object of type " + value.getClass() + " to a MetaDate parameter.");
		}
		
	}
	
	
	@Override
	public void setEmptyValue()
	{
		try {
			setValue(new Date());
		}
		catch (MetaParameterFormatException ex) {
			// never happens
			ex.printStackTrace();
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
			return formatDate((Date)getValue(), format_specifier);
		} else {
			return "null";
		}
		
	}
	
	
	public static String formatDate(Date date) {
		
		return getDateFormat().format(date);
		
	}
	
	
	public static String formatDate(Date date, String format_specifier) {
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		SimpleDateFormat formatter = new SimpleDateFormat(format_specifier);
		formatter.setTimeZone(tz);
		
		
//		String out=formatter.format(date);				
	
		return formatter.format(date);
		
	}
	
	
	public static DateFormat getDateFormat() {
		
		return dateFormat;
		
	}

}
