package oz.infra.excel.demo;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

public class PoiDemo1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// create a new workbook
		HSSFWorkbook workBook = new HSSFWorkbook();
		// Then you can create a new worksheet within that workbook:

		// create a new worksheet
		HSSFSheet sheet = workBook.createSheet();
		// etc...
		// You create rows by calling the Worksheet object's createRow method.
		// Remember, the row index starts from 0.

		// ***************header data******************//
		// create a header row
		HSSFRow headerRow = sheet.createRow((short) 0);
		// To create the merged area for the caption you want to define a merged
		// region from the first to the third row, and from first to the tenth
		// column. Here's the code:

		// define the area for the header data(row1->row3, col1-->col10)
		sheet.addMergedRegion(new Region(0, (short) 0, 2, (short) 10));
		// After creating the caption region, create a cell to contain the
		// caption value.

		// create the header data cell
		HSSFCell headerCell = headerRow.createCell((short) 0);
		// add the date to the header cell
		headerCell.setCellValue("The Bowling Score");
		// Because the header row spans multiple rows and columns, the caption
		// "The Bowling Score" will span the defined merged region.

		// Worksheet cells have many display properties that you can control
		// through HSSF. To do that, you create an HSSFCellStyle object and
		// apply styles such as cell alignment (CENTER/LEFT/RIGHT). The
		// following code shows how you can set the background colors and the
		// font style for individual cells.

		// create a style for the header cell
		HSSFCellStyle headerStyle = workBook.createCellStyle();

		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerCell.setCellStyle(headerStyle);

		// create a style for this header columns
		HSSFCellStyle columnHeaderStyle = workBook.createCellStyle();

		columnHeaderStyle.setFillBackgroundColor(HSSFColor.BLUE_GREY.index);

		columnHeaderStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);

		HSSFFont font = workBook.createFont();
		font.setColor(HSSFFont.COLOR_RED);
		columnHeaderStyle.setFont(font);
		// After defining cell styles, you can apply those styles as follows:

		// HSSFCell colHeading1 =
		// columnHeaderRow.createCell((short) 0);
		// HSSFCell colHeading2 =
		// columnHeaderRow.createCell((short) 4);

		// colHeading1.setCellStyle(columnHeaderStyle);
		// colHeading2.setCellStyle(columnHeaderStyle);
		// The Worksheet essentially consists of multiple rows and cells. You
		// can create as many rows and cells as you need and then apply styles
		// and assign data to the cells. The following example creates five rows
		// with two cells in each row. The code sets the cell values last, using
		// the setCellValue method.

		// **************report data rows and cols*********//
		// create 5 rows of data
		HSSFRow row1 = sheet.createRow((short) 5);
		HSSFRow row2 = sheet.createRow((short) 6);
		HSSFRow row3 = sheet.createRow((short) 7);
		HSSFRow row4 = sheet.createRow((short) 8);
		HSSFRow row5 = sheet.createRow((short) 9);

		// create the 2 cells for each row
		HSSFCell c11 = row1.createCell((short) 0);
		HSSFCell c12 = row1.createCell((short) 4);
		HSSFCell c21 = row2.createCell((short) 0);
		HSSFCell c22 = row2.createCell((short) 4);
		HSSFCell c31 = row3.createCell((short) 0);
		HSSFCell c32 = row3.createCell((short) 4);
		HSSFCell c41 = row4.createCell((short) 0);
		HSSFCell c42 = row4.createCell((short) 4);

		// writing data to the cells
		c11.setCellValue("Sam");
		c12.setCellValue(100);

		c21.setCellValue("John");
		c22.setCellValue(50);

		c31.setCellValue("Paul");
		c32.setCellValue(25);

		c41.setCellValue("Richard");
		c42.setCellValue(20);
		// One of the main advantages of Excel is that you can attach
		// mathematical formulas to cells. The following code shows how to
		// attach a formula to a cell. The example assigns a formula to
		// calculate the total value of other cells in the worksheet. Notice
		// that, in order to create a cell with a formula, you must first set
		// the cell type to the FORMULA type.

		// //create a formula for the total
		// totalValue.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		// totalValue.setCellFormula("SUM(E6:E9)");
		// When the workbook is complete, you can save it to a file system by
		// opening a FileOutputStream object for the destination file and
		// writing the Workbook contents to the stream using the write method.
		try {
			FileOutputStream stream = new FileOutputStream("c:\\temp\\Book1.xls");
			workBook.write(stream);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// It is really as simple as that. Here's the entire procedure in
		// condensed form.
		//
		// First, create a HSSFWorkbook object.
		// Obtain a HSSFWorksheet object from the HSSFWorkbook object. Note,
		// that the only way, you can obtain a reference to a Worksheet object
		// is via a Workbook object. There's no public constructor for the
		// Worksheet object. This makes sense because Excel has no model for
		// Worksheets that exist separately from a Workbook.
		// Similarly, you obtain HSSFRow objects from the Worksheet object and
		// HSSFCell objects from Row objects—the existence of each type depends
		// on its parent.
		// To create headers, create a merged region of rows and cells using the
		// org.apache.poi.hssf.util.Region object. Note that the collections of
		// rows and cells are zero-based.
		// Add the data to the cells using the HSSFCell object's setCellValue
		// method. Cells support all Java native data types such as String, int,
		// boolean etc. The API also supports some other commonly used data
		// types such as Date and Calendar and lets you set those through the
		// setCellValue() method.
		// The HSSFCellStyle class in HSSF handles display attributes for the
		// cells such as color, font, width, height etc.. You can create an
		// arbitrary style for a particular cell. For complex attributes such as
		// colors and fonts HSSF provides objects such as HSSFColour and
		// HSSFFont.
		// The cells support formulas. The example applies the formula
		// SUM(E6:E9) to the total cell value.
		// Lastly, to physically create the Excel document in the file system,
		// you need to create an OutputStream object and call the HSSFWorkbook's
		// write method, passing the created OutputStream object as a parameter.
	}
}
