package pl.edu.uj.okulo.windows;

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
import pl.edu.uj.okulo.engine.SensorDescription;
import pl.edu.uj.okulo.engine.Utilities;

public class AddSensorPane extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel venLabel, prodLabel, nameLabel, dpiLabel, infoLabel;
	private JTextField venIn, prodIn, nameIn, dpiIn;
	private AddSensorFrame parent;
	
	public AddSensorPane(AddSensorFrame p)
	{
		super(new GridBagLayout());
		parent = p;
		GridBagConstraints c = new GridBagConstraints();
		venLabel = new JLabel(" Vendor id: ");
		prodLabel = new JLabel("Product id: ");
		nameLabel = new JLabel("      Nazwa: ");
		dpiLabel = new JLabel("            DPI: ");
		infoLabel = new JLabel("         Wypełnij poniższe pola.");
		
		venIn = new JTextField();
		nameIn = new JTextField();
		prodIn = new JTextField();
		dpiIn = new JTextField();
		
		Dimension d = nameIn.getPreferredSize();
		d.setSize(150, d.getHeight());
		nameIn.setPreferredSize(d);
		venIn.setPreferredSize(d);
		prodIn.setPreferredSize(d);
		dpiIn.setPreferredSize(d);
		
        c.insets = new Insets(0,5,10,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        add(infoLabel, c);
        c.insets = new Insets(3,5,3,5);
        c.gridwidth = 1;
		c.gridy = 1;
		add(nameLabel, c);
		c.gridx = 1;
		add(nameIn, c);
		c.gridy = 2;
		add(venIn,c);
		c.gridx = 0;
		add(venLabel, c);
		c.gridy = 3;
		add(prodLabel, c);
		c.gridx = 1;
		add(prodIn, c);
		c.gridy = 4;
		add(dpiIn, c);
		c.gridx = 0;
		add(dpiLabel, c);
		
		ControlPane controlPane = new ControlPane(this);
		c.gridy = 5;
		c.gridwidth = 2;
		add(controlPane, c);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getActionCommand().equals(MainPane.ADD_ACTION))
		{
			if(checkAllInput())
			{
				try {
					Configuration.getConfiguration().addSensor(new SensorDescription(this.nameIn.getText(),
							Short.parseShort(this.venIn.getText()), Short.parseShort(this.prodIn.getText()),
							Integer.parseInt(this.dpiIn.getText())));
					parent.addSensorToList(this.nameIn.getText().trim());
				} catch (NumberFormatException e) {
					Utilities.showErrorInformation(parent, e.getMessage(), "Błąd");
				} catch (FileNotFoundException e) {
					Utilities.showErrorInformation(parent, e.getMessage(), "Błąd");
				} catch (IOException e) {
					Utilities.showErrorInformation(parent, e.getMessage(), "Błąd");
				}
			}
		}
		this.parent.setVisible(false);
	}

	private boolean checkAllInput() {
		if(this.nameIn.getText().trim().length()==0)
		{
			showErrorInformation("nazwą","Nazwa sensora musi zostać podana.");
			return false;
		}
		if(!Utilities.isInt(this.venIn.getText().trim()))
		{
			showErrorInformation("vendor id", "Vendor id musi byc liczbą całkowitą.");
			return false;
		}
		if(!Utilities.isInt(this.prodIn.getText().trim()))
		{
			showErrorInformation("product id", "Product id musi byc liczbą całkowitą.");
			return false;
		}
		if(!Utilities.isInt(this.dpiIn.getText().trim()))
		{
			showErrorInformation("DPI", "DPI musi byc liczbą całkowitą.");
			return false;
		}
		return true;
	}

	private void showErrorInformation(String name, String message) {
		Utilities.showErrorInformation(parent, "Wystapił błąd podczas sprawdzania wartośc w polu z "+name+"."
				+System.getProperty("line.separator")
				+message, "Błąd walidacji");
	}
}
