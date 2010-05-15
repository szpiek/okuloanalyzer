package pl.edu.uj.okulo.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.usb.UsbConst;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;

import pl.edu.uj.okulo.log.OkLogger;
import pl.edu.uj.okulo.windows.panels.StatusPanel;

public class SensorThread extends Thread {
	
	StatusPanel status, odczyt;
	UsbInterface sensor;
	private boolean run = true, sensorDisconnected = false;
	public static final long SLEEP_STATUS = 10000;
	public static final long SLEEP_READ_STATUS = 2000;
	private FileOutputStream dataFile;

	public SensorThread(UsbInterface usb, StatusPanel st, StatusPanel od, File f)
	{
		this.sensor = usb;
		this.status = st;
		this.odczyt = od;
		prepareForWriting(f);
	}
	
	public void setAllRed()
	{
		this.odczyt.setRed();
		this.status.setRed();
	}
	
	public void setAllBlack()
	{
		this.odczyt.setBlack();
		this.status.setBlack();
	}
	
	public void stopThreads()
	{
		this.run = false;
	}
	
	public void setOutputFile(File f)
	{
		prepareForWriting(f);
	}
	
	private synchronized FileOutputStream getDataFile()
	{
		return dataFile;
	}
	
	private void prepareForWriting(File f)
	{
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				OkLogger.error(e.getMessage());
			}
		try {
			this.dataFile = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			OkLogger.error(e.getMessage());
		}
	}
	
	private void error(final Exception e)
	{
		OkLogger.error(e.getMessage());
		isDisconnected();
		this.setAllRed();
	}
	
	public void run()
	{
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				while(run)
				{
					if(sensor.isClaimed() && sensor.isActive() && !isDisconnected())
					{
						OkLogger.info("disc: "+isDisconnected());
						status.setGreen();
					}
					else
					{
						status.setRed();
					}
					try {
						Thread.sleep(SLEEP_STATUS);
					} catch (InterruptedException e) {
						OkLogger.info("Błąd w wątku sprawdzającym status sensora! "+e.getMessage());
					}
				}
			}
		});
		t.start();
		
		List<?> endpoints = sensor.getUsbEndpoints();
		UsbEndpoint mainPoint = null;
		for(Object e: endpoints)
		{
			mainPoint = (UsbEndpoint)e;
			if (UsbConst.ENDPOINT_TYPE_INTERRUPT == mainPoint.getType() && 
					UsbConst.ENDPOINT_DIRECTION_IN == mainPoint.getDirection())
				break;
		}
		if(mainPoint==null)
		{
			OkLogger.error("Nie można znaleźć prawidłowego EndPoint dla wybranego urządzenia!");
			odczyt.setRed();
			return;
		}
		UsbPipe pipe = mainPoint.getUsbPipe();
		try {
			pipe.open();
		} catch (UsbException e1) {
			error(e1);
			return;
		}
		SensorReadThread sensorRead = new SensorReadThread(pipe);
		sensorRead.start();
		while(run)
		{
			OkLogger.info("Dlugosc wyslanych danych: "+sensorRead.getStatus());
			if(sensorRead.getStatus()==0 && !isDisconnected())
			{
				OkLogger.info("Ustawiam odczyt");
				odczyt.setDarkGreen();
			}
			sensorRead.setStatus(0);
			try {
				Thread.sleep(SLEEP_READ_STATUS);
			} catch (InterruptedException e1) {
				error(e1);
				return;
			}
			//OkLogger.info("Next iteration...");
		}
		sensorRead.stopThread();
		try {
			pipe.abortAllSubmissions();
			pipe.close();
		} catch (UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public boolean isDisconnected()
	{
		if(!sensorDisconnected);
			try
			{
				sensor.getUsbConfiguration().getUsbDevice().getParentUsbPort();
			}
			catch(UsbDisconnectedException e)
			{
				OkLogger.error(e.getMessage());
				OkLogger.info("Ustawiam na true");
				sensorDisconnected = true;
				setAllRed();
			}
		return sensorDisconnected;
	}
	
	private void setOdczytGreen()
	{
		this.odczyt.setGreen();
	}
	
	private byte getValue(Byte val)
	{
//		return (byte) Integer.parseInt(Long.toHexString(new Long(0x00000000000000ff & val)),16);
		return (byte)val.intValue();
	}
	
	private class SensorReadThread extends Thread
	{
		UsbPipe pipe;
		int status = 0;
		public SensorReadThread(final UsbPipe pipe)
		{
			this.pipe = pipe;
		}
		
		@Override
		public void run() {
			byte[] data = new byte[pipe.getUsbEndpoint().getUsbEndpointDescriptor().wMaxPacketSize()];
			while(run)
			{
				try {
					status = pipe.syncSubmit(data);
				} catch (UsbException e1) {
					error(e1);
					return;
				}
				OkLogger.info("Odczytano: x "+getValue(data[1])+" y "+getValue(data[2]));
				String output = System.currentTimeMillis()+"\t"+getValue(data[1])+"\t"+getValue(data[2])+System.getProperty("line.separator");
				try {
					getDataFile().write(output.getBytes());
				} catch (IOException e) {
					OkLogger.error(e.getMessage());
				}
				if(status>0 && !isDisconnected())
					setOdczytGreen();
			}
		}
		
		public int getStatus()
		{
			return status;
		}
		
		public void setStatus(int stat)
		{
			this.status = stat;
		}
		
		@SuppressWarnings("deprecation")
		public void stopThread()
		{
			try {
				getDataFile().close();
			} catch (IOException e) {
				OkLogger.error(e.getMessage());
			}
			this.stop();
		}
		
	}
}
