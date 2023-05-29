package oz.infra.db.metadata;

import java.sql.ResultSetMetaData;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.print.PrintUtils;

public class ColumnMetaData {
	private String catalogName = null;
	private String columnClassName = null;
	private String columnTypeName = null;
	private String schemaName = null;
	private String tableName = null;
	private int columnDisplaySize = -1;
	private int columnType = -1;
	private int precision = -1;
	private boolean nullable = true;
	private String columnLabel = null;
	private String columnName = null;
	private boolean autoIncrement = false;
	private boolean caseSensitive = false;
	private boolean readOnly = false;
	private boolean isSigned = false;

	public ColumnMetaData(final ResultSetMetaData resultSetMetaData, final int columnIndex) {
		try {
			catalogName = resultSetMetaData.getCatalogName(columnIndex);
			columnClassName = resultSetMetaData.getColumnClassName(columnIndex);
			columnTypeName = resultSetMetaData.getColumnTypeName(columnIndex);
			schemaName = resultSetMetaData.getSchemaName(columnIndex);
			tableName = resultSetMetaData.getTableName(columnIndex);
			columnDisplaySize = resultSetMetaData.getColumnDisplaySize(columnIndex);
			columnType = resultSetMetaData.getColumnType(columnIndex);
			precision = resultSetMetaData.getPrecision(columnIndex);
			nullable = resultSetMetaData.isNullable(columnIndex) == ResultSetMetaData.columnNullable;
			columnLabel = resultSetMetaData.getColumnLabel(columnIndex);
			columnName = resultSetMetaData.getColumnName(columnIndex);
			autoIncrement = resultSetMetaData.isAutoIncrement(columnIndex);
			caseSensitive = resultSetMetaData.isCaseSensitive(columnIndex);
			readOnly = resultSetMetaData.isReadOnly(columnIndex);
			isSigned = resultSetMetaData.isSigned(columnIndex);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	public String getCatalogName() {
		return catalogName;
	}

	public String getColumnClassName() {
		return columnClassName;
	}

	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}

	public String getColumnLabel() {
		return columnLabel;
	}

	public String getColumnName() {
		return columnName;
	}

	public int getColumnType() {
		return columnType;
	}

	public String getColumnTypeName() {
		return columnTypeName;
	}

	public int getPrecision() {
		return precision;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public boolean getAutoIncrement() {
		return autoIncrement;
	}

	public boolean getCaseSensitive() {
		return caseSensitive;
	}

	public boolean getNullable() {
		return nullable;
	}

	public boolean getReadOnly() {
		return readOnly;
	}

	public boolean getIsSigned() {
		return isSigned;
	}

	public String getAsCommaSeparate() {
		return PrintUtils.printObjectFields(this, OzConstants.COMMA,
				PrintUtils.PrintOption.HEADER_AND_DATA);
	}
}
