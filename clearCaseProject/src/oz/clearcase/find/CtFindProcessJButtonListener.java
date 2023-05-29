/*
 * Created on 11/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package oz.clearcase.find;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseClass;

/**
 * @author Oded
 */
public class CtFindProcessJButtonListener implements ActionListener {
	private String queryString = "";
	private Logger logger = Logger.getLogger(this.getClass().toString());
	// private final String delimiterString = "!>:<";
	private final String delimiterString = ":!>x";
	private int fmtItems;
	private String[] actualColumnNames;
	private static final String NEW_LINE = "\n";

	public final void actionPerformed(final ActionEvent e) {
		performFind();
	}

	//
	// perform find
	//
	final void performFind() {
		queryString = "";
		String elementVersionString = "";
		String branchName = "";
		if (CtFindParametersJPanel.getElementJRadioButton().isSelected()) {
			elementVersionString = "-element";
		} else if (CtFindParametersJPanel.getVersionJRadioButton().isSelected()) {
			elementVersionString = "-version";
		} else if (CtFindParametersJPanel.getBranchJRadioButton().isSelected()) {
			elementVersionString = "-branch";
		}
		String[] clearToolfindParams = new String[15]; // will resize later
		clearToolfindParams[0] = "";
		clearToolfindParams[1] = "find";
		clearToolfindParams[2] = CtFindParametersJPanel.getVobDirectoryJTextField().getText();
		int paramsNum = 3;
		String namePattern = CtFindParametersJPanel.getNamePatternJTextField().getText();
		if (namePattern.length() > 0) {
			clearToolfindParams[paramsNum] = "-name";
			paramsNum++;
			clearToolfindParams[paramsNum] = namePattern;
			paramsNum++;
		}
		if (CtFindParametersJPanel.getCurrentViewOnlyJCheckBox().isSelected()) {
			clearToolfindParams[paramsNum] = "-cview";
			paramsNum++;
		}
		String creatorOwnerUserid = CtFindParametersJPanel.getCreatorUseridJTextField().getText();
		if (creatorOwnerUserid.length() > 0) {
			clearToolfindParams[paramsNum] = "-user";
			paramsNum++;
			clearToolfindParams[paramsNum] = creatorOwnerUserid;
			paramsNum++;
		}
		clearToolfindParams[paramsNum] = elementVersionString;
		paramsNum++;
		String fromDate = CtFindParametersJPanel.getFromDateJTextField().getText();
		String toDate = CtFindParametersJPanel.getToDateJTextField().getText();
		branchName = CtFindParametersJPanel.getBranchNameParameter();
		if (fromDate.length() > 0) {
			buildQueryString("created_since(" + fromDate + ")");
		}
		if (toDate.length() > 0) {
			buildQueryString("!created_since(" + toDate + ")");
		}
		if (branchName != null && branchName.length() > 0) {
			buildQueryString("brtype(" + branchName + ")");
		}
		if (queryString.length() > 0) {
			queryString = queryString + "}\"";
			clearToolfindParams[paramsNum] = queryString;
			paramsNum++;
		}
		// clearToolfindParams[paramsNum] = "-print";
		clearToolfindParams[paramsNum] = "-exec";
		String describeCommandString = "\"";
		String[] clearTooldescribeParams = buildDescribeCommand();
		for (int i = 0; i < clearTooldescribeParams.length; i++) {
			describeCommandString = describeCommandString + clearTooldescribeParams[i] + " ";
		}
		if (CtFindParametersJPanel.getElementJRadioButton().isSelected()) {
			describeCommandString = describeCommandString + "%CLEARCASE_PN%\"";
		} else {
			describeCommandString = describeCommandString + "%CLEARCASE_XPN%\"";
		}
		paramsNum++;
		clearToolfindParams[paramsNum] = describeCommandString;
		String[] actualParams = new String[paramsNum + 1];
		for (int i = 0; i < actualParams.length; i++) {
			actualParams[i] = clearToolfindParams[i];
		}
		ClearCaseClass ccc = new ClearCaseClass();
		if (ccc.clearToolCommand(actualParams)) {
			logger.fine(ccc.getClearCaseOut());
		}
		String[] stringArrayFindResults = (ccc.getClearCaseOut()).split(NEW_LINE);
		String[][] string2DimArrayFindResults = new String[stringArrayFindResults.length][fmtItems];
		for (int rowNum = 0; rowNum < stringArrayFindResults.length; rowNum++) {
			String[] stringArrayDescribe = stringArrayFindResults[rowNum].split(delimiterString);
			for (int i = 0; i < stringArrayDescribe.length; i++) {
				logger.finer("Split describe output: " + stringArrayDescribe[i]);
				string2DimArrayFindResults[rowNum][i] = stringArrayDescribe[i];
			}
		}
		new CtFindResultsJFrame(string2DimArrayFindResults, actualColumnNames);
	}

	//
	final void buildQueryString(final String queryItemString) {
		if (queryString.length() > 0) {
			queryString = queryString + " && ";
		} else if (queryString.length() == 0) {
			queryString = queryString + "\"{";
		}
		queryString = queryString + queryItemString;
	}

	//
	/*
	 * buildDescribeCommand
	 */
	String[] buildDescribeCommand() {
		String fmtString = "%n";
		fmtItems = 1;
		String[] columnNames = new String[5];
		columnNames[0] = "Name";
		if (CtFindParametersJPanel.getObjectDateJCheckBox().isSelected()) {
			fmtString = fmtString + delimiterString + "%Nd";
			columnNames[fmtItems] = "Date Time";
			fmtItems++;
		}
		if (CtFindParametersJPanel.getObjectUserJCheckBox().isSelected()) {
			if (CtFindParametersJPanel.getVersionJRadioButton().isSelected()) {
				fmtString = fmtString + delimiterString + "%u";
				columnNames[fmtItems] = "User";
			} else {
				fmtString = fmtString + delimiterString + "%[owner]p";
				columnNames[fmtItems] = "Owner";
			}
			fmtItems++;
		}
		if (CtFindParametersJPanel.getVersionJRadioButton().isSelected()
				&& CtFindParametersJPanel.getobjectActivityJCheckBox().isSelected()) {
			fmtString = fmtString + delimiterString + "%[activity]p";
			columnNames[fmtItems] = "Activity";
			fmtItems++;
		}
		if (CtFindParametersJPanel.getVersionJRadioButton().isSelected()
				&& CtFindParametersJPanel.getObjectCommentJCheckBox().isSelected()) {
			fmtString = fmtString + delimiterString + "%c";
			columnNames[fmtItems] = "Comment";
			fmtItems++;
		}
		actualColumnNames = new String[fmtItems];
		for (int i = 0; i < fmtItems; i++) {
			actualColumnNames[i] = columnNames[i];
		}
		fmtString = "\"" + fmtString + "\n\"";
		logger.finest("Format string: " + fmtString);
		String[] clearTooldescribeParams = { "cleartool.exe", "describe", "-fmt", fmtString, "" };
		return clearTooldescribeParams;
	}
}