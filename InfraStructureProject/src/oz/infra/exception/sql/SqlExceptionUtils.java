package oz.infra.exception.sql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class SqlExceptionUtils {
	private static Map<String, String> messageMap = new HashMap<String, String>();
	private static final Logger logger = JulUtils.getLogger();
	static {
		messageMap.put("     -1776", "The command cannot be issued on an HADR standby database.");
		messageMap.put("28000-4214", "User ID or Password invalid.");
		messageMap.put("22001-433",
				"An update or insert value is a string that is too long for the column, right truncation occurred.");
		messageMap.put("23505-803", "Duplicate key error. Unique key violation.");
		messageMap.put("42501-551", "Does not have the privilege to perform operation on object.");
		messageMap.put("42601-104", "Syntax error - Illegal symbol in statement");
		messageMap.put("42703-206", "object-name is not valid in the context where it is used");
		messageMap.put("42704-204", "The object identified by name is not defined in the DB2 subsystem.");
		messageMap.put("42710-601", "Duplicate object or constraint name.");
		messageMap.put("42711-612", "Duplicate name.");
		messageMap.put("42809-159", "The statement references object which identifies object-type rather than expected-object-type");
		messageMap.put("42884-440",
				"No authorized routine-type by the name routine-names having compatible arguments was found.");
		messageMap.put("42889-624",
				"The Table already has a primary key or unique key constraint with specified columns.");		
	}

	public static String explainMessage(final SQLException sqlex) {
		String explanation = messageMap.get(getKey(sqlex));
		logger.finest("explanation: " + explanation);
		return explanation;
	}

	private static String getKey(final SQLException sqlex) {
		String sqlState = sqlex.getSQLState();
		int errorCode = sqlex.getErrorCode();
		String key = sqlState + String.valueOf(errorCode);
		logger.finest("key:" + key);
		return key;
	}
}
