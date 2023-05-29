package oz.infra.list.test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;

public class ListUtilsTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testGetAsDelimitedString();
	}

	private static void testGetAsDelimitedString() {
		String[] array = { "aa", "bb" };
		List<String> list = Arrays.asList(array);
		logger.info(ListUtils.getAsDelimitedString(list, OzConstants.NUMBER_SIGN, Level.FINEST));
	}
}
