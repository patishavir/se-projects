package oz.utils.io;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.io.FolderUtils;
import oz.infra.system.SystemUtils;

public class CompareFoldersByContentsMain {
	private static Logger logger = Logger.getLogger(CompareFoldersByContentsMain.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			SystemUtils.printMessageAndExit("2 parameters must be provided", -1);
		}
		if (args[0].equalsIgnoreCase(args[1])) {
			SystemUtils.printMessageAndExit("Parameters must be different!", -1);
		}

		File dir1 = new File(args[0]);
		File dir2 = new File(args[1]);
		if (!dir1.exists()) {
			SystemUtils.printMessageAndExit("Folder " + args[0] + " does not exist !", -1);
		}
		if (!dir2.exists()) {
			SystemUtils.printMessageAndExit("Folder " + args[1] + " does not exist !", -1);
		}
		boolean returnCode = FolderUtils.compareFoldersByContents(dir1, dir2);
		if (returnCode) {
			logger.info("The contents of " + args[0] + " and " + args[1] + " match!");
			System.exit(0);
		} else {
			logger.info("The contents of " + args[0] + " and " + args[1] + " do not match!");
			System.exit(-1);
		}
	}
}
