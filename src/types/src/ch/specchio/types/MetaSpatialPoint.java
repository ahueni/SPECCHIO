package ch.specchio.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;


@XmlRootElement(name="meta_spatialpoint")
@XmlSeeAlso({MetaSpatialGeometry.class})
public class MetaSpatialPoint extends MetaSpatialGeometry {

	public MetaSpatialPoint() {
		// TODO Auto-generated constructor stub
	}


	public MetaSpatialPoint(String category_name, String category_value,
			Object meta_value) throws MetaParameterFormatException {
		super(category_name, category_value, meta_value);
		// TODO Auto-generated constructor stub
	}

	public MetaSpatialPoint(attribute attr, Object meta_value)
			throws MetaParameterFormatException {
		super(attr, meta_value);
		// TODO Auto-generated constructor stub
	}

	public MetaSpatialPoint(attribute attr) {
		super(attr);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Set the coordinates of this point.
	 * 
	 * @param coord		coordinate structure (Lon = x and Lat = y)
	 * 
	 * @throws MetaParameterFormatException	could set value
	 */	
	public void setValue(Point2D coord) throws MetaParameterFormatException {

		ArrayList<Point2D> coords = new ArrayList<Point2D>();
		coords.add(coord);
		ArrayListWrapper<Point2D> value = new ArrayListWrapper<Point2D>();
		value.setList(coords);

		this.setValue(value);

	}		
	
	
	/**
	 * Get the coordinates of this point.
	 * 
	 * return	coordinate structure (Lon = x and Lat = y)
	 */	
	public Point2D getPoint2D() {

		@SuppressWarnings("unchecked")
		ArrayListWrapper<Point2D> value = (ArrayListWrapper<Point2D>) this.getValue();
		List<Point2D> coords = value.getList();

		return coords.get(0);

	}			
	
	// values converted for DB insert: spatial syntax, e.g. "ST_GeomFromText('POINT(47.235 8.567)')"
	@Override
	public Object getEAVValue() {
		
		String spatial_string = "ST_GeomFromText('POINT(";
		@SuppressWarnings("unchecked")
		ArrayListWrapper<Point2D> value = (ArrayListWrapper<Point2D>) this.getValue();
		List<Point2D> coords = value.getList();
		
		spatial_string = spatial_string + coords.get(0).getY() + " " + coords.get(0).getX() + ")')";
		
		return spatial_string;
	}		

}
