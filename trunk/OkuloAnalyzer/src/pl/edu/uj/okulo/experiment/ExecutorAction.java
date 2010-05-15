package pl.edu.uj.okulo.experiment;

public class ExecutorAction {
	
	private Integer time;
	private Executor action;
	
	public ExecutorAction(Integer time, Executor a)
	{
		this.time = time;
		this.action = a;
	}
	
	public Executor getAction()
	{
		return this.action;
	}
	
	public Integer getTime()
	{
		return this.time;
	}
}
