package ch.specchio.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

class Figure extends JPanel
{
	private static final long serialVersionUID = 1L;
	private DrawingPanel dp;
	private JTextField caption;
	
	public Figure(String path, int width, int height)
	{				
		this(width, height, null);
		dp = new DrawingPanel(path, width, height);
		dp.setPreferredSize(new Dimension(width, height));

		this.add(dp, BorderLayout.CENTER);

	}
	
	public Figure(Image image, int width, int height, String caption)
	{				
		this(width, height, caption);
		dp = new DrawingPanel(image, width, height);
		dp.setPreferredSize(new Dimension(width, height));

		this.add(dp, BorderLayout.CENTER);

	}
	
	Figure(int width, int height, String caption)
	{
		this.setLayout(new BorderLayout());
		this.caption = new JTextField(caption, 30);
		this.add(this.caption, BorderLayout.SOUTH);			
	}
	
	
	public void addKeyListener(KeyListener kl) {
		
		this.caption.addKeyListener(kl);
		
	}
	
	
	public String getCaption()
	{
			return this.caption.getText();
	}
	
	
	public byte[] getJPEGImageData()
	{	
		return dp.getJPEGImageData();	
	}
	
	
	void redraw()
	{
		dp.redraw();		
	}
	
	public void setEditable(boolean editable)
	{
		this.caption.setEditable(editable);
	}
	
	
}