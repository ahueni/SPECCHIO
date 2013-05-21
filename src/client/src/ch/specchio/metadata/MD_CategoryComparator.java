package ch.specchio.metadata;

import java.util.Comparator;

import ch.specchio.types.Category;

/**
 * Class for comparing categories.
 */
public class MD_CategoryComparator implements Comparator<Category> {

	/**
	 * Compare two categories.
	 * 
	 * @return -1, 0, or +1 if category1 is less than, equal to, or greater than category2, respectively
	 */
	@Override
	public int compare(Category category1, Category category2) {
		
		return category1.compareTo(category2);
		
	}

}
