package pl.edu.uj.okulo.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;

import pl.edu.uj.okulo.log.OkLogger;
import pl.edu.uj.okulo.windows.panels.StatusPanel;

public class SensorStatusThread extends Thread {
	
	StatusPanel status, odczyt;
	UsbInterface sensor;
	private boolean run = true, sensorDisconnected = false;
	public static final long SLEEP_STATUS = 10000;
	public static final long SLEEP_READ_STATUS = 2000;
	private FileOutputStream dataFile;
	private long expStart;
	private long pauseTime = 0;
	private long pauseStart = 0;
	private boolean pause = false;
	private boolean isExperiment = false;
	private SensorReadThread sensorRead;

	public SensorStatusThread(UsbInterface usb, StatusPanel st, StatusPanel od, File f)
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
	
	public void setExperiment(boolean b)
	{
		this.isExperiment = b;
	}
	
	public void stopThreads()
	{
		this.run = false;
	}
	
	public void setOutputFile(File f)
	{
		prepareForWriting(f);
	}
	
	public void setOutputFileStream(FileOutputStream f)
	{
		this.dataFile = f;
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
		UsbPipe pipe = Utilities.getPipeForUsbInterface(sensor);
		if(pipe==null)
		{
			odczyt.setRed();
			return;
		}
		try {
			pipe.open();
		} catch (UsbException e1) {
			error(e1);
			return;
		}
		sensorRead = new SensorReadThread(pipe);
		sensorRead.start();
		while(run)
		{
			if(sensorRead.getStatus()==0 && !isDisconnected())
			{
				odczyt.setDarkGreen();
			}
			sensorRead.setStatus(0);
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
			OkLogger.error(e1.getMessage());
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
	
	public void setExpStart(long expStart) {
		this.expStart = expStart;
	}

	public long getExpStart() {
		return expStart;
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
				String output = this.getOutputString(data);
				try {
					getDataFile().write(output.getBytes());
				} catch (IOException e) {
					error(e);
				}
				if(status>0 && !isDisconnected())
					setOdczytGreen();
				if(isExperiment && pause)
					try {
						synchronized(this)
						{
							this.wait();
						}
					} catch (InterruptedException e) {
						error(e);
					}
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
		
		public void stopThread()
		{
			try {
				getDataFile().close();
			} catch (IOException e) {
				OkLogger.error(e.getMessage());
			}
			this.interrupt();
		}
		
		public String getOutputString(byte[] data)
		{
			long time = System.currentTimeMillis()-(isExperiment?expStart+pauseTime:0);
			OkLogger.info("TIme: "+System.currentTimeMillis()+" expTime: "+(
					expStart+pauseTime)+" "+time);
			return time+"\t"+Utilities.getByteValue(data[1])+"\t"+Utilities.getByteValue(data[2])+
			(isExperiment?"\t"+Engine.getInstance().getImpulses():"")+System.getProperty("line.separator");
		}
		
		public synchronized void awake()
		{
			this.notifyAll();
		}
		
	}

	public synchronized void pauseExperiment(boolean b) {
		if(pause)
		{
			sensorRead.awake();
			this.pause = false;
			this.pauseTime = this.pauseTime+(System.currentTimeMillis()-this.pauseStart);
			OkLogger.info(this.pauseTime);
		}
		else
		{
			this.pause = b;
			this.pauseStart = System.currentTimeMillis();
		}
	}
}
