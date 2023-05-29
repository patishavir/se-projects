package oz.reports;

import java.util.List;
import java.util.logging.Logger;

import oz.charts.ChartParameters;
import oz.charts.jfreechart.data.timeseries.TimeSeriesInfo;
import oz.data.LogDataUtils;
import oz.infra.logging.jul.JulUtils;

public class ReportMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		String propertiesFilePath = args[0];
		ChartParameters.processParameters(propertiesFilePath);
		List<TimeSeriesInfo> timeSeriesInfoList = LogDataUtils.getData(ChartParameters
				.getDataFolderPath());
		String report = ReportUtils.generateReport(timeSeriesInfoList);
		ReportUtils.writeAndOpenExcelWorkbook(report);
	}
}
