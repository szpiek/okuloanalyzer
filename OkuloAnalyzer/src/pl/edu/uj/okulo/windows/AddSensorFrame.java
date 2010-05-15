package pl.edu.uj.okulo.windows;

import javax.swing.JFrame;

import pl.edu.uj.okulo.windows.panels.AddSensorPanel;
import pl.edu.uj.okulo.windows.panels.MainPanel;

public class AddSensorFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 285;
	private int height = 200;
	private MainPanel mainP;
	
	public AddSensorFrame(MainPanel m)
	{
		super("Dodaj sensor");
		this.mainP = m;
		setResizable(false);
		setSize(width, height);
		setLocation(MainFrame.getLocationPoint());
		setContentPane(new AddSensorPanel(this));
	}
	
	public void addSensorToList(String name)
	{
		this.mainP.getSensorsList().addItem(name);
	}
}
