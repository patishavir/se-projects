package oz.jdir.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.jdir.gui.OpenJDialog;
import oz.jdir.gui.OpenOperationsEnum;

public class OpenActionListener implements ActionListener {
	private Logger logger = JulUtils.getLogger();
	private OpenJDialog openJDialog;

	public OpenActionListener(final OpenJDialog openJDialog) {
		this.openJDialog = openJDialog;
	}

	public void actionPerformed(final ActionEvent e) {
		String actionCommandString = e.getActionCommand();
		logger.finest("actionCommandString: " + actionCommandString);
		OpenOperationsEnum openOperation = OpenOperationsEnum.valueOf(actionCommandString);
		switch (openOperation) {
		case Update:
			logger.info(openOperation.name());
			logger.finest(openJDialog.getTextArea().getText());
			FileUtils.writeFile(openJDialog.getSourceFilePath(), openJDialog.getTextArea().getText());
			break;
		case Exit:
			logger.info(openOperation.name());
			openJDialog.setVisible(false);
			break;
		default:
			logger.severe("Oops!, " + e.getActionCommand() + " is an invalid entry.");
		}
	}
}