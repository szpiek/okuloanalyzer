package pl.edu.uj.okulo.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import pl.edu.uj.okulo.log.OkLogger;

public class Configuration {

	private static Configuration singleton;
	private HashMap<String, SensorDescription> allSensors = new HashMap<String, SensorDescription>();
	public final static String CONFIG_FILENAME = "config.ini";
	public final static String SENSOR_SEPARATOR = ";";
	
	private final String SENSORS_CONFIG_KEY = "sensors";
	
	
	private Configuration(){};
	
	public static Configuration getConfiguration()
	{
		if(singleton==null)
		{
			singleton = new Configuration();
		}
		return singleton;
	}
	
	public void loadConfiguration() throws FileNotFoundException, IOException
	{
		loadAllSensors(this.loadConfigFile().getProperty(SENSORS_CONFIG_KEY));
	}
	
	public void loadAllSensors(String sensors)
	{
		String[] s = sensors.split(Configuration.SENSOR_SEPARATOR);
		for(String sensor: s)
		{
			if(sensor.length()<3)
				continue;
			SensorDescription sd = new SensorDescription(sensor);
			OkLogger.info(sd);
			allSensors.put(sd.getName(), sd);
		}
	}

	public String[] getSensorsNames() {
		return allSensors.keySet().toArray(new String[]{});
	}

	public SensorDescription getSensor(String name) {
		return this.allSensors.get(name);
	}

	public int removeSensor(String name) throws FileNotFoundException, IOException {
		if(allSensors.size()==1)
			return -1;
		allSensors.remove(name);
		save();
		return 0;
	}
	
	public void addSensor(SensorDescription sen) throws FileNotFoundException, IOException
	{
		allSensors.put(sen.getName(), sen);
		save();
	}

	private void save() throws FileNotFoundException, IOException {
		String sensorsValue = "";
		for(String key: allSensors.keySet())
		{
			sensorsValue += key+SensorDescription.SENSOR_CONFIG_SEPARATOR
			+allSensors.get(key).getVendor()+SensorDescription.SENSOR_CONFIG_SEPARATOR+allSensors.get(key).getProduct()
			+SensorDescription.SENSOR_CONFIG_SEPARATOR+allSensors.get(key).getDpi()+Configuration.SENSOR_SEPARATOR;
		}
		Properties config = loadConfigFile();
		config.put(SENSORS_CONFIG_KEY, sensorsValue);
		FileOutputStream f = new FileOutputStream(new File(Configuration.CONFIG_FILENAME));
		config.store(f, "Saving config file");
	}
	
	private Properties loadConfigFile() throws FileNotFoundException, IOException
	{
		File f = new File(Configuration.CONFIG_FILENAME);
		Properties configuration = new Properties();
		configuration.load(new FileInputStream(f));
		return configuration;
	}
	
}
