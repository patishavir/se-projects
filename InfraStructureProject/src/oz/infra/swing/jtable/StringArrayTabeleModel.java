package oz.infra.swing.jtable;

import javax.swing.table.AbstractTableModel;

public class StringArrayTabeleModel extends AbstractTableModel {
	String[] columnNames;
	String[][] rowData;

	public StringArrayTabeleModel(final String[][] rowData, final String[] columnNames) {
		this.rowData = rowData;
		this.columnNames = columnNames;
	}

	public String getColumnName(final int col) {
		return columnNames[col].toString();
	}

	public int getRowCount() {
		return rowData.length;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public boolean isCellEditable(final int row, final int col) {
		return true;
	}

	public void setValueAt(final String value, final int row, final int col) {
		rowData[row][col] = value;
		fireTableCellUpdated(row, col);
	}

	public String getValueAt(final int row, final int col) {
		return rowData[row][col];
	}
}
