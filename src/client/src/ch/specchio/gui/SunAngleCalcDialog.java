package ch.specchio.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.e175.klaus.solarpositioning.AzimuthZenithAngle;
import net.e175.klaus.solarpositioning.Grena3;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;
import ch.specchio.constants.UserRoles;
import ch.specchio.types.ArrayListWrapper;
import ch.specchio.types.Capabilities;
import ch.specchio.types.CelestialAngle;
import ch.specchio.types.MetaDate;
import ch.specchio.types.MetaParameter;
import ch.specchio.types.MetaParameterFormatException;
import ch.specchio.types.MetaSimple;
import ch.specchio.types.MetaSpatialPoint;
import ch.specchio.types.MetaSpatialPolyline;
import ch.specchio.types.Point2D;
import ch.specchio.types.attribute;



/**
 * Dialogue for calculating sun angles.
 */
public class SunAngleCalcDialog extends JDialog implements ActionListener, TreeSelectionListener {

	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;
	
	/** client object */
	private SPECCHIOClient specchioClient;
	
	/** azimuth attribute to be filled in by this dialogue */
	private attribute azimuthAttribute;
	
	/** zenith attribute to be filled in by this dialogue */
	private attribute zenithAttribute;
	
	/** spectral data browser */
	private SpectralDataBrowser sdb;
	
	/** label for the number of selected spectra */
	private JLabel numSelectedLabel;
	
	/** text field for the number of selected spectra */
	private JTextField numSelectedField;
	
	/** "okay" button */
	private JButton submitButton;
	
	/** "dismiss" button */
	private JButton dismissButton;
	
	/** text for the "okay" button */
	private static final String SUBMIT = "Apply";
	
	/** text for the "dismiss" button */
	private static final String DISMISS = "Close";
	
	
	/**
	 * Constructor.
	 * 
	 * @param owner	the frame that owns this dialogue
	 * @param modal	true if the dialogue should be modal
	 * 
	 * @throws SPECCHIOClientException	error contacting server
	 */
	public SunAngleCalcDialog(Frame owner, boolean modal) throws SPECCHIOClientException {
		
		super(owner, "Sun Angle Calculation", modal);
		
		// get a reference to the application's client object
		specchioClient = SPECCHIOApplication.getInstance().getClient();
		
		// get the attribute descriptors to be filled in
		Hashtable<String, attribute> attributes = specchioClient.getAttributesNameHash();
		azimuthAttribute = attributes.get("Illumination Azimuth");
		zenithAttribute = attributes.get("Illumination Zenith");
		if (azimuthAttribute == null || zenithAttribute == null) {
			throw new SPECCHIOClientException("The application server does not support the illumination azimuth and illumination zenith attributes.");
		}
		
		// set up the root panel with a vertical box layout
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		getContentPane().add(rootPanel);
		
		// add a spectral data browser for selecting nodes
		sdb = new SpectralDataBrowser(specchioClient, !specchioClient.isLoggedInWithRole(UserRoles.ADMIN));
		sdb.build_tree();
		sdb.tree.addTreeSelectionListener(this);
		rootPanel.add(sdb);
		
		// create a panel for the selection information
		JPanel selectionPanel = new JPanel();
		rootPanel.add(selectionPanel);
		
		// add a field for displaying the number of selected spectra
		numSelectedLabel = new JLabel("Number of Selected Spectra:");
		selectionPanel.add(numSelectedLabel);
		numSelectedField = new JTextField(20);
		numSelectedField.setEditable(false);
		selectionPanel.add(numSelectedField);
		
		// create a panel for the buttons
		JPanel buttonPanel = new JPanel();
		rootPanel.add(buttonPanel);
		
		// create the "okay" button
		submitButton = new JButton(SUBMIT);
		submitButton.setActionCommand(SUBMIT);
		submitButton.addActionListener(this);
		buttonPanel.add(submitButton);
		
		// crate the "dismiss" button
		dismissButton = new JButton(DISMISS);
		dismissButton.setActionCommand(DISMISS);
		dismissButton.addActionListener(this);
		buttonPanel.add(dismissButton);
		
		// lay out the dialogue
		pack();
		
	}
	
	
	/**
	 * Button handler.
	 * 
	 * @param event	the event to be handled
	 */
	public void actionPerformed(ActionEvent event) {
		
		if (SUBMIT.equals(event.getActionCommand())) {
			
			try {
				// get the selected items
				List<Integer> spectrumIds = sdb.get_selected_spectrum_ids();
			
				if (spectrumIds.size() > 0) {
					// launch a thread to perform the actual work
					SunAngleCalcThread thread = new SunAngleCalcThread(spectrumIds);
					thread.start();
				}
				
			} catch (SPECCHIOClientException ex) {
				ErrorDialog error = new ErrorDialog((Frame)this.getOwner(), "Error", ex.getUserMessage(), ex);
				error.setVisible(true);
			}
			
		} else if (DISMISS.equals(event.getActionCommand())) {
			
			// close the dialogue
			setVisible(false);
			
		}
		
	}
	
	
	/**
	 * Tree selection handler.
	 * 
	 * @param event	the event to be handled
	 */
	public void valueChanged(TreeSelectionEvent event) {
		
		try {
			// get the selected items
			List<Integer> spectrumIds = sdb.get_selected_spectrum_ids();
			
			// display the number of selected spectra
			if (spectrumIds.size() > 0) {
				numSelectedField.setText(Integer.toString(spectrumIds.size()));
			} else {
				numSelectedField.setText(null);
			}
			
		} catch (SPECCHIOClientException ex) {
			ErrorDialog error = new ErrorDialog((Frame)this.getOwner(), "Error", ex.getUserMessage(), ex);
			error.setVisible(true);
		}
		
	}
	
	
	/**
	 * Worker thread for performing the actual calculations.
	 */
	private class SunAngleCalcThread extends Thread {
		
