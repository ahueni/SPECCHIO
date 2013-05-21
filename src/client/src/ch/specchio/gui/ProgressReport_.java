package ch.specchio.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class ProgressReport_ extends JPanel implements ListCellRenderer{

	private static final long serialVersionUID = 1L;
	JLabel operation_desc;	
	JLabel curr_op;
	GridbagLayouter l;
	
	public ProgressReport_ ()
	{
		l = new GridbagLayouter(this);
		
		// set surounding border
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	public void set_op_desc(String desc)
	{
		operation_desc.setText(desc);
	}
	
	public void set_curr_op_desc(String desc)
	{
		curr_op.setText(desc);
	}

	public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
		// TODO Auto-generated method stub
		return this;
	}

	
}
