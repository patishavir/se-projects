package oz.clearcase.view.ccviewsmgr.gui;

import java.awt.Font;
import java.awt.Toolkit;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import oz.clearcase.view.ccviewsmgr.CCViewsInfo;
import oz.clearcase.view.ccviewsmgr.listeners.CCViewsJTableMouseListener;
import oz.infra.logging.jul.JulUtils;

public class CCViewsJTable extends JTable {
	private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
	private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 14);
	private static CCViewsJTable ccViewsJTable = null;
	private static CCViewsJTableModel ccViewsTableModel = new CCViewsJTableModel();
	// private static TableBubbleSortDecorator decorator;
	private static TableColumn[] tableColumn;
	private static CCViewsJTableMouseListener ccViewsJTableMouseListener = new CCViewsJTableMouseListener();
	private static final Logger logger = JulUtils.getLogger();

	private CCViewsJTable() {
		ccViewsJTable = this;
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		addMouseListener(ccViewsJTableMouseListener);
		setFont(NORMAL_FONT);
	}

	public static JTable updateViewsJTable() {
		if (ccViewsJTable == null) {
			new CCViewsJTable();
		}
		CCViewsInfo.buildCCViewsArray();
		JTableHeader viewJTableheader = ccViewsJTable.getTableHeader();
		viewJTableheader.setFont(HEADER_FONT);
		ccViewsJTable.setModel(ccViewsTableModel);
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(ccViewsTableModel);
		ccViewsJTable.setRowSorter(sorter);
		tableColumn = new TableColumn[CCViewsInfo.getViewTableHeader().length];
		double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		for (int i = 0; i < tableColumn.length; i++) {
			tableColumn[i] = ccViewsJTable.getColumnModel().getColumn(i);
			tableColumn[i].setPreferredWidth((int) (screenWidth * CCViewsInfo
					.getViewTableColumnWeight()[i]));
		}
		return ccViewsJTable;
	}

	public static JTable getViewJTable() {
		return ccViewsJTable;
	}
}