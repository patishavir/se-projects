package oz.charts;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;

public class ChartParameters {
	private static String dataFolderPath = null;
	private static String title = null;
	private static String tileAxisLabel = "Time";
	private static String valueAxisLabel = "Cpu utilization %";
	private static int valueColumnIndex = Integer.MIN_VALUE;
	private static String fileNamePrefix = null;
	private static String rootFolderPath = null;
	private static String timeRange = null;
	private static String[] timeRangeArray = null;
	private static int chartWidth = Integer.MIN_VALUE;
	private static int chartHeight = Integer.MIN_VALUE;
	private static String chartPngFolderPath = null;
	private static String reportFolderPath = null;
	private static String chartPngName = null;
	private static boolean showInJframe = true;
	private static String columnHeaders = null;

	private static Logger logger = JulUtils.getLogger();

	public static int getChartHeight() {
		return chartHeight;
	}

	public static String getChartPngFolderPath() {
		return chartPngFolderPath;
	}

	public static String getChartPngName() {
		return chartPngName;
	}

	public static int getChartWidth() {
		return chartWidth;
	}

	public static String getColumnHeaders() {
		return columnHeaders;
	}

	public static String getDataFolderPath() {
		return dataFolderPath;
	}

	public static String getFileNamePrefix() {
		return fileNamePrefix;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getReportFolderPath() {
		return reportFolderPath;
	}

	public static String getTileAxisLabel() {
		return tileAxisLabel;
	}

	public static String[] getTimeRangeArray() {
		return timeRangeArray;
	}

	public static String getTitle() {
		return title;
	}

	public static String getValueAxisLabel() {
		return valueAxisLabel;
	}

	public static int getValueColumnIndex() {
		return valueColumnIndex;
	}

	public static boolean isShowInJframe() {
		return showInJframe;
	}

	public static void processParameters(final String propertiesFilePath) {
		logger.info("Starting. properties file path: " + propertiesFilePath);
		FileUtils.terminateIfFileDoesNotExist(propertiesFilePath);
		rootFolderPath = PathUtils.getParentFolderPath(propertiesFilePath);
		logger.finest(rootFolderPath);
		Properties chartProperties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		PropertiesUtils.updatePropertiesUsingEnvironmentVarialbes(chartProperties);
		logger.info(PropertiesUtils.getAsDelimitedString(chartProperties));
		ReflectionUtils.setFieldsFromProperties(chartProperties, ChartParameters.class);
	}

	public static void setChartHeight(final int chartHeight) {
		ChartParameters.chartHeight = chartHeight;
	}

	public static void setChartPngFolderPath(final String chartPngFolderPath) {
		ChartParameters.chartPngFolderPath = PathUtils.getFullPath(rootFolderPath, chartPngFolderPath);
		FolderUtils.createFolderIfNotExists(ChartParameters.chartPngFolderPath);
	}

	public static void setChartPngName(final String chartPngName) {
		ChartParameters.chartPngName = chartPngName;
	}

	public static void setChartWidth(final int chartWidth) {
		ChartParameters.chartWidth = chartWidth;
	}

	public static void setColumnHeaders(final String columnHeaders) {
		ChartParameters.columnHeaders = columnHeaders;
	}

	public static void setDataFolderPath(final String dataFolderPath) {
		ChartParameters.dataFolderPath = dataFolderPath;
	}

	public static void setFileNamePrefix(final String fileNamePrefix) {
		ChartParameters.fileNamePrefix = fileNamePrefix;
	}

	public static void setReportFolderPath(final String reportFolderPath) {
		ChartParameters.reportFolderPath = PathUtils.getFullPath(rootFolderPath, reportFolderPath);
		FolderUtils.createFolderIfNotExists(ChartParameters.reportFolderPath);
	}

	public static void setShowInJframe(final boolean showInJframe) {
		ChartParameters.showInJframe = showInJframe;
	}

	public static void setTileAxisLabel(final String tileAxisLabel) {
		ChartParameters.tileAxisLabel = tileAxisLabel;
	}

	public static void setTimeRange(final String timeRange) {
		ChartParameters.timeRange = timeRange;
		timeRangeArray = ChartParameters.timeRange.split(OzConstants.UNDERSCORE);
	}

	public static void setTitle(final String title) {
		ChartParameters.title = title;
	}

	public static void setValueAxisLabel(final String valueAxisLabel) {
		ChartParameters.valueAxisLabel = valueAxisLabel;
	}

	public static void setValueColumnIndex(final int valueColumnIndex) {
		ChartParameters.valueColumnIndex = valueColumnIndex;
	}

}
