package oz.clearcase.utils;

import oz.clearcase.infra.ClearCaseUtils;
import oz.infra.system.SystemUtils;

public class ClearCaseRemoveChangeSetMain {
	public static void main(final String[] args) {
		if (args.length < 2) {
			SystemUtils.printMessageAndExit("Parameters have not been supplied. Processed has been aborted", -1);
		}
		String activity = args[0];
		String logFilePath = args[1];
		String currentView = args[2];
		if (ClearCaseUtils.removeChangeSet(activity, logFilePath, currentView)) {
			SystemUtils.printMessageAndExit("removeChangeSet completed for " + activity, 0);
		} else {
			SystemUtils.printMessageAndExit("removeChangeSet failed for " + activity, -1);
		}
	}
}
