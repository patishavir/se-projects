package oz.utils.cm.ds.common.cc;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import oz.clearcase.clearfsimport.ClearFsImportUtils;
import oz.clearcase.infra.ClearCaseStartViewSetAct;
import oz.clearcase.infra.ClearCaseUtils;
import oz.clearcase.infra.ClearCaseUtils.VIEWTYPE_ENUM;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.fibi.gm.GMUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.utils.cm.ds.DsCmRunParameters;

public class DsCmCCUtils {
	private static final Logger logger = JulUtils.getLogger();

	private static SystemCommandExecutorResponse add2SourceControl(final String ccViewRootFolderPath, final String folder2ImportPath) {
		List<String> parameterList = Arrays.asList(ClearFsImportUtils.RECURSE, ClearFsImportUtils.NSETEVENT);
		SystemCommandExecutorResponse scer = ClearFsImportUtils.runClearfsimport(folder2ImportPath, ccViewRootFolderPath, parameterList);
		logger.info("clearfsimport has completed. return code: " + String.valueOf(scer.getReturnCode()));
		return scer;
	}

	public static int importToClearCase(final String ccViewRootFolderPath, final String folder2ImportPath, final String viewTag,
			final String streamSelector, final String stgloc) {
		String yyyy = String.valueOf(DateTimeUtils.getCurrentYear());
		String activitySelector = viewTag + OzConstants.UNDERSCORE + yyyy;
		FolderUtils.dieUnlessFolderExists(folder2ImportPath);
		ClearCaseStartViewSetAct.startViewSetAct(viewTag, ccViewRootFolderPath, streamSelector, stgloc, activitySelector);
		VIEWTYPE_ENUM viewType = VIEWTYPE_ENUM.SNAPSHOT;
		if (viewTag != null && viewTag.length() > 0) {
			viewType = ClearCaseUtils.getViewTypeByViewTag(viewTag);
		}
		if (viewType.equals(VIEWTYPE_ENUM.DYNAMIC)) {
			ClearCaseUtils.startView(viewTag, ccViewRootFolderPath, true);
		} else {
			SystemCommandExecutorResponse scer = ClearCaseUtils.updateCurrentView(ccViewRootFolderPath);
			logger.info("return code: " + String.valueOf(scer.getReturnCode()));
		}
		File ccViewRootFolder = new File(ccViewRootFolderPath).getAbsoluteFile();
		FolderUtils.dieUnlessFolderExists(ccViewRootFolder);

		String activity = ClearCaseUtils.getActivitySetInCurrentView(ccViewRootFolderPath);
		logger.info(StringUtils.concat("current activity: " + activity));
		SystemCommandExecutorResponse scer = add2SourceControl(ccViewRootFolderPath, folder2ImportPath);
		int returnCode = scer.getReturnCode();
		String clearFsImportLogFolderPath = PathUtils.getParentFolderPath(folder2ImportPath);
		String logFilePath = PathUtils.getFullPath(clearFsImportLogFolderPath,
				"clearfsimport_" + SystemPropertiesUtils.getUserName() + OzConstants.UNDERSCORE + SystemUtils.getHostname() + OzConstants.UNDERSCORE
						+ DsCmRunParameters.getEnvironment() + OzConstants.UNDERSCORE + DateTimeUtils.getTimeStamp() + ".log");
		String clearFsImportOutput = scer.getExecutorResponse();
		FileUtils.writeFile(logFilePath, clearFsImportOutput);
		sendEmail(clearFsImportOutput, scer.getReturnCode());
		return returnCode;
	}

	private static void sendEmail(final String mailMessage, final int returnCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("<HTML><BODY>");
		sb.append(mailMessage);
		sb.append("</BODY></HTML>");
		String subject = "datastage clearfsimport has completed. return code: 0";
		if (returnCode != 0) {
			subject = "datastage clearfsimport has failed. return code: ".concat(String.valueOf(returnCode));
		}
		String userName = SystemPropertiesUtils.getUserName();
		subject = subject.concat(" .Ran by: " + userName + " on " + SystemUtils.getHostname());
		String body = sb.toString();
		String recepients = DsCmCCParameters.getMailRecepients();
		GMUtils.sendEmail(subject, body, recepients);
	}
}
