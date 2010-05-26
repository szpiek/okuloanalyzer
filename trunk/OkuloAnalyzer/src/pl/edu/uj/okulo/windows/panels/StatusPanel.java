package pl.edu.uj.okulo.windows.panels;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class StatusPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Color c = Color.BLACK;
	private boolean block = false;

	public void paintComponent(Graphics g)
	{
		g.setColor(c);
		g.fillRect(15, 7, 10, 10);
	}

	public synchronized void setGreen() {
		if(!block)
		{
			this.c = Color.GREEN;
			this.repaint();
		}
	}
	
	public synchronized  void setBlack()
	{
		this.c = Color.black;
		this.repaint();
		block = false;
	}
	
	public synchronized  void setDarkGreen()
	{
		if(!block)
		{
			this.c = new Color(0,128,0);
			this.repaint();
		}
	}
	
	public synchronized void setRed()
	{
		this.c = Color.red;
		this.repaint();
		block = true;
	}
}
