import javax.usb.UsbException;

import pl.edu.uj.okulo.windows.MainFrame;


public class Main {

	/**
	 * @param args
	 * @throws UsbException 
	 * @throws SecurityException 
	 */

	
	public static void main(String[] args) throws SecurityException, UsbException {	
		MainFrame f = new MainFrame();
		f.setVisible(true);
	}

}
