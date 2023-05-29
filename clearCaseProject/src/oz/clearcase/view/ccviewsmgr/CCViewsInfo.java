package oz.clearcase.view.ccviewsmgr;

import java.io.File;
import java.text.DecimalFormat;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.clearcase.infra.ClearCaseClass;
import oz.clearcase.view.ccviewsmgr.gui.CCViewStatusAreaJPanel;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;

/**
 * @author Oded
 */
public class CCViewsInfo {

	private static boolean refresh = true;
	private static String[] detailedViewTableHeader = null;
	private static double[] detailedViewTableColumnWeight = null;
	private static String[][] detailedViewTable = null;
	private static String[] detailedSplitArray = null;
	private static int[] detailedViewTableIndices = null;
	private static int detailedViewTableRowCount = 0;
	//
	private static String[] nonDetailedViewTableHeader = null;
	private static double[] nonDetailedViewTableColumnWeight = null;
	private static String[][] nonDetailedViewTable = null;
	private static String[] nonDetailedSplitArray = null;
	private static int[] nonDetailedViewTableIndices = null;
	private static int nonDetailedViewTableRowCount = 0;
	//
	private static String viewNumber = "";
	private static String viewUser = "";
	private static String viewTag = "";
	private static String viewStorage = "";
	//
	private static final String NEW_LINE = "\n";
	private static final Logger logger = JulUtils.getLogger();

