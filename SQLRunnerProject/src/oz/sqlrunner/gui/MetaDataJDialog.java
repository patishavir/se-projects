package oz.sqlrunner.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import oz.infra.swing.container.ContainerUtils;
import oz.infra.swing.dimension.DimensionUtils;

public class MetaDataJDialog extends JDialog {
	static final long serialVersionUID = 1L;

	public MetaDataJDialog(final JTable jtable, final String title) {
		super(SQLRunnerJFrame.getSqlRunnerJFrame());
		final int xMargin = 15;
		final int yMargin = 40;
		DimensionUtils.getAsString(jtable.getPreferredSize(), Level.FINEST);
		jtable.setFillsViewportHeight(false);
		JScrollPane jscrollPane = new JScrollPane(jtable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		DimensionUtils.getAsString(jtable.getPreferredSize(), Level.INFO);
		jscrollPane.setPreferredSize(DimensionUtils.adjustDimension(jtable.getPreferredSize(), xMargin, yMargin));
		DimensionUtils.getAsString(jscrollPane.getPreferredSize(), Level.INFO);
		add(jscrollPane, BorderLayout.CENTER);
		DimensionUtils.getAsString(this.getPreferredSize(), Level.INFO);
		setPreferredSize(DimensionUtils.adjustDimension(jscrollPane.getPreferredSize(), xMargin, yMargin));
		Dimension preferredSize = getPreferredSize();
		setSize(preferredSize);
		ContainerUtils.positionComponentCentrallyInParent(this);
		setTitle(title);
		pack();
		setVisible(true);
	}
}
