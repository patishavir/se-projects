package oz.jdir.gui;

import java.awt.Color;
import java.util.Comparator;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import oz.jdir.JdirInfo;
import oz.jdir.gui.listeners.DirPanelMouseListener;
import oz.jdir.gui.listeners.RowSMListSelectionListener;

public class DirJTable extends JTable {
	private DirPanel dirPanel;

	private static final String[] columnNames = { "#", "File Name", "Length", "Last Modified" };

	private static final double[] columnPreferredWidthRatio = { 0.07, 0.51, 0.12, 0.30 };

	private static final int[] columnMaxWidth = { 40, 9999, 60, 150 };

	private static final boolean ALLOW_ROW_SELECTION = true;

	private Logger logger = Logger.getLogger(this.getClass().toString());

	DirJTable(final JdirInfo jd, final DirPanel dirPanelP) {
		logger.finest("Entering DirJTable constructor");
		this.dirPanel = dirPanelP;
		DirTableModel dirTableModel = new DirTableModel(jd, columnNames);
		this.setModel(dirTableModel);
		this.setDefaultRenderer(Object.class, new JdirTableCellRenderer());
		this.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		if (ALLOW_ROW_SELECTION) { // true by default
			this.setRowSelectionAllowed(true);
			ListSelectionModel rowSM = this.getSelectionModel();
			RowSMListSelectionListener rowSMListSelectionListener = new RowSMListSelectionListener();
			rowSM.addListSelectionListener(rowSMListSelectionListener);
		} else {
			this.setRowSelectionAllowed(false);
		}
		this.setSelectionBackground(Color.blue);
		this.setSelectionForeground(Color.green);
		DirPanelMouseListener dirPanelMouseListener = new DirPanelMouseListener(dirPanel);
		this.addMouseListener(dirPanelMouseListener);
		TableRowSorter<TableModel> tableRowSorter = new TableRowSorter<TableModel>(this.getModel());
		Comparator<Integer> integerComaparator = new Comparator<Integer>() {
			public int compare(Integer i1, Integer i2) {
				return i1.compareTo(i2);
			}
		};
		Comparator<Long> longComaparator = new Comparator<Long>() {
			public int compare(Long l1, Long l2) {
				return l1.compareTo(l2);
			}
		};
		tableRowSorter.setComparator(0, integerComaparator);
		tableRowSorter.setComparator(2, longComaparator);
		tableRowSorter.setComparator(3, longComaparator);
		this.setRowSorter(tableRowSorter);
	}

	public void repaint() {
		int j = this.getColumnCount();
		TableColumn column;
		for (int i = 0; i < j; i++) {
			column = this.getColumnModel().getColumn(i);
			int prefWidth = (int) (getWidth() * columnPreferredWidthRatio[i]);
			column.setPreferredWidth(prefWidth);
			column.setMaxWidth(columnMaxWidth[i]);
		}
		super.repaint();
	}
}
