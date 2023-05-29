/*
 * Created on 24/07/2005
 */
package oz.jdir;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.InputParameters;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.jdir.fileoperations.FileOperationsEnum;
import oz.jdir.gui.DirFrame;

/**
 * @author Oded
 */
public final class JdirParameters {
	//
	private static String compareUtilityPath = "";
	private static String consoleHandler = "yes";
	private static String fileHandler = "no";
	private static String logFilePath = "c:\\temp\\jdir.log";
	private static Logger logger = JulUtils.getLogger();
	private static String loggingLevel = "INFO";
	private static boolean enableJFRameLoggingHandler = false;
	//
	private static final int DEFAULT_PROGRESS_INDICATOR_INTERVAL = 10;
	//
	private static JdirInfo dd = new JdirInfo();
	private static String destinationDir = null;
	private static JdirInfo sd = new JdirInfo();
	private static String sourceDir = null;
	//
	private static String FilePathExcludeFilter = "";
	private static String FilePathIncludeFilter = "";
	//
	private static String fileType2Change = null;
	//
	private static String string2Find = null;
	private static String replaceWithString = null;
	private static String suffixFilter = null;
	private static final String replaceStringGuiXmlFile = "findAndReplaceGUI.xml";
	//
	private static String messageDigestAlgorithm = "MD5";
	private static String newFileType = null;
	private static String operation;
	private static String optionsGuiXmlFile = "jdirOptionsGUI.xml";
	private static String versionInfoFilePath = "/oz/jdir/versionInfo.txt";
	private static int progressIndicatorInterval = DEFAULT_PROGRESS_INDICATOR_INTERVAL;
	//
	private static String ResponsibleTeam = "";
	private static String ResponsibleTeams = "CollateralRiki#CollateralJulia#CollateralElla#LevSara#LevYaniv";
	//
	private static String gui = "yes";
	private static String show = null;
	private static ShowOptionsEnum showOption = null;
	private static boolean showToolBar = false;
	private static int numberOfPanes = 2;
	//
	public static final String SIZE_AND_MESSAGE_DIGEST = "Size and message digest";
	private static String fileComparisonCriteria = SIZE_AND_MESSAGE_DIGEST;
	public static final String SIZE_AND_BINARY_COMPARISON = "Size and binary comparison";
	public static final String SIZE_AND_TIME_STAMP = "Size and time stamp";

	public static DirFrame dirFrame = null;

	//
	/*
	 * ProcessInputParameters method
	 */
	static void processParameters(final String[] args) {
		processJdirDeployProperties();
		final String[][] parametersArray = { { "sourceDir", null }, { "destinationDir", null }, { "gui", "yes" },
				{ "show", null }, { "operation", null }, { "fileComparisonCriteria", SIZE_AND_MESSAGE_DIGEST },
				{ "loggingLevel", "INFO" }, { "ConsoleHandler", "yes" }, { "FileHandler", "no" },
				{ "logFilePath", "c:\\temp\\jdir.log" }, { "FilePathIncludeFilter", "" },
				{ "FilePathExcludeFilter", "" }, { "ResponsibleTeam", "" }, { "ResponsibleTeams", "" },
				{ "string2Find", null }, { "replaceWithString", null }, { "suffixFilter", null } };
		new InputParameters().processParameters(args, parametersArray, new JdirParameters());
		logger.finest("Parameters processing done.");
		// complete parameters processing
		JdirParameters.getSd().setDirName(sourceDir);
		JdirParameters.getDd().setDirName(destinationDir);
		if (show != null) {
			JdirParameters.setShowOption(ShowOptionsEnum.valueOf(show));
		}
		if (numberOfPanes == 1) {
			dd.setEnabled(false);
		}
		JulUtils.setLoggingLevel(loggingLevel);

		return;
	}

