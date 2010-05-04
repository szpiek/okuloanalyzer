package pl.edu.uj.okulo.windows;

import javax.swing.JFrame;

public class ConfigurationFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 270;
	private int height = 180;
	
	public ConfigurationFrame()
	{
		super("Konfiguracja");
		setSize(width, height);
		setLocation(MainFrame.getLocationPoint());
		setResizable(true);
		setContentPane(new ConfigurationPane(this));
	}
	

}
