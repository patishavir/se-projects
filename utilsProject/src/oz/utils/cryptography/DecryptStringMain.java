package oz.utils.cryptography;

import java.util.logging.Logger;

import oz.infra.cryptography.CryptographyUtils;
import oz.infra.logging.jul.JulUtils;

public class DecryptStringMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {

		// decryptString("4cca46a7609c4d7688d9ade68341b57c");
		// decryptString("4cca46a7609c4d7688d9ade68341b57c");
		// decryptString("153d0f757d243877410cf416a158e51243565728bd83e09764de75674245f967");
		// decryptString("54f4914f3864bce48b26a022eb610bd5412e87674417e9241227086a387d16af");
		// decryptString("54f4914f3864bce48b26a022eb610bd5412e87674417e9241227086a387d16af");
		// String string = "b86258ff45a5420b8bfee6930b2b8cb6";
		// String string = "f17e40e8f496e57693cc6e7e238ceab1";
		// String string = "d285273aabf019ce4626a76a49b58b63";
		// String string = "f17e40e8f496e57693cc6e7e238ceab1";
		String string = "8bc81861f473b860115196040891c1d5e3f9153e5f4401d88fd039c29a779a08";
		string = "0354bf0a88422e03a43c43b707ad5e97";
		string = "0354bf0a88422e03a43c43b707ad5e97";
		string = "11db45b1866fdba9ea3c8d98223eb3f4";
		string = "ab17bb8089a826bd4d29030206510bcc";
		string = "11db45b1866fdba9ea3c8d98223eb3f4";
		decryptString(string);
	}

	private static String decryptString(final String string2Decrypt) {
		String decryptedString = CryptographyUtils.decryptString(string2Decrypt);
		logger.info("string2Decrypt: " + string2Decrypt + "decryptedString: " + decryptedString);
		return decryptedString;
	}
}
