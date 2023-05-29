package oz.test.run;

public class TestRunner {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// org.junit.runner.JUnitCore
		// .main("oz.test.cryptography.TestEncryptDecrypt");
		org.junit.runner.JUnitCore.main("oz.test.infra.TestStringUtils",
				"oz.test.infra.TestFileUtils", "oz.test.infra.TestFolderUtils",
				"oz.test.jdir.JdirTest", "oz.test.cryptography.TestEncryptDecrypt"
			//	, "oz.infra.list.test.TestlistUtils"
				);
	}
}
