package oz.zel.file;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.system.SystemUtils;
import oz.zel.ZelParameters;

public class Csv2XmlConversion {
	private static HashMap<String, String> nameConversionHashMap = new HashMap<String, String>();
	private static Logger logger = JulUtils.getLogger();

	public static void processFile() {
		buildNameConversionHashMap();
		performCsvFileProcessing();
	}

	public static void buildNameConversionHashMap() {
		final int matrixColumnNumber = 2;
		String[] nameConversionRecords = FileUtils
				.readTextFile2Array(ZelParameters.structureCsvFilePath);
		for (int i = 0; i < nameConversionRecords.length; i++) {
			String[] nameConversionRecord = nameConversionRecords[i].split(OzConstants.COMMA);
			String key = nameConversionRecord[0].trim();
			String value = nameConversionRecord[matrixColumnNumber];
			logger.info("key: " + key + " value: " + value + " key length: "
					+ String.valueOf(key.length()));
			nameConversionHashMap.put(key, value);
		}
		MapUtils.printMap(nameConversionHashMap, Level.INFO);
	}

	public static void performCsvFileProcessing() {
		StringBuilder sb = new StringBuilder();
		String[] zelRecords = FileUtils.readTextFile2Array(ZelParameters.dataCsvFilePath);
		String[] zelFieldsNames = zelRecords[0].split(OzConstants.COMMA);
		String[] zelConvertedFieldNames = new String[zelFieldsNames.length];
		for (int i = 0; i < zelFieldsNames.length; i++) {
			zelConvertedFieldNames[i] = nameConversionHashMap.get(zelFieldsNames[i]);
			String key = zelFieldsNames[i];
			String value = zelConvertedFieldNames[i];
			logger.info("key: " + key + " value: " + value + " key length: "
					+ String.valueOf(key.length()));
			if (zelConvertedFieldNames[i] == null || zelConvertedFieldNames[i].trim().length() == 0) {
				logger.warning("******* Missing field name for: " + zelFieldsNames[i]);
			}
		}
		int recordIndex = 1;
		int records2Process = zelRecords.length;
		ArrayUtils.printArray(zelFieldsNames, Level.FINE);
		for (recordIndex = 1; recordIndex < records2Process; recordIndex++) {
			sb.append(ZelParameters.RECORD_PREFIX);
			String[] zelFields = zelRecords[recordIndex].split(OzConstants.COMMA);
			for (int fieldIndex = 0; fieldIndex < zelFields.length; fieldIndex++) {
				sb.append("<" + zelConvertedFieldNames[fieldIndex] + ">");
				sb.append(zelFields[fieldIndex]);
				sb.append("</" + zelConvertedFieldNames[fieldIndex] + ">");
				sb.append(SystemUtils.LINE_SEPARATOR);
			}
			sb.append(ZelParameters.RECORD_SUFFIX);
		}
		sb.append(ZelParameters.XML_TAIL);
		if (logger.isLoggable(Level.FINE)) {
			logger.info(sb.toString());
		}
		int recordsProcessed = recordIndex - 1;
		// add XML Header
		sb.insert(0, getXmlHeader(recordsProcessed));
		FileUtils.writeFile(ZelParameters.getXmlfilepath(), sb.toString());
		logger.info(String.valueOf(recordsProcessed) + " records have been processed.");
		logger.info(String.valueOf(zelFieldsNames.length)
				+ " fields per record have been processed.");
	}

	private static String getXmlHeader(final int recordsProcessed) {
		StringBuilder sb = new StringBuilder(ZelParameters.XML_HEADER1);
		sb.append(String.valueOf(recordsProcessed));
		sb.append(ZelParameters.XML_HEADER2);
		return sb.toString();
	}

}
