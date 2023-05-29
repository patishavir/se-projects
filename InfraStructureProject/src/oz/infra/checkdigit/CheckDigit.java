package oz.infra.checkdigit;

import java.util.logging.Logger;

public class CheckDigit {
	private static Logger logger = Logger.getLogger(CheckDigit.class.getName());
	private final static int ID_LENGTH = 8;

	public static String generateCheckDigit(final String id) {
		String checkDigitString = "";
		int checkDigitInt = 0;
		int[] weigthArray = new int[ID_LENGTH];
		int[] idIntArray = new int[ID_LENGTH];
		int[] multiplyArray = new int[ID_LENGTH];
		int[] digitSumArray = new int[ID_LENGTH];
		for (int i = 0; i < ID_LENGTH; i++) {
			weigthArray[i] = (i % 2) + 1;
		}
		long longId = Long.parseLong(id);
		for (int i = idIntArray.length; i > 0; i--) {
			idIntArray[i - 1] = (int) (longId % 10);
			longId = longId / 10;
		}
		for (int i = 0; i < ID_LENGTH; i++) {
			multiplyArray[i] = idIntArray[i] * weigthArray[i];
		}
		for (int i = 0; i < ID_LENGTH; i++) {
			digitSumArray[i] = (multiplyArray[i] / 10) + (multiplyArray[i] % 10);
		}
		for (int i = 0; i < ID_LENGTH; i++) {
			digitSumArray[i] = (multiplyArray[i] / 10) + (multiplyArray[i] % 10);
		}
		for (int i = 0; i < ID_LENGTH; i++) {
			checkDigitInt = checkDigitInt + digitSumArray[i];
		}
		checkDigitInt = (10 - (checkDigitInt % 10)) % 10;
		checkDigitString = String.valueOf(checkDigitInt);
		logger.finest(id + " Check digit: " + checkDigitInt);
		return checkDigitString;
	}
}