package pl.edu.uj.okulo.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import pl.edu.uj.okulo.log.OkLogger;

public class Configuration {

	private static Configuration singleton;
	private HashMap<String, SensorDescription> allSensors = new HashMap<String, SensorDescription>();
	public final static String CONFIG_FILENAME = "config.ini";
	
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
		File f = new File(Configuration.CONFIG_FILENAME);
		Properties configuration = new Properties();
		configuration.load(new FileInputStream(f));
		loadAllSensors(configuration.getProperty(SENSORS_CONFIG_KEY));
	}
	
	public void loadAllSensors(String sensors)
	{
		String[] s = sensors.split(";");
		for(String sensor: s)
		{
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
	
}
