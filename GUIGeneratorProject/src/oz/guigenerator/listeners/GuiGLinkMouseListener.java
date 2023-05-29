package oz.guigenerator.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import oz.guigenerator.gui.GuiGeneratorJTextPane;
import oz.infra.run.RunExecInaThread;

public class GuiGLinkMouseListener extends MouseAdapter {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public final void mouseClicked(final MouseEvent me) {
		logger.finest("Mouse clicked");
		GuiGeneratorJTextPane textPane = (GuiGeneratorJTextPane) me.getSource();

		String url = textPane.getUrl();
		logger.finest("url: " + url);
		String osName = System.getProperty("os.name");
		logger.finest("OS name: " + osName);
		if (osName.startsWith("Windows")) {
			String[] params = { "cmd.exe", "/c", "start", url };
			RunExecInaThread runExecInaThread = new RunExecInaThread(params);
			runExecInaThread.start();
		} else {
			logger.warning("Link is currently supported for Windows only!");
		}
	}
}
