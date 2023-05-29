package oz.monitor.monitors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oz.infra.datetime.StopWatch;
import oz.infra.db.DBUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;
import oz.monitor.common.OzMonitorResponse;

public class DBMonitor extends AbstractOzMonitor {
	private String url;
	private String jdbcDriverClassName;
	private String sqlStatement;

	public DBMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		boolean isResourceAvailable = false;
		String ozMonitorMessage = getResource() + " is not available!";
		String ozMonitorLongMessage = null;
		Connection connection = null;
		int recordCount = -1;
		StopWatch stopWatch = new StopWatch();
		String userName = getUserName();
		try {
			String password = getPassword();
			boolean verbose = false;
			logger.finest("getting connection for url: " + url + " jdbcDriverClassName: " + jdbcDriverClassName
					+ " userName: " + userName);
			connection = DBUtils.getConnection(url, userName, password, jdbcDriverClassName, verbose);
			if (connection != null) {
				logger.info("getConnection for url: " + url + " user: " + userName + " jdbcDriverClassName:  "
						+ jdbcDriverClassName + " has completed  successfully");
				Statement sqlStatement2Execute = connection.createStatement();
				ResultSet resultSet = sqlStatement2Execute.executeQuery(sqlStatement);
				while (resultSet.next()) {
					recordCount = resultSet.getInt(1);
				}
				DBUtils.closeConnection(connection);
				isResourceAvailable = recordCount > 0;
				if (recordCount > 0) {
					long responseTime = stopWatch.getElapsedTimeLong();
					ozMonitorMessage = StringUtils.concat(getResource(), " is available! response time ",
							String.valueOf(responseTime), " milliseconds.");
				}
			}
		} catch (SQLException ex) {
			logger.warning("getConnection for url: " + url + " user: " + userName + " jdbcDriverClassName:  "
					+ jdbcDriverClassName + " has failed");
			ExceptionUtils.printMessageAndStackTrace(ex);
			ozMonitorMessage = ex.getMessage();
			ozMonitorLongMessage = ex.getMessage();
		}
		return generateOzMonitorResponse(isResourceAvailable, ozMonitorMessage, ozMonitorLongMessage);
	}

	public final void setJdbcDriverClassName(final String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
	}

	public final void setSqlStatement(final String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}

	public final void setUrl(final String url) {
		this.url = url;
	}
}
