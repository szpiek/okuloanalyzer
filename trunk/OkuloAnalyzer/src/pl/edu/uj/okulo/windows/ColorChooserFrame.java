package pl.edu.uj.okulo.windows;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColorChooserFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 450;
	private int height = 400;
	private ConfigurationPane parent;
	private JColorChooser cChooser;
	
	
	public ColorChooserFrame(ConfigurationPane p, Color color)
	{
		super("Wybierz kolor");
		parent = p;
		JPanel pane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		cChooser = new JColorChooser(color);
		pane.add(cChooser);
		
		setContentPane(pane);
		setSize(width, height);
		setLocation(MainFrame.getLocationPoint());
		ControlPanel control = new ControlPanel(parent);
		control.setAddText("OK");
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		pane.add(control, c);
		setResizable(false);
	}
	
	public Color getSelectedColor()
	{
		return cChooser.getColor();
	}
}
