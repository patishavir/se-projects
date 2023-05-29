package oz.infra.elastic.api.document;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import oz.infra.elastic.data.EmployeeGenerator;
import oz.infra.elastic.data.classes.Employee;
import oz.infra.json.jackson.JacksonUtils;

public class ElasticSearchIndexUtils {
	private static String INDEX = "employees";
	private static RestHighLevelClient client = getRestHighLevelClient();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int id = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
//		indexDocument(id);
//		getRequest(id);
		createIndexRequest();
//		deleteIndexRequest();
//		deleteIndexRequest();

	}

	private static void createIndexRequest() {
		CreateIndexRequest request = new CreateIndexRequest(INDEX);
		request.settings(Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 1));
		request.setTimeout(TimeValue.timeValueMinutes(2));
		try {
			CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
			System.out.println(createIndexResponse.toString());
			System.out.println("isAcknowledged: " + String.valueOf(createIndexResponse.isAcknowledged()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void deleteIndexRequest() {
		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(INDEX);
		try {
			AcknowledgedResponse deleteIndexResponse = client.indices().delete(deleteIndexRequest,
					RequestOptions.DEFAULT);
			System.out.println(deleteIndexRequest.toString());
			System.out.println(deleteIndexResponse.toString());
			System.out.println("isAcknowledged: " + String.valueOf(deleteIndexResponse.isAcknowledged()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static String getJsonString(final int id) {
		Employee employee = EmployeeGenerator.getEmployee(id);
		String jsonString = JacksonUtils.javaObject2Json(employee);
		return jsonString;
	}

	private static void getRequest(final int id) {
		String idString = String.valueOf(id);
		GetRequest getRequest = new GetRequest(INDEX, idString);
		System.out.println(getRequest.toString());
	}

	private static RestHighLevelClient getRestHighLevelClient() {
		RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
				RestClient.builder(new HttpHost("localhost", 9200, "http"), new HttpHost("localhost", 9201, "http")));
		return restHighLevelClient;
	}

	private static void indexDocument(final int id) {
		String jsonString = getJsonString(id);
		String idString = String.valueOf(id);
		indexJsonString(jsonString, idString, INDEX);
	}

	private static void indexJsonString(final String jsonString, final String id, final String index) {
		IndexRequest indexRequest = new IndexRequest(index);
		indexRequest.id(id);
		IndexRequest indexRequestOutcome = indexRequest.source(jsonString, XContentType.JSON);
		System.out.println(indexRequestOutcome.toString());
	}
}