package oz.infra.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import oz.infra.exception.ExceptionUtils;

public class JacksonUtils {
	public static <T> String javaObject2Json(T genericObject) {
		ObjectMapper mapper = new ObjectMapper();
		// Converting the Object to JSONString
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(genericObject);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return jsonString;
	}

}