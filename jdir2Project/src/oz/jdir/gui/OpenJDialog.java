package oz.jdir.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import oz.infra.io.FileUtils;
import oz.infra.swing.dimension.DimensionUtils;
import oz.jdir.gui.listeners.OpenActionListener;

public class OpenJDialog extends JDialog {
	private String sourceFilePath;
	private JTextArea textArea;

	public OpenJDialog(final String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
		String fileContents = FileUtils.readTextFile(sourceFilePath);
		textArea = new JTextArea(fileContents);
		textArea.setLineWrap(false);
		textArea.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(textArea);
		scrollPane.setPreferredSize(new Dimension(DimensionUtils.getScreenWidth() * 3 / 4, DimensionUtils
				.getScreenHeight() * 3 / 4));

		// OpenMenuBar openMenuBar = new OpenMenuBar(new
		// OpenActionListener(this));
		// setJMenuBar(openMenuBar);
		OpenToolBar openToolBar = new OpenToolBar(new OpenActionListener(this));
		getContentPane().add(openToolBar, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}

	public final String getSourceFilePath() {
		return sourceFilePath;
	}

	public final JTextArea getTextArea() {
		return textArea;
	}
}
