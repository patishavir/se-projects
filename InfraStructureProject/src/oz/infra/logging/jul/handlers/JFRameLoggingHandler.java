package oz.infra.logging.jul.handlers;

import java.awt.Font;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import oz.infra.datetime.DateTimeUtils;

public class JFRameLoggingHandler extends Handler {
	private JTextArea jTextArea;

	private JFrame jframe;

	public JFRameLoggingHandler() {
		Logger rootLogger = Logger.getLogger("");
		rootLogger.addHandler(this);
		jTextArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(jTextArea, 20, 30);
		jTextArea.setEditable(false);
		jTextArea.setFont(new Font("Ariel", 0, 12));
		jTextArea.setLineWrap(false);
		jTextArea.setWrapStyleWord(true);
		jframe = new JFrame("Jdir log");
		jframe.add(scrollPane);
		jframe.setDefaultCloseOperation(0);
		jframe.setState(1);
	}

	public void publish(LogRecord logRecord) {
		String logMessage = DateTimeUtils.getCurrentTime() + " " + logRecord.getLevel() + " "
				+ logRecord.getSourceClassName() + " " + logRecord.getSourceMethodName()
				+ logRecord.getMessage();
		jTextArea.append(logMessage + "\n");
		jframe.setVisible(true);
		jframe.pack();
		jframe.repaint();
	}

	public void flush() {
	}

	public void close() {
	}
}
