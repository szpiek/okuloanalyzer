package pl.edu.uj.okulo.windows;

import javax.swing.JFrame;

import pl.edu.uj.okulo.windows.panels.ExperimentPanel;

public class ExperimentFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 460;
	private int height = 380;
	
	
	public ExperimentFrame()
	{
		super("Eksperyment");
		setSize(width, height);
		setResizable(false);
		setLocation(MainFrame.getLocationPoint());
		setContentPane(new ExperimentPanel(this));
	}
}
