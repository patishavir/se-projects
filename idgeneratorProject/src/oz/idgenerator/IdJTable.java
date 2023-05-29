package oz.idgenerator;

import java.awt.Font;

import javax.swing.JTable;

public class IdJTable extends JTable {
	Font normalFont = new Font("Arial", Font.BOLD, 14);
	Font largeFont = new Font("Arial", Font.BOLD, 16);

	IdJTable(final String[][] idTable, final String[] idTableHeader) {
		super(idTable, idTableHeader);
		this.setFont(largeFont);
		this.setCellSelectionEnabled(true);
		this.setDefaultRenderer(Object.class, new IdTableCellRenderer());
		this.getTableHeader().setFont(normalFont);
	}
}