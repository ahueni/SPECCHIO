package ch.specchio.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ch.specchio.file.reader.campaign.CampaignDataLoaderListener;

public class LoadCampaignDataHandler implements CampaignDataLoaderListener {
	
	private ProgressReportTextPanel p_rep;
	private ProgressReportTextPanel p_spectra_insert_rep;	
	
	
	/**
	 * Default constructor.
	 */
	public LoadCampaignDataHandler() {
		
	}
	
	/**
	 * Handler for errors during loading.
	 *
	 * @param message	the error message
	 */
	public void campaignDataLoadError(String message) {

		JOptionPane.showMessageDialog(null, "Error while loading files: " + message);
		
	}
	
	
	/**
	 * Handler for the beginning of the load.
	 */
	public void campaignDataLoading() {
		
		// create a new progress report and add it to the operation pane
		OperationsPane op = OperationsPane.getInstance();
		p_rep = new ProgressReportTextPanel("Loading campaign data.", "# of processed files");
		op.add_report(p_rep);

		// create progress report for the spectra
		p_spectra_insert_rep = new ProgressReportTextPanel("Loading campaign data.", "# of inserted spectra");
		op.add_report(p_spectra_insert_rep);

		
	}
	
	
	/**
	 * Handler for completion of loading.
	 * 
	 * @param file_errors	a list of files that contained errors
	 */
	public void campaignDataLoaded(int num_files, List<String> file_errors) {

		OperationsPane op = OperationsPane.getInstance();
		p_rep.set_operation("Done!");
		op.remove_report(p_rep);

		p_spectra_insert_rep.set_operation("Done!");
		op.remove_report(p_spectra_insert_rep);

		SPECCHIOApplication app = SPECCHIOApplication.getInstance();
		JOptionPane.showMessageDialog(app.get_frame(), num_files + " files successfully processed.");
		
		if (file_errors.size()>0)
		{
			// report on errors
			JFrame report = new JFrame("File Error List");
			report.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			report.setLayout(new BorderLayout());
			
			// create text field
			JTextArea error_text_area = new JTextArea(8, 50);
			
			JScrollPane scroll_pane = new JScrollPane(error_text_area);	
			scroll_pane.setPreferredSize(new Dimension(450, 200));
			
			String str = "";
			
			// add errors to error text area
			for (int i=0;i<file_errors.size();i++)
			{
				str = str + file_errors.get(i);					
			}
			
			error_text_area.setText(str);				
		
			report.add(scroll_pane);
			
			report.pack();
			
			report.setVisible(true);
			
		}
		
	}
	
	
	/**
	 * Handler for file count updates.
	 * 
	 * @param fileCount		the new value of the file counter
	 * @param spectrumCount	the new value of the spectrum counter
	 */
	public void campaignDataLoadFileCount(int fileCount, int spectrumCount) {

		p_rep.set_progress(fileCount);
		p_spectra_insert_rep.set_progress(spectrumCount);
		
	}
	
	
	/**
	 * Handler for an operation reported by the loader.
	 * 
	 * @param message	a message describing the current operation
	 */
	public void campaignDataLoadOperation(String message) {
		
		p_rep.set_operation(message);
	
	}

}
