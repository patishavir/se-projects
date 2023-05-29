package oz.infra.run.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.run.RunExec;

public class RunExecTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// runExecTest("cmd.exe /c C:\\Windows\\System32\\PING.EXE snif-udb");
		runExecTest("c:\\windows\\system32\\cmd.exe /c dir c:\\temp");
	}

	private static void runExecTest(final String command) {
		RunExec runExec = new RunExec();
		String[] paramsArray = { command };
		int retCode = runExec.runCommand(paramsArray);
		logger.info(String.valueOf(retCode));
	}

}
