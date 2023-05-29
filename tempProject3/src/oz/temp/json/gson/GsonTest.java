package oz.temp.json.gson;

import java.util.Map;

public class GsonTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jsonObjectString = "{\"remoteHost\": \"10.12.101.197\" , \"timeStamp\":  \"05/Feb/2019:13:52:44-389\",  \"requestFirstLine\": \"GET /favicon.ico HTTP/1.1\" , \"status\": \"403\" , \"bytes\": 286 ,\"websphereApplicationServer\": \"-\" ,\"responseTime\":261 ,\"referer\": \"-\" ,\"host\": \"suswastest2.fibi.corp\" ,\"jsessionId\": \"-\"   ,\"userAgent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36]\" }";
		Map<String, Object> mapObj = oz.infra.json.gson.GsonUtils.getMapFromJsonString(jsonObjectString);
		String myJsonKey = "remoteHost";
		String strValue = (String) mapObj.get(myJsonKey);
		boolean contains = mapObj.containsKey(myJsonKey);
		System.out.println(strValue);
		System.out.println(contains);
	}
}