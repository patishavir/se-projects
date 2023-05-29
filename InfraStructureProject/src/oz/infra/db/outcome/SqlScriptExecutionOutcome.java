package oz.infra.db.outcome;

import java.util.List;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;

public class SqlScriptExecutionOutcome {
	private String scriptFilePath;
	private List<SqlExecutionOutcome> sqlExecutionOutcomeList;
	private boolean successfullExecution = true;
	private int failuredCount = 0;
	private int successCount = 0;
	private String summayLine = null;
	private static Logger logger = JulUtils.getLogger();

	public SqlScriptExecutionOutcome(final List<SqlExecutionOutcome> sqlExecutionOutcomeList,
			final String scriptFilePath) {
		this.sqlExecutionOutcomeList = sqlExecutionOutcomeList;
		this.scriptFilePath = scriptFilePath;
		if (sqlExecutionOutcomeList == null) {
			logger.warning("Sql execution outcome List is null !");
		} else {
			for (SqlExecutionOutcome sqlExecutionOutcome : sqlExecutionOutcomeList) {
				if (!sqlExecutionOutcome.isExecutionSuccessful()) {
					successfullExecution = false;
					failuredCount++;
				} else {
					successCount++;
				}
			}
		}
	}

	public final String getScriptFilePath() {
		return scriptFilePath;
	}

	public final List<SqlExecutionOutcome> getSqlExecutionOutcomeList() {
		return sqlExecutionOutcomeList;
	}

	public final boolean isSuccessfullExecution() {
		return successfullExecution;
	}

	public StringBuilder print() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n" + PrintUtils.getSeparatorLine("Script path: " + scriptFilePath, OzConstants.HASH));
		sb.append(SqlExecutionOutcome.print(sqlExecutionOutcomeList));
		StringBuilder summarySb = new StringBuilder();
		summarySb.append("\n" + String.valueOf(successCount + failuredCount) + " statements have been executed.");
		summarySb.append("\n" + String.valueOf(failuredCount) + " statements execution have failed.");
		summayLine = "\n" + scriptFilePath + summarySb.toString();
		sb.append(summarySb);
		return sb;
	}

	public String getSummayLine() {
		return summayLine;
	}
}