	private static void processJdirDeployProperties() {
		Properties jdirDeployProperties = PropertiesUtils.loadPropertiesFromClassPath("oz.jdir.jdirDeploy.properties");
		numberOfPanes = Integer.parseInt(jdirDeployProperties.getProperty("numberOfPanes"));
		showToolBar = jdirDeployProperties.getProperty("showToolBar").equalsIgnoreCase("yes");
		String enableJFRameLoggingHandlerString = jdirDeployProperties.getProperty("enableJFRameLoggingHandler");
		if (enableJFRameLoggingHandlerString != null) {
			enableJFRameLoggingHandler = enableJFRameLoggingHandlerString.equalsIgnoreCase("yes");
		}

		for (FileOperationsEnum fileOperation1 : FileOperationsEnum.values()) {
			String operationProprrty = jdirDeployProperties.getProperty(fileOperation1.toString());
			fileOperation1.setEnabled(operationProprrty != null && operationProprrty.equalsIgnoreCase("enable"));
			logger.finest(fileOperation1.toString() + " " + fileOperation1.isEnabled());
		}
	}

	public static final String getCompareUtilityPath() {
		return compareUtilityPath;
	}

	public static final String getConsoleHandler() {
		return consoleHandler;
	}

	public static JdirInfo getDd() {
		return dd;
	}

	public static String getFileComparisonCriteria() {
		return fileComparisonCriteria;
	}

	public static final String getFileHandler() {
		return fileHandler;
	}

	public static final String getFilePathExcludeFilter() {
		return FilePathExcludeFilter;
	}

	public static final String getFilePathIncludeFilter() {
		return FilePathIncludeFilter;
	}

	public static final String getFileType2Change() {
		return fileType2Change;
	}

	public static String getMessageDigestAlgorithm() {
		return messageDigestAlgorithm;
	}

	public static final String getNewFileType() {
		return newFileType;
	}

	public static int getNumberOfPanes() {
		return numberOfPanes;
	}

	static String getOperation() {
		return operation;
	}

	public static final String getOptionsGuiXmlFile() {
		return optionsGuiXmlFile;
	}

	public static int getProgressIndicatorInterval() {
		return progressIndicatorInterval;
	}

	public static String getResponsibleTeam() {
		return ResponsibleTeam;
	}

	public static String getResponsibleTeams() {
		return ResponsibleTeams;
	}

	public static JdirInfo getSd() {
		return sd;
	}

	public static ShowOptionsEnum getShowOption() {
		return showOption;
	}

	public static final String getVersionInfoFilePath() {
		return versionInfoFilePath;
	}

	public static boolean isGui() {
		return gui.equalsIgnoreCase("yes");
	}

	public static boolean isShowToolBar() {
		return showToolBar;
	}

	public static final void setCompareUtilityPath(final String compareUtilityPathParam) {
		compareUtilityPath = compareUtilityPathParam;
	}

	public static final void setConsoleHandler(final String consoleHandlerP) {
		if (!consoleHandlerP.equalsIgnoreCase(consoleHandler)) {
			if (consoleHandlerP.equalsIgnoreCase("yes")) {
				JulUtils.addHandler(JulUtils.getFileHandler(logFilePath));
			} else {
				JulUtils.removeHandlers(JulUtils.getConsoleHandlerClassName());
			}
		}
		JdirParameters.consoleHandler = consoleHandlerP;
	}

	public static void setDd(final JdirInfo ddP) {
		JdirParameters.dd = ddP;
	}

	public static final void setDestinationDir(final String destinationDir) {
		if (destinationDir != null && destinationDir.length() > 0 && !new File(destinationDir).isDirectory()) {
			logger.severe("Destination dir must be a directory!");
		} else {
			JdirParameters.destinationDir = destinationDir;
		}
	}

	public static void setFileComparisonCriteria(final String fileComparisonCriteriaP) {
		JdirParameters.fileComparisonCriteria = fileComparisonCriteriaP;
	}

	public static final void setFileHandler(final String fileHandlerP) {
		if (!fileHandlerP.equalsIgnoreCase(fileHandler)) {
			JulUtils.addFileHandler(logFilePath);
		}
		JdirParameters.fileHandler = fileHandlerP;
	}

	public static final void setFilePathExcludeFilter(final String filePathExcludeFilter) {
		FilePathExcludeFilter = filePathExcludeFilter;
	}

	public static final void setFilePathIncludeFilter(final String filePathFilterP) {
		FilePathIncludeFilter = filePathFilterP;
		if (showOption != null) {
			DirMatchTable.getDirMatchTable().buidMatchTable(false);
		}
	}

