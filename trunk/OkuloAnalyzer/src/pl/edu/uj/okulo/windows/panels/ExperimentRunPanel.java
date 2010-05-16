package pl.edu.uj.okulo.windows.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pl.edu.uj.okulo.engine.Configuration;
import pl.edu.uj.okulo.experiment.ExperimentDrawThread;
import pl.edu.uj.okulo.experiment.ExperimentManager;
import pl.edu.uj.okulo.experiment.ThreadListener;
import pl.edu.uj.okulo.log.OkLogger;
import pl.edu.uj.okulo.windows.ExperimentRunFrame;

public class ExperimentRunPanel extends JPanel implements ActionListener, ThreadListener {

	private static final long serialVersionUID = 1L;
	private JButton play, pause, stop, save;
	private ExperimentRunFrame parent;
	private final String SAVE_ACTION = "save", STOP_ACTION = "stop", PLAY_ACTION = "play", PAUSE_ACTION = "pause";
	private File targetDir = null;
	private JPanel experimentPanel;
	private ExperimentDrawThread experimentThread;
	
	public ExperimentRunPanel(ExperimentRunFrame p)
	{
		super(new GridBagLayout());
		parent = p;
		ImageIcon playIcon = new ImageIcon("play.gif");
		ImageIcon pauseIcon = new ImageIcon("pause.gif");
		ImageIcon stopIcon = new ImageIcon("stop.gif");
		JPanel topPane = new JPanel(new GridBagLayout());
		JLabel info = new JLabel("Panel kontrolny: ");
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		play = new JButton(playIcon);
		pause = new JButton(pauseIcon);
		stop = new JButton(stopIcon);
		save = new JButton("Zapisz");
		
		save.setActionCommand(SAVE_ACTION);
		play.setActionCommand(PLAY_ACTION);
		stop.setActionCommand(STOP_ACTION);
		pause.setActionCommand(PAUSE_ACTION);
		
		save.addActionListener(this);
		play.addActionListener(this);
		stop.addActionListener(this);
		pause.addActionListener(this);
		
		setAllEnabled(false);
		topPane.add(info, c);
		c.gridx = 1;
		topPane.add(play, c);
		c.gridx = 2;
		topPane.add(pause, c);
		c.gridx = 3;
		topPane.add(stop, c);
		c.gridx = 4;
		c.ipady = 5;
		topPane.add(save, c);
		c.gridx= 0;
		c.gridy= 0;
		add(topPane,c);
		c.insets = new Insets(0, 0, 0, 0);
		experimentPanel = new JPanel();
		experimentPanel.setBackground(Configuration.getConfiguration().getBackgroundColor());
		c.ipadx = parent.getWidth();
		c.ipady = parent.getHeight()-100;
		c.gridx=0;
		c.gridy = 1;
		add(experimentPanel,c);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getActionCommand().equals(SAVE_ACTION))
		{
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setDialogTitle("Katalog docelowy wyników eksperymentu");
			int val = fc.showSaveDialog(this);	
			if(val==JFileChooser.APPROVE_OPTION)
			{
				this.targetDir = fc.getSelectedFile();
			}
		}
		else if(a.getActionCommand().equals(PLAY_ACTION))
		{
			if(experimentThread!=null && experimentThread.isPaused())
			{
				experimentThread.setPause(false);
				experimentThread.awake();
				pause.setEnabled(true);
			}
			else
			{
				if(this.targetDir==null)
				{
					int ret = JOptionPane.showConfirmDialog(this, "Nie wybrano miejsca zapisu wyników eksperymentu. Wyniki nie zostaną zapisane! Czy chcesz kontynuować?",
							"Nie wybrano miejsca zapisu wyników eksperymentu", JOptionPane.YES_NO_OPTION);
					OkLogger.info(ret);
					if(ret==0)
					{
						showExperiment();
						setAllEnabled(true);
					}
				}
				else
				{
					showExperiment();
					setAllEnabled(true);
				}
			}
		}
		else if(a.getActionCommand().equals(STOP_ACTION))
		{
			stopExperiment();
			setAllEnabled(false);
		}
		else if(a.getActionCommand().equals(PAUSE_ACTION))
		{
			if(this.experimentThread!=null)
			{
				pause.setEnabled(false);
				experimentThread.setPause(true);
			}
		}
	}
	
	private void showExperiment() {
		Configuration.getConfiguration().setFrameHeight(experimentPanel.getHeight());
		Configuration.getConfiguration().setFrameWidth(experimentPanel.getWidth());
		experimentThread = new ExperimentDrawThread(experimentPanel, ExperimentManager.getManager().getEvents());
		experimentThread.addThreadListener(this);
		experimentThread.start();
	}
	
	private void stopExperiment()
	{
		if(experimentThread!=null)
			experimentThread.interrupt();
	}

	private void setAllEnabled(boolean b)
	{
		pause.setEnabled(b);
		stop.setEnabled(b);
	}

	@Override
	public void stopThread() {
		setAllEnabled(false);
	}

}
