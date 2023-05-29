package oz.sqlrunner.gui.listeners;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import oz.guigenerator.GuiGeneratorDefaultsProviderInterface;
import oz.guigenerator.GuiGeneratorMain;
import oz.guigenerator.GuiGeneratorParamsFileProcessor;
import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.jtable.JTableUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.tableoperations.ObjectExplorerOperationObserver;

public class ObjectExplorerMouseListener extends MouseAdapter implements GuiGeneratorDefaultsProviderInterface {
	private static final String NUMBER_OF_RECORDS = "numberOfRecords";
	private Logger logger = JulUtils.getLogger();
	private GuiGeneratorParamsFileProcessor browseGuiGeneratorParamsFileProcessor;

	public void mouseClicked(final MouseEvent e) {
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		if (SwingUtilities.isRightMouseButton(e)) {
			JTable jtable = (JTable) e.getSource();
			// get the coordinates of the mouse click
			Point point = e.getPoint();
			logger.finest("point.x: " + String.valueOf(point.x) + " point.y: " + String.valueOf(point.y));
			// get the row index that contains that coordinate
			int rowNumber = jtable.rowAtPoint(point);
			int columnNumber = jtable.columnAtPoint(point);
			String tableNameColumnName = sqlRunnerParameters.getTableNameColumName();
			String creatorColumnName = sqlRunnerParameters.getCreatorColumName();
			logger.finest(
					"Row number: " + String.valueOf(rowNumber) + " Column number: " + String.valueOf(columnNumber));
			int nameColumnIndex = JTableUtils.getColumnIndex(jtable, tableNameColumnName);
			logger.finest(tableNameColumnName + " column index: " + String.valueOf(nameColumnIndex));
			int creatorColumnIndex = JTableUtils.getColumnIndex(jtable, creatorColumnName);
			logger.finest(creatorColumnName + " column index: " + String.valueOf(creatorColumnIndex));
			String tableName = ((String) jtable.getValueAt(rowNumber, nameColumnIndex)).trim();
			String tableCreator = ((String) jtable.getValueAt(rowNumber, creatorColumnIndex)).trim();
			logger.finest("Name: " + tableName + " tableCreator: " + tableCreator);
			sqlRunnerParameters.setTableName(tableName);
			sqlRunnerParameters.setTableCreator(tableCreator);
			Component rootComponent = sqlRunnerParameters.getSqlRunnerJFrame();
			browseGuiGeneratorParamsFileProcessor = GuiGeneratorMain.showGui(browseGuiGeneratorParamsFileProcessor,
					SQLRunnerStaticParameters.getobjectExplorerContextMenuGuiXmlFile(),
					ObjectExplorerOperationObserver.getSQLRunnerObjectExplorerOperationObserver(), this, rootComponent);
		}
	}

	public final String getDefaultValue(final String key) {
		String value = null;
		if (key.equalsIgnoreCase(NUMBER_OF_RECORDS)) {
			value = SQLRunnerStaticParameters.getDefaultBrowseMaxNumberOfRecords();
			logger.info(key + "=" + value);
		}
		return value;
	}

	public final String[] getValues(final String key) {
		logger.info("key: " + key);
		return null;
	}
}