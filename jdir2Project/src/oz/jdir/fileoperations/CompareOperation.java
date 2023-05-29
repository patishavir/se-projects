package oz.jdir.fileoperations;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.system.SystemUtils;
import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class CompareOperation extends AbstractFileOperation {
	private static final String EMPTY_LINE = "~+";
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		String destinationDir = dd.getDirName();
		FileInfo destinationFileInfo = dd.getFileInfoByRow(jdirIndex);
		String[] sourceFileParams = { sourceDir, null };
		if (sourceFileInfo != null) {
			sourceFileParams[1] = sourceFileInfo.getFilePartialPath();
		}
		String[] destinationFileParams = { destinationDir, null };
		if (destinationFileInfo != null) {
			destinationFileParams[1] = destinationFileInfo.getFilePartialPath();
		}
		if (sourceFileInfo != null && destinationFileInfo != null) {
			/*
			 * source and destination are not null (both exist)
			 */
			String sourceFilePath = sourceFileParams[0] + File.separator + sourceFileParams[1];
			File sourceFile = new File(sourceFilePath);
			String destinationFilePath = destinationFileParams[0] + File.separator
					+ destinationFileParams[1];
			File destinationFile = new File(destinationFilePath);
			if (sourceFile.isFile() && destinationFile.isFile()) {
				String[] sourceFileArray = FileUtils.readTextFile2Array(sourceFilePath);
				String[] destinationFileArray = FileUtils.readTextFile2Array(destinationFilePath);
				logger.info(String.valueOf("sourceFileArray.length=" + sourceFileArray.length));
				logger.info(String.valueOf("destinationFileArray.length="
						+ destinationFileArray.length));
				ArrayList<String> sourceFileArrayList = new ArrayList<String>();
				ArrayList<String> destinationFileArrayList = new ArrayList<String>();
				final int FORWARD_SEARCH_LINE_LIMIT = 100;
				logger.finest("sourceFileArray.length: " + String.valueOf(sourceFileArray.length)
						+ " destinationFileArray.length: "
						+ String.valueOf(destinationFileArray.length));
				for (int s = 0, d = 0; s < sourceFileArray.length
						|| d < destinationFileArray.length;) {
					logger.finest("s: " + String.valueOf(s) + " d: " + String.valueOf(d));
					if ((s < sourceFileArray.length) && (d == destinationFileArray.length)) {
						s = fillEntries(sourceFileArrayList, destinationFileArrayList,
								sourceFileArray, s, sourceFileArray.length - s);
						break;
					} else if ((s == sourceFileArray.length) && (d < destinationFileArray.length)) {
						d = fillEntries(destinationFileArrayList, sourceFileArrayList,
								destinationFileArray, d, destinationFileArray.length - d);
						break;
					} else if (s < sourceFileArray.length && d < destinationFileArray.length
							&& sourceFileArray[s].equals(destinationFileArray[d])) {
						sourceFileArrayList.add(sourceFileArray[s]);
						destinationFileArrayList.add(destinationFileArray[d]);
						s++;
						d++;
					} else {
						boolean matchFound = false;
						for (int offset = 1; offset + s < sourceFileArray.length
								&& offset + s < s + FORWARD_SEARCH_LINE_LIMIT; offset++) {
							if (sourceFileArray[offset + s].equals(destinationFileArray[d])) {
								s = fillEntries(sourceFileArrayList, destinationFileArrayList,
										sourceFileArray, s, offset);
								matchFound = true;
							}

						}
						for (int offset = 1; offset + d < destinationFileArray.length
								&& offset + d < d + FORWARD_SEARCH_LINE_LIMIT; offset++) {
							if (destinationFileArray[offset + d].equals(sourceFileArray[s])) {
								d = fillEntries(destinationFileArrayList, sourceFileArrayList,
										destinationFileArray, d, offset);
								matchFound = true;
							}

						}
						if (!matchFound) {
							s = fillEntries(sourceFileArrayList, destinationFileArrayList,
									sourceFileArray, s, 1);
							d = fillEntries(destinationFileArrayList, sourceFileArrayList,
									destinationFileArray, d, 1);
						}
					}
				}

				listArrayLists(sourceFileArrayList, destinationFileArrayList);
				return true;
			}
		}
		logger.warning("Source File and Destination file should both exist !");
		return true;
	}

	private int fillEntries(final ArrayList<String> arrayList1, final ArrayList<String> arrayList2,
			final String[] stringArray, final int s, final int count) {
		int ptr1 = s;
		for (int i = 0; i < count; i++) {
			arrayList1.add(stringArray[ptr1]);
			ptr1++;
			arrayList2.add(EMPTY_LINE);
		}
		return ptr1;
	}

	private void listArrayLists(final ArrayList<String> arrayList1,
			final ArrayList<String> arrayList2) {
		int i = 0;
		StringBuilder stringBuilder = new StringBuilder();
		for (i = 0; i < arrayList1.size() || i < arrayList2.size(); i++) {
			stringBuilder.append(SystemUtils.LINE_SEPARATOR);
			if (i < arrayList1.size()) {
				stringBuilder.append(arrayList1.get(i) + OzConstants.TAB);
			} else {
				stringBuilder.append(EMPTY_LINE + OzConstants.TAB);
			}
			if (i < arrayList2.size()) {
				stringBuilder.append(arrayList2.get(i) + OzConstants.TAB);
			} else {
				stringBuilder.append(EMPTY_LINE + OzConstants.TAB);
			}
		}
		logger.info(stringBuilder.toString());
	}
}