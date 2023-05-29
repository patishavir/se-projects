package oz.infra.jna.test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.jna.JnaUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.system.SystemUtils;

public class TestJnaUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testGetRecursiveGroupMembership();
		logger.info(PrintUtils.getSeparatorLine("User Groups"));
		testGetUserGroups();
	}

	private static void testGetRecursiveGroupMembership() {
		List<String> groupsList = JnaUtils.getRecursiveGroupMembership();
		ListUtils.getAsDelimitedString(groupsList, SystemUtils.LINE_SEPARATOR, Level.INFO);
	}

	private static void testGetUserGroups() {
		List<String> groupsList = JnaUtils.getUserGroups("s177571");
		ListUtils.getAsDelimitedString(groupsList, SystemUtils.LINE_SEPARATOR, Level.INFO);
	}
}
