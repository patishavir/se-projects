package oz.jdir;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.clearcase.infra.ClearCaseAttributes;
import oz.clearcase.infra.ClearCaseClass;
import oz.jdir.gui.DirPanel;

/*
 * class Jdir hold all dir Info
 */
public class JdirInfo extends Observable implements Runnable {
	private static int BUILD_FILE_LIST_LOGGING_INTERVAL = 250;
	private static final String DIRMATCHTABLE_PROCESSING_DONE = "JdirInfo: DirMatchTable processing done";

	public static final String getDIRMATCHTABLE_PROCESSING_DONE() {
		return DIRMATCHTABLE_PROCESSING_DONE;
	}

	private String builtDirName = "";
	private int[] dirFileInfoListPtrs;
	private boolean dirIsClearCaseManaged = false;
	private String dirName = "";
	private File dirNameFile;
	private DirPanel dirPanel;
	private int fileCounter;
	private ArrayList<FileInfo> fileList = new ArrayList<FileInfo>();
	private Calendar lastModifiedCalendar;
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private int matchTableEntries = 0;
	private boolean enabled = true;

	/*
	 * 
	 */
	public final int add2FileList(final FileInfo fileInfo) {
		logger.finest("fileList size: " + String.valueOf(fileList.size()));
		fileList.add(fileInfo);
		return fileList.size() - 1;

	}

	/*
	 * build filelist using Java File class
	 */
	private void buildFileList(final File dir) {
		if (!(dir.isDirectory())) {
			logger.fine(dir.getAbsolutePath() + " is not a directory");
			System.exit(-1);
		}
		File[] children = dir.listFiles();
		for (int i = 0; i < children.length; i++) {
			add2FileList(new FileInfo(children[i], dirName));
			if (children[i].isDirectory()) {
				buildFileList(children[i]);
			} else {
				fileCounter++;
				if (fileCounter % BUILD_FILE_LIST_LOGGING_INTERVAL == 0) {
					logger.info("Proccesing completed for " + String.valueOf(fileCounter)
							+ " files in " + builtDirName);
				}
			}
		}
		return;
	}

	/*
	 * buildOneDirectoryFileList
	 */
	private final void buildOneDirectoryFileList(final File dir) {
		if (dir != null) {
			builtDirName = dir.getAbsolutePath();
			logger.info("starting buildFileList for " + builtDirName + "<<<<< ");
			buildFileList(dir);
			fileList.trimToSize();
			logger.info("Completed buildFileList for " + builtDirName + " >>>>>");
		}
	}

	/*
	 * clearFileList
	 */
	private void clearFileList() {
		fileList.clear();
		fileCounter = 0;

	}

	public final String getBuiltDirName() {
		return builtDirName;
	}

	public final int[] getDirsFilesCounters() {
		logger.info("starting getDirsFilesCounters for " + dirName + " !");
		int[] counters = { 0, 0 };
		for (int rowPtr = 0; rowPtr < dirFileInfoListPtrs.length; rowPtr++) {
			if (dirFileInfoListPtrs[rowPtr] >= 0) {
				if (fileList.get(dirFileInfoListPtrs[rowPtr]).isDirectory()) {
					counters[0]++;
				} else if (fileList.get(dirFileInfoListPtrs[rowPtr]).isFile()) {
					counters[1]++;
				} else {
					logger.severe("Entry is neither a file nor a directory !");
					System.exit(-1);
				}
			}
		}
		logger.finest("getDirsFilesCounters completed for " + dirName + " !");
		return counters;
	}

	final int[] getDirFileInfoListPtrs() {
		return dirFileInfoListPtrs;
	}

	/**
	 * getters and setters
	 */
	public final String getDirName() {
		return dirName;
	}

	private final File getDirNameFile() {
		return dirNameFile;
	}

	public final DirPanel getDirPanel() {
		return dirPanel;
	}

	public final int getFileCounter() {
		return fileCounter;
	}

