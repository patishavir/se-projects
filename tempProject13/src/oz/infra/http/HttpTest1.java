package oz.infra.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class HttpTest1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String uri = "http://127.0.0.1:9200";
		post(uri, "");
	}

	public static void post(String uri, String data) {
		try {
			HttpClient client = HttpClient.newBuilder().build();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).POST(BodyPublishers.ofString(data))
					.build();

			HttpResponse<?> response = client.send(request, BodyHandlers.discarding());
			System.out.println(response.statusCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}