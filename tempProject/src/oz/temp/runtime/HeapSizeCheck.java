package oz.temp.runtime;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.runtime.RunTimeUtils;
import oz.infra.string.StringUtils;

public class HeapSizeCheck {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RunTimeUtils.displayMemoryInfo(Level.INFO, "staring ... \t\t");
		inflateHeap();
		RunTimeUtils.displayMemoryInfo(Level.INFO,
				"after returning from method ...");
		RunTimeUtils.gc(Level.INFO);
		// ThreadUtils.sleep(3000, Level.INFO);
		RunTimeUtils.displayMemoryInfo(Level.INFO,
				"after garbage collection ... ");
	}

	private static void inflateHeap() {
		String x1 = StringUtils.repeatString("qwertyuiop", 1500000);
		String x2 = x1;
		logger.info("x1 length: " + String.valueOf(x1.length()));
		x1=null;
		logger.info("x2 length: " + String.valueOf(x2.length()));		
		RunTimeUtils.displayMemoryInfo(Level.INFO,
				"after string allocation ...");
	}
}
