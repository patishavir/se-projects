package jtable;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class SpringTable {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new SpringTable().makeUI();
			}
		});
	}

	public void makeUI() {
		Object[][] data = new Object[3][3];
		Object[] columnHeaders = new Object[3];
		String str = "data ";
		StringBuffer part = new StringBuffer(str);

		for (int i = 0; i < data.length; i++) {
			columnHeaders[i] = "Column " + i;
			for (int j = 0; j < data[i].length; j++) {
				part.append(str);
				data[i][j] = part.toString();
			}
		}
		JTable table = new JTable(data, columnHeaders) {

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width,
						tableColumn.getPreferredWidth()));
				return component;
			}

		};
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 200);
		frame.setLocationRelativeTo(null);
		frame.add(new JScrollPane(table));
		frame.setVisible(true);
	}
}