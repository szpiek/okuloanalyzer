package pl.edu.uj.okulo.windows.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


public class SensorsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private StatusPanel statusStan, statusOdczyt;

	public SensorsPanel(String title)
	{
		super(new GridLayout(0,2));
		
		Border line = BorderFactory.createLineBorder(Color.BLACK);
        TitledBorder sb = BorderFactory.createTitledBorder(line, title);		

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        setBorder(sb);
        Dimension d = getSize();
        d.height = 75;
        setPreferredSize(d);
        JLabel stan = new JLabel("      Stan: ");
        add(stan, c);
        statusStan = new StatusPanel();
        add(statusStan, c);
        c.gridy = 1;
        statusOdczyt= new StatusPanel();
        JLabel odczyt = new JLabel("  Odczyt: ");
        add(odczyt, c);
        add(statusOdczyt, c);		
	}
	
	public StatusPanel getStatusStan()
	{
		return this.statusStan;
	}
	
	public StatusPanel getStatusOdczyt()
	{
		return this.statusOdczyt;
	}
}
