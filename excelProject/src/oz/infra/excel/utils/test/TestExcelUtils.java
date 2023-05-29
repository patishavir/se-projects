package oz.infra.excel.utils.test;

import java.util.logging.Logger;

import oz.infra.excel.utils.ExcelUtils;
import oz.infra.logging.jul.JulUtils;

public class TestExcelUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// ExcelUtils.openExcelWorkbook("C:\\temp\\1.xls");
		// logger.info(ExcelUtils.getExcelExeFilePath());
		// ExcelUtils.convert2ColumnIndex("a");
		// ExcelUtils.convert2ColumnIndex("aa");
		// ExcelUtils.convert2ColumnIndex("aaa");
		// ExcelUtils.replaceAllAndTrimColumn("C:\\oj\\projects\\excelProject\\data\\1.xls",
		// "c",
		// "\"", OzConstants.EMPTY_STRING, OzConstants.UNDERSCORE, "~$");
		testIsExcelFile("c:\\temp\\1.xls");
		testIsExcelFile("c:\\temp\\xxx.xlsx");
		testIsExcelFile("c:\\temp\\xxx.xlsxx");
		testIsExcelFile("c:\\temp\\1.1xls");

	}

	private static void testIsExcelFile(final String filePath) {
		logger.info(filePath + " " + String.valueOf(ExcelUtils.isExcelFile(filePath)));
	}
}
