package oz.sqlrunner.handlers;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import oz.guigenerator.GuiGeneratorDefaultsProviderInterface;
import oz.infra.array.ArrayUtils;
import oz.infra.db.DBUtils;
import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;

public class FilterHandler implements Observer, GuiGeneratorDefaultsProviderInterface {
	public static final String NAME = "name";
	public static final String CREATOR = "creator";
	private String nameFilter = null;
	private String creatorFilter = null;
	private String databaseObjectTypeFilter = null;
	private Hashtable<String, String> filterParametersHashTable;
	private SQLRunnerConnectionParameters sqlRunnerParameters = null;
	private static Logger logger = JulUtils.getLogger();

	public final void update(final Observable observable, final Object parametersHashTableObj) {
		sqlRunnerParameters = SQLRunnerStaticParameters.getConnectionSqlRunnerParameters();
		filterParametersHashTable = (Hashtable<String, String>) parametersHashTableObj;
		nameFilter = filterParametersHashTable.get(NAME);
		sqlRunnerParameters.setNameFilter(nameFilter);
		creatorFilter = filterParametersHashTable.get(CREATOR);
		sqlRunnerParameters.setCreatorFilter(creatorFilter);
		databaseObjectTypeFilter = filterParametersHashTable.get("TYPE");
		sqlRunnerParameters.setDatabaseObjectTypeFilter(databaseObjectTypeFilter);
		logger.finest("nameFilter:" + nameFilter + " creatorFilter: " + creatorFilter
				+ " databaseObjectTypeFilter: " + databaseObjectTypeFilter);
		if (sqlRunnerParameters.getConnection() != null) {
			ObjectExplorerHandler.showObjectExplorer();
		}
	}

	public final String getDefaultValue(final String key) {
		String value = null;
		if (key.equalsIgnoreCase(NAME)) {
			value = SQLRunnerStaticParameters.getConnectionSqlRunnerParameters().getNameFilter();
		}
		if (key.equalsIgnoreCase(CREATOR)) {
			value = SQLRunnerStaticParameters.getConnectionSqlRunnerParameters().getCreatorFilter();
		}
		logger.info(key + "=" + value);
		return value;
	}

	public final String[] getValues(final String key) {
		sqlRunnerParameters = SQLRunnerStaticParameters.getConnectionSqlRunnerParameters();
		logger.info("getDefaultValues " + key);
		if (key.equalsIgnoreCase(CREATOR)) {
			String creatorFilterUsedLast = sqlRunnerParameters.getCreatorFilter();
			String[] schemasArray = getSchemasArray();
			if (creatorFilterUsedLast != null && creatorFilterUsedLast.length() > 0
					&& schemasArray.length > 0 && (!schemasArray[0].equals(creatorFilterUsedLast))) {
				ArrayUtils.moveEntry(schemasArray, creatorFilterUsedLast, 0);
			}
			Arrays.sort(schemasArray, 1, schemasArray.length);
			return schemasArray;
		} else {
			return null;
		}
	}

	private String[] getSchemasArray() {
		sqlRunnerParameters = SQLRunnerStaticParameters.getConnectionSqlRunnerParameters();
		String[] schemasArray = sqlRunnerParameters.getSchemasArray();
		if (schemasArray == null) {
			logger.info("Starting " + SystemUtils.getCurrentMethodName());
			Connection connection = sqlRunnerParameters.getConnection();
			try {
				if (sqlRunnerParameters.isUseDatabaseMetaData()) {
					DatabaseMetaData databaseMetaData = connection.getMetaData();
					ResultSet resultSet = databaseMetaData.getSchemas();
					schemasArray = DBUtils.getStringColumnFromResultSet(resultSet, 1);
				} else {
					String sqlStatementString = sqlRunnerParameters.getDataBaseProductEnum()
							.getGetSchemasSelectStatement();
					 SqlExecutionOutcome sqlExecutionOutcome = SQLStatementHandler.executeSqlStatement(sqlStatementString);
					 schemasArray = ArrayUtils.getSelectedArrayColumn(sqlExecutionOutcome.getResultSetArray(),0);
				}
				Arrays.sort(schemasArray);
				sqlRunnerParameters.setSchemasArray(schemasArray);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.warning(ex.getMessage());
			}
		}
		return schemasArray;
	}
}
