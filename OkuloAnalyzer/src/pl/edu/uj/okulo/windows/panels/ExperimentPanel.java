package pl.edu.uj.okulo.windows.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pl.edu.uj.okulo.engine.Utilities;
import pl.edu.uj.okulo.experiment.ExperimentManager;
import pl.edu.uj.okulo.windows.ConfigurationFrame;
import pl.edu.uj.okulo.windows.ExperimentRunFrame;
import pl.edu.uj.okulo.windows.PreviewFrame;

public class ExperimentPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JButton newExp, preview, open, save, run, config;
	private JTextField nameText, descriptionText;
	private JTextArea definitionArea;
	private JFrame parent;
	private PreviewFrame previewFrame;
	private ConfigurationFrame configurationFrame;
	private ExperimentRunFrame expFrame;
	
	private final String NEW_ACTION = "newExp";
	private final String SAVE_ACTION = "save";	
	private final String OPEN_ACTION = "open";
	private final String PREVIEW_ACTION = "preview";
	private final String RUN_ACTION = "run";	
	private final String CONFIGURATION_ACTION = "config";
	
	public ExperimentPanel(JFrame p)
	{
		super(new GridBagLayout());
		parent = p;
		JLabel nameLabel = new JLabel("   Nazwa: ");
		JLabel description = new JLabel("       Opis: ");
		JLabel definition = new JLabel("Definicja eksperymentu: ");
		JLabel info = new JLabel("a,b,c; a-bodziec, b-czas trwania, c-czas do kolejnego bodźca");
		
		newExp = new JButton("Nowy");
		preview = new JButton("Podgląd");
		open = new JButton("Wczytaj");
		save = new JButton("Zapisz");
		run = new JButton("Uruchom");
		config = new JButton("Konfiguracja");
		
		newExp.setActionCommand(NEW_ACTION);
		preview.setActionCommand(PREVIEW_ACTION);
		open.setActionCommand(OPEN_ACTION);
		save.setActionCommand(SAVE_ACTION);
		run.setActionCommand(RUN_ACTION);
		config.setActionCommand(CONFIGURATION_ACTION);
		
		newExp.addActionListener(this);
		preview.addActionListener(this);
		open.addActionListener(this);
		save.addActionListener(this);
		run.addActionListener(this);
		config.addActionListener(this);
		
		nameText = new JTextField();
		descriptionText = new JTextField();
		definitionArea = new JTextArea();
		definitionArea.setWrapStyleWord(true);
		JScrollPane scrollpane = new JScrollPane(definitionArea);
		
		
		//		definitionArea.setRows(20);
		
		this.setEnabledAll(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 3, 5, 3);
		c.gridwidth = 1;
		add(newExp, c);
		c.gridx = 1;
		add(open, c);
		c.gridx = 2;
		add(config, c);
		c.gridx = 3;
		add(save, c);
		c.gridy = 1;
		c.gridx = 0;
		add(preview, c);
		c.gridx = 1;
		c.gridwidth = 3;
		add(run, c);
		c.gridwidth = 1;
		c.gridy = 2;
		c.gridx = 0;
		add(nameLabel, c);
		c.gridx = 1;
		c.gridwidth = 3;
		add(nameText, c);
		c.gridwidth = 1;
		c.gridy = 3;
		c.gridx = 0;
		add(description, c);
		c.gridx = 1;
		c.gridwidth = 3;
		add(descriptionText, c);
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 145, 5, 5);
		add(definition, c);
		c.gridy = 5;
		c.insets = new Insets(5, 3, 3, 3);
		add(info, c);
		c.gridy = 6;
		c.insets = new Insets(5, 3, 3, 3);
		c.ipady = 138;
		add(scrollpane, c);
