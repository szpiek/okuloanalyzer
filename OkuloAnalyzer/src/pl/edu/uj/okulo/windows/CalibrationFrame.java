package pl.edu.uj.okulo.windows;

import javax.swing.JFrame;

import pl.edu.uj.okulo.windows.panels.CalibrationPanel;

public class CalibrationFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 280;
	private int height = 160;
	
	public CalibrationFrame()
	{
		super("Kalibracja");
		setResizable(false);
		setSize(width, height);
		setLocation(MainFrame.getLocationPoint());
		CalibrationPanel pane = new CalibrationPanel(this);
		setContentPane(pane);
	}
}
