package pl.edu.uj.okulo.engine;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.usb.UsbConst;
import javax.usb.UsbEndpoint;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;

import pl.edu.uj.okulo.log.OkLogger;

public class Utilities {

	public static boolean isInt(String number)
	{
		try
		{
			Integer.parseInt(number);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	
	public static boolean isDouble(String number)
	{
		try
		{
			Double.parseDouble(number);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}	
	
	public static void showErrorInformation(JFrame parent, String message, String title)
	{
		JOptionPane.showMessageDialog(parent, message, 
				title, JOptionPane.ERROR_MESSAGE);		
	}
	
	public static UsbPipe getPipeForUsbInterface(UsbInterface usb)
	{
		List<?> endpoints = usb.getUsbEndpoints();
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
			return null;
		}
		return mainPoint.getUsbPipe();		
	}

	public static byte getByteValue(Byte val)
	{
		return (byte)val.intValue();
	}
}
