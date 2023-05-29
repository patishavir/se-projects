package oz.infra.io.filefilter.test;

import java.util.logging.Logger;

import oz.infra.io.filefilter.FileNameRegExpresionAndNameRange;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;

public class FileNameSuffixAndNameRangeTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		testFileNameSuffixAndNameRange(RegexpUtils.REGEXP_SQL_FILE, "dbchanges_6.05.02-dbchanges_6.05.03",
				"dbchanges_6.05.03.sql");
		testFileNameSuffixAndNameRange(RegexpUtils.REGEXP_SQL_FILE, "dbchanges_6.05.02-dbchanges_6.05.03",
				"dbchanges_6.05.033.sql");
		testFileNameSuffixAndNameRange(RegexpUtils.REGEXP_SQL_FILE, "dbchanges_6.05.02-dbchanges_6.05.03.",
				"dbchanges_6.05.03.sql");
		testFileNameSuffixAndNameRange(RegexpUtils.REGEXP_SQL_FILE, "dbchanges_6.05.02-dbchanges_6.05.03",
				"dbchanges_6.05.04.sql");
	}

	private static void testFileNameSuffixAndNameRange(final String suffixRegExpression, final String fileNameRange,
			final String fileName2Test) {
		FileNameRegExpresionAndNameRange fileNameFiltersSuffixAndNameRange = new FileNameRegExpresionAndNameRange(
				suffixRegExpression, fileNameRange);
		logger.info(String.valueOf(fileNameFiltersSuffixAndNameRange.accept(null, fileName2Test)));
	}
}
