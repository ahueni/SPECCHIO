package ch.specchio.eav_db;

public class id_and_op_struct
{
	public String id;
	public String op;
	
	// if id is null: return {is, null}
	// if id is not null returns {id_value, =}
	public id_and_op_struct(Integer id)
	{
	   if(id==0)
	   {
		   this.id = "null";
		   this.op = "is";
	   }
	   else
	   {
		   this.id = id.toString();
		   this.op = "=";  
	   }
	}
	
	
	public id_and_op_struct(String id)
	{
		this(Integer.parseInt(id));
	}
}
