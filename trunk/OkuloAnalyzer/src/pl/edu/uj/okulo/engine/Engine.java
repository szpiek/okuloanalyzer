package pl.edu.uj.okulo.engine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	
	private final String fileTemplate = "tempData_";
	
	private Engine(){}
	
	public static Engine getInstance()
	{
		if(singleton==null)
		{
			singleton = new Engine();
		}
		return singleton;
	}
	
	private File getTempDataFile()
	{
		return new File("tempData_"+new SimpleDateFormat("yyyyMMdd_HH.mm").format(new Date())+".dat");
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
		sensor1 = new SensorThread(sensors.get(0),statuses[0], statuses[1], singleton.getTempDataFile());
		if(statuses.length>2)
		{
			sensor2 = new SensorThread(sensors.get(1),statuses[2],statuses[3], singleton.getTempDataFile());
		}
		sensor1.start();
	}

	public void disconnectSensors() throws UsbClaimException, UsbNotActiveException, UsbDisconnectedException, UsbException {
	
		if(sensor1!=null)
		{
			sensor1.stopThreads();
		}
		while(sensor1.isAlive())
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				OkLogger.error(e.getMessage());
			}
			
		}
		try
		{
			for(UsbInterface u: sensors)
			{
				OkLogger.info("Odłączam sensor: "+u);
					u.release();
			}
		}
		catch(UsbException e)
		{
			throw e;
		}
		finally
		{
			sensor1.setAllBlack();
		}
	}

}
