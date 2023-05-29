package oz.sqlrunner.tableoperations;

import java.awt.Component;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import oz.guigenerator.GuiGeneratorMain;
import oz.guigenerator.GuiGeneratorParamsFileProcessor;
import oz.infra.logging.jul.JulUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.listeners.enums.ObjectExplorerOperationEnum;
import oz.sqlrunner.handlers.RowCountHandler;

public class ObjectExplorerOperationObserver implements Observer {
	private static Logger logger = JulUtils.getLogger();
	private static final ObjectExplorerOperationObserver objectExplorerOperationObserver = new ObjectExplorerOperationObserver();
	private GuiGeneratorParamsFileProcessor exportGuiGeneratorParamsFileProcessor = null;
	private GuiGeneratorParamsFileProcessor importGuiGeneratorParamsFileProcessor = null;
	private SQLRunnerConnectionParameters sqlRunnerConnectionParameters = SQLRunnerStaticParameters
			.getConnectionSqlRunnerParameters();
	private BrowseHandler browseHandler = new BrowseHandler();

	public final void update(final Observable observable, final Object parametersHashTableObj) {
		Hashtable<String, String> parametersHashTable = (Hashtable<String, String>) parametersHashTableObj;
		String numberOfRecords = parametersHashTable.get("numberOfRecords");
		String objectExplorerOperationString = parametersHashTable.get("ObjectExplorerOperation");
		logger.info("Processing objectExplorerOperationString: " + objectExplorerOperationString);
		ObjectExplorerOperationEnum objectExplorerOperationEnum = ObjectExplorerOperationEnum
				.valueOf(objectExplorerOperationString);
		Component rootComponent = sqlRunnerConnectionParameters.getSqlRunnerJFrame();
		switch (objectExplorerOperationEnum) {
		case BROWSE:
			browseHandler.browseData(numberOfRecords);
			break;
		case COUNT:
			RowCountHandler.getSqlRunneRowCountHandler().countRows();
			break;
		case DROP:
		case TRUNCATE:
			TableOperationHandler.getTableoperationhandler()
					.handleTableOperation(objectExplorerOperationEnum.toString());
			logger.info(objectExplorerOperationEnum.name() + " processing ...");
			break;
		case EXPORT:
			ExportObserver exportObserver = ExportObserver.getExportObserver();
			exportGuiGeneratorParamsFileProcessor = GuiGeneratorMain.showGui(exportGuiGeneratorParamsFileProcessor,
					SQLRunnerStaticParameters.getExportGuiXmlFile(), exportObserver, rootComponent);
			break;
		case IMPORT:
			ImportObserver importObserver = ImportObserver.getImportObserver();
			importGuiGeneratorParamsFileProcessor = GuiGeneratorMain.showGui(importGuiGeneratorParamsFileProcessor,
					SQLRunnerStaticParameters.getImportGuiXmlFile(), importObserver, rootComponent);
			break;
		case SHOW_META_DATA:
			ShowResultSetHandler showMetaDataObserver = ShowResultSetHandler.getShowResultSetHandler();
			showMetaDataObserver.showResultSet(ObjectExplorerOperationEnum.SHOW_META_DATA);
			break;
		case SHOW_PRIVILEGES:
			ShowResultSetHandler showPrivilegesObserver = ShowResultSetHandler.getShowResultSetHandler();
			showPrivilegesObserver.showResultSet(ObjectExplorerOperationEnum.SHOW_PRIVILEGES);
			break;
		case SHOW_PRIMARY_KEYS:
			ShowResultSetHandler showPrimaryKeysObserver = ShowResultSetHandler.getShowResultSetHandler();
			showPrimaryKeysObserver.showResultSet(ObjectExplorerOperationEnum.SHOW_PRIMARY_KEYS);
			break;
		case GENERATE_TEMPLATE:
			break;
		}
	}

	public static ObjectExplorerOperationObserver getSQLRunnerObjectExplorerOperationObserver() {
		return objectExplorerOperationObserver;
	}
}
