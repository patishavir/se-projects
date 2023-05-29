package oz.infra.cryptography;

import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Properties;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public class CryptographyUtils {
	private static final String HOST_NAME = SystemUtils.getLocalHostName();
	private static final String USER_NAME = SystemPropertiesUtils.getUserName();
	private static final String DEFAULT_DELIMITER = OzConstants.UNDERSCORE;
	private static final EncryptionMethodEnum DEFAULT_ENCRYPTION_METHOD = EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME;
	private static final Logger logger = JulUtils.getLogger();

	/**
	 * * Decrypt a byte array given the same secret key spec used to encrypt the
	 * message * @param message * @param skeyspec * @return * @throws Exception
	 */
	public static byte[] decrypt(final byte[] message) throws Exception {
		SecretKeySpec skeyspec = getSecretKeySpec();
		Cipher cipher = Cipher.getInstance(skeyspec.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, skeyspec);
		byte[] decrypted = cipher.doFinal(message);
		return decrypted;
	}

	public static String decryptString(final String string2Decrypt) {
		String decryptedString = null;
		if (string2Decrypt != null) {
			byte[] encryptedBytes = StringUtils.convertHexStringToByteArray(string2Decrypt);
			try {
				byte[] decryptedBytes = CryptographyUtils.decrypt(encryptedBytes);
				decryptedString = new String(decryptedBytes);
				logger.finest("string2Decrypt: " + string2Decrypt + " decryptedString " + decryptedString);
			} catch (Exception exception) {
				logger.warning(exception.getMessage());
				exception.printStackTrace();
			}
		}
		return decryptedString;
	}

	public static String decryptString(final String string2Decrypt, final EncryptionMethodEnum encryptionMethod,
			final String... delimiterArg) {
		String decryptedString = null;
		switch (encryptionMethod) {
		case PLAIN:
			decryptedString = decryptString(string2Decrypt);
			break;
		case NONE:
			decryptedString = string2Decrypt;
			break;
		case WITH_HOSTNAME_AND_USERNAME:
		case COMPLEX:
			decryptedString = decryptStringWithHostNameAndUserName(string2Decrypt, delimiterArg);
			break;
		}
		return decryptedString;
	}

	private static String decryptStringWithHostNameAndUserName(final String string2Dycrypt,
			final String... delimiterArg) {
		String resultString = null;
		String delimiter = VarArgsUtils.getMyArg(DEFAULT_DELIMITER, delimiterArg);
		if (string2Dycrypt != null) {
			String decryptedString = decryptString(string2Dycrypt);
			logger.finest(decryptedString);
			String[] stringBreakDown = decryptedString.split(delimiter);
			String decryptedHostname = stringBreakDown[0];
			String decryptedUsername = stringBreakDown[1];
			if (decryptedUsername.length() == 0) {
				decryptedUsername = delimiter + stringBreakDown[2];
			}
			if (decryptedHostname.equalsIgnoreCase(HOST_NAME) && decryptedUsername.equalsIgnoreCase(USER_NAME)) {
				resultString = stringBreakDown[stringBreakDown.length - 1];
			} else {
				logger.warning("\ndecryption has failed. user/hostname mismatch.\t " + HOST_NAME + " <---> "
						+ decryptedHostname + "\t  " + USER_NAME + " <---> " + decryptedUsername);
			}
		}
		logger.finest("resultString: " + resultString);
		return resultString;
	}

	/**
	 * * Encrypt a byte array given the secret key spec
	 * 
	 * @param message
	 *            * @param skeyspec * @return * @throws Exception
	 */
	public static byte[] encrypt(final byte[] message) throws Exception {
		SecretKeySpec skeyspec = getSecretKeySpec();
		Cipher cipher = Cipher.getInstance(skeyspec.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
		byte[] encrypted = cipher.doFinal(message);
		return encrypted;
	}

	public static String encryptString(final String string2Encrypt) {
		String encryptesString = null;
		try {
			byte[] encryptedBytes = encrypt(string2Encrypt.getBytes());
			encryptesString = StringUtils.convertByteArrayToHexStr(encryptedBytes);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		logger.finest("string2Encrypt: " + string2Encrypt + " encryptesString: " + encryptesString);
		return encryptesString;
	}

	public static String encryptString(final String string2Encrypt, final EncryptionMethodEnum encryptionMethod,
			final String... delimiterArg) {
		String encryptedString = null;
		switch (encryptionMethod) {
		case PLAIN:
			encryptedString = encryptString(string2Encrypt);
			break;
		case NONE:
			encryptedString = string2Encrypt;
			break;
		case WITH_HOSTNAME_AND_USERNAME:
		case COMPLEX:
			encryptedString = encryptStringWithHostNameAndUserName(string2Encrypt, delimiterArg);
			break;
		}
		return encryptedString;
	}

	private static String encryptStringWithHostNameAndUserName(final String string2Encrypt,
			final String... delimiterArg) {
		String delimiter = VarArgsUtils.getMyArg(DEFAULT_DELIMITER, delimiterArg);
		return encryptStringWithHostNameAndUserName(string2Encrypt, delimiter);
	}

	public static String encryptStringWithHostNameAndUserName(final String string2Encrypt, final String delimiter) {
		String fullString2Encrypt = HOST_NAME + delimiter + USER_NAME + delimiter + string2Encrypt;
		logger.info("host name: " + HOST_NAME + " user name: " + USER_NAME);
		// logger.finest(fullString2Encrypt);
		return encryptString(fullString2Encrypt);
	}

	public static String getPassword(final Properties properties) {
		String password = properties.getProperty(ParametersUtils.PASSWORD);
		String encryptionMethodString = properties.getProperty(ParametersUtils.ENCRYPTION_METHOD);
		String delimiter = properties.getProperty(ParametersUtils.DELIMITER);
		if (delimiter == null) {
			delimiter = DEFAULT_DELIMITER;
		}
		EncryptionMethodEnum encryptionMethodEnum = DEFAULT_ENCRYPTION_METHOD;
		if (encryptionMethodString != null && encryptionMethodString.length() > 0) {
			encryptionMethodEnum = EncryptionMethodEnum.valueOf(encryptionMethodString);
		}
		password = CryptographyUtils.decryptString(password, encryptionMethodEnum, delimiter);
		return password;
	}

	private static SecretKeySpec getSecretKeySpec() throws Exception {
		return getSecretKeySpec(null, null);
	}

	private static SecretKeySpec getSecretKeySpec(final String passphraseParam, final String algorithmParam)
			throws Exception {
		// 8-byte Salt -
		// SHOULD NOT BE
		// DISCLOSED //
		// alternative
		// approach is
		// to have the
		// salt passed
		// from the
		// calling class (pass-the-salt)?
		String passphrase = "7uGHYVEgtrsbx@pey47#gebq$";
		if (passphraseParam != null) {
			passphrase = passphraseParam;
		}
		String algorithm = "AES";
		if (algorithmParam != null) {
			algorithm = algorithmParam;
		}
		byte[] salt = { (byte) 0xA9, (byte) 0x87, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0xA5, (byte) 0xE3,
				(byte) 0xB2 }; // Iteration
		// count
		final int iterationCount = 1024;
		KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, iterationCount);
		SecretKey secretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(secretKey.getEncoded());
		md.update(salt);
		for (int i = 1; i < iterationCount; i++) {
			md.update(md.digest());
		}
		byte[] keyBytes = md.digest();
		SecretKeySpec skeyspec = new SecretKeySpec(keyBytes, algorithm);
		logger.finest("skeyspec: " + skeyspec.toString());
		return skeyspec;
	}
}
