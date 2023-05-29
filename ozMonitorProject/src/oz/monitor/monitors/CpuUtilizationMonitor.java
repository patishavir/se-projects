package oz.monitor.monitors;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.map.MapUtils;
import oz.infra.process.OsProcess;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.performance.PerformanceUtils;
import oz.monitor.OzMonitorParameters;
import oz.monitor.common.OzMonitorResponse;

public class CpuUtilizationMonitor extends AbstractOzMonitor {
	private static int debugCpuUtilzation = 90;
	private String cpuUtilizationThreshHold = null;
	private String proccesToActionScriptMap = null;
	private Map<String, String> proccesToActionScriptHashMap = new HashMap<String, String>();
	private int cpuUtilizationThreshHoldInt = Integer.MIN_VALUE;
	private boolean debug = false;

	// private Logger cpuUtilizationLogger = null;
	public CpuUtilizationMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
		cpuUtilizationThreshHoldInt = Integer.parseInt(cpuUtilizationThreshHold);
		logger.finest("cpuUtilizationThreshHold: " + cpuUtilizationThreshHold);
	}

	private int getCpuUtilzation() {
		int cpuUtilization = PerformanceUtils.getCpuUtilzation();
		if (debug) {
			final int debugDelta = -10;
			cpuUtilization = debugCpuUtilzation;
			debugCpuUtilzation += debugDelta;
		}
		return cpuUtilization;
	}

	private OsProcess getTopCpuConsumerProcess() {
		OsProcess osProcess = PerformanceUtils.getTopCpuConsumerProcess();
		if (debug) {
			logger.warning("Just for debug !!!!!!!!!!!!!!!!!");
			osProcess = new OsProcess("777", "httpd", "/nd61/IBM/HTTPServer/bin/httpd");
		}
		return osProcess;
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		logger.finest("Monitoring: " + getResource());
		boolean isResourceAvailable = false;
		int cpuUtilization = getCpuUtilzation();
		String cpuUtiliazationString = String.valueOf(cpuUtilization);
		String ozMonitorMessage = "cpu utilzation " + cpuUtiliazationString + " % is above the "
				+ cpuUtilizationThreshHold + "% threshhold !";
		String ozMonitorLongMessage = ozMonitorMessage;
		String[] actionOnFailureParams = null;
		if (cpuUtilization < cpuUtilizationThreshHoldInt) {
			isResourceAvailable = true;
			ozMonitorMessage = ozMonitorMessage.replace("above", "below");
		} else {
			OsProcess osProcess = getTopCpuConsumerProcess();
			if (osProcess != null) {
				ozMonitorLongMessage = StringUtils.concat(ozMonitorMessage, SystemUtils.LINE_SEPARATOR,
						osProcess.toString());
				logger.info(ozMonitorLongMessage);
				String commandFullPath = osProcess.getCommandFullPath();
				String actionScriptPath = proccesToActionScriptHashMap.get(commandFullPath);
				if (actionScriptPath != null) {
					String pid = osProcess.getPid();
					String[] actionOnFailureParams1 = { actionScriptPath, pid };
					actionOnFailureParams = actionOnFailureParams1;
				}
			}
		}
		if (debug) {
			ArrayUtils.printArray(actionOnFailureParams, OzConstants.TAB, "actionOnFailureParams");
		}
		return generateOzMonitorResponse(isResourceAvailable, ozMonitorMessage, ozMonitorLongMessage,
				actionOnFailureParams);
	}

	public void setCpuUtilizationThreshHold(String cpuUtilizationThreshHold) {
		this.cpuUtilizationThreshHold = cpuUtilizationThreshHold;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setProccesToActionScriptMap(final String proccesToActionScriptMap) {
		this.proccesToActionScriptMap = proccesToActionScriptMap;
		logger.finest(proccesToActionScriptMap);
		String[] mapArray = proccesToActionScriptMap.split(OzConstants.COMMA);
		for (String map1 : mapArray) {
			String[] map1Array = map1.split(OzConstants.EQUAL_SIGN);
			String scripFullPath = PathUtils.getFullPath(OzMonitorParameters.getRootFolderPath(), map1Array[1]);
			proccesToActionScriptHashMap.put(map1Array[0], scripFullPath);
			FileUtils.terminateIfFileDoesNotExist(scripFullPath);
		}
		MapUtils.printMap(proccesToActionScriptHashMap, "proccesToActionScriptHashMap: ", Level.INFO);
	}
}
