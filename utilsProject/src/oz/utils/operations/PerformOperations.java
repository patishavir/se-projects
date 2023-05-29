package oz.utils.operations;

import java.io.File;
import java.io.FileFilter;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.filefilter.FileFilterIsFileAndRegExpression;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.watcher.WatchDirService;
import oz.infra.operaion.Operations;
import oz.infra.operaion.Outcome;
import oz.infra.parameters.ParametersUtils;
import oz.infra.process.FileProcessor;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;

public class PerformOperations implements FileProcessor {
	private static PerformOperations performOperations = new PerformOperations();
	private static final Logger logger = JulUtils.getLogger();

	private static String getOperationPropertiesFilePath(final Operations operation, final File[] properitesFilesArray) {
		String propertiesFilePath = null;
		for (File file1 : properitesFilesArray) {
			String fileName = file1.getName();
			logger.info(fileName + "     " + operation.name());
			if (fileName.toLowerCase().startsWith(operation.name().toLowerCase())) {
				propertiesFilePath = file1.getAbsolutePath();
				break;
			}
		}
		logger.info(propertiesFilePath);
		return propertiesFilePath;
	}

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		PerformOperations performOperations = new PerformOperations();
		processParameters(args[0]);
	}

	private static void processParameters(final String PerformOperationsPropertiesFilePath) {
		PerformOperationsParameters performOperationsParameters = new PerformOperationsParameters();
		logger.info(performOperationsParameters.asString());
		ParametersUtils.processPatameters(PerformOperationsPropertiesFilePath, performOperationsParameters);
		String propertiesFolderPath = performOperationsParameters.getPropertiesFolderPath();
		FileFilter fileFilter = new FileFilterIsFileAndRegExpression(RegexpUtils.REGEXP_PROPERTIES_FILE);
		File[] properitesFilesArray = FolderUtils.getFilesInFolder(propertiesFolderPath, fileFilter);
		ArrayUtils.printArray(properitesFilesArray);
		String[] operationsArray = performOperationsParameters.getOperations2PerformArray();
		// for (String operation1 : operationsArray) {
		// logger.info(operation1);
		// String propertiesFileFullPath = PathUtils.getFullPath(propertiesFolderPath, operation1);
		// logger.info(propertiesFileFullPath);
		//
		//
		String propertiesFilePath = null;
		for (String operationStr : operationsArray) {
			Operations operation = Operations.valueOf(operationStr);
			switch (operation) {
			case FORCE_SINGLE_INSTANCE:
				propertiesFilePath = getOperationPropertiesFilePath(operation, properitesFilesArray);
				// SingleInstanceUtils.confirmSingleInstanceRun(propertiesFilePath);
				performOperation(operation, propertiesFilePath);
				break;
			case WATCH:
				propertiesFilePath = getOperationPropertiesFilePath(operation, properitesFilesArray);
				new WatchDirService(propertiesFilePath, performOperations);
				break;
			case REMOTECOPY:
				propertiesFilePath = getOperationPropertiesFilePath(operation, properitesFilesArray);
				Properties remoteProperties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
				break;
			}
		}
	}

	private static Object performOperation(final Operations operation, final String propertiesFilePath) {
		// try {
		// Class clazz = Class.forName(operation.getClassName());
		// Method method = clazz.getDeclaredMethod(operation.getMethodName(), propertiesFilePath.getClass());
		// // logger.info("invoke: " + clazz.getName() + OzConstants.DOT + method.toString());
		// logger.info("invoke: " + method.toString());
		// method.invoke(null, propertiesFilePath);
		// } catch (Exception ex) {
		// ExceptionUtils.printMessageAndStackTrace(ex);
		// }
		return ReflectionUtils.invokeMethod(operation.getClassName(), operation.getMethodName(), propertiesFilePath);
	}

	// private static void sendGmMail(final String subject) {
	// Properties emailProperties = PropertiesUtils.loadPropertiesFile(gmMailPorpertiesFilePath);
	// Properties gmMailProperties =
	// GMUtils.getGmEmailDefaultProperties(emailProperties.getProperty(GMUtils.GM_ENVIRONMENT));
	// gmMailProperties.put(GMUtils.TO, emailProperties.getProperty(GMUtils.TO));
	// gmMailProperties.put(GMUtils.FROM, emailProperties.getProperty(GMUtils.FROM));
	// gmMailProperties.put(GMUtils.SUBJECT, subject);
	// logger.finest(PropertiesUtils.getAsDelimitedString(gmMailProperties));
	// GMUtils.sendEmail(gmMailProperties);
	// }

	@Override
	public Outcome processFile(final String filePath) {
		Outcome outcome = Outcome.SUCCESS;
		logger.info("processing filePath: " + filePath);
		Properties remoteProperties = null; // PropertiesUtils.loadPropertiesFile(remotePropertiesFilePath);
		remoteProperties.put(SshParams.SOURCE_FILE, filePath);
		int retcode = JschUtils.scpTo(remoteProperties);
		String server = remoteProperties.getProperty(SshParams.SERVER);
		String message = filePath + " has been successfully copied to " + server;
		if (retcode != 0) {
			outcome = Outcome.FAILURE;
			message = "copy of " + filePath + " to " + server + " has failed";
		}
		logger.info(message + ". outcome: " + outcome.toString());
		// sendGmMail(message);
		return outcome;
	}

	// private void watch(final String propertiesFilePath) {
	// Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
	// WatchDirServiceParameters watchDirServiceParameters = new WatchDirServiceParameters(properties);
	// new WatchDirService(watchDirServiceParameters, this);
	// }
}