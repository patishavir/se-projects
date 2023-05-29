package oz.snifit;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import oz.infra.collection.CollectionUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.db.DBUtils;
import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.logging.jul.JulUtils;

public class SynteticMonitor2 {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		Properties connetionProperties = new Properties();
		Connection connection = DBUtils.getConnection("O:\\Scripts\\java\\db2\\dbproperties\\W284UEA8\\QZ2.properties",
				EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME);
		logger.info(connection.toString());
		getDatabaseStatus(connection);
	}

	// =========================================================================================================================
	public static String[] getDatabaseStatus(final Connection conn) {
		DataSource ds = null;

		try {

			DatabaseMetaData metaData;
			try {
				metaData = conn.getMetaData();
			} catch (SQLException e) {
				return new String[] { "", Boolean.FALSE.toString(), "could not get metadata", e.getMessage() };
			}
			String dbName;
			try {
				String url = metaData.getURL();
				dbName = url.substring(url.lastIndexOf('/') + 1);
			} catch (SQLException e) {
				return new String[] { "", Boolean.FALSE.toString(), "could not get database name", e.getMessage() };
			}
			String username;
			try {
				username = metaData.getUserName();
				username = "MATAFAPP";
			} catch (SQLException e) {
				return new String[] { dbName, Boolean.FALSE.toString(), "could not get username", e.getMessage() };
			}
			ResultSet tables = null;
			HashSet<String> usertables = new HashSet<String>();
			int tableCount = 0;
			try {
				String[] types = { "TABLE" };
				tables = metaData.getTables(null, "MATAF", "%", types);
				while (tables.next()) {
					tableCount++;
					usertables.add(tables.getString("TABLE_NAME"));
				}
			} catch (SQLException e) {
				return new String[] { dbName, Boolean.FALSE.toString(), "could not get tables", e.getMessage() };
			} finally {
				if (tables != null)
					try {
						tables.close();
					} catch (SQLException e) {
					}
			}
			CollectionUtils.printCollection(usertables, "title", Level.INFO, ",");
			ResultSet privileges = null;
			HashSet<String> selectprivs = new HashSet<String>();
			HashSet<String> insertprivs = new HashSet<String>();
			HashSet<String> updateprivs = new HashSet<String>();
			HashSet<String> deleteprivs = new HashSet<String>();
			try {
				privileges = metaData.getTablePrivileges(null, "MATAF", "%");
				// DBUtils.printResultSet(privileges);
				// System.exit(1);
				while (privileges.next()) {
					String tableNam = privileges.getString("TABLE_NAME");
					if (tableNam.equals("SDST_SUG_CHIYUV")) {
						ResultSetUtils.printResultSet(privileges);
					}
					logger.info("tableNam: " + tableNam);
					String grantee = privileges.getString("GRANTEE");
					String privilege = privileges.getString("PRIVILEGE");
					if (grantee.equalsIgnoreCase(username)) {
						if (privilege.equalsIgnoreCase("SELECT")) {
							selectprivs.add(tableNam);
						} else if (privilege.equalsIgnoreCase("INSERT")) {
							insertprivs.add(tableNam);
						} else if (privilege.equalsIgnoreCase("UPDATE")) {
							updateprivs.add(tableNam);
						} else if (privilege.equalsIgnoreCase("DELETE")) {
							deleteprivs.add(tableNam);
						}
					}
				}
			} catch (SQLException e) {
				return new String[] { dbName, Boolean.FALSE.toString(), "could not get tables privileges",
						e.getMessage() };
			} finally {
				if (privileges != null)
					try {
						privileges.close();
					} catch (SQLException e) {
					}
			}
			usertables.removeAll(selectprivs);
			if (usertables.size() > 0) {
				return new String[] { dbName, Boolean.FALSE.toString(),
						"user:" + username + " does not have SELECT privilege on table:" + usertables, "" };
			}
			ResultSet synonyms = null;
			HashSet<String> usersynonyms = new HashSet<String>();
			int synonymsCount = 0;
			try {
				String[] types = { "SYNONYM" };
				synonyms = metaData.getTables(null, "MATAF", "%", types);
				while (synonyms.next()) {
					synonymsCount++;
					usersynonyms.add(synonyms.getString("TABLE_NAME"));
				}
			} catch (SQLException e) {
				return new String[] { dbName, Boolean.FALSE.toString(), "could not get synonyms", e.getMessage() };
			} finally {
				if (synonyms != null)
					try {
						synonyms.close();
					} catch (SQLException e) {
					}
			}
			HashSet<String> testset = new HashSet<String>(usersynonyms);
			testset.removeAll(selectprivs);
			if (testset.size() > 0) {
				return new String[] { dbName, Boolean.FALSE.toString(),
						"user:" + username + " does not have SELECT privilege on table:" + testset, "" };
			}
			testset = new HashSet<String>(usersynonyms);
			testset.removeAll(insertprivs);
			if (testset.size() > 0) {
				return new String[] { dbName, Boolean.FALSE.toString(),
						"user:" + username + " does not have INSERT privilege on table:" + testset, "" };
			}
			testset = new HashSet<String>(usersynonyms);
			testset.removeAll(updateprivs);
			if (testset.size() > 0) {
				return new String[] { dbName, Boolean.FALSE.toString(),
						"user:" + username + " does not have UPDATE privilege on table:" + testset, "" };
			}
			testset = new HashSet<String>(usersynonyms);
			testset.removeAll(deleteprivs);
			if (testset.size() > 0) {
				return new String[] { dbName, Boolean.FALSE.toString(),
						"user:" + username + " does not have DELETE privilege on table:" + testset, "" };
			}
			ResultSet views;
			try {
				String[] types = { "VIEW" };
				views = metaData.getTables(null, "MATAF", "%", types);
			} catch (SQLException e) {
				return new String[] { dbName, Boolean.FALSE.toString(), "could not get views", e.getMessage() };
			}
			int viewCount = 0;
			try {
				while (views.next()) {
					viewCount++;
				}
			} catch (SQLException e) {
				return new String[] { dbName, Boolean.FALSE.toString(), "could not count views", e.getMessage() };
			}
			return new String[] { dbName, Boolean.TRUE.toString(),
					"USER:" + username + ",TBLS:" + tableCount + ",NICKS:" + synonymsCount + ",VIEWS:" + viewCount, "" };
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
	}

}
