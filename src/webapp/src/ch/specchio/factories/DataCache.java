package ch.specchio.factories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ListIterator;

import ch.specchio.constants.SensorType;
import ch.specchio.eav_db.SQL_StatementBuilder;
import ch.specchio.spaces.MeasurementUnit;
import ch.specchio.types.Institute;
import ch.specchio.types.Instrument;
import ch.specchio.types.ReferenceBrand;
import ch.specchio.types.Sensor;
import ch.specchio.types.SpectralFile;


public class DataCache {
	
	SQL_StatementBuilder SQL;
	
	ArrayList<Institute> institutes;
	ArrayList<Instrument> instruments;
	ArrayList<Sensor> sensors;
	ArrayList<MeasurementUnit> measurement_units;
	ArrayList<GoniometerStruct> goniometers;
	ArrayList<SamplingEnvironmentStruct> sampling_environments;	
	ArrayList<BeamGeometryStruct> beam_geometries;
	ArrayList<ReferenceBrand> reference_brands;
	
	
	
	public DataCache(SQL_StatementBuilder SQL) throws SQLException
	{
		this.SQL = SQL;
		load_cache();
	}
	
	private void load_cache()
	{
		load_institutes();
		load_sensors();
		load_instruments();
		load_measurement_units();
		load_goniometers();
		load_sampling_environments();
		load_measurement_types();
		load_reference_brands();
	}
	
	
	public void add_institute(Institute institute)
	{
		if (get_institute_by_id(institute.getInstituteId()) == null)
			institutes.add(institute);
	}
	
	
	public ArrayList<Institute> get_institutes()
	{
		return institutes;
	}
		
	
	
	public Institute get_institute_by_id(int institutes_id)
	{
		Institute inst = null;
		
		ListIterator<Institute> li = institutes.listIterator();
		
		boolean found = false;
		
		while(li.hasNext() && !found)
		{
			inst = li.next();
			
			if (inst.getInstituteId() == institutes_id) found = true;			
		}
		
		return (found)? inst : null;		
		
	}
	
	
	public Institute get_institute_by_name(String institute_name)
	{
		Institute inst = null;
		
		ListIterator<Institute> li = institutes.listIterator();
		
		boolean found = false;
		
		while(li.hasNext() && !found)
		{
			inst = li.next();
			
			if (inst.toString().equals(institute_name)) found = true;			
		}
		
		return found ? inst : null;		
		
	}




	private void load_institutes() {
		
		institutes = new ArrayList<Institute>();
		
		try {
			
			Statement stmt = SQL.createStatement();

			// read information from database
			String[] tables = new String[]{"institute"};
			String[] attr = new String[]{"institute_id", "name", "department"};

			String query = SQL.assemble_sql_select_query(
						SQL.conc_attributes(attr),
						SQL.conc_tables(tables), "");
			
						
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Institute inst = new Institute(); 
				inst.setInstituteId(rs.getInt(1));
				inst.setInstituteName(rs.getString(2));
				inst.setDepartment(rs.getString(3));
				
				this.institutes.add(inst);
			}
			rs.close();
			
			stmt.close();
						
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

	}	
	
	
	
	public Instrument get_instrument(int instrument_id) throws SQLException
	{
		return get_instrument(instrument_id, 0);		
	}
	
	
	public Instrument get_instrument(int instrument_id, int calibration_id) throws SQLException
	{	
		Instrument instrument = null;
		Instrument s;
		// search 
		ListIterator<Instrument> li = instruments.listIterator();

		if(instrument_id != 0)
		{
			while(li.hasNext() && instrument == null)
			{
				s = li.next();
				if(s.getInstrumentId() == instrument_id && s.getCalibrationId() == calibration_id)
					instrument = s;			
			}
		}
				
		return instrument;
	}
	
	public Instrument get_instrument_by_serial_id(String serial_id, int sensor_id) throws SQLException
	{
		Instrument instrument = null;
		Instrument s;
		// search 
		ListIterator<Instrument> li = instruments.listIterator();

		
			while(li.hasNext() && instrument == null)
			{
				s = li.next();
				if(s.getInstrumentNumber().get_value() != null && s.getInstrumentNumber().get_value().equals(serial_id) && s.getSensorId() == sensor_id)
					instrument = s;			
			}

				
		return instrument;		
		
		
	}	
	
	
	
