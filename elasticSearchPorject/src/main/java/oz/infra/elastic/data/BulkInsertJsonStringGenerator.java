package oz.infra.elastic.data;

import static oz.infra.constants.OzConstants.COMMA;
import static oz.infra.constants.OzConstants.DOUBLE_QUOTE;

import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

import oz.infra.elastic.data.classes.Employee;
import oz.infra.json.jackson.JacksonUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.system.SystemUtils;

public class BulkInsertJsonStringGenerator {

	private static final String INDEX = "employees";
	private static int id = 99999;
	private static int numberOfObjects2Generate = 10000;
	private static final String BULK_OPERATION_INDEX1 = "{\"index\": { ";
	private static final String BULK_OPERATION_INDEX2 = "\"_index\" : ";
	private static final String BULK_OPERATION_INDEX3 = " \"_id\": ";
	private static final String BULK_OPERATION_INDEX4 = "}}".concat(SystemUtils.LINE_SEPARATOR);
	private static String outputFilePath = "D:\\oj\\elk\\data\\employees.json";
	// private static final String BULK_OPERATION_INDEX1 =
	// ""{"index":{"_id":"""1"}};SystemUtils.LINE_SEPARATOR

	private static Logger logger = JulUtils.getLogger();

	private static String getBulkInsertJsonString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numberOfObjects2Generate; i++) {
			Employee employee = EmployeeGenerator.getEmployee(++id);
			sb.append(BULK_OPERATION_INDEX1);
			sb.append(BULK_OPERATION_INDEX2 + DOUBLE_QUOTE + INDEX + DOUBLE_QUOTE + COMMA);
			sb.append(BULK_OPERATION_INDEX3 + DOUBLE_QUOTE + String.valueOf(employee.getId()) + DOUBLE_QUOTE);
			sb.append(BULK_OPERATION_INDEX4);
			String jsonString = JacksonUtils.javaObject2Json(employee);
			sb.append(jsonString);
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		String bulkInsertJsonString = sb.toString();
		logger.finest(SystemUtils.LINE_SEPARATOR.concat(bulkInsertJsonString));
		return bulkInsertJsonString;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String bulkInsertJsonString = getBulkInsertJsonString();
		NioUtils.writeString2File(outputFilePath, bulkInsertJsonString, StandardOpenOption.CREATE);
	}
}