package oz.infra.run;

import java.util.logging.Logger;

public class RunExecInaThread extends Thread {
	private String[] paramsArray;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	/**
	 * @param paramsArrayP
	 */
	public RunExecInaThread(final String[] paramsArrayP) {
		this.paramsArray = paramsArrayP;
	}

	public void run() {
		logger.fine("Entering the Thread Run method");
		for (int i = 0; i < paramsArray.length; i++) {
			logger.fine(paramsArray[i]);
		}
		// ArrayUtils.printStringArray(paramsArray);
		RunExec re = new RunExec();
		re.runCommand(paramsArray);
	}
}
