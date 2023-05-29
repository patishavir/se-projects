package oz.utils.load.generators;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;
import oz.utils.load.LoadGeneratorMain;

public class LoadGeneratorScript extends AbstractLoadGenerator {
	private String scriptPath = null;
	private static Logger logger = JulUtils.getLogger();

	public LoadGeneratorScript(final Properties properties) {
		ReflectionUtils.setFieldsFromProperties(properties, this);
	}

	public void generateLoad() {
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(scriptPath);
		logger.info(StringUtils.concat("Return code: ", String.valueOf(systemCommandExecutorResponse.getReturnCode())));
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = PathUtils.getFullPath(LoadGeneratorMain.getRootFolderPath(),
				PathUtils.adjustPathToCurrentOS(scriptPath));
		FileUtils.terminateIfFileDoesNotExist(this.scriptPath);
		logger.info(scriptPath);
	}
}
