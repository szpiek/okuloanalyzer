package pl.edu.uj.okulo.windows;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.usb.UsbClaimException;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;

import pl.edu.uj.okulo.engine.Configuration;
import pl.edu.uj.okulo.engine.Engine;
import pl.edu.uj.okulo.engine.SensorDescription;
import pl.edu.uj.okulo.engine.Utilities;
import pl.edu.uj.okulo.log.OkLogger;

public class MainPane extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	protected JButton addSensor, removeSensor, sensors, calibration;
	protected JProgressBar progress;
	protected JComboBox sensorsList;
	protected JFrame mainFrame, addSensorFrame;
	protected SensorsPanel sensorsPane1, sensorsPane2;
	
	public static final String FIND_SENSORS_ACTION = "findSensors";
	public static final String DISCONNECT_SENSORS = "disconnectSensors";
	public static final String RUN_CALIBRATION_ACTION = "calibrate";
	public static final String ADD_SENSOR_ACTION = "addSensor";
	public static final String REMOVE_SENSOR_ACTION = "removeSensor";
	
	private static final String FIND_SENSORS_BUTTON = "Znajdź sensory";
	
	public MainPane(JFrame frame)
	{
		super(new GridBagLayout());
		// ustawiamy przycisk do szukania sensorow
		Dimension d = this.getPreferredSize();
		
        sensors = this.prepareButton(MainPane.FIND_SENSORS_BUTTON, MainPane.FIND_SENSORS_ACTION);
        calibration = this.prepareButton("Kalibruj", MainPane.RUN_CALIBRATION_ACTION);
        addSensor = this.prepareButton("Dodaj sensor", MainPane.ADD_SENSOR_ACTION);
        removeSensor = this.prepareButton("Usuń sensor", MainPane.REMOVE_SENSOR_ACTION);
        
        sensors.addActionListener(this);
        calibration.addActionListener(this);
         
        addSensor.addActionListener(this);
        removeSensor.addActionListener(this);
        
        sensors.setToolTipText("Kliknij aby szukac sensorów");

        progress = new JProgressBar(); 
        
        sensorsList = new JComboBox(Configuration.getConfiguration().getSensorsNames());
        
        this.mainFrame = frame;
        
        
        GridBagConstraints c = new GridBagConstraints();
        // pierwszy wiersz
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(3,5,3,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        add(sensors, c);
        c.gridx = 1;
        add(sensorsList, c);
        //drugi wiersz wiersz
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        add(progress, c);
        // trzeci wiersz
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        add(addSensor, c);
        c.gridx = 1;
        add(removeSensor, c);
        //czwarty wiersz
        c.ipadx = 1;
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(25, 5, 5, 5);
        add(calibration, c);
        d = sensorsList.getPreferredSize();
        d.width = 145;
        sensorsList.setPreferredSize(d);
        c.insets = new Insets(5, 5, 5, 5);
        
        // sensors panel
        c.gridy = 4;
        c.gridheight = 2;
        sensorsPane1 = new SensorsPanel("Sensor 1");
        sensorsPane2 = new SensorsPanel("Sensor 2");
        add(sensorsPane1, c);
        c.gridx = 1;
        add(sensorsPane2, c);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(MainPane.FIND_SENSORS_ACTION))
		{
			progress.setIndeterminate(true);
			Thread t = new Thread(
					new Runnable()
					{
						public void run()
						{
							SensorDescription sd = Configuration.getConfiguration().getSensor(sensorsList.getSelectedItem().toString());
							int found = 1;
							try {
								found = Engine.getInstance().findSensors(sd)?1:0;
							} catch (SecurityException e1) {
								OkLogger.info(e1.getMessage());
								found = -1;
							} catch (UsbException e1) {
								OkLogger.info(e1.getMessage());
								found = -1;
							}
							actionFinished(found);
						}
					});
			t.start();
		}
		else if(e.getActionCommand().equals(MainPane.DISCONNECT_SENSORS))
		{
			try {
				Engine.getInstance().disconnectSensors();
			} catch (UsbClaimException e1) {
				Utilities.showErrorInformation(mainFrame, "Wystapił błąd podczas odłączania sensorów. ["+e1.getMessage()+"]", "Błąd");
			} catch (UsbNotActiveException e1) {
				Utilities.showErrorInformation(mainFrame, "Wystapił błąd podczas odłączania sensorów. ["+e1.getMessage()+"]", "Błąd");
			} catch (UsbDisconnectedException e1) {
				Utilities.showErrorInformation(mainFrame, "Wystapił błąd podczas odłączania sensorów. ["+e1.getMessage()+"]", "Błąd");
			} catch (UsbException e1) {
				Utilities.showErrorInformation(mainFrame, "Wystapił błąd podczas odłączania sensorów. ["+e1.getMessage()+"]", "Błąd");
			}
			finally
			{
				this.sensorsList.setEnabled(true);
				this.sensors.setText(MainPane.FIND_SENSORS_BUTTON);
				this.sensors.setActionCommand(FIND_SENSORS_ACTION);
			}
		}
		else if(e.getActionCommand().equals(MainPane.REMOVE_SENSOR_ACTION))
		{
			if(this.sensorsList.getItemCount()==1)
			{
				JOptionPane.showMessageDialog(this.mainFrame, "Nie można usunąć ostatniego sensora.");
				return;
			}
			int answer = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąć wybrany sensor?", "Potwierdź usunięcie", JOptionPane.YES_NO_OPTION);
			int saveErr = -1;
			if(answer==0)
			{
				try {
					saveErr = Configuration.getConfiguration().removeSensor(this.sensorsList.getSelectedItem().toString());
		        } catch (FileNotFoundException e1) {
		        	OkLogger.info(e1.getMessage());
		        	Utilities.showErrorInformation(mainFrame, "Nie znaleziono pliku konfiguracyjnego "+Configuration.CONFIG_FILENAME+" ["+e1.getMessage()+"]", 
		        			"Błąd");
		        } catch (IOException e1) {
		        	OkLogger.info(e1.getMessage());
		        	Utilities.showErrorInformation(mainFrame, "Błąd podczas otwierania pliku konfiguracyjnego "+Configuration.CONFIG_FILENAME+" ["+e1.getMessage()+"]", 
        			"Błąd");
		        }
		        if(saveErr==0)
		        	this.sensorsList.removeItem(this.sensorsList.getSelectedItem());
			}
		     
		}
		else if(e.getActionCommand().equals(MainPane.ADD_SENSOR_ACTION))
		{
			if(this.addSensorFrame==null || !this.addSensorFrame.isVisible())
			{
				this.addSensorFrame = new AddSensorFrame(this);
				this.addSensorFrame.setVisible(true);
			}
		}
	}
	
	private void actionFinished(int found)
	{
		OkLogger.info("Finished");
		this.progress.setIndeterminate(false);
		if(found>0)
		{
			this.sensorsList.setEnabled(false);
			this.sensors.setText("Odłącz sensory");
			this.sensors.setActionCommand(DISCONNECT_SENSORS);
			JOptionPane.showMessageDialog(this.mainFrame, "Znaleziono sensory.");
			Engine.getInstance().runSensors(this.sensorsPane1.getStatusStan(), this.sensorsPane1.getStatusOdczyt());
		}
		else if (found<0)
			Utilities.showErrorInformation(this.mainFrame, "Wystapił błąd podczas szukania sensorów.", "Błąd");	
		else
			JOptionPane.showMessageDialog(this.mainFrame, "Nie znaleziono szukanych sensorów.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private JButton prepareButton(String text, String action)
	{
		JButton b = new JButton(text);
        b.setVerticalTextPosition(AbstractButton.CENTER);
        b.setHorizontalTextPosition(AbstractButton.LEADING);
        b.setActionCommand(action);
        return b;
	}
	
	public JComboBox getSensorsList()
	{
		return this.sensorsList;
	}
	
	

}
