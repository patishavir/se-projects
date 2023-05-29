package oz.infra.system.performance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.process.OsProcess;
import oz.infra.regexp.RegexpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.performance.sna.StatusDependentLu;

public class PerformanceUtils {
	private static final String ACTIVE = "Active";
	private static final String SSCP = "SSCP";
	private static final String LU_PREFIX = "LU";
	private static String sarPath = "sar";

	private static Logger logger = JulUtils.getLogger();

	private static int getAixCpuUtilization() {
		int cpuUtilization = Integer.MAX_VALUE;
		final String[] sarParameters = { sarPath, "-u", "4", "1" };
		List<String> commands = Arrays.asList(sarParameters);
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner
				.run(commands);
		logger.finest(systemCommandExecutorResponse.getExecutorResponse());
		if (systemCommandExecutorResponse.getReturnCode() == 0) {
			String vmstatString = systemCommandExecutorResponse.getStdout().toString();
			String[] vmstatArray = vmstatString.split(SystemUtils.LINE_SEPARATOR);
			String[] vmstatHeaderArray = vmstatArray[vmstatArray.length - 2]
					.split(RegexpUtils.REGEXP_WHITE_SPACE);
			String[] vmstatFiguresArray = vmstatArray[vmstatArray.length - 1]
					.split(RegexpUtils.REGEXP_WHITE_SPACE);
			if ((vmstatHeaderArray.length > 0)
					&& (vmstatHeaderArray.length == vmstatFiguresArray.length)) {
				final String idleTitle = "%idle";
				int idIndex = ArrayUtils.getFullStringIndex(vmstatHeaderArray, idleTitle);
				cpuUtilization = OzConstants.INT_100
						- Integer.parseInt(vmstatFiguresArray[idIndex]);
			} else {
				logger.severe("Zero legnth array or Array lengthes not eaual ! ");
			}
		} else {
			logger.warning(systemCommandExecutorResponse.getStdout().toString());
		}
		return cpuUtilization;
	}

	public static int getCpuUtilzation() {
		int cpuUtilization = Integer.MAX_VALUE;
		if (SystemPropertiesUtils.getOsName().equals(OzConstants.AIX)) {
			cpuUtilization = getAixCpuUtilization();
		} else {
			if (SystemPropertiesUtils.getOsName().startsWith(OzConstants.WINDOWS)) {
				cpuUtilization = getWindowsCpuUtilization();
			}
		}
		return cpuUtilization;
	}

	public static StatusDependentLu getSnaStatusDependentLu() {
		int activeCount = 0;
		int sscpCount = 0;
		String[] snaadminParameters = { "/usr/bin/snaadmin", " status_dependent_lu" };
		if (!SystemPropertiesUtils.getOsName().equals(OzConstants.AIX)) {
			final String testDataFilePath = "C:\\oj\\projects\\InfraStructureProject\\src\\oz\\infra\\system\\performance\\sna\\data\\status_dependent_lu.txt";
			final String[] typeParameters = { "C:\\WINDOWS\\system32\\cmd.exe", "/c", "type",
					testDataFilePath };
			snaadminParameters = typeParameters;
		}
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner
				.run(snaadminParameters);
		if (systemCommandExecutorResponse.getReturnCode() != 0) {
			logger.info(systemCommandExecutorResponse.getExecutorResponse());
		} else {
			String snaLuStatus = systemCommandExecutorResponse.getStdout().toString();
			String[] snaLuStatusArray = snaLuStatus.split(OzConstants.LINE_FEED);
			logger.finest("number of output lines: " + String.valueOf(snaLuStatusArray.length));
			for (int i = 0; i < snaLuStatusArray.length; i++) {
				logger.finest(snaLuStatusArray[i]);
				String[] luStatusLineArray = snaLuStatusArray[i].trim().split(
						RegexpUtils.REGEXP_WHITE_SPACE);
				logger.finest("number of line entries: " + String.valueOf(luStatusLineArray.length));
				ArrayUtils.printArray(luStatusLineArray, Level.FINEST);
				if (luStatusLineArray.length > 2 && luStatusLineArray[1].startsWith(LU_PREFIX)) {
					if (luStatusLineArray[luStatusLineArray.length - 1].equals(ACTIVE)) {
						activeCount++;
					}
					if (luStatusLineArray[luStatusLineArray.length - 1].equals(SSCP)) {
						sscpCount++;
					}
				}
			}
		}
		// return null;

		logger.info("Active lus:" + String.valueOf(activeCount) + " free count: "
				+ String.valueOf(sscpCount));
		return new StatusDependentLu(sscpCount, activeCount);
	}

