package oz.sqlrunner.handlers;

import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import oz.infra.font.FontUtils;
import oz.infra.logging.jul.JulUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;

public class UsageHandler {
	private static Logger logger = JulUtils.getLogger();

	public static void showUsageDialog() {
		String usageMessage = getUsageMessage();
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		UIManager.put("OptionPane.font", FontUtils.ARIAL_PLAIN_14);
		JOptionPane.showMessageDialog(sqlRunnerParameters.getSqlRunnerJFrame(), usageMessage,
				"SQL statements syntax", JOptionPane.DEFAULT_OPTION);
	}

	private static String getUsageMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nDelete:");
		sb.append("\nDELETE FROM table_name WHERE some_column=some_value");
		sb.append("\nDELETE FROM table_name   (delete all rows!)");
		sb.append("\n");
		sb.append("\nInsert:");
		sb.append("\nINSERT INTO table_name (column1, column2, column3,...) VALUES (value1, value2, value3,...)");
		sb.append("\n");
		sb.append("\nUpdate:");
		sb.append("\n	UPDATE table_name SET column1=value, column2=value2,... WHERE some_column=some_value");
		sb.append("\n");

		return sb.toString();
	}
}
