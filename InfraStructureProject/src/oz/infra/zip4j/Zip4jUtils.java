package oz.infra.zip4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class Zip4jUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void addFileDeflateComp(final String zipFilePath, final String file2AddPath,
			final String rootFolderInZip) {
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			// Zip file may not necessarily exist. If zip file exists, then
			// all these files are added to the zip file. If zip file does not
			// exist, then a new zip file is created with the files mentioned
			ZipFile zipFile = new ZipFile(zipFilePath);

			// Build the list of files to be added in the array list
			// Objects of type File have to be added to the ArrayList

			// Initiate Zip Parameters which define various properties such
			// as compression method, etc. More parameters are explained in
			// other
			// examples
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			// set compression method to deflate compression
			// Set the compression level. This value has to be in between 0 to 9
			// Several predefined compression levels are available
			// DEFLATE_LEVEL_FASTEST - Lowest compression level but higher speed
			// of compression
			// DEFLATE_LEVEL_FAST - Low compression level but higher speed of
			// compression
			// DEFLATE_LEVEL_NORMAL - Optimal balance between compression
			// level/speed
			// DEFLATE_LEVEL_MAXIMUM - High compression level with a compromise
			// of speed
			// DEFLATE_LEVEL_ULTRA - Highest compression level but low speed
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			// Now add files to the zip file
			// Note: To add a single file, the method addFile can be used
			// Note: If the zip file already exists and if this zip file is a
			// split file
			// then this method throws an exception as Zip Format Specification
			// does not
			// allow updating split zip files
			parameters.setRootFolderInZip(rootFolderInZip);
			zipFile.addFile(new File(file2AddPath), parameters);
		} catch (ZipException ex) {
			ex.printStackTrace();
		}
	}

	public static void addFilesDeflateComp(final String zipFilePath, final ArrayList filesToAdd) {
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			// Zip file may not necessarily exist. If zip file exists, then
			// all these files are added to the zip file. If zip file does not
			// exist, then a new zip file is created with the files mentioned
			ZipFile zipFile = new ZipFile(zipFilePath);

			// Build the list of files to be added in the array list
			// Objects of type File have to be added to the ArrayList
			// ArrayList filesToAdd = new ArrayList();
			// filesToAdd.add(new File("c:\\ZipTest\\sample.txt"));
			// filesToAdd.add(new File("c:\\ZipTest\\myvideo.avi"));
			// filesToAdd.add(new File("c:\\ZipTest\\mysong.mp3"));

			// Initiate Zip Parameters which define various properties such
			// as compression method, etc. More parameters are explained in
			// other
			// examples
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			// set compression method to deflate compression
			// Set the compression level. This value has to be in between 0 to 9
			// Several predefined compression levels are available
			// DEFLATE_LEVEL_FASTEST - Lowest compression level but higher speed
			// of compression
			// DEFLATE_LEVEL_FAST - Low compression level but higher speed of
			// compression
			// DEFLATE_LEVEL_NORMAL - Optimal balance between compression
			// level/speed
			// DEFLATE_LEVEL_MAXIMUM - High compression level with a compromise
			// of speed
			// DEFLATE_LEVEL_ULTRA - Highest compression level but low speed
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			// Now add files to the zip file
			// Note: To add a single file, the method addFile can be used
			// Note: If the zip file already exists and if this zip file is a
			// split file
			// then this method throws an exception as Zip Format Specification
			// does not
			// allow updating split zip files
			zipFile.addFiles(filesToAdd, parameters);
		} catch (ZipException ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	public static void addFilesDeflateComp(final String zipFilePath, final String folder2Add) {
		List<File> fileList = FolderUtils.getRecursivelyAllFiles(folder2Add);
		ArrayList filesToAdd = new ArrayList();
		for (File file1 : fileList) {
			filesToAdd.add(file1);
		}
		addFilesDeflateComp(zipFilePath, filesToAdd);
	}

	public static void addFolder(final String zipFilePath, final String folder2AddPath, final String rootFolderInZip) {
		logger.info(StringUtils.concat("starting AddFolder ", folder2AddPath, " to ", zipFilePath,
				".\n root folder in zip: ", rootFolderInZip));
		StopWatch stopWatch = new StopWatch();
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(zipFilePath);

			// Folder to add
			String folderToAdd = folder2AddPath;

			// Initiate Zip Parameters which define various properties such
			// as compression method, etc.
			ZipParameters parameters = new ZipParameters();

			// set compression method to store compression
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

			// Set the compression level
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			if (rootFolderInZip != null) {
				parameters.setRootFolderInZip(rootFolderInZip);
			}
			// Add folder to the zip file
			zipFile.addFolder(folderToAdd, parameters);
			String message = StringUtils.concat(folder2AddPath, " has been successfully added to ", zipFilePath,
					"\nElapsed time: ");
			logger.info(stopWatch.appendElapsedTimeToMessage(message));

		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	public static void extractAllFiles(final String zipFilePath, final String targetFolderPath) {
		logger.info(StringUtils.concat("starting extractAllFiles from ", zipFilePath, " to ", targetFolderPath));
		StopWatch stopWatch = new StopWatch();
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(zipFilePath);
			// Extracts all files to the path specified
			zipFile.extractAll(targetFolderPath);
			String message = StringUtils.concat(OzConstants.LINE_FEED, zipFilePath,
					" has been successfully extracted to ", targetFolderPath, " : Elapsed time: ");
			logger.info(stopWatch.appendElapsedTimeToMessage(message));
		} catch (ZipException ex) {
			ex.printStackTrace();
		}

	}

	public static void extractSingleFile(final String zipFilePath, final String relativePath,
			final String targetFolderPath, final String password) {
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(zipFilePath);
			// Check to see if the zip file is password protected
			if (zipFile.isEncrypted()) {
				// if yes, then set the password for the zip file
				zipFile.setPassword(password);
			}
			// Specify the file name which has to be extracted and the path to
			// which
			// this file has to be extracted
			zipFile.extractFile(relativePath, targetFolderPath);
			// Note that the file name is the relative file name in the zip
			// file.
			// For example if the zip file contains a file "mysong.mp3" in a
			// folder
			// "FolderToAdd", then extraction of this file can be done as below:
			zipFile.extractFile(relativePath, targetFolderPath);
		} catch (ZipException ex) {
			ex.printStackTrace();
		}
	}

	private Zip4jUtils() {

	}
}
