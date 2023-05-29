package oz.infra.jna;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Netapi32Util;
import com.sun.jna.platform.win32.Netapi32Util.Group;

import oz.infra.logging.jul.JulUtils;

public class JnaUtils {
	private static Logger logger = JulUtils.getLogger();

	public static List<String> getCurrentUserGroups() {
		return getUserGroups(Advapi32Util.getUserName());
	}

	public static List<String> getUserGroups(final String userName) {
		Group[] groups = Netapi32Util.getUserGroups(userName, Netapi32Util.getDCName());
		List<String> userGroups = new ArrayList<String>();
		for (Group group : groups) {
			userGroups.add(group.name);
		}
		return userGroups;
	}

	public static List<String> getRecursiveGroupMembership() {
		List<String> groupList = new ArrayList<String>();
		for (Advapi32Util.Account account : Advapi32Util.getCurrentUserGroups()) {
			groupList.add(account.fqn);
		}
		return groupList;
	}
}
