package ch.specchio.gui;

public interface ProgressReportInterface {
	
	public void set_progress(final Double value);
	public boolean set_progress(final int value);
	public void set_operation(String op);
	public void set_min_max(int min, int max);

}
