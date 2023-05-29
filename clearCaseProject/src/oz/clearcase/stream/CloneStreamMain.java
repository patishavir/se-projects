package oz.clearcase.stream;

import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseUtils;
import oz.infra.logging.jul.JulUtils;

public class CloneStreamMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pvob = args[0];
		String stream2Clone = args[1];
		String newStream = args[2];
		String newStreamDisposition = args[3];
		int returnCode = ClearCaseUtils.cloneStream(pvob, stream2Clone, newStream,
				newStreamDisposition);
		logger.info("Return code: " + String.valueOf(returnCode));
	}
}
