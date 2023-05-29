package oz.clearcase.utils;

import java.io.File;
import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseUtils;
import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class ListChangeSet {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String viewPath = args[0];
		String activity = args[1];
		listChangeSet(viewPath, activity);

	}

	private static void listChangeSet(final String viewPath, final String activity) {
		logger.info(StringUtils.concat("viewPath: ", viewPath, "  activity: ", activity));
		File viewContextFile = new File(viewPath);
		String[] changeSetArray = ClearCaseUtils.getChangeSet(activity, viewContextFile);
		if (changeSetArray != null && changeSetArray.length > 0) {
			String header = StringUtils.concat("\nChange set for activity:  ", activity, OzConstants.LINE_FEED,
					OzConstants.LINE_FEED);
			StringBuilder sb = ArrayUtils.printArray(changeSetArray, OzConstants.LINE_FEED, header, null, false);
			logger.info(sb.toString());
			StringBuilder rmverSb = new StringBuilder(SystemUtils.LINE_SEPARATOR);
			for (int i = 0; i < changeSetArray.length; i++) {
				rmverSb.append("\ncleartool.exe rmver -xhlink ".concat(changeSetArray[i]));
			}
			logger.info(rmverSb.toString());
		} else {
			logger.info("change set of " + activity + " is empty !");
		}
	}
}
