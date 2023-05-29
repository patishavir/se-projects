package oz.xmlbeans.performancemonitor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlException;

import noNamespace.BoundedRangeStatisticType;
import noNamespace.CountStatisticType;
import noNamespace.NodeType;
import noNamespace.PerformanceMonitorDocument;
import noNamespace.PerformanceMonitorType;
import noNamespace.ServerType;
import noNamespace.StatType;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.excel.utils.ExcelUtils;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.infra.system.SystemUtils;

public class PerformanceMonitor {
	private static String nodeName = null;
	private static String serverName = null;
	private static final String delimiter = OzConstants.COMMA;
	private static final String LF = SystemUtils.LINE_SEPARATOR;
	private static final StringBuilder countStatistcsSb = new StringBuilder();
	private static final StringBuilder boundedRangeStatisticSb = new StringBuilder();
	private static boolean headerPrinted = false;

	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) throws XmlException, IOException {
		String xmlFilePath = args[0];
		String excelFilePath = args[1];
		File xmlFile = new File(xmlFilePath);
		PerformanceMonitorDocument performanceMonitorDocument = PerformanceMonitorDocument.Factory.parse(xmlFile);
		logger.info(xmlFile.getAbsolutePath() + " has validated: " + performanceMonitorDocument.validate());
		PerformanceMonitorType performanceMonitorType = performanceMonitorDocument.getPerformanceMonitor();
		List<NodeType> nodeTypeList = performanceMonitorType.getNodeList();
		// performanceMonitorType.
		for (NodeType nodeType1 : nodeTypeList) {
			nodeName = nodeType1.getName();
			List<ServerType> serverTypeList = nodeType1.getServerList();
			for (ServerType serverType1 : serverTypeList) {
				serverName = serverType1.getName();
				logger.info("serverName: " + serverName);
				StatType topStatType = serverType1.getStat();
				if (topStatType != null) {
					processBoundedRangeStatistic(topStatType);
					processCountStatistic(topStatType);
				}
			}
			// sb.append(LF);
		}
		logger.info(countStatistcsSb.toString());
		System.exit(0);
		String[] excelDataArray = { countStatistcsSb.toString(), boundedRangeStatisticSb.toString() };
		String[] excelSheetNamesArray = { "countStatistcs", "boundedRangeStatistics" };
		excelFilePath = excelFilePath.concat(DateTimeUtils.formatCurrentTime(DateTimeUtils.DATE_FORMAT_yyyyMMdd_HHmmss))
				.concat(OzConstants.XLS_SUFFIX);
		ExcelWorkbook.writeWorkbook(excelFilePath, excelDataArray, excelSheetNamesArray);
		ExcelUtils.openExcelWorkbook(excelFilePath);
	}

	private static void processBoundedRangeStatistic(final StatType topStatType) {
		logger.finest(topStatType.toString());
		List<StatType> statTypeList = topStatType.getStatList();
		for (StatType statType1 : statTypeList) {
			String statName = statType1.getName();
			String prefix = nodeName + delimiter + serverName + delimiter + topStatType.getName() + delimiter
					+ statName;
			List<BoundedRangeStatisticType> boundedRangeStatisticList = statType1.getBoundedRangeStatisticList();
			logger.finest(prefix + delimiter + "boundedRangeStatisticTypeList size:"
					+ String.valueOf(boundedRangeStatisticList.size()));
			for (BoundedRangeStatisticType boundedRangeStatistic1 : boundedRangeStatisticList) {
				String boundedRangeStatisticLine = formatBoundedRangeStatistic(prefix, boundedRangeStatistic1,
						delimiter);
				boundedRangeStatisticSb.append(boundedRangeStatisticLine);
				boundedRangeStatisticSb.append(LF);
				logger.info(" BBBBBBBBBBBBBBBBBBBBB " + boundedRangeStatisticLine);
			}
			processBoundedRangeStatistic(statType1);
		}
	}

	private static void processCountStatistic(final StatType topStatType) {
		List<StatType> statTypeList = topStatType.getStatList();
		for (StatType statType1 : statTypeList) {
			String statName = statType1.getName();
			String prefix = nodeName + delimiter + serverName + delimiter + topStatType.getName() + delimiter
					+ statName;
			List<CountStatisticType> countStatisticList = statType1.getCountStatisticList();
			logger.finest(
					prefix + delimiter + "countStatisticTypeList size:" + String.valueOf(countStatisticList.size()));
			for (CountStatisticType countStatistic1 : countStatisticList) {
				// processCountStatistic(statType1);
				if (countStatistic1.getCount() > 0) {
					String countStatisticLine = formatCountStatistic(prefix, countStatistic1, delimiter);
					countStatistcsSb.append(countStatisticLine);
					countStatistcsSb.append(LF);
					logger.info(" LLLLLLLLLLLLLLLLLLLLLLLLLL " + countStatisticLine);
				}
			}
			processCountStatistic(statType1);
		}
	}

	private static String formatBoundedRangeStatistic(final String prefix,
			final BoundedRangeStatisticType boundedRangeStatisticType1, final String delimiter) {
		final String[][] boundedRangeStatisticFieldNames = { { "name", "name" }, { "upperBound", "upperBound" },
				{ "highWaterMark", "highWaterMark" }, { "lowWaterMark", "lowWaterMark" },
				{ "lowerBound", "lowerBound" }, { "mean", "mean" }, { "value", "value" }

		};
		StringBuilder stringBuilder = new StringBuilder();
		if (!headerPrinted) {
			stringBuilder.append("BoundedRangeStatistic");
			stringBuilder.append(LF);
			stringBuilder.append(prefix);
			stringBuilder.append(delimiter);
			stringBuilder.append(PrintUtils.printObjectFields(boundedRangeStatisticType1,
					boundedRangeStatisticFieldNames, delimiter, PrintOption.HEADER_ONLY));
			stringBuilder.append(LF);
			headerPrinted = true;
		}
		stringBuilder.append(prefix);
		stringBuilder.append(delimiter);
		stringBuilder.append(PrintUtils.printObjectFields(boundedRangeStatisticType1, boundedRangeStatisticFieldNames,
				delimiter, PrintOption.DATA_ONLY));
		logger.finest(stringBuilder.toString());
		return stringBuilder.toString();

	}

	private static String formatCountStatistic(final String prefix, final CountStatisticType countStatisticType1,
			final String delimiter) {
		final String[][] countStatisticFieldNames = { { "name", "name" }, { "count", "count" }, { "unit", "unit" },
				{ "startTime", "startTime" }, { "lastSampleTime", "lastSampleTime" } };
		StringBuilder stringBuilder = new StringBuilder();
		if (!headerPrinted) {
			stringBuilder.append("CountStatistic");
			stringBuilder.append(LF);
			stringBuilder.append(prefix);
			stringBuilder.append(delimiter);
			//
			stringBuilder.append("name");
			stringBuilder.append(delimiter);
			stringBuilder.append("count");
			stringBuilder.append(delimiter);
			stringBuilder.append("unit");
			stringBuilder.append(delimiter);
			stringBuilder.append("startTime");
			stringBuilder.append(delimiter);
			stringBuilder.append("lastSampleTime");
			stringBuilder.append(delimiter);
			stringBuilder.append(LF);
			headerPrinted = true;
		}
		stringBuilder.append(prefix);
		stringBuilder.append(delimiter);
		stringBuilder.append(countStatisticType1.getName());
		stringBuilder.append(delimiter);
		stringBuilder.append(countStatisticType1.getCount());
		stringBuilder.append(delimiter);
		stringBuilder.append(countStatisticType1.getUnit());
		stringBuilder.append(delimiter);
		stringBuilder.append(DateTimeUtils.formatDate(countStatisticType1.getStartTime(),
				DateTimeUtils.DATE_FORMAT_yyyyMMdd_HHmmss));
		stringBuilder.append(delimiter);
		stringBuilder.append(DateTimeUtils.formatDate(countStatisticType1.getLastSampleTime(),
				DateTimeUtils.DATE_FORMAT_yyyyMMdd_HHmmss));
		stringBuilder.append(delimiter);
		logger.finest(stringBuilder.toString());
		return stringBuilder.toString();

	}
}