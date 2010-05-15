package pl.edu.uj.okulo.experiment;

public class ExecutorEvent {

	private Integer time;
	private Short action;
	
	public ExecutorEvent(Integer time, Short a)
	{
		this.time = time;
		this.action = a;
	}
	
	public Short getAction()
	{
		return this.action;
	}
	
	public Integer getTime()
	{
		return this.time;
	}
}
