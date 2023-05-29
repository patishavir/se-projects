package oz.infra.excel.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;

import oz.infra.datetime.DateTimeUtils;

public class FormatCellData {
	public static void main(final String[] args) throws Exception {
		/* Create Workbook and Worksheet */
		HSSFWorkbook myWorkbook = new HSSFWorkbook();
		HSSFSheet mySheet = myWorkbook.createSheet("Cell Date");
		/* Create two styles that take different formats */
		HSSFCellStyle my_style_0 = myWorkbook.createCellStyle();
		HSSFCellStyle my_style_1 = myWorkbook.createCellStyle();
		/* Define date formats with the style */
		my_style_0.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		my_style_1.setDataFormat(HSSFDataFormat.getBuiltinFormat("d-mmm-yy"));
		/* Define data using java.util.Calendar */
		Calendar calendar = new GregorianCalendar();
		calendar.set(1982, Calendar.NOVEMBER, 25);
		/* Create Cell Data and Attach Date formats */
		Row row = mySheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue(calendar);
		cell.setCellStyle(my_style_0); // style 1
		row = mySheet.createRow(1);
		cell = row.createCell(1);
		cell.setCellValue(calendar);
		cell.setCellStyle(my_style_1); // style 2

		Cell cell2 = row.createCell(10);
		cell2.setCellValue(1234567890);
		HSSFCellStyle style = myWorkbook.createCellStyle();
		style = myWorkbook.createCellStyle();
		DataFormat format = myWorkbook.createDataFormat();
		style.setDataFormat(format.getFormat("###,###,###,###"));
		cell2.setCellStyle(style);
		/* Write changes to the workbook */
		String timeStamp = DateTimeUtils.getTimeStamp();
		FileOutputStream out = new FileOutputStream(new File("C:\\temp\\cell_format_" + timeStamp + ".xls"));
		myWorkbook.write(out);
		out.close();
	}
}