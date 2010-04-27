package pl.edu.uj.okulo.log;

import org.apache.log4j.Logger;

public class OkLogger {

	private static Logger log = Logger.getLogger("pl.edu.uj.okulo.log.Log4jDemo");
	
	public static Logger getLogger()
	{
		return log;
	}
	
	public static void info(Object message)
	{
		OkLogger.log.info(message);
	}
	
	public static void debug(Object message)
	{
		OkLogger.log.debug(message);
	}

	public static void warn(Object message) {
		OkLogger.log.warn(message);
	}

	public static void error(Object message) {
		OkLogger.log.error(message);
	}
}
