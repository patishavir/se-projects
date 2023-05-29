package oz.infra.system.gc;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class GcUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void printGCStats() {
		long totalGarbageCollections = 0;
		long garbageCollectionTime = 0;
		for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
			long count = gc.getCollectionCount();
			if (count >= 0) {
				totalGarbageCollections += count;
			}
			long time = gc.getCollectionTime();

			if (time >= 0) {
				garbageCollectionTime += time;
			}
		}
		StringBuilder sb = new StringBuilder("\nTotal Garbage Collections: ");
		sb.append(totalGarbageCollections);
		sb.append("\nTotal Garbage Collection Time (ms): ");
		sb.append(garbageCollectionTime);
		logger.info(sb.toString());
	}
}
