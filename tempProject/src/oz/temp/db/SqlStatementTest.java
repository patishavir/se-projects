package oz.temp.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.db.DBUtils;
import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.logging.jul.JulUtils;

public class SqlStatementTest {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		runTest(args[0]);
	}

	private static void runTest(final String propertiesFilePath) {
		Connection connection = DBUtils.getConnection(propertiesFilePath);
		logger.info(connection.toString());
		try {
			Statement statement = connection.createStatement();
			String sqlStatement = "Select * from MATAF.glst_snif\nopiupoiuoiu -- ballah balhh\n--\n   /* jkljkllj \n"
					+ "jkjkljkllkll" + "*/" + "opiupoiuoiu";
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			String[] output = ResultSetUtils.getResultSetAs1DimArray(resultSet);
			ArrayUtils.printArray(output);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
