package oz.test.cryptography;

import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.junit.Test;

import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.exception.ExceptionUtils;
import oz.infra.string.StringUtils;
import oz.test.infra.TestStringUtils;

public class TestEncryptDecrypt {
	private static Logger logger = Logger.getLogger(TestStringUtils.class.getName());

	private static String testDecryptString(final String string2Decrypt) {
		String decryptedString = CryptographyUtils.decryptString(string2Decrypt);
		logger.info("string2Decrypt: " + string2Decrypt + "decryptedString: " + decryptedString);
		return decryptedString;
	}

	private static String testDecryptStringWithHostNameAndUserName(final String string2Decrypt) {
		String fullString2Decrypt = CryptographyUtils.decryptString(string2Decrypt,
				EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME);
		logger.info(fullString2Decrypt);
		return fullString2Decrypt;
	}

	private static String testEncryptString(final String string2Encrypt) {
		String encryptedString = CryptographyUtils.encryptString(string2Encrypt);
		logger.info("string2Encrypt:" + string2Encrypt + " encryptedString: " + encryptedString);
		return encryptedString;
	}

	private static String testEncryptStringWithHostNameAndUserName(final String string2Encrypt) {
		String encryptedString = CryptographyUtils.encryptString(string2Encrypt,
				EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME);
		logger.info(encryptedString);
		return encryptedString;
	}

	@Test
	public void testEncryptDecrypt() {
		String message = "1234qwerasdfzxcv";
		try {
			byte[] encryptedBytesArray = CryptographyUtils.encrypt(message.getBytes());
			String encryptedString = StringUtils.convertByteArrayToHexStr(encryptedBytesArray);
			byte[] decryptedBytesArray = CryptographyUtils.decrypt(StringUtils
					.convertHexStringToByteArray(encryptedString));
			String decryptedString = new String(decryptedBytesArray);
			logger.info("message: " + message + "   decrypted: " + decryptedString);
			assertEquals(message, decryptedString);
		} catch (Exception exception) {
			ExceptionUtils.printMessageAndStackTrace(exception);
		}
	}

	@Test
	public void testEncryptDecryptString() {
		String string2Encrypt = "11223344556677881122334455667788qazwsx";
		//
		String encryptedString = testEncryptString(string2Encrypt);
		String decryptedString = testDecryptString(encryptedString);
		logger.info("string2Encrypt: " + string2Encrypt + " encryptedString: " + encryptedString + " decryptedString: "
				+ decryptedString);
		assertEquals(string2Encrypt, decryptedString);
	}

	@Test
	public void testEncryptDecryptStringWithHostNameAndUserName() {
		String string2Encrypt = "11223344556677881122334455667788qazwsx?";
		//
		String encryptedString = testEncryptStringWithHostNameAndUserName(string2Encrypt);
		String decryptedString = testDecryptStringWithHostNameAndUserName(encryptedString);
		logger.info("string2Encrypt: " + string2Encrypt + " encryptedString: " + encryptedString + " decryptedString: "
				+ decryptedString);
		assertEquals(string2Encrypt, decryptedString);
	}

}
