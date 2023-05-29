package oz.jdir.gui;

import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;

/*
 * class DirTableModel @author Oded
 */
class DirTableModel extends AbstractTableModel {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private String[] columnNames;
	private final JdirInfo jd;

	DirTableModel(final JdirInfo jdP, final String[] columNames) {
		this.jd = jdP;
		this.columnNames = columNames;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public final int getRowCount() {
		return jd.getMatchTableEntries();
	}

	public String getColumnName(final int col) {
		return columnNames[col];
	}

	public final Object getValueAt(final int row, final int col) {
		if (col == 0) {
			return new Integer(row);
		}
		FileInfo rowFileInfo = jd.getFileInfoByRow(row);
		if (rowFileInfo == null)
			return null;
		switch (col) {
		case 1:
			return rowFileInfo.getFilePartialPath();
		case 2:
			return new Long(rowFileInfo.length());
		case 3:
			return new Long(rowFileInfo.lastModified());
		default:
			logger.severe("Invalid column number!\nProcessing terminated.");
			System.exit(-1);
			return null;
		}
	}
}