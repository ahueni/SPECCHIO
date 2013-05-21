package ch.specchio.queries;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="query_condition")
@XmlSeeAlso({EAVQueryConditionObject.class, QueryConditionObject.class, SpectrumQueryCondition.class})
public abstract class QueryCondition {
	
	public abstract String getFieldName();
	public abstract String getAttributeName();
	public abstract Object getValue();
	public abstract String getOperator();
	public abstract String getTableName();
	public abstract Boolean matches(QueryCondition qc);
	public abstract void setFieldName(String fieldname);

}
