package oz.utils.emergencyreports.branch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.StopWatch;
import oz.infra.db.DBUtils;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.PathUtils;
import oz.infra.itext.ItextUtils;
import oz.infra.logging.jul.JulUtils;
import oz.utils.emergencyreports.EmergencyReports;
import oz.utils.emergencyreports.EmergencyReportsParameters;

public class BranchProcessor implements Runnable {
	private String branch;
	private String sqlStatementString = null;
	private static int processingStarted = 0;
	private static int processingCompleted = 0;
	private static int connectionFailures = 0;
	private static int nullResultSet = 0;

	private enum OutputFormatEnum {
		CSV, XLSX, PDF
	}

	private static Logger logger = JulUtils.getLogger();

	public static int getConnectionFailures() {
		return connectionFailures;
	}

	public static int getNullResultSet() {
		return nullResultSet;
	}

	public static int getProcessingCompleted() {
		return processingCompleted;
	}

	private static synchronized void incrementConnectionFailuresCounter() {
		connectionFailures++;
	}

	private static synchronized void incrementNullResultSetCounter() {
		nullResultSet++;
	}

	private static synchronized void incrementProcessingCompletedCounter() {
		processingCompleted++;
	}

	private static synchronized void incrementProcessingStartedCounter() {
		processingStarted++;
	}

	public BranchProcessor(final String branch, final String sqlStatementString) {
		this.branch = branch;
		this.sqlStatementString = sqlStatementString;
	}

	@Override
	public void run() {
		String outputFormat = EmergencyReportsParameters.getOutputFormat();
		OutputFormatEnum outputFormatEnum = OutputFormatEnum.valueOf(outputFormat);
		StopWatch stopWatch = new StopWatch();
		incrementProcessingStartedCounter();
		DBUtils.setLoginTimeout(EmergencyReportsParameters.getLoginTimeOut());
		EncryptionMethodEnum encryptionMethod = EncryptionMethodEnum
				.valueOf(EmergencyReportsParameters.getDecriptPassword());
		Connection connection = DBUtils.getConnection(EmergencyReportsParameters.getDbPropertiesFilePath());
		if (connection != null) {
			ResultSet resultSet = DBUtils.getResultSet(connection, sqlStatementString);
			if (resultSet != null) {
				String reportsFolder = EmergencyReportsParameters.getReportsFolder();
				String suffix = null;
				switch (outputFormatEnum) {
				case CSV:
					suffix = OzConstants.CSV_SUFFIX;
					// ResultSetUtils.resultSet2Csv(resultSet, null,
					// reportsFolder, branch + suffix);
					break;
				case XLSX:
					suffix = OzConstants.XLSX_SUFFIX;
					String excelWorkBookFilePath = PathUtils.getFullPath(reportsFolder, branch + suffix);
					String sheetNamePrefix = EmergencyReportsParameters.getSheetNamePrefix();
					String sheetName = sheetNamePrefix.concat(" ").concat(branch);
					ExcelWorkbook.writeWorkbook(excelWorkBookFilePath, resultSet, sheetName);
					break;
				case PDF:
					suffix = OzConstants.PDF_SUFFIX;
					String[] columnNames = DBMetaDataUtils.getResultSetColumnNames(resultSet);
					String pdfFilePath = PathUtils.getFullPath(reportsFolder, branch + suffix);
					ItextUtils.writePdf(pdfFilePath, columnNames, resultSet);
					break;
				}
				logger.info(stopWatch.appendElapsedTimeToMessage(
						"Emergency report processing has completed for branch " + branch + " in"));
				incrementProcessingCompletedCounter();
			} else {
				incrementNullResultSetCounter();
				logger.warning("Resultset for branch " + branch + " is null !");
			}
			try {
				connection.close();
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}

		} else {
			logger.warning("Connect attempt " + String.valueOf(processingStarted) + " failed for brach " + branch);
			incrementConnectionFailuresCounter();
		}
		EmergencyReports.processBranchProcessingCompletion(branch);
	}

}
