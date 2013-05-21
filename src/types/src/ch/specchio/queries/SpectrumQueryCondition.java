package ch.specchio.queries;

import javax.xml.bind.annotation.*;


@XmlRootElement(name="spectrum_query_condition")
public class SpectrumQueryCondition extends EAVQueryConditionObject {
	
	public SpectrumQueryCondition()
	{
	}

	public SpectrumQueryCondition(String tablename, String fieldname)
	{
		super(tablename, fieldname);		
		
		this.attribute_name = "";
	}
	
	public Boolean matches(QueryCondition cond) {
		
		Boolean matches = false;
		
		if(cond.getTableName().equals(this.tablename) && cond.getFieldName().equals(this.fieldname) && cond.getValue().equals(this.value) && cond.getOperator().equals(this.operator)) matches = true;
		
		return matches;
	}

	public String get_condition(String spectral_data_tablename_alias,
			String spectral_data_table_PK_name) {
		return null;
	}


}
