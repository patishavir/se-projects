package oz.infra.okhttp.test;

import java.io.IOException;

import oz.infra.okhttp.OkHttpUtils;

public class OkHttpUtilsTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub




		// issue the Get request
		String getResponse = 	OkHttpUtils.doGetRequest("http://127.0.0.1:9200/");
		System.out.println(getResponse);
	

		// issue the post request

		String json = OkHttpUtils.getJsonString("Jesse", "Jake");
		String postResponse = OkHttpUtils.doPostJsonRequest("http://127.0.0.1:9200/_search?pretty", json);
		System.out.println(postResponse.length());
		System.out.println(postResponse);
		System.exit(0);
	}
}
