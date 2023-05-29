package oz.infra.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class ZipFileUtils {
	private static Logger logger = JulUtils.getLogger();

	/*
	 * performMessageDigestsComparison method
	 */
	public static final boolean compareJarFiles(final String file1Path,
			final String file2Path) {
		boolean returnCode = false;
		if (file1Path.equalsIgnoreCase(file2Path)) {
			logger.warning("Comapring the same file !!! " + file1Path);
			returnCode = true;
		} else {
			try {
				ZipEntry[] zipEntryArray1 = ZipFileUtils
						.buildZipEntryArrayList(file1Path);
				ZipEntry[] zipEntryArray2 = ZipFileUtils
						.buildZipEntryArrayList(file2Path);
				if (zipEntryArray1.length == zipEntryArray2.length) {
					returnCode = true;
					for (int i = 0; i < zipEntryArray1.length; i++) {
						if (!zipEntryArray1[i].getName().equals(
								zipEntryArray2[i].getName())
								|| zipEntryArray1[i].getSize() != zipEntryArray2[i]
										.getSize()
								|| zipEntryArray1[i].getCrc() != zipEntryArray2[i]
										.getCrc()) {
							returnCode = false;
							logger.info("Zip entries are not equal: "
									+ zipEntryArray1[i].getName()
									+ " size: "
									+ String.valueOf(zipEntryArray1[i]
											.getSize())
									+ " crc: "
									+ String.valueOf(zipEntryArray1[i].getCrc())
									+ "\t"
									+ zipEntryArray2[i].getName()
									+ " size: "
									+ String.valueOf(zipEntryArray2[i]
											.getSize())
									+ " crc: "
									+ String.valueOf(zipEntryArray2[i].getCrc()));
							// break;
						}
					}
				} else {
					logger.info("Different number of file in archive: "
							+ String.valueOf(zipEntryArray1.length) + " , "
							+ String.valueOf(zipEntryArray2.length));
				}
			} catch (IOException ioException) {
				ioException.printStackTrace();
				logger.warning(ioException.getMessage());
				returnCode = false;
			}
		}
		if (returnCode) {
			logger.info("Contents of " + file1Path + " , " + file2Path
					+ " is equal!");
		} else {
			logger.info("Contents of " + file1Path + " , " + file2Path
					+ " is not equal!");
		}
		return returnCode;
	}

	public static ZipEntry[] buildZipEntryArrayList(final String zipFilePath)
			throws IOException {
		List<ZipEntry> zipEntryList = new ArrayList<ZipEntry>();
		ZipFile zipFile = new ZipFile(zipFilePath);
		for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries
				.hasMoreElements();) {
			ZipEntry zipEntry = (ZipEntry) entries.nextElement();
			zipEntryList.add(zipEntry);
			logger.finer(zipEntry.getName());
		}
		ZipEntry[] zipEntryArray = new ZipEntry[0];
		zipEntryArray = zipEntryList.toArray(zipEntryArray);
		logger.finer("\n\n");
		return zipEntryArray;
	}

	public static StringBuilder getZipContents(final String zipFilePath) {
		StringBuilder sb = new StringBuilder();
		String header = StringUtils.concat("\nContents listing of ",
				zipFilePath, SystemUtils.LINE_SEPARATOR);
		sb.append(header);
		try {
			ZipEntry[] zipEntriesArray = buildZipEntryArrayList(zipFilePath);
			for (ZipEntry zipEntry1 : zipEntriesArray) {
				sb.append(SystemUtils.LINE_SEPARATOR);
				sb.append(zipEntry1.getName());
			}
			logger.finest(sb.toString());
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			logger.warning(ExceptionUtils.getStackTrace(ex));
			sb = null;
		}
		return sb;
	}
}
