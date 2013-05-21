package ch.specchio.file.reader.campaign;

import ch.specchio.types.Campaign;


abstract public class CampaignDataLoader extends Thread {
	

	protected Campaign campaign;

	protected int file_counter;
	protected int spectrum_counter;
	protected int inserted_spec_cnt;
	protected CampaignDataLoaderListener listener;
	
	
	// concrete class of a spectral file loader and the campaign id whose data
	// is to be loaded
	protected CampaignDataLoader(CampaignDataLoaderListener listener)
	{
		this.listener = listener;
		file_counter = 0;
		spectrum_counter = 0;
		inserted_spec_cnt = 0;
		
	}
	

	public void set_campaign(Campaign c) {
		this.campaign = c;
		
	}
	
}
