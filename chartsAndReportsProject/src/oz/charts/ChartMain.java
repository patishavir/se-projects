package oz.charts;

import java.util.List;
import java.util.logging.Logger;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import oz.charts.jfreechart.JfreeChartUtils;
import oz.charts.jfreechart.data.timeseries.TimeSeriesInfo;
import oz.data.LogDataUtils;
import oz.infra.logging.jul.JulUtils;

// TimeSeriesDemo1
public class ChartMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String args[]) {
		String propertiesFilePath = args[0];
		ChartParameters.processParameters(propertiesFilePath);
		List<TimeSeriesInfo> timeSeriesInfoList = LogDataUtils.getData(ChartParameters
				.getDataFolderPath());
		XYDataset xydataset = JfreeChartUtils.createXYDataset(timeSeriesInfoList);
		JFreeChart jfreechart = JfreeChartUtils.createChart(xydataset);
		JfreeChartUtils.saveChartAsPNG(jfreechart);
		if (ChartParameters.isShowInJframe()) {
			JfreeChartUtils.showInJframe(jfreechart);
		}
		logger.info("All done ...");
	}
}
