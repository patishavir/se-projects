package oz.infra.constants;

public final class OzConstants {
	private OzConstants() {
	}

	// integer constants
	public static final int INT_MINUS_ONE = -1;
	public static final int INT_0 = 0;
	public static final int INT_1 = 1;
	public static final int INT_10 = 10;
	public static final int INT_12 = 12;
	public static final int INT_150 = 150;
	public static final int INT_180 = 180;
	public static final int INT_100 = 100;
	public static final int INT_200 = 200;
	public static final int INT_256 = 256;
	public static final int INT_1000 = 1000;
	public static final int INT_1024 = 1024;
	public static final int INT_2000 = 2000;
	public static final int INT_10000 = 10000;
	public static final int INT_16384 = 16384;
	public static final int INT_15000 = 15000;
	public static final int INT_100000 = 100000;
	public static final int INT_128 = 128;
	public static final int INT_2 = 2;
	public static final int INT_20 = 20;
	public static final int INT_24 = 24;
	public static final int INT_25 = 25;
	public static final int INT_3 = 3;
	public static final int INT_30 = 30;
	public static final int INT_4 = 4;
	public static final int INT_5 = 5;
	public static final int INT_50 = 50;
	public static final int INT_5000 = 5000;
	public static final int INT_50000 = 50000;
	public static final int INT_500000 = 500000;
	public static final int INT_6 = 6;
	public static final int INT_60 = 60;
	public static final int INT_7 = 7;
	public static final int INT_72 = 72;
	public static final int INT_8 = 8;
	public static final int INT_80 = 80;
	public static final int INT_8192 = 8192;
	public static final int INT_9 = 9;
	public static final int INT_HUNDRED = 100;
	public static final int INT_THOUSAND = 1000;
	public static final int INT_MILLION = 1000000;
	public static final long LONG_0 = 0L;
	// string constants
	public static final String ASTERISK = "*";
	public static final String BACK_SLASH = "\\";
	public static final String BLANK = " ";
	public static final String CARRIAGE_RETURN = "\r";
	public static final String CARRIAGE_RETURN_LINE_FEED = "\r\n";
	public static final String WINDOWS_LINE_SEPARATOR = "\r\n";
	public static final String WINDOWS_FILE_SEPARATOR = "\\";
	public static final String UNIX_LINE_SEPARATOR = "\n";
	public static final String UNIX_FILE_SEPARATOR = "/";
	public static final String UNIX_TMP_FOLDER = "/tmp";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String DOT = ".";
	public static final String TILDE = "~";
	public static final String EQUAL_SIGN = "=";
	public static final String LINE_FEED = "\n";
	public static final String MINUS_SIGN = "-";
	public static final String HYPHEN = "-";
	public static final String SEMICOLON = ";";
	public static final String SLASH = "/";
	public static final String TAB = "\t";
	public static final String UNDERSCORE = "_";
	public static final String NUMBER_SIGN = "#";
	public static final String HASH = "#";
	public static final String PERCENT = "%";
	public static final String PLUS_SIGN = "+";
	public static final String EXCLAMATION_MARK = "!";
	public static final String QUESTION_MARK = "?";
	public static final String AT_SIGN = "@";
	public static final String GREATER_THAN = ">";
	public static final String LESS_THAN = "<";
	public static final String DOUBLE_QUOTE = "\"";
	public static final String QUOTE = "'";
	public static final String LEFT_CURLY_BRACE = "{";
	public static final String RIGHT_CURLY_BRACE = "}";
	public static final String LEFT_PARENTHESIS = "(";
	public static final String RIGHT_PARENTHESIS = ")";
	public static final String EMPTY_STRING = "";
	public static final String OFFICE_TEMP_FILE_PREFIX = "~$";
	//
	public static final String ASTERISKS_20 = "********************";
	//
	public static final String YES = "YES";
	public static final String NO = "NO";
	//
	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";
	//
	public static final String HTTP = "HTTP";
	// HTML
	public static final String HTML_HEADER_LTR = "<HTML DIR=\"LTR\">";
	public static final String HTML_BR = "<BR>";
	public static final int HTTP_STATUS_200 = 200;
	public static final int HTTP_STATUS_304 = 304;
	public static final int HTTP_STATUS_500 = 500;
	public static final String HTML_CLOSE = "</HTML>";
	// Exit Statuses
	public static final int EXIT_STATUS_OK = 0;
	public static final int EXIT_STATUS_ABNORMAL = -1;
	// java
	public static final int STRING_NOT_FOUND = -1;
	//
	public static final String AIX = "AIX";
	public static final String WINDOWS = "Windows";
	//
	public static final String BAT_SUFFIX = ".bat";
	public static final String CSV_SUFFIX = ".csv";
	public static final String EAR_SUFFIX = ".ear";
	public static final String ERR_SUFFIX = ".err";
	public static final String LOG_SUFFIX = ".log";
	public static final String HTML_SUFFIX = ".html";
	public static final String PDF_SUFFIX = ".pdf";
	public static final String PNG_SUFFIX = ".png";
	public static final String PROPERTIES_SUFFIX = ".properties";
	public static final String SH_SUFFIX = ".sh";
	public static final String TXT_SUFFIX = ".txt";
	public static final String XLS_SUFFIX = ".xls";
	public static final String XLSX_SUFFIX = ".xlsx";
	public static final String XML_SUFFIX = ".xml";
	public static final String ZIP_SUFFIX = ".zip";

	public static final String LOCALE_iw_IL = "iw-IL";
	public static final String LOCALE_en_US = "en-US";

	public static final String NULL_STRING = "null";

	public static final String WIN1252_ENCODING = "Cp1252";
	public static final String WIN1255_ENCODING = "Cp1255";

	public static final String WIN1255_CHARSET = "windows-1255";
	public static final String UTF8_CHARSET = "UTF-8";
	public static final String ISO_8859_1 = "ISO-8859-1";

	public static final String LOCAL_HOST = "localhost";

	public static final String RESOURCE_REFERENCE_PREFIX = "java:comp/env/";

	// FEFF is the Unicode char represented by the UTF-8 byte order mark (EF BB
	// BF).
	public static final String UTF8_BOM = "\uFEFF";

	public static final String LATIN_ALPHABET_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LATIN_ALPHABET_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

	public static final String HEBREW_CHARS = "\u05d0\u05d1\u05d2\u05d3\u05d4\u05d5\u05d6\u05d7\u05d8\u05d9\u05db\u05da\u05dc\u05de\u05dd\u05e0\u05df\u05e1\u05e2\u05e4\u05e3\u05e6\u05e5\u05e7\u05e8\u05e9\u05ea";

	public static final String KRB5_MECH_OID = "1.2.840.113554.1.2.2";
	public static final String SPNEGO_MECH_OID = "1.3.6.1.5.5.2";
	
	// Windows shell commands
	public static final String PAUSE = "pause";
}