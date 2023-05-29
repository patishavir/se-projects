package oz.temp.jul;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class JulTest {

	private static String[] params = { "1", "22", "333", "4444", "55555", "baboona" };
	private static String template = "first: {0} second {1} {2}  {3} last: {4}  baboona: {5}";
	private static Logger ozLogger = JulUtils.getLogger();
	private static Logger julLogger = Logger.getLogger(JulTest.class.getCanonicalName());

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// ozTest();
		julTest();
	}

	private static void ozTest() {
		ozLogger.log(Level.INFO, template, params);
		ozLogger.log(Level.INFO, template, params);
		ozLogger.log(Level.INFO, template, params);
		ozLogger.log(Level.INFO, template, params);
		ozLogger.log(Level.INFO, template, params);
	}

	private static void julTest() {
		DataFormatter dataFormatter = new DataFormatter (); 
		Handler[] handlers = julLogger.getHandlers();
		for ( Handler handler : handlers) {
			handler.setFormatter(dataFormatter);
		}
		// java.util.logging.SimpleFormatter.format = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n";
		julLogger.log(Level.INFO, template, params);
	}

}
