package pl.edu.uj.okulo.windows;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pl.edu.uj.okulo.engine.Configuration;
import pl.edu.uj.okulo.experiment.ExperimentDrawThread;
import pl.edu.uj.okulo.experiment.ExperimentManager;

public class PreviewFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int width = 475;
	public static final int height = 240;
	private JPanel mainP;
	
	public PreviewFrame()
	{
		super("Podgląd eksperymentu");
		mainP = new JPanel();
		setResizable(false);
		setLocation(MainFrame.getLocationPoint());
		setSize(width, height);
		Dimension d = mainP.getPreferredSize();
		d.setSize(width, height);
		mainP.setPreferredSize(d);
		Container content = getContentPane();
		content.add(mainP, "Center");
		mainP.setBackground(Configuration.getConfiguration().getBackgroundColor());
	}
	
	public void startPreview()
	{
		Configuration.getConfiguration().setFrameHeight(height);
		Configuration.getConfiguration().setFrameWidth(width);
		ExperimentDrawThread thread = new ExperimentDrawThread(mainP, ExperimentManager.getManager().getEvents());
		thread.start();
		
//		ArrayList<ExecutorAction> allEvents = ExperimentManager.getManager().getEvents();
//		for(ExecutorAction a : allEvents)
//		{
//			this.mainP.setCurrentEvent(a.getAction());
//			this.mainP.repaint();
////			System.out.println("TIT! "+a.getTime());
////			try {
////				Thread.sleep(a.getTime());
////			} catch (InterruptedException e) {
////				Utilities.showErrorInformation(this, "Błąd podczas uruchamiania podglądu eksperymentu ["+e.getMessage()+"]", "Błąd");
////			}
//		}
	}
}
