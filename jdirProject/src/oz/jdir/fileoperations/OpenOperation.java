package oz.jdir.fileoperations;

import java.util.logging.Logger;

import oz.infra.run.RunExecInaThread;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class OpenOperation extends AbstractFileOperation {

	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		initialize(sd, dd, jdirIndex);
		String osName = System.getProperty("os.name");
		logger.finest("OS name: " + osName);
		if (osName.startsWith("Windows")) {
			String[] params = { "cmd.exe", "/c", sourceFilePath };
			RunExecInaThread runExecInaThread = new RunExecInaThread(params);
			runExecInaThread.start();
		} else {
			logger.warning("Open operation currently supported for Windows only!");
		}

		// RunExec re = new RunExec();
		// re.runCommand(params);
		return true;
	}
}