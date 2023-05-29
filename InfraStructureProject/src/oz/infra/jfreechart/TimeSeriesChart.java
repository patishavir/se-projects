package oz.infra.jfreechart;

import java.awt.Color;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import oz.infra.constants.OzConstants;

public final class TimeSeriesChart {
	private TimeSeriesChart() {
	}

	public static JFreeChart createChart(final String[][] chartDataStringArray,
			final String chartTitle, final String domainLabel, final String rangeLabel,
			final String[] seriesLabelsArray, final int[] columnIndexArray,
			final String dateFormatParameter) {
		String dateFormat = "dd/MM";
		if (dateFormatParameter != null) {
			dateFormat = dateFormatParameter;
		}
		XYDataset xydataset = createDataset(chartDataStringArray, seriesLabelsArray,
				columnIndexArray);
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(chartTitle, domainLabel,
				rangeLabel, xydataset, true, true, false);
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
		dateaxis.setTickLabelsVisible(true);
		dateaxis.setVerticalTickLabels(true);
		dateaxis.setDateFormatOverride(new SimpleDateFormat(dateFormat));
		return jfreechart;
	}

	private static XYDataset createDataset(final String[][] chartDataStringArray,
			final String[] seriesLabelsArray, final int[] columnIndexArray) {
		TimeSeries[] timeSeriesArray = new TimeSeries[seriesLabelsArray.length];
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		for (int timeSeriesIndex = 0; timeSeriesIndex < timeSeriesArray.length; timeSeriesIndex++) {
			timeSeriesArray[timeSeriesIndex] = new TimeSeries(seriesLabelsArray[timeSeriesIndex],
					org.jfree.data.time.Day.class);
			int columnIndex = columnIndexArray[timeSeriesIndex];
			for (int rowPtr = 0; rowPtr < chartDataStringArray.length
					&& rowPtr < OzConstants.INT_30; rowPtr++) {
				int day = Integer.parseInt(chartDataStringArray[rowPtr][0].substring(0, 2));
				int month = Integer.parseInt(chartDataStringArray[rowPtr][0].substring(
						OzConstants.INT_3, OzConstants.INT_5));
				int year = Integer.parseInt(chartDataStringArray[rowPtr][0].substring(
						OzConstants.INT_6, OzConstants.INT_10));
				Double doubleNumber = 0.0d;
				if (chartDataStringArray[rowPtr][columnIndex] != null
						&& chartDataStringArray[rowPtr][columnIndex].length() > 0) {
					doubleNumber = Double.parseDouble(chartDataStringArray[rowPtr][columnIndex]);
				}
				timeSeriesArray[timeSeriesIndex].add(new Day(day, month, year), doubleNumber);
			}
			timeseriescollection.addSeries(timeSeriesArray[timeSeriesIndex]);
		}
		return timeseriescollection;
	}
}
