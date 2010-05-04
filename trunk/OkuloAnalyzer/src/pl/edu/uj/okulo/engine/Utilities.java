package pl.edu.uj.okulo.engine;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
}
