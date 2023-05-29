package oz.infra.excel.workbook;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class ExcelWorkbook {
	public static enum DirectiveEnum {
		Formula, FormatAsRichTextString, Header01, ColumnHeaders, RowBackGroundColor
	}

	private static final String COLUMN_DELIMITER = OzConstants.COMMA;
	private static final String LINE_DELIMITER = OzConstants.LINE_FEED;
	public static final String ACTUAL_CONTENT_DELIMITER = OzConstants.NUMBER_SIGN;
	private static final String DIRECTIVE_DELIMITER = "@#$";
	public static final String HEADER01_DIRECTIVE = DIRECTIVE_DELIMITER + DirectiveEnum.Header01.toString()
			+ DIRECTIVE_DELIMITER;
	public static final String COLUMN_HEADERS_DIRECTIVE = DIRECTIVE_DELIMITER + DirectiveEnum.ColumnHeaders.toString()
			+ DIRECTIVE_DELIMITER;
	public static final String FORMULA_DIRECTIVE = DIRECTIVE_DELIMITER + DirectiveEnum.Formula.toString()
			+ DIRECTIVE_DELIMITER;
	public static final String FORMAT_AS_RICH_TEXT_DIRECTIVE = DIRECTIVE_DELIMITER
			+ DirectiveEnum.FormatAsRichTextString.toString() + DIRECTIVE_DELIMITER;
	public static final String ROW_BACKGROUND_COLOR_DIRECTIVE = DIRECTIVE_DELIMITER
			+ DirectiveEnum.RowBackGroundColor.toString() + DIRECTIVE_DELIMITER;
	private static Logger logger = JulUtils.getLogger();

	public static void writeWorkbook(final String filePath, final ResultSet resultSet, final String sheetName) {
		logger.finest("write workbook " + filePath + " sheet: " + sheetName + " ...");
		ExcelWorkbook excelWorkbook = new ExcelWorkbook(filePath);
		excelWorkbook.addSheet(resultSet, sheetName);
		excelWorkbook.closeWorkBook();
	}

	public static void writeWorkbook(final String filePath, final String contents, final String sheetName) {
		String[] contentsArray = { contents };
		String[] sheetNamesArray = { sheetName };
		writeWorkbook(filePath, contentsArray, sheetNamesArray);
	}

	public static void writeWorkbook(final String filePath, final String[] contentsArray,
			final String[] sheetNamesArray) {
		logger.finest("write workbook 1 " + filePath + " ...");
		ExcelWorkbook excelWorkbook = new ExcelWorkbook(filePath);
		logger.finest("write workbook 2 " + filePath + " ...");
		for (int i = 0; i < contentsArray.length; i++) {
			logger.info("processing sheet: " + sheetNamesArray[i]);
			String[][] sheetContentsArray = ArrayUtils.string2Dim2Array(contentsArray[i], LINE_DELIMITER,
					COLUMN_DELIMITER);
			excelWorkbook.addSheet(sheetContentsArray, sheetNamesArray[i]);
		}
		logger.finest("write workbook 3 " + filePath + " ...");
		excelWorkbook.closeWorkBook();
	}

	public static void writeWorkbook(final String filePath, final String[][] contentsArray, final String sheetName) {
		logger.finest("write workbook " + filePath + " sheet: " + sheetName + " ...");
		ExcelWorkbook excelWorkbook = new ExcelWorkbook(filePath);
		excelWorkbook.addSheet(contentsArray, sheetName);
		excelWorkbook.closeWorkBook();
	}

	private String filePath = null;

	private Workbook wb = new XSSFWorkbook();
	private Sheet sheet = null;
	private CellStyle defaultStyle = null;
	private CellStyle headerStyle = null;
	private CellStyle cellStyleAlignCenter = null;
	private CellStyle twoDecimalsCellSyle = null;

	private CreationHelper creationHelper = null;

	private int rowNumber = Integer.MIN_VALUE;

	private int maxColumn = Integer.MIN_VALUE;

	private int sheetCount = 0;

	private StopWatch stopWatch = null;

	public ExcelWorkbook(final String filePath) {
		stopWatch = new StopWatch();
		this.filePath = filePath;
		if (filePath.toLowerCase().endsWith(OzConstants.XLS_SUFFIX)) {
			wb = new HSSFWorkbook();
		}
		buildCellStyles();
	}

	private void addRow(final String[] cellContents) {
		final String commaVariable = "%COMMA%";
		Row row = sheet.createRow(rowNumber);
		CellStyle rowStyle = defaultStyle;
		for (int cellnum = 0; cellnum < cellContents.length; cellnum++) {
			Cell cell = row.createCell(cellnum);
			cell.setCellStyle(rowStyle);
			String currentCellContents = cellContents[cellnum];
			if (currentCellContents != null) {
				currentCellContents = currentCellContents.trim();
				if (currentCellContents.startsWith(DIRECTIVE_DELIMITER)) {
					int closingDelimiterIndex = currentCellContents.indexOf(DIRECTIVE_DELIMITER, 1);
					String directive = currentCellContents.substring(DIRECTIVE_DELIMITER.length(),
							closingDelimiterIndex);
					String actualContents = currentCellContents
							.substring(closingDelimiterIndex + DIRECTIVE_DELIMITER.length());
					actualContents = actualContents.replaceAll(commaVariable, OzConstants.COMMA);
					logger.finest("directive: " + directive + " directiveContents: " + actualContents);
					DirectiveEnum directiveEnum = DirectiveEnum.valueOf(directive);
					switch (directiveEnum) {
					case Formula:
						cell.setCellFormula(actualContents);
						break;
					case FormatAsRichTextString:
						cell.setCellValue(creationHelper.createRichTextString(actualContents));
						break;
					case Header01:
						if (maxColumn > 0) {
							CellRangeAddress headerCellRangeAddress = new CellRangeAddress(rowNumber, rowNumber, 0,
									maxColumn);
							sheet.addMergedRegion(headerCellRangeAddress);
						}
						Cell cell0 = row.createCell(0);
						setCellValue(cell0, actualContents);
						headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
						cell0.setCellStyle(headerStyle);
						break;
					case ColumnHeaders:
						setCellValue(cell, actualContents);
						rowStyle = headerStyle;
						cell.setCellStyle(headerStyle);
						break;
					case RowBackGroundColor:
						CellStyle foregroundColorStyle = wb.createCellStyle();
						foregroundColorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
						String[] actualContentsArray = actualContents.split(ACTUAL_CONTENT_DELIMITER);
						logger.finest(actualContentsArray[0] + " " + String.valueOf(actualContentsArray.length)
								+ " >>>>>>>>>>>>>>>");
						foregroundColorStyle.setFillForegroundColor(Short.parseShort(actualContentsArray[0]));
						if (actualContentsArray.length == 2) {
							setCellValue(cell, actualContentsArray[1]);
						}
						cell.setCellStyle(foregroundColorStyle);
						break;
					}
				} else {
					setCellValue(cell, currentCellContents);
				}
			}
		}
	}

	private void addSheet(final ResultSet resultSet, final String sheetName) {
		sheetCount += 1;
		sheet = wb.createSheet(sheetName);
		rowNumber = 0;
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			maxColumn = metaData.getColumnCount();
			String[] columnNames = DBMetaDataUtils.getResultSetColumnNames(resultSet);
			addRow(columnNames);
			String[] row = new String[maxColumn];
			while (resultSet.next()) {
				for (int i = 0; i < maxColumn; i++) {
					row[i] = resultSet.getString(i + 1);
				}
				rowNumber++;
				addRow(row);
			}
			logger.finest("add sheet " + sheetName + " 2 ...");
			autoSizeColums(0, maxColumn);
			logger.finest("add sheet " + sheetName + " 3 ...");
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	private void addSheet(final String[][] contents, final String sheetName) {
		sheetCount += 1;
		sheet = wb.createSheet(sheetName);
		maxColumn = contents[0].length - 1;
		for (rowNumber = 0; rowNumber < contents.length; rowNumber++) {
			if ((contents[rowNumber].length - 1) > maxColumn) {
				maxColumn = contents[rowNumber].length - 1;
			}
			addRow(contents[rowNumber]);
		}
		logger.finest("add sheet " + sheetName + " 2 ...");
		autoSizeColums(0, maxColumn);
		logger.finest("add sheet " + sheetName + " 3 ...");
	}

	private void autoSizeColums(final int fromColumn, final int toColumn) {
		StopWatch stopWatch = new StopWatch();
		for (int i = fromColumn; i <= toColumn; i++) {
			sheet.autoSizeColumn(i);
		}
		stopWatch.logElapsedTimeMessage("autoSizeColums completed in");
	}

	private void buildCellStyles() {
		short defaultFontHeight = OzConstants.INT_10;
		Font defaultFont = wb.createFont();
		defaultFont.setFontHeightInPoints(defaultFontHeight);
		defaultFont.setFontName("Arial");
		defaultStyle = wb.createCellStyle();
		defaultStyle.setFont(defaultFont);
		//
		short headerFontHeight = OzConstants.INT_12;
		Font headerFont = wb.createFont();
		headerFont.setFontHeightInPoints(headerFontHeight);
		headerFont.setColor(IndexedColors.BLUE.getIndex());
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);
		//
		twoDecimalsCellSyle = wb.createCellStyle();
		DataFormat format = wb.createDataFormat();
		twoDecimalsCellSyle.setDataFormat(format.getFormat("0.00"));
		cellStyleAlignCenter = wb.createCellStyle();
		cellStyleAlignCenter.setAlignment(CellStyle.ALIGN_CENTER);
		creationHelper = wb.getCreationHelper();
		//
	}

	private void closeWorkBook() {
		logger.finest("Close workbook " + filePath + " ...");
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			wb.write(out);
			out.close();
			if (sheetCount == 1) {
				String message = filePath + " has been created successfully ! " + String.valueOf(rowNumber) + " rows "
						+ String.valueOf(maxColumn + 1) + " columns. Processing time:";
				logger.info(stopWatch.appendElapsedTimeToMessage(message));
			} else {
				String message = filePath + " has been created successfully ! " + String.valueOf(sheetCount)
						+ " sheets. Processing time:";
				logger.info(stopWatch.appendElapsedTimeToMessage(message));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}

	private void setCellValue(final Cell cell, final String currentCellContents) {
		if (StringUtils.isFloatingPointNumber(currentCellContents)) {
			cell.setCellValue(Double.parseDouble(currentCellContents));
			if (currentCellContents.indexOf(OzConstants.DOT) > OzConstants.STRING_NOT_FOUND) {
				cell.setCellStyle(twoDecimalsCellSyle);
			}
		} else {
			cell.setCellValue(currentCellContents);
		}
	}
}
