package oz.infra.list.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.junit.Test;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.list.ListUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class JUnitTestListUtils {

	@Test
	public void testListUtilsPrint() {
		ArrayList<String> ar = new ArrayList<String>();
		ListUtils.getAsDelimitedString(ar, Level.INFO);
		String s1 = "1AAAAAAAAAAAAAAAAAAAA";
		String s2 = "2AAAAAAAAAAAAAAAAAAAA";
		String s3 = "3AAAAAAAAAAAAAAAAAAAA";
		ar.add(s1);
		ar.add(s2);
		ar.add(s3);
		ListUtils.getAsDelimitedString(ar, Level.FINE);
		ListUtils.getAsDelimitedString(ar, Level.INFO);
		ListUtils.getAsDelimitedString(ar, Level.WARNING);
		String expectedRString = SystemUtils.LINE_SEPARATOR + s1 + SystemUtils.LINE_SEPARATOR + s2
				+ SystemUtils.LINE_SEPARATOR + s3;
		String result = ListUtils.getAsDelimitedString(ar, Level.FINE);
		assertNull(result);
		result = ListUtils.getAsDelimitedString(ar, Level.INFO);
		assertTrue(expectedRString.equals(result));
		List<String> commandInformation = new ArrayList<String>();
		commandInformation.add("str1");
		commandInformation.add("str2");
		ListUtils.getAsDelimitedString(commandInformation, Level.INFO);
	}

	@Test
	public void testListToStringArray() {
		// List<Integer> il = new ArrayList<Integer>();
		// il.add(new Integer(17));
		// il.add(new Integer(831));
		// il.add(new Integer(955831));
		List<String> sl = new ArrayList<String>();
		sl.add("0000000jjjjjjjjjj");
		sl.add("1111111jjjjjjjjjj");
		sl.add("2222222jjjjjjjjjj");
		String[] stringArray = ListUtils.stringListToStringArray(sl);
		ArrayUtils.printArray(stringArray);
		assertTrue(stringArray.length == 3);

	}

	@Test
	public void testprintListOfLists() {
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> list1 = new ArrayList<String>();
		list1.add("a1");
		list1.add("a2");
		list.add(list1);
		List<String> list2 = new ArrayList<String>();
		list2.add("b1");
		list2.add("b2");
		list.add(list2);
		String result = ListUtils.printListOfLists(list, "title", Level.INFO, OzConstants.COMMA);
		String expected = StringUtils.concat("title", SystemUtils.LINE_SEPARATOR, "a1,a2",
				SystemUtils.LINE_SEPARATOR, "b1,b2", SystemUtils.LINE_SEPARATOR);
		assertEquals(result, expected);

	}
}