//		add(nameLabel, c);
//		c.gridx = 1;
//		add(name, c);
	}


	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getActionCommand().equals(NEW_ACTION))
		{
			JFileChooser fc = new JFileChooser();
			int val = fc.showSaveDialog(this);
			if(val==JFileChooser.APPROVE_OPTION)
			{
				ExperimentManager.getManager().setExpFile(fc.getSelectedFile());
				setEnabledAll(true);
				this.nameText.setText("");
				this.descriptionText.setText("");
				this.definitionArea.setText("");
			}
		}
		else if(a.getActionCommand().equals(SAVE_ACTION))
		{
			if(checkAll())
			{
				boolean isOk = true;
				try {
					ExperimentManager.getManager().saveExpFile(nameText.getText().trim(), descriptionText.getText().trim(),
							definitionArea.getText().trim());
				} catch (FileNotFoundException e) {
					Utilities.showErrorInformation(parent, e.getMessage(), "Błąd");
					isOk = false;
				} catch (IOException e) {
					Utilities.showErrorInformation(parent, e.getMessage(), "Błąd");
					isOk = false;
				} 
				if(isOk)
					JOptionPane.showMessageDialog(parent, "Pomyślnie zapisano eksperyment.");
			}
		}
		else if(a.getActionCommand().equals(OPEN_ACTION))
		{
			JFileChooser fc = new JFileChooser();
			int val = fc.showOpenDialog(this);
			if(val==JFileChooser.APPROVE_OPTION)
			{
				ExperimentManager.getManager().setExpFile(fc.getSelectedFile());
				try {
					ExperimentManager.getManager().readFile();
				} catch (FileNotFoundException e) {
					Utilities.showErrorInformation(parent, e.getMessage(), "Błąd");
					return;
				} catch (IOException e) {
					Utilities.showErrorInformation(parent, e.getMessage(), "Błąd");
					return;
				}
				this.nameText.setText(ExperimentManager.getManager().getExpName());
				this.descriptionText.setText(ExperimentManager.getManager().getExpDescription());
				this.definitionArea.setText(ExperimentManager.getManager().getExpDefinition());
				setEnabledAll(true);
			}
		}
		else if(a.getActionCommand().equals(PREVIEW_ACTION))
		{
			ExperimentManager.getManager().prepareTimeLine(this.definitionArea.getText());
			if(previewFrame!=null)
			{
				previewFrame.setVisible(false);
			}
			previewFrame = null;
			previewFrame = new PreviewFrame();
			previewFrame.setVisible(true);
			previewFrame.startPreview();
		}
		else if(a.getActionCommand().equals(CONFIGURATION_ACTION))
		{
			this.showConfigurationWindow();
		}
		else if(a.getActionCommand().equals(RUN_ACTION))
		{
			ExperimentManager.getManager().prepareTimeLine(this.definitionArea.getText());
			this.runExperiment();
		}
	}
	
	private void runExperiment() {
		if(expFrame!=null && expFrame.isVisible())
			expFrame.setVisible(false);
		expFrame = new ExperimentRunFrame(this.nameText.getText().trim());
	    expFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);  //Maximizing the frame
	    expFrame.setExtendedState(JFrame.ICONIFIED | expFrame.getExtendedState());
	    expFrame.setVisible(true);
	    try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    expFrame.setResizable(false);
	    expFrame.setPaneSize();
	}


	private boolean checkAll() {
		if(nameText.getText().trim().isEmpty() || 
				descriptionText.getText().trim().isEmpty() || 
				definitionArea.getText().trim().isEmpty())
		{
			Utilities.showErrorInformation(parent, "Wszystkie pola muszą być wypełnione!", "Błąd");
			return false;
		}
		return true;
	}


	private void setEnabledAll(boolean enabled)
	{
		preview.setEnabled(enabled);
		save.setEnabled(enabled);		
		nameText.setEnabled(enabled);
		descriptionText.setEnabled(enabled);
		definitionArea.setEnabled(enabled);		
		run.setEnabled(enabled);
	}
	
	private void showConfigurationWindow()
	{
		if(configurationFrame!=null && configurationFrame.isVisible())
			configurationFrame.toFront();
		else
		{
			configurationFrame = new ConfigurationFrame();
			configurationFrame.setVisible(true);
		}
	}
}
