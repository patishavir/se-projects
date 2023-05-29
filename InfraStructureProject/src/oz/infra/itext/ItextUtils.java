package oz.infra.itext;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.logging.Logger;

import org.jfree.chart.JFreeChart;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;

public class ItextUtils {
	private static Logger logger = JulUtils.getLogger();

	public static PdfContentByte addJFreeChartToPdf(final JFreeChart chart, final int width,
			final int height, final PdfWriter pdfWriter, final int x, final int y) {
		// step 1
		PdfContentByte cb = pdfWriter.getDirectContent();
		PdfTemplate tp = cb.createTemplate(width, height);
		Graphics2D g2d = tp.createGraphics(width, height, new DefaultFontMapper());
		Rectangle2D r2d = new Rectangle2D.Double(0, 0, width, height);
		chart.draw(g2d, r2d);
		g2d.dispose();
		cb.addTemplate(tp, x, y);
		return cb;
	}

	public static void arrayToPdf(final String[] columnNames, final String[][] contents) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("c:\\temp\\tablePDF.pdf"));
			document.open();
			PdfPTable table = new PdfPTable(columnNames.length);
			for (int col = 0; col < columnNames.length; col++) {
				table.addCell(columnNames[col]);

			}
			for (int row = 0; row < contents.length; row++) {
				for (int col = 0; col < contents[0].length; col++) {
					table.addCell(contents[row][col]);
				}
			}

			document.add(table);
			document.close();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}

	}

	public static void writePdf(final String pdfFilePath, final String[] columnNames,
			final ResultSet resultSet) {
		Document document = new Document(PageSize.A1.rotate());
		try {
			PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
			document.open();
			PdfPTable table = new PdfPTable(columnNames.length);
			for (int col = 0; col < columnNames.length; col++) {
				table.addCell(columnNames[col]);
			}
			try {
				int columnCount = columnNames.length;
				logger.finest("Column count: " + columnCount);
				int columnIndex;
				while (resultSet.next()) {

					for (columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
						table.addCell(resultSet.getString(columnIndex));
					}
				}
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
			document.add(table);
			document.close();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}

	}

}
