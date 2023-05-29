package oz.sqlrunner.gui;

import java.awt.Color;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import oz.infra.string.StringUtils;
import oz.infra.swing.jtext.JTextUtils;

public class SQLStatementTextPane extends JTextPane {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private String newline = System.getProperty("line.separator");
	private AbstractDocument doc;
	private static final String SQL_KEYWORDS = " SELECT FROM WHERE FETCH ";

	SQLStatementTextPane(final String sqlStatementString) {
		setCaretPosition(0);
		setMargin(new Insets(5, 5, 5, 5));
		StyledDocument styledDoc = this.getStyledDocument();
		if (styledDoc instanceof AbstractDocument) {
			doc = (AbstractDocument) styledDoc;
			// doc.setDocumentFilter(new DocumentSizeFilter(MAX_CHARACTERS));
		} else {
			logger.warning("Text pane's document isn't an AbstractDocument!");
			System.exit(-1);
		}
		formatSqlStatement(sqlStatementString);
		JTextUtils.addUndoRedoFunctionality(this);
		// ExcelAdapter excelAdapter = new ExcelAdapter(this);
		// new CopyPastePopupMenuAdapter(this, excelAdapter,
		// CopyPastePopupMenuAdapter.BOTH);
	}

	private void formatSqlStatement(final String sqlSatementString) {
		String[] splitSQLStatementArray = StringUtils.splitOnWhiteSpaces(sqlSatementString);
		SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(simpleAttributeSet, "Arial");
		StyleConstants.setFontSize(simpleAttributeSet, 14);
		StyleConstants.setBold(simpleAttributeSet, true);
		StyleConstants.setForeground(simpleAttributeSet, Color.DARK_GRAY);
		StyleConstants.setItalic(simpleAttributeSet, false);

		SimpleAttributeSet sqlKeywordAttributeSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(sqlKeywordAttributeSet, "Arial");
		StyleConstants.setFontSize(sqlKeywordAttributeSet, 14);
		StyleConstants.setBold(sqlKeywordAttributeSet, true);
		StyleConstants.setForeground(sqlKeywordAttributeSet, Color.BLUE);
		StyleConstants.setItalic(sqlKeywordAttributeSet, true);
		String string2Insert = null;
		try {
			for (int i = 0; i < splitSQLStatementArray.length; i++) {
				if (SQL_KEYWORDS.indexOf(splitSQLStatementArray[i].toUpperCase()) > -1) {
					if (i > 0) {
						string2Insert = newline + splitSQLStatementArray[i] + newline + "   ";
					} else {
						string2Insert = splitSQLStatementArray[i] + newline + "   ";
					}
					if (splitSQLStatementArray[i].equalsIgnoreCase("FETCH")
							&& i < splitSQLStatementArray.length
							&& splitSQLStatementArray[i + 1].equalsIgnoreCase("FIRST")) {
						string2Insert = newline + splitSQLStatementArray[i] + " ";
						logger.info(StringUtils.join(splitSQLStatementArray, i, " "));
					}
					doc.insertString(doc.getLength(), string2Insert, sqlKeywordAttributeSet);
				} else {
					doc.insertString(doc.getLength(), splitSQLStatementArray[i] + " ",
							simpleAttributeSet);
				}
			}
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text.");
		}
	}
}
