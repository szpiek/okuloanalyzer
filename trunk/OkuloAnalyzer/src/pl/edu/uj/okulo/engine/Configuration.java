package pl.edu.uj.okulo.engine;

import java.awt.Color;
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
	private Color colorImpuls = null;
	private Color colorBackground = null;
	public final static String CONFIG_FILENAME = "config.ini";
	public final static String SENSOR_SEPARATOR = ";";
	
	private final String SENSORS_CONFIG_KEY = "sensors";
	private final String MWIDTH_CONFIG_KEY = "mWidth";
	private final String DISTANCE_CONFIG_KEY = "mDistance";
	private final String IMPCOLOR_CONFIG_KEY = "impColor";
	private final String BACKCOLOR_CONFIG_KEY = "backColor";
	private final String IMPSIZE_CONFIG_KEY = "impSize";
	
	private Properties configFile = null;
	private final Color DEFAULT_COLOR_IMPULS = Color.black;
	private final Color DEFAULT_COLOR_BACKGROUND = Color.white;
	
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

	public void save() throws FileNotFoundException, IOException {
		String sensorsValue = "";
		for(String key: allSensors.keySet())
		{
			sensorsValue += key+SensorDescription.SENSOR_CONFIG_SEPARATOR
			+allSensors.get(key).getVendor()+SensorDescription.SENSOR_CONFIG_SEPARATOR+allSensors.get(key).getProduct()
			+SensorDescription.SENSOR_CONFIG_SEPARATOR+allSensors.get(key).getDpi()+Configuration.SENSOR_SEPARATOR;
		}
		configFile.put(SENSORS_CONFIG_KEY, sensorsValue);
		FileOutputStream f = new FileOutputStream(new File(Configuration.CONFIG_FILENAME));
		configFile.store(f, "Saving config file");
	}
	
	private Properties loadConfigFile() throws FileNotFoundException, IOException
	{
		File f = new File(Configuration.CONFIG_FILENAME);
		configFile = new Properties();
		configFile.load(new FileInputStream(f));
		if(configFile.isEmpty())
			throw new IllegalArgumentException("Config file is empty!");
		if(configFile.containsKey(IMPCOLOR_CONFIG_KEY))
		{
			this.colorImpuls = new Color(Integer.parseInt((String)configFile.get(IMPCOLOR_CONFIG_KEY)));
		}
		if(configFile.containsKey(BACKCOLOR_CONFIG_KEY))
		{
			this.colorImpuls = new Color(Integer.parseInt((String)configFile.get(BACKCOLOR_CONFIG_KEY)));
		}
		return configFile;
	}

	public double getWidth() {
		if(configFile.containsKey(MWIDTH_CONFIG_KEY) && Utilities.isDouble(configFile.getProperty(MWIDTH_CONFIG_KEY)))
			return Double.parseDouble((String)configFile.get(MWIDTH_CONFIG_KEY));
		return 0;
	}

	public int getDistance() {
		if(configFile.containsKey(DISTANCE_CONFIG_KEY) && Utilities.isInt(configFile.getProperty(DISTANCE_CONFIG_KEY)))
			return Integer.parseInt((String)configFile.get(DISTANCE_CONFIG_KEY));
		return 0;
	}

	public void setWidth(double value) {
		configFile.put(MWIDTH_CONFIG_KEY, value+"");
	}

	public void setDistance(int value) {
		configFile.put(DISTANCE_CONFIG_KEY, value+"");
	}
	
	public Color getImpulseColor()
	{
		if(colorImpuls==null)
			return DEFAULT_COLOR_IMPULS;
		return colorImpuls;
	}
	
	public Color getBackgroundColor()
	{
		if(colorBackground==null)
			return DEFAULT_COLOR_BACKGROUND;
		return colorBackground;
	}
	
	public Integer getImpSize()
	{
		if(!configFile.containsKey(IMPSIZE_CONFIG_KEY))
			return 3;
		return Integer.parseInt(configFile.getProperty(IMPSIZE_CONFIG_KEY));
	}
	
	public void setImpSize(String value)
	{
		configFile.setProperty(IMPSIZE_CONFIG_KEY, value);
	}
	
	public void setImpulseColor(Color c)
	{
		configFile.setProperty(IMPCOLOR_CONFIG_KEY, c.getRGB()+"");
		this.colorImpuls = c;
	}
	
	public void setBackgroundColor(Color c)
	{
		configFile.setProperty(BACKCOLOR_CONFIG_KEY, c.getRGB()+"");
		this.colorBackground = c;
	}
	
}
