package pl.edu.uj.okulo.windows;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import pl.edu.uj.okulo.log.OkLogger;

public class MainPane extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	protected JButton sensors, calibration;
	protected JProgressBar progress;
	protected JComboBox sensorsList;
	protected JFrame mainFrame;
	protected SensorsPanel sensorsPane1, sensorsPane2;
	
	public static final String FIND_SENSORS_ACTION = "findSensors";
	public static final String DISCONNECT_SENSORS = "disconnectSensors";
	public static final String RUN_CALIBRATION_ACTION = "calibrate";
	
	private static final String FIND_SENSORS_BUTTON = "Znajdź sensory";
	
	public MainPane(JFrame frame)
	{
		super(new GridBagLayout());
		// ustawiamy przycisk do szukania sensorow
		Dimension d = this.getPreferredSize();
		d.height = 400;
		this.setPreferredSize(d);
		
        sensors = new JButton(MainPane.FIND_SENSORS_BUTTON);
        sensors.setVerticalTextPosition(AbstractButton.CENTER);
        sensors.setHorizontalTextPosition(AbstractButton.LEADING);
        sensors.setActionCommand(FIND_SENSORS_ACTION);
      
        calibration = new JButton("Kalibruj");
        calibration.setVerticalTextPosition(AbstractButton.CENTER);
        calibration.setHorizontalTextPosition(AbstractButton.LEADING);
        calibration.setActionCommand(RUN_CALIBRATION_ACTION);
        
        sensors.addActionListener(this);
        calibration.addActionListener(this);
        
        sensors.setToolTipText("Kliknij aby szukac sensorów");

        progress = new JProgressBar(); 
        
        sensorsList = new JComboBox(Configuration.getConfiguration().getSensorsNames());
        
        this.mainFrame = frame;
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(3,5,3,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        add(sensors, c);
        c.gridx = 1;
        add(sensorsList, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        add(progress, c);
        c.ipadx = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(5, 5, 5, 5);
        add(calibration, c);
        d = sensorsList.getPreferredSize();
        d.width = 145;
        sensorsList.setPreferredSize(d);
        
        
        // sensors panel
        c.gridy = 3;
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
				JOptionPane.showMessageDialog(this.mainFrame, "Wystapił błąd podczas odłączania sensorów. ["+e1.getMessage()+"]", 
						"Błąd", JOptionPane.ERROR_MESSAGE);	
			} catch (UsbNotActiveException e1) {
				JOptionPane.showMessageDialog(this.mainFrame, "Wystapił błąd podczas odłączania sensorów. ["+e1.getMessage()+"]", 
						"Błąd", JOptionPane.ERROR_MESSAGE);	
			} catch (UsbDisconnectedException e1) {
				JOptionPane.showMessageDialog(this.mainFrame, "Wystapił błąd podczas odłączania sensorów. ["+e1.getMessage()+"]", 
						"Błąd", JOptionPane.ERROR_MESSAGE);	
			} catch (UsbException e1) {
				JOptionPane.showMessageDialog(this.mainFrame, "Wystapił błąd podczas odłączania sensorów. ["+e1.getMessage()+"]", 
						"Błąd", JOptionPane.ERROR_MESSAGE);
			}
			finally
			{
				this.sensorsList.setEnabled(true);
				this.sensors.setText(MainPane.FIND_SENSORS_BUTTON);
				this.sensors.setActionCommand(FIND_SENSORS_ACTION);
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
			JOptionPane.showMessageDialog(this.mainFrame, "Wystapił błąd podczas szukania sensorów.", 
					"Błąd", JOptionPane.ERROR_MESSAGE);		
		else
			JOptionPane.showMessageDialog(this.mainFrame, "Nie znaleziono szukanych sensorów.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
	}

}
