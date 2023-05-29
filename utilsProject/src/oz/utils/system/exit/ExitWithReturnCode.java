package oz.utils.system.exit;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;

public class ExitWithReturnCode {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String returnCodeString = String.valueOf(OzConstants.EXIT_STATUS_ABNORMAL);
		if (args.length == 1) {
			returnCodeString = args[0];
		}

		int returnCode = Integer.parseInt(returnCodeString);
		logger.info("Exit with return code: " + returnCodeString);
		System.exit(returnCode);
	}

}
