package pl.edu.uj.okulo.experiment;

import java.util.ArrayList;

import javax.swing.JPanel;

import pl.edu.uj.okulo.log.OkLogger;

public class ExperimentDrawThread extends Thread {

	private JPanel canvas;
	private ArrayList<ExecutorAction> allEvents;
	
	public ExperimentDrawThread(JPanel pane, ArrayList<ExecutorAction> events)
	{
		this.canvas = pane;
		this.allEvents = events;
	}
	
	
	public void run()
	{
		int start = 1000;
		for(ExecutorAction a : allEvents)
		{
			OkLogger.info("Czas: "+a.getTime());
			try {
				Thread.sleep(start+a.getTime());
			} catch (InterruptedException e) {
				OkLogger.error(e.getMessage());
			}
			if(start==1000)
				start = 0;
			a.getAction().execute(canvas.getGraphics());
		}
	}
	
}
