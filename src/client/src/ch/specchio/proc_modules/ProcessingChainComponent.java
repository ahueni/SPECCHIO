package ch.specchio.proc_modules;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import ch.specchio.processing_plane.ProcessingPlane;
import ch.specchio.processing_plane.ProcessingPlaneObject;


public abstract class ProcessingChainComponent extends Thread implements ActionListener {
	
	protected Frame owner;
	protected JPopupMenu popup;
	MouseListener popupListener;
	protected JPanel info_panel = null;
	protected ProcessingPlane processing_plane = null;
	protected ProcessingPlaneObject ppo; // containing processing plane object of this component
	
	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {				
				popup.show(e.getComponent(),
						e.getX(), e.getY());
				
			}
		}
	}	
	
	public ProcessingChainComponent(Frame frame)
	{
		owner = frame;
		popup = new JPopupMenu();		
		popupListener = new PopupListener();		
	}
	
	
	public void set_processing_plane(ProcessingPlane pp)
	{
		this.processing_plane = pp;
	}
	
	public void set_ppo(ProcessingPlaneObject ppo)
	{
		this.ppo = ppo;
	}
	
	public ProcessingPlaneObject get_ppo()
	{
		return ppo;
	}

	
	public JPanel get_info_panel()
	{
		info_panel = new JPanel();
		info_panel.setSize(new Dimension(260, 80));
		info_panel.addMouseListener(popupListener);
		return info_panel;
	}
	
	
	public Frame get_owner()
	{
		return owner;
	}
	
	public void show_menu(Component c, int x, int y)
	{
		popup.show(c, x, y);
		
	}

}
