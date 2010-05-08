package pl.edu.uj.okulo.windows;

import java.awt.Point;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pl.edu.uj.okulo.engine.Configuration;
import pl.edu.uj.okulo.log.OkLogger;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 350;
	private int height = 320;

	private static int left = 100;
	private static int top = 100;
	
	public MainFrame()
	{
		super("OkuloAnalyzer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        
        try {
        	Configuration.getConfiguration().loadConfiguration();
        } catch (FileNotFoundException e) {
        	OkLogger.info(e.getMessage());
        	JOptionPane.showMessageDialog(this, "Nie znaleziono pliku konfiguracyjnego "+Configuration.CONFIG_FILENAME+" ["+e.getMessage()+"]",
        			"Błąd", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        	OkLogger.info(e.getMessage());
        	JOptionPane.showMessageDialog(this, "Błąd podczas otwierania pliku konfiguracyjnego "+Configuration.CONFIG_FILENAME+" ["+e.getMessage()+"]",
        			"Błąd", JOptionPane.ERROR_MESSAGE);
        }        
        MainPanel pane = new MainPanel(this);
        pane.setOpaque(true);
        this.setContentPane(pane);
        
        
        //show main frame near left upper corner of screen
        this.setLocation(new Point(left, top));      
        this.setSize(width, height);
        this.setResizable(false);
	}
	
	public static Point getLocationPoint()
	{
		return new Point(left,top);
	}
}
