package oz.sqlrunner.gui.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import oz.sqlrunner.handlers.ExitHandler;

public class SQLRunnerWindowListener extends WindowAdapter {

	private Logger logger = Logger.getLogger(this.getClass().toString());

	public void windowClosed(WindowEvent e) {
		ExitHandler.doExit("Window has been closed ...");
	}
}
