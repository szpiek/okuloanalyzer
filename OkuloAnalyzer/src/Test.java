import javax.usb.UsbException;

import pl.edu.uj.okulo.usb.FindSensors;
import pl.edu.uj.okulo.windows.MainFrame;


public class Test {

	/**
	 * @param args
	 * @throws UsbException 
	 * @throws SecurityException 
	 */

	
	public static void main(String[] args) throws SecurityException, UsbException {	
		MainFrame f = new MainFrame();
		f.setVisible(true);
//		FindSensors.getSensors();
	}

}
