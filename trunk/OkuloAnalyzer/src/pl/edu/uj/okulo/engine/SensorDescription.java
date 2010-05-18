package pl.edu.uj.okulo.engine;

import pl.edu.uj.okulo.log.OkLogger;

public class SensorDescription {

	public static final String SENSOR_CONFIG_SEPARATOR = ":";
	public static final short LEFT = 0;
	public static final short RIGHT = 1;
	
	private String name = "";
	private short vendorId = 0;
	private short productId = 0;
	private int dpi = 0;
	private short position;
	
	public SensorDescription(String config)
	{
		String[] s = config.split(SensorDescription.SENSOR_CONFIG_SEPARATOR);
		if(s.length<4)
		{
			OkLogger.warn("Wrong sensor's description ["+config+"]");
			return;
		}
		name = s[0];
		vendorId = Short.parseShort(s[1]);
		productId = Short.parseShort(s[2]);
		dpi = Integer.parseInt(s[3]);
	}
	
	public SensorDescription(String name, short vendorId, short productId, int dpi)
	{
		this.name = name;
		this.vendorId = vendorId;
		this.productId = productId;
		this.dpi = dpi;
	}
	
	public short getVendor()
	{
		return this.vendorId;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public short getProduct()
	{
		return this.productId;
	}
	
	public String toString()
	{
		return "Name: "+name+", vendorId: "+vendorId+", productId: "+productId;
	}

	public Integer getDpi() {
		return dpi;
	}
	
	public short getPosition()
	{
		return this.position;
	}
}	