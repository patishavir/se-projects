package oz.test.infra;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.Test;

import oz.infra.db.DBUtils;
import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.logging.jul.JulUtils;

public class TestDBUtils {
	private static Logger logger = JulUtils.getLogger();
	/**
	 * @param args
	 */
	@Test
	public void getResultSetAsHashMap() {
		logger.info("starting test ...");
		// Connection connection = HermeshUtils.getConnection2HRUCM();
		Connection connection = null;
		if (connection == null) {
			logger.severe("Error: No active Connection");
			assertTrue(false);
		} else {
			String bugReportSqlStatementString = "SELECT id, closedinversion from cqhrucm.testdirector WHERE closedinversion IS NOT NULL";
			HashMap<String, String> bugReportHashMap = ResultSetUtils
					.getResultSetAsHashMap(connection,
							bugReportSqlStatementString);
			String drishaSqlStatementString = "select distinct T1.id,T17.id from ( ( Defect T1 LEFT OUTER JOIN parent_child_links T17mm ON T1.dbid = T17mm.parent_dbid  and 16778720 = T17mm.parent_fielddef_id  ) LEFT OUTER JOIN enttable T17 ON T17mm.child_dbid = T17.dbid  ) where T1.dbid <> 0";
			HashMap<String, String> drishaHashMap = ResultSetUtils
					.getResultSetAsHashMap(connection, drishaSqlStatementString);
			DBUtils.closeConnection(connection);
			assertTrue(true);
		}

	}
}
