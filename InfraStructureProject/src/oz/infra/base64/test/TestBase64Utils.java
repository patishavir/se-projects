package oz.infra.base64.test;

import java.util.Arrays;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.base64.Base64Utils;
import oz.infra.logging.jul.JulUtils;

public class TestBase64Utils {

	private static final Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		byte[] bytes = { 1, 2, 3, 4, 5 };
		ArrayUtils.printArray(bytes, "\n", "Input bytes:\n");
		String encoded = testEncode(bytes);
		byte[] decodedBytes = testDecode(encoded);
		logger.info(String.valueOf(Arrays.equals(bytes, decodedBytes)));
	}

	private static String testEncode(final byte[] inbytes) {
		String encoded = Base64Utils.encode(inbytes);
		logger.info(encoded);
		return encoded;
	}

	private static byte[] testDecode(final String encoded) {
		byte[] outbytes = Base64Utils.decode(encoded);
		ArrayUtils.printArray(outbytes, "\n", "Decoded bytes:\n");
		return outbytes;
	}
}
