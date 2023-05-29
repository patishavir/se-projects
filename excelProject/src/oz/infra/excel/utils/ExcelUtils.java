package oz.infra.excel.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import oz.infra.constants.OzConstants;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;

public class ExcelUtils {

	public enum ExcelUtilsEnum {
		changeBgColorOnValueChange
	};

	private static Logger logger = JulUtils.getLogger();

	public static String changeBgColorOnValueChange(final String csvFilePath, final int column, final String colorString) {
		String[] csvLines = FileUtils.readTextFile2Array(csvFilePath);
		return changeBgColorOnValueChange(csvLines, column, colorString);
	}

	public static String changeBgColorOnValueChange(final String[] csvLines, final int column, final String colorList) {
		final String[] defaultColorArray = { "GREY_25_PERCENT", "WHITE" };
		String[] colorArray = null;
		if (colorList == null) {
			colorArray = defaultColorArray;
		} else {
			colorArray = colorList.split(OzConstants.COMMA);
		}
		String[] linePrefixArray = new String[colorArray.length];
		for (int i = 0; i < linePrefixArray.length; i++) {
			linePrefixArray[i] = ExcelWorkbook.ROW_BACKGROUND_COLOR_DIRECTIVE + String.valueOf(getColorIndex(colorArray[i])) + "#";
		}
		String currentValue = null;
		int linePrefixArrayPtr = 0;
		for (int i = 0; csvLines != null && i < csvLines.length; i++) {
			if (csvLines[i].length() > 0) {
				String[] csvLine = csvLines[i].split(OzConstants.COMMA);
				if (currentValue == null) {
					currentValue = csvLine[column];
				}
				if (!csvLine[column].equalsIgnoreCase(currentValue)) {
					linePrefixArrayPtr++;

					if (linePrefixArrayPtr >= linePrefixArray.length) {
						linePrefixArrayPtr = 0;
					}
				}
				logger.finest("linePrefixArrayPtr: " + String.valueOf(linePrefixArrayPtr));
				csvLines[i] = linePrefixArray[linePrefixArrayPtr].concat(csvLines[i]);
				currentValue = csvLine[column];
			}
		}
		String resultString = StringUtils.stringArray2String(csvLines, OzConstants.LINE_FEED);
		logger.finest(resultString);
		return resultString;
	}

	public static int convert2ColumnIndex(String columnName) {
		columnName = columnName.toUpperCase();
		int columnIndex = 0;
		for (int i = 0; i < columnName.length(); i++) {
			int delta = columnName.charAt(i) - 64;
			columnIndex = columnIndex * 26 + delta;
		}
		columnIndex--;
		logger.finest("columnName: " + columnName + " columnIdex: " + String.valueOf(columnIndex));
		return columnIndex;
	}

	public static short getColorIndex(final String color) {
		return IndexedColors.valueOf(color).getIndex();
	}

	public static String getExcelExeFilePath() {
		String[] excelExePathArray = { "C:\\Program Files\\Microsoft Office\\OFFICE12\\EXCEL.EXE",
				"C:\\Program Files\\Microsoft Office\\Office12\\XLVIEW.EXE", "C:\\Program Files\\Microsoft Office\\Office14\\EXCEL.EXE",
				"C:\\Program Files (x86)\\Microsoft Office\\Office14\\EXCEL.EXE", "C:\\Program Files (x86)\\Microsoft Office\\Office16\\EXCEL.EXE" };
		for (String excelExeFilePath : excelExePathArray) {
			if (FileUtils.isFileExists(excelExeFilePath)) {
				return excelExeFilePath;
			}
		}
		logger.warning("Excel.exe does not exist!");
		return null;
	}

	public static Workbook getWorkBook(final String filePath) {
		Workbook wb = null;
		logger.info("Processing " + filePath + " ...");
		File file = new File(filePath);
		if (file.isFile() && (isExcelFile(filePath))) {
			try {
				InputStream inp = new FileInputStream(filePath);
				wb = WorkbookFactory.create(inp);
			} catch (Exception ex) {
				logger.info(ExceptionUtils.printMessageAndStackTrace(ex));
			}
		}
		return wb;
	}

	public static boolean isExcelFile(final String filePath) {
		boolean returnValue = false;
		String fileType = OzConstants.DOT + PathUtils.getFileExtension(filePath);
		if (fileType.equalsIgnoreCase(OzConstants.XLS_SUFFIX) || fileType.equalsIgnoreCase(OzConstants.XLSX_SUFFIX)) {
			returnValue = true;
		}
		return returnValue;
	}

	public static int openExcelWorkbook(final String excelWorkbookFilePath) {
		logger.info("opening " + excelWorkbookFilePath);
		String excelExeFilePath = getExcelExeFilePath();
		int returnCode = -1;
		if (excelExeFilePath == null) {
			logger.info(excelExeFilePath + " does not exist!");
		} else {
			File excelWorkbookFile = new File(excelWorkbookFilePath);
			if (excelWorkbookFile.exists()) {
				String[] parametersArray = { excelExeFilePath, excelWorkbookFilePath };
				returnCode = SystemCommandExecutorRunner.run(parametersArray).getReturnCode();
			} else {
				logger.info(excelWorkbookFilePath + " does not exist!");
			}
		}
		logger.finest("returning ... return code: " + String.valueOf(returnCode));
		return returnCode;
	}

	public static void replaceAllAndTrimColumn(final Workbook workbook, final String columnName, final String string2Replace,
			final String replacement) {
		Sheet sheet = workbook.getSheetAt(0);
		int columIndex = convert2ColumnIndex(columnName);
		for (Row row : sheet) {
			Cell cell = row.getCell(columIndex);
			if (cell != null) {
				int cellType = cell.getCellType();
				logger.finest("cell type: " + String.valueOf(cellType));
				if (cellType == Cell.CELL_TYPE_STRING) {
					String cellContents = cell.getStringCellValue();
					logger.finest("cellContents: " + cellContents);
					cellContents = cellContents.replaceAll(string2Replace, replacement);
					cellContents = cellContents.trim();
					cell.setCellValue(cellContents);
				}
			}
		}
	}

	public static void writeWorkBook(final String outFilePath, final Workbook workbook) {
		try {
			FileOutputStream fileOut = new FileOutputStream(outFilePath);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception ex) {
			logger.warning(ExceptionUtils.printMessageAndStackTrace(ex));
		}
	}
}