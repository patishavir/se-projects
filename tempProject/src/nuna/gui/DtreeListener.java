package nuna.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import nuna.Dtree;

public class DtreeListener implements ActionListener {
	private enum DtreeEnum {
		LOAD, SEARCH, DELETE, ADDWORD
	}

	private DtreeJFrame dtreeJFrame = null;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public DtreeListener(final DtreeJFrame dtreeJFrame) {
		this.dtreeJFrame = dtreeJFrame;
	}

	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String actionCommand = button.getActionCommand();
		JTextField jTextField = dtreeJFrame.getTextFieldMap().get(actionCommand);
		String parameter = jTextField.getText();
		DtreeEnum buttonEnum = DtreeEnum.valueOf(actionCommand);
		String result = null;
		logger.finest(buttonEnum.toString() + " parameter: " + parameter + "...");
		switch (buttonEnum) {
		case LOAD:
			result = Dtree.load(parameter);
			break;
		case ADDWORD:
			result = Dtree.getD().add(parameter);
			break;	
		case SEARCH:
			result = Dtree.getD().searchWord(parameter);
			break;
		case DELETE:
			result = Dtree.getD().delWord(parameter);
			break;
		}
		JOptionPane.showMessageDialog(dtreeJFrame, result);
	}
}
