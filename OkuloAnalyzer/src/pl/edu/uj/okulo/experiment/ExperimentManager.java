package pl.edu.uj.okulo.experiment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import pl.edu.uj.okulo.log.OkLogger;

public class ExperimentManager {

	private File experimentFile;
	private Properties all;
	private static ExperimentManager singleton;
	private HashMap<String, ArrayList<ExecutorEvent>> timeLines = new HashMap<String, ArrayList<ExecutorEvent>>();
	private ArrayList<ExecutorAction> allEvents = new ArrayList<ExecutorAction>();
	
	private static final String NAME_KEY = "name";
	private static final String DESCRIPTION_KEY = "description";
	private static final String DEFINITION_KEY = "definition";
	private ExperimentManager(){};
	
	public static ExperimentManager getManager()
	{
		if(singleton==null)
			singleton = new ExperimentManager();
		return singleton;
	}
	
	public void setExpFile(File f)
	{
		this.experimentFile = f;
	}

	public void saveExpFile(String name, String description, String definition) throws FileNotFoundException, IOException {
		all = new Properties();
		all.put(ExperimentManager.NAME_KEY, name);
		all.put(ExperimentManager.DESCRIPTION_KEY, description);
		all.put(ExperimentManager.DEFINITION_KEY, definition);
		all.store(new FileOutputStream(experimentFile), "Saving: "+new Date());
	}

	public void readFile() throws FileNotFoundException, IOException {
		all = new Properties();
		all.load(new FileInputStream(experimentFile));
		if(!all.containsKey(NAME_KEY) || !all.containsKey(DESCRIPTION_KEY) || !all.containsKey(DEFINITION_KEY))
			throw new IOException("Niepoprawny pliK! Nie znaleziono wszystkich wymaganych informacji!");
	}

	public String getExpName() {
		return all.getProperty(NAME_KEY);
	}

	public String getExpDescription() {
		return all.getProperty(DESCRIPTION_KEY);
	}

	public String getExpDefinition() {
		return all.getProperty(DEFINITION_KEY);
	}
	
	public void prepareTimeLine(String definition)
	{
		String[] allSteps = definition.split(";");
		// zaczynamy od 0 milisekund
		int start = 0;
		timeLines = new HashMap<String, ArrayList<ExecutorEvent>>();
		for(int i=0;i<allSteps.length;i++)
		{
			String[] step = allSteps[i].split(",");
			if(!timeLines.containsKey(step[0].trim()))
				timeLines.put(step[0].trim(), new ArrayList<ExecutorEvent>());
			ArrayList<ExecutorEvent> tline = timeLines.get(step[0].trim());
			
			// dodajemy zdarzenie poczatkowe (pokazanie bodzca)
			tline.add(new ExecutorEvent(start, Executor.DRAW));
			int stop = Integer.parseInt(step[1].trim());
			
			// dodajemy zdarzenie koncowe (usuniecie bodzca)
			tline.add(new ExecutorEvent(start+stop, Executor.CLEAN));
			timeLines.put(step[0].trim(), tline);
			int gap = Integer.parseInt(step[2].trim());
			
			// obliczamy poczatek kolejnego bodzca
			start = start + stop + gap;
		}
		HashMap<Integer, Executor> tempEvents = new HashMap<Integer, Executor>();
		ArrayList<Integer> allKeys = new ArrayList<Integer>();
		for(String s : timeLines.keySet())
		{
			for(ExecutorEvent i : timeLines.get(s))
			{
				Executor e = new ExecutorImpl(Integer.parseInt(s), i.getAction());
				if(tempEvents.containsKey(i.getTime()))
					tempEvents.get(i.getTime()).addExecutor(e);
				else
					tempEvents.put(i.getTime(), e);
				if(!allKeys.contains(i.getTime()))
					allKeys.add(i.getTime());
			}
		}
		allEvents = new ArrayList<ExecutorAction>();
		Collections.sort(allKeys);
		for(Integer key: allKeys)
		{
			OkLogger.info(key);
			allEvents.add(new ExecutorAction(key, tempEvents.get(key)));
		}
	}
	
	public ArrayList<ExecutorAction> getEvents()
	{
		return this.allEvents;
	}
}
