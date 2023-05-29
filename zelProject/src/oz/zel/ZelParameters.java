package oz.zel;

public class ZelParameters {

	public static final String dataCsvFilePath = "c:\\oj\\projects\\zelProject\\files\\data.csv";
	public static final String structureCsvFilePath = "c:\\oj\\projects\\zelProject\\files\\structure.csv";
	public static final String XML_HEADER1 = "<?xml version=\"1.0\" encoding=\"windows-1255\"?>"
			+ "\n" + "<MAT_HEADER>" + "\n" + "<MAT_NO_RECORDS>";
	public static final String XML_HEADER2 = "</MAT_NO_RECORDS>\n";
	public static final String XML_TAIL = "</MAT_HEADER>\n";
	public static final String RECORD_PREFIX = "<MAT_ARRAY>\n";
	public static final String RECORD_SUFFIX = "</MAT_ARRAY>\n";
	private static final String xmlFilePath = "c:\\oj\\projects\\zelProject\\files\\1.xml";

	public static String getXmlfilepath() {
		return xmlFilePath;
	}

}
