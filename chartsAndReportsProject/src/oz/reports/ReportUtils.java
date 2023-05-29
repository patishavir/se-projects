package oz.reports;

import java.util.List;
import java.util.logging.Logger;

import oz.charts.ChartParameters;
import oz.charts.jfreechart.data.timeseries.TimeSeriesInfo;
import oz.charts.jfreechart.data.timeseries.TimeSeriesMinuteElement;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.excel.utils.ExcelUtils;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.number.NumberUtils;
import oz.infra.string.StringUtils;

public class ReportUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void writeAndOpenExcelWorkbook(final String report) {
		String fileName = StringUtils.concat(ChartParameters.getFileNamePrefix(), DateTimeUtils
				.getCurrentDate(DateTimeUtils.DATE_FORMAT_yyyyMMdd_HHmmss), OzConstants.XLS_SUFFIX);
		String xlsFilePath = PathUtils.getFullPath(ChartParameters.getReportFolderPath(), fileName);
		logger.finest(xlsFilePath);
		ExcelWorkbook.writeWorkbook(xlsFilePath, report, ChartParameters.getTitle());
		ExcelUtils.openExcelWorkbook(xlsFilePath);
	}
	public static String getReportTitle () {
		StringBuilder sb = new StringBuilder();
		sb.append(ExcelWorkbook.HEADER01_DIRECTIVE);
		sb.append(ChartParameters.getTitle());
		sb.append(" ");
		sb.append(ChartParameters.getTimeRangeArray()[0]);
		sb.append("-");
		sb.append(ChartParameters.getTimeRangeArray()[1]);
		sb.append("\n");
		sb.append(ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE);
		sb.append(ChartParameters.getColumnHeaders());
		sb.append("\n");
		return sb.toString();
	}
	public static String generateReport(final List<TimeSeriesInfo> timeSeriesInfoList) {
		StringBuilder sb = new StringBuilder(ReportUtils.getReportTitle());
		for (TimeSeriesInfo timeSeriesInfo1 : timeSeriesInfoList) {
			double total = 0;
			int count = 0;
			logger.finest(timeSeriesInfo1.getString());
			List<TimeSeriesMinuteElement> timeSeriesMinuteElementList = timeSeriesInfo1
					.getTimeSeriesMinuteElementAR();
			for (TimeSeriesMinuteElement timeSeriesMinuteElement1 : timeSeriesMinuteElementList) {
				total += timeSeriesMinuteElement1.getValue();
				count++;
			}
			double average = total / count;
			String averageString = NumberUtils.format(average, NumberUtils.FORMATTER_00_00);
			sb.append(StringUtils.concat(timeSeriesInfo1.getLabel(), ",", averageString, ",",
					String.valueOf(total), ",", String.valueOf(count), "\n"));
		}
		logger.info(StringUtils.concat("\n",sb.toString()));
		return sb.toString();
	}
}
