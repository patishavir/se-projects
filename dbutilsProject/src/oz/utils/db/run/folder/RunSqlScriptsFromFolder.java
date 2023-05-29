package oz.utils.db.run.folder;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.db.DBFileUtils;
import oz.infra.db.DBFolderUtils;
import oz.infra.db.outcome.SqlScriptExecutionOutcome;
import oz.infra.email.EmailUtils;
import oz.infra.fibi.gm.GMUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class RunSqlScriptsFromFolder {
	public static final String SMTP_EMAIL_PROPERTIES_FILE_PATH = "smtpEmailProprtiesFilePath";

	private static Logger logger = JulUtils.getLogger();

	private static void sendGmHtmlMail(final String subject, final String logsFolderPath) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div dir=ltr>");
		String hyperLink = "<a href=\"" + logsFolderPath + "\">DB changes run logs</a>";
		sb.append(hyperLink);
		sb.append("</div>");
		Properties gmMailProperties = GMUtils.getGmEmailDefaultProperties();
		GMUtils.sendEmail(gmMailProperties, subject, sb.toString());
	}

	private static void smtpEmailReport(final String emailProprtiesFilePath, final String message,
			final String attachmentPath) {
		if (emailProprtiesFilePath != null) {
			Properties emailProperties = PropertiesUtils.loadPropertiesFile(emailProprtiesFilePath);
			String subject = "DB changes summary " + DateTimeUtils.formatDate(DateTimeUtils.DATE_FORMAT_dd_MM_yyyy);
			emailProperties.setProperty("subject", subject);
			emailProperties.setProperty("message", message);
			emailProperties.setProperty("attachmentPath", attachmentPath);
			EmailUtils.sendMultiPartEmail(emailProperties);
		}
	}

	public static final void processFolder(final Properties currentProperties) {
		String timeStamp = DateTimeUtils.getTimeStamp();
		String database = PathUtils
				.getNameWithoutExtension(currentProperties.getProperty(DBFileUtils.DB_PROPERTIES_FILE_PATH));
		String logsFolderPath = currentProperties.getProperty(ParametersUtils.LOGS_FOLDER) + File.separator + database
				+ OzConstants.UNDERSCORE + timeStamp;
		File logsFolderFile = new File(logsFolderPath);
		logsFolderFile.mkdirs();
		String logFileName = database + OzConstants.UNDERSCORE + timeStamp;
		String logFilePrefix = logsFolderPath + File.separator + logFileName;
		String logFilePath = logFilePrefix + ".log";
		String errFilePath = logFilePrefix + ".err";
		String summaryFilePath = logFilePrefix + ".summary.log";
		JulUtils.closeHandlers();
		JulUtils.removeHandlers(JulUtils.getFileHandlerCanonicalName());
		JulUtils.addFileHandler(errFilePath);
		logger.info(SystemUtils.getRunInfo());
		//
		List<SqlScriptExecutionOutcome> listOfSqlScriptExecutionOutcome = DBFolderUtils
				.runSqlScriptsFromFolder(currentProperties);
		//
		StringBuilder reportSb = new StringBuilder();
		StringBuilder summarySb = new StringBuilder();
		for (SqlScriptExecutionOutcome sqlScriptExecutionOutcome : listOfSqlScriptExecutionOutcome) {
			reportSb.append(sqlScriptExecutionOutcome.print().toString());
			summarySb.append("\n" + sqlScriptExecutionOutcome.getSummayLine());
		}
		String report = reportSb.toString();
		String subject = "DB changes run summary for " + database;
		String summary1 = subject + ":";
		String summary2 = StringUtils.repeatString("=", summary1.length());
		String summary = summary1 + "\n" + summary2 + summarySb.toString();
		FileUtils.writeFile(logFilePath, report);
		FileUtils.writeFile(summaryFilePath, summary);
		sendGmHtmlMail(subject, logsFolderPath);
	}
}
