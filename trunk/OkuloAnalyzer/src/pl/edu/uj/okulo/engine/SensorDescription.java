package pl.edu.uj.okulo.engine;

import pl.edu.uj.okulo.log.OkLogger;

public class SensorDescription {

	private String name = "";
	private short vendorId = 0;
	private short productId = 0;
	
	public SensorDescription(String config)
	{
		String[] s = config.split(":");
		if(s.length<3)
		{
			OkLogger.warn("Wrong sensor's description ["+config+"]");
			return;
		}
		name = s[0];
		vendorId = Short.parseShort(s[1]);
		productId = Short.parseShort(s[2]);
	}
	
	public SensorDescription(String name, short vendorId, short productId)
	{
		this.name = name;
		this.vendorId = vendorId;
		this.productId = productId;
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
}	