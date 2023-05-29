package oz.ssh.run.gui;

import java.util.logging.Logger;

import oz.guigenerator.GuiGeneratorDefaultsProviderInterface;
import oz.guigenerator.GuiGeneratorMain;
import oz.guigenerator.GuiGeneratorParamsFileProcessor;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.ssh.run.RunSshHandler;

public class RunSshGuiHandler implements GuiGeneratorDefaultsProviderInterface {
	private static final String SSH_GUI_XML_FILE = "/oz/ssh/run/gui/xml/sshrun.xml";
	private String[] commands = null;
	private static Logger logger = JulUtils.getLogger();
	private GuiGeneratorParamsFileProcessor guiGeneratorParamsFileProcessor;

	public final String getDefaultValue(final String key) {
		logger.finest("getDefaultValue: " + key);
		return null;
	}

	public final String[] getValues(final String key) {
		logger.finest("getDefaultValues " + key);
		if (key.equalsIgnoreCase("command")) {
			return commands;
		} else {
			return null;
		}
	}

	public void processGui(final String commandsFilePath) {
		commands = FileUtils.readTextFile2Array(commandsFilePath);
		guiGeneratorParamsFileProcessor = GuiGeneratorMain.showGui(guiGeneratorParamsFileProcessor, SSH_GUI_XML_FILE, new RunSshHandler(), this,
				null);
	}
}
