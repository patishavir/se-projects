package oz.infra.console.test;

import java.util.logging.Logger;

import oz.infra.console.ConsoleUtils;
import oz.infra.logging.jul.JulUtils;

public class ConsoleUtilsTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testReadLineFromConsole();
	}

	private static void testReadLineFromConsole() {
		logger.info(ConsoleUtils.readLineFromConsole());
	}

}