	public static void buildCCViewsArray() {
		logger.finer("Starting buildCCViewsArray()");
		boolean showDetails = CCViewsParameters.isShowDetails();
		logger.info("Entering buildViewTable: "
				+ DateTimeUtils.getCurrentTime());
		String[] tokens;
		DecimalFormat formatter;
		buildViewInfoSplitArray(showDetails);
		String computerNameColon = System.getenv("COMPUTERNAME") + ":";
		String[] splitStringArray = null;
		if (!showDetails) {
			/*
			 * No details loop
			 */
			splitStringArray = nonDetailedSplitArray;
			nonDetailedViewTableHeader = new String[] { "#", "View tag",
					"View Storage", "Stream(?)", "owner" };
			nonDetailedViewTableColumnWeight = new double[] { 0.04, 0.20, 0.55,
					0.15, 0.06 };
			nonDetailedViewTable = new String[splitStringArray.length][nonDetailedViewTableHeader.length];
			nonDetailedViewTableIndices = new int[splitStringArray.length];
			// build formatter
			formatter = buildFormatter(splitStringArray.length);
			nonDetailedViewTableRowCount = 0;
			for (int i = 0; i < splitStringArray.length; i++) {
				logger.finer(splitStringArray[i]);
				viewNumber = "";
				viewUser = "";
				viewTag = "";
				viewStorage = "";
				String viewStream = "";
				tokens = splitStringArray[i].trim().split("\\s+");
				if (tokens.length == 3 && tokens[0].equals("*")) {
					viewTag = tokens[1];
					viewStorage = tokens[2];
				} else if (tokens.length == 2) {
					viewTag = tokens[0];
					viewStorage = tokens[1];
				}
				int l = viewStorage.lastIndexOf(File.separator) + 1;
				int k = viewStorage.lastIndexOf(".");
				if (k > l) {
					viewStream = viewStorage.substring(l, k);
				}
				k = viewStorage.lastIndexOf(File.separator);
				l = viewStorage.lastIndexOf(File.separator, (k - 1));
				if (k > l) {
					viewUser = (viewStorage.substring(l + 1, k)).toLowerCase();
				}
				viewNumber = formatter.format(i);
				nonDetailedViewTable[i][0] = viewNumber;
				nonDetailedViewTable[i][1] = viewTag;
				nonDetailedViewTable[i][2] = viewStorage;
				if (viewStorage.indexOf(computerNameColon) == 0) {
					nonDetailedViewTable[i][2] = viewStorage
							.substring(computerNameColon.length());
				}
				nonDetailedViewTable[i][3] = viewStream;
				nonDetailedViewTable[i][4] = viewUser;
				if (viewFilter()) {
					nonDetailedViewTableIndices[nonDetailedViewTableRowCount] = i;
					nonDetailedViewTableRowCount++;
				}
			}
		}
		/*
		 * show details processing
		 */
		else {
			splitStringArray = detailedSplitArray;
			detailedViewTableHeader = new String[] { "#", "View tag",
					"View Storage", "Last access", "View tag uuid" };
			detailedViewTableColumnWeight = new double[] { 0.05, 0.15, 0.50,
					0.08, 0.22 };
			// get View count
			int viewsCount = 0;
			for (int i = 0; i < splitStringArray.length; i++) {
				tokens = splitStringArray[i].trim().split("\\s+");
				if (tokens[0].equals("Tag:")) {
					viewsCount++;
				}
			}
			logger.finer("Number of views is: " + String.valueOf(viewsCount));
			detailedViewTable = new String[viewsCount][detailedViewTableHeader.length];
			detailedViewTableIndices = new int[viewsCount];
			// String myDate;
			formatter = buildFormatter(viewsCount);
			detailedViewTableRowCount = 0;
			int j = -1;
			String viewLastAccess = "";
			String viewTagUuid = "";
			for (int i = 0; i < splitStringArray.length; i++) {
				tokens = splitStringArray[i].trim().split("\\s+");
				if (tokens[0].equals("Tag:")) {
					viewNumber = "";
					viewUser = "";
					viewTag = tokens[1];
					viewStorage = "null";
					// make sure we get no nulls
					viewLastAccess = "null";
					// make sure we get no nulls
					viewTagUuid = "null";
					// make sure we get no nulls
				} else if (tokens[0].equals("Global")
						&& (tokens[1].equals("path:"))) {
					viewStorage = tokens[2];
				} else if (tokens[0].equals("View")
						&& (tokens[1].equals("server"))
						&& (tokens[2].equals("access"))
						&& (tokens[3].equals("path:"))) {
					viewStorage = tokens[4];
				} else if (tokens[0].equals("Last")
						&& (tokens[1].equals("accessed"))) {
					String[] dateSplitArray = tokens[2].split("-");
					String myMonth = DateTimeUtils
							.getGregorianMonthNumFromName(dateSplitArray[1]);
					if (myMonth == null)
						myMonth = DateTimeUtils
								.getGregorianMonthNumFromHebrewName(dateSplitArray[1]);
					if (myMonth == null)
						logger.warning("Invalid date format");
					viewLastAccess = "20" + dateSplitArray[2].substring(0, 2)
							+ "-" + myMonth + "-" + dateSplitArray[0];
				} else if (tokens[0].equals("View")
						&& (tokens[1].equals("tag"))
						& (tokens[2].substring(0, 5).equals("uuid:"))) {
					viewTagUuid = tokens[2].substring(5);
				} else if (tokens[0].equals("Owner:")
						&& tokens[1].indexOf("\\") != -1) {
					viewUser = tokens[1].substring(1 + tokens[1].indexOf("\\"));
				} else if (tokens[0].equals("Other:")) {
					j++;
					viewNumber = formatter.format(j);
					detailedViewTable[j][0] = viewNumber;
					detailedViewTable[j][1] = viewTag;
					detailedViewTable[j][2] = viewStorage;
					detailedViewTable[j][3] = viewLastAccess;
					detailedViewTable[j][4] = viewTagUuid;
					if (viewFilter()) {
						detailedViewTableIndices[detailedViewTableRowCount] = j;
						detailedViewTableRowCount++;
					}
				}
			}
		}
		/*
		 * almost done !
		 */
		CCViewStatusAreaJPanel.setStatus2(String.valueOf(getRowCount())
				+ " views have been found");
		logger.info(String.valueOf(splitStringArray.length)
				+ " input lines have been processed.");
		logger.info("Leaving buildViewTable: " + DateTimeUtils.getCurrentTime());
	}

	/*
	 * buildFormatter
	 */
	private static DecimalFormat buildFormatter(final int arrayLength) {
		int m = (String.valueOf(arrayLength)).length();
		char[] formatterArray = new char[m];
		for (int i = 0; i < m; i++) {
			formatterArray[i] = '0';
		}
		return new DecimalFormat(new String(formatterArray));
	}

