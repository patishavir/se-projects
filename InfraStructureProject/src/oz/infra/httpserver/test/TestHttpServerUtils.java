package oz.infra.httpserver.test;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import oz.infra.httpserver.AccessLogRecord;
import oz.infra.httpserver.HttpServerUtils;
import oz.infra.io.FileUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class TestHttpServerUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// testgetFileNamesFromWebPage();
		testJsonStringToAccessLogRecordObject();
	}

	private static void testgetFileNamesFromWebPage() {
		String filePath = "./files/fileListPage.html";
		File file = new File(filePath);
		logger.info(file.getAbsolutePath());
		if (!file.exists()) {
			logger.info(file.getAbsolutePath() + " does not exist !");
		}
		String pageContents = FileUtils.readTextFile(file);
		logger.info(pageContents);
		ArrayList<String> fileNamesList = HttpServerUtils.getFileNamesFromWebPage(pageContents);
		logger.info(ListUtils.getAsDelimitedString(fileNamesList));
	}

	private static void testJsonStringToAccessLogRecordObject() {
		final String jsonString = "{\"remoteHost\": \"10.12.101.197\" , \"timeStamp\":  \"10/Feb/2019:13:42:09-854\",  \"requestFirstLine\": \"GET /favicon.ico HTTP/1.1\" , \"status\": \"403\" , \"bytes\": 286 ,\"websphereApplicationServer\": \"-\" ,\"responseTime\":266 ,\"referer\": \"-\" ,\"host\": \"suswastest2.fibi.corp\" ,\"jsessionId\": \"-\"   ,\"userAgent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36]\" }";
		AccessLogRecord accessLogRecord = HttpServerUtils.jsonStringToAccessLogRecordObject(jsonString);
		logger.info(accessLogRecord.getAsString());
	}

}