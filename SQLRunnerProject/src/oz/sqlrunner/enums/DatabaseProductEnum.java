package oz.sqlrunner.enums;

import oz.sqlrunner.handlers.enums.LimitNumberOfRowsClauseTypeEnum;

public enum DatabaseProductEnum {
	IBM_DB2_ALIAS("COM.ibm.db2.jdbc.app.DB2Driver", "FETCH FIRST MAX_NUMBER_OF_ROWS ROWS ONLY",
			LimitNumberOfRowsClauseTypeEnum.valueOf("APPEND"),
			"SELECT DISTINCT CREATOR FROM SYSIBM.SYSTABLES",
			"SELECT CREATOR, NAME, TYPE, COLCOUNT, REMARKS  FROM SYSIBM.SYSTABLES WHERE TYPE IN ('T','V')"), IBM_DB2_UNIVERSAL_TYPE(
			"com.ibm.db2.jcc.DB2Driver", "FETCH FIRST MAX_NUMBER_OF_ROWS ROWS ONLY",
			LimitNumberOfRowsClauseTypeEnum.valueOf("APPEND"),
			"SELECT DISTINCT CREATOR FROM SYSIBM.SYSTABLES",
			"SELECT CREATOR, NAME, TYPE, COLCOUNT, REMARKS  FROM SYSIBM.SYSTABLES WHERE TYPE IN ('T','V')"), ORACLE(
			"oracle.jdbc.driver.OracleDriver", "WHERE ROWNUM <= MAX_NUMBER_OF_ROWS",
			LimitNumberOfRowsClauseTypeEnum.valueOf("WHERECLAUSE"), null, null), SQL_SERVER(
			"com.microsoft.sqlserver.jdbc.SQLServerDriver", "SELECT TOP MAX_NUMBER_OF_ROWS",
			LimitNumberOfRowsClauseTypeEnum.valueOf("SELECT"), null, null), MYSQL(
			"com.mysql.jdbc.Driver", "LIMIT MAX_NUMBER_OF_ROWS", LimitNumberOfRowsClauseTypeEnum
					.valueOf("APPEND"), null, null);
	private String jdbcDriverClass;
	private String limitNumberOfRowsClause;
	private String getSchemasSelectStatementString;
	private String objectBrowsersqlStatementString;
	private LimitNumberOfRowsClauseTypeEnum limitNumberOfRowsClauseType;

	private DatabaseProductEnum(final String jdbcDriverClass, final String selectTopString,
			final LimitNumberOfRowsClauseTypeEnum limitNumberOfRecordsClauseType,
			final String getSchemasSelectStatement, final String objectBrowsersqlStatementString) {
		this.jdbcDriverClass = jdbcDriverClass;
		this.limitNumberOfRowsClause = selectTopString;
		this.limitNumberOfRowsClauseType = limitNumberOfRecordsClauseType;
		this.getSchemasSelectStatementString = getSchemasSelectStatement;
		this.objectBrowsersqlStatementString = objectBrowsersqlStatementString;
	}

	public String getJdbcDriverClass() {
		return jdbcDriverClass;
	}

	public String getLimitNumberOfRowsClause() {
		return limitNumberOfRowsClause;
	}

	public LimitNumberOfRowsClauseTypeEnum getLimitNumberOfRowsClauseType() {
		return limitNumberOfRowsClauseType;
	}

	public String getGetSchemasSelectStatement() {
		return getSchemasSelectStatementString;
	}

	public String getObjectBrowsersqlStatementString() {
		return objectBrowsersqlStatementString;
	}
}
