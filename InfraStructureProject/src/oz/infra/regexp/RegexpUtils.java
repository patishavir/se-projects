package oz.infra.regexp;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;

public final class RegexpUtils {
	private static Logger logger = JulUtils.getLogger();

	public static final String REGEXP_WHITE_SPACE = "\\s+";

	public static final String REGEXP_NON_WHITE_SPACE = "\\S+";
	public static final String REGEXP_DIGIT = "\\d";
	public static final String REGEXP_DIGITS = "\\d+";
	public static final String REGEXP_NON_DIGIT = "\\D";
	public static final String REGEXP_ALPHA = "[a-zA-Z]";
	public static final String REGEXP_ALPHA_LOWER_CASE = "[a-z]";
	public static final String REGEXP_ALPHA_UPPER_CASE = "[A-Z]";
	public static final String REGEXP_ANYSTRING = ".*";
	public static final String REGEXP_DOT = "\\.";
	public static final String REGEXP_BACK_SLASH = "\\\\";
	public static final String REGEXP_DOT_OR_UNDERSCORE = "[\\.,_}";
	public static final String REGEXP_FLOATING_POINT_NUMBER = "^[-+]?[0-9]*\\.?[0-9]+$";
	public static final String REGEXP_ESCAPE_ALL_START = "\\Q";
	public static final String REGEXP_ESCAPE_ALL_END = "\\E";
	public static final String REGEXP_CELL_PHONE_NUMBER = "\\d{3}-\\d{7}";
	public static final String REGEXP_EAR_FILE = "\\S+\\.[eE][aA][rR]$";
	public static final String REGEXP_EAR_OR_WAR_FILE = "\\S+\\.[eE][aA][rR]$|\\S+\\.[wW][aA][rR]$";
	public static final String REGEXP_JAVA_FILE = "\\S+\\.[jJ][aA][vV][aA]$";
	public static final String REGEXP_SQL_FILE = ".+\\.[sS][qQ][lL]";
	public static final String REGEXP_WAR_FILE = "\\S+\\.[wW][aA][rR]$";
	public static final String REGEXP_ZIP_FILE = ".+\\.[zZ][iI][pP]$";
	public static final String REGEXP_PROPERTIES_FILE = ".+\\.[pP][rR][oO][pP][eE][rR][tT][iI][eE][sS]$";
	public static final String REGEXP_ANY_FILE_SEPARATOR = "/|\\\\";
	public static final String REGEXP_DDMMYYY_DATE = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[012])(19|20)\\d\\d$";

	public static final String REGEXP_IPV4_ADDRESS = "\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
			+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
			+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";

	public static boolean find(final String inputString, final String regexpression) {
		// true if subsequence of inputString matches regexpression
		Pattern pattern = Pattern.compile(regexpression);
		Matcher matcher = pattern.matcher(inputString);
		boolean matchFound = matcher.find();
		logger.finest(inputString + OzConstants.TAB + regexpression + OzConstants.TAB + matchFound);
		return matchFound;
	}

	public static boolean lookingAt(final String inputString, final String regexpression) {
		// true if prefix of inputString matches regexpression
		Pattern pattern = Pattern.compile(regexpression);
		Matcher matcher = pattern.matcher(inputString);
		boolean matchFound = matcher.lookingAt();
		logger.finest(inputString + OzConstants.TAB + regexpression + OzConstants.TAB + matchFound);
		return matchFound;
	}

	public static boolean matches(final String inputString, final Pattern pattern) {
		// true if subsequence of inputString matches regexpression
		boolean matchFound = false;
		if ((inputString != null) && (pattern != null)) {
			Matcher matcher = pattern.matcher(inputString);
			matchFound = matcher.matches();
		}
		logger.finest(inputString + OzConstants.TAB + pattern.toString() + OzConstants.TAB + matchFound);
		return matchFound;
	}

	public static boolean matches(final String inputString, final String regexpression) {
		// true if subsequence of inputString matches regexpression
		Pattern pattern = Pattern.compile(regexpression);
		return matches(inputString, pattern);
	}

	private RegexpUtils() {
	}
}
