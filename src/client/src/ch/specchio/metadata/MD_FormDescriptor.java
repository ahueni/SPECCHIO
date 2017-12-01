package ch.specchio.metadata;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ch.specchio.types.Category;


/**
 * This class describes a metadata form.
 */
public class MD_FormDescriptor {
	
	/** the set of categories in this descriptor */
	private Set<Category> categories;
	
	boolean add_spectrum_table_fields = false;
	
	
	/**
	 * Default constructor. Creates a descriptor with no categories.
	 */
	public MD_FormDescriptor(Comparator<Category> comparatorIn) {
		
		categories = new TreeSet<Category>(comparatorIn);
		
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param categoriesIn	a list of categories with which to initialise the descriptor
	 * @param comparatorIn	the comparator to use in sorting the categories
	 */
	public MD_FormDescriptor(Collection<Category> categoriesIn, Comparator<Category> comparatorIn) {
		
		// initialise member variables
		this(comparatorIn);
		
		// copy the list of categories
		for (Category categoryIn : categoriesIn) {
			Category category = new Category(categoryIn.category_id, categoryIn.name);
			categories.add(category);
		}
		
	}
	
	
	/**
	 * Add a category to the descriptor.
	 * 
	 * @param name	the name of the category to add
	 */
	public void addCategory(String name) {
		
		categories.add(new Category(name));
		
	}
	
	
	/**
	 * Remove all of the categories from the descriptor.
	 */
	public void clear() {
		
		categories.clear();
		
	}
	
	
	/**
	 * Get the list of categories in this desctriptor.
	 * 
	 * @return a reference to the internal list of categories
	 */
	public Collection<Category> getCategories() {
		
		return categories;
		
	}
	
	
	/**
	 * Remove a category from the descriptor.
	 * 
	 * @param name	the name of the category to remove
	 */
	public void removeCategory(String name) {
		
		Iterator<Category> iter = categories.iterator();
		while (iter.hasNext()) {
			if (iter.next().name.equals(name)) {
				iter.remove();
				break;
			}
		}
		
	}
		
	public boolean DoAdd_spectrum_table_fields() {
		return add_spectrum_table_fields;
	}

	public void setAdd_spectrum_table_fields(boolean add_spectrum_table_fields) {
		this.add_spectrum_table_fields = add_spectrum_table_fields;
	}		

}
