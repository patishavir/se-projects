package oz.loganalyzer.data;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.filter.StringFilter;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;
import oz.loganalyzer.LogAnalyzerParameters;

public class LogAnalyzerFileNameFilter implements StringFilter {
	private static final String logfileNameFilter = LogAnalyzerParameters.getLogfileNameFilter();
	private static final String[][] timeRangeArray = LogAnalyzerParameters
			.getFileNameTimeRangeArray();
	private static Logger logger = JulUtils.getLogger();

	public boolean accept(final String fileName) {
		logger.finest("fileName: " + fileName);

		boolean returnCode = false;
		if (logfileNameFilter == null || logfileNameFilter.length() == 0) {
			returnCode = true;
		} else {
			String regexp = RegexpUtils.REGEXP_ANYSTRING + logfileNameFilter
					+ RegexpUtils.REGEXP_ANYSTRING;
			returnCode = fileName.matches(regexp);
		}
		if (returnCode && timeRangeArray != null) {
			String logTime = fileName.substring(fileName.length() - OzConstants.INT_5);
			logger.finest("logTime: " + logTime);

			for (int i = 0; i < timeRangeArray.length; i++) {
				returnCode = StringUtils.isStringWithinRage(logTime, timeRangeArray[i][0],
						timeRangeArray[i][1]);
				if (returnCode) {
					break;
				}
			}
		}
		if (returnCode) {
			logger.finest(fileName + " has been accepted");
		} else {
			logger.finest(fileName + " has been rejected");
		}
		return returnCode;
	}

	public String toString() {
		String string = "logfileNameFilter: " + logfileNameFilter;
		if (timeRangeArray != null) {
			string = string.concat("\ntimeRangeArray:");
			for (int i = 0; i < timeRangeArray.length; i++) {
				string = string.concat("\n" + timeRangeArray[i][0] + " - " + timeRangeArray[i][1]);
			}
		}
		return string;

	}
}
