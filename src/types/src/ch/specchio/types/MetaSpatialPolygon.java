package ch.specchio.types;

import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="meta_spatialpolygon")
public class MetaSpatialPolygon extends MetaSpatialGeometry {

	public MetaSpatialPolygon() {
		// TODO Auto-generated constructor stub
	}

	public MetaSpatialPolygon(String category_name, String category_value,
			Object meta_value) throws MetaParameterFormatException {
		super(category_name, category_value, meta_value);
		// TODO Auto-generated constructor stub
	}

	public MetaSpatialPolygon(attribute attr, Object meta_value)
			throws MetaParameterFormatException {
		super(attr, meta_value);
		// TODO Auto-generated constructor stub
	}

	public MetaSpatialPolygon(attribute attr) {
		super(attr);
		// TODO Auto-generated constructor stub
	}
	
	// values converted for DB insert: spatial syntax, e.g. "ST_GeomFromText('Polygon((46 8, 46 9, 48 9, 48 8, 46 8))')"
	// in this case the polygon is defined as a single closed linestring (i.e. a linear ring)
	@Override
	public Object getEAVValue() {
		
		String spatial_string = "ST_GeomFromText('Polygon((";
		@SuppressWarnings("unchecked")
		ArrayListWrapper<Point2D> value = (ArrayListWrapper<Point2D>) this.getValue();
		List<Point2D> coords = value.getList();
		
		// check if first point is equal to last point; if not, add first as last point to close the linestring
		if(coords.get(0) != coords.get(coords.size()-1))
		{
			coords.add(coords.get(0));
		}
		
		ListIterator<Point2D> iter = coords.listIterator();
		while(iter.hasNext())
		{
			Point2D coord = iter.next();
			spatial_string = spatial_string + coord.getY() + " " + coord.getX() + (iter.hasNext() ? ",":"");
		}
		
		spatial_string = spatial_string + "))')";
		
		return spatial_string;
	}		
	

}
