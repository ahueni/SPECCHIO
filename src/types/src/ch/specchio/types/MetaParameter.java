package ch.specchio.types;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.specchio.jaxb.XmlMetaParameterValueAdapter;


/**
 * This class represents a metadata parameter.
 */
@XmlRootElement(name="meta_parameter")
@XmlSeeAlso({MetaDate.class,MetaDocument.class,MetaFile.class,MetaImage.class,MetaMatrix.class,MetaSimple.class,MetaTaxonomy.class})
public abstract class MetaParameter {

	private String default_storage_field = null;
	private Integer attribute_id = null;
	private String attribute_name;
	private String category_name;
	private String category_value;
	private Object value;
	private Integer unit_id = 0;
	private String unit_name;
	private Integer default_unit_id = 0;
	private Integer eav_id = 0;
	
	
	protected MetaParameter() {
		
	}
	
	
	protected MetaParameter(int eav_id) {
		
		this.eav_id = eav_id;
		
	}
	
	
	protected MetaParameter(String category_name, String category_value, Object meta_value)
	{
		this.category_name = category_name;
		this.category_value = category_value;
		setValue(meta_value);
	}
	
	
	protected MetaParameter(attribute attr)
	{
		this.category_name = attr.cat_name;
		this.attribute_id = attr.getId();
		this.attribute_name = attr.getName();
		this.unit_id = attr.default_unit_id;
		this.default_storage_field = attr.getDefaultStorageField();
	}
	
	
	@XmlElement(name="attribute_id")
	public Integer getAttributeId() { return this.attribute_id; }
	public void setAttributeId(Integer attribute_id) {	this.attribute_id = attribute_id; }
	
	@XmlElement(name="attribute_name")
	public String getAttributeName() { return this.attribute_name; }
	public void setAttributeName(String attribute_name) { this.attribute_name = attribute_name; }
	
	@XmlElement(name="category_name")
	public String getCategoryName() { return this.category_name; }
	public void setCategoryName(String category_name) { this.category_name = category_name; }
	
	@XmlElement(name="category_value")
	public String getCategoryValue() { return this.category_value; }
	public void setCategoryValue(String category_value) { this.category_value = category_value; }
	
	@XmlElement(name="default_unit_id")
	public Integer getDefaultUnitId() { return this.default_unit_id; }
	public void setDefaultUnitId(int default_unit_id) { this.default_unit_id = default_unit_id; }
	
	@XmlElement(name="default_storage_field")
	public String getDefaultStorageField() { return this.default_storage_field; }
	public void setDefaultStorageField(String default_storage_field) { this.default_storage_field = default_storage_field; }
	
	@XmlElement(name="eav_id")
	public Integer getEavId() { return this.eav_id; }
	public void setEavId(Integer eav_id) { this.eav_id = eav_id; }
	
	
	public int getFieldPos() {

		if(this.default_storage_field == null)
		{
			return 0;
		}
		
		if (this.default_storage_field.equals("int_val")) return 1;
		else if (default_storage_field.equals("double_val")) return 2;
		else if (default_storage_field.equals("string_val")) return 3;
		else if (default_storage_field.equals("binary_val")) return 4;
		else if (default_storage_field.equals("datetime_val")) return 5;
		else if (default_storage_field.equals("taxonomy_id")) return 6;
		
		return 0;
	}	
	
	
	@XmlElement(name="unit_id")
	public Integer getUnitId()
	{
		return this.unit_id;
	}
	
	
	public void setUnitId(Integer unit_id) {
		
		this.unit_id = unit_id;
		
	}
	
	
	@XmlElement(name="unit_name")
	public String getUnitName() {
		
		return this.unit_name;
		
	}
	
	
	public void setUnitName(String unit_name) {
		
		this.unit_name = unit_name;
		
	}
	
	
	public void setUnits(Units u) {
		
		setUnitId(u.id);
		setUnitName(u.short_name);
		
	}
	
	
	@XmlElement(name="value")
	@XmlJavaTypeAdapter(XmlMetaParameterValueAdapter.class)
	public Object getValue() {
		
		return this.value;
		
	}
	
	
	public void setValue(Object value) {
		
		this.value = value;
		
	}
	
	
	public void setValue(Object value, String unit_string)
	{
		this.setValue(value);
		this.setUnitName(unit_string);
	}
	
	
	public static MetaParameter newInstance()
	{
		return new MetaSimple();
	}
	
	
	public static MetaParameter newInstance(attribute attr)
	{
		MetaParameter mp;
		
//		if(attr == null){
//			int x = 1;
//		}
//		
//		System.out.println("attr: " + attr.getName());
		
		// construct a meta-parameter of the type appropirate to the attribute
		if (attr.default_storage_field.equals("datetime_val"))
			mp = new MetaDate(attr);
		else if (attr.cat_name.equals("Pictures"))
			mp = new MetaImage(attr);
		else if (attr.cat_name.equals("PDFs"))
			mp = new MetaDocument(attr);
		else if (attr.default_storage_field.equals("taxonomy_id"))
			mp = new MetaTaxonomy(attr);
		else
			mp = new MetaSimple(attr);
		
		// create an empty value
		mp.setEmptyValue();
		
		return mp;
	}
	
	
	public static MetaParameter newInstance(String category_name, String category_value, Object meta_value)
	{
		MetaParameter mp;
		
		if (MetaDate.supportsValue(meta_value)) {
			mp = new MetaDate(category_name, category_value, meta_value);
		} else if (MetaImage.supportsValue(meta_value)) {
			mp = new MetaImage(category_name, category_value, meta_value);
		} else if (MetaDocument.supportsValue(meta_value)) {
			mp = new MetaDocument(category_name, category_value, meta_value);
		} else if (MetaMatrix.supportsValue(meta_value)) {
			mp = new MetaMatrix(category_name, category_value, meta_value);
		} else if (MetaTaxonomy.supportsValue(meta_value)) {
			mp = new MetaTaxonomy(category_name, category_value, meta_value);
		} else {
			mp = new MetaSimple(category_name, category_value, meta_value);
		}
		 
		 return mp;
		 
	}
	
	
	public static MetaParameter newInstance(String category_name, String category_value)
	{	
		return newInstance(category_name, category_value, null);	
	}
	
	
	public abstract boolean allows_multi_insert();
	
	public abstract void setEmptyValue();
	
	public abstract void setValueFromString(String s) throws MetaParameterFormatException;
	
	public abstract String valueAsString();

}
