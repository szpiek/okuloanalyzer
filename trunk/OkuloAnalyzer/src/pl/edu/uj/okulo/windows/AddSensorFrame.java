package pl.edu.uj.okulo.windows;

import javax.swing.JFrame;

public class AddSensorFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 285;
	private int height = 200;
	private MainPane mainP;
	
	public AddSensorFrame(MainPane m)
	{
		super("Dodaj sensor");
		this.mainP = m;
		setResizable(false);
		setSize(width, height);
		setLocation(MainFrame.getLocationPoint());
		setContentPane(new AddSensorPane(this));
	}
	
	public void addSensorToList(String name)
	{
		this.mainP.getSensorsList().addItem(name);
	}
}
