package oz.utils.cm.ds;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.utils.cm.ds.common.DsCmZipUtils;
import oz.utils.cm.ds.common.cc.DsCmCCParameters;
import oz.utils.cm.ds.common.cc.DsCmCCUtils;
import oz.utils.cm.ds.deployment.DsCmParameters;
import oz.utils.cm.ds.deployment.DsDeployment;
import oz.utils.cm.ds.sql.DsCmSqlParameters;
import oz.utils.cm.ds.sql.run.DsCmSqlRun;

public class DsCmMain {

	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		haavara(args);
	}

	private static void haavara(final String[] args) {
		logger.info(SystemUtils.getRunInfo());
		DsCmRunParameters.processParameters(args[0]);
		if (DsCmRunParameters.isRunDs()) {
			DsCmParameters.processParameters(args[1]);
			DsDeployment.deploy();
		} else {
			logger.info("datastage imports have not been run !");
		}
		if (DsCmRunParameters.isRunSql()) {
			DsCmSqlParameters.processParameters(args[2]);
			DsCmSqlRun.run();
		} else {
			logger.info("sql scripts have not been run !");
		}
		DsCmCCParameters.processParameters(args[3]);
		if (DsCmCCParameters.isImportToClearCase() && DsCmCCParameters.isAnything2Import()) {
			String folder2ImportPath = new File(DsCmZipUtils.getZipFolderPath()).getParent();
			DsCmCCUtils.importToClearCase(DsCmCCParameters.getCcViewRootFolderPath(), folder2ImportPath, DsCmCCParameters.getViewTag(),
					DsCmCCParameters.getStreamSelector(), DsCmCCParameters.getStgloc());
		} else {
			logger.info("importToClearCase has not been performed !");
		}
		logger.info("all done !");
		JulUtils.closeHandlers();
	}
}
