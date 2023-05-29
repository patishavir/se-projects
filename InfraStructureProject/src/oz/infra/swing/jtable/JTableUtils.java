package oz.infra.swing.jtable;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import oz.infra.font.FontUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.swing.dimension.DimensionUtils;

public final class JTableUtils {
	private static Logger logger = JulUtils.getLogger();

	public static JTable autoResizeColWidth(final JTable table) {
		DefaultTableModel model = new DefaultTableModel();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setModel(model);

		int margin = 5;

		for (int i = 0; i < table.getColumnCount(); i++) {
			int vColIndex = i;
			DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
			TableColumn col = colModel.getColumn(vColIndex);
			int width = 0;

			// Get width of column header
			TableCellRenderer renderer = col.getHeaderRenderer();

			if (renderer == null) {
				renderer = table.getTableHeader().getDefaultRenderer();
			}

			Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);

			width = comp.getPreferredSize().width;

			// Get maximum width of column data
			for (int r = 0; r < table.getRowCount(); r++) {
				renderer = table.getCellRenderer(r, vColIndex);
				comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, vColIndex), false, false, r,
						vColIndex);
				width = Math.max(width, comp.getPreferredSize().width);
			}

			// Add margin
			width += 2 * margin;

			// Set the width
			col.setPreferredWidth(width);
		}

		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
				.setHorizontalAlignment(SwingConstants.LEFT);

		// table.setAutoCreateRowSorter(true);
		// table.getTableHeader().setReorderingAllowed(false);

		return table;
	}

	public static int getColumnIndex(final JTable jtable, final String columnName) {
		int columnIndex = -1;
		if (jtable != null && (jtable instanceof JTable)) {
			int columnCount = jtable.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				logger.finest("Current column name: " + jtable.getColumnName(i) + "    Column name: " + columnName);
				if (jtable.getColumnName(i).equalsIgnoreCase(columnName)) {
					columnIndex = i;
					break;
				}
			}
		}
		return columnIndex;
	}

	public static String[] getColumnNamesArray(final JTable jtable) {
		int columncount = jtable.getColumnCount();
		ArrayList<String> columnNamesArrayList = new ArrayList<String>();
		for (int col = 0; col < columncount; col++) {
			columnNamesArrayList.add(jtable.getColumnName(col));
		}
		String[] columnNamesArray = columnNamesArrayList.toArray(new String[columnNamesArrayList.size()]);
		return columnNamesArray;
	}

	public static String getColumnNamesString(final JTable jtable) {
		String[] columnNamesArray = getColumnNamesArray(jtable);
		return StringUtils.join(columnNamesArray, ",");
	}

	private static int getJTableColumnWidth(final JTable jTable, final String string, final Font font) {
		FontMetrics fontMetrics = jTable.getFontMetrics(font);
		Graphics graphics = jTable.getGraphics();
		Rectangle2D rectangle2D = fontMetrics.getStringBounds(string, graphics);
		int columnWidth = (int) (rectangle2D.getWidth());
		return columnWidth;
	}

	public static JTable getSingleCellJTable(final String text) {
		JTable jtable = new JTable(1, 1);
		jtable.setValueAt(text, 0, 0);
		jtable.setTableHeader(null);
		jtable.setFont(FontUtils.ARIAL_BOLD_14);
		TableColumn tableColumn = jtable.getColumnModel().getColumn(0);
		tableColumn.setPreferredWidth(280);
		return jtable;
	}

	public static JTable getSpingJTable(final Object[][] jtableArray, final Object[] header) {
		JTable jtable = new JTable(jtableArray, header) {
			@Override
			public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		return jtable;
	}

	public static JTable getTestJTable(final int columns, final int rows, final String id) {
		String[] columnNames = new String[columns];
		String[][] data = new String[rows][columns];
		for (int c = 0; c < columns; c++) {
			columnNames[c] = id + ":Column " + String.valueOf(c);
		}
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				data[r][c] = "Col " + String.valueOf(c) + " Row " + String.valueOf(r);
			}
		}
		return new JTable(data, columnNames);
	}

	public static void setColumnsWidths(final JTable jTable, final String[][] rowdata, final String[] columnNames) {
		final int cellMargin = 10;
		DimensionUtils.getAsString(jTable.getPreferredSize(), Level.FINEST);
		for (int column = 0; column < columnNames.length; column++) {
			TableColumn tableColumn = jTable.getColumnModel().getColumn(column);
			JTableHeader jtableHeader = jTable.getTableHeader();
			int stringMaxWidth = getJTableColumnWidth(jTable, columnNames[column], jtableHeader.getFont());
			int columnWidth = stringMaxWidth;
			logger.finest("number of rows: " + String.valueOf(rowdata.length));
			logger.finest("number of columns: " + String.valueOf(rowdata[0].length));
			Font font = jTable.getFont();
			for (int row = 0; row < rowdata.length; row++) {
				logger.finest("row:" + String.valueOf(row) + " column:" + String.valueOf(column) + " ++++++++++ ");
				if (rowdata[row][column] != null && rowdata[row][column].length() > 0) {
					columnWidth = getJTableColumnWidth(jTable, rowdata[row][column], font);
					if (columnWidth > stringMaxWidth) {
						logger.finest("row:" + String.valueOf(row) + " column:" + String.valueOf(column)
								+ " Column width = " + String.valueOf(columnWidth));
						stringMaxWidth = columnWidth;
					}
				}
			}
			logger.finer("Column " + String.valueOf(column) + " width: " + String.valueOf(stringMaxWidth)
					+ " =============");
			tableColumn.setPreferredWidth(stringMaxWidth + cellMargin);
		}
		DimensionUtils.getAsString(jTable.getPreferredSize(), Level.FINEST);
	}

	public static void setHeaderHorizontalAlignment(final JTable jtable, final int alignment) {
		TableCellRenderer renderer = jtable.getTableHeader().getDefaultRenderer();
		JLabel label = (JLabel) renderer;
		label.setHorizontalAlignment(alignment);
	}

	public static void setUniformColumnsWidths(final JTable jTable, final int columnWidth) {
		TableColumnModel tableColumnModel = jTable.getColumnModel();
		int columnCount = tableColumnModel.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			tableColumnModel.getColumn(i).setPreferredWidth(columnWidth);
		}
	}

	private JTableUtils() {
	}
}
