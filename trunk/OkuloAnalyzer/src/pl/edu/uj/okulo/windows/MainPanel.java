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
import pl.edu.uj.okulo.experiment.ExperimentConstants;
import pl.edu.uj.okulo.log.OkLogger;

public class MainPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	protected JButton addSensor, removeSensor, sensors, calibration, configuration, experiment;
	protected JProgressBar progress;
	protected JComboBox sensorsList, experimentList;
	protected JFrame mainFrame, addSensorFrame;
	protected SensorsPanel sensorsPane1, sensorsPane2;
	
	public static final String FIND_SENSORS_ACTION = "findSensors";
	public static final String DISCONNECT_SENSORS = "disconnectSensors";
	public static final String RUN_CALIBRATION_ACTION = "calibrate";
	public static final String ADD_SENSOR_ACTION = "addSensor";
	public static final String REMOVE_SENSOR_ACTION = "removeSensor";
	public static final String CONFIGURE_ACTION = "configure";
	public static final String EXPERIMENT_ACTION = "experiment";
	
	private static final String FIND_SENSORS_BUTTON = "Znajdź sensory";
	
	//stale
	public static String ADD_ACTION = "add";
	public static String CANCEL_ACTION = "cancel";	
	
	public MainPanel(JFrame frame)
	{
		super(new GridBagLayout());
		// ustawiamy przycisk do szukania sensorow
		Dimension d = this.getPreferredSize();
		
        sensors = this.prepareButton(MainPanel.FIND_SENSORS_BUTTON, MainPanel.FIND_SENSORS_ACTION);
        calibration = this.prepareButton("Kalibruj", MainPanel.RUN_CALIBRATION_ACTION);
        addSensor = this.prepareButton("Dodaj sensor", MainPanel.ADD_SENSOR_ACTION);
        removeSensor = this.prepareButton("Usuń sensor", MainPanel.REMOVE_SENSOR_ACTION);
        configuration = this.prepareButton("Konfiguruj", MainPanel.CONFIGURE_ACTION);
        experiment = this.prepareButton("Eksperyment", MainPanel.EXPERIMENT_ACTION);
        
        sensors.addActionListener(this);
        calibration.addActionListener(this);
        configuration.addActionListener(this);
        addSensor.addActionListener(this);
        removeSensor.addActionListener(this);
        experiment.addActionListener(this);
        
        sensors.setToolTipText("Kliknij aby szukac sensorów");

        progress = new JProgressBar(); 
        
        sensorsList = new JComboBox(Configuration.getConfiguration().getSensorsNames());
        experimentList = new JComboBox(ExperimentConstants.getExperiments());
        
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
        c.gridx = 1;
        add(configuration, c);
        d = sensorsList.getPreferredSize();
        d.width = 145;
        sensorsList.setPreferredSize(d);
        c.insets = new Insets(5, 5, 5, 5);
        
        c.gridy = 4;
        add(experimentList, c);
        c.gridx = 0;
        add(experiment,c);
        
        // sensors panel
        c.gridy = 5;
        c.gridheight = 2;
        sensorsPane1 = new SensorsPanel("Sensor 1");
        sensorsPane2 = new SensorsPanel("Sensor 2");
        add(sensorsPane1, c);
        c.gridx = 1;
        add(sensorsPane2, c);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(MainPanel.FIND_SENSORS_ACTION))
		{
			progress.setIndeterminate(true);
			sensors.setEnabled(false);
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
							finally
							{
								sensors.setEnabled(true);
							}
							found(found);
						}
					});
			t.start();
		}
		else if(e.getActionCommand().equals(MainPanel.DISCONNECT_SENSORS))
		{
			sensors.setEnabled(false);
			progress.setIndeterminate(true);
			Thread t = new Thread(new Runnable(){
				public void run()
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
						sensorsList.setEnabled(true);
						progress.setIndeterminate(false);
						sensors.setEnabled(true);
					}
					sensors.setText(MainPanel.FIND_SENSORS_BUTTON);
					sensors.setActionCommand(FIND_SENSORS_ACTION);
				}
			});
			t.start();
		}
		else if(e.getActionCommand().equals(MainPanel.REMOVE_SENSOR_ACTION))
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
		else if(e.getActionCommand().equals(MainPanel.ADD_SENSOR_ACTION))
		{
			if(this.addSensorFrame==null || !this.addSensorFrame.isVisible())
			{
				this.addSensorFrame = new AddSensorFrame(this);
				this.addSensorFrame.setVisible(true);
			}
		}
		else if(e.getActionCommand().equals(MainPanel.RUN_CALIBRATION_ACTION))
		{
			new CalibrationFrame().setVisible(true);
		}
		else if(e.getActionCommand().equals(MainPanel.CONFIGURE_ACTION))
		{
			new ConfigurationFrame().setVisible(true);
		}
		else if(e.getActionCommand().equals(MainPanel.EXPERIMENT_ACTION))
		{
			new ExperimentFrame().setVisible(true);
		}
	}
	
	private void found(int found)
	{
		OkLogger.info("Finished");
		this.progress.setIndeterminate(false);
		if(found>0)
		{
			Engine.getInstance().runSensors(this.sensorsPane1.getStatusStan(), this.sensorsPane1.getStatusOdczyt());
			this.sensorsList.setEnabled(false);
			this.sensors.setText("Odłącz sensory");
			this.sensors.setActionCommand(DISCONNECT_SENSORS);
			JOptionPane.showMessageDialog(this.mainFrame, "Znaleziono sensory.");
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
