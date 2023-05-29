/*
 * Created on 23/07/2005
 */
package oz.utils.files.cleanup;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

/**
 * @author Oded
 */
public class FilesCleanUp {
	private static Logger logger = JulUtils.getLogger();

	/*
	 * Main
	 */
	public static void main(final String[] args) {
		logger.info(SystemUtils.getRunInfo());
		FilesCleanUpParameters.processParameters(args);
	}

	static final void processFolder(final String myFolder, final boolean recursive, final String namePattern,
			final long deleteIfAgeinDaysIsGreaterThan, final long fileLength, final boolean actuallyDelete, final int depth,
			final FilesFoldersEnum filesFoldersEnum) {

		long deleteIfAgeInMillisGraterThan = DateTimeUtils.MILLIS_IN_A_DAY * deleteIfAgeinDaysIsGreaterThan;
		long now = System.currentTimeMillis();
		File[] filesArray;
		File myFolderFile = new File(myFolder);
		/*
		 * get folder files and directories
		 */
		if (recursive) {
			FolderUtils folderUtils = new FolderUtils();
			filesArray = folderUtils.getAllFilesAndFoldersInFolder(myFolderFile, depth);
		} else {
			filesArray = myFolderFile.listFiles();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("\nREM " + filesArray.length + " files and directories in " + myFolder);
		sb.append("\nREM " + "Crieria: name pattern=" + namePattern + " age in milisecodes greater than="
				+ String.valueOf(deleteIfAgeInMillisGraterThan) + " (" + String.valueOf(deleteIfAgeinDaysIsGreaterThan)
				+ " days) fileLength greater than=" + String.valueOf(fileLength));
		//
		int selectedFiles = 0;
		int selectedFolders = 0;
		for (int i = 0; i < filesArray.length; i++) {
			File myFile = filesArray[i];
			String myFileName = myFile.getName();
			long myLastModified = myFile.lastModified();
			long myFileAge = now - myLastModified;
			if (((namePattern == null) || myFileName.matches(namePattern)) && myFileAge > deleteIfAgeInMillisGraterThan) {
				if (myFile.isFile() && (filesFoldersEnum == FilesFoldersEnum.FILES)) {
					if (myFile.length() > fileLength) {
						selectedFiles++;
						if (!actuallyDelete) {
							sb.append("\ndel \"" + myFile.getAbsolutePath() + "\"");
						} else {
							sb.append("\nerase \"" + myFile.getAbsolutePath() + "\"");
							FileUtils.deleteAndLogResult(myFile);
						}
					}
				} else {
					if (myFile.isDirectory() && (filesFoldersEnum == FilesFoldersEnum.FOLDERS)) {
						selectedFolders++;
						if (!actuallyDelete) {
							sb.append("\nrmdir /s /q \"" + myFile.getAbsolutePath() + "\"");
						} else {
							sb.append("\nerase /s /q \"" + myFile.getAbsolutePath() + "\"");
							FolderUtils.deleteFolder(myFile);
						}
					}
				}
			}

		}
		String summaryLine = StringUtils.concat("\nREM ", String.valueOf(selectedFiles), " files selected ", String.valueOf(selectedFolders),
				" folders selected.");
		sb.append(summaryLine);
		System.out.println(sb.toString());
	}
}