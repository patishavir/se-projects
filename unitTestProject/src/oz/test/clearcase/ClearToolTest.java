package oz.test.clearcase;

import java.util.logging.Logger;

import oz.clearcase.infra.ClearToolCommand;
import oz.clearcase.infra.ClearToolResults;
import oz.infra.array.ArrayUtils;
import oz.infra.logging.jul.JulUtils;

public class ClearToolTest {
	private static  Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClearToolCommand clearToolCommand = new ClearToolCommand();
		String[] clearToolparams = new String[] { "", "ls", "-r",
				"C:\\CCViews\\HRCLR_A_11\\HRVob10\\HRCollateralC\\CollateralEJB" };
		// String[] clearToolparams = new String[] { "", "lsview", "-l", "-prop"
		// };
		logger.info("Starting test ...");
		ArrayUtils.printArray(clearToolparams);
		ClearToolResults clearToolResults = clearToolCommand.runClearToolCommand(clearToolparams,
				false);
		String[] stdoutArray = null;
		if (clearToolResults.getReturnCode() != 0 || clearToolResults.getStdErr().length() > 0) {
			logger.warning(clearToolResults.getStdErr());
		} else {
			if (clearToolResults.getStdOut().length() == 0) {
				stdoutArray = null;
			} else {
				stdoutArray = clearToolResults.getStdOut().split("\n");
				logger.info(String.valueOf("Array length: " + stdoutArray.length));
			}
		}

	}

}
