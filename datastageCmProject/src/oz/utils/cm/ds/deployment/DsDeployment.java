package oz.utils.cm.ds.deployment;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.io.filefilter.FileFilterIsFileAndRegExpression;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.utils.cm.ds.common.DsCmEmailUtils;
import oz.utils.cm.ds.common.DsCmUtils;
import oz.utils.cm.ds.common.DsCmZipUtils;
import oz.utils.cm.ds.common.cc.DsCmCCParameters;

public class DsDeployment {
	private static String sourceFolderPath = DsCmParameters.getSourceFolderPath();
	private static File sourceFolder = null;
	private static final String DSX_FILES_REG_EXPRESSION = "\\S+.dsx";
	private static final String CURRENT_VERSION_FOLDER_NAME = "currentVersion";

	private static final Logger logger = JulUtils.getLogger();

	public static void deploy() {
		sourceFolder = new File(sourceFolderPath);
		FolderUtils.dieUnlessFolderExists(sourceFolder);
		File[] files2ProcessArray = getFilesToProcess();
		String doneFolderPath = DsCmUtils.makeDoneFolder(sourceFolderPath);
		for (File dsxFile2Process : files2ProcessArray) {
			DsCommands dsCommands = new DsCommands(dsxFile2Process);
			dsCommands.runDsCmdImport();
			dsCommands.runDscc();
			DsCmUtils.move2DoneFolder(dsxFile2Process, doneFolderPath);
		}
		DsCmEmailUtils.sendEmails(DsCommands.getEmailMap(), "datastage deployment results for ",
				DsCmParameters.getAdditionalMailRecepients());
		StringBuilder sb = new StringBuilder(StringUtils.concat(SystemUtils.LINE_SEPARATOR, SystemUtils.LINE_SEPARATOR,
				"Deployment has completed ...", SystemUtils.LINE_SEPARATOR));
		sb.append(DsCommands.getImportStats().getStatsReport());
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append(DsCommands.getCompileStats().getStatsReport());
		sb.append(PrintUtils.getSeparatorLine("Datastage deployment has completed"));
		sb.append(SystemUtils.LINE_SEPARATOR);
		logger.info(sb.toString());
		if (DsCommands.getImportStats().getSuccessfulImports() > 0) {
			DsCmCCParameters.setAnything2Import(true);
			DsCmZipUtils.zipContents(sourceFolder);
		}
	}

	private static File[] getFilesToProcess() {
		String currentVersionPath = PathUtils.getFullPath(sourceFolderPath, CURRENT_VERSION_FOLDER_NAME);
		return FolderUtils.getFilesInFolder(currentVersionPath, new FileFilterIsFileAndRegExpression(
				DSX_FILES_REG_EXPRESSION, true), Level.INFO);
	}

}
