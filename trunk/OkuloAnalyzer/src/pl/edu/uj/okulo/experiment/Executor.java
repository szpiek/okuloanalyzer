package pl.edu.uj.okulo.experiment;

import java.awt.Graphics;

public interface Executor {
	public static final Short DRAW = 1;
	public static final Short CLEAN = 0;
	
	public void execute(Graphics g);
	public void addExecutor(Executor e);
}
