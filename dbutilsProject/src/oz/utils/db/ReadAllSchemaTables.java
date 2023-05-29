package oz.utils.db;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;

public class ReadAllSchemaTables {

	/**
	 * @param args
	 */
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		Properties properties = PropertiesUtils.loadPropertiesFile(args[0]);
		String password = properties.getProperty("password");
		properties.setProperty("password", password);
		String schemaName = properties.getProperty("schemaName");
		List<String> tableNameList = DBMetaDataUtils.getSchemaTableNames(properties, schemaName);
		ListUtils.getAsDelimitedString(tableNameList, Level.INFO);
		logger.info(StringUtils.concat(String.valueOf(tableNameList.size()), " tables found in schema ", schemaName));
	}
}
