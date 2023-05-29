package jdumper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JOptionPane;

import jdumper.ui.JDFrame;

public class JpcapDumper {
	public static Properties JDProperty;
	public static javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
	static Vector frames = new Vector();

	public static void main(String[] args) {
		try {
			Class c = Class.forName("jpcap.JpcapCaptor");
		} catch (ClassNotFoundException e) {
			JOptionPane
					.showMessageDialog(null,
							"Cannot find Jpcap. Please download and install Jpcap before running.");
			System.exit(0);
		}
		JDPacketAnalyzerLoader.loadDefaultAnalyzer();
		JDStatisticsTakerLoader.loadStatisticsTaker();
		loadProperty();
		openNewWindow();
	}

	public static void saveProperty() {
		if (JDProperty == null)
			return;
		try {
			JDProperty.store((OutputStream) new FileOutputStream(
					"JpcapDumper.property"), "JpcapDumper");
			// JDProperty.store(new
			// FileOutputStream("JpcapDumper.property"),"JpcapDumper");
		} catch (IOException e) {
		} catch (ClassCastException e) {
		}
	}

	static void loadProperty() {
		try {
			JDProperty = new Properties();
			JDProperty.load((InputStream) new FileInputStream(
					"JpcapDumper.property"));
		} catch (IOException e) {
		}
	}

	public static void openNewWindow() {
		JDCaptor captor = new JDCaptor();
		frames.add(JDFrame.openNewWindow(captor));
	}

	public static void closeWindow(JDFrame frame) {
		frame.captor.stopCapture();
		frame.captor.saveIfNot();
		frame.captor.closeAllWindows();
		frames.remove(frame);
		frame.dispose();
		if (frames.isEmpty()) {
			saveProperty();
			System.exit(0);
		}
	}

	protected void finalize() throws Throwable {
		saveProperty();
	}
}
