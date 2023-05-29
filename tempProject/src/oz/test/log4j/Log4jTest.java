package oz.test.log4j;

import java.io.File;

import oz.infra.io.PathUtils;
import oz.infra.logging.log4j.Log4jUtils;

public class Log4jTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		runTest("./args/log4j/log4j.properties");
	}

	private static void runTest(final String propertiesFilePath) {
		System.out.println("get full path:" + PathUtils.getAbsolutePath("."));
		File propertiesFile = new File(propertiesFilePath);
		System.out.print(propertiesFile.isFile());
		Log4jUtils.configure(propertiesFilePath);
	}
}
