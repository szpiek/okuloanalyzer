package pl.edu.uj.okulo.windows;

import javax.swing.JFrame;

public class ExperimentFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 285;
	private int height = 200;
	private MainPanel mainP;
	
	
	public ExperimentFrame()
	{
		super("Eksperyment");
		setSize(width, height);
		setLocation(MainFrame.getLocationPoint());
	}
}
