package oz.jdir;

import java.io.File;
import java.util.Collections;
import java.util.logging.Logger;

import oz.infra.datetime.StopWatch;
import oz.jdir.fileoperations.FileOperationsEnum;

/**
 * @author Oded
 */
// Synchronize directories - Build combined list
public final class DirMatchTable implements Runnable {
	private static DirMatchTable dirMatchTable = new DirMatchTable();
	private JdirInfo dd;
	private int ddFileListSize;
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private StopWatch myStopWatch = new StopWatch();
	private JdirInfo sd;
	private int sdFileListSize;
	private boolean rebuildFileList;

	private DirMatchTable() {
	}

	public static DirMatchTable getDirMatchTable() {
		return dirMatchTable;
	}

	public void buidMatchTable(final boolean rebuildFileList) {
		this.rebuildFileList = rebuildFileList;
		Thread buidMatchTableThread = new Thread(this);
		buidMatchTableThread.start();
		try {
			buidMatchTableThread.join();
		} catch (InterruptedException iex) {
			iex.printStackTrace();
			logger.severe(iex.getMessage());

		}
	}

	public void run() {
		logger.info("Entering buidMatchTable method");
		sd = JdirParameters.getSd();
		dd = JdirParameters.getDd();
		if (rebuildFileList || (sd.getFileList().isEmpty() && sd.getDirName().trim().length() > 0)
				|| (dd.getFileList().isEmpty() && dd.getDirName().trim().length() > 0)) {
			logger.fine("*** Entering buildFileList method. ***");
			try {
				Thread sdThread = new Thread(sd);
				sdThread.start();
				Thread ddThread = new Thread(dd);
				ddThread.start();
				ddThread.join();
				sdThread.join();
			} catch (InterruptedException iex) {
				iex.printStackTrace();
				logger.warning(iex.getMessage());
			}
			sdFileListSize = sd.getFileList().size();
			ddFileListSize = dd.getFileList().size();
			logger.info(" sdFileListSize: " + String.valueOf(sdFileListSize) + " ddFileListSize: "
					+ String.valueOf(ddFileListSize));
			build2SidedFileInfoPtrs();
			logger.fine("Exiting from buildFileList method.");
		} else {
			build2SidedFileInfoPtrs();
		}
		boolean temporarilyDisableOeration = false;
		if (sd.getBuiltDirName().length() == 0 || dd.getBuiltDirName().length() == 0) {
			temporarilyDisableOeration = true;
		}
		for (FileOperationsEnum fileoperation1 : FileOperationsEnum.values()) {
			if (fileoperation1.getRequiredSides() > 1) {
				fileoperation1.setTemporarilyDisabled(temporarilyDisableOeration);
			}
		}
	}

