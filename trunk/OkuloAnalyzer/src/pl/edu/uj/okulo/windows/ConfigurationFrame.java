package pl.edu.uj.okulo.windows;

import javax.swing.JFrame;

import pl.edu.uj.okulo.windows.panels.ConfigurationPanel;

public class ConfigurationFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 420;
	private int height = 180;
	
	public ConfigurationFrame()
	{
		super("Konfiguracja");
		setSize(width, height);
		setLocation(MainFrame.getLocationPoint());
		setResizable(false);
		setContentPane(new ConfigurationPanel(this));
	}
	

}
