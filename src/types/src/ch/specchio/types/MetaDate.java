package ch.specchio.types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ch.specchio.jaxb.XmlDateAdapter;

@XmlRootElement(name="meta_date")
public class MetaDate extends MetaParameter {
	
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	private static DateTimeFormatter formatter = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT).withZoneUTC();
	
	protected MetaDate() {
		super();
		setDefaultStorageField("datetime_val");
	}
	

	protected MetaDate(String category_name, String category_value, Object meta_value) throws MetaParameterFormatException {
		super(category_name, category_value, meta_value);
		setUnitName("Date");
		setDefaultStorageField("datetime_val");
	}
	
	protected MetaDate(attribute attr, Object meta_value) throws MetaParameterFormatException
	{
		super(attr, meta_value);
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
	public boolean hasEqualValue(MetaParameter mp)
	{
		return mp.getValue().equals(getValue());
	}		
	
	@Override
	public void setValue(Object value) throws MetaParameterFormatException
	{
		if (value instanceof DateTime) {
			super.setValue(value); 
		} else if (value instanceof Date) {
			
			 // efforts to avoid the local time zone interfering with the time
			
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date)value);
			

			SimpleDateFormat formatter_1 = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			
			String out=formatter_1.format(cal.getTime());	
			
			DateTime dt = formatter.parseDateTime(out);
			
			
			setValue(dt); 
		} else if (value instanceof Long) {
			// For Jersey we (de-)serialises DateTime as Long objects, but we want a DateTime object
			super.setValue(new DateTime(value, DateTimeZone.UTC));		
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
			DateTime now = new DateTime(DateTimeZone.UTC);
			setValue(now);
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
		return value != null && (value instanceof DateTime || value instanceof XMLGregorianCalendar || value instanceof Long);	
	}
	
	
	public String valueAsString(String format_specifier) {
		
		if (getValue() != null) {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(format_specifier);
			return fmt.print((DateTime)getValue());
		} else {
			return "null";
		}
		
	}
	
	public Date valueAsDate() {
		// conversion trick to avoid forcing to local time zone: https://www.mkyong.com/java/convert-datetime-to-date-but-timezone-is-missing/
		Date dateInZ = ((DateTime) this.getValue()).toLocalDateTime().toDate();
		return dateInZ;
	}
	
	public DateTime valueAsDateTime() {
		return ((DateTime) this.getValue());
	}	
	
//	public DateTime valueAsJodaDate() {
//		
//		DateTimeFormatter formatter = DateTimeFormat.forPattern(MetaDate.DEFAULT_DATE_FORMAT);
//		formatter.withZoneUTC();
//		DateTime dt = formatter.parseDateTime((String) this.getValue()); 
//
//		return dt;
//	}	
	
	
	public static String formatDate(DateTime date) {
		
		return formatter.print(date);
		
	}
	
	
	public static String formatDate(DateTime date, String format_specifier) {
		
//		TimeZone tz = TimeZone.getTimeZone("UTC");
//		SimpleDateFormat formatter = new SimpleDateFormat(format_specifier);
//		formatter.setTimeZone(tz);
//		
//		
////		String out=formatter.format(date);				
//	
//		return formatter.format(date);
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern(format_specifier);
		String str = fmt.print(date);
		
		return str;
		
	}
	
	
	public static DateTimeFormatter getDateFormatter() {
		
		return formatter;
		
	}
	
	public static DateFormat getDateFormat() {
		
		return dateFormat;
		
	}

}
