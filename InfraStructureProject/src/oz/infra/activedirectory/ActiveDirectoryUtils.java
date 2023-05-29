package oz.infra.activedirectory;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;

public class ActiveDirectoryUtils {
	private static final String CSCRIPT = "cscript.exe";
	private static final String NOLOGO = "//Nologo";
	private static final String GET_MY_GROUPS_PATH = "\\\\matafcc\\Scripts\\Env\\getMyGroups.vbs";
	public static final String SAM_ACCOUNT_NAME = "sAMAccountName";
	private static final Logger logger = JulUtils.getLogger();

	public static String[] getMyGroups() {
		String[] parameters = { CSCRIPT, NOLOGO, GET_MY_GROUPS_PATH };
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parameters);
		String[] groupsArray = systemCommandExecutorResponse.getStdout().toString().split(OzConstants.LINE_FEED);
		logger.info(StringUtils.concat("you belong to ", String.valueOf(groupsArray.length), " groups."));
		return groupsArray;
	}
}
