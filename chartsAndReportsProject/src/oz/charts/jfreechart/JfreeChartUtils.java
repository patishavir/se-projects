package oz.charts.jfreechart;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

import oz.charts.ChartParameters;
import oz.charts.jfreechart.data.timeseries.TimeSeriesInfo;
import oz.charts.jfreechart.data.timeseries.TimeSeriesMinuteElement;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.dimension.DimensionUtils;

public class JfreeChartUtils {
	private static Logger logger = JulUtils.getLogger();

	public static JFreeChart createChart(XYDataset xydataset) {
		String title = ChartParameters.getTitle() + " " + TimeSeriesMinuteElement.getChartDate();
		ChartParameters.setTitle(title);
		String tileAxisLabel = ChartParameters.getTileAxisLabel();
		String valueAxisLabel = ChartParameters.getValueAxisLabel();
		boolean lengend = true;
		boolean tooltips = true;
		boolean urls = false;
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title, tileAxisLabel,
				valueAxisLabel, xydataset, lengend, tooltips, urls);
		jfreechart.setBackgroundPaint(Color.white);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setBackgroundPaint(Color.lightGray);
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);
		xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		org.jfree.chart.renderer.xy.XYItemRenderer xyitemrenderer = xyplot.getRenderer();
		if (xyitemrenderer instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyitemrenderer;
			xylineandshaperenderer.setBaseShapesVisible(true);
			xylineandshaperenderer.setBaseShapesFilled(true);
		}
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
		return jfreechart;
	}

	public static XYDataset createXYDataset(final List<TimeSeriesInfo> timeSeriesInfoList) {
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		for (TimeSeriesInfo timeSeriesInfo1 : timeSeriesInfoList) {
			TimeSeries timeseries = new TimeSeries(timeSeriesInfo1.getLabel(),
					org.jfree.data.time.Minute.class);
			logger.info("Processing " + timeSeriesInfo1.getLabel());
			for (TimeSeriesMinuteElement timeSeriesMinuteElement : timeSeriesInfo1
					.getTimeSeriesMinuteElementAR()) {
				logger.finest(timeSeriesMinuteElement.getString());
				try {
					timeseries.add(timeSeriesMinuteElement.getMinute(),
							timeSeriesMinuteElement.getValue());
				} catch (SeriesException sex) {
					logger.warning(timeSeriesMinuteElement.getString());
					logger.warning(sex.getMessage());
					sex.printStackTrace();
				}
			}
			timeseriescollection.addSeries(timeseries);
		}
		return timeseriescollection;
	}

	public static void showInJframe(final JFreeChart jfreechart) {
		ApplicationFrame ApplicationFrame = new ApplicationFrame(ChartParameters.getTitle());
		// ChartUtilities.saveChartAsPNG(file, chart, width, height);
		ChartPanel chartpanel = new ChartPanel(jfreechart, false);
		chartpanel.setPreferredSize(new Dimension(DimensionUtils.getScreenWidth() * 9 / 10,
				(int) DimensionUtils.getScreenHeight() * 9 / 10));
		chartpanel.setMouseZoomable(true, false);
		ApplicationFrame.setContentPane(chartpanel);
		ApplicationFrame.pack();
		RefineryUtilities.centerFrameOnScreen(ApplicationFrame);
		ApplicationFrame.setVisible(true);
	}

	public static void saveChartAsPNG(final JFreeChart jfreechart) {
		String chartPngFolderPath = ChartParameters.getChartPngFolderPath();
		String chartPngName = ChartParameters.getChartPngName();
		if (chartPngName == null || chartPngName.length() == 0) {
			chartPngName = ChartParameters.getTitle().replaceAll(OzConstants.BLANK,
					OzConstants.UNDERSCORE)
					+ OzConstants.UNDERSCORE
					+ DateTimeUtils.getCurrentDate(DateTimeUtils.DATE_FORMAT_yyyyMMdd_HHmmss)
					+ OzConstants.PNG_SUFFIX;
			chartPngName = chartPngName.replaceAll("\\/", "\\-");
		}
		String chartPngPath = PathUtils.getFullPath(chartPngFolderPath, chartPngName);
		logger.info(chartPngPath);
		if (chartPngPath != null && chartPngPath.length() > 0) {
			File chartPngFile = new File(chartPngPath);
			try {
				ChartUtilities.saveChartAsPNG(chartPngFile, jfreechart,
						ChartParameters.getChartWidth(), ChartParameters.getChartHeight());
			} catch (Exception ex) {
				logger.warning(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}
