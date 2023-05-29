package oz.temp.json.jackson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonJacksonUtils1 {

	public static void main(String args[]) {
		String jsonString = "{\"libraryname\":\"My Library\",\"mymusic\":[{\"Artist Name\":\"Aaron\",\"Song Name\":\"Beautiful\"},{\"Artist Name\":\"Britney\",\"Song Name\":\"Oops I did It Again\"},	{\"Artist Name\":\"Britney\",\"Song Name\":\"Stronger\"}]}";
		ObjectMapper mapper = new ObjectMapper();
		Library lib = null;
		try {
			lib = mapper.readValue(jsonString, Library.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(lib.toString());
		System.out.println(lib.getAsString());
	}

	private void xxx() {
		ObjectMapper mapper = new ObjectMapper();
		Map m = new HashMap<String, String>();
		Map personMap = new HashMap<String, String>();
		Map personDetail = new HashMap<String, String>();
		personDetail.put("firstname", "Bob");
		personDetail.put("lastname", "jackson");
		personDetail.put("age", "12");
		personDetail.put("city", "Berlin");
		personMap.put("person", personDetail);
		// convert Map to json string
		try {
			System.out.println(mapper.writeValueAsString(personMap));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// convert json to Map
		String json = "{\"person\":{\"age\":\"12\",\"lastname\":\"jackson\"" + ",\"firstname\":\"Bob\",\"city\":\"Berlin\"}}";
		try {
			Map<String, String> map = mapper.readValue(json, Map.class);
			System.out.println("Map is " + map);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}