package pl.edu.uj.okulo.engine;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbInterface;

import pl.edu.uj.okulo.log.OkLogger;
import pl.edu.uj.okulo.windows.StatusPanel;

public class SensorThread extends Thread {
	
	StatusPanel status, odczyt;
	UsbInterface sensor;
	public static final long SLEEP_STATUS = 10000;

	public SensorThread(UsbInterface usb, StatusPanel st, StatusPanel od)
	{
		this.sensor = usb;
		this.status = st;
		this.odczyt = od;
	}
	
	public void run()
	{
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				while(true)
				{
					boolean disc = false;
					try
					{
						sensor.getUsbConfiguration().getUsbDevice().getParentUsbPort();
					}
					catch(UsbDisconnectedException e)
					{
						OkLogger.error(e.getMessage());
						disc = true;
					}
					OkLogger.info("claimed: "+sensor.isClaimed()+" active: "+sensor.isActive()+" disconnected: "+disc);
					if(sensor.isClaimed() && sensor.isActive() && !disc)
					{
						status.setGreen();
					}
					else
					{
						status.setRed();
					}
					status.repaint();
					try {
						Thread.sleep(SLEEP_STATUS);
					} catch (InterruptedException e) {
						OkLogger.info("Błąd w wątku sprawdzającym status sensora! "+e.getMessage());
					}
				}
			}
		});
		t.start();
		
		while(true)
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			OkLogger.info("test Thread");
		}
	}
}
