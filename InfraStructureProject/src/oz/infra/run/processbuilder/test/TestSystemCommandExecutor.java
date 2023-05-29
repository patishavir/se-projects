package oz.infra.run.processbuilder.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.system.SystemPropertiesUtils;

public class TestSystemCommandExecutor {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {

		// build the system command we want to run

		String osName = SystemPropertiesUtils.getOsName();
		logger.info("osName: " + osName);
		List<String> commands = null;
		Map<String, String> environmentMap = null;
		if (osName.toLowerCase().startsWith("win")) {
			commands = new ArrayList<String>();
			commands.add("\\\\matafcc\\private$\\s177571\\Scripts\\scheduledTasks\\clearCase\\refreshCC.bat");
			environmentMap = new HashMap<String, String>();
			run(commands, environmentMap);
			//
			commands = new ArrayList<String>();
			commands.add("c:\\windows\\system32\\cmd.exe");
			commands.add("/c");
			commands.add("dir");
			commands.add("/s");
			environmentMap = new HashMap<String, String>();
			run(commands, environmentMap);
			//
			commands = new ArrayList<String>();
			commands.add("c:\\windows\\system32\\cmd.exe");
			commands.add("/c");
			commands.add("md c:\\temp");
			run(commands, environmentMap);
			//
			commands = new ArrayList<String>();
			commands.add("c:\\windows\\system32\\cmd.exe");
			commands.add("/c");
			commands.add("set");
			environmentMap = new HashMap<String, String>();
			environmentMap.put("MYDIR", "c:/temp");
			run(commands, environmentMap);
			//
			commands = new ArrayList<String>();
			commands.add("c:\\windows\\system32\\cmd.exe");
			commands.add("/c");
			commands.add("echo");
			commands.add("%MYDIR%");
			environmentMap = new HashMap<String, String>();
			environmentMap.put("MYDIR", "c:/temp");
			run(commands, environmentMap);
			//
			commands = new ArrayList<String>();
			commands.add("c:\\windows\\system32\\cmd.exe");
			commands.add("/c");
			commands.add("dir");
			commands.add("%MYDIR%");
			environmentMap = new HashMap<String, String>();
			environmentMap.put("MYDIR", "c:\\temp");
			run(commands, environmentMap);
			//
		} else {
			environmentMap = null;
			commands = new ArrayList<String>();
			commands.add("/bin/sh");
			commands.add("-c");
			commands.add("ls -l /var/tmp | grep tmp");
			run(commands, environmentMap);
		}
	}

	private static void run(final List<String> commands, final Map<String, String> environmentMap) {
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(commands,
				environmentMap);
		int result = systemCommandExecutorResponse.getReturnCode();

		// get the stdout and stderr from the command that was run
		String stdout = systemCommandExecutorResponse.getStdout();
		String stderr = systemCommandExecutorResponse.getStderr();

		// print the stdout and stderr
		System.out.println("The numeric result of the command was: " + result);
		System.out.println("STDOUT:");
		System.out.println(stdout);
		System.out.println("STDERR:");
		System.out.println(stderr);
	}
}
