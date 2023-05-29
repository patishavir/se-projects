package oz.utils.p8.log;

public class P8LogAnalyzerParameters {
	private static  String filePath = null;
	private static  String eventDelimiterRegexp = "^\\d\\d\\d\\d-[0-1][0-9]-[0-5][0-9]T";

	public static String getEventDelimiterRegexp() {
		return eventDelimiterRegexp;
	}

	public static String getFilePath() {
		return filePath;
	}

	public static void setEventDelimiterRegexp(String eventDelimiterRegexp) {
		P8LogAnalyzerParameters.eventDelimiterRegexp = eventDelimiterRegexp;
	}

	public static void setFilePath(String filePath) {
		P8LogAnalyzerParameters.filePath = filePath;
	}

}