	public Instrument get_instrument(String instrument_number, String company) 
	{		
		Instrument instrument = null;
		Instrument s;
		// search through instrument list
		ListIterator<Instrument> li = instruments.listIterator();

		
			while(li.hasNext() && instrument == null)
			{
				s = li.next();
				if(s.getInstrumentNumber().get_value() != null && s.getInstrumentNumber().get_value().equals(instrument_number) && s.getSensor().getManufacturerShortName().get_value().equals(company))
					instrument = s;			
			}

				
		return instrument;		

	}
	
	

	public String get_instrument_id_for_file(SpectralFile spec_file, int spec_no) throws SQLException {
		String instrument_id = "null";
		
		Integer sensor_id = get_sensor_id_for_file(spec_file, spec_no);
		
		Instrument instr = get_instrument_by_serial_id(spec_file.getInstrumentNumber(), sensor_id);
		 
		
		if(instr == null) // try via centre wvls
		{
			instr = get_instrument_by_centre_wvls(spec_file, spec_no);
		}
		
		if (instr != null) instrument_id = Integer.toString(instr.getInstrumentId());

		return instrument_id;

	}
	
	
	private Instrument get_instrument_by_centre_wvls(SpectralFile spec_file, int spec_no) throws SQLException {
		
		Instrument instrument = null;
		Instrument i;

		ListIterator<Instrument> li = instruments.listIterator();
		boolean wvls_match = true;
		boolean serial_no_match = true;
		
		// get wvls information
		Float[] wvls = null;
		double[] d_wvls = null;
		if(spec_file.getWvls().size() > 0)
		{
			wvls = spec_file.getWvls(spec_no);
			d_wvls = new double[spec_file.getWvls(spec_no).length];
			for (int j=0;j<d_wvls.length;j++) d_wvls[j] = wvls[j];
		}
		else
		{
			Integer sensor_id = get_sensor_id_for_file(spec_file, spec_no);
			d_wvls = this.get_sensor(sensor_id).getAverageWavelengths();
		}
		


		while(li.hasNext() && instrument == null)
		{
			i = li.next();


			// quick check: first and last band				
			boolean possible_match = (d_wvls.length == i.getNoOfBands()) & (d_wvls[0] == i.getCentreWavelengths()[0]) & (d_wvls[d_wvls.length-1] == i.getCentreWavelengths()[i.getNoOfBands()-1]);

			if(possible_match) // do a full check
			{
				// check of the centre wavelengths match
				int band = 0;
				for(Double wvl : d_wvls)
				{
					wvls_match = wvls_match & (wvl == i.getCentreWavelengths()[band++]);

					if (wvls_match == false) break;
				}				
			}
			
			if(possible_match && spec_file.getInstrumentNumber() != null &&
					!spec_file.getInstrumentNumber().equals("")) // check the serial number
			{
				if(spec_file.getInstrumentNumber().equals(i.getInstrumentNumber())) 
				{
					serial_no_match = true;
				}
				else
				{
					serial_no_match = false;
				}
			}

			if(possible_match && wvls_match && serial_no_match) instrument = i;


		}

		if(instrument == null)
		{
			// unknown instrument
			System.out.println("unknown instrument");

			// insert as new instrument:

			Integer sensor_id;
			try {
				sensor_id = this.get_sensor_id_for_file(spec_file, spec_no);

				if(sensor_id != 0)
				{					

					// connect to DB as admin to insert a new instrument

					InstrumentationFactory factory = new InstrumentationFactory();

					Instrument instr = new Instrument();

					Sensor s = this.get_sensor(sensor_id);

					instr.setInstrumentName(s.getName().get_value() + " #" + spec_file.getInstrumentNumber() + " instrument");
					instr.setSensorId(sensor_id);
					instr.setInstrumentNumber(spec_file.getInstrumentNumber());
					instr.setSensor(s);
					instr.setAverageWavelengths(d_wvls);

					factory.insertInstrument(instr);
					factory.updateInstrument(instr);
					factory.dispose();
					
					instrument = get_instrument_by_serial_id(spec_file.getInstrumentNumber(), sensor_id);
				}


			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SPECCHIOFactoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
			
		
		return instrument;
	}
	
	
	public void add_instrument(Instrument instr) throws SQLException
	{
		if (get_instrument(instr.getInstrumentId()) == null)
			instruments.add(instr);
	}
	
	public void delete_instrument(int instrument_id) throws SQLException {
		
		ListIterator<Instrument> iter = instruments.listIterator();
		while (iter.hasNext()) {
			if (iter.next().getInstrumentId() == instrument_id) {
				iter.remove();
			}
		}
		
	}

	private void load_instruments() {
		
		instruments = new ArrayList<Instrument>();
		
		try {
			
			Statement stmt = SQL.createStatement();

			// read information from database
			String[] tables = new String[]{"instrument"};
			String[] attr = new String[]{"instrument_id", "name", "institute_id", "serial_number", "sensor_id"};

			String query = SQL.assemble_sql_select_query(
						SQL.conc_attributes(attr),
						SQL.conc_tables(tables),
						"");
			
						
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				int i = 1;
				int instrument_id = rs.getInt(i++);
				String name = rs.getString(i++);
				int institute_id = rs.getInt(i++);
				String instr_no = rs.getString(i++);
				int sensor_id = rs.getInt(i++);
				
				Instrument instr = load_instrument(instrument_id, name, institute_id, instr_no, sensor_id);
				this.instruments.add(instr);
				
			}
			rs.close();		

			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

	}
	
	private Instrument load_instrument(int instrument_id2, String name, int institute_id,
			String instr_no2, int sensor_id2) {

		Instrument instrument = new Instrument();
		
		instrument.setInstrumentId(instrument_id2);
		instrument.setInstrumentName(name);
		
		Institute inst = get_institute_by_id(institute_id);
		if (inst != null) {
			instrument.setInstrumentOwner(inst.toString());
		}
		
		instrument.setInstrumentNumber(instr_no2);
		
		instrument.setSensorId(sensor_id2);
		
		instrument.setSensor(get_sensor(sensor_id2));
		
		return instrument;
		
	}
	
	
	public ArrayList<Sensor> get_sensors()
	{	
		return this.sensors;
	}
	
	public Sensor get_sensor(int sensor_id)
	{	
		Sensor sensor = null;
		Sensor s;
		
		if(sensor_id != 0)
		{
		
			// search through sensor list
			ListIterator<Sensor> li = sensors.listIterator();

			while(li.hasNext() && sensor == null)
			{
				s = li.next();
				if(s.getSensorId() == sensor_id)
					sensor = s;			
			}
		}
				
		return sensor;
	}
	
	
	public Sensor get_sensor_by_name(String sensor_name)
	{	
		Sensor sensor = null;
		Sensor s;
		
		if(sensor_name != null)
		{
		
			// search through sensor list
			ListIterator<Sensor> li = sensors.listIterator();

			while(li.hasNext() && sensor == null)
			{
				s = li.next();
				if(s.getName().value.equals(sensor_name))
					sensor = s;			
			}
		}
				
		return sensor;
	}	
	
	
	public Sensor get_sensor(Float[] wvls, String company)
	{
		float nm_diff_threshold = 3.0f;
		Sensor sensor = null;
		Sensor s;
		boolean wvls_match = true;
			// search through sensor list
			ListIterator<Sensor> li = sensors.listIterator();
			
			while(li.hasNext() && sensor == null)
			{
				s = li.next();
				if(s.getNumberOfChannels().value == wvls.length && s.getManufacturerShortName().get_value().equals(company))
				{
					// check of the centre wavelengths match
//					int band = 0;
//					for(Float wvl : wvls)
//					{
//						wvls_match = wvls_match & ((wvl - s.getAverageWavelengths()[band++]) < nm_diff_threshold);
//						
//						if (wvls_match == false) break;
//					}
					
					// quick check: first and last band					
					wvls_match = (wvls[0] - s.getAverageWavelengths()[0]) < nm_diff_threshold & (wvls[wvls.length-1] - s.getAverageWavelengths()[s.getNumberOfChannels().get_value()-1]) < nm_diff_threshold;
					
					if(wvls_match) sensor = s;		
				}
			}
				
		return sensor;		
	}
	
	
	
	// should also check if the wvls are matching!
	public Sensor get_sensor(Float[] wvls)
	{
		Sensor sensor = null;
		Sensor s;
			// search through sensor list
			ListIterator<Sensor> li = sensors.listIterator();

			while(li.hasNext() && sensor == null)
			{
				s = li.next();
				int no_of_sensor_bands = s.getNumberOfChannels().value;
				if(no_of_sensor_bands == wvls.length)
					sensor = s;			
			}
				
		return sensor;		
	}
	
	
	public Sensor get_sensor(String company, int instrument_type_number)
	{
		Sensor sensor = null;
		Sensor s;
		
		// search through sensor list
		ListIterator<Sensor> li = sensors.listIterator();

		while(li.hasNext() && sensor == null)
		{
			s = li.next();
			if(s.getManufacturerShortName().get_value().equals(company) && s.getSensorTypeNumber() == instrument_type_number)
				sensor = s;			
		}

		return sensor;		

	}
	
	// returns the sensor_id based on information read from the input file
	// or 'null' if sensor could not be found in the database
	public Integer get_sensor_id_for_file(SpectralFile spec_file, int spec_no) throws SQLException {
		Integer sensor_id = 0;

		if (spec_file.getCompany().equals("APOGEE")) {
			
			Sensor s = get_sensor(spec_file.getWvls(0), spec_file.getCompany());

			return s.getSensorId();

		}
		
		if (spec_file.getCompany().equals("PP Systems" ) && spec_file.getFileFormatName().equals("UniSpec_SPU")) {
			
			Sensor s = get_sensor(spec_file.getWvls(0), spec_file.getCompany());

			return s.getSensorId();	
			
		}

		if (spec_file.getCompany().equals("COST_OO_CSV")) {

			Sensor s = get_sensor(new Float[spec_file.getNumberOfChannels(0)], "OceanOptics");

			return s.getSensorId();

		}

		// last case: for ASD calibration files where the instrument type number
		// is set to zero for the *.ILL and *.REF files
		if (spec_file.getCompany() == null || spec_file.getInstrumentTypeNumber() == -1
				|| (spec_file.getCompany().equals("ASD") && spec_file.getInstrumentTypeNumber() == 0)) {
			// try via sensor bank
			//SensorBank sb = SensorBank.getInstance();
			Sensor s = get_sensor(new Float[spec_file.getNumberOfChannels(0)]);
			if (s == null)
				return sensor_id; // "null"
			else
				return s.getSensorId();
		}

		// get sensor id via the instrument if instrument number is defined
		if (spec_file.getInstrumentNumber() != null) {
			
			Instrument i = get_instrument(spec_file.getInstrumentNumber(), spec_file.getCompany());
			
			if (i != null)
			{
				return i.getSensor().getSensorId();
			}

		}
		
		Sensor s = get_sensor(spec_file.getCompany(), spec_file.getInstrumentTypeNumber());

		return (s != null)? s.getSensorId() : 0;

	}
	
	
	public void add_sensor(Sensor s) {
		
		if (get_sensor(s.getSensorId()) == null) {
			this.sensors.add(s);
		}
		
	}
	
	
	private void load_sensors() {
		
		sensors = new ArrayList<Sensor>();
		
		try {
			
			Statement stmt = SQL.createStatement();

			// read information from database

			String[] tables = new String[]{"sensor"};
			String[] attr = new String[]{"sensor_id", "name", "no_of_channels", "description", "manufacturer_id", "sensor_type_no"};

			String query = SQL.assemble_sql_select_query(
						SQL.conc_attributes(attr),
						SQL.conc_tables(tables),
						"");
			
						
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				int i = 1;
				int  sensor_id = rs.getInt(i++);
				String name = rs.getString(i++);
				int no_of_channels = rs.getInt(i++);
				String description = rs.getString(i++);
				int manufacturer_id = rs.getInt(i++);
				int sensor_type_no = rs.getInt(i++);
				
				Sensor sensor = load_sensor(sensor_id, name, no_of_channels, description, manufacturer_id, sensor_type_no);
				this.sensors.add(sensor);
				
			}
			rs.close();
			
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

	}
	
	
	private Sensor load_sensor(int sensor_id2, String name2, int no_of_channels2, String description,
			int manufacturer_id2, int sensor_type_no) throws SQLException {
		
		Sensor sensor = new Sensor();
		
		sensor.setSensorId(sensor_id2);
		sensor.setName(name2);
		sensor.setNumberOfChannels(no_of_channels2);
		sensor.setDescription(description);
		sensor.setManufacturerId(manufacturer_id2);
		sensor.setSensorTypeNumber(sensor_type_no);
		
		sensor.setValid(true);
		
		load_sensor_definition(sensor);
		
		return sensor;
		
	}

	private void load_sensor_definition(Sensor sensor) throws SQLException
	{		
		int element_type_sum = 0;
			
		sensor.setAverageWavelengths(new double[sensor.getNumberOfChannels().value]);
		sensor.setElementTypes(new short[sensor.getNumberOfChannels().value]);
			
		// get sensor elements info
		String query = "select se.avg_wavelength, sety.code from sensor_element se, sensor_element_type sety where " +
				"se.sensor_element_type_id = sety.sensor_element_type_id and " +
				"sensor_id = " + Integer.toString(sensor.getSensorId()) + " order by sensor_element_id";
			
		Statement stmt = SQL.createStatement();
		ResultSet rs = stmt.executeQuery(query);
			
		int i = 0;
		while (rs.next()) {
			sensor.setAverageWavelength(i, rs.getFloat(1));
			sensor.setElementType(i, rs.getShort(2));
			element_type_sum += sensor.getElementType(i);
			i++;
		}
		rs.close();

		java.text.DecimalFormat df = new java.text.DecimalFormat("###.####");
		sensor.setWavelengthRange(
				df.format(sensor.getAverageWavelength(0)) +
				" - " +
				df.format(sensor.getAverageWavelength(sensor.getNumberOfChannels().value - 1))
			);
			
		// get manufacturer info
		query = "select name, www, short_name from manufacturer where manufacturer_id = " + Integer.toString(sensor.getManufacturerId());
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			sensor.setManufacturerName(rs.getString(1));
			sensor.setManufacturerWWW(rs.getString(2));
			sensor.setManufacturerShortName(rs.getString(3));
		}
		rs.close();	
			
		// check if it is a pure instrument (i.e. narrow or broadband only)
		// the check on the sum works because only codes of 0 and 1 are possible
		// thus any value between indicates a mixed instrument
		if(element_type_sum > 0 && element_type_sum <= sensor.getNumberOfChannels().value)
		{
			sensor.setSensorType(SensorType.NB_BB);
		}
		else
		{
			if(element_type_sum == SensorType.NB)
				sensor.setSensorType(SensorType.NB);
			else
				sensor.setSensorType(SensorType.BB);	
		}
		
		stmt.close();
		
	}
	
	
	
	public int get_measurement_unit_id(int ASD_coding)
	{			
		return get_measurement_unit(ASD_coding).getUnitId();						
	}
	
	
	public MeasurementUnit get_measurement_unit(int ASD_coding)
	{	
		MeasurementUnit s = null;
			// search through sensor list
			ListIterator<MeasurementUnit> li = measurement_units.listIterator();

			while(li.hasNext())
			{
				s = li.next();
				if(s.getUnitNumber() == ASD_coding)
					break;			
			}
		
		return s;
	}

	public Integer get_measurement_unit_id_for_file(SpectralFile spec_file, int index) throws SQLException {
		
		Integer measurement_units = spec_file.getMeasurementUnits(index);
		int id = get_measurement_unit_id(measurement_units);
		//return (measurement_units != 0)? get_measurement_unit_id(measurement_units) : 0;
		return id;
		
	}

	public String get_measurement_type_id_for_file(SpectralFile spec_file, int spec_no) {
		
		int measurement_type_id = get_measurement_type_id(Integer.toString(spec_file.getMeasurementType(spec_no)));
		
		return SQL.is_null_key_get_val_and_op(measurement_type_id).id;
		
	}
	
	
	
	private void load_measurement_units() {
		
		measurement_units = new ArrayList<MeasurementUnit>();
		
		try {
			
			Statement stmt = SQL.createStatement();

			// read information from database

			String query = "select measurement_unit_id, ASD_coding, name from measurement_unit";

		ResultSet rs;
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			int measurement_unit_id = rs.getInt(1);
			int ASD_coding = rs.getInt(2);
			String name = rs.getString(3);
			
			MeasurementUnit mu = new MeasurementUnit();
			mu.setUnitId(measurement_unit_id);
			mu.setUnitNumber(ASD_coding);
			mu.setUnitName(name);
			
			measurement_units.add(mu);
		}

			rs.close();					
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

	}

	
	public int get_goniometer_id(String name)
	{
		GoniometerStruct o = null;
			// search through sensor list
			ListIterator<GoniometerStruct> li = goniometers.listIterator();

			while(li.hasNext())
			{
				o = li.next();
				if(o.name.equals(name))
					break;			
			}
			
				
		return o.goniometer_id;				
		
	}	
	
	
	private void load_goniometers() {
		
		goniometers = new ArrayList<GoniometerStruct>();
		
		try {
			
			Statement stmt = SQL.createStatement();

			// read information from database
			String query = "select goniometer_id, name from goniometer";

			
						
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				int i = 1;
				GoniometerStruct g = new GoniometerStruct();
				g.goniometer_id = rs.getInt(i++);
				g.name = rs.getString(i++);

				this.goniometers.add(g);
				
			}
			rs.close();		

						
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

	}		
	
	
	public int get_sampling_environment_id(String name)
	{
		SamplingEnvironmentStruct o = null;
			// search through sensor list
			ListIterator<SamplingEnvironmentStruct> li = sampling_environments.listIterator();

			while(li.hasNext())
			{
				o = li.next();
				if(o.name.equals(name))
					break;			
			}
			
				
		return o.sampling_environment_id;				
		
	}	
	
	
	private void load_sampling_environments() {
		
		sampling_environments = new ArrayList<SamplingEnvironmentStruct>();
		
		try {
			
			Statement stmt = SQL.createStatement();

			// read information from database
			String query = "select sampling_environment_id, name from sampling_environment";

			
						
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				int i = 1;
				SamplingEnvironmentStruct o = new SamplingEnvironmentStruct();
				o.sampling_environment_id = rs.getInt(i++);
				o.name = rs.getString(i++);

				this.sampling_environments.add(o);
				
			}
			rs.close();		

			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

	}		
	
	public int get_measurement_type_id(String beam_geometry)
	{
		if (beam_geometry.equals("0")) return 0;
		
		BeamGeometryStruct s = null;
			// search through sensor list
			ListIterator<BeamGeometryStruct> li = beam_geometries.listIterator();

			while(li.hasNext())
			{
				s = li.next();
				if(s.name.contains(beam_geometry))
					break;			
			}
			
				
		return s.measurement_type_id;				
		
	}
	
	
	
	private void load_measurement_types() {
		
		beam_geometries = new ArrayList<BeamGeometryStruct>();

		try {

			Statement stmt = SQL.createStatement();

			// read information from database
			String query = "select measurement_type_id, name from measurement_type";

			ResultSet rs;
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				BeamGeometryStruct o = new BeamGeometryStruct();

				o.measurement_type_id = rs.getInt(1);
				o.name = rs.getString(2);

				beam_geometries.add(o);
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

	}
	
	
	public ReferenceBrand get_reference_brand_by_name(String reference_brand_name)
	{
		ReferenceBrand brand = null;
		
		ListIterator<ReferenceBrand> li = reference_brands.listIterator();
		
		boolean found = false;
		
		while(li.hasNext() && !found)
		{
			brand = li.next();
			
			if (brand.toString().equals(reference_brand_name)) found = true;			
		}
		
		return brand;		
		
	}
	
	
	private void load_reference_brands() {
		
		reference_brands = new ArrayList<ReferenceBrand>();
		
		try {
			
			Statement stmt = SQL.createStatement();

			// read information from database
			String[] tables = new String[]{"reference_brand"};
			String[] attr = new String[]{"reference_brand_id", "reference_type_id", "manufacturer_id", "name"};

			String query = SQL.assemble_sql_select_query(
						SQL.conc_attributes(attr),
						SQL.conc_tables(tables),
						"");
			
						
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				int i = 1;
				int reference_brand_id = rs.getInt(i++);
				int reference_type_id = rs.getInt(i++);
				int manufacturer_id = rs.getInt(i++);
				String name = rs.getString(i++);
				
				ReferenceBrand brand = new ReferenceBrand(reference_brand_id, reference_type_id, manufacturer_id, name);
				this.reference_brands.add(brand);
				
			}
			rs.close();		

			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

	}
	

}


class GoniometerStruct
{
	public String name;
	public int goniometer_id;
}

class SamplingEnvironmentStruct
{
	public String name;
	public int sampling_environment_id;
}

class BeamGeometryStruct
{	
	public String name;
	public int measurement_type_id;	
}


