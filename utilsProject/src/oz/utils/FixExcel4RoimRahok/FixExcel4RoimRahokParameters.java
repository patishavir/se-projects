package oz.utils.FixExcel4RoimRahok;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;

public class FixExcel4RoimRahokParameters {
	private static String folderPath = null;
	private static String fileProcessorWatcherClass = null;
	private static String targetFilePrefix = null;
	private static String targetFolderPath = null;
	private static String rootFolderPath = null;
	private static String string2Replace = null;
	private static String replacement = null;
	private static String columnNames = null;
	private static String[] columnNamesArray = null;
	private static boolean openExcelWorkBook = false;
	private static Logger logger = JulUtils.getLogger();

	public static String[] getColumnNamesArray() {
		return columnNamesArray;
	}

	public static String getFileProcessorWatcherClass() {
		return fileProcessorWatcherClass;
	}

	public static String getFolderPath() {
		return folderPath;
	}

	public static String getReplacement() {
		return replacement;
	}

	public static String getString2Replace() {
		return string2Replace;
	}

	public static String getTargetFilePrefix() {
		return targetFilePrefix;
	}

	public static String getTargetFolderPath() {
		return targetFolderPath;
	}

	public static boolean isOpenExcelWorkBook() {
		return openExcelWorkBook;
	}

	public static void processParameters(final String propertiesFilePath) {
		FileUtils.terminateIfFileDoesNotExist(propertiesFilePath);
		rootFolderPath = PathUtils.getParentFolderPath(propertiesFilePath);
		logger.finest("rootFolderPath: " + rootFolderPath);
		Properties props = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		PropertiesUtils.printProperties(props, Level.INFO);
		ReflectionUtils.setFieldsFromProperties(props, FixExcel4RoimRahokParameters.class);
		logger.info("\nrootFolderPath: " + rootFolderPath + "\nfolderPath:" + folderPath);
	}

	public static void setColumnNames(String columnNames) {
		FixExcel4RoimRahokParameters.columnNames = columnNames;
		columnNamesArray = columnNames.split(OzConstants.COMMA);

	}

	public static void setFileProcessorWatcherClass(String fileProcessorWatcherClass) {
		FixExcel4RoimRahokParameters.fileProcessorWatcherClass = fileProcessorWatcherClass;
	}

	public static void setFolderPath(final String folderPath) {
		FixExcel4RoimRahokParameters.folderPath = folderPath;
	}

	public static void setOpenExcelWorkBook(boolean openExcelWorkBook) {
		FixExcel4RoimRahokParameters.openExcelWorkBook = openExcelWorkBook;
	}

	public static void setReplacement(String replacement) {
		FixExcel4RoimRahokParameters.replacement = replacement;
	}

	public static void setString2Replace(String string2Replace) {
		FixExcel4RoimRahokParameters.string2Replace = string2Replace;
	}

	public static void setTargetFilePrefix(String targetFilePrefix) {
		FixExcel4RoimRahokParameters.targetFilePrefix = targetFilePrefix;
	}

	public static void setTargetFolderPath(String targetFolderPath) {
		FixExcel4RoimRahokParameters.targetFolderPath = targetFolderPath;
	}
}
