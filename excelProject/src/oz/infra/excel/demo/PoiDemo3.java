package oz.infra.excel.demo;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PoiDemo3 {

	private static void colorDemo() {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("new sheet");

		// Create a row and put some cells in it. Rows are 0 based.
		Row row = sheet.createRow((short) 1);

		// Aqua background
		CellStyle style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(CellStyle.BIG_SPOTS);
		// style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Cell cell = row.createCell((short) 1);
		cell.setCellValue("X");
		cell.setCellStyle(style);

		// Orange "foreground", foreground being the fill foreground not the
		// font color.
		style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell = row.createCell((short) 2);
		cell.setCellValue("XX");
		cell.setCellStyle(style);

		// Orange "foreground", foreground being the fill foreground not the
		// font color.
		style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell = row.createCell((short) 3);
		cell.setCellValue("XXX");
		cell.setCellStyle(style);

		// Orange "foreground", foreground being the fill foreground not the
		// font color.
		style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell = row.createCell((short) 4);
		cell.setCellValue("XXXX");
		cell.setCellStyle(style);

		// Orange "foreground", foreground being the fill foreground not the
		// font color.
		style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell = row.createCell((short) 5);
		cell.setCellValue("XXXXX");
		cell.setCellStyle(style);

		// Orange "foreground", foreground being the fill foreground not the
		// font color.
		style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell = row.createCell((short) 6);
		cell.setCellValue("XXXXXX");
		cell.setCellStyle(style);

		// Orange "foreground", foreground being the fill foreground not the
		// font color.
		style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.LIME.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell = row.createCell((short) 7);
		cell.setCellValue("XXXXXXX");
		cell.setCellStyle(style);
		writeWorkbook(wb);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		colorDemo();
		xssfColorDemo();
	}

	private static void writeWorkbook(final Workbook wb) {

		// Write the output to a file
		try {
			FileOutputStream fileOut = new FileOutputStream("c:\\temp\\workbook.xlsx");
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void xssfColorDemo() {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		cell.setCellValue("custom  colors");

		XSSFCellStyle style1 = wb.createCellStyle();
		XSSFColor xssfColor = new XSSFColor(new java.awt.Color(200, 200, 200));
		style1.setFillForegroundColor(xssfColor);
		style1.setFillPattern(CellStyle.SOLID_FOREGROUND);

		cell.setCellStyle(style1);

		try {
			FileOutputStream fileOut = new FileOutputStream("c:\\temp\\XSSFworkbook.xlsx");
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
