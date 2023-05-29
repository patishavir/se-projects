package oz.utils.modifyear;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class ModifyEarMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		String earFilePath = ModifyEar.modifyEar(args[0], args[1], args[2]);
		logger.info(StringUtils.concat(earFilePath, " has been successfully created."));
		System.exit(OzConstants.EXIT_STATUS_OK);

	}
}
