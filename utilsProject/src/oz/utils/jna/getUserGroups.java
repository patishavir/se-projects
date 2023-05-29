package oz.utils.jna;

import java.util.List;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.jna.JnaUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;

public class getUserGroups {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String userName = args[0];
		List<String> userGroups = JnaUtils.getUserGroups(userName);
		logger.info(OzConstants.LINE_FEED + ListUtils.getAsDelimitedString(userGroups));
	}

}
