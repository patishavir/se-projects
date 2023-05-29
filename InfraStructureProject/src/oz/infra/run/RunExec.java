package oz.infra.run;

import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;

public class RunExec {
	private InputStreamGobbler errorGobbler;
	private InputStreamGobbler outputGobbler;
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private boolean processCompleted = false;

	public int runCommand(final String[] paramsArray) {
		int returnCode = -1;
		try {
			String execMessage = ArrayUtils.getAsDelimitedString(paramsArray, OzConstants.BLANK);
			Runtime runTime = Runtime.getRuntime();
			Process proc = runTime.exec(paramsArray);
			errorGobbler = new InputStreamGobbler(this, proc.getErrorStream(), "ERROR");
			// any output?
			outputGobbler = new InputStreamGobbler(this, proc.getInputStream(), "Output");
			// kick them off
			errorGobbler.start();
			outputGobbler.start();
			logger.finest("RunExec waitfor command: " + execMessage);
			returnCode = proc.waitFor();
			logger.finest("RunExec returnCode: " + returnCode);
			processCompleted = true;
			logger.finer("RunExec outputString: " + getOutputString());
			logger.finer("RunExec errString: " + getErrString());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return returnCode;
	}

	public final String getErrString() {
		return errorGobbler.getStreamBuffer();
	}

	public final String getOutputString() {
		return outputGobbler.getStreamBuffer();
	}

	final boolean isProcessCompleted() {
		return processCompleted;
	}
}