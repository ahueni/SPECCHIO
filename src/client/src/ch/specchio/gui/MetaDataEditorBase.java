package ch.specchio.gui;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import ch.specchio.client.SPECCHIOClient;
import ch.specchio.client.SPECCHIOClientException;


public abstract class MetaDataEditorBase extends SpectralMetaDataBase implements ActionListener
{
	private static final long serialVersionUID = 1L;
//	public SpectralDataBrowser sdb;
//	SPECCHIOClient specchio_client;

	public MetaDataEditorBase(String frame_title) throws SPECCHIOClientException
	{
		super(frame_title);
		
		// get a reference to the application's client object
		this.specchio_client = SPECCHIOApplication.getInstance().getClient();
	}

	public void update_done() throws SPECCHIOClientException {
		// TODO Auto-generated method stub
		
	}
	
}
