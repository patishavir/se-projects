package oz.infra.excel.workbook.test;

import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.IndexedColors;

import oz.infra.excel.utils.ExcelUtils;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.logging.jul.JulUtils;

public class TestExcelWorkbook {
	private static final String Header01 = "@#$Header01@#$";
	private static final String HeaderRow = "@#$HeaderRow@#$";
	private static final String Formula = "@#$Formula@#$SUM(A1:A3)";
	private static final String Formula2 = "@#$Formula@#$COUNTIF(B1:B3%COMMA%\"prod\")";
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 testExcelWorkbook1();
		// printIndex();
		// testChangeBgColorOnValueChange();
		// testWriteWorkbook();
		// testWriteWorkbook2();
		// testWriteWorkbook3();
		// testWriteWorkbook4();
		// testWriteWorkbook5();
	}

	private static void printIndex() {
		logger.info("AQUA: " + String.valueOf(IndexedColors.AQUA.getIndex()));
		logger.info("BLUE_GREY: " + String.valueOf(IndexedColors.BLUE_GREY.getIndex()));
		logger.info("GREY_25_PERCENT: " + String.valueOf(IndexedColors.GREY_25_PERCENT.getIndex()));
		logger.info("GOLD: " + String.valueOf(IndexedColors.GOLD.getIndex()));
		logger.info("YELLOW: " + String.valueOf(IndexedColors.YELLOW.getIndex()));
	}

	private static void testChangeBgColorOnValueChange() {
		String xlsxFilePath = "c:\\temp\\2.xlsx";
		String xlsFilePath = "c:\\temp\\2.xls";
		String[] sheetNamesArray = { "daf 01" };
		String csvFilePath = "C:\\oj\\projects\\excelProject\\data\\2.csv";
		String contents = ExcelUtils
				.changeBgColorOnValueChange(
						csvFilePath,
						1,
						"BLUE,YELLOW,WHITE,GREEN,GREY_25_PERCENT,LIME,AQUA,LIGHT_GREEN,TAN,LIGHT_ORANGE,LIGHT_CORNFLOWER_BLUE,PALE_BLUE,LEMON_CHIFFON,PINK,ROSE,LIGHT_TURQUOISE,LIGHT_YELLOW,LAVENDER,GOLD,INDIGO,TEAL,SKY_BLUE,SEA_GREEN");
		ExcelWorkbook.writeWorkbook(xlsFilePath, contents, "sheetName 1");
	}

	private static void testExcelWorkbook1() {
		String xlsxFilePath = "c:\\temp\\11.xlsx";
		String xlsFilePath = "c:\\temp\\11.xls";
		logger.info(Formula2);
		String[] contentsArray = {
				ExcelWorkbook.ROW_BACKGROUND_COLOR_DIRECTIVE + "22#ZZZZZ,2,3\n2,prod,6\n3,prod,9\n",
				"1,2,3\n2,prod,6\n3,prod,9\n" + Formula + "\n" + Formula2,
				ExcelWorkbook.HEADER01_DIRECTIVE
						+ "sd123456\n*** header ***\n"
						+ ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE
						+ "1111,yyyy,zzzz\n111,222,333\n2222,qqqqqqq,aaaaaaa,zzzzz,wwww,ssss,xxxxx\n333,+++header2 +++\nwwwwwww,uuuuu,l,ooooooo,uuuuu,ttttt,rrrrr,eeeeeeee",
				"098765\n*** header ***\n1111,yyyy,zzzz\n111,222,333\nqqqqqqq,aaaaaaa,zzzzz,wwww,ssss,xxxxx\n"
						+ ExcelWorkbook.HEADER01_DIRECTIVE
						+ "+++header2 +++\nwwwwwww,uuuuu,l\n000,999,888,777,666,555qqaazzwwssx,aaaaaaa,bbbbb,ccccc" };
		String[] sheetNamesArray = { "daf 11", "daf 12", "daf 13", "daf 14" };
		ExcelWorkbook.writeWorkbook(xlsxFilePath, contentsArray, sheetNamesArray);
		ExcelWorkbook.writeWorkbook(xlsFilePath, contentsArray, sheetNamesArray);
	}

	private static void testWriteWorkbook() {
		String xlsxFilePath = "c:\\temp\\1.xls";
		String[] contentsArray = { "111,222, ן¿½ן¿½ן¿½ן¿½ ן¿½ן¿½ן¿½ן¿½ן¿½ ,1,777,ן¿½ן¿½ן¿½ן¿½ ן¿½ן¿½ן¿½ן¿½ן¿½" };
		String[] sheetNamesArray = { "sheeeet" };
		logger.info("starting ... xlsxFilePath: " + xlsxFilePath);
		ExcelWorkbook.writeWorkbook(xlsxFilePath, contentsArray, sheetNamesArray);

	}

	private static void testWriteWorkbook2() {
		String xlsxFilePath = "c:\\temp\\2.xls";
		String[] contentsArray = { "\n111,222, AAA ,1,777,BBB\n,kkk,,, ASDFGHJKL\n\n,,,a,b,c,d,e,f,g,h,i,j" };
		String[] sheetNamesArray = { "sheeeet" };
		logger.info("starting ... xlsxFilePath: " + xlsxFilePath);
		ExcelWorkbook.writeWorkbook(xlsxFilePath, contentsArray, sheetNamesArray);

	}

	private static void testWriteWorkbook3() {
		String xlsxFilePath = "c:\\temp\\3.xls";
		String[][] contentsArray = { { null, "1", "2", "3", "4", "5" },
				{ "1", null, "3", "4", "5" }, { "1", "2", null, "4", "5" },
				{ "1", "2", "3", null, "5", null, "7" } };
		String sheetNamesArray = "sheeeet";
		logger.info("starting ... xlsxFilePath: " + xlsxFilePath);
		ExcelWorkbook.writeWorkbook(xlsxFilePath, contentsArray, sheetNamesArray);

	}

	private static void testWriteWorkbook4() {
		String xlsxFilePath = "c:\\temp\\4.xls";
		String[][] contentsArray = { { "OP_SEX", "OP_SEX_DESC" }, { "*", "1" },
				{ "1", "*", null, "4", "5" }, { "1", "׳‘׳‘׳•׳�", "3", null, "5", null, "7" } };
		String sheetNamesArray = "sheeeet";
		logger.info("starting ... xlsxFilePath: " + xlsxFilePath);
		ExcelWorkbook.writeWorkbook(xlsxFilePath, contentsArray, sheetNamesArray);

	}

	private static void testWriteWorkbook5() {
		String xlsxFilePath = "c:\\temp\\5.xls";
		String[][] contentsArray = {
				{ ExcelWorkbook.HEADER01_DIRECTIVE + "+++++ header1 +++++" },
				{ ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE + "OP_SEX",
						ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE + "OP_SEX_DESC" },
				{ "*", "1.123456" }, { "1", "*", null, "4", "5" },
				{ "1", "׳‘׳‘׳•׳�", "3", null, "5", null, "7.0", "10.2", "17.123456789" } };
		String sheetNamesArray = "sheeeet";
		logger.info("starting ... xlsxFilePath: " + xlsxFilePath);
		ExcelWorkbook.writeWorkbook(xlsxFilePath, contentsArray, sheetNamesArray);

	}
}
