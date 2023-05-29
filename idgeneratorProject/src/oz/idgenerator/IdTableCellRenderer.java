package oz.idgenerator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

class IdTableCellRenderer extends DefaultTableCellRenderer {
	private static Color defaultColor = Color.white;
	private static Color selectedColor = new Color(204, 205, 247);
	Font largeFont = new Font("Arial", Font.BOLD, 16);

	public Component getTableCellRendererComponent(final JTable table, final Object value,
			final boolean isSelected, final boolean hasFocus, final int row, final int column) {
		JLabel l = new JLabel();
		l.setBackground(defaultColor);
		l.setOpaque(true);
		if (value == null) {
			return l;
		}
		l.setText(value.toString());
		l.setHorizontalAlignment(JLabel.CENTER);
		l.setFont(largeFont);
		if (isSelected) {
			l.setBackground(selectedColor);
		}
		return l;
	}
}