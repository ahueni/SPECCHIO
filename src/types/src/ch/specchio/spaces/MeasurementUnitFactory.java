package ch.specchio.spaces;

public class MeasurementUnitFactory 
{
	
	private static MeasurementUnitFactory instance = null;

	   protected MeasurementUnitFactory() {   

	   }
	   
	   public static MeasurementUnitFactory getInstance() {
	      if(instance == null) {
	    	  instance = new MeasurementUnitFactory();
	      }
	      return instance;
	   }
	   
	   public MeasurementUnit create_from_coding(int measurement_unit_no)
	   {
		   MeasurementUnit unit = new MeasurementUnit();
		   unit.setUnitNumber(measurement_unit_no);
		   return unit;
	   }
}
