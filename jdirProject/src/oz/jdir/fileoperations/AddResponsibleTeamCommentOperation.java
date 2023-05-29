package oz.jdir.fileoperations;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.system.SystemUtils;
import oz.jdir.JdirParameters;
import oz.jdir.abstractfileoperations.AbstractFileContentsModificationOperation;

public class AddResponsibleTeamCommentOperation extends AbstractFileContentsModificationOperation {

	private Logger logger = Logger.getLogger(this.getClass().toString());

	private static final String sufixFilter = ".java";
	private static final String responsibleTeamCommentPrefix = "// ";
	private static final String responsibleTeamCommentSuffix = " team is responsible for maintaining this class";
	private static final String PACKAGE = "package";

	protected String modifyFileContents(final String currentFileContents) {
		String newFileContents = currentFileContents;
		int packageIndex = currentFileContents.indexOf(PACKAGE);
		String prePackageString = currentFileContents.substring(0, packageIndex).trim();
		String responsibleTeamComment = responsibleTeamCommentPrefix
				+ JdirParameters.getResponsibleTeam() + responsibleTeamCommentSuffix;
		if (!prePackageString.startsWith(responsibleTeamComment)) {
			if (prePackageString.length() == 0
					|| (prePackageString.startsWith("/*") && prePackageString.endsWith("*/") && prePackageString
							.indexOf("*/") == prePackageString.lastIndexOf("*/"))) {

				newFileContents = responsibleTeamComment + SystemUtils.LINE_SEPARATOR
						+ currentFileContents;

			} else {
				int responsibleTeamCommentSuffixIndex = prePackageString
						.indexOf(responsibleTeamCommentSuffix);
				String currentResponsibleTeam = "";
				if (responsibleTeamCommentSuffixIndex > responsibleTeamCommentPrefix.length()) {
					currentResponsibleTeam = prePackageString.substring(
							responsibleTeamCommentPrefix.length(),
							responsibleTeamCommentSuffixIndex);
				}
				if (prePackageString.startsWith(responsibleTeamCommentPrefix)
						&& responsibleTeamCommentSuffixIndex > -1
						&& JdirParameters.getResponsibleTeams().trim()
								.indexOf(currentResponsibleTeam) > -1) {
					newFileContents = responsibleTeamComment + SystemUtils.LINE_SEPARATOR
							+ currentFileContents.substring(prePackageString.length());
				}
			}
		}
		return newFileContents;
	}

	/*
	 * 
	 */
	protected boolean processParameters() {
		if (JdirParameters.getResponsibleTeam().length() == 0
				|| JdirParameters.getResponsibleTeams().length() == 0) {
			String errorMessage = "No responsible team/teams specified!\nOperation aborted.";
			logger.warning(errorMessage);
			return false;
		} else {
			return true;
		}
	}

	/*
	 * validateFilePath
	 */
	protected boolean validateFilePath(final String currentFilePath, final File currentFile) {
		boolean returnCode = true;
		if (currentFilePath.length() < 10 || currentFile.length() <= PACKAGE.length()) {
			logger.info(currentFilePath + " path is too short for a java class. ");
			returnCode = false;
		} else {
			String fileExtension = oz.infra.io.PathUtils.getFileExtension(currentFilePath);
			if (currentFile.isFile() && !sufixFilter.equalsIgnoreCase("." + fileExtension)) {
				logger.info("File extension is not .java . " + currentFilePath
						+ " has not been processed!");
				returnCode = false;
			}
		}
		return returnCode;
	}

	/*
	 * validateFileContents
	 */
	protected boolean validateFileContents(final String currentFileContents,
			final String currentFilePath) {
		int packageIndex = currentFileContents.indexOf(PACKAGE);
		int classIndex = currentFileContents.indexOf("class");
		if (packageIndex < 0 || classIndex < 0) {
			logger.info("File does not have a package or a class statement. " + currentFilePath
					+ " has not been processed!");
			return false;
		} else {
			return true;
		}

	}

	/*
	 * getSufixFilter
	 */
	protected String getSuffixFilter() {
		return sufixFilter;
	}
}