	//
	private final FileInfo getFileInfoByFullPath(final String fullPath) {
		if (fullPath.startsWith(builtDirName)) {
			String myPartialPath = fullPath.substring(builtDirName.length() + 1);
			for (FileInfo fileInfo1 : fileList) {
				if (fileInfo1.getFilePartialPath().equals(myPartialPath)) {
					return fileInfo1;
				}
			}
		}
		return null;
	}

	//
	public final FileInfo getFileInfoByRow(final int row) {
		if (dirFileInfoListPtrs[row] < 0) {
			return null;
		}
		return fileList.get(dirFileInfoListPtrs[row]);
	}

	public final void setFileInfoByRow(final int row, final int fileListPtr) {
		if (dirFileInfoListPtrs[row] < 0) {
			logger.severe("Invalid parameter: " + String.valueOf(row));
		}
		dirFileInfoListPtrs[row] = fileListPtr;
	}

	final ArrayList<FileInfo> getFileList() {
		return fileList;
	}

	public final Calendar getLastModifiedCalendar() {
		return lastModifiedCalendar;
	}

	public final int getMatchTableEntries() {
		return matchTableEntries;
	}

	public final int getRowByFullPath(final String fullPath) {
		String path2Comapre;
		if (fullPath.startsWith(builtDirName)) {
			String myPartialPath = fullPath.substring(builtDirName.length() + 1);
			for (int i = 0; i < dirFileInfoListPtrs.length; i++) {
				if (dirFileInfoListPtrs[i] >= 0) {
					path2Comapre = fileList.get(dirFileInfoListPtrs[i]).getFilePartialPath();
					if (path2Comapre.equals(myPartialPath)) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	final void initializeDirFileInfoListPtrs(final int arraySize) {
		dirFileInfoListPtrs = new int[arraySize];
		for (int i = 0; i < arraySize; i++) {
			dirFileInfoListPtrs[i] = -1;
		}
	}

	public final boolean isDirIsClearCaseManaged() {
		return dirIsClearCaseManaged;
	}

	//
	private final void jdirInit() {
		// JOptionPane.showMessageDialog(null, "Starting jdirInit ...");
		if (dirName != null && dirName.trim().length() > 0) {
			dirNameFile = new File(dirName);
			if (!dirNameFile.isDirectory()) {
				String errorMessage = "Bad Param:" + dirName + " is not a directory !";
				logger.warning(errorMessage);
				System.exit(-1);
			} else {
				if (JdirParameters.isClearCaseSupported()
						&& JdirParameters.isClearCaseIntegrationEnabled()) {
					ClearCaseClass ccc = new ClearCaseClass();
					dirIsClearCaseManaged = ccc.isElementManaged(dirName);
				}
			}
			logger.finest(dirName + ": dirIsClearCaseManaged=" + dirIsClearCaseManaged);
			clearFileList();
		}
	}

	//
	final void printdirFileInfoTable(final String title) {
		System.out.println(title);
		for (int i = 0; i < dirFileInfoListPtrs.length; i++) {
			if (dirFileInfoListPtrs[i] != -1) {
				logger.info(getFileInfoByRow(i).getFilePartialPath());
			}
		}
		int[] counters = getDirsFilesCounters();
		logger.info(String.valueOf(counters[1]) + " files " + String.valueOf(counters[0])
				+ " directories");
	}

	/*
	 * removeFromFileList
	 */
	public final void removeFromFileList(final int row) {
		int startIndex = dirFileInfoListPtrs[row];
		if (!((FileInfo) fileList.get(startIndex)).isDirectory()) {
			fileList.set(startIndex, null);
		} else {
			final String partialPath2Remove = ((FileInfo) fileList.get(startIndex))
					.getFilePartialPath();
			for (int i = startIndex; i < fileList.size(); i++) {
				String myPartialPath = ((FileInfo) fileList.get(i)).getFilePartialPath();
				if (myPartialPath.startsWith(partialPath2Remove)) {
					fileList.set(i, null);
				}
			}
		}
	}

	final void removeNullsFromFileList() {
		for (int i = fileList.size() - 1; i >= 0; i--) {
			if (fileList.get(i) == null) {
				fileList.remove(i);
			}
		}
	}

	public final void run() {
		if (enabled) {
			logger.info("****** Entering buildFileList method for " + getDirName() + " ***");
			if (dirName.trim().length() == 0) {
				dirNameFile = null;
				clearFileList();
				dirFileInfoListPtrs = null;
				fileList = new ArrayList<FileInfo>();
				dirIsClearCaseManaged = false;
				builtDirName = "";
			} else {
				jdirInit();
				logger.info("****** jdirInit() done for " + this.getDirName() + " ***");
				this.buildOneDirectoryFileList(this.getDirNameFile());
				logger.info("******* setClearCaseFileAttributes for " + this.getDirName());
				this.setClearCaseFileAttributes();
				logger.fine("Exiting from buildFileList1 method for: " + dirName);
			}
		}
	}

	/*
	 * setClearCaseFileAttributes
	 */
	final void setClearCaseFileAttributes() {
		if (JdirParameters.isClearCaseIntegrationEnabled() && dirIsClearCaseManaged) {
			ClearCaseClass ccc = new ClearCaseClass();
			ClearCaseAttributes[] clearCaseAttributes = null;
			logger.info("starting setClearCaseFileAttributes for " + dirName);
			clearCaseAttributes = ccc.buildClearCaseAttributesTable(dirName);
			for (int i = 0; i < clearCaseAttributes.length; i++) {
				if (clearCaseAttributes[i] != null) {
					setClearCaseFileAttributes1(clearCaseAttributes[i]);
					logger.finer(clearCaseAttributes[i].getFileFullPath() + " "
							+ clearCaseAttributes[i].isCCmaganaged() + " "
							+ clearCaseAttributes[i].isCheckedOut());
				}
			}
			logger.info("Completed setClearCaseFileAttributes for " + dirName);
		}
	}

	/*
	 * setClearCaseFileAttributes1
	 */
	private void setClearCaseFileAttributes1(final ClearCaseAttributes clearCaseAttributes) {
		if (JdirParameters.isClearCaseIntegrationEnabled() && dirIsClearCaseManaged) {
			for (int j = 0; j < fileList.size(); j++) {
				String myFilePath = ((FileInfo) fileList.get(j)).getFilePartialPath();
				String ccFilePath = clearCaseAttributes.getFileFullPath().substring(
						dirName.length() + 1);
				if (myFilePath.equalsIgnoreCase(ccFilePath)) {
					((FileInfo) fileList.get(j)).setClearCaseAttributes(clearCaseAttributes);
					return;
				}
			}
			logger.warning(clearCaseAttributes.getFileFullPath() + " not found in fileList !!!");
		}
		return;
	}

	public final void setDirName(final String dirNameParam) {
		dirName = "";
		if (dirNameParam != null && dirNameParam.trim().length() > 0) {
			dirNameFile = new File(dirNameParam);
			if (dirNameFile.exists() && dirNameFile.isDirectory()) {
				dirName = dirNameParam;
			} else {
				JOptionPane.showMessageDialog(null, dirNameParam + " is not a directory !",
						"Bad input parameter", JOptionPane.ERROR_MESSAGE);
				getDirPanel().getDirectoryPathJtextField().requestFocus();
				return;
			}
		}
	}

	public final void setDirPanel(final DirPanel dirPanelP) {
		this.dirPanel = dirPanelP;
	}

	public final void setLastModifiedCalendar(final Calendar lastModifiedCalendarP) {
		this.lastModifiedCalendar = lastModifiedCalendarP;
	}

	public final void setMatchTableEntries(final int matchTableEntriesP) {
		this.matchTableEntries = matchTableEntriesP;
	}

	final void updateDirPanel() {
		setChanged();
		notifyObservers(DIRMATCHTABLE_PROCESSING_DONE);
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}