package oz.infra.json.gson;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonUtils {
	private GsonUtils() {
	}
	/*
	 * mod_log_config LogFormat
	 * "{\"remoteHost\": \"%h\" , \"timeStamp\":  \"%{%d/%b/%Y:%T}t-%{msec_frac}t\",  \"requestFirstLine\": \"%r\" , \"status\": \"%>s\" , \"bytes\": %b ,\"websphereApplicationServer\": \"%{WAS}e\" ,\"responseTime\":%D ,\"referer\"
	 * : \ "%{Referer}i\" ,\"host\": \"%{Host}i\" ,\"jsessionId\": \"%{JSESSIONID}C\"   ,\"userAgent\": \"%{User-agent}i]\" }" json
	 *
	 */
	public static Map<String, Object> getMapFromJsonString(final String jsonObjectString) {
		return new Gson().fromJson(jsonObjectString, new TypeToken<HashMap<String, Object>>() {
		}.getType());
	}
}