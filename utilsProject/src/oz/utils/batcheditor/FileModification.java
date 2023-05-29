package oz.utils.batcheditor;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.env.EnvironmentUtils;

public class FileModification {
	private enum BatchEditorOperationEnum {
		REPLACE, DELETE
	}

	private String operation = "REPLACE";
	private BatchEditorOperationEnum batchEditorOperation = BatchEditorOperationEnum.valueOf(operation);
	private String filePath = null;
	private String sourceString = null;
	private String[] sourceStringArray = null;
	private String targetString = null;
	private String[] targetStringArray = null;
	private String lineFilter = null;
	private String[] lineFilterArray = null;
	private String stringsDelimiter = ",";
	private String lineSeparator = SystemUtils.LINE_SEPARATOR;
	private static Map<String, String> environmentMap = EnvironmentUtils.getEnvironmentVariablesMap();
	private int rowsModified = 0;
	private static Logger logger = JulUtils.getLogger();

	public FileModification(final Properties properties) {
		logger.finest("Start processing ...");
		ReflectionUtils.setFieldsFromProperties(properties, this);
		if (targetStringArray != null && sourceStringArray.length != targetStringArray.length) {
			SystemUtils.printMessageAndExit(StringUtils.concat(
					"Number of source Strings must match the number of target Strings.",
					"\nProcessing has been aborted!"), OzConstants.EXIT_STATUS_ABNORMAL);
		}
		if (lineFilterArray != null && lineFilterArray.length != sourceStringArray.length) {
			SystemUtils.printMessageAndExit(StringUtils.concat(
					"Number of source Strings must match the number of line filter Strings.",
					"\nProcessing has been aborted!"), OzConstants.EXIT_STATUS_ABNORMAL);
		}
		ArrayUtils.printArray(sourceStringArray, SystemUtils.LINE_SEPARATOR, "source String Array:\n", Level.FINEST);
		ArrayUtils.printArray(targetStringArray, SystemUtils.LINE_SEPARATOR, "target String Array:\n", Level.FINEST);
	}

	private boolean doesStringMatch(final String string2Process) {
		boolean rc = false;
		for (int i = 0; i < sourceStringArray.length; i++) {
			if (string2Process != null
					&& string2Process.length() > 0
					&& string2Process.indexOf(sourceStringArray[i]) >= 0
					&& ((lineFilterArray == null) || (lineFilterArray[i].length() > 0 && string2Process
							.matches(lineFilterArray[i])))) {
				rc = true;
				logger.finest(StringUtils.concat(string2Process, " does match !"));
			}
		}
		return rc;
	}

	public boolean doProcess() {
		logger.info(StringUtils.concat("Start editing ", filePath));
		String[] contentsArray = FileUtils.readTextFile2Array(filePath);
		ArrayUtils.printArray(contentsArray, SystemUtils.LINE_SEPARATOR, "Source file:\n", Level.FINEST);
		BatchEditorOperationEnum batchEditorOperation = BatchEditorOperationEnum.valueOf(operation);
		switch (batchEditorOperation) {
		case REPLACE:
			for (int row = 0; row < contentsArray.length; row++) {
				if (contentsArray[row] != null && contentsArray[row].length() > 0) {
					contentsArray[row] = processReplace(contentsArray[row]);
				}
			}
			break;
		case DELETE:
			ArrayList<String> contentsList = new ArrayList<String>();
			for (int row = 0; row < contentsArray.length; row++) {
				if (contentsArray[row] != null && contentsArray[row].length() > 0) {
					if (!doesStringMatch(contentsArray[row])) {
						contentsList.add(contentsArray[row]);
					} else {
						rowsModified++;
					}
				}
			}
			contentsArray = ListUtils.stringListToStringArray(contentsList);
			break;
		}
		if (rowsModified > 0) {
			File targetFile = new File(filePath);
			targetFile.setWritable(true);
			FileUtils.writeFile(filePath, contentsArray, lineSeparator);
			ArrayUtils.printArray(contentsArray, SystemUtils.LINE_SEPARATOR, "Modified file:\n", Level.FINEST);
		}
		return (rowsModified > 0);
	}

	private String processReplace(final String stringParameter) {
		String string2Process = stringParameter;
		logger.finest(string2Process);
		for (int i = 0; i < sourceStringArray.length; i++) {
			if (doesStringMatch(string2Process)) {
				string2Process = string2Process.replace(sourceStringArray[i], targetStringArray[i]);
				logger.finest(StringUtils.concat(string2Process, " has been modified"));
				rowsModified++;
			}
		}
		return string2Process;
	}

	public final void setFilePath(final String filePath) {
		this.filePath = filePath;
		FileUtils.terminateIfFileDoesNotExist(filePath);
	}

	public final void setLineFilter(final String lineFilter) {
		if (lineFilter != null && lineFilter.length() > 0) {
			lineFilterArray = lineFilter.split(stringsDelimiter);
		}
	}

	public final void setLineSeparator(final String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}

	public final void setOperation(final String operation) {
		this.operation = operation;
		try {
			batchEditorOperation = BatchEditorOperationEnum.valueOf(operation);
		} catch (Exception ex) {
			SystemUtils.printMessageAndExit(operation + " is an invalid operation. Processing has been aborted!", -1);
		}
	}

	public final void setSourceString(final String sourceString) {
		sourceStringArray = sourceString.split(stringsDelimiter);
		sourceStringArray = StringUtils.substituteVariables(sourceStringArray, environmentMap);
	}

	public final void setStringsDelimiter(final String stringsDelimiter) {
		this.stringsDelimiter = stringsDelimiter;
	}

	public final void setTargetString(final String targetString) {
		targetStringArray = targetString.split(stringsDelimiter);
		targetStringArray = StringUtils.substituteVariables(targetStringArray, environmentMap);
	}
}
