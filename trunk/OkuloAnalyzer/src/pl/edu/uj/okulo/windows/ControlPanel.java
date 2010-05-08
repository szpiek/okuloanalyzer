package pl.edu.uj.okulo.windows;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton add, cancel;
	
	public ControlPanel(ActionListener listener)
	{
		super(new GridBagLayout());
		add = new JButton("Dodaj");
		cancel = new JButton("Anuluj");
		GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3,5,3,5);
        add.setActionCommand(MainPanel.ADD_ACTION);
        cancel.setActionCommand(MainPanel.CANCEL_ACTION);
        add.addActionListener(listener);
        cancel.addActionListener(listener);
        add(add,c);
        c.gridx = 1;
        add(cancel, c);
	}
	
	public void setAddText(String text)
	{
		this.add.setText(text);
	}
	
	public JButton getAddButton()
	{
		return this.add;
	}
	
	public void setAddAction(String action)
	{
		this.add.setActionCommand(action);
	}
	
	public void setCancelAction(String action)
	{
		this.cancel.setActionCommand(action);
	}
}
