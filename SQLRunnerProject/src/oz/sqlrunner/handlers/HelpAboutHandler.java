package oz.sqlrunner.handlers;

import java.sql.Connection;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import oz.infra.constants.OzConstants;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.font.FontUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;

public class HelpAboutHandler {
	private static Logger logger = JulUtils.getLogger();

	public static void showAboutDialog() {
		String implementationVersionLine = OzConstants.EMPTY_STRING;
		String implementationVersion = SystemUtils.getImplementationVersion();
		if (implementationVersion != null) {
			implementationVersionLine = StringUtils.concat("\n", SystemUtils.getImplementationVersion());
		}
		String aboutMessage = StringUtils.concat("SQL Runner version 1.0", "\nWritten by Oded Zimerman",
				implementationVersionLine, "\n\n");
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Connection connection = sqlRunnerParameters.getConnection();
		UIManager.put("OptionPane.font", FontUtils.ARIAL_PLAIN_14);
		JOptionPane.showMessageDialog(sqlRunnerParameters.getSqlRunnerJFrame(),
				aboutMessage + DBMetaDataUtils.getDataBaseMetaDataDetails(connection), "SQL Runner",
				JOptionPane.DEFAULT_OPTION);
	}
}