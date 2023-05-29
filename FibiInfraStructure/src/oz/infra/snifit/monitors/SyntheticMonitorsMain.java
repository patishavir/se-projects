package oz.infra.snifit.monitors;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.http.win.HttpWinClientUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.system.SystemUtils;
import oz.infra.xml.XMLUtils;

public class SyntheticMonitorsMain {

	private static final String RET_CODE = "RET_CODE";
	private static final String FAIL = "fail";
	private static final String SUCCESS = "success";
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		runSyntheticMonitors(args);
	}

	private static void runSyntheticMonitors(final String[] args) {
		String filePath = args[0];
		int failureCount = 0;
		int successCount = 0;
		logger.info(SystemUtils.getRunInfo());
		String[] urlsArray = FileUtils.readTextFile2Array(filePath);
		if (urlsArray == null) {
			String exitMessage = "failed to read " + filePath + ". \nProcessing has been aborted !";
			SystemUtils.printMessageAndExit(exitMessage, OzConstants.EXIT_STATUS_ABNORMAL, true);
		}
		Map<String, String> resultMap = HttpWinClientUtils.getMultiplePageContents(urlsArray);
		StringBuilder successSb = new StringBuilder();
		StringBuilder failureSb = new StringBuilder();
		Set<String> urlsSet = resultMap.keySet();
		for (String myUrl : urlsSet) {
			String value = FAIL;
			String separatorLine = PrintUtils.getSeparatorLine(myUrl, 1, 1, OzConstants.EQUAL_SIGN);
			String pageContents = resultMap.get(myUrl);
			String prettyPageContents = XMLUtils.formatXmlString(pageContents);
			if (prettyPageContents != null) {
				pageContents = prettyPageContents;
				value = XMLUtils.getElementValue(prettyPageContents, RET_CODE);
			}
			if (value.equals(SUCCESS)) {
				successCount++;
				successSb.append(separatorLine);
				successSb.append(pageContents);
			} else {
				failureCount++;
				failureSb.append(separatorLine);
				failureSb.append(pageContents);
			}
		}
		String separatorLine = PrintUtils.getSeparatorLine("All done !", 1, 0, OzConstants.EQUAL_SIGN);
		successSb.append(separatorLine);
		failureSb.append(separatorLine);
		System.out.println(successSb.toString());
		logger.warning(failureSb.toString());
		String summaryLine = OzConstants.LINE_FEED + String.valueOf(successCount) + " monitors have run successfully.\n"
				+ String.valueOf(failureCount) + " monitors have failed.";
		logger.info(summaryLine);
	}
}
