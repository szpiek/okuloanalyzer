package pl.edu.uj.okulo.experiment;

import java.awt.Graphics;

import pl.edu.uj.okulo.engine.Configuration;
import pl.edu.uj.okulo.log.OkLogger;

public class ExecutorImpl implements Executor {

	private Executor exec;
	private Integer id;
	private Short action;
	
	public ExecutorImpl(Integer id, Short action)
	{
		this.id = id;
		this.action = action;
	}
	
	@Override
	public void execute(Graphics g) {
		if(action.equals(DRAW))
		{
			g.setColor(Configuration.getConfiguration().getImpulseColor());
			g.fillOval(Configuration.getConfiguration().getWidthPosition(id), 
					Configuration.getConfiguration().getHeightPosition(),
					Configuration.getConfiguration().getImpSize(), 
					Configuration.getConfiguration().getImpSize());
//			OkLogger.info("Rysuje: "+id);
		}
		else if(action.equals(CLEAN))
		{
			g.setColor(Configuration.getConfiguration().getBackgroundColor());
			g.fillOval(Configuration.getConfiguration().getWidthPosition(id), 
					Configuration.getConfiguration().getHeightPosition(),
					Configuration.getConfiguration().getImpSize(), 
					Configuration.getConfiguration().getImpSize());			
//			OkLogger.info("Zmazuje: "+id);
		}
		if(exec!=null)
			exec.execute(g);
		else
			g.dispose();
	}

	@Override
	public void addExecutor(Executor e) {
		if(exec==null)
			this.exec = e;
		else
			this.exec.addExecutor(e);
	}

}
