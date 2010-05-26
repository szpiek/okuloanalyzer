package pl.edu.uj.okulo.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import pl.edu.uj.okulo.windows.panels.StatusPanel;


public class Engine {

	private static Engine singleton = null;
	private final int sensorsAmount = 1;
	private ArrayList<UsbInterface> sensors = new ArrayList<UsbInterface>();
	private SensorStatusThread sensor1, sensor2;
	private FileOutputStream expFileLeft, expFileRight;
	private SensorDescription currentSensor;	
	private boolean[] impulses = new boolean[Configuration.maxImpulses];
	
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
		{
			currentSensor = sd;
			return true;
		}
		return false;
	}
	
	public void runTempSensors(StatusPanel ... statuses)
	{
		sensor1 = new SensorStatusThread(sensors.get(0),statuses[0], statuses[1], getTempDataFile());
		if(statuses.length>2)
		{
			sensor2 = new SensorStatusThread(sensors.get(1),statuses[2],statuses[3], getTempDataFile());
			sensor2.start();
		}
		sensor1.start();
	}
	
	public SensorDescription getCurrentSensor()
	{
		return this.currentSensor;
	}
	
	public void prepareExpFiles(File targetDir)
	{
		File f_left = new File(getExpFileName(targetDir, "left"));
		File f_right = new File(getExpFileName(targetDir, "right"));
		try {
			expFileLeft = new FileOutputStream(f_left);
			expFileRight = new FileOutputStream(f_right);
		} catch (FileNotFoundException e) {
			OkLogger.error(e.getMessage());
		}
		try {
			putInfoInFiles(expFileLeft);
			putInfoInFiles(expFileRight);
		} catch (IOException e) {
			OkLogger.error(e.getMessage());
		}
	}
	
	private void putInfoInFiles(FileOutputStream f) throws IOException
	{
		f.write(("sensorName="+getCurrentSensor().getName()+System.getProperty("line.separator")).getBytes());
		f.write(("sensorDPI="+getCurrentSensor().getDpi()+System.getProperty("line.separator")).getBytes());
		f.write(("sensorPosition="+(getCurrentSensor().getPosition()==SensorDescription.LEFT?"left":"right")+System.getProperty("line.separator")).getBytes());
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
			currentSensor = null;
		}
	}
	
	public File getTargetDir()
	{
		return this.getTargetDir();
	}
	
	public void setImpulse(Integer id, boolean imp)
	{
		this.impulses[id-1] = imp;
	}
	
	public String getImpulses()
	{
		String impRep = "";
		boolean isFilled = false;
		for(int i=0;i<impulses.length;i++)
		{
			if(impulses[i])
			{
				isFilled = true;
				impRep += (i+1);
			}
		}
		if(!isFilled)
			impRep = "x";
		return impRep;
	}
	
	public ArrayList<UsbInterface> getSensors()
	{
		return this.sensors;
	}
	
	public String getExpFileName(File targetDir, String position)
	{
		return targetDir.getAbsolutePath()+File.separator+"sensor_"+position+"_"
		+new SimpleDateFormat("yyyyMMdd_HH.mm").format(new Date())+".dat";		
	}

	public void startExperiment() {
		sensor1.setOutputFileStream(expFileLeft);
		sensor1.setExperiment(true);
		sensor1.setExpStart(System.currentTimeMillis());
	}
	
	public void stopExperiment()
	{
		sensor1.setOutputFile(getTempDataFile());
		sensor1.setExperiment(false);
	}

	public void pauseExperiment(boolean b) {
		sensor1.pauseExperiment(b);
	}
}
