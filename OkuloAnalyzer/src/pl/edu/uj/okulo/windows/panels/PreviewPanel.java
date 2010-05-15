package pl.edu.uj.okulo.windows.panels;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PreviewPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Color background = null;
	private Color impulse = null;
	private Integer impS = 0;
	
	public PreviewPanel(Color back, Color imp, Integer impSize)
	{
		background = back;
		impulse = imp;
		impS = impSize;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(impulse);
		g.fillOval(getWidth()/2-impS/2, getHeight()/2-impS/2, impS, impS);
	}
	
	public void setBackgroundColor(Color c)
	{
		this.background = c;
	}
	
	public void setImpulse(Color c)
	{
		this.impulse = c;
	}
	
	public void setImpSize(Integer i)
	{
		this.impS = i;
	}

}
