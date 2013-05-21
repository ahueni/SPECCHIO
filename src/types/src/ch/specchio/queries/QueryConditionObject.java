package ch.specchio.queries;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="query_condition_object")
public class QueryConditionObject extends EAVQueryConditionObject {
	
	public QueryConditionObject()
	{
		super();
	}
	
	public QueryConditionObject(String tablename, String fieldname)
	{
		super(tablename, fieldname);
	}

}
