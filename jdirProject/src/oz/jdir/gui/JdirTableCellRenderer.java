package oz.jdir.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import oz.clearcase.infra.ClearCaseAttributes;
import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.JdirParameters;

/*
 * class MyTableCellRenderer @author Oded
 */
class JdirTableCellRenderer extends DefaultTableCellRenderer {
	private static final Color selectedColor = Color.yellow;
	private static final Color checkedOutColor = new Color(190, 230, 40);
	private static final Color hijackedColor = new Color(250, 150, 50);
	private static final Color clearCaseManagedColor = new Color(120, 240, 250);
	private static final Color defaultColor = Color.white;
	private static final Color directoryColor = new Color(0, 100, 0);
	private static final Color newerColor = Color.blue;
	private static final Font labelFont = new Font("Arial", Font.BOLD, 12);
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm.ss");
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private static String ERROR_MESSAGE1 = "Could not determine whether source or destination table is being processed !";

	public Component getTableCellRendererComponent(final JTable jtable, final Object value,
			final boolean isSelected, final boolean hasFocus, final int viewRowIndex,
			final int column) {
		int modelRow = jtable.convertRowIndexToModel(viewRowIndex);
		FileInfo myDirFileInfoTable;
		JLabel l = new JLabel();
		l.setBackground(defaultColor);
		l.setFont(labelFont);
		l.setOpaque(true);
		if (isSelected) {
			l.setBackground(selectedColor);
		}
		if (value == null) {
			return l;
		}
		l.setText(value.toString());
		FileInfo sdDirFileInfo = JdirParameters.getSd().getFileInfoByRow(modelRow);
		FileInfo ddDirFileInfo = JdirParameters.getDd().getFileInfoByRow(modelRow);
		switch (column) {
		case 0: // sequence number column
			l.setHorizontalAlignment(JLabel.RIGHT);
			break;
		case 1: // file name column
			if (value == null) {
				break;
			}
			JdirInfo myJd = null;
			l.setHorizontalAlignment(JLabel.LEFT);
			Color myColor = defaultColor;
			// char myCCStatus = ' ';
			if (JdirParameters.getSd().getDirPanel().getDirJTable() == jtable) {
				myJd = JdirParameters.getSd();
			} else if (JdirParameters.getDd().getDirPanel().getDirJTable() == jtable) {
				myJd = JdirParameters.getDd();
			} else {
				logger.severe(ERROR_MESSAGE1);
				System.exit(-1);
			}
			myDirFileInfoTable = myJd.getFileInfoByRow(modelRow);
			ClearCaseAttributes myClearCaseAttributes = myDirFileInfoTable.getClearCaseAttributes();
			String myFilePath = myDirFileInfoTable.getFilePartialPath();
			l.setText(myFilePath);
			l.setToolTipText(myFilePath);
			if (JdirParameters.isClearCaseIntegrationEnabled() && myClearCaseAttributes != null) {
				myColor = defaultColor;
				if (myClearCaseAttributes.isCCmaganaged()) {
					myColor = clearCaseManagedColor;
				}
				if (myClearCaseAttributes.isCheckedOut()) {
					myColor = checkedOutColor;
				} else if (myClearCaseAttributes.isHijacked()) {
					myColor = hijackedColor;
				} else if (myClearCaseAttributes.isFromFoundationBaseline()) {
					myColor = Color.LIGHT_GRAY;
				}
			}
			l.setOpaque(true);
			if (!myColor.equals(defaultColor)) {
				l.setBackground(myColor);
			}
			if ((myDirFileInfoTable != null) && myDirFileInfoTable.isDirectory()) {
				l.setForeground(directoryColor);
				l.setText(l.getText() + " (d)");
			}
			break;
		case 2: // file length column
			l.setText(String.valueOf(value));
			l.setHorizontalAlignment(JLabel.RIGHT);
			break;
		case 3: // date time column
			if (value == null) {
				break;
			}
			l.setText(formatter.format(new Date(((Long) value).longValue())));
			l.setHorizontalAlignment(JLabel.CENTER);
			if (sdDirFileInfo != null && ddDirFileInfo != null) {
				// Set label color to blue if date is higher
				long sdLastModified = sdDirFileInfo.lastModified();
				long ddLastModified = ddDirFileInfo.lastModified();
				/*
				 * Newer file is colored blue
				 */
				if (JdirParameters.getSd().getDirPanel().getDirJTable() == jtable) {
					if (sdLastModified > ddLastModified) {
						l.setForeground(newerColor);
					}
				} else if (JdirParameters.getDd().getDirPanel().getDirJTable() == jtable) {
					if (ddLastModified > sdLastModified) {
						l.setForeground(newerColor);
					}
				} else {
					logger.severe(ERROR_MESSAGE1);
					System.exit(-1);
				}
			}
			break;
		}
		return l;
	}
}