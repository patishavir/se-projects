package oz.temp.json.jackson;

import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import oz.infra.httpserver.AccessLogRecord;
import oz.infra.logging.jul.JulUtils;

public class JsonJacksonUtils {
	private static String jsonString = "{\"remoteHost\": \"10.12.101.197\" , \"timeStamp\":  \"10/Feb/2019:13:42:09-854\",  \"requestFirstLine\": \"GET /favicon.ico HTTP/1.1\" , \"status\": \"403\" , \"bytes\": 286 ,\"websphereApplicationServer\": \"-\" ,\"responseTime\":266 ,\"referer\": \"-\" ,\"host\": \"suswastest2.fibi.corp\" ,\"jsessionId\": \"-\"   ,\"userAgent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36]\" }";
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		jsonStringToJavaObject();
	}

	private static void jsonStringToJavaObject() {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			// AccessLogRecord accessLogRecord = new AccessLogRecord();
			// accessLogRecord = objectMapper.readValue(jsonString, accessLogRecord.getClass());
			 Object object = objectMapper.readValue(jsonString, AccessLogRecord.class);
			AccessLogRecord accessLogRecord = (AccessLogRecord) object;
			logger.info(accessLogRecord.getHost());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
