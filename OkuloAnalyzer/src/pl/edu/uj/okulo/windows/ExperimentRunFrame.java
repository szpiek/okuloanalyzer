package pl.edu.uj.okulo.windows;

import javax.swing.JFrame;

import pl.edu.uj.okulo.windows.panels.ExperimentRunPanel;

public class ExperimentRunFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 460;
	private int height = 380;
	
	public ExperimentRunFrame(String name)
	{
		super("Eksperyment "+name);
//		setSize(width, height);
//		setResizable(false);
//		setExtendedState(JFrame.MAXIMIZED_BOTH);
//		setLocation(MainFrame.getLocationPoint());
	}

	public void setPaneSize() {
		setContentPane(new ExperimentRunPanel(this));
	}
}
