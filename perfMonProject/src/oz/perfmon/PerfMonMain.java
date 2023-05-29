package oz.perfmon;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.RunExec;

public class PerfMonMain {
	private static Logger logger = JulUtils.getLogger();
	private static String[] commandsArray;

	public static void main(final String[] args) {
		PerfMonMain perfMonMain = new PerfMonMain();
		int numberOfCommands = perfMonMain.processInputParameters(args);
		perfMonMain.runCommands(numberOfCommands);
	}

	private void runCommands(final int numberOfCommands) {
		RunExec runExec = new RunExec();
		long elapsedTime = 0;
		long totalTime = 0;
		int retunCode;
		for (int i = 0; i < numberOfCommands; i++) {
			long startTime = System.currentTimeMillis();
			String[] command1Array = commandsArray[i].split(" ");
			retunCode = runExec.runCommand(command1Array);
			if (retunCode != 0) {
				System.err.println(runExec.getErrString());
			}
			elapsedTime = System.currentTimeMillis() - startTime;
			System.out.println(DateTimeUtils.getCurrentTime() + "," + System.getenv("USERNAME")
					+ "," + System.getenv("COMPUTERNAME") + "," + String.valueOf(i) + ","
					+ String.valueOf(elapsedTime) + ",return Code:" + String.valueOf(retunCode)
					+ "," + commandsArray[i]);
			totalTime = totalTime + elapsedTime;
		}
		System.out.println(DateTimeUtils.getCurrentTime() + "," + System.getenv("USERNAME") + ","
				+ System.getenv("COMPUTERNAME") + "," + "999," + String.valueOf(totalTime)
				+ ",************" + ",Total time in miliseconds.");
	}

	private int processInputParameters(final String[] args) {
		new PerfMonParameters().processParameters(args);
		int numberOfCommands = Integer.parseInt(PerfMonParameters.getNumberOfCommands());
		commandsArray = new String[numberOfCommands];
		PerfMonParameters perfMonParameters = new PerfMonParameters();
		int i;
		try {
			for (i = 0; i < numberOfCommands; i++) {
				Method myMethod = PerfMonParameters.class.getDeclaredMethod(
						"getCommand" + String.valueOf(i + 1), null);
				commandsArray[i] = (String) myMethod.invoke(perfMonParameters, null);
				logger.finest("commandsArray[" + String.valueOf(i) + "])  " + commandsArray[i]);
				if (commandsArray[i] == null) {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			i = -1;
		}
		return i;
	}
}
