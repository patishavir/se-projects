package oz.temp.switchcase;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.utils.autodeploy.AutoDeployOperations;

public class SwitchCase {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AutoDeployOperations autoDeployType = AutoDeployOperations.PORTALWATCHER;

		switch (autoDeployType) {

		case DEPLOYEAR:
			logger.info("inside DEPLOYEAR");
			break;

		case EARWATCHER:
			logger.info("inside EARWATCHER");
			break;

		case DEPLOYPORTAL:
			logger.info("inside DEPLOYPORTAL");
			break;

		case PORTALWATCHER:
			logger.info("inside PORTALWATCHER");
			break;
		}
	}
}
