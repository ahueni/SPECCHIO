package ch.specchio.gui;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	Image image;
	Image scaled;
	int width, height;
	String path;

	
	DrawingPanel (Image image, int width, int height)
	{
		this.width = width;
		this.height = height;
		this.image = image;
		
		scale_image();
		
	}
	
	DrawingPanel (String path, int width, int height)
	{ 
		this.width = width;
		this.height = height;
		this.path = path;
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		image = toolkit.getImage(path);

		
		SPECCHIOApplication app = SPECCHIOApplication.getInstance();
		MediaTracker mediaTracker = new MediaTracker(app.get_frame());
		mediaTracker.addImage(image, 0);
		try
		{
			mediaTracker.waitForID(0);
		}
		catch (InterruptedException ie)
		{
			System.err.println(ie);
			System.exit(1);
		}
		
		scale_image();
	}
	
	
	void scale_image()
	{
		int x, y;
		int imgX = image.getWidth (this);
		int imgY = image.getHeight (this);
		
		
		
		if (imgX > imgY)
		{
			float xy_factor = (float)imgX / imgY;
			x = width;
			y = (int) (x/xy_factor);
		}
		else
		{
			float yx_factor = (float)imgY / imgX;
			y = height;
			x = (int) (y/yx_factor);			
		}
		
		scaled = image.getScaledInstance(x, y, Image.SCALE_FAST);				
	}
	
	public byte[] getJPEGImageData() {	
		
	    BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
	    Graphics bg = bImage.getGraphics();
	    bg.drawImage(image, 0, 0, null);
	    bg.dispose();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
			ImageIO.write(bImage, "jpeg", baos);
		} catch (IOException e) {
			// don't know why this might happen
			e.printStackTrace();
		}
	    
	    return baos.toByteArray();
		
	}
	
	public void paintComponent (Graphics g) {
		super.paintComponent (g);
		g.drawImage (scaled, 0, 0, this);
	} 
	
	void redraw()
	{
		Graphics g = getGraphics();
		if (g != null) paintComponent(g);
		else repaint();
		
	}
	
	
}




