package pl.edu.uj.okulo.windows.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.edu.uj.okulo.engine.Configuration;
import pl.edu.uj.okulo.engine.Utilities;
import pl.edu.uj.okulo.windows.CalibrationFrame;

public class CalibrationPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel width, distance;
	private JTextField widthIn, distanceIn;
	private CalibrationFrame parent;

	public CalibrationPanel(CalibrationFrame p)
	{
		super(new GridBagLayout());
		this.parent = p;
		width = new JLabel("     Szerokość monitora:");
		distance = new JLabel("Odleglość od monitora:");
		widthIn = new JTextField(Configuration.getConfiguration().getWidth()==0?"":Configuration.getConfiguration().getWidth()+"");
		distanceIn = new JTextField(Configuration.getConfiguration().getDistance()==0?"":Configuration.getConfiguration().getDistance()+"");
		Dimension d = widthIn.getPreferredSize();
		d.setSize(70, d.getHeight()+3);
		widthIn.setPreferredSize(d);
		distanceIn.setPreferredSize(d);
		
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3,5,3,5);
        add(width, c);
        c.gridx = 1;
        add(widthIn, c);
        c.gridy = 1;
        add(distanceIn, c);
        c.gridx = 0;
        add(distance, c);
        c.gridy = 2;
        c.gridwidth = 2;
        c.insets = new Insets(12, 5, 0, 5);
        ControlPanel controlPane = new ControlPanel(this);
        controlPane.setAddText("Zapisz");
        add(controlPane, c);
        
	}

	private void showErrorInformation(String name, String message) {
		Utilities.showErrorInformation(parent, "Wystapił błąd podczas sprawdzania wartośc w polu "+name+"."
				+System.getProperty("line.separator")
				+message, "Błąd walidacji");
	}	
	
	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getActionCommand().equals(MainPanel.ADD_ACTION))
		{
			if(!Utilities.isDouble(this.widthIn.getText().trim()) || Double.parseDouble(this.widthIn.getText().trim())<=0)
			{
				showErrorInformation("szerokości monitora", "Szerokość monitora musi być dodatnią liczbą rzeczywistą.");
				return;
			}
			if(!Utilities.isInt(this.distanceIn.getText().trim()) || Integer.parseInt(this.distanceIn.getText().trim())<=0)
			{
				showErrorInformation("odległości od monitora", "Odległość od monitora musi być dodatnią liczbą całkowitą.");
				return;
			}
			Configuration.getConfiguration().setWidth(Double.parseDouble(this.widthIn.getText().trim()));
			Configuration.getConfiguration().setDistance(Integer.parseInt(this.distanceIn.getText().trim()));
			try {
				Configuration.getConfiguration().save();
			} catch (FileNotFoundException e) {
				Utilities.showErrorInformation(parent, "Błąd podczas zapisywania konfiguracji! "+e.getMessage(), "Błąd");
			} catch (IOException e) {
				Utilities.showErrorInformation(parent, "Błąd podczas zapisywania konfiguracji! "+e.getMessage(), "Błąd");
			}
		}
		this.parent.setVisible(false);
	}
}
