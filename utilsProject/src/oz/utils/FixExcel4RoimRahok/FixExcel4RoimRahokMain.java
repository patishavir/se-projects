package oz.utils.FixExcel4RoimRahok;

import java.util.logging.Logger;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;

public class FixExcel4RoimRahokMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		FixExcel4RoimRahokParameters.processParameters(args[0]);
		try {
			Class clazz = Class.forName(FixExcel4RoimRahokParameters.getFileProcessorWatcherClass());
			clazz.newInstance();
		} catch (Exception ex) {
			logger.warning(ExceptionUtils.printMessageAndStackTrace(ex));
		}
	}
}
