package oz.temp.maller;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.collection.CollectionUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;

public class CheckId {
	private static HashSet<String> userIdsSet = null;
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		buildUserIdsSet("args/maller/EliM/AD-ADV-PS.CSV");
		processFile("args/maller/EliM/users.csv");
		processFile("args/maller/EliM/users1.csv");
	}

	private static void buildUserIdsSet(final String filePath) {
		String[] userIdsArray = FileUtils.readTextFile2Array(filePath);
		userIdsSet = new HashSet<String>(userIdsArray.length);
		for (int i = 1; i < userIdsArray.length; i++) {
			String[] lineArray = userIdsArray[i].split(OzConstants.COMMA);
			userIdsSet.add(lineArray[0]);
		}
		CollectionUtils.printCollection(userIdsSet, "Title:", Level.INFO, "\n");
		logger.info(String.valueOf(userIdsSet.size()) + " values in set");
	}

	private static void processFile(final String filePath) {
		logger.info("processing " + filePath + " ...");
		String[] usersArray = FileUtils.readTextFile2Array(filePath);
		int found = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < usersArray.length; i++) {
			String[] lineArray = usersArray[i].split(OzConstants.COMMA);
			logger.fine("line: " + String.valueOf(i) + "  " + lineArray[0]);
			if (userIdsSet.contains(lineArray[0])) {
				logger.warning(usersArray[i]);
				sb.append(usersArray[i]);
				sb.append("\n");
				found++;
			}
		}
		logger.info(String.valueOf(found) + " records found.");
		FileUtils.writeFile(filePath + ".txt", sb.toString());
	}
}
