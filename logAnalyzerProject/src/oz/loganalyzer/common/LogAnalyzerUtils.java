package oz.loganalyzer.common;

import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.email.EmailUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.swing.jtable.JTableUtils;
import oz.loganalyzer.LogAnalyzerParameters;

public class LogAnalyzerUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void emailReport(final String reportFilePath) {
		if (LogAnalyzerParameters.isEmailReport()) {
			String emailProprtiesFilePath = LogAnalyzerParameters.getEmailProprtiesFilePath();
			if (emailProprtiesFilePath != null) {
				Properties emailProperties = PropertiesUtils.loadPropertiesFile(emailProprtiesFilePath);
				String subject = StringUtils.concat(LogAnalyzerParameters.getSystem(), " report ",
						DateTimeUtils.formatDate(DateTimeUtils.DATE_FORMAT_dd_MM_yyyy));
				emailProperties.setProperty("subject", subject);
				emailProperties.setProperty("message", subject);
				emailProperties.setProperty("attachmentPath", reportFilePath);
				EmailUtils.sendMultiPartEmail(emailProperties);
			}
		}
	}

	public static void generateSwingOutput(final String[] reportsArray, final String[] sheetNamesArray) {
		JTabbedPane tabbedPane = new JTabbedPane();
		logger.finest("reportsArray length= " + String.valueOf(reportsArray.length));
		for (int j = 0; j < reportsArray.length; j++) {
			String[] rows = reportsArray[j].split(OzConstants.LINE_FEED);
			logger.finest("**** rows length= " + String.valueOf(rows.length));
			if (rows.length > 1) {
				Object[] header = rows[1].split(OzConstants.COMMA);
				int numberOfColumns = header.length;
				Object[][] jtableArray = new String[rows.length - 2][numberOfColumns];
				for (int i = 2; i < rows.length; i++) {
					String[] columns = rows[i].split(OzConstants.COMMA);
					logger.finest("i=" + String.valueOf(i) + " columns length= " + String.valueOf(columns.length));
					String[] fullRow = ArrayUtils.initStringArray(numberOfColumns, "");
					for (int ii = 0; (ii < columns.length) && (ii < fullRow.length); ii++) {
						fullRow[ii] = columns[ii];
					}
					jtableArray[i - 2] = fullRow;
				}
				JTable jtable = JTableUtils.getSpingJTable(jtableArray, header);
				JScrollPane jScrollPane = new JScrollPane(jtable);
				tabbedPane.addTab(sheetNamesArray[j], jScrollPane);
			}
		}
		JFrame jframe = new JFrame("Http server log report");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(tabbedPane);
		jframe.pack();
		jframe.setExtendedState(jframe.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		jframe.setVisible(true);
	}

	public static String getTitle(final String titleText, final String excelDirective) {
		String title = "";
		if (LogAnalyzerParameters.isGenerateExcelWorkBook()) {
			title = title.concat(excelDirective);
		}
		title = title.concat(titleText);
		return title;
	}
}
