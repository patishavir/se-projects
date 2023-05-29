package oz.infra.base64.test;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import oz.infra.array.ArrayUtils;
import oz.infra.base64.Base64Utils;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class JUnitTestBase64Utils {

	private static final Logger logger = JulUtils.getLogger();

	@BeforeClass
	public static void beforeClass() {
		logger.info(StringUtils.concat(OzConstants.LINE_FEED,
				StringUtils.repeatChar('<', OzConstants.INT_100), "\nstarting ..."));
	}

	@Test
	public void testDecodeEncode() {
		// TODO Auto-generated method stub
		byte[] inbytes = { 1, 2, 3, 4, 5, 6, 7 };
		String encoded = testEncode(inbytes);
		byte[] decodedBytes = testDecode(encoded);
		logger.info(String.valueOf(Arrays.equals(inbytes, decodedBytes)));
		Assert.assertArrayEquals(inbytes, decodedBytes);

	}

	private static String testEncode(final byte[] inbytes) {
		ArrayUtils.printArray(inbytes, "\n", "Input bytes:\n");
		String encoded = Base64Utils.encode(inbytes);
		logger.info(encoded);
		return encoded;
	}

	private static byte[] testDecode(final String encoded) {
		byte[] outbytes = Base64Utils.decode(encoded);
		ArrayUtils.printArray(outbytes, "\n", "Decoded bytes:\n");
		return outbytes;
	}

	@AfterClass
	public static void afterClass() {
		logger.info(StringUtils.concat("done  ...", OzConstants.LINE_FEED,
				StringUtils.repeatChar('>', OzConstants.INT_100), OzConstants.LINE_FEED));
	}
}
