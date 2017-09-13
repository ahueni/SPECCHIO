package ch.specchio.types;

import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="meta_spatialpolyline")
public class MetaSpatialPolyline extends MetaSpatialGeometry {

	public MetaSpatialPolyline() {
		// TODO Auto-generated constructor stub
	}

	public MetaSpatialPolyline(String category_name, String category_value,
			Object meta_value) throws MetaParameterFormatException {
		super(category_name, category_value, meta_value);
		// TODO Auto-generated constructor stub
	}

	public MetaSpatialPolyline(attribute attr, Object meta_value)
			throws MetaParameterFormatException {
		super(attr, meta_value);
		// TODO Auto-generated constructor stub
	}

	public MetaSpatialPolyline(attribute attr) {
		super(attr);
		// TODO Auto-generated constructor stub
	}
	
	
	// values converted for DB insert: spatial syntax, e.g. "ST_GeomFromText('LineString(46 8, 46 9, 48 9, 48 8)')"

	@Override
	public Object getEAVValue() {
		
		String spatial_string = "ST_GeomFromText('LineString(";
		@SuppressWarnings("unchecked")
		ArrayListWrapper<Point2D> value = (ArrayListWrapper<Point2D>) this.getValue();
		List<Point2D> coords = value.getList();
		
		
		ListIterator<Point2D> iter = coords.listIterator();
		while(iter.hasNext())
		{
			Point2D coord = iter.next();
			spatial_string = spatial_string + coord.getY() + " " + coord.getX() + (iter.hasNext() ? ",":"");
		}
		
		spatial_string = spatial_string + ")')";
		
		return spatial_string;
	}		
	

}
