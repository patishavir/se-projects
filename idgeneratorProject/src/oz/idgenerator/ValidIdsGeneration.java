package oz.idgenerator;

import java.text.DecimalFormat;
import java.util.Random;

import oz.infra.checkdigit.CheckDigit;

public class ValidIdsGeneration {
	private static final int IDS2GENERATE = 8;

	public static void main(final String[] args) {
		long myLongId;
		int ids2Generate = IDS2GENERATE;
		DecimalFormat formatter = new DecimalFormat("00000000");
		if (args.length > 0) {
			myLongId = Long.parseLong(args[0]);
		} else {
			Random random = new Random();
			myLongId = random.nextLong() % 100000000;
		}
		myLongId = myLongId < 0 ? -myLongId : myLongId;
		if (args.length > 1) {
			ids2Generate = Integer.parseInt(args[1]);
		}
		System.out.println(String.valueOf(ids2Generate) + " valid ids starting with: "
				+ formatter.format(myLongId));
		for (long i = 0; i < ids2Generate; i++) {
			String myStringid = formatter.format(myLongId + i);
			System.out.println(myStringid + CheckDigit.generateCheckDigit(myStringid));
		}
	}

	public static final int getIDS2GENERATE() {
		return IDS2GENERATE;
	}
}