package oz.utils.db;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.db.DBUtils;
import oz.infra.db.db2.DB2Utils;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class Generate4AllTables {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		String propertiesFilePath = args[0];
		String commandsFilePath = args[1];
		generate4AllTables(propertiesFilePath, commandsFilePath);
	}

	private static void generate4AllTables(final String propertiesFilePath, final String commandsFilePath) {
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		String schemaName = properties.getProperty("schemaName");
		String folderPath = properties.getProperty("folderPath");
		String url = properties.getProperty("url");
		properties.put("driverClassName", DB2Utils.DB2_JCC_DB2DRIVER);
		Connection connection = null;
		try {
			connection = DBUtils.getConnection(properties);
			List<String> tableNameList = DBMetaDataUtils.getSchemaTableNames(connection, schemaName);
			ListUtils.getAsDelimitedString(tableNameList, Level.INFO);
			logger.info(
					StringUtils.concat(String.valueOf(tableNameList.size()), " tables found in schema ", schemaName));
			StringBuilder sb = new StringBuilder();
			Map<String, String> map = new HashMap<String, String>();
			map.put("schemaName", schemaName);
			String[] commandArray = FileUtils.readTextFile2Array(commandsFilePath);
			int commandsCount = 0;
			for (String tableName : tableNameList) {
				map.put("tableName", tableName);
				for (int i = 0; i < commandArray.length; i++) {
					if (!commandArray[i].startsWith(OzConstants.NUMBER_SIGN)) {
						String command2Run = StringUtils.substituteVariables(commandArray[i], map);
						sb.append(command2Run);
						sb.append(DB2Utils.COMMAND_SEPARATOR);
						sb.append(SystemUtils.LINE_SEPARATOR);
						commandsCount++;
					}
				}
			}
			String commands2Run = sb.toString();
			logger.info(commands2Run);
			String fullFolderPath = PathUtils.getFullPath(PathUtils.getParentFolderPath(propertiesFilePath),
					folderPath);
			String scriptPath = PathUtils.getFullPath(fullFolderPath,
					StringUtils.concat(DB2Utils.getDBNameFromUrl(url), OzConstants.UNDERSCORE, schemaName + ".sql"));
			FileUtils.writeFile(scriptPath, commands2Run);
			logger.info(String.valueOf(commandsCount) + " commands have been generated.");
			Object runCommands = properties.get("runCommands");
			if (runCommands != null && runCommands.toString().equalsIgnoreCase(OzConstants.YES)) {
				DBUtils.runSqlStatements(connection, commands2Run);
			}
			connection.isClosed();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return;
	}
}
