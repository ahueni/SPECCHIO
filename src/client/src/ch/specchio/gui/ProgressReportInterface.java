package ch.specchio.gui;

public interface ProgressReportInterface {
	
	public void set_component(String c);
	public void set_indeterminate(boolean indeterminate);
	public boolean set_progress(double value);
	public boolean set_progress(int value);
	public void set_operation(String op);
	public void set_min_max(int min, int max);

}
