package ch.specchio.types;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Meta-parameter class for spatial types.
 */
@XmlRootElement(name="meta_spatialgeometry")
@XmlSeeAlso({MetaSpatialPoint.class})
public  class MetaSpatialGeometry extends MetaParameter {

	public MetaSpatialGeometry() {
		super();
		setDefaultStorageField("spatial_val");
	}

//	public MetaSpatialGeometry(int eav_id) {
//		super(eav_id);
//		// TODO Auto-generated constructor stub
//	}

	public MetaSpatialGeometry(String category_name, String category_value,
			Object meta_value) throws MetaParameterFormatException {
		super(category_name, category_value, meta_value);
		setDefaultStorageField("spatial_val");
	}

	public MetaSpatialGeometry(attribute attr, Object meta_value)
			throws MetaParameterFormatException {
		super(attr);
		ArrayListWrapper<Point2D> value = new ArrayListWrapper<Point2D>();
		
		// convert from SQL spatial syntax to point objects in list
		String tmp = (String) meta_value;
		String str =tmp.replaceAll("[^\\d\\s.,-]", ""); // remove all non-numeric characters but keep minus and commas
		
		String[] coord_pairs = str.split(",");
		
		for(int i=0;i<coord_pairs.length;i++)
		{
			String[] coord_pair = coord_pairs[i].split(" ");
			
			Point2D coord = new Point2D(0.0, 0.0);
			
			// convert to double
			try
			{
				coord.setY(Double.valueOf(coord_pair[0]));
			} catch(NullPointerException e)
			{				
			}
			catch(NumberFormatException e)
			{
			}
			
			try
			{
				coord.setX(Double.valueOf(coord_pair[1]));
			} catch(NullPointerException e)
			{				
			}
			catch(NumberFormatException e)
			{
			}
			
			value.getList().add(coord);			
			
		}
		
		this.setValue(value);
		

		setDefaultStorageField("spatial_val");
	}

	public MetaSpatialGeometry(attribute attr) {
		super(attr);
		setDefaultStorageField("spatial_val");
	}

	@Override
	public boolean allows_multi_insert() {
		// TODO Auto-generated method stub
		return true;
	}

	
	public void setValue(ArrayList<Point2D> coords) throws MetaParameterFormatException {

		ArrayListWrapper<Point2D> value = new ArrayListWrapper<Point2D>();
		value.setList(coords);

		this.setValue(value);

	}	
	
	@Override
	public void setEmptyValue() {
		try {
			setValue(new ArrayListWrapper<Point2D>());
		} catch (MetaParameterFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String valueAsString() {
		
		String spatial_string = "";
		@SuppressWarnings("unchecked")
		ArrayListWrapper<Point2D> value = (ArrayListWrapper<Point2D>) this.getValue();
		List<Point2D> coords = value.getList();
		
		
		ListIterator<Point2D> iter = coords.listIterator();
		while(iter.hasNext())
		{
			Point2D coord = iter.next();
			spatial_string = spatial_string + coord.getY() + " " + coord.getX() + (iter.hasNext() ? ";":"");
		}

		
		return spatial_string;
	}
	

	

}
