package oz.clearcase.find;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CtFindResultsTableCellRenderer extends DefaultTableCellRenderer {
	private static final Color SELECTEDCOLOR = new Color(204, 204, 255);

	public final Component getTableCellRendererComponent(final JTable table, final Object value,
			final boolean isSelected, final boolean hasFocus, final int row, final int column) {
		JLabel l = new JLabel();
		// l.setBorder(BorderFactory.createLineBorder(Color.black));
		l.setOpaque(true);
		if (isSelected) {
			l.setBackground(SELECTEDCOLOR);
		} else {
			l.setBackground(Color.WHITE);
		}
		if (value == null) {
			return l;
		}
		l.setText(value.toString());
		l.setToolTipText(value.toString());
		if (table.getColumnName(column).equals("Name")) {
			int vobDirectoryStringLength = CtFindParametersJPanel.getVobDirectoryJTextField()
					.getText().length();
			if (value != null && value.toString().length() > vobDirectoryStringLength) {
				l.setText(value.toString().substring(vobDirectoryStringLength));
			} else {
				l.setText(value.toString());
			}
		} else if (table.getColumnName(column).equals("Date Time")) {
			l.setHorizontalAlignment(JLabel.CENTER);
			String stringValue = (String) value;
			if (stringValue.indexOf(".") == 8) {
				String dateTimeString = stringValue.substring(0, 4) + "/"
						+ stringValue.substring(4, 6) + "/" + stringValue.substring(6, 8);
				dateTimeString = dateTimeString + "   " + stringValue.substring(9, 11) + ":"
						+ stringValue.substring(11, 13) + ":" + stringValue.substring(13, 15);
				l.setText(dateTimeString);
				table.setValueAt(dateTimeString, row, column);
			}
		} else if ((table.getColumnName(column).equals("Owner"))
				|| (table.getColumnName(column).equals("User"))) {
			l.setHorizontalAlignment(JLabel.CENTER);
		}
		return l;
	}
}