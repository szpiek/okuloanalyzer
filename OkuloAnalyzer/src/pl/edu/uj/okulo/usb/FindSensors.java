package pl.edu.uj.okulo.usb;

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import javax.usb.UsbInterfacePolicy;

import pl.edu.uj.okulo.log.OkLogger;

public class FindSensors {

	private static short sensorVendorId = 1133;//1203;
	private static short sensorProductId = -16362;//12556;
	private static final byte MOUSE_CLASS = 0x03;
	private static final byte MOUSE_SUBCLASS = 0x01;
	private static final byte MOUSE_PROTOCOL = 0x02;
	
	public static ArrayList<UsbInterface> getSensors(short vendorId, short productId) throws SecurityException, UsbException
	{
		sensorVendorId = vendorId;
		sensorProductId = productId;
		ArrayList<UsbInterface> sensors = getHids();
		// device can be claimed thanks to force claim
		UsbInterfacePolicy uiP = new UsbInterfacePolicy() {
			public boolean forceClaim(UsbInterface i) { return true; }
		};		
		OkLogger.info("Found "+sensors.size()+" sensors");
		for(UsbInterface u: sensors)
		{
			OkLogger.info("Claiming sensor");
			u.claim(uiP);
		}
		return sensors;
	}
	
	private static ArrayList<UsbInterface> getHids() throws SecurityException, UsbException
	{
		OkLogger.info("Starting searching for devices");
		return getHids(UsbHostManager.getUsbServices().getRootUsbHub());
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<UsbInterface> getHids(UsbDevice dev)
	{
		ArrayList<UsbInterface> sensors = new ArrayList<UsbInterface>();
		if (dev.isConfigured()) {
			List ifaces = dev.getActiveUsbConfiguration().getUsbInterfaces();

			for (int i=0; i<ifaces.size(); i++) {
				UsbInterface usbInterface = (UsbInterface)ifaces.get(i);
				UsbInterfaceDescriptor descriptor = usbInterface.getUsbInterfaceDescriptor();
				UsbDeviceDescriptor devDesc = usbInterface.getUsbConfiguration().getUsbDevice().getUsbDeviceDescriptor();
				OkLogger.debug("Device description: ProductId: "+devDesc.idProduct()+", VendorId: "+devDesc.idVendor()+
						" Subclass: "+descriptor.bInterfaceSubClass()+", Protocol: "+descriptor.bInterfaceProtocol());
				if (MOUSE_CLASS == usbInterface.getUsbInterfaceDescriptor().bInterfaceClass()
						&& descriptor.bInterfaceSubClass() == MOUSE_SUBCLASS
						&& descriptor.bInterfaceProtocol() == MOUSE_PROTOCOL
						&& devDesc.idProduct() == sensorProductId
						&& devDesc.idVendor() == sensorVendorId
						)
				{
					OkLogger.info("Found proper device! VendorId: "+devDesc.idVendor()+" ProductId:"+devDesc.idProduct());
					sensors.add(usbInterface);
				}
			}
		}

		if (dev.isUsbHub()) {
			List devices = ((UsbHub)dev).getAttachedUsbDevices();
			for (int i=0; i<devices.size(); i++)
			{
				OkLogger.debug("Found USB hub");
				sensors.addAll(getHids((UsbDevice)devices.get(i)));
			}
		}

		return sensors;		
	}
	
}
