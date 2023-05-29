package oz.infra.swing;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables. The
 * clipboard data format used by the adapter is compatible with the clipboard
 * format used by Excel. This provides for clipboard interoperability between
 * enabled JTables and Excel.
 */
public class ExcelAdapter implements ActionListener {
	private String rowstring, value;
	private Clipboard systemClipboard;
	private StringSelection stringSelection;
	private JComponent jComponent1;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	/**
	 * The Excel Adapter is constructed with a JTable on which it enables
	 * Copy-Paste and acts as a Clipboard listener.
	 */
	public ExcelAdapter(final JComponent jComponent) {
		// System.out.println("Entering ExcelAdapter constructor");
		jComponent1 = jComponent;
		KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
		// Identifying the copy KeyStroke user can modify this
		// to copy on some other Key combination.
		KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
		// Identifying the Paste KeyStroke user can modify this
		// to copy on some other Key combination.
		jComponent1.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);
		jComponent1.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
		systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	/**
	 * Public Accessor methods for the Table on which this adapter acts.
	 */
	/**
	 * This method is activated on the Keystrokes we are listening to in this
	 * implementation. Here it listens for Copy and Paste ActionCommands.
	 * Selections comprising non-adjacent cells result in invalid selection and
	 * then copy action cannot be performed. Paste is done by aligning the upper
	 * left corner of the selection with the 1st element in the current
	 * selection of the JTable.
	 */
	public void actionPerformed(ActionEvent e) {
		// System.out.println("Entering ExcelAdapter actionEvent");
		/*
		 * copy processing
		 */
		if (e.getActionCommand().compareTo("Copy") == 0) {
			StringBuffer stringBuffer = new StringBuffer();
			// Check to ensure we have selected only a contiguous block of
			// cells
			if (jComponent1 instanceof JTable) {
				JTable jTable1 = (JTable) jComponent1;
				int numcols = jTable1.getSelectedColumnCount();
				int numrows = jTable1.getSelectedRowCount();
				int[] rowsselected = jTable1.getSelectedRows();
				int[] colsselected = jTable1.getSelectedColumns();
				if (!((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0] && numrows == rowsselected.length) && (numcols - 1 == colsselected[colsselected.length - 1]
						- colsselected[0] && numcols == colsselected.length))) {
					JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
							"Invalid Copy Selection", JOptionPane.ERROR_MESSAGE);
					return;
				}
				for (int i = 0; i < numrows; i++) {
					for (int j = 0; j < numcols; j++) {
						stringBuffer.append(jTable1.getValueAt(rowsselected[i], colsselected[j]));
						if (j < numcols - 1)
							stringBuffer.append("\t");
					}
					stringBuffer.append("\n");
				}
				stringSelection = new StringSelection(stringBuffer.toString());
			} else if (jComponent1 instanceof JTextComponent) {
				JTextComponent jTextComponent1 = (JTextComponent) jComponent1;
				stringSelection = new StringSelection(jTextComponent1.getSelectedText());
			}
			systemClipboard.setContents(stringSelection, stringSelection);
		}
		/*
		 * paste processing
		 */
		if (e.getActionCommand().compareTo("Paste") == 0) {
			logger.finest("Trying to Paste");
			try {
				if (jComponent1 instanceof JTable) {
					JTable jTable1 = (JTable) jComponent1;
					int startRow = (jTable1.getSelectedRows())[0];
					int startCol = (jTable1.getSelectedColumns())[0];
					String trstring = (String) (systemClipboard.getContents(this)
							.getTransferData(DataFlavor.stringFlavor));
					System.out.println("String is:" + trstring);
					StringTokenizer st1 = new StringTokenizer(trstring, "\n");
					for (int i = 0; st1.hasMoreTokens(); i++) {
						rowstring = st1.nextToken();
						StringTokenizer st2 = new StringTokenizer(rowstring, "\t");
						for (int j = 0; st2.hasMoreTokens(); j++) {
							value = (String) st2.nextToken();
							if (startRow + i < jTable1.getRowCount()
									&& startCol + j < jTable1.getColumnCount())
								jTable1.setValueAt(value, startRow + i, startCol + j);
							System.out.println("Putting " + value + "at row=" + startRow + i
									+ "column=" + startCol + j);
						}
					}
				} else if (jComponent1 instanceof JTextComponent) {
					JTextComponent jTextComponent1 = (JTextComponent) jComponent1;
					String String2Paste = (String) (systemClipboard.getContents(this)
							.getTransferData(DataFlavor.stringFlavor));
					String sourceString = jTextComponent1.getText();
					String targetString = sourceString.substring(0,
							jTextComponent1.getSelectionStart())
							+ String2Paste
							+ sourceString.substring(jTextComponent1.getSelectionEnd());
					jTextComponent1.setText(targetString);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}