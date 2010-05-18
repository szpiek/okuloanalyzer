package pl.edu.uj.okulo.windows;

import javax.swing.JFrame;

import pl.edu.uj.okulo.windows.panels.ExperimentRunPanel;

public class ExperimentRunFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public ExperimentRunFrame(String name)
	{
		super("Eksperyment "+name);
	}

	public void setPaneSize() {
		setContentPane(new ExperimentRunPanel(this));
	}
}
