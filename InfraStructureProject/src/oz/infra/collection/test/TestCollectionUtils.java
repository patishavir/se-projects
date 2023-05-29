package oz.infra.collection.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.collection.CollectionUtils;
import oz.infra.logging.jul.JulUtils;

public class TestCollectionUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testPrintAndSortCollection();
//		testPrintAndSortCollection();
//		testConvertToGenericSet();
//		testConvertToGenericSet1();
	}

	private static void testConvertToGenericSet() {
		Set set = new HashSet();
		set.add("s111");
		set.add("s222");
		set.add("s333");
		Set<String> sSet = new HashSet<String>();
		CollectionUtils.convertToGenericSet(set, sSet);
		logger.info(String.valueOf(set.size()) + "   " + String.valueOf(sSet.size()));
		CollectionUtils.printCollection(sSet, Level.INFO);
		set.add(new Integer(7));
		CollectionUtils.convertToGenericSet(set, sSet);
		logger.info(String.valueOf(set.size()) + "   " + String.valueOf(sSet.size()));
		CollectionUtils.printCollection(sSet, Level.INFO);
	}

	private static void testConvertToGenericSet1() {
		Set set = new HashSet();
		set.add(new Integer(111));
		set.add(new Integer(222));
		set.add(new Integer(333));
		Set<Integer> iSet = new HashSet<Integer>();
		CollectionUtils.convertToGenericSet(set, iSet);
		logger.info(String.valueOf(set.size()) + "   " + String.valueOf(iSet.size()));
		CollectionUtils.printCollection(iSet, Level.INFO);
		set.add("Stringgggg");
		CollectionUtils.convertToGenericSet(set, iSet);
		logger.info(String.valueOf(set.size()) + "   " + String.valueOf(iSet.size()));
		CollectionUtils.printCollection(iSet, Level.INFO);
	}

	private static void testPrintAndSortCollection() {
		List<String> listOfStrings = new ArrayList<String>();
		listOfStrings.add("z\n");
		listOfStrings.add("y\n");
		listOfStrings.add("x\n");
		listOfStrings.add("w\n");
		CollectionUtils.printCollection(listOfStrings, Level.INFO);
		CollectionUtils.printCollection(listOfStrings, "title", Level.INFO);
		logger.info(CollectionUtils.getAsDelimitedString(listOfStrings));
		Collections.sort(listOfStrings);
		logger.info(CollectionUtils.getAsDelimitedString(listOfStrings));
	}
}
