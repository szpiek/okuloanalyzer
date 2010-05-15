package pl.edu.uj.okulo.windows.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.edu.uj.okulo.engine.Configuration;
import pl.edu.uj.okulo.engine.Utilities;
import pl.edu.uj.okulo.windows.ColorChooserFrame;
import pl.edu.uj.okulo.windows.ConfigurationFrame;

public class ConfigurationPanel extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private final String SAVE_ACTION = "saveConfiguration";
	private final String CANCEL_ACTION = "cancelConfiguration";	
	
	private JLabel info, backColor, impColor, impSize;
	private JLabel backColorBack, impColorBack;
	private ColorChooserFrame back, imp;
	private JTextField sizeIn;
	private ControlPanel control;
	private PreviewPanel preview;
	private ConfigurationFrame parent;

	public ConfigurationPanel(ConfigurationFrame p)
	{
		super(new GridBagLayout());
		parent = p;
		info = new JLabel("Konfiguracja pola eksperymentu.");
		backColor = new JLabel("                       Kolor tła:");
		impColor = new JLabel("               Kolor bodźca:");
		impColor.setPreferredSize(new Dimension(10,100));
		impSize = new JLabel("Średnica bodźca w px:");
		preview = new PreviewPanel(Configuration.getConfiguration().getBackgroundColor(),
				Configuration.getConfiguration().getImpulseColor(),
				Configuration.getConfiguration().getImpSize());
		
		
		Dimension d;
		
		preview.setOpaque(true);

		
		backColorBack = new JLabel(" ");
		d = backColorBack.getPreferredSize();
		d.setSize(40, d.getHeight()+15);
		backColorBack.setPreferredSize(d);
		backColorBack.setOpaque(true);
		backColorBack.setBackground(Configuration.getConfiguration().getBackgroundColor());
		backColorBack.addMouseListener(this);
		
		impColorBack = new JLabel("                  ");
		impColorBack.setOpaque(true);
		impColorBack.setBackground(Configuration.getConfiguration().getImpulseColor());
		impColorBack.addMouseListener(this);
		
		sizeIn = new JTextField();
		sizeIn.setText(Configuration.getConfiguration().getImpSize()+"");
		d = sizeIn.getPreferredSize();
		d.setSize(40, d.getHeight());
		sizeIn.setPreferredSize(d);
		sizeIn.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent k) {
				if((k.getKeyChar()<48 || k.getKeyChar()>57) && k.getKeyChar()!=8 && k.getKeyChar()!=127)
				{
					k.consume();
				}
				else
				{
					String number = sizeIn.getText()+k.getKeyChar();
					if(Utilities.isInt(number))
					{
						preview.setImpSize(Integer.parseInt(number));
						preview.repaint();
					}
				}
			}
			
		});
		
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(3, 5, 10, 5);
		c.gridwidth = 4;
		add(info, c);
		c.insets = new Insets(3, 5, 3, 5);
		c.gridwidth = 1;
		c.gridy = 1;
		add(impColor, c);
		c.gridx = 1;
		c.ipady = 2;
		add(impColorBack, c);
		c.gridy = 2;
		add(backColorBack, c);
		c.ipady = 0;
		c.gridx = 0;
		add(backColor, c);
		c.gridy = 3;
		add(impSize, c);
		c.gridx = 1;
		add(sizeIn, c);
		control = new ControlPanel(this);
		control.setAddAction(SAVE_ACTION);
		control.setCancelAction(CANCEL_ACTION);
		control.setAddText("Zapisz");
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 4;
		c.insets = new Insets(10, 5, 3, 5);
		add(control, c);
		
		c.insets = new Insets(0, 5, 0, 0);		
		c.gridx = 2;
		c.gridy = 0;
		c.gridheight = 5;
		c.ipadx = 140;
		c.ipady = 40;
		add(preview, c);
		
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if(back!=null)
		{
			if(a.getActionCommand().equals(MainPanel.ADD_ACTION))
			{
				this.backColorBack.setBackground(back.getSelectedColor());
				this.preview.setBackgroundColor(back.getSelectedColor());
				this.preview.repaint();
			}
			back.setVisible(false);
			back = null;
		}
		if(imp!=null)
		{
			if(a.getActionCommand().equals(MainPanel.ADD_ACTION))
			{
				this.impColorBack.setBackground(imp.getSelectedColor());
				this.preview.setImpulse(imp.getSelectedColor());
				this.preview.repaint();
			}
			imp.setVisible(false);
			imp = null;			
		}
		if(a.getActionCommand().equals(SAVE_ACTION))
		{
			if(!Utilities.isInt(this.sizeIn.getText().trim()) 
					|| Integer.parseInt(this.sizeIn.getText().trim())<=0)
			{
				Utilities.showErrorInformation(parent, "Wielkość bodźca musi być dodatnią liczbą całkowitą.", "Błąd");
				return;
			}
			Configuration.getConfiguration().setImpSize(this.sizeIn.getText().trim());
			Configuration.getConfiguration().setBackgroundColor(this.backColorBack.getBackground());
			Configuration.getConfiguration().setImpulseColor(this.impColorBack.getBackground());
			try {
				Configuration.getConfiguration().save();
			} catch (FileNotFoundException e) {
				Utilities.showErrorInformation(parent, "Błąd podczas zapisywania konfiguracji! "+e.getMessage(), "Błąd");
			} catch (IOException e) {
				Utilities.showErrorInformation(parent, "Błąd podczas zapisywania konfiguracji! "+e.getMessage(), "Błąd");
			}
			this.parent.setVisible(false);
		}
		else if(a.getActionCommand().equals(CANCEL_ACTION))
		{
			this.parent.setVisible(false);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getComponent().equals(this.backColorBack))
		{
			if(back!=null)
			{
				back.toFront();
			}
			else
			{
				back = new ColorChooserFrame(this, Configuration.getConfiguration().getBackgroundColor());
				back.setVisible(true);
			}
		}
		else if(e.getComponent().equals(this.impColorBack))
		{
			if(imp!=null)
			{
				imp.toFront();
			}
			else
			{
				imp = new ColorChooserFrame(this, Configuration.getConfiguration().getImpulseColor());
				imp.setVisible(true);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
}
