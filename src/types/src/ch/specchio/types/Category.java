package ch.specchio.types;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="Category")
public class Category implements Comparable<Category> {
	@XmlElement public int category_id;
	@XmlElement public String name;
	
	public Category(int category_id, String name) { this.category_id = category_id; this.name = name; }
	public Category(String name) { this(0, name); }
	public Category() { this(0, ""); }
	
	@Override
	public int compareTo(Category other) {
		
		return this.name.compareTo(other.name);
		
	}
	
	@Override
	public boolean equals(Object other) {
		
		if (other instanceof Category) {
			return this.name.equals(((Category)other).name);
		} else {
			return false;
		}
		
	}

}
