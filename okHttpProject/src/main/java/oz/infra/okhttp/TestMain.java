package oz.infra.okhttp;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestMain {
	OkHttpClient client = new OkHttpClient();

	// code request code here
	String doGetRequest(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	// post request code here

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	// test data
	String bowlingJson(String player1, String player2) {
            return "{     \"query\": {        \"match_all\": {}    }}";
          }

	String doPostRequest(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public static void main(String[] args) throws IOException {

		// issue the Get request
		TestMain example = new TestMain();
		String getResponse = example.doGetRequest("http://127.0.0.1:9200/");
		System.out.println(getResponse);

		// issue the post request

		String json = example.bowlingJson("Jesse", "Jake");
		String postResponse = example.doPostRequest("http://127.0.0.1:9200/_search?pretty", json);
		System.out.println(postResponse);
	}
}