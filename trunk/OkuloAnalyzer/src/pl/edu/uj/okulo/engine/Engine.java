package pl.edu.uj.okulo.engine;

import java.util.ArrayList;

import javax.usb.UsbClaimException;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbNotActiveException;

import pl.edu.uj.okulo.log.OkLogger;
import pl.edu.uj.okulo.usb.FindSensors;
import pl.edu.uj.okulo.windows.StatusPanel;


public class Engine {

	private static Engine singleton = null;
	private final int sensorsAmount = 1;
	private ArrayList<UsbInterface> sensors = new ArrayList<UsbInterface>();
	private SensorThread sensor1, sensor2;
	
	private Engine(){}
	
	public static Engine getInstance()
	{
		if(singleton==null)
		{
			singleton = new Engine();
		}
		return singleton;
	}
	
	public boolean findSensors(SensorDescription sd) throws SecurityException, UsbException
	{
		this.sensors = FindSensors.getSensors(sd.getVendor(), sd.getProduct());
		if(this.sensors.size()==this.sensorsAmount)
			return true;
		return false;
	}
	
	public void runSensors(StatusPanel ... statuses)
	{
		sensor1 = new SensorThread(sensors.get(0),statuses[0], statuses[1]);
		if(statuses.length>2)
		{
			sensor2 = new SensorThread(sensors.get(1),statuses[2],statuses[3]);
		}
		sensor1.start();
	}

	public void disconnectSensors() throws UsbClaimException, UsbNotActiveException, UsbDisconnectedException, UsbException {
		
		for(UsbInterface u: sensors)
		{
			OkLogger.info("Odłączam sensor: "+u);
			u.release();
		}
	}

}