	/*
	 * build2SidedFileInfoPtrs
	 */
	private void build2SidedFileInfoPtrs() {
		boolean showAll = JdirParameters.getShowOption().equals(ShowOptionsEnum.All);
		boolean showEqual = JdirParameters.getShowOption().equals(ShowOptionsEnum.Equal);
		boolean showDiff = JdirParameters.getShowOption().equals(ShowOptionsEnum.Diff);
		logger.fine("Entering build2SidedFileInfoPtrs method.");
		logger.finer("Start sorting fileLists");
		sd.removeNullsFromFileList();
		dd.removeNullsFromFileList();
		sd.getFileList().trimToSize();
		dd.getFileList().trimToSize();
		sdFileListSize = sd.getFileList().size();
		ddFileListSize = dd.getFileList().size();
		Collections.sort(sd.getFileList());
		Collections.sort(dd.getFileList());
		logger.finer("Sorting fileLists completed");
		sd.initializeDirFileInfoListPtrs(sdFileListSize + ddFileListSize);
		dd.initializeDirFileInfoListPtrs(sdFileListSize + ddFileListSize);
		FileInfo sdFileInfo = null;
		FileInfo ddFileInfo = null;
		int ptr2ptr = 0;
		int sdPtr = 0; // sd fileList pointer
		int ddPtr = 0; // dd fileList pointer
		String sdDirName = sd.getDirName();
		String ddDirName = dd.getDirName();
		do {
			if (sdPtr == sdFileListSize) {
				for (int k2 = ptr2ptr; (!showEqual) && (ddPtr < ddFileListSize); ddPtr++) {
					ddFileInfo = (FileInfo) dd.getFileList().get(ddPtr);
					if (FileInfoFilter.accept(ddFileInfo)) {
						logger.finest("accepted: " + ddFileInfo.getFilePartialPath());
						dd.getDirFileInfoListPtrs()[k2] = ddPtr;
						k2++;
						ptr2ptr++;
					}
				}
				break;
			}
			if (ddPtr == ddFileListSize) {
				for (int k2 = ptr2ptr; (!showEqual) && (sdPtr < sdFileListSize); sdPtr++) {
					sdFileInfo = (FileInfo) sd.getFileList().get(sdPtr);
					if (FileInfoFilter.accept(sdFileInfo)) {
						logger.finest("accepted: " + sdFileInfo.getFilePartialPath());
						sd.getDirFileInfoListPtrs()[k2] = sdPtr;
						k2++;
						ptr2ptr++;
					}
				}
				break;
			}
			sdFileInfo = (FileInfo) sd.getFileList().get(sdPtr);
			if (!FileInfoFilter.accept(sdFileInfo)) {
				sdPtr++;
				continue;
			}
			ddFileInfo = (FileInfo) dd.getFileList().get(ddPtr);
			if (!FileInfoFilter.accept(ddFileInfo)) {
				ddPtr++;
				continue;
			}
			String sdPath2Compare = sdFileInfo.getFilePartialPath();
			String ddPath2Compare = ddFileInfo.getFilePartialPath();
			logger.finest("sdPath2Compare=" + sdPath2Compare + "   ddPath2Compare=" + ddPath2Compare);
			if (sdPath2Compare.equals(ddPath2Compare)) {
				// same file name in both sides
				String sdFullPath = sdDirName + File.separator + sdPath2Compare;
				String ddFullPath = ddDirName + File.separator + ddPath2Compare;
				if ((showAll)
						|| (showEqual && FileComparison.compareFiles(sdFileInfo, sdPath2Compare, sdFullPath, ddFileInfo,
								ddPath2Compare, ddFullPath))
						|| (showDiff && !FileComparison.compareFiles(sdFileInfo, sdPath2Compare, sdFullPath, ddFileInfo,
								ddPath2Compare, ddFullPath))) {
					sd.getDirFileInfoListPtrs()[ptr2ptr] = sdPtr;
					dd.getDirFileInfoListPtrs()[ptr2ptr] = ddPtr;
					ptr2ptr++;
				}
				sdPtr++;
				ddPtr++;
			} else if (sdPath2Compare.compareTo(ddPath2Compare) > 0) {
				if (!showEqual) {
					dd.getDirFileInfoListPtrs()[ptr2ptr] = ddPtr;
					ptr2ptr++;
				}
				ddPtr++;
			} else {
				if (!showEqual) {
					sd.getDirFileInfoListPtrs()[ptr2ptr] = sdPtr;
					ptr2ptr++;
				}
				sdPtr++;
			}
		} while (sdPtr < sdFileListSize || ddPtr < ddFileListSize);
		// end of loop
		logger.info("build2SidedFileInfoPtrs completed. ptr2ptr=" + String.valueOf(ptr2ptr));
		sd.setMatchTableEntries(ptr2ptr);
		dd.setMatchTableEntries(ptr2ptr);
		logger.info("Completed buidMatchTable processing in " + myStopWatch.getElapsedTimeString() + " using "
				+ JdirParameters.getFileComparisonCriteria());
		if (JdirParameters.getFileComparisonCriteria().indexOf("digest") != -1) {
			logger.info("Message digest algorithm: " + JdirParameters.getMessageDigestAlgorithm());
		}
		sd.updateDirPanel();
		dd.updateDirPanel();
	}
}