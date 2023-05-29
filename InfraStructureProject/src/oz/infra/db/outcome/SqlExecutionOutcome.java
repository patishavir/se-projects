package oz.infra.db.outcome;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class SqlExecutionOutcome {
	private String[] columnNames = null;
	private String[][] resultSetArray = null;
	private String sqlStatementString = null;
	private String errorMessage = null;
	private boolean executionSuccessful = true;

	private static Logger logger = JulUtils.getLogger();

	public static StringBuilder print(final List<SqlExecutionOutcome> sqlExecutionOutcomeList) {
		StringBuilder sb = new StringBuilder();
		if (sqlExecutionOutcomeList != null) {
			for (SqlExecutionOutcome sqlExecutionOutcome : sqlExecutionOutcomeList) {
				sb.append(sqlExecutionOutcome.print().toString() + "\n");
			}
		}
		return sb;
	}

	public static void writeResultSetToFile(final List<SqlExecutionOutcome> sqlExecutionOutcomeList,
			final List<String> filePathsList) {
		if (sqlExecutionOutcomeList.size() != filePathsList.size()) {
			logger.warning("outcome list and file paths list have differnts sizes. execution terminated.");
		} else {
			int i = 0;
			for (SqlExecutionOutcome sqlExecutionOutcome : sqlExecutionOutcomeList) {
				writeResultSetToFile(sqlExecutionOutcome, filePathsList.get(i));
				i++;
			}
		}
	}

	public static void writeResultSetToFile(final SqlExecutionOutcome sqlExecutionOutcome, final String filePath) {
		String delimiter = OzConstants.COMMA;
		StringBuilder sb = new StringBuilder();
		if (sqlExecutionOutcome.columnNames != null) {
			sb.append(ArrayUtils.getAsDelimitedString(sqlExecutionOutcome.columnNames, delimiter));
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		if (sqlExecutionOutcome.resultSetArray != null) {
			sb.append(ArrayUtils.print2DimArray(sqlExecutionOutcome.resultSetArray, delimiter, Level.FINEST));
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		FileUtils.writeFile(filePath, sb.toString());
	}

	public SqlExecutionOutcome(final String sqlStatementString, final String[] columnNames,
			final String[][] resultSetArray) {
		this.sqlStatementString = sqlStatementString;
		this.columnNames = columnNames;
		this.resultSetArray = resultSetArray;
	}

	public final String[] getColumnNames() {
		return columnNames;
	}

	public final String getErrorMessage() {
		return errorMessage;
	}

	public final String[][] getResultSetArray() {
		return resultSetArray;
	}

	public String getSqlStatementString() {
		return sqlStatementString;
	}

	public boolean isExecutionSuccessful() {
		return executionSuccessful;
	}

	public final StringBuilder print(final Level... levelParam) {
		String delimiter = OzConstants.TAB;
		Level level = Level.FINEST;
		if (levelParam.length == 1) {
			level = levelParam[0];
		}
		StringBuilder sb = new StringBuilder(SystemUtils.LINE_SEPARATOR);
		if (sqlStatementString != null) {
			sb.append(sqlStatementString);
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		if (executionSuccessful) {
			sb.append("Execution has been successful !\n");
		} else {
			sb.append("******* Execution has failed ! *******\n");
		}
		if (columnNames != null) {
			sb.append(ArrayUtils.getAsDelimitedString(columnNames, delimiter));
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		if (resultSetArray != null) {
			sb.append(ArrayUtils.print2DimArray(resultSetArray, delimiter, Level.FINEST));
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		if (errorMessage != null) {
			sb.append(errorMessage);
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		logger.log(level, sb.toString());
		return sb;
	}

	public final void setErrorInfo(final String sqlStatementString, final String errorMessage) {
		this.sqlStatementString = sqlStatementString;
		this.errorMessage = errorMessage;
		this.executionSuccessful = false;
	}
}
