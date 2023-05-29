package oz.infra.itext.text;

import oz.infra.itext.ItextUtils;

public class ItextUtilsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testArrayToPdf();
	}

	private static void testArrayToPdf() {
		String[][] contents = { { "aaa", "bbb" },
				{ "111111111111", "00000000000000000000000000000000" } };
		String[] columnNames = { "0p0p9o9o", "7u7u6y6y" };
		ItextUtils.arrayToPdf(columnNames, contents);
	}
}