		/** the spectrum identifiers to be processed */
		private Integer spectrumIds[];
		
		
		/**
		 * Constructor.
		 * 
		 * @param spectrumIdsIn	the list of spectrum identifiers to be processed
		 */
		public SunAngleCalcThread(List<Integer> spectrumIdsIn) {
			
			super();
			
			// save the input parameters for later
			spectrumIds = spectrumIdsIn.toArray(new Integer[spectrumIdsIn.size()]);
			
		}
		
		
		/**
		 * Main thread method.
		 */
		public void run() {
			
			// create a progress report
			ProgressReportDialog pr = new ProgressReportDialog(SunAngleCalcDialog.this, "Sun Angle Calculator", false, 20);
			pr.set_operation("Updating illumination angles");
			pr.setVisible(true);
			
			ArrayList<Integer> updatedIds = new ArrayList<Integer>();

			// calculate angles for each identifier
			int cnt = 0;
			int progress = 0;
			double tot = new Double(spectrumIds.length);
			try {
				
				boolean spatial_extension = specchioClient.getCapability(Capabilities.SPATIAL_EXTENSION) != null && specchioClient.getCapability(Capabilities.SPATIAL_EXTENSION).equals("true");
				
				for (Integer id : spectrumIds) {
					
					// download spectrum metadata from server
					//Spectrum s = specchioClient.getSpectrum(id, true);
					//Metadata md = s.getMetadata();
					//ArrayList<Integer> ids = new ArrayList<Integer>();
					//ids.add(id);
					
					//System.out.println(id);
					
					boolean spat_pos_available = false;
					double lat = 0, lon = 0;
					
					// get latitude and longitude
					if(spatial_extension)
					{
						//ArrayList<MetaParameter> pos_tmp = specchioClient.getMetaparameters(ids, "Spatial Position");
						//MetaSpatialPoint pos = (MetaSpatialPoint)md.get_first_entry("Spatial Position");
						//MetaSpatialPoint pos = (MetaSpatialPoint) pos_tmp.get(0);
//						MetaParameter tmp = specchioClient.getMetaparameter(id, "Spatial Position");
						MetaSpatialPoint pos = (MetaSpatialPoint)specchioClient.getMetaparameter(id, "Spatial Position");
						
						if(pos != null)
						{
							lat = pos.getPoint2D().getY();
							lon = pos.getPoint2D().getX();
							spat_pos_available = true;
						}
						
						if(!spat_pos_available)
						{
							//MetaSpatialPolyline t = (MetaSpatialPolyline)md.get_first_entry("Spatial Transect");
							//pos_tmp = specchioClient.getMetaparameters(ids, "Spatial Transect");
							//MetaSpatialPolyline t = (MetaSpatialPolyline) pos_tmp.get(0);
							MetaSpatialPolyline t = (MetaSpatialPolyline)specchioClient.getMetaparameter(id, "Spatial Transect");
							
							if(t != null)
							{
							
								ArrayListWrapper wrapper = (ArrayListWrapper) t.getValue();
								List coords = wrapper.getList();

								Point2D coord1 = (Point2D) coords.get(0);
								Point2D coord_end = (Point2D) coords.get(coords.size()-1);

								lat = (coord1.getY() + coord_end.getY()) / 2;
								lon = (coord1.getX() + coord_end.getX()) / 2;
								spat_pos_available = true;
							}
							
							
						}
						
						
					}
					else
					{
//						ArrayList<MetaParameter> lat_tmp = specchioClient.getMetaparameters(ids, "Latitude");
//						ArrayList<MetaParameter> lon_tmp = specchioClient.getMetaparameters(ids, "Longitude");
////						MetaSimple latitude = (MetaSimple)md.get_first_entry("Latitude");
////						MetaSimple longitude = (MetaSimple)md.get_first_entry("Longitude");
//						
//						MetaSimple latitude = (MetaSimple) lat_tmp.get(0);
//						MetaSimple longitude = (MetaSimple) lon_tmp.get(0);
						
						MetaSimple latitude = (MetaSimple) specchioClient.getMetaparameter(id, "Latitude");
						MetaSimple longitude = (MetaSimple) specchioClient.getMetaparameter(id, "Longitude");

						if (latitude != null && longitude != null)
						{
							lat = (Double)latitude.getValue();
							lon = (Double)longitude.getValue(); // longitude east of Greenwich is positive, west is negative
							spat_pos_available = true;
						}
					}
					
					// get acquisition time
					//MetaDate acquisitionTime = (MetaDate)md.get_first_entry("Acquisition Time (UTC)");
//					ArrayList<MetaParameter> utc_tmp = specchioClient.getMetaparameters(ids, "Acquisition Time (UTC)");
//					MetaDate acquisitionTime = (MetaDate)utc_tmp.get(0);
					MetaDate acquisitionTime = null;
					if(spat_pos_available)
					{
						acquisitionTime = (MetaDate) specchioClient.getMetaparameter(id, "Acquisition Time (UTC)");
					}
							
					// calculate angles only if we have a position and acquisition time
					if (spat_pos_available && acquisitionTime != null) {
						
						// calculate the angle of the sun at the position and acquisition time
						CelestialAngle angle = calculateSunAngle(
								lat,
								lon,
								(DateTime)acquisitionTime.getValue()
							);
						
						// round sun angle to 4 digits
						double azimuth = MetaDataEditorView.round(angle.azimuth, 6);
						double zenith = MetaDataEditorView.round(angle.zenith, 6);
						
						// build the list of identifiers to be updated
						ArrayList<Integer> updateIds = new ArrayList<Integer>();
						updateIds.add(id);
						
						// update azimuth
						MetaParameter azimuthParameter = (MetaParameter) specchioClient.getMetaparameter(id, "Illumination Azimuth");  ; // md.get_first_entry(azimuthAttribute.getId());
						
						if (azimuthParameter == null) {
							azimuthParameter = MetaParameter.newInstance(azimuthAttribute);
						}
						azimuthParameter.setValue(new Double(azimuth));
						specchioClient.updateEavMetadata(azimuthParameter, updateIds);
						
						// update zenith
						MetaParameter zenithParameter = (MetaParameter) specchioClient.getMetaparameter(id, "Illumination Zenith");
						//MetaParameter zenithParameter = md.get_first_entry(zenithAttribute.getId());
						if (zenithParameter == null) {
							zenithParameter = MetaParameter.newInstance(zenithAttribute);
						}
						zenithParameter.setValue(new Double(zenith));
						specchioClient.updateEavMetadata(zenithParameter, updateIds);
						
						// add the identifier to the list of updated identifiers
						updatedIds.add(id);									
						
						// udpate counter
						cnt++;
						
					}
					
					// update progress meter
					pr.set_progress(++progress * 100.0 / tot);
					
				}
				
			}
			catch (SPECCHIOClientException ex) {
				// error contacting server
				ErrorDialog error = new ErrorDialog((Frame)SunAngleCalcDialog.this.getOwner(), "Error", ex.getUserMessage(), ex);
				error.setVisible(true);
			}
			catch (ClassCastException ex) {
				// invalid data stored in the database
				ErrorDialog error = new ErrorDialog((Frame)SunAngleCalcDialog.this.getOwner(), "Error", "The database contains invalid data for one or more of these spectra.", ex);
				error.setVisible(true);
			}
			catch (MetaParameterFormatException ex) {
				// the parameter have the wrong type
				ErrorDialog error = new ErrorDialog((Frame)SunAngleCalcDialog.this.getOwner(), "Error", "The illumination attributes have the wrong type. Please contact your system administrator.", ex);
				error.setVisible(true);
			}
			
			if (updatedIds.size() > 0) {
				
				attribute attr = specchioClient.getAttributesNameHash().get("Solar Angle Computation");
							
				specchioClient.removeEavMetadata(attr, updatedIds, MetaParameter.SPECTRUM_LEVEL); // remove any existing solar angle computation entries
				
				// create a metaparameter noting that the time was shifted
				MetaParameter mpCalcInfo;
				DateTime dt = new DateTime(DateTimeZone.UTC);
				try {
					mpCalcInfo = MetaParameter.newInstance(
							attr,
							"Solar angles calculated using the SPECCHIO sun angle function (" + dt.toString() + ")"
						);
					
					// add the metaparameter to the database
					pr.set_operation("Updating database: adding provenance info ...");
					specchioClient.updateEavMetadata(mpCalcInfo, updatedIds);
					
				} catch (MetaParameterFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
			}			
			
			// show a completion message
			StringBuffer message = new StringBuffer();
			if (cnt > 0) {
				message.append("Sun angles computed for " + Integer.toString(cnt) + " spectra.");
				if (cnt < progress) {
					message.append("\n" + Integer.toString(progress - cnt) + " of the selected spectra do not have longitude, latitude or acquisition time data.");
					message.append("\nSun angles cannot be computed for these spectra.");
				}
			} else {
				message.append("No longitude, latitude or UTC acquisition time data found. No sun angles could be computed.");
			}
			JOptionPane.showMessageDialog(SunAngleCalcDialog.this, message, "Calculation complete", JOptionPane.INFORMATION_MESSAGE, SPECCHIOApplication.specchio_icon);
			
			pr.setVisible(false);
		}
		
		
		/**
		 * Calculate the angle of the sun for a given longitude, latitude and time.
		 * 
		 * @param latitude	the latitude
		 * @param longitude	the longitude
		 * @param dateTime		the date and time
		 * 
		 * @return a new CelestialAngle object representing the position of the sun
		 */
		private CelestialAngle calculateSunAngle(double latitude, double longitude, DateTime dateTime) {
			
		

			// get the time of year
////			TimeZone tz = TimeZone.getDefault();
////			Calendar cal = Calendar.getInstance(tz);
////			cal.setTime(dateTime);
////			int dy = cal.get(Calendar.DAY_OF_YEAR);
//			int dy = dateTime.getDayOfYear();
//			dy--; // Java starts the count at 1 (first day of year)
//				// but this routine expects the first day as 0 (zero)
////			int hh = cal.get(Calendar.HOUR_OF_DAY);
////			int mm = cal.get(Calendar.MINUTE);
////			int ss = cal.get(Calendar.SECOND);
//			
//			int hh = dateTime.getHourOfDay();
//			int mm = dateTime.getMinuteOfHour();
//			int ss = dateTime.getSecondOfMinute();
//
//						
//			double hours = hh + mm/60.0 + ss/3600.0;
//											
//			
//			int timezone = 0; // time zone: we expect the capture time to be in GMT
//				
//			double wdy = 2*Math.PI*dy/365.0; 
//			
//			// sun declination [rad]
//			double delta = 0.006918-0.399912*Math.cos(wdy)+0.070257*Math.sin(wdy)-0.006758*Math.cos(2*wdy)+0.000908*Math.sin(2*wdy);
//			
//			// longitude correction
//			double lc = -longitude / 15;
//			
//			// time equation
//			double et = 0.0172+0.4281*Math.cos(wdy)-7.3515*Math.sin(wdy)- 3.3495*Math.cos(2*wdy)-9.3619*Math.sin(2*wdy);
//			
//			// true solar time
//			double tst = hours-timezone+lc+et/60;
//			double wtst = Math.PI*(tst-12)/12;
//			
//			if(wtst > Math.PI)
//				wtst=wtst-2*Math.PI;
//			
//			if(wtst < -Math.PI)
//				wtst=wtst+2*Math.PI;
//			
//			// sun height, sun zenith angle
//			double lat_r = Math.toRadians(latitude);
//			
//			
//			double h = Math.asin(Math.cos(lat_r)*Math.cos(delta)*Math.cos(wtst)+Math.sin(lat_r)*Math.sin(delta));
//			
//			double thz = Math.PI/2 - h;
//			double nen = Math.sin(lat_r)*Math.cos(delta)*Math.cos(wtst)-Math.cos(lat_r)*Math.sin(delta);
//			double phi = Math.acos(nen/Math.cos(h));
//			
//			if( wtst < 0)
//				phi=-phi;
//			
//			double azimuth = phi + Math.PI;
//			if(azimuth > 2*Math.PI)
//				azimuth = azimuth - 2*Math.PI;
//			
//			return new CelestialAngle(Math.toDegrees(azimuth), Math.toDegrees(thz));
			
//	        GregorianCalendar time = new GregorianCalendar(new SimpleTimeZone(+1 * 60 * 60 * 1000, "UTC"));
//	        time.set(2012, Calendar.JANUARY, 1, 12, 0, 0);
	        
			GregorianCalendar time = dateTime.toGregorianCalendar();

	        AzimuthZenithAngle result = Grena3.calculateSolarPosition(time,
	        		latitude, longitude, 65, 1000, 20);
	        
	        
	        
	        return new CelestialAngle(result.getAzimuth(),result.getZenithAngle());

			
			
			
		}
		
	}

}
