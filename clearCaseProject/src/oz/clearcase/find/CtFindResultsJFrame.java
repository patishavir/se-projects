package oz.clearcase.find;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import oz.infra.swing.CopyPastePopupMenuAdapter;
import oz.infra.swing.ExcelAdapter;
import oz.infra.swing.jtable.TableSorter;

/**
 * @author Oded
 */
public class CtFindResultsJFrame extends JFrame {
	private static final Border loweredetched = BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED);
	private static int invokationCounter = 0;
	private JScrollPane jscrollPaneCtFindResults = new JScrollPane();
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	CtFindResultsJFrame(final String[][] string2DimArrayFindResults,
			final String[] actualColumnNames) {
		setTitle("Cleartool find results");
		configurefindResultsJTable(string2DimArrayFindResults, actualColumnNames);
		invokationCounter++;
		JPanel statusJPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		statusJPanel.setBorder(loweredetched);
		//
		JLabel numberofElementsJLabel = new JLabel();
		numberofElementsJLabel.setHorizontalAlignment(JLabel.LEFT);
		statusJPanel.add(numberofElementsJLabel);
		numberofElementsJLabel.setText("Number of elements: "
				+ String.valueOf(string2DimArrayFindResults.length));
		//
		GridBagLayout gridbag = new GridBagLayout();
		Container cp = getContentPane();
		cp.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.PAGE_START;
		//
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.01;
		c.weighty = 0.01;
		c.ipadx = 10;
		c.ipady = 10;
		c.insets = new Insets(10, 10, 0, 10);
		cp.add(jscrollPaneCtFindResults, c);
		//
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.01;
		c.weighty = 0.01;
		c.ipadx = 3;
		c.ipady = 3;
		c.insets = new Insets(10, 10, 10, 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		cp.add(statusJPanel, c);
		//
		setLocation(invokationCounter * 15, invokationCounter * 15);
		pack();
		setVisible(true);
	}

	//
	final void configurefindResultsJTable(final String[][] string2DimArrayFindResults,
			final String[] actualColumnNames) {
		double screenSizeWidthFactor;
		double screenSizeHeightFactor;
		TableModel tableModel = new DefaultTableModel(string2DimArrayFindResults, actualColumnNames);
		TableSorter tableSorter = new TableSorter(tableModel);
		JTable findResultsJTable = new JTable(tableSorter);
		findResultsJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		findResultsJTable.setCellSelectionEnabled(false);
		findResultsJTable.setColumnSelectionAllowed(false);
		findResultsJTable.setRowSelectionAllowed(true);
		tableSorter.setTableHeader(findResultsJTable.getTableHeader());
		findResultsJTable.setBackground(Color.WHITE);
		findResultsJTable.setDefaultRenderer(Object.class, new CtFindResultsTableCellRenderer());
		int tableRowCount = findResultsJTable.getRowCount();
		if (tableRowCount > 35) {
			screenSizeHeightFactor = 0.95;
		} else {
			screenSizeHeightFactor = (double) (tableRowCount + 10) / 50;
		}
		int tableColumnCount = findResultsJTable.getColumnCount();
		if (tableColumnCount < 4) {
			screenSizeWidthFactor = 0.75;
		} else {
			screenSizeWidthFactor = 0.95;
		}
		setLocation(invokationCounter * 25, invokationCounter * 20);
		ExcelAdapter excelAdapter = new ExcelAdapter(findResultsJTable);
		CopyPastePopupMenuAdapter copyPastePopupMenuAdapter = new CopyPastePopupMenuAdapter(
				findResultsJTable, excelAdapter, CopyPastePopupMenuAdapter.COPYONLY);
		/*
		 * add version tree menu item
		 */
		JPopupMenu popup = copyPastePopupMenuAdapter.getPopup();
		String lsvtreeString = "Version tree";
		JMenuItem jmiLsvtree = new JMenuItem(lsvtreeString);
		jmiLsvtree.setActionCommand(lsvtreeString);
		JmiLsvtreeActionListener jmiLsvtreeActionListener = new JmiLsvtreeActionListener(
				findResultsJTable);
		jmiLsvtree.addActionListener(jmiLsvtreeActionListener);
		popup.add(jmiLsvtree);
		/*
		 * Set Columns width
		 */
		TableColumnModel tableColumnModel = findResultsJTable.getColumnModel();
		TableColumn tableColumn;
		double screenWidth = (int) screenSize.getWidth();
		int columnCount = findResultsJTable.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			tableColumn = tableColumnModel.getColumn(i);
			String headerValue = (String) tableColumn.getHeaderValue();
			if (headerValue.equals("Name")) {
				tableColumn.setPreferredWidth((int) (screenWidth * 0.5));
			} else if (headerValue.equals("Date Time")) {
				tableColumn.setPreferredWidth((int) (screenWidth * 0.12));
			} else if (headerValue.equals("User") || headerValue.equals("Owner")) {
				tableColumn.setPreferredWidth((int) (screenWidth * 0.06));
			}
		}
		/*
		 * jscrollPaneCtFindResults setup
		 */
		Dimension preferredDimension = new Dimension(
				(int) (screenSize.width * screenSizeWidthFactor),
				(int) (screenSize.height * screenSizeHeightFactor));
		jscrollPaneCtFindResults
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPaneCtFindResults.setPreferredSize(preferredDimension);
		jscrollPaneCtFindResults.setViewportView(findResultsJTable);
		jscrollPaneCtFindResults.setBackground(Color.WHITE);
		Rectangle rect = findResultsJTable.getBounds();
		jscrollPaneCtFindResults.scrollRectToVisible(rect);
	}
}