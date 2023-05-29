package oz.utils.p8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.db.DBUtils;
import oz.infra.email.EmailUtils;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class P8UsageReport {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		P8UsageParameters.processParameters(args[0]);
		generateReport(P8UsageParameters.getDbPropertiesFile(), P8UsageParameters.getSqlSriptPath());
	}

	private static void generateReport(final String connetionPropertiesFilePath, final String sqlScriptFilePath) {
		StopWatch stopWatch = new StopWatch();
		Connection connection = DBUtils.getConnection(connetionPropertiesFilePath);
		logger.finest(connection.toString());
		String sqlScript = FileUtils.readTextFile(sqlScriptFilePath);
		logger.finest(sqlScript);
		ResultSet resultSet = DBUtils.getResultSet(connection, sqlScript);
		String timeStamp = DateTimeUtils.getTimeStamp();
		String excelFilePath = PathUtils.getFullPath(P8UsageParameters.getExcelFolderPath(), StringUtils.concat(
				P8UsageParameters.getExcelFileName(), OzConstants.UNDERSCORE, timeStamp, OzConstants.XLSX_SUFFIX));
		ExcelWorkbook.writeWorkbook(excelFilePath, resultSet, "P8 Usage report");
		emailReport(excelFilePath);
		stopWatch.logElapsedTimeMessage();
	}

	public static void emailReport(final String reportFilePath) {
		Properties emailProperties = new Properties();
		emailProperties.setProperty("debug", "no");
		emailProperties.setProperty("hostname", "mail.fibi.co.il");
		String subject = "P8 usage report for ".concat(DateTimeUtils.getCurrentDate(DateTimeUtils.DATE_FORMAT_dd_MM_yyyy));
		emailProperties.setProperty("subject", subject);
		emailProperties.setProperty("message", subject);
		emailProperties.setProperty("attachmentPath", reportFilePath);
		emailProperties.setProperty("from", "Zimerman.O@fibi.co.il");
		emailProperties.setProperty("to", P8UsageParameters.getSmtpMailTo());
		EmailUtils.sendMultiPartEmail(emailProperties);
	}
}
