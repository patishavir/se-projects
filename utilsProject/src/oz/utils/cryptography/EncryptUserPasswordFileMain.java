package oz.utils.cryptography;

import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.io.FileUtils;

public class EncryptUserPasswordFileMain {
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		String[] lines = FileUtils.readTextFile2Array(args[0]);
		for (String line : lines) {
			if (sb.length() > 0) {
				sb.append(OzConstants.LINE_FEED);
			}
			String[] fields = line.split(OzConstants.COMMA);
			String encriptedPass = CryptographyUtils.encryptString(fields[1]);
			String newLine = fields[0].concat(OzConstants.COMMA).concat(
					encriptedPass);
			sb.append(newLine);
		}
		FileUtils.writeFile(args[0] + ".encrypted", sb.toString());
	}
}
