package oz.infra.run.processbuilder.test;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.system.SystemUtils;

public class SystemCommandExecutorRunnerTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testRun();
		testRunAs();
	}

	public static void testRun() {
		String[] parametersArray1 = { "c:\\WINDOWS\\system32\\notepad.exe" };
		SystemCommandExecutorResponse systemCommandExecutorResponse1 = SystemCommandExecutorRunner
				.run(parametersArray1);
		logger.info(systemCommandExecutorResponse1.getExecutorResponse());

		String[] parametersArray2 = { "c:\\windows\\system32\\cmd.exe", "/c", "dir %MYDIR%" };
		Map<String, String> environmentMap = new HashMap<String, String>();
		environmentMap.put("MYDIR", "c:\\temp");
		SystemCommandExecutorResponse systemCommandExecutorResponse2 = SystemCommandExecutorRunner
				.run(parametersArray2, environmentMap);
		logger.info(systemCommandExecutorResponse2.getExecutorResponse());

	}

	public static void testRunAs() {

		// String[] parametersArray = { "c:\\WINDOWS\\system32\\whoami.exe" };
		String[] parametersArray = { "C:\\oj\\projects\\InfraStructureProject\\scripts\\SystemCommandExecutor\\whoami.bat" };
		String[] parametersArray2 = { "c:\\windows\\system32\\cmd.exe", "/c", "set" };
		String parameter = "C:\\oj\\projects\\InfraStructureProject\\scripts\\SystemCommandExecutor\\whoami.bat";
		String psexecPath = "C:\\oj\\projects\\InfraStructureProject\\utils\\PsExec.exe";
		String userName = "OdedPC\\db2admin";
		String password = "db2admin";
		String tagetMachine = "OdedPC";
		if (!SystemUtils.getLocalHostName().startsWith("Oded")) {
			userName = "D797MC01\\s177571";
			password = "nab00t";
			tagetMachine = "s5380440";
		}
		//
		SystemCommandExecutorResponse systemCommandExecutorResponse10 = SystemCommandExecutorRunner
				.runAs(psexecPath, userName, password, tagetMachine, parametersArray,
						getEnvironmentMap());
		logger.info(systemCommandExecutorResponse10.getExecutorResponse());

		SystemCommandExecutorResponse systemCommandExecutorResponse11 = SystemCommandExecutorRunner
				.runAs(psexecPath, userName, password, tagetMachine, parametersArray);
		logger.info(systemCommandExecutorResponse11.getExecutorResponse());

		SystemCommandExecutorResponse systemCommandExecutorResponse20 = SystemCommandExecutorRunner
				.runAs(psexecPath, userName, password, tagetMachine, parametersArray2,
						getEnvironmentMap());
		logger.info(systemCommandExecutorResponse20.getExecutorResponse());

		SystemCommandExecutorResponse systemCommandExecutorResponse30 = SystemCommandExecutorRunner
				.runAs(psexecPath, userName, password, tagetMachine, parameter, getEnvironmentMap());
		logger.info(systemCommandExecutorResponse30.getExecutorResponse());

		SystemCommandExecutorResponse systemCommandExecutorResponse31 = SystemCommandExecutorRunner
				.runAs(psexecPath, userName, password, tagetMachine, parameter);
		logger.info(systemCommandExecutorResponse31.getExecutorResponse());

	}

	private static Map<String, String> getEnvironmentMap() {
		Map<String, String> environmentMap = new HashMap<String, String>();
		environmentMap.put("MYDIR", "c:\\temp");
		return environmentMap;
	}

}
