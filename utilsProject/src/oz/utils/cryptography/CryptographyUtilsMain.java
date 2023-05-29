package oz.utils.cryptography;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class CryptographyUtilsMain {
	private static final String[] DELIMITERS = { OzConstants.UNDERSCORE, OzConstants.TILDE };
	private static final String MATAFCC_PREFIX = "s5380440" + OzConstants.TILDE + "_svc4albd" + OzConstants.TILDE;
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String string2Encrypt = null;
		if (args.length == 0) {
			string2Encrypt = JOptionPane.showInputDialog(null, "string2Encrypt:",
					"delimiter: " + ArrayUtils.getAsDelimitedString(DELIMITERS, OzConstants.COMMA),
					JOptionPane.QUESTION_MESSAGE);
		} else {
			string2Encrypt = args[0];
		}
		//
		for (String delimiter : DELIMITERS) {
			logger.info(PrintUtils.getSeparatorLine("delimiter: ".concat(delimiter), 2, 0, OzConstants.EQUAL_SIGN));
			String encryptedString = testEncryptString(string2Encrypt);
			String decryptedString = testDecryptString(encryptedString);
			logger.info(SystemUtils.LINE_SEPARATOR + "string2Encrypt: " + string2Encrypt + " encryptedString: "
					+ encryptedString + " decryptedString: " + decryptedString);
			//
			encryptedString = testEncryptStringWithHostNameAndUserName(string2Encrypt, delimiter);
			decryptedString = testDecryptStringWithHostNameAndUserName(encryptedString, delimiter);
			logger.info(StringUtils.concat("WithHostNameAndUserName:", SystemUtils.LINE_SEPARATOR, "string2Encrypt: ",
					string2Encrypt, " encryptedString: ", encryptedString, " decryptedString: ", decryptedString,
					" delimiter: ", delimiter));

			//
			String encryptedString4Matafcc = testEncryptString(MATAFCC_PREFIX + string2Encrypt);
			String decryptedString4Matafcc = testDecryptString(encryptedString4Matafcc);
			logger.info(SystemUtils.LINE_SEPARATOR + "string2Encrypt: " + string2Encrypt + " encryptedString: "
					+ encryptedString4Matafcc + " decryptedString: " + decryptedString4Matafcc);
		}
		logger.info(PrintUtils.getSeparatorLine("all done", 2, 0, OzConstants.EQUAL_SIGN));
	}

	private static String testEncryptString(final String string2Encrypt) {
		String encryptedString = CryptographyUtils.encryptString(string2Encrypt);
		logger.finest("string2Encrypt:" + string2Encrypt + " encryptedString: " + encryptedString);
		return encryptedString;
	}

	private static String testDecryptString(final String string2Decrypt) {
		String decryptedString = CryptographyUtils.decryptString(string2Decrypt);
		logger.finest("string2Decrypt: " + string2Decrypt + "decryptedString: " + decryptedString);
		return decryptedString;
	}

	private static String testEncryptStringWithHostNameAndUserName(final String string2Encrypt,
			String... delemiterArg) {
		String encryptedString = CryptographyUtils.encryptString(string2Encrypt,
				EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME, delemiterArg);
		logger.finest(encryptedString);
		return encryptedString;
	}

	private static String testDecryptStringWithHostNameAndUserName(final String string2Decrypt,
			String... delemiterArg) {
		String fullString2Decrypt = CryptographyUtils.decryptString(string2Decrypt,
				EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME, delemiterArg);
		logger.finest(fullString2Decrypt);
		return fullString2Decrypt;
	}
}
