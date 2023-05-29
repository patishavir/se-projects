package oz.ssh.run;

import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.ssh.run.gui.RunSshGuiHandler;

public class RunSshMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		logger.info(SystemUtils.getRunInfo());
		logger.info("Starting ...");
		String commandsFilePath = args[0];
		FileUtils.terminateIfFileDoesNotExist(commandsFilePath);
		new RunSshGuiHandler().processGui(args[0]);
	}
}