	public static HashMap<String, String> getSnaTrafficStatistics() {
		// name = ifs
		// stats_type = LS
		// table_type = STATS
		// dlc_type = ETHERNET
		// ls_st_mus_sent = 291
		// ls_st_mus_received = 329
		// ls_st_bytes_sent = 16144
		// ls_st_bytes_received = 71173
		// ls_st_test_cmds_sent = 0
		// ls_st_test_cmds_rec = 0
		// ls_st_data_pkt_resent = 0
		// ls_st_inv_pkt_rec = 0
		// ls_st_adp_rec_err = 0
		// ls_st_adp_send_err = 0
		// ls_st_rec_inact_to = 0
		// ls_st_cmd_polls_sent = 444
		// ls_st_cmd_repolls_sent = 0
		// ls_st_cmd_cont_repolls = 0
		HashMap<String, String> snaStatsHM = null;
		String[] snaadminParameters = { "/usr/bin/snaadmin", "query_statistics", ",name=ifs" };
		if (!SystemPropertiesUtils.getOsName().equals(OzConstants.AIX)) {
			final String testDataFilePath = "C:\\oj\\projects\\InfraStructureProject\\src\\oz\\infra\\system\\performance\\sna\\data\\statistics.txt";
			final String[] typeParameters = { "C:\\WINDOWS\\system32\\cmd.exe", "/c", "type",
					testDataFilePath };
			snaadminParameters = typeParameters;
		}
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner
				.run(snaadminParameters);
		if (systemCommandExecutorResponse.getReturnCode() != 0) {
			logger.info(systemCommandExecutorResponse.getExecutorResponse());
		} else {
			String snaStats = systemCommandExecutorResponse.getStdout().toString();
			String[] snaStatsArray = snaStats.split(OzConstants.LINE_FEED);
			logger.finest("number of output lines: " + String.valueOf(snaStatsArray.length));
			snaStatsHM = new HashMap<String, String>();
			for (String snaStats1 : snaStatsArray) {
				String[] snaStats1Array = snaStats1.split(OzConstants.EQUAL_SIGN);
				if (snaStats1Array.length == 2) {
					logger.finest(snaStats1Array[0].trim() + OzConstants.EQUAL_SIGN
							+ snaStats1Array[1].trim());
					snaStatsHM.put(snaStats1Array[0].trim(), snaStats1Array[1].trim());
				}
			}
		}
		// return null;
		return snaStatsHM;
	}

	public static OsProcess getTopCpuConsumerProcess() {
		OsProcess osProcess = null;
		if (SystemPropertiesUtils.getOsName().equals(OzConstants.AIX)) {
			// /usr/bin/ps -eo "%C %p %c %a"
			// final String[] psParameters = { "/usr/bin/ps", "-eo",
			// "\"%C %p %c %a\"" };
			final String[] psParameters = { "/usr/bin/ps", "-eo", "%C %p %c %a" };
			SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner
					.run(psParameters);
			if (systemCommandExecutorResponse != null
					&& systemCommandExecutorResponse.getReturnCode() == 0) {
				logger.finest(systemCommandExecutorResponse.getExecutorResponse()
						.toString());
				String[] psArray = systemCommandExecutorResponse.getStdout().toString().trim()
						.split(SystemUtils.LINE_SEPARATOR);
				osProcess = getTopCpuConsumerProcess(psArray);
			}
		} else {
			logger.info("Just for debug !!!!!!!!!!!!!!!!!");
			osProcess = new OsProcess("777", "httpd", "/nd61/IBM/HTTPServer/bin/httpd");
		}
		return osProcess;
	}

	public static OsProcess getTopCpuConsumerProcess(final String[] psArray) {
		ArrayUtils.printArray(psArray, SystemUtils.LINE_SEPARATOR, "Ps output: ", Level.FINEST);
		float maxCpuUtilization = -Float.MIN_VALUE;
		String[] maxLine = null;
		for (int i = 0; i < psArray.length; i++) {
			String[] line = psArray[i].trim().split(RegexpUtils.REGEXP_WHITE_SPACE);
			boolean validLine = (line.length > 3)
					&& (line[0].indexOf(OzConstants.DOT) > OzConstants.STRING_NOT_FOUND)
					&& StringUtils.isFloatingPointNumber(line[0]);
			if (validLine) {
				try {
					float cpuUtilization = Float.parseFloat(line[0]);
					if (cpuUtilization > maxCpuUtilization) {
						maxCpuUtilization = cpuUtilization;
						maxLine = line;
						logger.finest(StringUtils.concat("cpu utilization: ", line[0]));
					}
				} catch (Exception ex) {
					logger.info(StringUtils.concat("String:", line[0], " is not numeric!"));
				}
			}
		}
		ArrayUtils.printArray(maxLine, null, "Max array: \t", Level.INFO);
		OsProcess osProcess = null;
		if (maxLine != null) {
			osProcess = new OsProcess(maxLine[1], maxLine[2], maxLine[3]);
			logger.info(osProcess.toString());
		}
		return osProcess;
	}

	private static int getWindowsCpuUtilization() {
		final String[] sarParameters = { "c:\\windows\\system32\\typeperf.exe",
				"\\Processor(_Total)\\% Processor Time", "-sc", "1" };
		List<String> commands = Arrays.asList(sarParameters);
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner
				.run(commands);
		logger.finest(systemCommandExecutorResponse.getExecutorResponse());
		String[] stdOutArray = systemCommandExecutorResponse.getStdout().toString().split(
				OzConstants.LINE_FEED);
		logger.finest(stdOutArray[2]);
		String cpuUtilizationString = stdOutArray[2].split(OzConstants.COMMA)[1];
		logger.finest(cpuUtilizationString);
		cpuUtilizationString = cpuUtilizationString.substring(1, cpuUtilizationString
				.indexOf(OzConstants.DOT));
		logger.finest("cpuUtilization: " + cpuUtilizationString + " %");
		return Integer.parseInt(cpuUtilizationString);
	}
}
