package com.mkyong.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Java11HttpClientExample5 {

	private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	public static void main(String[] args) throws IOException, InterruptedException {
		Java11HttpClientExample5 obj = new Java11HttpClientExample5();
		obj.sendPOST();
	}

	private void sendPOST() throws IOException, InterruptedException {

		// json formatted data
		String json = new StringBuilder().append("{").append("\"name\":\"mkyong\",").append("\"notes\":\"hello\"")
				.append("}").toString();

		// add json header
		HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json))
				.uri(URI.create("https://httpbin.org/post")).setHeader("User-Agent", "Java 11 HttpClient Bot") // add
																												// request
																												// header
				.header("Content-Type", "application/json").build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		// print status code
		System.out.println(response.statusCode());

		// print response body
		System.out.println(response.body());

	}

}
