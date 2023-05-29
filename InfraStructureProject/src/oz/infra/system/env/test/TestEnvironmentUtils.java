package oz.infra.system.env.test;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.env.EnvironmentUtils;

public class TestEnvironmentUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testSetWindowsEnvironmentVariable();
		testSetJvmEnvironmentVariable();
		testGetEnvironmentVariable();
	}

	private static void testSetWindowsEnvironmentVariable() {
		EnvironmentUtils.setWindowsEnvironmentVariable("zvlMachinevar", "zvlMachinevalue",
				EnvironmentUtils.Scope_Enum.LOCAL_MACHINE_SCOPE);
		EnvironmentUtils.setWindowsEnvironmentVariable("zvlCurrentUservar", "zvlCurrentUserValue",
				EnvironmentUtils.Scope_Enum.CURRENT_USER_SCOPE);
		EnvironmentUtils.setWindowsEnvironmentVariable("zvlCurrentUservar2", "zvlCurrentUserValue2");
	}

	private static void testSetJvmEnvironmentVariable() {
		EnvironmentUtils.printEnvironmentVariablesMap();
		logger.info(StringUtils.concat(SystemUtils.LINE_SEPARATOR, StringUtils.repeatString("=", 128)));
		Map<String, String> newVar = new HashMap<String, String>();
		newVar.put("vvv", "kkk");
		newVar.put("TMP", "c:\\temp");
		EnvironmentUtils.setJvmEnvironmentVariable(newVar);
		EnvironmentUtils.printEnvironmentVariablesMap();
	}

	private static void testGetEnvironmentVariable() {
		String xxx = EnvironmentUtils.getEnvironmentVariable("xxx");
		logger.info("xxx= " + xxx);
		String xxx1 = EnvironmentUtils.getEnvironmentVariable("xxx1");
		logger.info("xxx1= " + xxx1);
	}
}
