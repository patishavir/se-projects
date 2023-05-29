package oz.infra.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpTest1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void whenPostRequestUsingHttpClient_thenCorrect()
			throws ClientProtocolException, IOException {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(“http://www.example.com”);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(“username”, “John”));
			params.add(new BasicNameValuePair(“password”, “pass”));
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpResponse response = client.execute(httpPost);
			assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
			client.close();
			}
}