	public static final void setFileType2Change(final String fileType2ChangeP) {
		JdirParameters.fileType2Change = fileType2ChangeP;
	}

	public static final void setGui(final String gui) {
		JdirParameters.gui = gui;
	}

	public static final void setLogFilePath(final String logFilePathP) {
		JdirParameters.logFilePath = logFilePathP;
	}

	public static final void setLoggingLevel(final String loggingLevelP) {
		JdirParameters.loggingLevel = loggingLevelP.toUpperCase();
		logger.info("loggingLevel: " + loggingLevel);
		JulUtils.setLoggingLevel(loggingLevel);
	}

	public static void setMessageDigestAlgorithm(final String messageDigestP) {
		JdirParameters.messageDigestAlgorithm = messageDigestP;
	}

	public static final void setNewFileType(final String newFileTypeP) {
		JdirParameters.newFileType = newFileTypeP;
	}

	public static void setNumberOfPanes(int numberOfPanes) {
		JdirParameters.numberOfPanes = numberOfPanes;
	}

	public static void setOperation(final String operationP) {
		JdirParameters.operation = operationP;
	}

	public static final void setOptionsGuiXmlFile(final String optionsGuiXmlFile) {
		JdirParameters.optionsGuiXmlFile = optionsGuiXmlFile;
	}

	public static void setResponsibleTeam(final String responsibleTeam) {
		JdirParameters.ResponsibleTeam = responsibleTeam;
	}

	public static void setResponsibleTeams(final String responsibleTeams) {
		JdirParameters.ResponsibleTeams = responsibleTeams;
	}

	public static void setSd(final JdirInfo sdP) {
		JdirParameters.sd = sdP;
	}

	public static final void setShow(final String show) {
		JdirParameters.show = show;
	}

	public static void setShowOption(final ShowOptionsEnum showOptionP) {
		JdirParameters.showOption = showOptionP;
	}

	public static final void setSourceDir(final String sourceDir) {
		if (sourceDir != null && sourceDir.length() > 0 && !new File(sourceDir).isDirectory()) {
			logger.severe("Source dir must be a directory!");
		} else {
			JdirParameters.sourceDir = sourceDir;
		}
	}

	public static boolean isEnableJFRameLoggingHandler() {
		return enableJFRameLoggingHandler;
	}

	public static String getString2Find() {
		return string2Find;
	}

	public static void setString2Find(final String string2Replace) {
		JdirParameters.string2Find = string2Replace;
	}

	public static String getReplaceWithString() {
		return replaceWithString;
	}

	public static void setReplaceWithString(final String replaceWithString) {
		JdirParameters.replaceWithString = replaceWithString;
	}

	public static String getReplaceStringGuiXmlFile() {
		return replaceStringGuiXmlFile;
	}

	public static void update(final Observable Observable, final Object parametersHashTableObj) {

		Object[] setParamArray = { null };
		logger.info("start update processing");
		Hashtable<String, String> parametersHashTable = (Hashtable<String, String>) parametersHashTableObj;
		JdirParameters jdirParameters = new JdirParameters();
		for (Enumeration<String> enumeration = parametersHashTable.keys(); enumeration.hasMoreElements();) {
			String key = enumeration.nextElement();
			String value = parametersHashTable.get(key);
			Class[] klass = { String.class };
			String methodName = "set" + StringUtils.first2Upper(key);
			logger.info("methodName: " + methodName);
			try {
				Method myMethod = JdirParameters.class.getDeclaredMethod(methodName, klass);
				setParamArray[0] = value;
				logger.info(key + "=" + value);
				myMethod.invoke(jdirParameters, setParamArray);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.warning(ex.getMessage());
			}
		}

	}

	public static String getSuffixFilter() {
		return suffixFilter;
	}

	public static void setSuffixFilter(final String suffixFilter) {
		JdirParameters.suffixFilter = suffixFilter;
	}

	public static DirFrame getDirFrame() {
		return dirFrame;
	}

	public static void setDirFrame(DirFrame dirFrame) {
		JdirParameters.dirFrame = dirFrame;
	}
}