package oz.infra.db.sql.comment;

import java.util.logging.Handler;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class SqlCommentsUtils {
	public static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;
	public static final String BLOCK_COMMENT_PREFIX = "/*";
	public static final String BLOCK_COMMENT_SUFFIX = "*/";
	public static final String COMMENT_PREFIX = "--";
	private static final Logger logger = JulUtils.getLogger();

	private static final String REMOVED_COMMENTS_LOG_NAME = "removedCommentsLogger";
	private static final String REMOVED_COMMENTS_LOG_PATH = "c:/temp/removedCommentsLogger.log";
	private static final Logger removedCommentsLogger = JulUtils.getLogger(REMOVED_COMMENTS_LOG_NAME, false);

	static {
		try {
			Handler fileHandler = JulUtils.getFileHandler(REMOVED_COMMENTS_LOG_PATH);
			removedCommentsLogger.addHandler(fileHandler);
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
		}
	}

	public static String removeBlockComments(final String sqlStatementString) {
		String exitMessage = null;
		final String blockCommentMismatch = "Block comment mismatch found.\nprocessing has been terminated.";
		StringBuilder includedSb = new StringBuilder();
		StringBuilder removedSb = new StringBuilder();
		int[] prefixIndexesArray = StringUtils.getSubStringIndexes(sqlStatementString, BLOCK_COMMENT_PREFIX);
		int[] suffixIndexesArray = StringUtils.getSubStringIndexes(sqlStatementString, BLOCK_COMMENT_SUFFIX);
		logger.finest("prefix index array: " + ArrayUtils.getAsDelimitedString(prefixIndexesArray));
		logger.finest("suffix index array: " + ArrayUtils.getAsDelimitedString(suffixIndexesArray));
		int prefixIndexesArrayLength = prefixIndexesArray.length;
		if (prefixIndexesArray.length != suffixIndexesArray.length) {
			exitMessage = blockCommentMismatch;
		}
		for (int i = 0; i < prefixIndexesArray.length; i++) {
			if (prefixIndexesArray[i] > suffixIndexesArray[i]) {
				exitMessage = blockCommentMismatch;
			}
		}
		// nested block comments not allowed
		for (int i = 0; i < prefixIndexesArray.length - 1; i++) {
			if (suffixIndexesArray[i] > prefixIndexesArray[i + 1]) {
				exitMessage = blockCommentMismatch;
			}
		}
		if (exitMessage != null) {
			logger.warning("\n".concat(includedSb.toString()));
			SystemUtils.printMessageAndExit(exitMessage, OzConstants.EXIT_STATUS_ABNORMAL, false);
		}
		int beginIndex = 0;
		for (int i = 0; i < prefixIndexesArrayLength; i++) {
			includedSb.append(sqlStatementString.substring(beginIndex, prefixIndexesArray[i]));
			removedSb.append(sqlStatementString.substring(prefixIndexesArray[i],
					suffixIndexesArray[i] + BLOCK_COMMENT_SUFFIX.length()));
			beginIndex = suffixIndexesArray[i] + BLOCK_COMMENT_SUFFIX.length();
		}
		includedSb.append(sqlStatementString.substring(beginIndex));
		String removedString = removedSb.toString().trim();
		if (removedString.length() > 0) {
			removedCommentsLogger.info("------------ removed:\n".concat(removedString));
		}
		int diff = sqlStatementString.length() - includedSb.length() - removedSb.length();
		if (diff != 0) {
			logger.info(StringUtils.concat("sqlStatementString length: ", String.valueOf(sqlStatementString.length()),
					" result String: ", String.valueOf(includedSb.length()), " removed String: ",
					String.valueOf(removedSb.length()), " diff: ", String.valueOf(diff)));
		}
		String[] includedArray = includedSb.toString().split(OzConstants.CARRIAGE_RETURN_LINE_FEED);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < includedArray.length; i++) {
			if (includedArray[i].trim().length() > 0) {
				sb.append(includedArray[i]);
				sb.append(LINE_SEPARATOR);
			}
		}
		return sb.toString();
	}

	public static String removeComments(final String sqlStatementString) {
		String[] sqlStatementsArray = sqlStatementString.split(OzConstants.LINE_FEED);
		StringBuilder includedSb = new StringBuilder();
		StringBuilder removedSb = new StringBuilder();
		for (String line : sqlStatementsArray) {
			line = line.trim();
			String[] lineArray = line.trim().split(RegexpUtils.REGEXP_WHITE_SPACE);
			if (lineArray.length > 0) {
				if (lineArray[0].equals(COMMENT_PREFIX)) {
					removedSb.append(line);
					removedSb.append(LINE_SEPARATOR);
				} else {
					includedSb.append(line);
					includedSb.append(LINE_SEPARATOR);
				}
			}
		}
		if (removedSb.length() > 0) {
			removedCommentsLogger.info("------------ removed:\n" + removedSb.toString().trim());
		}
		return includedSb.toString();
	}
}
