package ch.specchio.gui;

//used to hold table data in the combobox
public class combo_table_data {
	
	public String name; // string shown in the combobox
	public String value; // value associated with this item
	public Integer id; // table id
	
	public combo_table_data(String name, Integer id, String value){
		this.name = name;
		this.id = id;
		this.value = value;
	}
	
	public combo_table_data(String name, Integer id){
		this(name, id, null);
	}
	
	public combo_table_data(String name){
		this(name, 0, null);
	}
	
	public combo_table_data(String name, String value){
		this(name, 0, value);
	}
	
	public String getValue() {
		return (value != null)? value : id.toString();	
	}
		
	
	public String toString() { 
		return name; 
	}
	
}

