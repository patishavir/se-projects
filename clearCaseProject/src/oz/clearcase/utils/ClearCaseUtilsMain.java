package oz.clearcase.utils;

import java.io.File;
import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseUtils;
import oz.infra.logging.jul.JulUtils;

public class ClearCaseUtilsMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	/*
	 * main
	 */
	public static void main(final String[] args) {
		String currentView = "c:\\ccviews\\bababa";
		File currentViewFile = new File(currentView);
		String activity = "testactivity1@\\projects";
		System.setProperty("user.dir",
				"D:\\CCViews\\InitialProject_Dev\\sources");
		String[] changeSetArray = ClearCaseUtils.getChangeSet(activity,
				currentViewFile);
		String logFilePath = "c:\\temp\removeChangeSet.log";
		ClearCaseUtils.removeChangeSet(activity, logFilePath, currentView);

	}
}
