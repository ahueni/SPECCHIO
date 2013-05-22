package ch.specchio.file.reader.spectrum;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import ch.specchio.types.SpectralFile;

public class XLS_FileLoader extends SpectralFileLoader {

	public XLS_FileLoader() {
		super("XLS");
	}

	@Override
	public SpectralFile load(File file) throws IOException {
		
		SpectralFile f = null;
		
		// read xls file
		try {
			Workbook w = Workbook.getWorkbook(file);			
			Sheet sheet = w.getSheet(0);
			f = new SpectralFile();
			
			f.setFilename(file.getName());
			f.setFileFormatName(this.file_format_name);
			f.setCompany(""); // unknown sensor company
			
			f.setNumberOfSpectra(sheet.getColumns() - 1); 	// first column is wavelength	
			
			// get spectrum file names
			// set capture dates: equal to loading date
			TimeZone tz = TimeZone.getTimeZone("UTC");
			Calendar cal = Calendar.getInstance(tz);
			for(int c = 1;c<sheet.getColumns();c++)
			{
				f.addSpectrumFilename(file.getName() + "_" + sheet.getCell(c, 0).getContents());
				f.setCaptureDate(c-1, cal.getTime());	
			}
			
			// get wvls
			Float[] wvls = new Float[sheet.getRows()-1];
			
			Cell[] col1 = sheet.getColumn(0);
			
			for(int r=1;r<sheet.getRows();r++)
			{
				String value = col1[r].getContents();
				wvls[r-1] = Float.valueOf(value);
			}
			
			f.addNumberOfChannels(wvls.length);
			
			// get spectra
			Float[][] spectra = new Float[sheet.getColumns()-1][sheet.getRows()-1];
			
			for(int c = 1;c<sheet.getColumns();c++)
			{
				Cell[] col = sheet.getColumn(c);
				for(int r=1;r<sheet.getRows();r++)
				{
					spectra[c-1][r-1] = Float.valueOf(col[r].getContents());
				}
				
				f.addWvls(wvls); // add wvls for each spectrum
			}
			
			f.setMeasurements(spectra);
			
			
		} catch (BiffException ex) {
			throw new IOException(ex);
		} catch (NumberFormatException ex) {
			throw new IOException("Invalid number format (" + ex.getMessage() + ")", ex);
		}
		
		return f;
	}

}
