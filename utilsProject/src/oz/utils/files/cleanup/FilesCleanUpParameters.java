package oz.utils.files.cleanup;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.XMLFileParameters;

public class FilesCleanUpParameters {
	private static final String FOLDER = "folder";
	private static final String RECURSIVE = "recursive";
	private static final String DEPTH = "depth";
	private static final String DELETE_IF_AGE_IN_DAYS_IS_GREATER_THAN = "deleteIfAgeinDaysIsGreaterThan";
	private static final String NAME_PATTERN = "namePattern";
	private static final String DELETE_IF_FILE_LENGTH_GREATER_THAN = "deleteIfFileLengthGreaterThan";
	private static final String ACTUALLY_DELETE = "actuallyDelete";
	private static final String FILES_OR_FOLDERS = "filesOrFolders";

	private static Logger logger = JulUtils.getLogger();

	public static void processParameters(final String[] args) {
		if (args.length != 1) {
			logger.warning("One parameter should be specified.\nProcessing terminated.");
			System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
		}
		int numberOfInputParameters = XMLFileParameters.buildInputParameters(args[0]);
		if (numberOfInputParameters < 1) {
			logger.warning("Input parameters processing failed.\nProcessing terminated.");
			System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
		}
		for (int i = 0; i < numberOfInputParameters; i++) {
			/*
			 * get actual input parameters
			 */
			String myFolder = XMLFileParameters.getInputParameter(FOLDER, i);
			File myFolderFile = new File(myFolder);
			if (myFolderFile.isDirectory()) {
				// System.out.println("Rem folder=" + myFolder);
				String recursiveString = XMLFileParameters.getInputParameter(RECURSIVE, i);
				boolean recursive = (recursiveString == null) ? false : recursiveString.equalsIgnoreCase(OzConstants.YES);
				int depth = 0;
				if (recursive) {
					String depthString = XMLFileParameters.getInputParameter(DEPTH, i);
					depth = (depthString == null) ? 0 : Integer.parseInt(depthString.trim());
				}
				String deleteIfAgeinDaysIsGreaterThanString = XMLFileParameters.getInputParameter(DELETE_IF_AGE_IN_DAYS_IS_GREATER_THAN, i);
				long deleteIfAgeinDaysIsGreaterThan = (deleteIfAgeinDaysIsGreaterThanString == null) ? OzConstants.INT_0
						: Long.parseLong(deleteIfAgeinDaysIsGreaterThanString.trim());
				String myPattern = XMLFileParameters.getInputParameter(NAME_PATTERN, i);
				String actuallyDeleteString = XMLFileParameters.getInputParameter(ACTUALLY_DELETE, i);
				boolean actuallyDelete = (actuallyDeleteString == null) ? false : actuallyDeleteString.equalsIgnoreCase(OzConstants.YES);
				String fileLengthString = XMLFileParameters.getInputParameter(DELETE_IF_FILE_LENGTH_GREATER_THAN, i);
				long fileLength = (fileLengthString == null) ? OzConstants.INT_MINUS_ONE : Long.parseLong(fileLengthString.trim());
				String filesOrFoldersString = XMLFileParameters.getInputParameter(FILES_OR_FOLDERS, i);
				FilesFoldersEnum filesFoldersEnum = (filesOrFoldersString == null) ? FilesFoldersEnum.FILES
						: FilesFoldersEnum.valueOf(filesOrFoldersString);
				FilesCleanUp.processFolder(myFolder, recursive, myPattern, deleteIfAgeinDaysIsGreaterThan, fileLength, actuallyDelete, depth,
						filesFoldersEnum);
			} else {
				logger.warning(myFolder + " is not a directory. Skipping this parameter !");
			}
		}
	}

}
