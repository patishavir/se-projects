package oz.utils.modifyear.test;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.utils.modifyear.ModifyEar;

public class TestModifyEar {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		String earFilePath = ModifyEar.modifyEar(".\\args\\modifyEar\\modifyEar.properties",
				"C:\\temp\\auto\\good\\MatafEAR.ear", "T_OZ");
		logger.info(StringUtils.concat(earFilePath, " has been successfully created."));
		SystemUtils.printMessageAndExit("all done ...", OzConstants.EXIT_STATUS_OK, false);
	}
}
