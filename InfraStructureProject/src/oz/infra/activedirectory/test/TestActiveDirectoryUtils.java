package oz.infra.activedirectory.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.activedirectory.ActiveDirectoryUtils;
import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;

public class TestActiveDirectoryUtils {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String[] groups = ActiveDirectoryUtils.getMyGroups();
		ArrayUtils.printArray(groups, OzConstants.LINE_FEED, "my groups",
				Level.FINEST);
	}
}
