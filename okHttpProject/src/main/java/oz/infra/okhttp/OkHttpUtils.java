package oz.infra.okhttp;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

	private static OkHttpClient client = new OkHttpClient();
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	// code request code here
	public static String doGetRequest(final String url){
		try {
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
		} catch(Exception ex) {
			ex.printStackTrace();
			return  null;
		}
	
	}

	// post request code here

	// test data
	public static String getJsonString(String player1, String player2) {
		return "{     \"query\": {        \"match_all\": {}    }}";
	}

	public static String doPostJsonRequest(String url, String json)  {
		try {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
		} catch(Exception ex) {
			ex.printStackTrace();
			return  null;
		}
	}
}
