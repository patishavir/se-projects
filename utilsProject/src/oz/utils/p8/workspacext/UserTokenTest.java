package oz.utils.p8.workspacext;

import java.util.logging.Logger;

import oz.infra.http.HTTPUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;

public class UserTokenTest {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String userToken = getUserToken();
		showObjects(userToken);
	}

	private static void showObjects(final String userToken) {
		final String getObject1Url = "http://fibiux02.fibi.corp:9084/WorkplaceXT/getContent?objectStoreName=BBL_OS1&id={019B9D66-3FBC-5999-8A6F-DE6AF109C7FA}&objectType=document&ut=";
		final String getObject2Url = "http://fibiux02.fibi.corp:9084/WorkplaceXT/getContent?objectStoreName=BBL_OS1&id={024C1708-3FBC-5999-8A6F-DE6AF109C7FA}&objectType=document&ut=";
		startIexplore(getObject1Url + userToken);
		// startIexplore(getObject2Url + userToken);
		// HTTPUtils.getPageContents(getObject1Url + userToken);
		// HTTPUtils.getPageContents(getObject2Url + userToken);
	}

	private static String getUserToken() {
		final String getUserTokenUrl = "http://fibiux02.fibi.corp:9084/WorkplaceXT/setCredentials?op=getUserToken&userId=IFN_ADMIN&password=8XB5kW&verify=true";
		logger.info("getUserTokenUrl: " + getUserTokenUrl);
		String userToken = HTTPUtils.getPageContents(getUserTokenUrl);
		logger.info("userToken: " + userToken);
		return userToken;
	}

	private static void startIexplore(final String url) {
		logger.info("url:" + url);
		final String internetExplorerPath = "C:\\Program Files\\Internet Explorer\\iexplore.exe";
		logger.info(url);
		String[] parameters = { internetExplorerPath, url };
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner
				.run(parameters);
		logger.info(systemCommandExecutorResponse.getExecutorResponse());
	}

}
