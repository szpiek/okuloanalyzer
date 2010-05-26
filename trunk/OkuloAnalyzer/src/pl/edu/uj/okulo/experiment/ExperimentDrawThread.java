package pl.edu.uj.okulo.experiment;

import java.util.ArrayList;

import javax.swing.JPanel;

import pl.edu.uj.okulo.log.OkLogger;

public class ExperimentDrawThread extends Thread {

	private JPanel canvas;
	private ArrayList<ExecutorAction> allEvents;
	private boolean pause = false;
	private long startPause = 0;
	private long stopPause = 0;
	private ThreadListener tListener;
	
	public ExperimentDrawThread(JPanel pane, ArrayList<ExecutorAction> events)
	{
		this.canvas = pane;
		this.allEvents = events;
	}
	
	public void addThreadListener(ThreadListener l)
	{
		tListener = l;
	}
	
	public void setPause(boolean p)
	{
		this.pause = p;
		if(p)
			startPause = System.currentTimeMillis();
	}
	
	public boolean isPaused()
	{
		return this.pause;
	}
	
	public void run()
	{
		int start = 1000;
		int timePassed = 0;
		for(ExecutorAction a : allEvents)
		{
			try {
				Thread.sleep(start+a.getTime()-timePassed);
			} catch (InterruptedException e) {
				OkLogger.error(e.getMessage());
			}
			if(this.pause)
			{
				try {
					stopPause = System.currentTimeMillis();
					synchronized(this)
					{
						this.wait();
					}
					sleep(stopPause - startPause);
				} catch (InterruptedException e) {
					OkLogger.error(e.getMessage());
				}
			}
			if(start==1000)
				start = 0;
			timePassed += a.getTime()-timePassed;
			OkLogger.info("Time::: "+a.getTime());
			OkLogger.info("Time::: "+timePassed);
			a.getAction().execute(canvas.getGraphics());
		}
		if(tListener!=null)
		{
			tListener.stopThread();
		}
	}

	public synchronized void awake() {
		this.notifyAll();
	}
	
}
