package oz.idgenerator;

import java.text.DecimalFormat;

import oz.infra.checkdigit.CheckDigit;
import oz.infra.string.StringUtils;

public class IdTable {
	private String[] idTableHeader = {"ההההההההההההההההההה", "ווווווווווווווווווווווווווווווווווווווווו" };

	public String[][] getIdTable(final String ids2GenerateString, final String startingIdString) {
		DecimalFormat formatter = new DecimalFormat("00000000");
		int ids2Generate = ValidIdsGeneration.getIDS2GENERATE();
		if (ids2GenerateString.length() > 0 && StringUtils.isJustDigits(ids2GenerateString)) {
			ids2Generate = Integer.parseInt(ids2GenerateString);
		}
		long startingId = Math.abs(Long.parseLong(startingIdString) % 100000000);
		String[][] idTable = new String[ids2Generate][2];
		String checkDigitString;
		for (int i = 0; i < ids2Generate; i++) {
			String myStringid = formatter.format(startingId + i);
			checkDigitString = CheckDigit.generateCheckDigit(myStringid);
			idTable[i][1] = " ";
			idTable[i][1] = formatter.format(startingId + i) + checkDigitString;
		}
		return idTable;
	}

	/**
	 * @return
	 */
	public final String[] getIdTableHeader() {
		return idTableHeader;
	}
}
