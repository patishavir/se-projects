package oz.utils.activedirectory;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.jna.JnaUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class GroupsMembership {
	private static Logger logger = JulUtils.getLogger();

	enum OperationsEnum {
		getMyGroupsRecursively, getMyGroups, getUserGroups
	};

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String groups = null;
		OperationsEnum operationsEnum = OperationsEnum.valueOf(args[0]);
		switch (operationsEnum) {
		case getMyGroupsRecursively:
			groups = getRecursiveGroupMembership();
			break;
		case getMyGroups:
			groups = getMyGroups();
			break;
		case getUserGroups:
			groups = getUserGroups(args[1]);
			break;
		}
		System.out.println(groups);
	}

	private static String getRecursiveGroupMembership() {
		List<String> groupsList = JnaUtils.getRecursiveGroupMembership();
		String groups = ListUtils.getAsDelimitedString(groupsList, SystemUtils.LINE_SEPARATOR, Level.FINEST);
		return groups;
	}

	private static String getMyGroups() {
		List<String> groupsList = JnaUtils.getUserGroups("s177571");
		String groups = ListUtils.getAsDelimitedString(groupsList, SystemUtils.LINE_SEPARATOR, Level.FINEST);
		return groups;
	}

	private static String getUserGroups(final String user) {
		List<String> groupsList = JnaUtils.getUserGroups(user);
		String groups = ListUtils.getAsDelimitedString(groupsList, SystemUtils.LINE_SEPARATOR, Level.FINEST);
		return groups;
	}

}
