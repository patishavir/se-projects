package oz.infra.io.tailer;

import java.io.File;
import java.util.logging.Logger;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import oz.infra.logging.jul.JulUtils;
import oz.infra.thread.ThreadUtils;

public class TestTailer {
	private File file = null;
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO code application logic here
		// String filePath = args[0];
		logger.info("starting ...");
		String filePath = "c:\\temp\\ttest.log";
		File file = new File(filePath);
		long delayMillis = 1000L;
		TailerListener tailerListener = new TailerListenerImpl();
		Tailer tailer = Tailer.create(file, tailerListener, delayMillis);
		ThreadUtils.sleep(Long.MAX_VALUE);
	}
}
