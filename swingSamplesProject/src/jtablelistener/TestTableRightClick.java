package jtablelistener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class TestTableRightClick {

	protected void initUI() {
		final JFrame frame = new JFrame(TestTableRightClick.class.getSimpleName());
		Vector<String> columns = new Vector<String>(Arrays.asList("Name", "Age"));
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		for (int i = 0; i < 50; i++) {
			Vector<String> row = new Vector<String>();
			for (int j = 0; j < columns.size(); j++) {
				row.add("Cell " + (i + 1) + "," + (j + 1));
			}
			data.add(row);
		}
		final JTable table = new JTable(data, columns);
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "Right-click performed on table and choose DELETE");
			}
		});
		popupMenu.add(deleteItem);
		table.setComponentPopupMenu(popupMenu);
		frame.add(new JScrollPane(table), BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new TestTableRightClick().initUI();
			}
		});
	}
}