package oz.clearcase.view.mkview;

import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class MkviewMain {
	/**
	 * @param args
	 */
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JulUtils.addFileHandler(args[0]);
		logger.info(SystemUtils.getRunInfo());
		String[] args1 = ArrayUtils.shift(args, 1);
		MkviewHandler.doMkview(args1);
		JulUtils.closeHandlers();
		System.exit(OzConstants.EXIT_STATUS_OK);
	}
}
