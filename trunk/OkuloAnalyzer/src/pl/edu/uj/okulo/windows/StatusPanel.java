package pl.edu.uj.okulo.windows;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class StatusPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Color c = Color.BLACK;

	public void paintComponent(Graphics g)
	{
		g.setColor(c);
		g.fillRect(15, 7, 10, 10);
	}

	public void setGreen() {
		this.c = Color.GREEN;
		this.repaint();
	}
	
	public void setBlack()
	{
		this.c = Color.black;
		this.repaint();
	}
	
	public void setDarkGreen()
	{
		this.c = new Color(0,128,0);
		this.repaint();
	}
	
	public void setRed()
	{
		this.c = Color.red;
		this.repaint();
	}
}
