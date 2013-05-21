package ch.specchio.gui;

import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JPanel;

import ch.specchio.types.SerialisableBufferedImage;

public class ImagePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	public SerialisableBufferedImage  image;
	
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

}