	private static void buildViewInfoSplitArray(final boolean showDetails) {
		logger.finest("buildViewInfoSplitArray()");
		if (showDetails && detailedSplitArray != null && !refresh) {
			return;
		}
		if (!showDetails && nonDetailedSplitArray != null && !refresh) {
			return;
		}
		refresh = false;
		logger.finest("Undetailed file path="
				+ CCViewsParameters.getNonDetailedFilePath());
		logger.finest("Detailed file path="
				+ CCViewsParameters.getDetailedFilePath());
		if (CCViewsParameters.isFileInput()) {
			/*
			 * Read view info from files
			 */
			if (showDetails) {
				logger.finer("Detailed file path="
						+ CCViewsParameters.getDetailedFilePath());
				File detailedFile = new File(
						CCViewsParameters.getDetailedFilePath());
				if (!detailedFile.exists()) {
					String errorMessage = detailedFile.getAbsolutePath()
							+ " does not exists !";
					logger.severe(errorMessage);
					JOptionPane.showMessageDialog(null, errorMessage,
							"Error message", JOptionPane.ERROR_MESSAGE);
					return;
				}
				detailedSplitArray = FileUtils.readTextFile(detailedFile)
						.split(NEW_LINE);
				return;
			}
			File undetailedFile = new File(
					CCViewsParameters.getNonDetailedFilePath());
			if (!undetailedFile.exists()) {
				String errorMessage = undetailedFile.getAbsolutePath()
						+ " does not exists !";
				logger.severe(errorMessage);
				JOptionPane.showMessageDialog(null, errorMessage,
						"Error message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			nonDetailedSplitArray = FileUtils.readTextFile(undetailedFile)
					.split(NEW_LINE);
			return;
		}
		/*
		 * perform cleartool command
		 */
		ClearCaseClass ccc = new ClearCaseClass();
		String[] clearToolparams;
		if (showDetails) {
			clearToolparams = new String[] { "", "lsview", "-l", "-prop" };
		} else {
			clearToolparams = new String[] { "", "lsview" };
		}
		PrintCommandHandler.printCommand(clearToolparams);
		if (ccc.clearToolCommand(clearToolparams)) {
			if (showDetails) {
				detailedSplitArray = (ccc.getClearCaseOut()).split(NEW_LINE);
			} else {
				nonDetailedSplitArray = (ccc.getClearCaseOut()).split(NEW_LINE);
			}
		} else {
			logger.warning("** Lsview operation failed. Program will  exit. **\n"
					+ ccc.getClearCaseErr());
			System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
		}
		return;
	}

	/*
	 * getRowCount
	 */
	public static int getRowCount() {
		int rowCount = 0;
		if (CCViewsParameters.isShowDetails()) {
			rowCount = detailedViewTableRowCount;
		} else {
			rowCount = nonDetailedViewTableRowCount;
		}
		logger.finest("RowCount is " + String.valueOf(rowCount));
		return rowCount;
	}

	/**
	 * @return double[]
	 */
	public static double[] getViewTableColumnWeight() {
		logger.finest("staring getViewTableColumnWeight()");
		if (CCViewsParameters.isShowDetails()) {
			return detailedViewTableColumnWeight;
		}
		return nonDetailedViewTableColumnWeight;
	}

	/**
	 * @return String[]
	 */
	public static String[] getViewTableHeader() {
		logger.finest("staring getViewTableHeader()");
		if (CCViewsParameters.isShowDetails()) {
			return detailedViewTableHeader;
		}
		return nonDetailedViewTableHeader;
	}

	/**
	 * @return String
	 */
	public static String getViewTableValueAt(final int i, final int j) {
		logger.finest("Starting getViewTableValueAt(" + String.valueOf(i) + ","
				+ String.valueOf(j) + ")");
		if (CCViewsParameters.isShowDetails()) {
			return detailedViewTable[detailedViewTableIndices[i]][j];
		}
		return nonDetailedViewTable[nonDetailedViewTableIndices[i]][j];
	}

	/*
	 * setRefresh
	 */
	public static void setRefresh(boolean refreshP) {
		logger.finest("Starting refresh()");
		refresh = refreshP;
	}

	/*
	 * setViewTableValueAt
	 */
	public static void setViewTableValueAt(String newValueString, final int i,
			final int j) {
		logger.finest("Starting setViewTableValueAt(" + newValueString + ","
				+ String.valueOf(i) + "," + String.valueOf(j) + ")");
		if (CCViewsParameters.isShowDetails()) {
			detailedViewTable[detailedViewTableIndices[i]][j] = newValueString;
		}
		nonDetailedViewTable[nonDetailedViewTableIndices[i]][j] = newValueString;
	}

	/*
	 * viewFilter
	 */
	private static boolean viewFilter() {
		String viewTagFilter = CCViewsParameters.getViewTagFilter();
		String viewStorageFilter = CCViewsParameters.getViewStorageFilter();
		boolean userCondition = CCViewsParameters.getUserName().length() == 0
				|| CCViewsParameters.getUserName().equalsIgnoreCase(viewUser);
		boolean tagFilterCondition = viewTagFilter.length() == 0
				|| viewTag.indexOf(viewTagFilter) > -1;
		boolean storageFilterCondition = viewStorageFilter.length() == 0
				|| viewStorage.indexOf(viewStorageFilter) > -1;
		logger.finest("viewTagFilter: " + viewTagFilter + "userCondition: "
				+ String.valueOf(userCondition) + " tagFilterCondition: "
				+ String.valueOf(tagFilterCondition));
		return (userCondition && tagFilterCondition && storageFilterCondition);
	}
}