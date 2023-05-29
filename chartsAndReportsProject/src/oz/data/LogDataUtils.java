package oz.data;

import static oz.infra.constants.OzConstants.HTTP;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

import oz.charts.ChartParameters;
import oz.charts.jfreechart.data.timeseries.TimeSeriesInfo;
import oz.charts.jfreechart.data.timeseries.TimeSeriesMinuteElement;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.filter.StringStartsWithFilter;
import oz.infra.filter.StringWithinRangeNonInclusiveFilter;
import oz.infra.filter.SubStringNotFoundFilter;
import oz.infra.http.HTTPUtils;
import oz.infra.httpserver.HttpServerUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class LogDataUtils {
	private static Logger logger = JulUtils.getLogger();
	private static SubStringNotFoundFilter subStringNotFoundFilter = null;

	private static List<TimeSeriesInfo> getTimeSeriesInfo(final Map<String, String[]> dataFilesContentsMap) {
		StringWithinRangeNonInclusiveFilter stringWithinRangeFilter = new StringWithinRangeNonInclusiveFilter(
				ChartParameters.getTimeRangeArray());
		ArrayList<TimeSeriesInfo> timeSeriesInfoList = new ArrayList<TimeSeriesInfo>();
		for (Entry<String, String[]> mapEntry : dataFilesContentsMap.entrySet()) {
			String[] dataRecordsArray = mapEntry.getValue();
			if (dataRecordsArray != null && dataRecordsArray.length > 0) {
				ArrayList<TimeSeriesMinuteElement> timeSeriesMinuteElementList = new ArrayList<TimeSeriesMinuteElement>();
				logger.info("number of lines: " + String.valueOf(dataRecordsArray.length));
				int invalidRecountCounter = 0;
				for (int rowPtr = 0; rowPtr < dataRecordsArray.length; rowPtr++) {
					TimeSeriesMinuteElement timeSeriesMinuteElement = new TimeSeriesMinuteElement(
							dataRecordsArray[rowPtr]);
					logger.finest(timeSeriesMinuteElement.getString());
					if (timeSeriesMinuteElement.isValidElement()) {
						if (stringWithinRangeFilter.accept(timeSeriesMinuteElement.getHhmm())) {
							timeSeriesMinuteElementList.add(timeSeriesMinuteElement);
						}
					} else {
						invalidRecountCounter++;
					}
				}
				String label = mapEntry.getKey();
				timeSeriesInfoList.add(new TimeSeriesInfo(label, timeSeriesMinuteElementList));
				String message = StringUtils.concat("adding ", label, " number of elements: ",
						String.valueOf(timeSeriesMinuteElementList.size()), " invalid records: ",
						String.valueOf(invalidRecountCounter));
				logger.info(message);
			}
		}
		return timeSeriesInfoList;
	}

	public static List<TimeSeriesInfo> getData(final String dataFolderPath) {
		logger.info("Processing " + dataFolderPath);
		String currentYear = String.valueOf(DateTimeUtils.getCurrentYear());
		subStringNotFoundFilter = new SubStringNotFoundFilter(currentYear);
		Map<String, String[]> dataFilesContentsMap = null;
		if (dataFolderPath != null && dataFolderPath.substring(0, HTTP.length()).equalsIgnoreCase(HTTP)) {
			dataFilesContentsMap = getHttpLogsData(dataFolderPath);
		} else {
			dataFilesContentsMap = getFileSystemLogsData(dataFolderPath);
		}
		return getTimeSeriesInfo(dataFilesContentsMap);
	}

	private static Map<String, String[]> getFileSystemLogsData(final String dataFolderPath) {
		FolderUtils.dieUnlessFolderExists(dataFolderPath);
		Map<String, String[]> dataFilesContentsMap = new TreeMap<String, String[]>();
		StringStartsWithFilter stringStartsWithFilter = new StringStartsWithFilter(ChartParameters.getFileNamePrefix());
		File dataFolder = new File(dataFolderPath);
		File[] dataFilesArray = dataFolder.listFiles();
		for (File dataFile1 : dataFilesArray) {
			if (dataFile1.length() > 0 && subStringNotFoundFilter.accept(dataFile1.getAbsolutePath())
					&& stringStartsWithFilter.accept(dataFile1.getName())) {
				String[] dataLinesArray = FileUtils.readTextFile2Array(dataFile1);
				if (dataLinesArray != null) {
					dataFilesContentsMap.put(getLabelFromFileName(dataFile1.getName()), dataLinesArray);
				}
			}
		}
		return dataFilesContentsMap;
	}

	private static Map<String, String[]> getHttpLogsData(final String url) {
		List<String> fileUrlsList = HttpServerUtils.getFileUrlsList(url, ChartParameters.getFileNamePrefix(),
				subStringNotFoundFilter);
		Map<String, String[]> dataFilesContentsMap = new TreeMap<String, String[]>();
		for (String fileUrl1 : fileUrlsList) {
			String[] dataLinesArray = HTTPUtils.getPageContents(fileUrl1).split("\n");
			if (dataLinesArray.length > 1) {
				String fileName = fileUrl1.substring(fileUrl1.lastIndexOf(OzConstants.SLASH) + 1);
				final int serverNameIndex = 9;
				String label = fileName.substring(serverNameIndex, fileName.lastIndexOf(OzConstants.DOT));
				dataFilesContentsMap.put(label, dataLinesArray);
			}
		}
		return dataFilesContentsMap;
	}

	private static String getLabelFromFileName(final String fileName) {
		final String snifp = "snifp";
		String label = fileName.substring(ChartParameters.getFileNamePrefix().length(), fileName.indexOf(".log"));
		if (label.startsWith(snifp)) {
			label = label.substring(snifp.length());
		}
		return label;

	}
}
