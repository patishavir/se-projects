package oz.infra.cryptography.test;

import java.util.logging.Logger;

import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class CryptographyUtilsTest {
	private static Logger logger = JulUtils.getLogger();
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		encryptDecrypt("WITH_HOSTNAME_AND_USERNAME");
		encryptDecrypt("COMPLEX");
	}

	private static void encryptDecrypt(final String method) {
		String string2Encrypt = "qwerty!@#$%";
		EncryptionMethodEnum encryptionMethodEnum = EncryptionMethodEnum.valueOf(method);
		String encryptedString = CryptographyUtils.encryptString(string2Encrypt, encryptionMethodEnum);
		String decryptedString = CryptographyUtils.decryptString(encryptedString, encryptionMethodEnum);
		logger.info(StringUtils.concat("method: ", method, " encryptedString: ", encryptedString, " decryptedString: ",
				decryptedString));
	}

}
