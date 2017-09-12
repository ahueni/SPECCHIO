package ch.specchio.gui;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import ch.specchio.gui.SpectrumMetadataPanel.ImageTransferable;
import ch.specchio.types.MetaImage;
import ch.specchio.types.SerialisableBufferedImage;

public class ImagePanel extends JPanel implements ActionListener, MouseListener{
	
	private static final long serialVersionUID = 1L;
	
	public SerialisableBufferedImage  image;
	public MetaImage mp;

	private JPopupMenu popupMenu;
	
	
	
	public ImagePanel()
	{
		// set up popup menu
		popupMenu = new JPopupMenu();

		JMenuItem menuItem = new JMenuItem("Copy image to clipboard");
		menuItem.setActionCommand("CopyToClipboard");
		menuItem.addActionListener(this);
		popupMenu.add(menuItem);

		menuItem = new JMenuItem("Open image in external viewer");
		menuItem.setActionCommand("OpenImage");
		menuItem.addActionListener(this);
		popupMenu.add(menuItem);						

		addMouseListener(this);
	
	}

	
	public void paint(Graphics g) {
		  
		try {
			int dstx1 = 0; int dsty1 = 0; int dstx2 = this.getWidth(); int dsty2 = this.getHeight();
		    int srcx1 = 0; int srcy1 = 0; int srcx2 = image.getImage().getWidth(); int srcy2	 = image.getImage().getHeight();	  
			  
			g.drawImage( image.getImage(), dstx1, dsty1, dstx2, dsty2, srcx1, srcy1, srcx2, srcy2, null);
		}
		catch (IOException ex) {
			// nothing we can do about it
			ex.printStackTrace();
		}
		catch (ClassNotFoundException ex) {
			// nothing we can do about it
			ex.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * Menu selection handler.
	 *
	 * @param event	the event to be handled
	 */
	public void actionPerformed(ActionEvent event) {
		
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		
		if ("CopyToClipboard".equals(event.getActionCommand())) {
			
	    	try {
	    		
	    		BufferedImage image = this.image.getImage();
	            ImageTransferable transferable = new ImageTransferable( image );
	            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferable, null);
	    		
	    	}
	    	catch (IllegalArgumentException ex) {
	    		// something wrong with the temporary file
	    		ErrorDialog error = new ErrorDialog(topFrame, "Could not start viewer", ex.getMessage(), ex);
	    		error.setVisible(true);
	    	}
	    	catch (IOException ex) {
	    		// no viewer found for this file type
	    		ErrorDialog error = new ErrorDialog(topFrame, "Could not start viewer", ex.getMessage(), ex);
	    		error.setVisible(true);
	    	} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			

		}
		
		if ("OpenImage".equals(event.getActionCommand())) {
			
	    	try {
	    		
	    		// write the meta-parameter value to a temporary file
	    		File temp = File.createTempFile("specchio", mp.getDefaultFilenameExtension());
	    		temp.deleteOnExit();
	    		FileOutputStream fos = new FileOutputStream(temp);
	    		mp.writeValue(fos);
	    		fos.close();

	    		// launch the external viewer
	    		Desktop.getDesktop().open(temp);
	    	}
	    	catch (IllegalArgumentException ex) {
	    		// something wrong with the temporary file
	    		ErrorDialog error = new ErrorDialog(topFrame, "Could not start viewer", ex.getMessage(), ex);
	    		error.setVisible(true);
	    	}
	    	catch (IOException ex) {
	    		// no viewer found for this file type
	    		ErrorDialog error = new ErrorDialog(topFrame, "Could not start viewer", ex.getMessage(), ex);
	    		error.setVisible(true);
	    	} 

		}						
		
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent event) {
		popupMenu.show(this, event.getX(), event.getY());
		
	}


	@Override
	public void mouseReleased(MouseEvent event) {
		popupMenu.show(this, event.getX(), event.getY());
		
	}
	
		

}
