package ch.specchio.file.reader.campaign;

import java.util.List;


/**
 * Interface to be implemented by classes that use campaign data loaders.
 */
public interface CampaignDataLoaderListener {
	
	
	/**
	 * Called when the loader experiences a fatal error.
	 *
	 * @param message	the error message
	 */
	public void campaignDataLoadError(String message);
	
	
	/**
	 * Called when the loader has processed all of its input data.
	 * 
	 * @param num_files		the number of files successfully processed
	 * @param num_spectra		the number of spectra successfully inserted
	 * @param file_errors	a list of files that contained errors
	 */
	public void campaignDataLoaded(int num_files, int num_spectra, List<String> file_errors);
	
	
	/**
	 * Called when the loader is ready to begin.
	 */
	public void campaignDataLoading();
	
	
	/**
	 * Called when a new file is processed
	 * 
	 * @param fileCount		the total number of files processed so far
	 * @param spectrumCount	the total number of spectra processed so far
	 */
	public void campaignDataLoadFileCount(int fileCount, int spectrumCount);
	
	
	/**
	 * Called when the loader embarks a new operation
	 * 
	 * @param message	a message describing the current operation
	 */
	public void campaignDataLoadOperation(String message);

}
