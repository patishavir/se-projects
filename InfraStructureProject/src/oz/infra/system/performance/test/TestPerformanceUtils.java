package oz.infra.system.performance.test;

import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.process.OsProcess;
import oz.infra.system.performance.PerformanceUtils;

public class TestPerformanceUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		//testGetTopCpuConsumerProcessId(0);
		// testGetTopCpuConsumerProcessId(1);
		// testGetTopCpuConsumerProcessId(2);
		 testGetTopCpuConsumerProcessId(3);
		//testGetTopCpuConsumerProcessId(4);
		// TODO Auto-generated method stub
		// PerformanceUtils.getSnaTrafficStatistics();
		// logger.info(String.valueOf(PerformanceUtils.getCpuUtilzation()));
		// MapUtils.printMap(PerformanceUtils.getSnaTrafficStatistics(),
		// Level.INFO);
		// testGetSnaStatusDependentLu();
	}

	private static void testGetTopCpuConsumerProcessId() {
		logger.info(PerformanceUtils.getTopCpuConsumerProcess().toString());
	}

	private static void testGetTopCpuConsumerProcessId(int id) {
		String filePath = "C:\\oj\\projects\\InfraStructureProject\\src\\oz\\infra\\system\\performance\\test\\data\\ps"
				+ String.valueOf(id) + ".txt";
		String[] psArray = FileUtils.readTextFile2Array(filePath);
		OsProcess osProcess = PerformanceUtils.getTopCpuConsumerProcess(psArray);
		if (osProcess != null) {
			logger.info(osProcess.toString());
		} else {
			logger.info("TopCpuConsumerProcess is null !");
		}
	}

	private static void testGetSnaStatusDependentLu() {
		PerformanceUtils.getSnaStatusDependentLu();
	}
}
