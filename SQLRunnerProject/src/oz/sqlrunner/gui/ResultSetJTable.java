package oz.sqlrunner.gui;

import java.sql.ResultSet;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import oz.infra.array.ArrayUtils;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.font.FontUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.jtable.JTableUtils;

public class ResultSetJTable extends JTable {
	private Logger logger = JulUtils.getLogger();
	private static final String RESULT_SET_IS_EMPTY = "Result set is empty";

	public ResultSetJTable() {
	}

	public ResultSetJTable(final ResultSet resultSet, final int[] selectedColumns) {
		String[] columnNames = DBMetaDataUtils.getResultSetColumnNames(resultSet);
		ArrayUtils.printArray(columnNames);
		String[][] rowData = ResultSetUtils.getResultSetAs2DimArray(resultSet);
		configResultSetJTable(columnNames, rowData, selectedColumns);
	}

	public void configResultSetJTable(final String[] columnNamesParam,
			final String[][] rowDataParam, final int[] selectedColumns) {
		String[] columnNames = columnNamesParam;
		String[][] rowData = rowDataParam;
		if (selectedColumns != null) {
			columnNames = ArrayUtils.selectArrayRows(columnNames, selectedColumns);
			rowData = ArrayUtils.getSelectedArrayColumns(rowData, selectedColumns);
		}
		if (rowData == null) {
			rowData = new String[1][columnNames.length];
			rowData[0][0] = RESULT_SET_IS_EMPTY;
		}
		if (rowData != null) {
			setAutoCreateRowSorter(true);
		}
		DefaultTableModel defaultTableModel = new DefaultTableModel(rowData, columnNames);
		this.setModel(defaultTableModel);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setFont(FontUtils.ARIAL_PLAIN_12);
		logger.info("Number of columnNames: " + String.valueOf(columnNames.length)
				+ " Number of rowData columns: " + String.valueOf(rowData[0].length));
		JTableUtils.setColumnsWidths(this, rowData, columnNames);
		this.doLayout();
		logger.finest("rows:" + String.valueOf(this.getRowCount()) + " coumns: "
				+ String.valueOf(this.getColumnCount()));
	}
}
