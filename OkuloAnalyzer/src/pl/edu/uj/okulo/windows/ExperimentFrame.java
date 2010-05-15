package pl.edu.uj.okulo.windows;

import javax.swing.JFrame;

import pl.edu.uj.okulo.windows.panels.ExperimentPanel;

public class ExperimentFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 475;
	private int height = 340;
	
	
	public ExperimentFrame()
	{
		super("Eksperyment");
		setSize(width, height);
		setResizable(false);
		setLocation(MainFrame.getLocationPoint());
		setContentPane(new ExperimentPanel(this));
	}
}
