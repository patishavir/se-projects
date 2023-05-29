package oz.infra.bytes;

public class ByteUtils {
	public static String byteArrayToHexString(final byte[] byteArray) {
		StringBuffer sb = new StringBuffer(byteArray.length * 2);
		for (int i = 0; i < byteArray.length; i++) {
			int v = byteArray[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	public static int getNumberOfOccurences(final byte[] bytesArray, final byte byteParameter) {
		int numberOfOccurences = 0;
		for (int i = 0; i < bytesArray.length; i++) {
			if (bytesArray[i] == byteParameter) {
				numberOfOccurences++;
			}
		}
		return numberOfOccurences;
	}
}
