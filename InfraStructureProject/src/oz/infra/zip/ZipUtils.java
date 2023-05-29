package oz.infra.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public class ZipUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void addFilesToExistingZip(final File existingZipFile, final File[] files2Add,
			final String... zipEntryBases) {
		StringBuilder sb = new StringBuilder();
		Outcome outcome = Outcome.SUCCESS;
		String zipEntryBase = VarArgsUtils.getMyArg(null, zipEntryBases);
		try {
			File tmpZip = File.createTempFile(existingZipFile.getName(), null);
			tmpZip.delete();
			if (existingZipFile.renameTo(tmpZip)) {
				logger.warning(StringUtils.concat(existingZipFile.getName(), " has been renamed to ",
						tmpZip.getAbsolutePath()));
				byte[] buffer = new byte[1024];
				ZipInputStream zin = new ZipInputStream(new FileInputStream(tmpZip));
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(existingZipFile));
				for (int i = 0; i < files2Add.length; i++) {
					InputStream in = new FileInputStream(files2Add[i]);
					String entryPath = files2Add[i].getAbsolutePath();
					if (zipEntryBase != null) {
						entryPath = PathUtils.getRelativePath(entryPath, zipEntryBase);
					}
					out.putNextEntry(new ZipEntry(entryPath));
					for (int read = in.read(buffer); read > -1; read = in.read(buffer)) {
						out.write(buffer, 0, read);
					}
					out.closeEntry();
					in.close();
					if (sb.length() > 0) {
						sb.append(OzConstants.COMMA);
					}
					sb.append(entryPath);
				}
				for (ZipEntry ze = zin.getNextEntry(); ze != null; ze = zin.getNextEntry()) {
					out.putNextEntry(ze);
					for (int read = zin.read(buffer); read > -1; read = zin.read(buffer)) {
						out.write(buffer, 0, read);
					}
					out.closeEntry();
				}
				out.close();
				tmpZip.delete();
				zin.close();
			} else {
				logger.warning(StringUtils.concat("failed to rename ", existingZipFile.getName(), "  to ",
						tmpZip.getAbsolutePath()));
				outcome = Outcome.FAILURE;
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			outcome = Outcome.FAILURE;
		}
		if (outcome == Outcome.SUCCESS) {
			logger.info(StringUtils.concat(sb.toString(), " have been added to ", existingZipFile.getName()));
		}
	}

	public static void addStringsToExistingZip(final File existingZipFile, final String[] files2Add,
			final String entryPaths[], final String... zipEntryBases) {
		StringBuilder sb = new StringBuilder();
		Outcome outcome = Outcome.SUCCESS;
		String zipEntryBase = VarArgsUtils.getMyArg(null, zipEntryBases);
		try {
			File tmpZip = File.createTempFile(existingZipFile.getName(), null);
			tmpZip.delete();
			if (existingZipFile.renameTo(tmpZip)) {
				logger.warning(StringUtils.concat(existingZipFile.getName(), " has been renamed to ",
						tmpZip.getAbsolutePath()));

				ZipInputStream zin = new ZipInputStream(new FileInputStream(tmpZip));
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(existingZipFile));
				String entryPath = null;
				for (int i = 0; i < files2Add.length; i++) {
					entryPath = entryPaths[i];
					if (zipEntryBase != null) {
						entryPath = PathUtils.getRelativePath(entryPath, zipEntryBase);
					}
					out.putNextEntry(new ZipEntry(entryPath));
					byte[] currentFileContents = files2Add[i].getBytes();
					out.write(currentFileContents, 0, currentFileContents.length);

					out.closeEntry();
					if (sb.length() > 0) {
						sb.append(OzConstants.COMMA);
					}
					sb.append(entryPath);
				}
				byte[] buffer = new byte[1024];
				for (ZipEntry ze = zin.getNextEntry(); ze != null; ze = zin.getNextEntry()) {
					out.putNextEntry(ze);
					for (int read = zin.read(buffer); read > -1; read = zin.read(buffer)) {
						out.write(buffer, 0, read);
					}
					out.closeEntry();
				}
				out.close();
				tmpZip.delete();
			} else {
				logger.warning(StringUtils.concat("failed to rename ", existingZipFile.getName(), "  to ",
						tmpZip.getAbsolutePath()));
				outcome = Outcome.FAILURE;
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			outcome = Outcome.FAILURE;
		}
		if (outcome == Outcome.SUCCESS) {
			logger.info(StringUtils.concat(sb.toString(), " have been added to ", existingZipFile.getName()));
		}
	}

	public static int extractAllFiles(final String zipFilePath, final String targetFolderPath) {
		StopWatch stopWatch = new StopWatch();
		String message = null;
		int fileCount = 0;
		int folderCount = 0;
		try {
			FolderUtils.createFolderIfNotExists(targetFolderPath);
			FileInputStream fin = new FileInputStream(zipFilePath);
			BufferedInputStream bin = new BufferedInputStream(fin);
			ZipInputStream zin = new ZipInputStream(bin);
			ZipEntry zipEntry1 = null;
			StringBuilder sb = new StringBuilder();
			while ((zipEntry1 = zin.getNextEntry()) != null) {
				String zeName = zipEntry1.getName();
				String entryFullPath = PathUtils.addPaths(targetFolderPath, zeName);
				entryFullPath = PathUtils.getCanonicalPath(entryFullPath);
				File entryParentFile = new File(entryFullPath).getParentFile();
				if (!entryParentFile.exists()) {
					entryParentFile.mkdirs();
				}
				sb.append(StringUtils.concat(SystemUtils.LINE_SEPARATOR, "entry Full Path: ", entryFullPath));
				if (zipEntry1.isDirectory()) {
					FolderUtils.createFolderIfNotExists(entryFullPath);
					folderCount++;
				} else {
					byte[] buffer = new byte[OzConstants.INT_16384];
					int len;
					OutputStream out = new FileOutputStream(entryFullPath);
					while ((len = zin.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					out.close();
					fileCount++;
				}
			}
			logger.info(sb.toString());
			fin.close();
			zin.close();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			fileCount = OzConstants.INT_MINUS_ONE;
			message = StringUtils.concat("Failed to extract all files from", zipFilePath);
		}
		message = StringUtils.concat("zip file path: ", zipFilePath, "  target folder path: ", targetFolderPath, ".  ",
				String.valueOf(folderCount), " folders and ", String.valueOf(fileCount), " files have been extracted.");
		logger.info(StringUtils.concat(message, " Elapsed time: ", stopWatch.getElapsedTimeString()));
		return fileCount;
	}

	public static Outcome extractFile(final String zipFilePath, final String file2Exatract,
			final String targetFilePath) {
		StopWatch stopWatch = new StopWatch();
		Outcome outcome = Outcome.SUCCESS;
		String message = StringUtils.concat(targetFilePath, " has been extracted from ", zipFilePath);
		try {
			OutputStream out = new FileOutputStream(targetFilePath);
			FileInputStream fin = new FileInputStream(zipFilePath);
			BufferedInputStream bin = new BufferedInputStream(fin);
			ZipInputStream zin = new ZipInputStream(bin);
			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {
				if (ze.getName().equals(file2Exatract)) {
					byte[] buffer = new byte[OzConstants.INT_8192];
					int len;
					while ((len = zin.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					out.close();
					fin.close();
					break;
				}
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			outcome = Outcome.FAILURE;
			message = StringUtils.concat("Failed to extract ", file2Exatract, " from ", zipFilePath);
		}
		stopWatch.logElapsedTimeMessage(StringUtils.concat(message, " Elapsed time: "));
		return outcome;
	}

	public static List<ZipEntry> getZipFileContents(final File inputZipFile) {
		List<ZipEntry> zipContents = new ArrayList<ZipEntry>();
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(inputZipFile);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				zipContents.add(zipEntry);
			}
		} catch (IOException ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return zipContents;
	}

	public static List<ZipEntry> getZipFileContents(final String inputZipFilePath) {
		return getZipFileContents(new File(inputZipFilePath));
	}

	public static String getZipRootFolder(final File zipFile) {
		List<ZipEntry> zipEntriesList = getZipFileContents(zipFile);
		String rootFolder = null;
		for (ZipEntry zipEntry1 : zipEntriesList) {
			String name1 = zipEntry1.getName();
			logger.finest(name1);
			String rootFolder1 = name1.split(RegexpUtils.REGEXP_ANY_FILE_SEPARATOR)[0];
			if (rootFolder == null) {
				rootFolder = rootFolder1;
			} else {
				if (!rootFolder.equals(rootFolder1)) {
					logger.warning(StringUtils.concat("more than one root in zip file ", zipFile.getAbsolutePath(), " ",
							rootFolder, " and ", rootFolder1));
					rootFolder = null;
					break;
				}

			}
		}
		return rootFolder;
	}

	public static String getZipRootFolder(final String zipFilePath) {
		return getZipRootFolder(new File(zipFilePath));
	}

	public static StringBuilder logZipFileContents(final File inputZipFile, final Level... levels) {
		Level level = VarArgsUtils.getMyArg(Level.INFO, levels);
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.concat(inputZipFile.getAbsolutePath(), " contents:\n"));
		List<ZipEntry> zipContents = getZipFileContents(inputZipFile);

		for (ZipEntry zipEntry : zipContents) {
			String name = zipEntry.getName();
			long size = zipEntry.getSize();
			long compressedSize = zipEntry.getCompressedSize();
			boolean isDirectory = zipEntry.isDirectory();
			String zipEntryLine = String.format(
					"name: %-50s \t| size: %6d \t| compressed size: %6d \t| is directory: %b\n", name, size,
					compressedSize, isDirectory);
			sb.append(zipEntryLine);
		}
		logger.log(level, sb.toString());
		return sb;
	}

	public static StringBuilder logZipFileContents(final String zipFilePath, final Level... levels) {
		File zipFile = new File(zipFilePath);
		return logZipFileContents(zipFile, levels);
	}

	public static Outcome zipFile2RelativePath(final String inputFilePath, final String relativePathInZip,
			final String... outputFilePaths) {
		StopWatch stopWatch = new StopWatch();
		Outcome myOutCome = Outcome.SUCCESS;
		String outputFilePath = VarArgsUtils.getMyArg(StringUtils.concat(inputFilePath, OzConstants.ZIP_SUFFIX),
				outputFilePaths);
		byte[] buffer = new byte[OzConstants.INT_1024];
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
			ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
			zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
			ZipEntry ze = new ZipEntry(relativePathInZip);
			zipOutputStream.putNextEntry(ze);
			FileInputStream fileInputStream = new FileInputStream(inputFilePath);
			int len;
			while ((len = fileInputStream.read(buffer)) > 0) {
				zipOutputStream.write(buffer, 0, len);
			}
			fileInputStream.close();
			zipOutputStream.closeEntry();
			zipOutputStream.close();
			logger.info(StringUtils.concat(inputFilePath, " has been successfully zipped to ", outputFilePath,
					". ZipEntry: " + relativePathInZip, " elapse time: ", stopWatch.getElapsedTimeString()));
		} catch (IOException ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			myOutCome = Outcome.FAILURE;
		}
		return myOutCome;
	}

	public static Outcome zipFile2Root(final File inputFile, final String... outputFilePaths) {
		return zipFile2Root(inputFile.getAbsolutePath(), outputFilePaths);
	}

	public static Outcome zipFile2Root(final String inputFilePath, final String... outputFilePaths) {
		String fileNameAndType = PathUtils.getFileNameAndExtension(inputFilePath);
		return zipFile2RelativePath(inputFilePath, fileNameAndType, outputFilePaths);
	}

	public static Outcome zipFiles(final String outputZipFilePath, final List<String> files2ZipList,
			final String... zipEntryBases) {
		Outcome myOutCome = Outcome.SUCCESS;
		int entries = 0;
		long size = 0;
		StopWatch stopWatch = new StopWatch();
		// String zipEntryBase = null;
		// if (zipEntryBaseArray != null & zipEntryBaseArray.length > 0) {
		// zipEntryBase = zipEntryBaseArray[0];
		// }
		String zipEntryBase = VarArgsUtils.getMyArg(null, zipEntryBases);
		zipEntryBase = PathUtils.getAbsolutePath(zipEntryBase);
		logger.info(StringUtils.concat("adding files to ", outputZipFilePath, " base: ", zipEntryBase));
		try {
			byte[] buffer = new byte[OzConstants.INT_1024];
			FileOutputStream fileOutputStream = new FileOutputStream(outputZipFilePath);
			ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
			for (String inputFilePath : files2ZipList) {
				File inputFile = new File(inputFilePath);
				String inputFileAbsolutePath = inputFile.getAbsolutePath();
				String entry = PathUtils.getFileNameAndExtension(inputFilePath);
				if (zipEntryBase != null
						&& inputFileAbsolutePath.toLowerCase().startsWith(zipEntryBase.toLowerCase())) {
					entry = inputFile.getAbsolutePath().substring(zipEntryBase.length() + 1);
				}
				if (inputFile.isDirectory()) {
					entry = entry.concat(OzConstants.SLASH);
				}
				logger.finest(StringUtils.concat("Adding " + inputFileAbsolutePath, " entry: ", entry.toString()));
				ZipEntry zipEntry = new ZipEntry(entry);
				zipOutputStream.putNextEntry(zipEntry);
				if (inputFile.isFile()) {
					int length;
					FileInputStream fileInputStream = new FileInputStream(inputFile);
					while ((length = fileInputStream.read(buffer)) > 0) {
						zipOutputStream.write(buffer, 0, length);
					}
					fileInputStream.close();
				}
				zipOutputStream.closeEntry();
				size = size + zipEntry.getSize();
				entries++;
			}
			zipOutputStream.close();
			logger.info(StringUtils.concat(outputZipFilePath, " Zip file has been created! entries: ",
					String.valueOf(entries), " total size: ", String.valueOf(size), ", ",
					stopWatch.getElapsedTimeString()));
			logZipFileContents(outputZipFilePath, Level.INFO);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			myOutCome = Outcome.FAILURE;
		}
		return myOutCome;
	}

	public static Outcome zipFiles(final String outputZipFilePath, final String[] sourceFileArray,
			final String... zipEntryBaseArray) {
		List<String> sourceFilesList = Arrays.asList(sourceFileArray);
		return zipFiles(outputZipFilePath, sourceFilesList, zipEntryBaseArray);
	}

	public static Outcome zipFolder(final String outputZipFilePath, final File folder2Zip,
			final String... zipEntryBaseArray) {
		List<File> files = FolderUtils.getRecursivelyAllFilesAndSubFolders(folder2Zip);
		List<String> filePathsList = new ArrayList<String>();
		for (File file1 : files) {
			filePathsList.add(file1.getAbsolutePath());
		}
		logger.info(OzConstants.LINE_FEED.concat(ListUtils.getAsDelimitedString(filePathsList)));
		return zipFiles(outputZipFilePath, filePathsList, zipEntryBaseArray);
	}

	public static Outcome zipFolder(final String outputZipFilePath, final String folder2ZipPath,
			final String... zipEntryBaseArray) {
		return zipFolder(outputZipFilePath, new File(folder2ZipPath), zipEntryBaseArray);
	}

}
