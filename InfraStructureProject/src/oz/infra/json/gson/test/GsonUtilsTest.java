package oz.infra.json.gson.test;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.json.gson.GsonUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;

public class GsonUtilsTest {
	private static String jsonObjectString = "{\"remoteHost\": \"10.12.101.197\" , \"timeStamp\":  \"10/Feb/2019:13:42:09-854\",  \"requestFirstLine\": \"GET /favicon.ico HTTP/1.1\" , \"status\": \"403\" , \"bytes\": 286 ,\"websphereApplicationServer\": \"-\" ,\"responseTime\":266 ,\"referer\": \"-\" ,\"host\": \"suswastest2.fibi.corp\" ,\"jsessionId\": \"-\"   ,\"userAgent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36]\" }";
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, Object> accessLogMap = GsonUtils.getMapFromJsonString(jsonObjectString);
		MapUtils.printMap(accessLogMap, "access log record map" ,Level.INFO);
	}

}
