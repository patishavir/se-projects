package oz.infra.run.processbuilder.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutor;

public class ProcessBuilderExampleOz {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) throws Exception {
		new ProcessBuilderExampleOz();
	}

	// can run basic ls or ps commands
	// can run command pipelines
	// can run sudo command if you know the password is correct
	public ProcessBuilderExampleOz() throws IOException, InterruptedException {
		// build the system command we want to run
		List<String> commands = new ArrayList<String>();
		// commands.add("c:\\Program Files\\IBM\\RationalSDLC\\ClearCase\\bin\\cleartool.exe");
		// commands.add("lsview");
		// commands.add("-l");
		commands.add("c:\\windows\\system32\\cmd.exe");
		commands.add("/c");
		commands.add("dir");
		commands.add("/s");
		// commands.add("c:\\windows\\system32");
		commands.add("%mydir%");
		Map<String, String> environmentMap = new HashMap<String, String>();
		environmentMap.put("mydir", "c:\\temp");

		commands = new ArrayList<String>();
		// commands.add("c:\\Program Files\\IBM\\RationalSDLC\\ClearCase\\bin\\cleartool.exe");
		// commands.add("lsview");
		// commands.add("-l");
		commands.add("c:\\windows\\system32\\cmd.exe");
		commands.add("/c");
		commands.add("md c:\\temp");
		execute(commands, environmentMap);
		// commands.add("/s");
		// commands.add("c:\\windows\\system32");
		commands = new ArrayList<String>();
		commands.add("%MYDIR%");
		environmentMap = new HashMap<String, String>();
		environmentMap.put("MYDIR", "c:\\temp");
		execute(commands, environmentMap);
	}

	private void execute(List<String> commands, Map<String, String> environmentMap) {
		// execute the command
		SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands, environmentMap);
		try {
			int result = commandExecutor.executeCommand();

			// get the stdout and stderr from the command that was run
			StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
			StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();

			// print the stdout and stderr
			System.out.println("The numeric result of the command was: " + result);
			System.out.println("STDOUT:");
			System.out.println(stdout);
			System.out.println("STDERR:");
			System.out.println(stderr);
		} catch (Exception ex) {
			logger.info(ex.getMessage());
		}
	}
}
