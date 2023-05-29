package oz.reports.sar;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.font.FontUtils;
import oz.infra.io.FileUtils;
import oz.infra.itext.ItextUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.system.SystemUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class TimeSeriesChartReport {
	private static Logger logger = JulUtils.getLogger();
	private static String reportDate = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath = args[0];
		String targetFilePath = null;
		if (args.length > 1) {
			targetFilePath = args[1];
		}
		Set<ChartDataObject> chartDataObjectsSet = getChartDataObjectsSet(filePath);
		XYDataset xydataset = createXYDataset(chartDataObjectsSet);
		String ddmmyyReportDate = reportDate.substring(3, 5) + OzConstants.SLASH
				+ reportDate.substring(0, 2) + OzConstants.SLASH + reportDate.substring(6, 8);
		String chartTitle = " מערכת סניפית - ניצול משאבי מעבד לתאריך " + ddmmyyReportDate;
		JFreeChart jFreeChart = createChart(xydataset, chartTitle);
		File targetFile = new File(targetFilePath);
		int width = 900;
		int height = 600;
		try {
			ChartUtilities.saveChartAsPNG(targetFile, jFreeChart, width, height);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		// SwingUtils.drawImage(jFreeChart.createBufferedImage(800, 600));
		// buildPdfFile(jFreeChart, targetFilePath);
	}

	private static Set<ChartDataObject> getChartDataObjectsSet(final String inputsFolderPath) {
		Set<ChartDataObject> chartDataObjectsSet = new HashSet<ChartDataObject>();
		File inputsFolder = new File(inputsFolderPath);
		if (!inputsFolder.exists()) {
			SystemUtils.printMessageAndExit(inputsFolderPath
					+ " folder not found.\n Processing has been terminated", -1);
		}
		if (!inputsFolder.isDirectory()) {
			SystemUtils.printMessageAndExit(inputsFolderPath
					+ " is not a folder.\n Processing has been terminated", -1);
		}
		File[] fileList = inputsFolder.listFiles();
		for (File file1 : fileList) {
			logger.info("Processing file: " + file1.getAbsolutePath());
			if (file1.isFile()) {
				ChartDataObject chartDataObject = buildChartDataObject(file1.getAbsolutePath());
				chartDataObjectsSet.add(chartDataObject);
			}
		}
		logger.finest("Set size: " + String.valueOf(chartDataObjectsSet.size()));
		return chartDataObjectsSet;
	}

	private static ChartDataObject buildChartDataObject(final String filePath) {
		String[] chartData = null;
		String[] rawChartData = FileUtils.readTextFile2Array(filePath);
		logger.finest("row count: " + String.valueOf(rawChartData.length));
		// ArrayUtils.printStringArray(chartData, "\n", "Chart data");
		chartData = ArrayUtils.selectArrayRowsByMinimalLength(rawChartData, 1);
		String[] firstLine = chartData[0].split(RegexpUtils.REGEXP_WHITE_SPACE);
		String serverName = firstLine[1];
		String myReportDate = firstLine[firstLine.length - 1];
		logger.info(serverName + myReportDate);
		if (reportDate == null) {
			reportDate = myReportDate;
		} else {
			if (!reportDate.equals(myReportDate)) {
				SystemUtils.printMessageAndExit(
						"Input files dates do not match.\nProcessing has been terminated.", -1);
			}
		}
		String titles = chartData[2];
		logger.info("titles " + titles);
		String[] titlesArray = titles.split(RegexpUtils.REGEXP_WHITE_SPACE);
		// ArrayUtils.printStringArray(titlesArray, "\n", "row data");
		chartData = ArrayUtils.selectArrayRowsByRegExpression(chartData, RegexpUtils.REGEXP_ALPHA,
				false);
		chartData = ArrayUtils.selectArrayRowsByRegExpression(chartData, RegexpUtils.REGEXP_DIGIT
				+ "|\\.|:", true);
		// ArrayUtils.printStringArray(chartData, "\n", "Chart data");
		String[][] chartDataArray = new String[chartData.length][titlesArray.length];
		for (int rowIndex = 0; rowIndex < chartData.length; rowIndex++) {
			chartDataArray[rowIndex] = chartData[rowIndex].split(RegexpUtils.REGEXP_WHITE_SPACE);
			logger.finest("chartData[rowIndex].length()"
					+ String.valueOf(chartData[rowIndex].length()) + OzConstants.TAB
					+ "chartDataArray[rowIndex]" + String.valueOf(chartDataArray[rowIndex].length));
		}
		logger.info("chartDataArray.length: " + String.valueOf(chartDataArray.length));
		logger.info("chartDataArray[0].length: " + String.valueOf(chartDataArray[0].length));
		// ArrayUtils.printStringArray(chartDataArray);
		ChartDataObject chartDataObject = new ChartDataObject();
		chartDataObject.setChartDataArray(chartDataArray);
		chartDataObject.setChartLabel(serverName + " CPU utilization (%)");
		return chartDataObject;
	}

	private static XYDataset createXYDataset(final Set<ChartDataObject> chartDataObjectsSet) {
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		for (ChartDataObject chartDataObject : chartDataObjectsSet) {
			TimeSeries timeseries = new TimeSeries(chartDataObject.getChartLabel(),
					org.jfree.data.time.Minute.class);
			String[][] chartDataArray = chartDataObject.getChartDataArray();
			for (int i = 0; i < chartDataArray.length; i++) {
				DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				try {
					logger.info(chartDataArray[i][0]);
					Date date = formatter.parse(chartDataArray[i][0]);
					timeseries.add(new Minute(date),
							Double.parseDouble(chartDataArray[i][chartDataArray[i].length - 1]));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			timeseriescollection.addSeries(timeseries);
		}
		return timeseriescollection;
	}

	private static JFreeChart createChart(final XYDataset xydataset, final String chartTitle) {
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(chartTitle, "Hour",
				"CPU untilization (%) ", xydataset, true, false, false);
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
			xylineandshaperenderer.setBaseShapesVisible(false);
		}
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("HH:00"));
		dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.HOUR, 1));
		dateaxis.setTickLabelFont(FontUtils.ARIAL_PLAIN_12);
		ValueAxis valueAxis = (NumberAxis) xyplot.getRangeAxis();
		valueAxis.setTickLabelFont(FontUtils.ARIAL_PLAIN_12);
		return jfreechart;
	}

	private static void buildPdfFile(final JFreeChart jFreeChart, final String pdfReportFilePath) {
		logger.info("Createing " + pdfReportFilePath);
		try {

			// step 2:
			// we create a writer
			Document pdfDocument = new Document(PageSize.A4.rotate());
			PdfWriter pdfWriter = PdfWriter.getInstance(
			// that listens to the document
					pdfDocument,
					// and directs a PDF-stream to a file
					new FileOutputStream(pdfReportFilePath));
			pdfDocument.addAuthor("Oded Z");
			pdfDocument.addCreator("Oded Z");
			pdfDocument.addSubject("Performance chart");
			pdfDocument.setPageSize(new Rectangle(1000, 800));
			pdfDocument.open();
			int chartWidth = 800;
			int chartHeight = 600;
			int chartX = 100;
			int chartY = 100;
			pdfDocument.newPage();
			ItextUtils.addJFreeChartToPdf(jFreeChart, chartWidth, chartHeight, pdfWriter, chartX,
					chartY);
			pdfDocument.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
