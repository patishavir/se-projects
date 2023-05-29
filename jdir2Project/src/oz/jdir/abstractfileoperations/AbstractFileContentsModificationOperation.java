package oz.jdir.abstractfileoperations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.DirectoryWalkerExtender;
import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.fileoperations.FileOperationsEnum;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public abstract class AbstractFileContentsModificationOperation extends AbstractFileOperation {

	private int successfulOperations = 0;
	private int unSuccessfulOperations = 0;

	public boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		initialize(sd, dd, jdirIndex);
		boolean returnCode = true;
		if (!processParameters()) {
			return false;
		} else {

			if (sourceFile.exists() & sourceFile.isFile()) {
				returnCode = processFile(sd, dd, jdirIndex, fileOperationJDialog, sourceFile);
				updateOperationsCounters(returnCode);
			} else if (sourceFile.exists() & sourceFile.isDirectory()) {
				DirectoryWalkerExtender directoryWalkerExtender = new DirectoryWalkerExtender(null,
						FileFilterUtils.suffixFileFilter(getSuffixFilter()), -1);
				ArrayList<File> myFiles = directoryWalkerExtender.startWalking(sourceFile);
				for (File file1 : myFiles) {
					if (file1.isFile()) {
						logger.finest("file to process: " + file1.getAbsolutePath());
						returnCode = processFile(sd, dd, jdirIndex, fileOperationJDialog, file1);
						updateOperationsCounters(returnCode);
					}
				}
			}
		}
		logger.info("Operation " + fileOperations1.toString() + " succeeded for "
				+ String.valueOf(successfulOperations) + " files.");
		if (unSuccessfulOperations > 0) {
			logger.warning("Operation " + fileOperations1.toString() + " failed for "
					+ String.valueOf(unSuccessfulOperations) + " files.");
		}
		if (successfulOperations == 0) {
			logger.warning(fileOperations1.toString() + " failed for " + sourceFilePath);
			returnCode = false;
		}
		return returnCode;
	}

	/*
	 * processFile
	 */
	private boolean processFile(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, File currentFile) {

		boolean returnCode = true;
		String currentFilePath = currentFile.getAbsolutePath();
		if (!currentFile.isFile()) {
			logger.info(currentFilePath + " has not been processed! It is not a file. ");
			return true;
		}
		if (validateFilePath(currentFilePath, currentFile)) {
			int myJdirIndex = sd.getRowByFullPath(currentFilePath);
			FileInfo currentFileInfo = sd.getFileInfoByRow(myJdirIndex);

			logger.finest(" " + currentFilePath + " last modified: "
					+ DateTimeUtils.formatDate(currentFile.lastModified(), "dd/MM/yyyy HH:mm:ss"));
		}
		String currentFileContents = null;
		try {
			currentFileContents = FileUtils.readFileToString(currentFile);
		} catch (IOException ioex) {
			ioex.printStackTrace();
			logger.warning(ioex.getMessage());
			return false;
		}
		if (validateFileContents(currentFileContents, currentFilePath)) {
			String newFileContents = modifyFileContents(currentFileContents);
			if (!newFileContents.equals(currentFileContents)) {

				boolean fileWritten = false;
				try {
					FileUtils.writeStringToFile(currentFile, newFileContents);
					logger.info("File has been successfully modified " + currentFilePath);
					fileWritten = true;
				} catch (IOException ioException) {
					ioException.printStackTrace();
					logger.warning(ioException.getMessage());
					returnCode = false;
				}

				logger.info(this.getClass().getSimpleName() + " operation succeeded for "
						+ currentFilePath);
			} else {
				logger.info("No need to modify " + currentFilePath);
			}
		} else {
			logger.warning(this.getClass().getSimpleName() + " validation failed for "
					+ currentFilePath);
			returnCode = false;
		}
		return returnCode;
	}

	private void updateOperationsCounters(final boolean returnCode) {
		if (returnCode) {
			successfulOperations++;
		} else {
			unSuccessfulOperations++;
		}
	}

	protected abstract String modifyFileContents(final String currentFileContents);

	/*
	 * 
	 */
	protected abstract boolean processParameters();

	/*
	 * validateFilePath
	 */
	protected abstract boolean validateFilePath(final String currentFilePath, final File currentFile);

	/*
	 * validateFileContents
	 */
	protected abstract boolean validateFileContents(final String currentFileContents,
			final String currentFilePath);

	/*
	 * getSufixFilter
	 */
	protected abstract String getSuffixFilter();
}
