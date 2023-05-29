package oz.clearcase.view.ccviewsmgr.gui;

import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import oz.clearcase.view.ccviewsmgr.CCViewsInfo;

/*
 * class DirTableModel @author Oded
 */
class CCViewsJTableModel extends AbstractTableModel {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public int getColumnCount() {
		String[] viewTableHeader = CCViewsInfo.getViewTableHeader();
		if (viewTableHeader == null) {
			return 0;
		}
		return CCViewsInfo.getViewTableHeader().length;
	}

	public final int getRowCount() {
		return CCViewsInfo.getRowCount();
	}

	public String getColumnName(final int col) {
		return CCViewsInfo.getViewTableHeader()[col];
	}

	public final Object getValueAt(final int row, final int col) {
		return CCViewsInfo.getViewTableValueAt(row, col);
	}

	public final void setValueAt(final Object obj, final int row, final int col) {
		CCViewsInfo.setViewTableValueAt((String) obj, row, col);
	}
}