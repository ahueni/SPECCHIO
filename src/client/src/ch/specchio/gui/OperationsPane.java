package ch.specchio.gui;


import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;


class ReportContainer extends JPanel{

	private static final long serialVersionUID = 1L;

	GridbagLayouter l;
	
	public ReportContainer ()
	{
		l = new GridbagLayouter(this);	
	}
}


class ReportRemover extends Thread
{
	ReportContainer frame; 
	ProgressReportPanel rep;
	OperationsPane op;
	
	public ReportRemover(OperationsPane op, ReportContainer frame, ProgressReportPanel rep)
	{
		this.frame = frame;
		this.rep = rep;		
		this.op = op;
	}
	
	public void run () 
	{				
		try {
			sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("remove component");
		frame.remove(rep);
		frame.repaint();
	}

}


// uses the Singleton Pattern
public class OperationsPane extends JScrollPane {

	private static final long serialVersionUID = 1L;
	private static OperationsPane instance = null;
	int rep_cnt;
	GridBagConstraints constraints;
	ScrollPaneLayout spl;	
	ReportContainer frame;
	
	//public JFrame application_frame;

	   protected OperationsPane() {
	      // Exists only to defeat instantiation.
		  setPreferredSize(new Dimension(200, 0));
		  spl = new ScrollPaneLayout();
		  setLayout(spl);
		  
		  rep_cnt = 0;
		  frame = new ReportContainer();
		  
		  constraints = new GridBagConstraints();
		  constraints.gridwidth = 1;
		  constraints.gridheight = 1;	
		  constraints.anchor = GridBagConstraints.NORTHWEST;
		  constraints.gridx = 0;  
		  
		  getViewport().add(frame);		  
	   }
	   public static OperationsPane getInstance() 
	   {
	      if(instance == null) {
	         instance = new OperationsPane();
	      }
	      return instance;
	   }
	   
	   public void add_report(ProgressReportPanel rep)
	   {
		   constraints.gridy = rep_cnt;
		   frame.l.insertComponent(rep, constraints);
		   rep_cnt++;
		   this.validate(); // force the redraw on screen
	   }
	   
	   public void remove_report(ProgressReportPanel rep)
	   {		   
		   ReportRemover rr = new ReportRemover(this, frame, rep);		   
		   rr.start();		   
	   }

}
